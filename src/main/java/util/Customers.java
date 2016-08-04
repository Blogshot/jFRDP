package util;

import main.Customer;

import java.util.ArrayList;

@SuppressWarnings("SameParameterValue")
public class Customers extends ArrayList<Customer> {

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  public boolean contains(String name) {

    for (Customer customer : this) {
      if (customer.getName().equals(name)) {
        return true;
      }
    }

    return false;
  }

  public Customer get(String name) {

    for (Customer customer : this) {
      if (customer.getName().equals(name)) {
        return customer;
      }
    }

    return null;
  }
}
