package Results;

/**
 * LoginResult
 * Returns an authtoken as well as the username and personID of who was logged in, and if the register succeeded
 */
public class LoginResult {
  /**
   * String authtoken
   * Unique authtoken
   */
  /**
   * string username
   * Unique username for user
   */
  /**
   * String personID
   * Unique identifier for this person
   */
  /**
   * bool success
   * Whether the request succeeded or failed
   */
  private String authtoken;
  private String username;
  private String personID;
  private boolean success = false;
  private String message;

  public String getMessage(){
    return message;
  }

  public void setMessage(String message){
    this.message= message;
  }

  public String getAuthtoken() {
    return authtoken;
  }

  public void setAuthtoken(String authtoken) {
    this.authtoken=authtoken;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username=username;
  }

  public String getPersonID() {
    return personID;
  }

  public void setPersonID(String personID) {
    this.personID=personID;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success=success;
  }
}
