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
        String reqData = readString(reqBody);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        System.out.println(reqData);

        LoginRequest request = (LoginRequest)gson.fromJson(reqData, LoginRequest.class);
        LoginService service = new LoginService();
        LoginResult result = service.login(request);


        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        OutputStream resBody = exchange.getResponseBody();
        gson.toJson((Object) result, (Appendable) resBody);
        resBody.close();

        exchange.getResponseBody().close();

        success = true;
      }

      if (!success) {
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        exchange.getResponseBody().close();
      }
    }
    catch (IOException e) {
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
      exchange.getResponseBody().close();
      e.printStackTrace();
    }
  }

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
