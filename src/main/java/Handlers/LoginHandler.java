package Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Results.LoginResult;
import Requests.LoginRequest;
import Services.LoginService;

public class LoginHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    boolean success = false;

    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {
        Headers reqHeaders = exchange.getRequestHeaders();
        InputStream reqBody = exchange.getRequestBody();

        // Read JSON string from the input stream
        String reqData = readString(reqBody);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        // Display/log the request JSON data
        System.out.println(reqData);

        // TODO: Claim a route based on the request data

        LoginRequest request = (LoginRequest)gson.fromJson(reqData, LoginRequest.class);

        LoginService service = new LoginService();
        LoginResult result = service.login(request);


        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        OutputStream resBody = exchange.getResponseBody();
        gson.toJson((Object) result, (Appendable) resBody);
        resBody.close();


        // Start sending the HTTP response to the client, starting with
        // the status code and any defined headers.
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

        // We are not sending a response body, so close the response body
        // output stream, indicating that the response is complete.
        exchange.getResponseBody().close();

        success = true;
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
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

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
