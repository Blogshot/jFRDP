package main;

import util.CustomMenuBar;
import util.Customers;
import util.Dialogs.InputDialog;
import util.Dialogs.SecretInputDialog;
import util.listeners.CDropTargetAdapter;
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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static util.ConfigManager.*;
import static util.MD5Wrapper.createHash;

public class MainForm {
  public static boolean closeDebugAutomatically;
  public static int primaryScreen;
  private JButton btn_add;
  private JButton btn_add_grp;
  private JPanel root;
  public JTree connectionList;
  
  public static Customers customers = new Customers();
  
  public Connection currentEdit;
  
  public static String secrethash = "";
  public static String master = "";
  public static String standardKeyboardLayout = "NONE";
  public static boolean useStandardKeyboardLayout = false;
  public static boolean useDebug = false;
  
  public static String executableName;
  
  public static String configName = "config.json";
  public static String connectionsName = "connections.json";
  
  static JFrame frame;
  
  public static String xfreerdpHome = "";
  
  public MainForm() {
  
    // prepare binary executable
    try {
      prepareXFreeRDP();
      prepareConfigFiles();
    } catch (IOException e) {
      e.printStackTrace();
    }
  
    executableName = getExecutable();
    
    
    frame = new JFrame("jFRDP");
    frame.setContentPane(this.root);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(300, 600));
    frame.pack();
    
    // Begin setup
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    
    customers = loadCustomers();
    
    new CustomMenuBar().create(frame);
    
    frame.setVisible(true);
    
    loadConfig();
    
    if (master.equals("disabled")) {
      prepareJTree();
    } else {
      
      // first iteration
      boolean repeat = true;
      
      while (repeat) {
        
        secrethash = createHash(new SecretInputDialog().show("Enter your master-key to unlock passwords"));
        
        if (secrethash.equals(master)) {
          // Ding Ding Ding!
          repeat = false;
          prepareJTree();
        } else {
          // Ohhh...
          repeat = true;
          JOptionPane.showMessageDialog(new JFrame(), "Master-Password incorrect!");
        }
      }
    }
    
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
    
  }
  
  private void prepareXFreeRDP() throws IOException {
    String homeDir = System.getProperty("user.home");
  
    xfreerdpHome = homeDir + "/.xfreerdp/";
    
    File executable = new File(xfreerdpHome + executableName);
    
    // Only if executable is not available
    if (!executable.exists()) {
      
      //gets file from inside the JAR file as an input stream
      InputStream is = getClass().getResource("/xfreerdp").openStream();
      
      // mkdir for the hidden config-folder
      Files.createDirectories(Paths.get(homeDir + "/.xfreerdp"));
      
      // write binary to home folder
      writeToFile(executable, is);
      
      // set permissions to execute by owner
      executable.setExecutable(true);
    }
  }
  
  private void prepareConfigFiles() throws IOException {
    String homeDir = System.getProperty("user.home");
    
    File config = new File(xfreerdpHome + configName);
    File connections = new File(xfreerdpHome + connectionsName);
  
    // mkdir for the hidden config-folder
    Files.createDirectories(Paths.get(homeDir + "/.xfreerdp"));
    
    // Only if executable is not available
    if (!config.exists()) {
      
      //gets file from inside the JAR file as an input stream
      InputStream isConfig = getClass().getResource("/exampleConfig.json").openStream();
      
      // write to home folder
      writeToFile(config, isConfig);
    }
    
    if (!connections.exists()) {
      //gets file from inside the JAR file as an input stream
      InputStream isConnections = getClass().getResource("/exampleConnections.json").openStream();
      
      // write to home folder
      writeToFile(connections, isConnections);
    }
  }
  
  private void writeToFile(File file, InputStream isFile) throws IOException {
    // write to home folder
    OutputStream os = new FileOutputStream(file);
  
    byte[] b = new byte[2048];
    int length;
  
    while ((length = isFile.read(b)) != -1) {
      os.write(b, 0, length);
    }
  
    isFile.close();
    os.close();
  }
  
  private void prepareJTree() {
  
    // Do not show default nodes on startup
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    connectionList.setModel(new DefaultTreeModel(root));
    connectionList.setRootVisible(false);
  
    // Setup listeners, etc
    connectionList.addMouseListener(new jTreeMouseAdapter(this, connectionList));
    connectionList.setCellRenderer(new jTreeCellRenderer());
    connectionList.setDragEnabled(true);
    connectionList.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    connectionList.setDropMode(DropMode.USE_SELECTION);
    connectionList.setDropTarget(new DropTarget(connectionList, TransferHandler.MOVE, new CDropTargetAdapter(this, connectionList)));
  
  
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
  }
  
  private static String OS = System.getProperty("os.name").toLowerCase();
  
  private static enum EXECUTABLES = {
      xfreerdp, wfreerdp.exe
  }
  
  public static String getExecutable() {
    
    System.out.println(OS);
    
    if (isWindows()) {
      return EXECUTABLES.wfreerdp.exe;
    } else if (isMac()) {
      return EXECUTABLES.xfreerdp;
    } else if (isUnix()) {
      return EXECUTABLES.xfreerdp;
    } else {
      System.out.println("Your OS is not supported!");
    }
  }
  
  public static boolean isWindows() {
    
    return (OS.indexOf("win") >= 0);
    
  }
  
  public static boolean isMac() {
    
    return (OS.indexOf("mac") >= 0);
    
  }
  
  public static boolean isUnix() {
    
    return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    
  }
  
}
