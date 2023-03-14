package passoff;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.LoadRequest;
import Results.LoadResult;
import Services.ClearService;
import Services.LoadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {
  private LoadResult result = null;
  private LoadService service = new LoadService();
  private LoadRequest request = new LoadRequest();
  private AuthToken myAuthtoken = null;
  private Event events[] = new Event[1];
  private Person persons[] = new Person[1];
  private User users[] = new User[1];
  private Person person = null;
  private Event event = null;
  private User user = null;

  @BeforeEach
  public void setUp() throws DataAccessException {
    myAuthtoken = new AuthToken("abcd-1234", "KyerHarris");
    event = new Event("eventID", "username", "personID", (float)10.0, (float)10.0, "country", "city", "event", 2000);
    person = new Person("personID", "username", "firstname", "lastname", "m", "fatherId", "motherId", "spouseID");
    user = new User("username", "password", "email", "firstname", "lastname", "m", "personID");
    events[0] = event;
    persons[0] = person;
    users[0] = user;
    request.setEvents(events);
    request.setPersons(persons);
    request.setUsers(users);

    ClearService clear = new ClearService();
    clear.clear();
  }

  @Test
  public void basicLoad() throws DataAccessException{
    result = service.load(request);

    Database db = new Database();
    Connection conn = db.getConnection();

    UserDao uDao = new UserDao(conn);
    PersonDao pDao = new PersonDao(conn);
    EventDao eDao = new EventDao(conn);

    assertNotNull(uDao.find(users[0].getUsername()));
    assertNotNull(pDao.find(persons[0].getPersonID()));
    assertNotNull(eDao.find(events[0].getEventID()));
    assertNotNull(result.getMessage());
    assertTrue(result.isSuccess());
    db.closeConnection(false);
  }

  @Test
  public void loadClears() throws DataAccessException{
    result = service.load(request);

    Database db = new Database();
    Connection conn = db.getConnection();

    UserDao uDao = new UserDao(conn);
    PersonDao pDao = new PersonDao(conn);
    EventDao eDao = new EventDao(conn);

    assertNotNull(uDao.find(users[0].getUsername()));
    assertNotNull(pDao.find(persons[0].getPersonID()));
    assertNotNull(eDao.find(events[0].getEventID()));
    assertNotNull(result.getMessage());
    assertTrue(result.isSuccess());
    db.closeConnection(false);

    Event event2 = new Event("ID", "user", "person", (float)100.0, (float)100.0, "try", "ty", "ent", 200);
    Person person2 = new Person("ID", "user", "first", "last", "f", "father", "mother", "spouse");
    User user2 = new User("user", "pass", "email", "first", "last", "f", "person");

    events[0] = event2;
    persons[0] = person2;
    users[0] = user2;

    result = service.load(request);

    db = new Database();
    conn = db.getConnection();

    uDao = new UserDao(conn);
    pDao = new PersonDao(conn);
    eDao = new EventDao(conn);

    assertNotNull(uDao.find(users[0].getUsername()));
    assertNotNull(pDao.find(persons[0].getPersonID()));
    assertNotNull(eDao.find(events[0].getEventID()));
    assertNotNull(result.getMessage());
    assertTrue(result.isSuccess());

    assertNotEquals(person.getPersonID(), pDao.find(person2.getPersonID()).getPersonID());
    assertNotEquals(event.getEventID(), eDao.find(event2.getEventID()).getEventID());
    assertNotEquals(user.getUsername(), uDao.find(user2.getUsername()).getUsername());
    db.closeConnection(false);
  }
}
