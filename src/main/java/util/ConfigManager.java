package util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import main.Connection;
import main.Customer;
import util.Dialogs.ErrorDialog;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;

import static main.MainForm.*;
import static util.MD5Wrapper.createHash;

public class ConfigManager {
  
  public static boolean startEdit = false;
  
  private static JsonObject getConfig() {
    try {
      Reader reader = new FileReader(xfreerdpHome + configName);
    
    
      BufferedReader br = new BufferedReader(reader);
      StringBuilder sb = new StringBuilder();
      String s;
      while ((s = br.readLine()) != null) {
        sb.append(s);
      }
      reader.close();
    
      String content = sb.toString();
    
      JsonElement root = new JsonParser().parse(content);
    
      return root.getAsJsonObject();
      
    } catch (Exception e) {
      JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
      e.printStackTrace();
      new ErrorDialog(e);
    }
    
    return new JsonObject();
  }
  
  public static void loadConfig() {
    
    try {
      master = getConfig().get("master").getAsString();
      standardKeyboardLayout = getConfig().get("standardKeyboardLayout").getAsString();
      useStandardKeyboardLayout = getConfig().get("useStandardKeyboardLayout").getAsBoolean();
      useDebug = getConfig().get("useDebug").getAsBoolean();
      closeDebugAutomatically = getConfig().get("closeDebugAutomatically").getAsBoolean();
      primaryScreen = getConfig().get("primaryScreen").getAsInt();
  
    } catch (NullPointerException e) {
      e.printStackTrace();
      new ErrorDialog(e);
    }
  }
  
  public static JsonElement loadConfig(String attribute) {
    if (getConfig().has(attribute)) {
      return getConfig().get(attribute);
    } else {
      return null;
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
        
        json.addProperty("standardKeyboardLayout", standardKeyboardLayout);
        json.addProperty("useStandardKeyboardLayout", useStandardKeyboardLayout);
        json.addProperty("useDebug", useDebug);
        json.addProperty("closeDebugAutomatically", closeDebugAutomatically);
        json.addProperty("primaryScreen", primaryScreen);
  
        FileWriter writer = new FileWriter(xfreerdpHome + configName);
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
      Reader reader = new FileReader(xfreerdpHome + connectionsName);
      
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
    // only do this when not loading Connection
    if (startEdit) {
      return;
    }
    
    System.out.print("Saving connections...");
    
    if (name.equals("")) {
      System.out.print("Invalid name, aborting.");
    } else {
      try {
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        String json = gson.toJson(customers);
        FileWriter writer = new FileWriter(xfreerdpHome + connectionsName);
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
