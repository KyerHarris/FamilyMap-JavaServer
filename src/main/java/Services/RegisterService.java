package Services;

import Results.RegisterResult;
import Requests.RegisterRequest;

/**
 * RegisterService
 * Creates a new user account (user row in the database)
 * Generates 4 generations of ancestor data for the new user (just like the /fill endpoint if called with a generations value of 4 and this new user’s username as parameters)
 * Logs the user in
 * Returns an authtoken
 */
public class RegisterService {
  /**
   * calls the register result createUser function, generate ancestor function
   * as well logs in the new user in the loginRegister
   * @param request
   * @return returns the RegisterResult
   */
  public RegisterResult register(RegisterRequest request){
    LoginService loginService = new LoginService();

    return null;
  }

}
