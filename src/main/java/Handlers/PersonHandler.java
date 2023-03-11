package Handlers;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Requests.PersonRequest;
import Results.PersonResult;
import Services.PersonService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import DataAccess.AuthTokenDao;
import Model.AuthToken;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

public class PersonHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    boolean success = false;
    Database db = new Database();

    try {
      Connection conn = db.getConnection();
      if (exchange.getRequestMethod().toLowerCase().equals("get")) {
        Headers reqHeaders = exchange.getRequestHeaders();
        if (reqHeaders.containsKey("Authorization")) {
          AuthTokenDao aDao = new AuthTokenDao(conn);
          String authToken = reqHeaders.getFirst("Authorization");
          if (aDao.find(authToken) != null) {
            InputStream reqBody = exchange.getRequestBody();
            String reqData = readString(reqBody);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            System.out.println(reqData);

            PersonRequest request = new PersonRequest();
            PersonService service = new PersonService();
            PersonResult result = null;
            request.setAuthToken(authToken);


            if(exchange.getRequestURI().toString().length() > 7) {
              String personID = exchange.getRequestURI().toString().substring(8);
              request.setPersonID(personID);
              result = service.findPerson(request, conn);
            }
            else{
              result = service.personTree(request, conn);
            }

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream resBody = exchange.getResponseBody();
            String json = gson.toJson(result);
            resBody.write(json.getBytes());

            exchange.getResponseBody().close();
            db.closeConnection(true);
            success = true;
          }
        }
      }

      if (!success) {
        // The HTTP request was invalid somehow, so we return a "bad request"
        // status code to the client.
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

        // We are not sending a response body, so close the response body
        // output stream, indicating that the response is complete.
        exchange.getResponseBody().close();
      }
    }
    catch (IOException e) {
      // Some kind of internal error has occurred inside the server (not the
      // client's fault), so we return an "internal server error" status code
      // to the client.
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

      // We are not sending a response body, so close the response body
      // output stream, indicating that the response is complete.
      exchange.getResponseBody().close();

      // Display/log the stack trace
      e.printStackTrace();
    }
    catch(DataAccessException e){
      db.closeConnection(false);
      e.printStackTrace();
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
      exchange.getResponseBody().close();
    }
  }

  /*
      The readString method shows how to read a String from an InputStream.
  */
  private String readString(InputStream is) throws IOException {
    StringBuilder sb = new StringBuilder();
    InputStreamReader sr = new InputStreamReader(is);
    char[] buf = new char[1024];
    int len;
    while ((len = sr.read(buf)) > 0) {
      sb.append(buf, 0, len);
    }
    return sb.toString();
  }
}
