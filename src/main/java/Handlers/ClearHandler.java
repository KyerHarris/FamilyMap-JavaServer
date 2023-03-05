package Handlers;

import Requests.LoginRequest;
import Services.ClearService;
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

import Requests.ClearRequest;
import Results.ClearResult;
import Services.ClearService;

public class ClearHandler implements HttpHandler {
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

        ClearRequest clearRequest = (ClearRequest)gson.fromJson(reqData, ClearRequest.class);
        ClearService clearService = new ClearService();
        ClearResult clearResult = clearService.clear(clearRequest);

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        OutputStream resBody = exchange.getResponseBody();
        gson.toJson((Object) clearResult, (Appendable) resBody);
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
      exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);

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
