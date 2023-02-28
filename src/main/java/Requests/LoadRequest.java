package Requests;

/**
 * LoadRequest
 * Clears all data from the database (just like the /clear API)
 * Loads the user, person, and event data from the request body into the database.
 */
public class LoadRequest {
  /**
   * string users
   * string persons
   * string events
   */
  private String users;
  private String persons;
  private String events;
}
