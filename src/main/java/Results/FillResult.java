package Results;

/**
 * FillResult
 * Returns if database was filled
 */
public class FillResult {
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
