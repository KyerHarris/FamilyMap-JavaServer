package passoff;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Results.ClearResult;
import Services.ClearService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
  private Database db = null;
  private AuthTokenDao aDao = null;
  private AuthToken myAuthtoken = null;
  private ClearService service = null;
  private Connection conn = null;
  private UserDao uDao = null;
  private PersonDao pDao = null;
  private EventDao eDao = null;
  private Event event = null;
  private Person person = null;
  private User user = null;

  @BeforeEach
  public void setUp() throws DataAccessException {
    db = new Database();
    conn = db.getConnection();
    service = new ClearService();
    service.clear();
    uDao = new UserDao(conn);
    pDao = new PersonDao(conn);
    eDao = new EventDao(conn);
    aDao = new AuthTokenDao(conn);

    myAuthtoken = new AuthToken("abcd-1234", "KyerHarris");
    event = new Event("eventID", "username", "personID", (float)10.0, (float)10.0, "country", "city", "event", 2000);
    person = new Person("personID", "username", "firstname", "lastname", "m", "fatherId", "motherId", "spouseID");
    user = new User("username", "password", "email", "firstname", "lastname", "m", "personID");

    uDao.insert(user);
    pDao.insert(person);
    eDao.insert(event);
    aDao.insert(myAuthtoken);
  }

  @Test
  public void clearSingleTable() throws DataAccessException{
    assertNotNull(aDao.find(myAuthtoken.getAuthtoken()));
    db.closeConnection(false);
    ClearResult result = service.clear();

    assertTrue(result.isSuccess());
    assertNotNull(result.getMessage());
  }

  @Test
  public void clearAllTables() throws DataAccessException{
    assertNotNull(uDao.find(user.getUsername()));
    assertNotNull(pDao.find(person.getPersonID()));
    assertNotNull(eDao.find(event.getEventID()));
    assertNotNull(aDao.find(myAuthtoken.getAuthtoken()));
    db.closeConnection(false);

    ClearResult result = service.clear();

    assertTrue(result.isSuccess());
  }
}
