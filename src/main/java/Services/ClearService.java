package Services;

import Requests.ClearRequest;
import Results.ClearResult;

import DataAccess.*;

import java.sql.Connection;

/**
 * ClearService
 * Deletes ALL data from the database, including user, authtoken, person, and event data
 * Returns if was successful
 */
public class ClearService {
  /**
   * clears the database
   * @return ClearResult
   */
  public ClearResult clear(ClearRequest request){
    ClearResult clearResult = new ClearResult();
    Database db = new Database();
    try{
      Connection conn = db.getConnection();
      AuthTokenDao aDao = new AuthTokenDao(conn);
      UserDao uDao = new UserDao(conn);
      PersonDao pDao = new PersonDao(conn);
      EventDao eDao = new EventDao(conn);

      aDao.clear();
      uDao.clear();
      pDao.clear();
      eDao.clear();

      db.closeConnection(true);
    }
    catch (DataAccessException error){
      error.printStackTrace();
      db.closeConnection(false);
      return clearResult;
    }

    return clearResult;
  }
}
