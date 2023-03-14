package passoff;

import DataAccess.Database;
import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class AuthtokenDaoTest {
  private Database db;
  private AuthToken myAuthtoken;
  private AuthTokenDao aDao;

  @BeforeEach
  public void setUp() throws DataAccessException {
    db = new Database();
    myAuthtoken = new AuthToken("abcd-1234", "KyerHarris");
    Connection conn = db.getConnection();
    aDao = new AuthTokenDao(conn);
    aDao.clear();
  }

  @AfterEach
  public void tearDown() {
    db.closeConnection(false);
  }

  @Test
  public void insertPass() throws DataAccessException {
    aDao.insert(myAuthtoken);
    AuthToken compareTest = aDao.find(myAuthtoken.getAuthtoken());
    assertNotNull(compareTest);
    assertEquals(myAuthtoken, compareTest);
  }

  @Test
  public void insertFail() throws DataAccessException {
    aDao.insert(myAuthtoken);
    assertThrows(DataAccessException.class, () -> aDao.insert(myAuthtoken));
  }

  @Test
  public void findPass() throws DataAccessException {
    aDao.insert(myAuthtoken);
    AuthToken compareTest = aDao.find(myAuthtoken.getAuthtoken());
    assertNotNull(compareTest);
    assertEquals(myAuthtoken, compareTest);
  }

  @Test
  public void findFail() throws DataAccessException {
    aDao.insert(myAuthtoken);
    assertNull(aDao.find("gibberish"));
  }

  @Test
  public void clearPass() throws DataAccessException{
    aDao.insert(myAuthtoken);
    AuthToken compareTest = aDao.find(myAuthtoken.getAuthtoken());
    assertNotNull(compareTest);
    assertEquals(myAuthtoken, compareTest);
    aDao.clear();
    assertNull(aDao.find(myAuthtoken.getAuthtoken()));
  }
}
