package util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import main.Connection;
import main.Customer;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;

import static main.MainForm.customers;
import static main.MainForm.master;
import static main.MainForm.secrethash;
import static util.MD5Wrapper.createHash;

public class ConfigManager {

  public static void loadConfig() {

    String content = null;
    try {
      Reader reader = new FileReader("config.json");


      BufferedReader br = new BufferedReader(reader);
      StringBuilder sb = new StringBuilder();
      String s;
      while ((s = br.readLine()) != null) {
        sb.append(s);
      }
      reader.close();

      content = sb.toString();

      JsonElement root = new JsonParser().parse(content);

      master = root.getAsJsonObject().get("master").getAsString();

    } catch (Exception e) {
      JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
      e.printStackTrace();
    }
  }

  public static void saveConfig() {
    saveConfig("config.json");
  }

  public static void saveConfig(String name) {
    System.out.print("Saving configuration...");

    try {

      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      JsonObject json = new JsonObject();

      json.addProperty("master", master);

      FileWriter writer = new FileWriter(name);
      writer.write(gson.toJson(json));
      writer.close();

      System.out.println("done.");

    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  public static Customers loadCustomers() {

    try {
      Reader reader = new FileReader("connections.json");

      BufferedReader br = new BufferedReader(reader);
      StringBuilder sb = new StringBuilder();
      String s;
      while ((s = br.readLine()) != null) {
        sb.append(s);
      }
      reader.close();

      String content = sb.toString();

      JsonElement root = new JsonParser().parse(content);

      Type type = new TypeToken<Customers>() {}.getType();

      return new Gson().fromJson(root, type);

    } catch (Exception e) {
      JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
      e.printStackTrace();
    }

    System.out.println("No connections found, returning empty array");
    return new Customers();
  }

  public static void saveConnections() {
    System.out.print("Saving connections...");

    try {

      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      String json = gson.toJson(customers);
      FileWriter writer = new FileWriter("connections.json");
      writer.write(json);
      writer.close();

      System.out.println("done.");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void setNewMaster(String newMaster) {

    String newHash = createHash(newMaster);

    System.out.println("Old hash: " + master);
    System.out.println("New hash: " + newHash);

    for (Customer cust : customers) {

      System.out.println("Re-Encrypting " + cust.name + "...");

      for (Connection connection : cust.connections) {

        System.out.println("  Re-Encrypting " + connection.label + "...");

        // Decrypt password
        String clearPass = connection.decrypt(connection.password);

        // Re-Encrypt with new key
        connection.encrypt(clearPass, newHash);

        System.out.println("    New encrypted pass: " + connection.password);
      }
    }

    System.out.println("Saving new Hash");

    // Set new Hash
    secrethash = newHash;
    master = newHash;

    saveConfig();
    saveConnections();
  }

}
