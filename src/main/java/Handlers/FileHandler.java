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


    boolean success = false;

    try {

      if (exchange.getRequestMethod().toLowerCase().equals("get")) {
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
          file = new File("web/HTML/404.html");
          exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
          OutputStream resBody = exchange.getResponseBody();
          Files.copy(file.toPath(), resBody);
          resBody.close();
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
