package main;

import util.ConnectionPopupMenu;
import util.CustomerPopupMenu;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.net.URI;

public class MainForm {
  public JTree connectionList;
  public JTextField resolution;
  public JTextField label;
  public JTextField user;
  public JTextField address;
  public JPasswordField pass;
  public JCheckBox console;
  public JCheckBox compression;
  public JPanel root;
  public JTextField domain;
  private JButton btn_masterkey;
  private JButton btn_add;
  private JButton btn_add_grp;
  public JTextField txt_note;

  public Connection currentEdit;

  public MainForm() {

    // Do not show default nodes on startup
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    connectionList.setModel(new DefaultTreeModel(root));
    connectionList.setRootVisible(false);

    label.addFocusListener(new saveListener("label"));
    address.addFocusListener(new saveListener("address"));
    user.addFocusListener(new saveListener("username"));
    pass.addFocusListener(new saveListener("password"));
    domain.addFocusListener(new saveListener("domain"));
    console.addFocusListener(new saveListener("console"));
    compression.addFocusListener(new saveListener("compression"));
    txt_note.addFocusListener(new saveListener("note"));


    btn_masterkey.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Start.setNewMaster(showInputDialog("Enter a master-key"));

        Start.saveConfig();
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
          if (!Start.customers.contains("Default")) {
            customer = new Customer("Default");
            Start.customers.add(customer);
          } else {
            customer = Start.customers.get("Default");
          }
        }

        System.out.println("Adding new Connection to " + customer.getName());
        customer.addConnection(new Connection());

        Start.saveConnections();
        Start.fillGUI();

