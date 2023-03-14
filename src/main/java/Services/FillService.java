package Services;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Requests.FillRequest;
import Results.FillResult;
import java.io.FileNotFoundException;
import Model.User;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import java.util.Random;

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
  int totalGenerations;

  /**
   * makes FillRequest
   * @param request
   * @return FillResult
   */
  public FillResult fill(FillRequest request){
    FillResult result = new FillResult();
    Database db = new Database();
    try{
      Connection conn = db.getConnection();
      result = fill(request, conn);
      if(!result.isSuccess()){
        db.closeConnection(false);
        result.setSuccess(false);
        return result;
      }
    }
    catch(DataAccessException error){
      db.closeConnection(false);
      error.printStackTrace();
      result.setMessage("Error: failed to fill " + request.getUsername());
      System.out.println("Fill request failed");
      return result;
    }

    db.closeConnection(true);
    return result;
  }

  public FillResult fill(FillRequest request, Connection conn){
    FillResult result = new FillResult();
    username = request.getUsername();
    totalGenerations = request.getGenerations();
    try{
      UserDao uDao = new UserDao(conn);
      PersonDao pDao = new PersonDao(conn);
      EventDao eDao = new EventDao(conn);
      User user = uDao.find(username);
      if(user == null){
        result.setMessage("Error: username not found");
        return result;
      }
      else {
        if (pDao.find(user.getPersonID()) != null){
          pDao.clearFamilyTree(username);
        }
          //Import names and locations
        GsonBuilder builder=new GsonBuilder();
        Gson gson=builder.create();
        try {
          Reader reader=new FileReader("json/locations.json");
          locationData=(LocationData) gson.fromJson(reader, LocationData.class);
          reader=new FileReader("json/mnames.json");
          mnames=(Mnames) gson.fromJson(reader, Mnames.class);
          reader=new FileReader("json/fnames.json");
          fnames=(Fnames) gson.fromJson(reader, Fnames.class);
          reader=new FileReader("json/snames.json");
          snames=(Snames) gson.fromJson(reader, Snames.class);
        } catch (FileNotFoundException error) {
          result.setMessage("Error: Could not load files");
          return result;
        }

        generateUser(user, request.getGenerations(), pDao, eDao);
      }
    }
    catch(DataAccessException error){
      result.setMessage("Error: Failed to fill ancestry");
      return result;
    }

    int numPersons = (int)(Math.pow(2, request.getGenerations() + 1) - 1);
    result.setMessage("Successfully added " + numPersons + " persons and " + ((numPersons * 3) - 1 ) + " events to the database.");
    result.setSuccess(true);
    return result;
  }

  private void generateUser(User user, int generations, PersonDao pDao, EventDao eDao){
    UUID uuid = UUID.randomUUID();
    Person mother = null;
    Person father = null;

    //creating parents
    mother = generatePerson("f", generations - 1, pDao, eDao);
    father = generatePerson("m", generations - 1, pDao, eDao);

    mother.setSpouseID(father.getPersonID());
    father.setSpouseID(mother.getPersonID());
    Event marriage = generateMarriage(generations);
    try{
      pDao.insert(mother);
      pDao.insert(father);
    }
    catch(DataAccessException error){
      error.printStackTrace();
      System.out.println("error inserting parents " + mother.getFirstName() + " and " + father.getFirstName());
    }
    marriage.setPersonID(mother.getPersonID());
    try {
      eDao.insert(marriage);
    }
    catch (DataAccessException error){
      error.printStackTrace();
      System.out.println("failed to create marriage event for " + father.getFirstName() + " and " + mother.getFirstName());
    }
    marriage.setPersonID(father.getPersonID());
    marriage.setEventID(uuid.toString());
    try {
      eDao.insert(marriage);
    }
    catch (DataAccessException error){
      error.printStackTrace();
      System.out.println("failed to create marriage event for " + father.getFirstName() + " and " + mother.getFirstName());
    }
    //setting person information
    Person person = null;
    person = generateUserData(generations, user, father, mother);
    int year;
    Event birth = generateBirth(generations, person);
    Event death = generateDeath(generations, person);

    try{
      pDao.insert(person);
    }
    catch(DataAccessException error){
      error.printStackTrace();
      System.out.println("error inserting personID " + person.getPersonID());
    }
    try{
      eDao.insert(birth);
    }
    catch(DataAccessException error){
      error.printStackTrace();
      System.out.println("error inserting eventID " + birth.getEventID());
    }
    try{
      eDao.insert(death);
    }
    catch(DataAccessException error){
      error.printStackTrace();
      System.out.println("error inserting eventID " + death.getEventID());
    }

  }

  private Person generatePerson(String gender, int generations, PersonDao pDao, EventDao eDao){
    UUID uuid = UUID.randomUUID();
    Person mother = null;
    Person father = null;

    if(generations > 0){
      //creating parents
      mother = generatePerson("f", generations - 1, pDao, eDao);
      father = generatePerson("m", generations - 1, pDao, eDao);

      mother.setSpouseID(father.getPersonID());
      father.setSpouseID(mother.getPersonID());
      Event marriage = generateMarriage(generations);
      try{
        pDao.insert(mother);
        pDao.insert(father);
      }
      catch(DataAccessException error){
        error.printStackTrace();
        System.out.println("error inserting parents " + mother.getFirstName() + " and " + father.getFirstName());
      }
      marriage.setPersonID(mother.getPersonID());
      try {
        eDao.insert(marriage);
      }
      catch (DataAccessException error){
        error.printStackTrace();
        System.out.println("failed to create marriage event for " + father.getFirstName() + " and " + mother.getFirstName());
      }
      marriage.setPersonID(father.getPersonID());
      marriage.setEventID(uuid.toString());
      try {
        eDao.insert(marriage);
      }
      catch (DataAccessException error){
        error.printStackTrace();
        System.out.println("failed to create marriage event for " + father.getFirstName() + " and " + mother.getFirstName());
      }
    }
    //setting person information
    Person person = null;
    person = generatePersonData(gender, father, mother);
    Event birth = generateBirth(generations, person);
    Event death = generateDeath(generations, person);

    try{
      eDao.insert(birth);
    }
    catch(DataAccessException error){
      error.printStackTrace();
      System.out.println("error inserting eventID " + birth.getEventID());
    }
    try{
      eDao.insert(death);
    }
    catch(DataAccessException error){
      error.printStackTrace();
      System.out.println("error inserting eventID " + death.getEventID());
    }

    return person;
  }

  private Event generateMarriage(int generations){
    UUID marriageID = UUID.randomUUID();
    Random rand = new Random();
    Location marriageLocation = locationData.data[rand.nextInt(locationData.data.length)];
    int year = baseYear - ((totalGenerations - generations) * rand.nextInt( 6)) - (20 * (totalGenerations - generations));


    Event marriage = new Event();
    marriage.setCity(marriageLocation.city);
    marriage.setCountry(marriageLocation.country);
    marriage.setLatitude(marriageLocation.latitude);
    marriage.setLongitude(marriageLocation.longitude);
    marriage.setAssociatedUsername(username);
    marriage.setEventType("marriage");
    marriage.setYear(year);
    marriage.setEventID(marriageID.toString());

    return marriage;
  }

  private Person generatePersonData(String gender, Person father, Person mother){
    Person person = new Person();
    Random rand = new Random();
    UUID personID = UUID.randomUUID();
    int firstName;


    if(father != null){
      person.setFatherID(father.getPersonID());
      person.setMotherID(mother.getPersonID());
    }
    if(gender.equals("m")) {
      person.setFirstName(mnames.data[rand.nextInt(mnames.data.length)]);
    }
    else{
      person.setFirstName(fnames.data[rand.nextInt(fnames.data.length)]);
    }
    int lastName = rand.nextInt(snames.data.length);
    person.setPersonID(personID.toString());
    person.setAssociatedUsername(username);
    person.setLastName(snames.data[lastName]);
    person.setGender(gender);

    return person;
  }

  private Person generateUserData(int generations, User user, Person father, Person mother){
    Person person = new Person();
    int firstName;

    if(generations > 0){
      person.setFatherID(father.getPersonID());
      person.setMotherID(mother.getPersonID());
    }
    person.setFirstName(user.getFirstName());
    person.setPersonID(user.getPersonID());
    person.setAssociatedUsername(username);
    person.setLastName(user.getLastName());
    person.setGender(user.getGender());

    return person;
  }

  private Event generateBirth(int generations, Person person){
    UUID birthID = UUID.randomUUID();
    Event birth = new Event();
    Random rand = new Random();
    Location birthLocation = locationData.data[rand.nextInt(locationData.data.length)];
    int year = baseYear - ((totalGenerations - generations) * rand.nextInt( 6)) - (30 * (totalGenerations - generations));

    birth.setPersonID(person.getPersonID());
    birth.setEventID(birthID.toString());
    birth.setEventType("birth");
    birth.setAssociatedUsername(username);
    birth.setYear(year);
    birth.setLongitude(birthLocation.longitude);
    birth.setLatitude(birthLocation.latitude);
    birth.setCity(birthLocation.city);
    birth.setCountry(birthLocation.country);

    return birth;
  }

  private Event generateDeath(int generations, Person person){
    UUID deathID = UUID.randomUUID();
    Event death = new Event();
    Random rand = new Random();
    Location deathLocation = locationData.data[rand.nextInt(locationData.data.length)];
    int year = baseYear - ((totalGenerations - generations) * rand.nextInt( 6)) - (10 * (totalGenerations - generations));


    death.setPersonID(person.getPersonID());
    death.setEventID(deathID.toString());
    death.setEventType("death");
    death.setAssociatedUsername(username);
    death.setYear(year);
    death.setLongitude(deathLocation.longitude);
    death.setLatitude(deathLocation.latitude);
    death.setCity(deathLocation.city);
    death.setCountry(deathLocation.country);

    return death;
  }

  class Location{
    float longitude;
    float latitude;
    String city;
    String country;
  }
  class LocationData {
    Location[] data;
  }
  class Mnames{
    String[] data;
  }
  class Fnames{
    String[] data;
  }
  class Snames{
    String[] data;
  }

}
