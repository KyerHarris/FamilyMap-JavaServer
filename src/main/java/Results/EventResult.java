package Results;

import Model.Event;

/**
 * EventResult
 * Returns the single Event object with the specified ID (if the event is associated with the current user). The current user is determined by the provided authtoken.
 * OR
 * Returns ALL events for ALL family members of the current user. The current user is determined from the provided auth token.
 */
public class EventResult {
  /**
   *   Event[] events
   *   All events related to the person and its family tree
   */
   /**
    * String eventID;
    * Unique identifier for this event
    */
   /**
    * String associatedUsername;
    * associatedUsername
    */
   /**
    * String personID;
    * personID
    */
   /**
    * Float latitude;
    * Latitude of event’s location
    */
   /**
    * Float longitude;
    * Longitude of event’s location
    */
   /**
    * String country;
    * Country in which event occurred
    */
   /**
    * String city;
    * City in which event occurred
    */
   /**
    * String eventType;
    * Type of event
    */
   /**
    * Integer year;
    * Year in which event occurred
    */
   /**
    * boolean success;
    * Whether the request succeeded or failed
    */
  private Event[] events;
  private String eventID;
  private String associatedUsername;
  private String personID;
  private Float latitude;
  private Float longitude;
  private String country;
  private String city;
  private String eventType;
  private Integer year;
  private boolean success;
  private String error;

  public EventResult(String eventID, String associatedUsername, String personID, Float latitude, Float longitude, String country, String city, String eventType, Integer year, boolean success) {
    this.eventID=eventID;
    this.associatedUsername=associatedUsername;
    this.personID=personID;
    this.latitude=latitude;
    this.longitude=longitude;
    this.country=country;
    this.city=city;
    this.eventType=eventType;
    this.year=year;
    this.success=success;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error=error;
  }

  public Event[] getEvents() {
    return events;
  }

  public void setEvents(Event[] events) {
    this.events=events;
  }

  public String getEventID() {
    return eventID;
  }

  public void setEventID(String eventID) {
    this.eventID=eventID;
  }

  public String getAssociatedUsername() {
    return associatedUsername;
  }

  public void setAssociatedUsername(String associatedUsername) {
    this.associatedUsername=associatedUsername;
  }

  public String getPersonID() {
    return personID;
  }

  public void setPersonID(String personID) {
    this.personID=personID;
  }

  public Float getLatitude() {
    return latitude;
  }

  public void setLatitude(Float latitude) {
    this.latitude=latitude;
  }

  public Float getLongitude() {
    return longitude;
  }

  public void setLongitude(Float longitude) {
    this.longitude=longitude;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country=country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city=city;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType=eventType;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year=year;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success=success;
  }
}
