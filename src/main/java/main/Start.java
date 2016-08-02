package main;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import util.TreePopupMenu;

import java.awt.event.ActionEvent;
import java.lang.reflect.Type;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;

public class Start {

  public static MainForm form;
  public static ArrayList<Customer> customers = new ArrayList<Customer>();

  static String secrethash = "";
  private static String master = "";

  public static void main(String[] args) {

    System.setProperty("apple.laf.useScreenMenuBar", "true");
    form = new MainForm();

    customers = loadCustomers();

    JFrame frame = new JFrame("jFRDP");
    frame.setContentPane(form.root);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(800, 600));
    frame.pack();

    setupMenuBar(frame);

    frame.setVisible(true);


    loadConfig();

    secrethash = createHash(showSecretInputDialog());

    if (secrethash.equals(master)) {
      fillGUI();
    } else {
      JOptionPane.showMessageDialog(new JFrame(), "Master-Password incorrect!");
    }
  }

  private static void setupMenuBar(JFrame frame) {

    JMenuBar menuBar = new JMenuBar();
    //Hinzufügen von Menüs
    JMenu menuFile =
        new JMenu("Datei");
    JMenu menuEdit =
        new JMenu("Bearbeiten");
    JMenu menuHelp =
        new JMenu("Hilfe");

    menuBar.add(menuFile);
    menuBar.add(menuEdit);
    menuBar.add(menuHelp);

    //Hinzufügen von Untermenüs
    JMenu menuFileNew =
        new JMenu("Neu");


    menuFile.add(menuFileNew);

    //Hinzufügen von Menüeinträgen in das Dateimenü
    JMenuItem menuItemFileSave = new JMenuItem(new AbstractAction("Speichern") {
      public void actionPerformed(ActionEvent e) {
        saveConfig();
        saveConnections();
      }
    });

    menuFile.add(menuItemFileSave);


    frame.setJMenuBar(menuBar);
  }

  public static String createHash(String input) {

    try {
      java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
      byte[] array = md.digest(input.getBytes());

      StringBuilder sb = new StringBuilder();
      for (byte anArray : array) {
        sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
      }
      return sb.toString().substring(0, 16);
    } catch (java.security.NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;

  }

  private static String expansionState = "";

  public static void fillGUI() {

    expansionState = getExpansionState();

    DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    form.connectionList.setModel(new DefaultTreeModel(root));


    for (Customer customer : customers) {

      System.out.println("Adding " + customer.name);

      DefaultMutableTreeNode customerNode = new DefaultMutableTreeNode(customer);

      for (Connection connection : customer.connections) {

        DefaultMutableTreeNode conn = new DefaultMutableTreeNode(connection);
        conn.setAllowsChildren(false);

        customerNode.add(conn);
      }

      root.add(customerNode);
    }

    form.connectionList.setRootVisible(false);
    form.connectionList.setShowsRootHandles(true);
    form.connectionList.expandPath(new TreePath(root));

    setExpansionState(expansionState);

    form.connectionList.repaint();

  }

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
    System.out.print("Saving...");

    try {

      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      JsonObject json = new JsonObject();

      json.addProperty("master", master);

      FileWriter writer = new FileWriter("config.json");
      writer.write(new Gson().toJson(json));
      writer.close();


    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static ArrayList<Customer> loadCustomers() {

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

      Type type = new TypeToken<ArrayList<Customer>>() {
      }.getType();
      ArrayList<Customer> customers = new Gson().fromJson(root, type);

      for (Customer customer : customers) {
        System.out.println(customer.name);
      }

      return customers;

    } catch (Exception e) {
      JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
      e.printStackTrace();
    }

    System.out.println("No connections found, returning empty array");
    return new ArrayList<Customer>();
  }

  private static String showSecretInputDialog() {

    // prompt the user to enter their master-key

    // get the user's input. note that if they press Cancel, 'secret' will be null
    return JOptionPane.showInputDialog(new JFrame(), "Enter your master-key to unlock passwords");
  }

  public static String showInputDialog(String text) {

    // prompt the user to enter their master-key

    // get the user's input. note that if they press Cancel, 'secret' will be null
    return JOptionPane.showInputDialog(new JFrame(), text);
  }

  public static void saveConnections() {
    System.out.print("Saving...");

    try {

      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      String json = gson.toJson(customers);
      FileWriter writer = new FileWriter("connections.json");
      writer.write(json);
      writer.close();


    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("done.");

  }

  public static void setNewMaster(String newMaster) {

    String newHash = createHash(newMaster);

    System.out.println("Old hash: " + master);
    System.out.println("New hash: " + newHash);

    for (Customer cust : customers) {

      System.out.println("Re-Encrypting " + cust.name + "...");

      for (Connection connection : cust.connections) {

        System.out.println("  Re-Encrypting " + connection.label + "...");

        String clearPass = connection.decrypt(connection.password);

        System.out.println("    Old encrypted pass: " + connection.password);
        System.out.println("    ClearPass: " + clearPass);

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

  private static class saveWrapper {

    @SerializedName("Connections")
    private ArrayList<Customer> customers;

    public saveWrapper(ArrayList<Customer> customers) {
      this.customers = customers;
    }
  }

  public static String getExpansionState(){

    StringBuilder sb = new StringBuilder();

    for(int i =0 ; i < form.connectionList.getRowCount(); i++){
      TreePath tp = form.connectionList.getPathForRow(i);
      if(form.connectionList.isExpanded(i)){
        sb.append(tp.toString());
        sb.append(",");
      }
    }

    return sb.toString();

  }

  public static void setExpansionState(String s){

    for(int i = 0 ; i<form.connectionList.getRowCount(); i++){
      TreePath tp = form.connectionList.getPathForRow(i);
      if(s.contains(tp.toString() )){
        form.connectionList.expandRow(i);
      }
    }
  }
}