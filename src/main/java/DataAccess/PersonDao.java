package DataAccess;

import Model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * PersonDao creates, edits, and deletes from the Person table
 */
public class PersonDao {
  private final Connection conn;

  public PersonDao(Connection conn) {
    this.conn = conn;
  }

  /**
   * inserts a person into the person table
   * @param person
   * @throws DataAccessException
   */
  public void insert(Person person) throws DataAccessException {
    //We can structure our string to be similar to a sql command, but if we insert question
    //marks we can change them later with help from the statement
    String sql = "INSERT INTO Person (personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      //Using the statements built-in set(type) functions we can pick the question mark we want
      //to fill in and give it a proper value. The first argument corresponds to the first
      //question mark found in our sql String
      stmt.setString(1, person.getPersonID());
      stmt.setString(2, person.getAssociatedUsername());
      stmt.setString(3, person.getFirstName());
      stmt.setString(4, person.getLastName());
      stmt.setString(5, person.getGender());
      stmt.setString(6, person.getFatherID());
      stmt.setString(7, person.getMotherID());
      stmt.setString(8, person.getSpouseID());

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while inserting a person into the database");
    }
  }

  /**
   * searches the person table for the associated personID
   * @param personID
   * @return The found person, or null if the ID doesn't exist
   * @throws DataAccessException
   */
  public Person find(String personID) throws DataAccessException {
    Person person;
    ResultSet rs;
    String sql = "SELECT * FROM Person WHERE personID = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, personID);
      rs = stmt.executeQuery();
      if (rs.next()) {
        person = new Person(rs.getString("personID"), rs.getString("associatedUsername"), rs.getString("firstName"), rs.getString("lastName"),
                rs.getString("gender"), rs.getString("fatherId"), rs.getString("motherID"), rs.getString("spouseID"));
        return person;
      }
      else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding a person in the database");
    }
  }

  public Person[] getFamilyTree(String username) throws DataAccessException{
    Person[] tree = new Person[0];
    ResultSet rs;
    String sql = "SELECT * FROM Person WHERE associatedUsername = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      rs = stmt.executeQuery();
      int i = 0;
      while (rs.next()) {
        Person person = new Person(rs.getString("personID"), rs.getString("associatedUsername"), rs.getString("firstName"), rs.getString("lastName"),
                rs.getString("gender"), rs.getString("fatherId"), rs.getString("motherID"), rs.getString("spouseID"));
        Person[] temp = new Person[i + 1];
        for (int j=0; j < tree.length; j++) {
          temp[j] = tree[j];
        }
        temp[i] = person;
        
        tree = temp;
        i++;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding a person in the database");
    }
    return tree;
  }

  /**
   * clears the person table
   * @throws DataAccessException
   */
  public void clear() throws DataAccessException {
    String sql = "DELETE FROM Person";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while clearing the person table");
    }
  }
}
