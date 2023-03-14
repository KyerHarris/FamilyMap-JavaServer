package Services;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
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
  public EventResult findEvent(EventRequest request){
    EventResult result = new EventResult();
    Database db = new Database();
    try{
      Connection conn = db.getConnection();
      EventDao eDao = new EventDao(conn);
      AuthTokenDao aDao = new AuthTokenDao(conn);
      if(aDao.find(request.getAuthToken()) != null) {
        Event event=eDao.find(request.getEventID());
        if (event.getAssociatedUsername().equals(aDao.find(request.getAuthToken()).getUsername())) {
          result.setInfo(event);
        } else {
          db.closeConnection(false);
          result.setMessage("Error: user cannot access this event");
          result.setSuccess(false);
          return result;
        }
      }
      else{
        result.setMessage("Error: authtoken not found");
        db.closeConnection(false);
        return result;
      }
    }
    catch (DataAccessException e){
      db.closeConnection(false);
      e.printStackTrace();
      result.setSuccess(false);
      result.setMessage("Error: failed to access Event Database");
      return result;
    }

    db.closeConnection(false);
    result.setSuccess(true);
    return result;
  }

  /**
   * get all events for the users family tree
   * @param request
   * @return all events in a Json string
   */
  public EventResult eventTree(EventRequest request){
    EventResult result = new EventResult();
    Database db = new Database();
    try{
      Connection conn = db.getConnection();
      EventDao eDao = new EventDao(conn);
      AuthTokenDao aDao = new AuthTokenDao(conn);
      if(aDao.find(request.getAuthToken()) != null) {
        String username=aDao.find(request.getAuthToken()).getUsername();
        result.setData(eDao.getFamilyEvents(username));
      }
      else{
        result.setMessage("Error: authtoken not found");
        db.closeConnection(false);
        return result;
      }
    }
    catch(DataAccessException e){
      e.printStackTrace();
      result.setSuccess(false);
      result.setMessage("Error: failed to access Database");
    }

    db.closeConnection(false);
    result.setSuccess(true);
    return result;
  }
}
