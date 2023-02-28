package passoff;

import DataAccess.Database;
import DataAccess.UserDao;
import DataAccess.DataAccessException;
import Model.Person;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class UserDaoTest {
  private Database db;
  private User myUser;
  private UserDao uDao;

  @BeforeEach
  public void setUp() throws DataAccessException {
    db = new Database();
    myUser = new User("KyerHarris", "abc123", "kyer@gmail.com", "kyer", "harris", "m", "1234-abcd");
    Connection conn = db.getConnection();
    uDao = new UserDao(conn);
    uDao.clear();
  }

  @AfterEach
  public void tearDown() {
    db.closeConnection(false);
  }

  @Test
  public void insertPass() throws DataAccessException {
    uDao.insert(myUser);
    User compareTest = uDao.find(myUser.getUsername());
    assertNotNull(compareTest);
    assertEquals(myUser, compareTest);
  }

  @Test
  public void insertFail() throws DataAccessException {
    uDao.insert(myUser);
    assertThrows(DataAccessException.class, () -> uDao.insert(myUser));
  }

  @Test
  public void findPass() throws DataAccessException {
    uDao.insert(myUser);
    User compareTest = uDao.find(myUser.getUsername());
    assertNotNull(compareTest);
    assertEquals(myUser, compareTest);
  }

  @Test
  public void findFail() throws DataAccessException {
    uDao.insert(myUser);
    assertNull(uDao.find("gibberish"));
  }

  @Test
  public void clearPass() throws DataAccessException{
    uDao.insert(myUser);
    User compareTest = uDao.find(myUser.getUsername());
    assertNotNull(compareTest);
    assertEquals(myUser, compareTest);
    uDao.clear();
    assertNull(uDao.find(myUser.getPersonID()));
  }
}
