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
  private String authToken;
  private String username;
  private String personID;
  private boolean success;
}
