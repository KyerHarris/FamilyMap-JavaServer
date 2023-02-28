package Services;

import Requests.LoadRequest;
import Results.LoadResult;

/**
 * LoadService
 * Clears all data from the database (just like the /clear API)
 * Loads the user, person, and event data from the request body into the database.
 * Returns if successful and how many users, events, and persons were added
 */
public class LoadService {
  /**
   * clears database
   * loads requested stuff into database
   * @param request
   * @return
   */
  public LoadResult load(LoadRequest request){
    return null;
  }
}
