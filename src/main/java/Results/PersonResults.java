package Results;

import Model.Person;

/**
 * Person Results
 * Returns the single Person object with the specified ID (if the person is associated with the current user). The current user is determined by the provided authtoken.
 * OR
 * Returns ALL family members of the current user. The current user is determined by the provided authtoken.
 */
public class PersonResults {
  /**
   * Person[]
   * All persons related to the person and their family tree
   */
  /**
   * String personID;
   * Unique identifier for this person
   */
  /**
   * String associatedUsername;
   * Username of user to which this person belongs
   */
  /**
   * String firstName;
   * Person’s first name
   */
  /**
   * String lastName;
   * Person’s last name
   */
  /**
   * String gender;
   * Person’s gender
   */
  /**
   * String fatherID;
   * Person ID of person’s father, may be null
   */
  /**
   * String motherID;
   * Person ID of person’s mother, may be null
   */
  /**
   * String spouseID;
   * Person ID of person’s spouse, may be null
   */
  /**
   * boolean success
   * Whether the request succeeded or failed
   */
  private Person[] familyTree;
  private String personID;
  private String associatedUsername;
  private String firstName;
  private String lastName;
  private String gender;
  private String fatherID;
  private String motherID;
  private String spouseID;
  boolean success;

  public PersonResults(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID, boolean success) {
    this.personID=personID;
    this.associatedUsername=associatedUsername;
    this.firstName=firstName;
    this.lastName=lastName;
    this.gender=gender;
    this.fatherID=fatherID;
    this.motherID=motherID;
    this.spouseID=spouseID;
    this.success=success;
  }

  public Person[] getFamilyTree() {
    return familyTree;
  }

  public void setFamilyTree(Person[] familyTree) {
    this.familyTree=familyTree;
  }

  public String getPersonID() {
    return personID;
  }

  public void setPersonID(String personID) {
    this.personID=personID;
  }

  public String getAssociatedUsername() {
    return associatedUsername;
  }

  public void setAssociatedUsername(String associatedUsername) {
    this.associatedUsername=associatedUsername;
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

  public String getFatherID() {
    return fatherID;
  }

  public void setFatherID(String fatherID) {
    this.fatherID=fatherID;
  }

  public String getMotherID() {
    return motherID;
  }

  public void setMotherID(String motherID) {
    this.motherID=motherID;
  }

  public String getSpouseID() {
    return spouseID;
  }

  public void setSpouseID(String spouseID) {
    this.spouseID=spouseID;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success=success;
  }
}
