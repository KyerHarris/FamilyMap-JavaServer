package Services;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Requests.FillRequest;
import Results.FillResult;
import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import javax.xml.crypto.Data;

/**
 * FillService
 * Populates the server's database with generated data for the specified username. The required "username" parameter must be a user already registered with the server. If there is any data in the database already associated with the given username, it is deleted.
 * The optional "generations" parameter lets the caller specify the number of generations of ancestors to be generated, and must be a non-negative integer (the default is 4, which results in 31 new persons each with associated events).
 * Returns if successful and how many events and persons were added
 */
public class FillService {
  LocationData locationData;
  Mnames mnames;
  Fnames fnames;
  Snames snames;
  String username;
  int baseYear = 2023;

  /**
   * makes FillRequest
   * @param request
   * @return FillResult
   */
  public FillResult fill(FillRequest request){
    FillResult result = new FillResult();
    username = request.getUsername();
    Database db = new Database();
    try{
      Connection conn = db.getConnection();
      UserDao uDao = new UserDao(conn);

      if(uDao.find(username) != null) {
        //Import names and locations
        GsonBuilder builder=new GsonBuilder();
        Gson gson=builder.create();
        try {
          Reader reader = new FileReader("json/locations.json");
          locationData = (LocationData) gson.fromJson(reader, LocationData.class);
          reader = new FileReader("json/mnames.json");
          mnames = (Mnames) gson.fromJson(reader, Mnames.class);
          reader = new FileReader("json/fnames.json");
          fnames = (Fnames) gson.fromJson(reader, Fnames.class);
          reader = new FileReader("json/snames.json");
          snames = (Snames) gson.fromJson(reader, Snames.class);
        } catch (FileNotFoundException error) {
          result.setError("Could not load files");
          db.closeConnection(false);
          return result;
        }

        generatePerson(uDao.find(username).getGender(), request.getGenerations(), conn);

      }
      else{
        result.setError("User does not exist");
        db.closeConnection(false);
        return result;
      }
    }
    catch(DataAccessException error){
      result.setError("Failed to fill ancestry");
      db.closeConnection(false);
      return result;
    }

    result.setSuccess(true);
    return result;
  }

  private Person generatePerson(String gender, int generations, Connection conn){
    PersonDao pDao = new PersonDao(conn);
    EventDao eDao = new EventDao(conn);
    Person mother = null;
    Person father = null;

    if(generations > 1){
      //creating parents
      mother = generatePerson("f", generations - 1, conn);
      father = generatePerson("m", generations - 1, conn);

      mother.setSpouseID(father.getPersonID());
      father.setSpouseID(mother.getPersonID());
      Event marriage = generateMarriage(generations, father, mother);
      try {
        eDao.insert(marriage);
      }
      catch (DataAccessException error){
        error.printStackTrace();
        System.out.println("failed to create marriage event for " + father.getFirstName() + " and " + mother.getFirstName());
      }
    }
    //setting person information
    Person person = generatePersonData(generations, gender, father, mother);

    try{
      pDao.insert(person);
      //creating and inserting birth & death
      eDao.insert(generateBirth(generations, person));
      eDao.insert(generateDeath(generations, person));
    }
    catch(DataAccessException error){
      error.printStackTrace();
      System.out.println("error inserting person or events");
    }

    return person;
  }

  private Event generateMarriage(int generations, Person father, Person mother){
    Location marriageLocation = locationData.data[(int)Math.floor(Math.random() *(locationData.data.length + 1))];
    UUID marriageID = UUID.randomUUID();

    Event marriage = new Event();
    marriage.setCity(marriageLocation.city);
    marriage.setCountry(marriageLocation.country);
    marriage.setLatitude(marriageLocation.latitude);
    marriage.setLongitude(marriageLocation.longitude);
    marriage.setAssociatedUsername(username);
    marriage.setEventType("marriage");
    marriage.setYear((int)Math.floor(Math.random() *((baseYear - (generations * 10)) - (baseYear - (generations * 20)) + 1) + (generations * 20)));
    marriage.setPersonID(mother.getPersonID());
    marriage.setSecondID(father.getPersonID());
    marriage.setEventID(marriageID.toString());

    return marriage;
  }

  private Person generatePersonData(int generations, String gender, Person father, Person mother){
    Person person = new Person();
    UUID personID = UUID.randomUUID();
    int firstName;

    if(generations > 1){
      person.setFatherID(father.getPersonID());
      person.setMotherID(mother.getPersonID());
    }
    if(gender.equals("m")) {
      person.setFirstName(mnames.data[(int)Math.floor(Math.random() * (mnames.data.length + 1))]);
    }
    else{
      person.setFirstName(fnames.data[(int)Math.floor(Math.random() * (fnames.data.length + 1))]);
    }
    int lastName = (int)Math.floor(Math.random() * (snames.data.length + 1));
    person.setPersonID(personID.toString());
    person.setAssociatedUsername(username);
    person.setLastName(snames.data[lastName]);
    person.setGender(gender);

    return person;
  }

  private Event generateBirth(int generations, Person person){
    UUID birthID = UUID.randomUUID();
    Event birth = new Event();
    Location birthLocation = locationData.data[(int)Math.floor(Math.random() *(locationData.data.length + 1))];

    birth.setPersonID(person.getPersonID());
    birth.setEventID(birthID.toString());
    birth.setEventType("birth");
    birth.setAssociatedUsername(username);
    birth.setYear((int)Math.floor(Math.random() *((baseYear - (generations * 10)) - (baseYear - (generations * 20)) + 1) + (generations * 20)));
    birth.setLongitude(birthLocation.longitude);
    birth.setLatitude(birthLocation.latitude);
    birth.setCity(birthLocation.city);
    birth.setCountry(birthLocation.country);

    return birth;
  }

  private Event generateDeath(int generations, Person person){
    UUID deathID = UUID.randomUUID();
    Event death = new Event();
    Location deathLocation = locationData.data[(int)Math.floor(Math.random() *(locationData.data.length + 1))];

    death.setPersonID(person.getPersonID());
    death.setEventID(deathID.toString());
    death.setEventType("death");
    death.setAssociatedUsername(username);
    death.setYear((int)Math.floor(Math.random() *((baseYear) - (baseYear - (generations * 10)) + 1) + (generations * 10)));
    death.setLongitude(deathLocation.longitude);
    death.setLatitude(deathLocation.latitude);
    death.setCity(deathLocation.city);
    death.setCountry(deathLocation.country);

    return death;
  }

  static class Location{
    float longitude;
    float latitude;
    String city;
    String country;
  }
  static class LocationData {
    Location[] data;
  }
  static class Mnames{
    String[] data;
  }
  static class Fnames{
    String[] data;
  }
  static class Snames{
    String[] data;
  }

}
