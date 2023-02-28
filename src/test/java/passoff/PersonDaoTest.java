package passoff;

import DataAccess.Database;
import DataAccess.PersonDao;
import DataAccess.DataAccessException;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonDaoTest {
  private Database db;
  private Person myPerson;
  private PersonDao pDao;

  @BeforeEach
  public void setUp() throws DataAccessException {
    db = new Database();
    myPerson= new Person("abcd-1234", "KyerHarris", "Kyer", "Harris", "m", "byron", "trisha", "spouse");
    Connection conn = db.getConnection();
    pDao= new PersonDao(conn);
    pDao.clear();
  }

  @AfterEach
  public void tearDown() {
    db.closeConnection(false);
  }

  @Test
  public void insertPass() throws DataAccessException {
    pDao.insert(myPerson);
    Person compareTest = pDao.find(myPerson.getPersonID());
    assertNotNull(compareTest);
    assertEquals(myPerson, compareTest);
  }

  @Test
  public void insertFail() throws DataAccessException {
    pDao.insert(myPerson);
    assertThrows(DataAccessException.class, () -> pDao.insert(myPerson));
  }

  @Test
  public void findPass() throws DataAccessException {
    pDao.insert(myPerson);
    Person compareTest = pDao.find(myPerson.getPersonID());
    assertNotNull(compareTest);
    assertEquals(myPerson, compareTest);
  }

  @Test
  public void findFail() throws DataAccessException {
    pDao.insert(myPerson);
    assertNull(pDao.find("gibberish"));
  }

  @Test
  public void clearPass() throws DataAccessException{
    pDao.insert(myPerson);
    Person compareTest = pDao.find(myPerson.getPersonID());
    assertNotNull(compareTest);
    assertEquals(myPerson, compareTest);
    pDao.clear();
    assertNull(pDao.find(myPerson.getPersonID()));
  }
}
