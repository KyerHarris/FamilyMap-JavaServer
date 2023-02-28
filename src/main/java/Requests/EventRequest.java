package Requests;

/**
 * EventRequest
 * Finds the event associated with the eventID
 * OR
 * Gets all events for all family members of the current user
 */
public class EventRequest {
  /**
   * eventId
   */
  String eventID;

  public String getEventID() {
    return eventID;
  }

  public void setEventID(String eventID) {
    this.eventID=eventID;
  }
}