        super.mouseClicked(e);
      }
    });

    btn_add_grp.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

        Start.customers.add(new Customer(showInputDialog("Enter a name for the new group")));

        Start.saveConnections();
        Start.fillGUI();

        super.mouseClicked(e);
      }
    });


    ImageIcon rdpIcon = new ImageIcon(MainForm.class.getResource("/rdp.png"));
    ImageIcon httpIcon = new ImageIcon(MainForm.class.getResource("/http.png"));

    connectionList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent event) {

        Connection connection;
        Customer customer;

        connectionList.setSelectionPath(connectionList.getPathForLocation(event.getX(), event.getY()));

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) connectionList.getLastSelectedPathComponent();


        if (node != null) {

          if (node.getUserObject() instanceof Connection) {
            connection = (Connection) node.getUserObject();
            customer = (Customer) ((DefaultMutableTreeNode) node.getParent()).getUserObject();

            if (SwingUtilities.isRightMouseButton(event)) {
              System.out.println("Right Mouse");
              new ConnectionPopupMenu(customer, connection, event.getX(), event.getY()).setVisible(true);
              return;
            }

            if (event.getClickCount() == 2) {

              if (connection.type == Connection.Type.rdp) {
                connection.openRDP();
              } else {
                try {
                  Desktop.getDesktop().browse(new URI(connection.address));
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
              return;
            }
            if (event.getClickCount() == 1) {
              connection.startEdit(MainForm.this);
              return;
            }
          } else if (node.getUserObject() instanceof Customer) {

            customer = (Customer)node.getUserObject();

            if (SwingUtilities.isRightMouseButton(event)) {
              System.out.println("Right Mouse");
              new CustomerPopupMenu(customer, event.getX(), event.getY()).setVisible(true);
              return;
            }
          }
        }
      }
    });


    connectionList.setCellRenderer(new DefaultTreeCellRenderer() {

      @Override
      public Component getTreeCellRendererComponent(JTree tree,
                                                    Object value, boolean selected, boolean expanded,
                                                    boolean isLeaf, int row, boolean focused) {
        Component c = super.getTreeCellRendererComponent(tree, value,
            selected, expanded, isLeaf, row, focused);

        Object o = ((DefaultMutableTreeNode)value).getUserObject();

        if (isLeaf && o instanceof Connection) {

          Connection conn = ((Connection)((DefaultMutableTreeNode)value).getUserObject());

          if (conn.type == Connection.Type.rdp) {
            setIcon(rdpIcon);
          }
          if (conn.type == Connection.Type.http) {
            setIcon(httpIcon);
          }
        }


        return c;
      }
    });

    connectionList.setDragEnabled(true);
    connectionList.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    connectionList.setDropMode(DropMode.USE_SELECTION);
    connectionList.setDropTarget(new DropTarget(connectionList, TransferHandler.MOVE,
        new DropTargetAdapter() {
          @Override
          public void drop(DropTargetDropEvent dtde) {

            TreePath selectionPath = connectionList.getSelectionPath();
            TreePath sourcePath = selectionPath.getParentPath();

            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectionPath
                .getLastPathComponent();

            Point dropLocation = dtde.getLocation();
            TreePath targetPath = connectionList.getClosestPathForLocation(
                dropLocation.x, dropLocation.y);

            System.out.println("###################");

            System.out.println("srcPath: " + sourcePath);
            System.out.println("targetPath: " + targetPath);
            System.out.println("selectedNode: " + selectedNode);


            DefaultMutableTreeNode targetParentNode = (DefaultMutableTreeNode) targetPath
                .getLastPathComponent();
            DefaultMutableTreeNode sourceParentNode = (DefaultMutableTreeNode) sourcePath
                .getLastPathComponent();

            if (isDropAllowed(sourceParentNode, targetParentNode, selectedNode)) {
              System.out.println("drop accept");
              sourceParentNode.remove(selectedNode);
              targetParentNode.add(selectedNode);

              ((Customer) targetParentNode.getUserObject()).connections.add((Connection) selectedNode.getUserObject());
              ((Customer) sourceParentNode.getUserObject()).connections.remove(selectedNode.getUserObject());

              dtde.dropComplete(true);
              connectionList.updateUI();

              Start.saveConnections();
              Start.fillGUI();

            } else {
              System.out.println("drop: reject");
              dtde.rejectDrop();
              dtde.dropComplete(false);
            }
          }

          private boolean isDropAllowed(DefaultMutableTreeNode sourceNode,
                                        DefaultMutableTreeNode targetNode,
                                        DefaultMutableTreeNode selectedNode) {

            return sourceNode.isLeaf() && !targetNode.isLeaf() && selectedNode.isLeaf();
          }

        }));

  }

  private static String showInputDialog(String text) {

    // prompt the user to enter their master-key

    // get the user's input. note that if they press Cancel, 'secret' will be null
    return JOptionPane.showInputDialog(new JFrame(), text);
  }

  private class saveListener extends FocusAdapter {

    String attribute;

    public saveListener(String attribute) {

      this.attribute = attribute;
    }

    @Override
    public void focusLost(FocusEvent e) {

      if (currentEdit != null) {

        Object o = currentEdit; // The object you want to inspect
        Class<?> c = o.getClass();

        try {
          Field attribute = c.getDeclaredField(this.attribute);

          attribute.setAccessible(true);

          try {
            String input = ((JTextField) e.getComponent()).getText();

            if (this.attribute.equals("password")) {
              // Only encrypt if there is a difference
              attribute.set(currentEdit, currentEdit.encrypt(input));

            } else if (this.attribute.equals("address")) {
              if (input.startsWith("http")) {
                currentEdit.type = Connection.Type.http;
              }
              attribute.set(currentEdit, ((JTextField) e.getComponent()).getText());
            } else {
              attribute.set(currentEdit, ((JTextField) e.getComponent()).getText());
            }

            Start.fillGUI();


          } catch (IllegalAccessException e1) {
            e1.printStackTrace();
          }
        } catch (NoSuchFieldException e1) {
          e1.printStackTrace();
        }

        Start.saveConnections();
      }

      super.focusLost(e);
    }
  }

}
