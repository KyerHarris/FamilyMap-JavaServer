package Requests;

/**
 * PersonRequest
 * Finds the person associated with the personID
 * OR
 * Gets all family members of the current user
 */
public class PersonRequest {
  /**
   * string personID
   */
  private String personID;
  private String authToken;

  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken=authToken;
  }

  public String getPersonID() {
    return personID;
  }

  public void setPersonID(String personID) {
    this.personID=personID;
  }
}
