package passoff;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDao;
import Model.AuthToken;
import Model.Event;
import Model.User;
import Requests.EventRequest;
import Requests.RegisterRequest;
import Results.EventResult;
import Services.ClearService;
import Services.EventService;
import Services.RegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
  private EventService service = null;
  private EventResult result = null;
  private User user = null;
  private Event event = null;
  private AuthToken myAuthtoken = null;


  @BeforeEach
  public void setUp() throws DataAccessException {
    ClearService clearService = new ClearService();
    clearService.clear();
    user = new User("username", "password", "email", "firstname", "lastname", "m", "personID");
    RegisterService registerService = new RegisterService();
    RegisterRequest registerRequest = new RegisterRequest(user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getGender());
    registerService.register(registerRequest);
    service = new EventService();
    Database db = new Database();
    Connection conn = db.getConnection();
    EventDao eDao = new EventDao(conn);
    event = eDao.getFamilyEvents(user.getUsername())[0];
    myAuthtoken = new AuthToken("abcd-1234", "username");
    AuthTokenDao aDao = new AuthTokenDao(conn);
    aDao.insert(myAuthtoken);
    db.closeConnection(true);
  }

  @Test
  public void findEventTest() throws  DataAccessException{
    EventRequest request = new EventRequest();
    request.setAuthToken(myAuthtoken.getAuthtoken());
    request.setEventID(event.getEventID());
    result = service.findEvent(request);

    assertTrue(result.isSuccess());
    assertEquals(event.getEventID(), result.getEventID());
    assertEquals(event.getAssociatedUsername(), result.getAssociatedUsername());
    assertEquals(event.getEventType(), result.getEventType());
    assertEquals(event.getPersonID(), result.getPersonID());
  }

  @Test
  public void eventTreeTest() throws DataAccessException{
    EventRequest request = new EventRequest();
    request.setAuthToken(myAuthtoken.getAuthtoken());
    request.setEventID(event.getEventID());
    result = service.eventTree(request);

    assertTrue(result.isSuccess());
    assertNotNull(result.getData());
    assertNull(result.getEventID());
    assertNull(result.getPersonID());
  }

}
