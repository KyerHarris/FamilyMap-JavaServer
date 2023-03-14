package Handlers;

import Requests.FillRequest;
import Results.FillResult;
import Services.FillService;
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

public class FillHandler implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    boolean success = false;
    int defaultGenerations = 4;

    try {
      if (exchange.getRequestMethod().toLowerCase().equals("post")) {
        String url = exchange.getRequestURI().toString().substring(6);
        System.out.println(url);
        String[] input = url.split("/");

        if(input.length > 0) {
          GsonBuilder builder=new GsonBuilder();
          Gson gson=builder.create();

          FillRequest request= new FillRequest();
          request.setUsername(input[0]);

          if(input.length > 1){
            request.setGenerations(Integer.parseInt(input[1]));
          }
          else {
            request.setGenerations(defaultGenerations);
          }

          FillService service=new FillService();
          FillResult result = service.fill(request);

          if(result.isSuccess()) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream resBody = exchange.getResponseBody();
            String json = gson.toJson(result);
            resBody.write(json.getBytes());
          }
          else{
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStream resBody = exchange.getResponseBody();
            String json = gson.toJson(result);
            resBody.write(json.getBytes());
          }

          exchange.getResponseBody().close();

          success = true;
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
