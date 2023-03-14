package Services;

import Results.RegisterResult;
import Requests.RegisterRequest;
import Requests.LoginRequest;
import Results.LoginResult;
import Requests.FillRequest;
import Results.FillResult;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.UserDao;
import DataAccess.Database;
import Model.User;


import java.sql.Connection;
import java.util.UUID;


/**
 * RegisterService
 * Creates a new user account (user row in the database)
 * Generates 4 generations of ancestor data for the new user (just like the /fill endpoint if called with a generations value of 4 and this new user’s username as parameters)
 * Logs the user in
 * Returns an authtoken
 */
public class RegisterService{
  /**
   * calls the register result createUser function, generate ancestor function
   * as well logs in the new user in the loginRegister
   * @param request
   * @return returns the RegisterResult
   */
  public RegisterResult register(RegisterRequest request){
    RegisterResult result = new RegisterResult();
    Database db = new Database();
    try{
      Connection conn = db.getConnection();
      AuthTokenDao aDao = new AuthTokenDao(conn);
      UserDao uDao = new UserDao(conn);
      UUID uuid = UUID.randomUUID();

      if (uDao.find(request.getUsername()) == null) {
        //putting the user into the database
        User user=new User(request.getUsername(), request.getPassword(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getGender(), uuid.toString());
        uDao.insert(user);
      }
      else{
        result.setMessage("Error: Username already taken");
        result.setSuccess(false);
        return result;
      }

      //logging in the user and getting back the authToken
      LoginRequest loginRequest = new LoginRequest(request.getUsername(), request.getPassword());
      LoginService loginService = new LoginService();
      LoginResult loginResult = loginService.login(conn, loginRequest);
      result.setSuccess(loginResult.isSuccess());
      if(result.isSuccess()){
        result.setAuthtoken(loginResult.getAuthtoken());
      }
      else{
        result.setMessage(loginResult.getMessage());
      }
      //setting personID & username
      result.setPersonID(uuid.toString());
      result.setUsername(request.getUsername());
      //filling tree
      FillRequest fillRequest = new FillRequest(request.getUsername());
      FillService fillService = new FillService();
      fillRequest.setGenerations(4);
      FillResult fillResult = fillService.fill(fillRequest, conn);
      result.setSuccess(fillResult.isSuccess());
      if(!result.isSuccess()){
        result.setMessage(fillResult.getMessage());
      }
    }
    catch(DataAccessException error){
      result.setMessage("Error: Register Request Failed");
      return result;
    }
    finally{
      if(result.isSuccess()){
        db.closeConnection(true);
      }
      else{
        db.closeConnection(false);
      }
    }

    return result;
  }

}
