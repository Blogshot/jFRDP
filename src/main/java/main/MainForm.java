package main;

import util.CustomMenuBar;
import util.Customers;
import util.listeners.CDropTargetAdapter;
import util.listeners.SaveListener;
import util.listeners.jTreeMouseAdapter;
import util.renderer.jTreeCellRenderer;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.*;

import static main.Start.showSecretInputDialog;
import static util.ConfigManager.*;
import static util.MD5Wrapper.createHash;

public class MainForm {
  public JTree connectionList;
  public JTextField resolution;
  public JTextField label;
  public JTextField user;
  public JTextField address;
  public JPasswordField pass;
  public JCheckBox console;
  public JCheckBox compression;
  private JPanel root;
  public JTextField domain;
  private JButton btn_masterkey;
  private JButton btn_add;
  private JButton btn_add_grp;
  public JTextField txt_note;

  public static Customers customers = new Customers();

  public Connection currentEdit;

  public static String secrethash = "";
  public static String master = "";

  public MainForm() {

    // Do not show default nodes on startup
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    connectionList.setModel(new DefaultTreeModel(root));
    connectionList.setRootVisible(false);

    // Begin setup
    System.setProperty("apple.laf.useScreenMenuBar", "true");

    customers = loadCustomers();

    JFrame frame = new JFrame("jFRDP");
    frame.setContentPane(this.root);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(800, 600));
    frame.pack();

    new CustomMenuBar().create(frame);

    frame.setVisible(true);

    loadConfig();

    secrethash = createHash(showSecretInputDialog());

    if (secrethash.equals(master)) {
      fillGUI();
    } else {
      JOptionPane.showMessageDialog(new JFrame(), "Master-Password incorrect!");
    }


    label.addFocusListener(new SaveListener(this, "label"));
    address.addFocusListener(new SaveListener(this, "address"));
    user.addFocusListener(new SaveListener(this, "username"));
    pass.addFocusListener(new SaveListener(this, "password"));
    domain.addFocusListener(new SaveListener(this, "domain"));
    console.addFocusListener(new SaveListener(this, "console"));
    compression.addFocusListener(new SaveListener(this, "compression"));
    txt_note.addFocusListener(new SaveListener(this, "note"));


    btn_masterkey.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        setNewMaster(showInputDialog("Enter a master-key"));

        saveConfig();
      }
    });

    btn_add.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

        Object selection = connectionList.getLastSelectedPathComponent();


        Customer customer;

        if (selection != null) {

          Object o = ((DefaultMutableTreeNode)connectionList.getLastSelectedPathComponent()).getUserObject();

          if (o instanceof Customer) {
            customer = (Customer)o;
          } else { // Is instance of Connection -> get Parent as Customer
            Object parent = ((DefaultMutableTreeNode)connectionList.getSelectionPath().getParentPath().getLastPathComponent()).getUserObject();
            customer = (Customer)parent;
          }
        } else {

          // Add if not existent
          if (!customers.contains("Default")) {
            customer = new Customer("Default");
            customers.add(customer);
          } else {
            customer = customers.get("Default");
          }
        }

        System.out.println("Adding new Connection to " + customer.getName());
        customer.addConnection(new Connection());

        saveConnections();
        fillGUI();

        super.mouseClicked(e);
      }
    });

    btn_add_grp.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

        customers.add(new Customer(showInputDialog("Enter a name for the new group")));

        saveConnections();
        fillGUI();

        super.mouseClicked(e);
      }
    });

    // Setup connectionList
    connectionList.addMouseListener(new jTreeMouseAdapter(this, connectionList));

    connectionList.setCellRenderer(new jTreeCellRenderer(this));

    connectionList.setDragEnabled(true);
    connectionList.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    connectionList.setDropMode(DropMode.USE_SELECTION);
    connectionList.setDropTarget(new DropTarget(connectionList, TransferHandler.MOVE, new CDropTargetAdapter(this, connectionList)));

  }

  private static String showInputDialog(String text) {
    // prompt the user to enter their master-key
    // get the user's input. note that if they press Cancel, 'secret' will be null
    return JOptionPane.showInputDialog(new JFrame(), text);
  }

  private String getExpansionState(){

    StringBuilder sb = new StringBuilder();

    for(int i =0 ; i < connectionList.getRowCount(); i++){
      TreePath tp = connectionList.getPathForRow(i);
      if(connectionList.isExpanded(i)){
        sb.append(tp.toString());
        sb.append(",");
      }
    }
    return sb.toString();
  }

  private void setExpansionState(String s){

    for(int i = 0 ; i<connectionList.getRowCount(); i++){
      TreePath tp = connectionList.getPathForRow(i);
      if(s.contains(tp.toString() )){
        connectionList.expandRow(i);
      }
    }
  }

  public void fillGUI() {

    String expansionState = getExpansionState();

    DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    connectionList.setModel(new DefaultTreeModel(root));


    for (Customer customer : customers) {

      DefaultMutableTreeNode customerNode = new DefaultMutableTreeNode(customer);

      for (Connection connection : customer.connections) {

        DefaultMutableTreeNode conn = new DefaultMutableTreeNode(connection);
        conn.setAllowsChildren(false);

        customerNode.add(conn);
      }

      root.add(customerNode);
    }

    connectionList.setRootVisible(false);
    connectionList.setShowsRootHandles(true);
    connectionList.expandPath(new TreePath(root));

    setExpansionState(expansionState);

    connectionList.repaint();

  }

}
