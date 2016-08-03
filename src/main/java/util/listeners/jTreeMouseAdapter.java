package util.listeners;

import main.Connection;
import main.Customer;
import main.MainForm;
import util.ConnectionPopupMenu;
import util.CustomerPopupMenu;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class jTreeMouseAdapter extends MouseAdapter {

  private final JTree connectionList;
  private final MainForm form;

  public jTreeMouseAdapter(MainForm form, JTree connectionList) {
    this.form = form;
    this.connectionList = connectionList;
  }

  @Override
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
          new ConnectionPopupMenu(form, customer, connection, event.getX(), event.getY()).setVisible(true);
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
          connection.startEdit(form);
        }
      } else if (node.getUserObject() instanceof Customer) {

        customer = (Customer) node.getUserObject();

        if (SwingUtilities.isRightMouseButton(event)) {
          System.out.println("Right Mouse");
          new CustomerPopupMenu(form, customer, event.getX(), event.getY()).setVisible(true);
        }
      }
    }
  }
}
