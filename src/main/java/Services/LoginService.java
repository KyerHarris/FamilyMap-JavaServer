package Services;

import DataAccess.DataAccessException;
import Requests.LoginRequest;
import Results.LoginResult;
import DataAccess.UserDao;
import DataAccess.Database;
import Model.User;

import java.sql.Connection;

/**
 * LoginService
 * Logs the user in
 * Returns an authtoken.
 */
public class LoginService {
  private Database db;
  /**
   * attempts to log the user in
   * @param request
   * @return the LoginResult
   */
  public LoginResult login(LoginRequest request){
    LoginResult loginResult = new LoginResult();
    db = new Database();
    try {
      Connection conn = db.getConnection();
      UserDao uDao = new UserDao(conn);
      User user = uDao.find(request.getUsername());
      if(user == null){
        loginResult.setError("username not found");
        return loginResult;
      }
      else if(user.getPassword() != request.getPassword()){
        loginResult.setError("password not correct");
        return loginResult;
      }
      //generate authtoken
      //set authtoken in table
      loginResult.setUsername(user.getUsername());
      loginResult.setPersonID(user.getPersonID());
      db.closeConnection(true);
    }
    catch(DataAccessException error){
      loginResult.setError("Login Request Failed");
      db.closeConnection(false);
      return loginResult;
    }
    //check if username and password are in the database.
    //add the personID
    //generate and Authtoken
    loginResult.setSuccess(true);
    return loginResult;
  }
}
