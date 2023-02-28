package Requests;

import Results.LoginResult;

/**
 * LoginRequest
 * Logs the user in
 */
public class LoginRequest {
  /**
   * String username
   * String password
   */
  private String username;
  private String password;

  /**
   * verifies the information provided
   * @return the LoginResult
   */
  public LoginResult verifyUser(){
    return null;
  }

  public LoginRequest(String username, String password) {
    this.username=username;
    this.password=password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username=username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password=password;
  }
}
