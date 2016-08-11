package util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import main.Connection;
import main.Customer;
import util.Dialogs.ErrorDialog;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;

import static main.MainForm.customers;
import static main.MainForm.master;
import static main.MainForm.secrethash;
import static util.MD5Wrapper.createHash;

public class ConfigManager {
  
  public static void loadConfig() {
    
    try {
      Reader reader = new FileReader("config.json");
      
      
      BufferedReader br = new BufferedReader(reader);
      StringBuilder sb = new StringBuilder();
      String s;
      while ((s = br.readLine()) != null) {
        sb.append(s);
      }
      reader.close();
      
      String content = sb.toString();
      
      JsonElement root = new JsonParser().parse(content);
      
      master = root.getAsJsonObject().get("master").getAsString();
      
    } catch (Exception e) {
      JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
      e.printStackTrace();
      new ErrorDialog(e);
      
    }
  }
  
  public static void saveConfig() {
    saveConfig("config.json");
  }
  
  public static void saveConfig(String name) {
    System.out.print("Saving configuration...");
    
    if (name.equals("")) {
      System.out.print("Invalid name, aborting.");
      
    } else {
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
        new ErrorDialog(e);
        
      }
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
      
      Type type = new TypeToken<Customers>() {
      }.getType();
      
      return new Gson().fromJson(root, type);
      
    } catch (Exception e) {
      e.printStackTrace();
      new ErrorDialog(e);
    }
    
    System.out.println("No connections found, returning empty array");
    return new Customers();
  }
  
  public static void saveConnections() {
    
    saveConnections("connections.json");
  }
  
  public static void saveConnections(String name) {
    System.out.print("Saving connections...");
    
    if (name.equals("")) {
      System.out.print("Invalid name, aborting.");
    } else {
      try {
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        String json = gson.toJson(customers);
        FileWriter writer = new FileWriter(name);
        writer.write(json);
        writer.close();
        
        System.out.println("done.");
        
      } catch (IOException e) {
        e.printStackTrace();
        new ErrorDialog(e);
      }
    }
  }
  
  public static void setNewMaster(String newMaster) {
    
    // Create hash of new master key
    String newHash = createHash(newMaster);
    
    for (Customer cust : customers) {
      
      for (Connection connection : cust.connections) {
        
        if (!connection.password.equals("")) {
          // Decrypt password
          String clearPass = connection.decrypt(connection.password);
          
          if (newMaster.equals("")) {
            // Do not encrypt
            connection.password = clearPass;
          } else {
            // Re-Encrypt with new key
            System.out.println("  Re-Encrypting " + connection.label + " with hash: " + newHash);
            
            System.out.println("  Old Pass/Pass-Hash " + connection.password);
            
            connection.password = connection.encrypt(clearPass, newHash);
            
            System.out.println("  New Pass-Hash " + connection.password);
          }
        }
      }
    }
    
    // Set new Hash
    if (newMaster.equals("")) {
      // mark master as disabled
      secrethash = "disabled";
      master = "disabled";
    } else {
      System.out.println("Saving new Hash");
      secrethash = newHash;
      master = newHash;
    }
    
    saveConfig();
    saveConnections();
  }
}
