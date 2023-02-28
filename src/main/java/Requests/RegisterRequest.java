package Requests;

import Results.RegisterResult;
import Model.User;

/**
 * RegisterRequest
 * Creates a new user account (user row in the database)
 * Generates 4 generations of ancestor data for the new user (just like the /fill endpoint if called with a generations value of 4 and this new user’s username as parameters)
 * Logs the user in
 */
public class RegisterRequest {
  /**
   * String username;
   * String password;
   * String email;
   * String firstName;
   * String lastName;
   * String gender;
   */
  private String username;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private String gender;

  /**
   * creates the User in the database as well as the user model
   * @return the user created
   */
  public User createUser(){
    return null;
  }

  /**
   * generates ancestors for the user
   * @param user
   * @return if generated successfully
   */
  public boolean generateAncestors(User user){
    return false;
  }


  public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
    this.username=username;
    this.password=password;
    this.email=email;
    this.firstName=firstName;
    this.lastName=lastName;
    this.gender=gender;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email=email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName=firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName=lastName;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender=gender;
  }
}
