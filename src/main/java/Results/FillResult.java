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
  private String message;
  private boolean success = false;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message=message;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success=success;
  }
}
