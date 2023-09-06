package main.java;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Assignment {

  private String authToken;

  public static void main(String[] args) throws IOException, InterruptedException {
    Assignment assignment = new Assignment();
    assignment.authenticate();
    assignment.createCustomer();
    assignment.getCustomers();
    assignment.deleteCustomer("<uuid>");
    assignment.updateCustomer("<uuid>");
  }

  public void authenticate() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp"))
        .POST(HttpRequest.BodyPublishers.ofString("""
            {
              "login_id": "test@sunbasedata.com",
              "password": "Test@123"
            }
            """))
        .build();

    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

    // Extract auth token
    authToken = response.body().substring(response.body().indexOf("Bearer ") + "Bearer ".length());
  }

  public void createCustomer() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create"))
        .header("Authorization", "Bearer " + authToken)
        .POST(HttpRequest.BodyPublishers.ofString("""
            {
              "first_name": "John",
              "last_name": "Doe",
              "street": "123 Main St",
              "city": "Anytown",
              "state": "CA",
              "email": "john@doe.com",
              "phone": "123-456-7890" 
            }
            """))
        .build();

    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
  }

  public void getCustomers() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list"))
        .header("Authorization", "Bearer " + authToken)
        .GET()
        .build();

    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

    List<Customer> customers = parseCustomerListJson(response.body());
  }

  public void deleteCustomer(String uuid) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=delete&uuid=" + uuid))
        .header("Authorization", "Bearer " + authToken)
        .POST(HttpRequest.BodyPublishers.noBody())
        .build();

    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
  }

  public void updateCustomer(String uuid) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=update&uuid=" + uuid))
        .header("Authorization", "Bearer " + authToken)
        .POST(HttpRequest.BodyPublishers.ofString("""
            {
              "first_name": "Jane",
              "last_name": "Doe",
              "street": "456 Park Ave",
              "city": "Anytown",
              "state": "CA",
              "email": "jane@doe.com",
              "phone": "987-654-3210"
            }
            """))
        .build();

    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
  }

  // Helper methods for parsing JSON
  public List<Customer> parseCustomerListJson(String json) {
    // Implement parsing logic
    return null; 
  }

}

class Customer {
  private String firstName;
  private String lastName;
  // other fields
}