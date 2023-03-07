package Results;

/**
 * ClearResult
 * Returns if database was cleared
 */
public class ClearResult {
  /**
   * bool success
   * Whether the request succeeded or failed
   */
  private boolean success = false;
  private String error;

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error=error;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success=success;
  }
}
