package Services;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.EventDao;
import Model.Event;
import Requests.EventRequest;
import Results.EventResult;

import java.sql.Connection;

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
  public EventResult findEvent(EventRequest request, Connection conn){
    EventResult result = new EventResult();
    EventDao eDao = new EventDao(conn);
    AuthTokenDao aDao = new AuthTokenDao(conn);

    try {
      Event event = eDao.find(request.getEventID());
      if(event.getAssociatedUsername().equals(aDao.find(request.getAuthToken()).getUsername())){
        result.setInfo(event);
      }
      else{
        result.setMessage("Error: user cannot access this event");
        result.setSuccess(false);
        return result;
      }
    }
    catch (DataAccessException e){
      e.printStackTrace();
      result.setSuccess(false);
      result.setMessage("Error: failed to access Event Database");
      return result;
    }

    result.setSuccess(true);
    return result;
  }

  /**
   * get all events for the users family tree
   * @param request
   * @return all events in a Json string
   */
  public EventResult eventTree(EventRequest request, Connection conn){
    EventResult result = new EventResult();
    EventDao eDao = new EventDao(conn);
    AuthTokenDao aDao = new AuthTokenDao(conn);

    try{
      String username = aDao.find(request.getAuthToken()).getUsername();
      result.setEvents(eDao.getFamilyEvents(username));
    }
    catch(DataAccessException e){
      e.printStackTrace();
      result.setSuccess(false);
      result.setMessage("Error: failed to access Database");
    }

    result.setSuccess(true);
    return result;
  }
}
