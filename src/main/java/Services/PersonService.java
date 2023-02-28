package Services;

import Requests.PersonRequest;
import Results.PersonResults;

/**
 * PersonService
 * Returns the single Person object with the specified ID (if the person is associated with the current user). The current user is determined by the provided authtoken.
 * OR
 * Returns ALL family members of the current user. The current user is determined by the provided authtoken.
 */
public class PersonService {
  /**
   * request the person with the correlating ID
   * @param request
   * @return the information on that person
   */
  public PersonResults personID(PersonRequest request){
    return null;
  }

  /**
   * gets the users family tree
   * @param request
   * @return Json string of the tree
   */
  public PersonResults personTree(PersonRequest request){
    return null;
  }
}
