package main;

import java.util.ArrayList;

public class Customer {

  public String name = "";
  public ArrayList<Connection> connections = new ArrayList<>();

  public Customer(String name) {
    this.name = name;
  }

  public void addConnection(Connection connection) {
    if (connection.address.startsWith("http")) {
      connection.type = Connection.Type.http;
    }
    if (connection.address.startsWith("ftp")) {
      connection.type = Connection.Type.ftp;
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


  public Customer(Customer another) {
    this.name = another.name;
    this.connections = another.connections;
  }

}
