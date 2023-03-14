package passoff;

import DataAccess.*;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Requests.PersonRequest;
import Requests.RegisterRequest;
import Results.PersonResult;
import Services.ClearService;
import Services.PersonService;
import Services.RegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {
  private PersonService service = new PersonService();
  private PersonResult result = null;
  private User user = null;
  private Person person = null;
  private AuthToken myAuthtoken = null;


  @BeforeEach
  public void setUp() throws DataAccessException {
    ClearService clearService = new ClearService();
    clearService.clear();
    user = new User("username", "password", "email", "firstname", "lastname", "m", "personID");
    RegisterService registerService = new RegisterService();
    RegisterRequest registerRequest = new RegisterRequest(user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getGender());
    registerService.register(registerRequest);
    Database db = new Database();
    Connection conn = db.getConnection();
    PersonDao pDao = new PersonDao(conn);
    person = pDao.getFamilyTree(user.getUsername())[16];
    myAuthtoken = new AuthToken("abcd-1234", "username");
    AuthTokenDao aDao = new AuthTokenDao(conn);
    aDao.insert(myAuthtoken);
    db.closeConnection(true);
  }

  @Test
  public void findEventTest() throws  DataAccessException{
    PersonRequest request = new PersonRequest();
    request.setAuthToken(myAuthtoken.getAuthtoken());
    request.setPersonID(person.getPersonID());
    result = service.getPerson(request);

    assertTrue(result.isSuccess());
    assertEquals(person.getPersonID(), result.getPersonID());
    assertEquals(person.getFirstName(), result.getFirstName());
    assertEquals(person.getAssociatedUsername(), result.getAssociatedUsername());
    assertEquals(person.getSpouseID(), result.getSpouseID());
  }

  @Test
  public void eventTreeTest() throws DataAccessException{
    PersonRequest request = new PersonRequest();
    request.setAuthToken(myAuthtoken.getAuthtoken());
    request.setPersonID(person.getPersonID());
    result = service.persons(request);

    assertTrue(result.isSuccess());
    assertNotNull(result.getData());
    assertNull(result.getPersonID());
    assertNull(result.getPersonID());
    assertNull(result.getMessage());
  }

}
