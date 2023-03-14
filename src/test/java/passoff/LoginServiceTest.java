package passoff;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.User;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.LoginResult;
import Results.RegisterResult;
import Services.ClearService;
import Services.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
  private User user = null;
  private LoginRequest goodRequest = new LoginRequest("username", "password");
  private LoginRequest badRequest1 = new LoginRequest("user", "password");
  private LoginRequest badRequest2 = new LoginRequest("username", "pass");
  private LoginResult result = null;
  private LoginService service = new LoginService();


  @BeforeEach
  public void setUp() throws DataAccessException {
    ClearService clear = new ClearService();
    clear.clear();
    user = new User("username", "password", "email", "firstname", "lastname", "m", "personID");
    Database db = new Database();
    Connection conn = db.getConnection();
    UserDao uDao = new UserDao(conn);
    uDao.insert(user);
    db.closeConnection(true);
  }

  @Test
  public void loginUser(){
    result = service.login(goodRequest);

    assertTrue(result.isSuccess());
    assertNotNull(result.getAuthtoken());
    assertNotNull(result.getPersonID());
    assertEquals(user.getUsername(), result.getUsername());
  }
  @Test
  public void failLogin(){
    result = service.login(goodRequest);

    assertTrue(result.isSuccess());
    assertNotNull(result.getAuthtoken());
    assertNotNull(result.getPersonID());
    assertEquals(user.getUsername(), result.getUsername());

    result = service.login(badRequest1);

    assertFalse(result.isSuccess());
    assertNotNull(result.getMessage());
    assertTrue(result.getMessage().contains("Error"));

    result = service.login(badRequest2);

    assertFalse(result.isSuccess());
    assertNotNull(result.getMessage());
    assertTrue(result.getMessage().contains("Error"));
  }

}
