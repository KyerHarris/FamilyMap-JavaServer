package Services;

import DataAccess.DataAccessException;
import Requests.PersonRequest;
import Results.PersonResult;
import DataAccess.PersonDao;
import DataAccess.AuthTokenDao;
import Model.Person;
import Model.AuthToken;

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
  public PersonResult findPerson(PersonRequest request, Connection conn){
    PersonResult result = new PersonResult();
    PersonDao pDao = new PersonDao(conn);
    AuthTokenDao aDao = new AuthTokenDao(conn);

    try {
      Person person = pDao.find(request.getPersonID());
      if(person.getAssociatedUsername().equals(aDao.find(request.getAuthToken()).getUsername())){
        result.setInfo(person);
      }
      else{
        result.setMessage("Error: user cannot access this person");
        result.setSuccess(false);
        return result;
      }
    }
    catch (DataAccessException e){
      e.printStackTrace();
      result.setSuccess(false);
      result.setMessage("Error: failed to access Person Database");
      return result;
    }

    result.setSuccess(true);
    return result;
  }

  /**
   * gets the users family tree
   * @param request
   * @return Json string of the tree
   */
  public PersonResult personTree(PersonRequest request, Connection conn){
    PersonResult result = new PersonResult();
    PersonDao pDao = new PersonDao(conn);
    AuthTokenDao aDao = new AuthTokenDao(conn);

    try{
      String username = aDao.find(request.getAuthToken()).getUsername();
      result.setFamilyTree(pDao.getFamilyTree(username));
    }
    catch(DataAccessException e){
      e.printStackTrace();
      result.setSuccess(false);
      result.setMessage("Error: failed to access Database");
    }

    result.setSuccess(true);
    return result;
  }
}
