package main;

import util.CustomMenuBar;
import util.Customers;
import util.Dialogs.InputDialog;
import util.Keyboards;
import util.listeners.CDropTargetAdapter;
import util.listeners.jTreeMouseAdapter;
import util.renderer.jTreeCellRenderer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static main.Start.form;
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
  public JPanel root;
  public JTextField txt_domain;
  private JButton btn_masterkey;
  private JButton btn_add;
  private JButton btn_add_grp;
  public JTextField txt_note;
  public JComboBox drp_keyboards;
  public JCheckBox cb_fitToScreen;
  
  public static Customers customers = new Customers();
  
  public Connection currentEdit;
  
  public static String secrethash = "";
  public static String master = "";
  public static String standardKeyboardLayout = "NONE";
  public static boolean useStandardKeyboardLayout = false;
  public static boolean useDebug = false;
  
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
    
    txt_label.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        currentEdit.label = txt_label.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void removeUpdate(DocumentEvent e) {
        currentEdit.label = txt_label.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void changedUpdate(DocumentEvent e) {
        currentEdit.label = txt_label.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
    
    txt_address.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        currentEdit.address = txt_address.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void removeUpdate(DocumentEvent e) {
        currentEdit.address = txt_address.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void changedUpdate(DocumentEvent e) {
        currentEdit.address = txt_address.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
    
    txt_username.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        currentEdit.username = txt_username.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void removeUpdate(DocumentEvent e) {
        currentEdit.username = txt_username.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void changedUpdate(DocumentEvent e) {
        currentEdit.username = txt_username.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
    
    // TODO deprecation
    txt_password.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        currentEdit.password = currentEdit.encrypt(txt_password.getText());
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void removeUpdate(DocumentEvent e) {
        currentEdit.password = currentEdit.encrypt(txt_password.getText());
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void changedUpdate(DocumentEvent e) {
        currentEdit.password = currentEdit.encrypt(txt_password.getText());
        saveConnections();
        connectionList.updateUI();
      }
    });
    
    txt_domain.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        currentEdit.domain = txt_domain.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void removeUpdate(DocumentEvent e) {
        currentEdit.domain = txt_domain.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void changedUpdate(DocumentEvent e) {
        currentEdit.domain = txt_domain.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
    
    txt_resolution.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        currentEdit.resolution = txt_resolution.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void removeUpdate(DocumentEvent e) {
        currentEdit.resolution = txt_resolution.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void changedUpdate(DocumentEvent e) {
        currentEdit.resolution = txt_resolution.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
    
    txt_note.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        currentEdit.note = txt_note.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void removeUpdate(DocumentEvent e) {
        currentEdit.note = txt_note.getText();
        saveConnections();
        connectionList.updateUI();
      }
      
      @Override
      public void changedUpdate(DocumentEvent e) {
        currentEdit.note = txt_note.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
  
    cb_fitToScreen.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        txt_resolution.setEnabled(cb_fitToScreen.isSelected());
        
        currentEdit.fitToScreen = cb_fitToScreen.isSelected();
        saveConnections();
      }
    });
    
    cb_console.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        currentEdit.console = cb_compression.isSelected();
        saveConnections();
      }
    });
    
    cb_compression.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        currentEdit.console = cb_compression.isSelected();
        saveConnections();
      }
    });
    
    drp_keyboards.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
  
        form.currentEdit.keyboardCode = Keyboards.getCodeFromName(drp_keyboards.getSelectedItem().toString());
        
        System.out.println(form.currentEdit.keyboardCode);
        
        saveConnections();
      }
    });
    
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
        
        super.mouseClicked(e);
      }
    });
    
    btn_add_grp.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        
        customers.add(new Customer(new InputDialog().show("Enter a name for the new group")));
        
        saveConnections();
        
        super.mouseClicked(e);
      }
    });
    
    // Setup connectionList
    connectionList.addMouseListener(new jTreeMouseAdapter(this, connectionList));
    
    connectionList.setCellRenderer(new jTreeCellRenderer());
    
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
    
    if (selection != null) {
      System.out.println(selection.getPath());
    }
    
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
    
    if (connectionList.getSelectionPath() != null) {
      Object o = connectionList.getSelectionPath().getLastPathComponent();
      if (o instanceof Connection) {
        ((Connection) o).startEdit(this);
      }
    }
    connectionList.repaint();
    
  }
}
