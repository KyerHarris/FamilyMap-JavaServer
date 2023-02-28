package Model;

import java.util.Objects;

/**
 * AuthToken
 * Holds the users Authoken
 */
public class AuthToken {
  /**
   * String authtoken
   * Unique authtoken
   */
  /**
   * String username
   * Unique username for user
   */
  private String authtoken;
  private String username;

  public AuthToken(String authtoken, String username) {
    this.authtoken=authtoken;
    this.username=username;
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
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuthToken authT = (AuthToken) o;
    return Objects.equals(authtoken, authT.authtoken) && Objects.equals(username, authT.username);
  }
}
