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
  private String error;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success=success;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error=error;
  }
}
