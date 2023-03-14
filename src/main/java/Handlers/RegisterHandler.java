package Handlers;

import Requests.RegisterRequest;
import Results.RegisterResult;
import Services.RegisterService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class RegisterHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    boolean success = false;
    RegisterResult result = null;
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {
        Headers reqHeaders = exchange.getRequestHeaders();
        InputStream reqBody = exchange.getRequestBody();
        String reqData = readString(reqBody);

        System.out.println(reqData);

        RegisterRequest request = (RegisterRequest)gson.fromJson(reqData, RegisterRequest.class);
        RegisterService service = new RegisterService();
        result = service.register(request);

        success = result.isSuccess();
      }

      if (!success) {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        OutputStream resBody = exchange.getResponseBody();
        String json = gson.toJson(result);
        resBody.write(json.getBytes());
        exchange.getResponseBody().close();
      }
      else{
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        OutputStream resBody = exchange.getResponseBody();
        String json = gson.toJson(result);
        resBody.write(json.getBytes());
        exchange.getResponseBody().close();
      }
    }
    catch (Exception e) {
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
