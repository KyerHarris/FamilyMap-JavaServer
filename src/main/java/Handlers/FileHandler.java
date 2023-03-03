package Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {

    // This handler allows a "Ticket to Ride" player to claim ability
    // route between two cities (part of the Ticket to Ride game).
    // The HTTP request body contains a JSON object indicating which
    // route the caller wants to claim (a route is defined by two cities).
    // This implementation is clearly unrealistic, because it
    // doesn't actually do anything other than print out the received JSON string.
    // It is also unrealistic in that it accepts only one specific
    // hard-coded auth token.
    // However, it does demonstrate the following:
    // 1. How to get the HTTP request type (or, "method")
    // 2. How to access HTTP request headers
    // 3. How to read JSON data from the HTTP request body
    // 4. How to return the desired status code (200, 404, etc.)
    //		in an HTTP response
    // 5. How to check an incoming HTTP request for an auth token

    boolean success = false;

    try {
      // Determine the HTTP request type (GET, POST, etc.).
      // Only allow POST requests for this operation.
      // This operation requires a POST request, because the
      // client is "posting" information to the server for processing.
      if (exchange.getRequestMethod().toLowerCase().equals("get")) {

        /*
        Headers reqHeaders = exchange.getRequestHeaders();
        InputStream reqBody = exchange.getRequestBody();
        String reqData = readString(reqBody);
        System.out.println(reqData);
         */

        //enter as a get method with a url
        String urlPath = exchange.getRequestURI().toString();
        if(urlPath.equals(File.separator) || urlPath == null || urlPath.equals("/")){
          StringBuilder temp = new StringBuilder();
          temp.append("web");
          temp.append(File.separator);
          temp.append("index.html");
          urlPath = temp.toString();
        }
        else{
          StringBuilder temp = new StringBuilder();
          temp.append("web");
          temp.append(urlPath);
          urlPath = temp.toString();
        }

        File file = new File(urlPath);

        // Start sending the HTTP response to the client, starting with
        // the status code and any defined headers.
        if(file.exists()) {
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
          OutputStream resBody = exchange.getResponseBody();
          Files.copy(file.toPath(), resBody);
          resBody.close();
        }
        else{
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        }
        // We are not sending a response body, so close the response body
        // output stream, indicating that the response is complete.
        exchange.getResponseBody().close();

        success = true;
      }
      else {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
        exchange.getResponseBody().close();
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
