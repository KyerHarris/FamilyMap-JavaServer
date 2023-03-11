package Services;

import Requests.LoadRequest;
import Results.LoadResult;
import DataAccess.*;
import Model.*;

import java.sql.Connection;

/**
 * LoadService
 * Clears all data from the database (just like the /clear API)
 * Loads the user, person, and event data from the request body into the database.
 * Returns if successful and how many users, events, and persons were added
 */
public class LoadService {
  /**
   * clears database
   * loads requested stuff into database
   * @param request
   * @return
   */
  public LoadResult load(LoadRequest request){
    LoadResult result = new LoadResult();
    Database db = new Database();


    try {
      Connection conn = db.getConnection();
      EventDao eDao = new EventDao(conn);
      PersonDao pDao = new PersonDao(conn);
      UserDao uDao = new UserDao(conn);

      for (int i=0; i < request.getUsers().length; i++) {
        User user=request.getUsers()[i];
        uDao.insert(user);
      }
      for (int i=0; i < request.getEvents().length; i++) {
        Event event = request.getEvents()[i];
        eDao.insert(event);
      }
      for (int i=0; i < request.getPersons().length; i++) {
        Person person = request.getPersons()[i];
        pDao.insert(person);
      }
      db.closeConnection(true);
    }
    catch(DataAccessException error){
      db.closeConnection(false);
      error.printStackTrace();
      result.setMessage("Error: failed to load data");
      result.setSuccess(false);
      return result;
    }

    result.setMessage("Successfully added " + request.getUsers().length + " users, " + request.getPersons().length +
            " persons, and " + request.getEvents().length + " events to the database.");
    result.setSuccess(true);
    return result;
  }
}
