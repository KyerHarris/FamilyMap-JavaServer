package Requests;

import Model.*;

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
  private User[] users;
  private Person[] persons;
  private Event[] events;

  public User[] getUsers() {
    return users;
  }

  public void setUsers(User[] users) {
    this.users=users;
  }

  public Person[] getPersons() {
    return persons;
  }

  public void setPersons(Person[] persons) {
    this.persons=persons;
  }

  public Event[] getEvents() {
    return events;
  }

  public void setEvents(Event[] events) {
    this.events=events;
  }
}
