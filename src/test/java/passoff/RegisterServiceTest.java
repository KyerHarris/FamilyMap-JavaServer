package passoff;

import DataAccess.DataAccessException;
import Model.User;
import Requests.RegisterRequest;
import Results.RegisterResult;
import Services.ClearService;
import Services.RegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {
  private User user = null;
  private RegisterRequest request = new RegisterRequest("username", "password", "email", "firstname", "lastname", "m");
  private RegisterResult result = null;
  private RegisterService service = new RegisterService();


  @BeforeEach
  public void setUp() throws DataAccessException {
    ClearService clear = new ClearService();
    clear.clear();
    user = new User("username", "password", "email", "firstname", "lastname", "m", "personID");
  }

  @Test
  public void registerUser(){
    result = service.register(request);

    assertTrue(result.isSuccess());
    assertNotNull(result.getAuthtoken());
    assertNotNull(result.getPersonID());
    assertEquals(user.getUsername(), result.getUsername());
  }
  @Test
  public void RegisterTwice(){
    result = service.register(request);

    assertTrue(result.isSuccess());
    assertNotNull(result.getAuthtoken());
    assertNotNull(result.getPersonID());
    assertEquals(user.getUsername(), result.getUsername());

    result = service.register(request);

    assertFalse(result.isSuccess());
    assertNotNull(result.getMessage());
    assertTrue(result.getMessage().contains("Error: Username already taken"));
  }

}
