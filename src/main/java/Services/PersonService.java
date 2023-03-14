package Services;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Requests.PersonRequest;
import Results.PersonResult;
import DataAccess.PersonDao;
import DataAccess.AuthTokenDao;
import Model.Person;

import java.sql.Connection;

/**
 * PersonService
 * Returns the single Person object with the specified ID (if the person is associated with the current user). The current user is determined by the provided authtoken.
 * OR
 * Returns ALL family members of the current user. The current user is determined by the provided authtoken.
 */
public class PersonService {
  /**
   * request the person with the correlating ID
   * @param request
   * @return the information on that person
   */
  public PersonResult getPerson(PersonRequest request){
    PersonResult result = new PersonResult();
    Database db = new Database();
    try{
      Connection conn = db.getConnection();
      PersonDao pDao = new PersonDao(conn);
      AuthTokenDao aDao = new AuthTokenDao(conn);
      if(aDao.find(request.getAuthToken()) != null) {
        Person person=pDao.find(request.getPersonID());
        if (person.getAssociatedUsername().equals(aDao.find(request.getAuthToken()).getUsername())) {
          result.setInfo(person);
        } else {
          db.closeConnection(false);
          result.setMessage("Error: user cannot access this person");
          result.setSuccess(false);
          return result;
        }
      }
      else{
        result.setMessage("Error: authtoken not found");
        db.closeConnection(false);
        return result;
      }
    }
    catch (DataAccessException e){
      e.printStackTrace();
      db.closeConnection(false);
      result.setSuccess(false);
      result.setMessage("Error: failed to access Person Database");
      return result;
    }

    db.closeConnection(true);
    result.setSuccess(true);
    return result;
  }

  /**
   * gets the users family tree
   * @param request
   * @return Json string of the tree
   */
  public PersonResult persons(PersonRequest request){
    PersonResult result = new PersonResult();
    Database db = new Database();
    try{
      Connection conn = db.getConnection();
      PersonDao pDao = new PersonDao(conn);
      AuthTokenDao aDao = new AuthTokenDao(conn);
      if(aDao.find(request.getAuthToken()) != null) {
        String username = aDao.find(request.getAuthToken()).getUsername();
        result.setData(pDao.getFamilyTree(username));
      }
      else{
        result.setMessage("Error: authtoken not found");
        db.closeConnection(false);
        return result;
      }
    }
    catch(DataAccessException e){
      db.closeConnection(false);
      e.printStackTrace();
      result.setSuccess(false);
      result.setMessage("Error: failed to access Database");
    }

    db.closeConnection(false);
    result.setSuccess(true);
    return result;
  }
}
