package main;

import util.CustomMenuBar;
import util.Customers;
import util.Dialogs.InputDialog;
import util.Keyboards;
import util.listeners.CDropTargetAdapter;
import util.listeners.SaveListener;
import util.listeners.jTreeMouseAdapter;
import util.renderer.jTreeCellRenderer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static main.Start.showSecretInputDialog;
import static util.ConfigManager.*;
import static util.MD5Wrapper.createHash;

public class MainForm {
  public JTree connectionList;
  public JTextField txt_resolution;
  public JTextField txt_label;
  public JTextField txt_username;
  public JTextField txt_address;
  public JPasswordField txt_password;
  public JCheckBox cb_console;
  public JCheckBox cb_compression;
  private JPanel root;
  public JTextField txt_domain;
  private JButton btn_masterkey;
  private JButton btn_add;
  private JButton btn_add_grp;
  public JTextField txt_note;
  public JComboBox drp_keyboards;
  
  public static Customers customers = new Customers();
  
  public Connection currentEdit;
  
  public static String secrethash = "";
  public static String master = "";
  
  public MainForm() {
    
    for (Keyboards.Keyboard keyboard : Keyboards.getKeyboards()) {
      drp_keyboards.addItem(keyboard);
    }
    
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
    
    if (master.equals("disabled")) {
      fillGUI();
    } else {
      secrethash = createHash(showSecretInputDialog());
  
      if (secrethash.equals(master)) {
        fillGUI();
      } else {
        JOptionPane.showMessageDialog(new JFrame(), "Master-Password incorrect!");
      }
    }
    
    txt_label.addFocusListener(new SaveListener(this, "label"));
    txt_address.addFocusListener(new SaveListener(this, "address"));
    txt_username.addFocusListener(new SaveListener(this, "username"));
    txt_password.addFocusListener(new SaveListener(this, "password"));
    txt_domain.addFocusListener(new SaveListener(this, "domain"));
    txt_resolution.addFocusListener(new SaveListener(this, "resolution"));
    cb_console.addFocusListener(new SaveListener(this, "console"));
    cb_compression.addFocusListener(new SaveListener(this, "compression"));
    txt_note.addFocusListener(new SaveListener(this, "note"));
    drp_keyboards.addFocusListener(new SaveListener(this, "keyboardCode"));
    
    
    btn_masterkey.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        
        setNewMaster(new InputDialog().show("Enter a master-key (leave blank to disable encryption)"));
        
        saveConfig();
      }
    });
    
    btn_add.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        
        Object selection = connectionList.getLastSelectedPathComponent();
        
        
        Customer customer;
        
        if (selection != null) {
          
          Object o = ((DefaultMutableTreeNode) connectionList.getLastSelectedPathComponent()).getUserObject();
          
          if (o instanceof Customer) {
            customer = (Customer) o;
          } else { // Is instance of Connection -> get Parent as Customer
            Object parent = ((DefaultMutableTreeNode) connectionList.getSelectionPath().getParentPath().getLastPathComponent()).getUserObject();
            customer = (Customer) parent;
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
        
        customers.add(new Customer(new InputDialog().show("Enter a name for the new group")));
  
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
    
  private String getExpansionState() {
    
    StringBuilder sb = new StringBuilder();
    
    for (int i = 0; i < connectionList.getRowCount(); i++) {
      TreePath tp = connectionList.getPathForRow(i);
      if (connectionList.isExpanded(i)) {
        sb.append(tp.toString());
        sb.append(",");
      }
    }
    return sb.toString();
  }
  
  private void setExpansionState(String s) {
    
    for (int i = 0; i < connectionList.getRowCount(); i++) {
      TreePath tp = connectionList.getPathForRow(i);
      if (s.contains(tp.toString())) {
        connectionList.expandRow(i);
      }
    }
  }
  
  public void fillGUI() {
    
    String expansionState = getExpansionState();
    TreePath selection = connectionList.getSelectionPath();
    
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
    connectionList.setSelectionPath(selection);
    
    connectionList.repaint();
    
  }
  
}
