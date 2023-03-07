package Services;

import Requests.LoginRequest;
import Results.LoginResult;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.UserDao;
import DataAccess.Database;
import Model.User;
import Model.AuthToken;

import java.sql.Connection;
import java.util.UUID;

/**
 * LoginService
 * Logs the user in
 * Returns an authtoken.
 */
public class LoginService {
  /**
   * attempts to log the user in
   * @param request
   * @return the LoginResult
   */
  public LoginResult login(LoginRequest request){
    LoginResult loginResult = new LoginResult();
    Database db = new Database();
    try {
      Connection conn = db.getConnection();
      loginResult = login(conn, request);
      if(loginResult.isSuccess()) {
        db.closeConnection(true);
      }
      else{
        db.closeConnection(false);
      }
    }
    catch(DataAccessException error){
      loginResult.setError("Login Request Failed");
      db.closeConnection(false);
      return loginResult;
    }

    return loginResult;
  }

  public LoginResult login(Connection conn, LoginRequest request){
    LoginResult loginResult = new LoginResult();
    try {
      AuthTokenDao aDao = new AuthTokenDao(conn);
      UserDao uDao = new UserDao(conn);
      User user = uDao.find(request.getUsername());
      if(user == null){
        loginResult.setError("username not found");
        return loginResult;
      }
      else if(!user.getPassword().equals(request.getPassword())){
        loginResult.setError("password not correct");
        return loginResult;
      }
      //Creating and inserting Authtoken
      UUID uuid = UUID.randomUUID();
      AuthToken authToken = new AuthToken(uuid.toString(), user.getUsername());
      aDao.insert(authToken);

      //updating information in loginResult
      loginResult.setAuthToken(uuid.toString());
      loginResult.setUsername(user.getUsername());
      loginResult.setPersonID(user.getPersonID());
    }
    catch(DataAccessException error){
      loginResult.setError("Login Request Failed");
      return loginResult;
    }

    loginResult.setSuccess(true);
    return loginResult;
  }
}
