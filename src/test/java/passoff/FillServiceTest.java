package passoff;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.User;
import Requests.FillRequest;
import Results.FillResult;
import Services.ClearService;
import Services.FillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
  private User user = null;
  private FillRequest request = new FillRequest("username");
  private FillResult result = null;
  private FillService service = new FillService();


  @BeforeEach
  public void setUp() throws DataAccessException {
    ClearService clear = new ClearService();
    clear.clear();
    request.setGenerations(4);
    Database db = new Database();
    Connection conn = db.getConnection();
    user = new User("username", "password", "email", "firstname", "lastname", "m", "personID");
    UserDao uDao = new UserDao(conn);
    uDao.insert(user);
    db.closeConnection(true);
  }

  @Test
  public void firstFill(){
    result = service.fill(request);

    assertTrue(result.isSuccess());
    assertNotNull(result.getMessage());
    assertTrue(result.getMessage().contains("31"));
    assertFalse(result.getMessage().contains("Error"));
  }
  @Test
  public void FillTwice(){
    result = service.fill(request);

    assertTrue(result.isSuccess());
    assertNotNull(result.getMessage());
    assertTrue(result.getMessage().contains("31"));
    assertFalse(result.getMessage().contains("Error"));

    request.setGenerations(5);
    result = service.fill(request);

    assertTrue(result.isSuccess());
    assertNotNull(result.getMessage());
    assertTrue(result.getMessage().contains("63"));
    assertFalse(result.getMessage().contains("Error"));
  }

}
