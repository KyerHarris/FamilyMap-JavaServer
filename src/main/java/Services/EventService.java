package Services;

import Requests.EventRequest;
import Results.EventResult;

/**
 * EventService
 * Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
 * OR
 * Returns the single Event object with the specified ID (if the event is associated with the current user). The current user is determined by the provided authtoken.
 */
public class EventService {
  /**
   * find the event associated with the ID
   * @param request
   * @return the event information
   */
  public EventResult eventID(EventRequest request){
    return null;
  }

  /**
   * get all events for the users family tree
   * @param request
   * @return all events in a Json string
   */
  public EventResult eventTree(EventRequest request){
    return null;
  }
}
