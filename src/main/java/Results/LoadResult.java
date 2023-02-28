package Results;

/**
 * LoadResult
 * Returns if database was cleared and loaded
 * Clears all data from the database (just like the /clear API)
 * Loads the user, person, and event data from the request body into the database.
 */
public class LoadResult {
  /**
   * boolean success
   * Whether the request succeeded or failed
   */
  private boolean success;
}
