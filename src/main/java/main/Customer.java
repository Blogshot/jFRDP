package main;

import java.util.ArrayList;

public class Customer {

  String name = "";
  ArrayList<Connection> connections = new ArrayList<Connection>();

  public Customer(String name) {
    this.name = name;
  }

  public void addConnection(Connection connection) {
    if (connection.address.startsWith("http")) {
      connection.type = Connection.Type.http;
    }
    connections.add(connection);
  }

  @Override
  public String toString() {
    return this.name;
  }

  public void removeConnection(Connection connection) {
    this.connections.remove(connection);
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
