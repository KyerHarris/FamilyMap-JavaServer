package Services;

import Requests.ClearRequest;
import Results.ClearResult;

/**
 * ClearService
 * Deletes ALL data from the database, including user, authtoken, person, and event data
 * Returns if was successful
 */
public class ClearService {
  /**
   * clears the database
   * @return ClearResult
   */
  public ClearResult clear(){
    ClearResult clearResult = new ClearResult();
    ClearRequest clearRequest = new ClearRequest();
    clearResult.setSuccess(clearRequest.clear());
    return clearResult;
  }
}
