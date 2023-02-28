package DataAccess;

import Model.AuthToken;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AuthTokenDao creates, edits, and deletes from the Authtoken table
 */
public class AuthTokenDao {
  private final Connection conn;

  public AuthTokenDao(Connection conn) {
    this.conn = conn;
  }

  /**
   * inserts an authtoken into the Authtoken table
   * @param authToken
   * @throws DataAccessException
   */
  public void insert(AuthToken authToken) throws DataAccessException {
    String sql = "INSERT INTO Authtoken (authtoken, username) VALUES(?,?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, authToken.getAuthtoken());
      stmt.setString(2, authToken.getUsername());

      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while inserting an event into the database");
    }
  }

  /**
   * searches the Authtoken table for the associated username
   * @param authToken
   * @return The found username, or null if the authtoken doesn't exist
   * @throws DataAccessException
   */
  public AuthToken find(String authToken) throws DataAccessException {
    AuthToken authToken1;
    ResultSet rs;
    String sql = "SELECT * FROM Authtoken WHERE authToken = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, authToken);
      rs = stmt.executeQuery();
      if (rs.next()) {
        authToken1 = new AuthToken(authToken, rs.getString("username"));
        return authToken1;
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while finding an event in the database");
    }

  }

  /**
   * clears the Authtoken table
   * @throws DataAccessException
   */
  public void clear() throws DataAccessException {
    String sql = "DELETE FROM Authtoken";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("Error encountered while clearing the event table");
    }
  }
}
