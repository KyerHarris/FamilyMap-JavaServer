package DataAccess;

import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDao creates, edits, and deletes from the User table
 */
public class UserDao {
  private final Connection conn;

  public UserDao(Connection conn) {
    this.conn = conn;
  }

  /**
   * inserts a user into the user table
   * @param user
   * @throws DataAccessException
   */
  public void insert(User user) throws DataAccessException {
    String sql = "INSERT INTO User (username, password, email, firstName, lastName, gender, personID) VALUES(?,?,?,?,?,?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getPassword());
      stmt.setString(3, user.getEmail());
      stmt.setString(4, user.getFirstName());
      stmt.setString(5, user.getLastName());
      stmt.setString(6, user.getGender());
      stmt.setString(7, user.getPersonID());

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while inserting an event into the database");
    }
  }

  /**
   * searches the user table for the associated username
   * @param username
   * @return The found user, or null if the username doesn't exist
   * @throws DataAccessException
   */
  public User find(String username) throws DataAccessException {
    User user;
    ResultSet rs;
    String sql = "SELECT * FROM User WHERE username = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, username);
      rs = stmt.executeQuery();
      if (rs.next()) {
        user = new User(rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"),
                rs.getString("gender"), rs.getString("personId"));
        return user;
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding an event in the database");
    }

  }

  /**
   * clears the user table
   * @throws DataAccessException
   */
  public void clear() throws DataAccessException {
    String sql = "DELETE FROM User";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while clearing the event table");
    }
  }
}
