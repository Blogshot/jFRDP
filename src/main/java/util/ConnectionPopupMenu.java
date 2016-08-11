package util;

import main.Connection;
import main.Customer;
import main.MainForm;
import main.Start;
import util.Dialogs.InputDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static util.ConfigManager.saveConnections;

public class ConnectionPopupMenu extends JPanel {

  public ConnectionPopupMenu(MainForm form, Customer customer, Connection connection, int x, int y) {

    JPopupMenu popup = new JPopupMenu();
    @SuppressWarnings("Convert2Lambda") ActionListener menuListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        System.out.println("Popup menu item [" + event.getActionCommand() + "] was pressed.");
        switch (event.getActionCommand()) {
          case "Delete":
            customer.removeConnection(connection);
            break;
          case "Rename":
            connection.label = new InputDialog().show("Enter a new name");
            break;
          case "Duplicate":
            customer.addConnection(new Connection(connection));
            break;
          case "Edit":
            connection.startEdit(Start.form);
            break;
        }

        saveConnections();
        form.fillGUI();
      }
    };

    JMenuItem item;

    MouseHoverListener hover = new MouseHoverListener();

    popup.add(item = new JMenuItem("Delete"));
    item.setHorizontalTextPosition(JMenuItem.RIGHT);
    item.addActionListener(menuListener);
    item.addMouseListener(hover);

    popup.add(item = new JMenuItem("Rename"));
    item.setHorizontalTextPosition(JMenuItem.RIGHT);
    item.addActionListener(menuListener);
    item.addMouseListener(hover);

    popup.add(item = new JMenuItem("Duplicate"));
    item.setHorizontalTextPosition(JMenuItem.RIGHT);
    item.addActionListener(menuListener);
    item.addMouseListener(hover);

    popup.add(item = new JMenuItem("Edit"));
    item.setHorizontalTextPosition(JMenuItem.RIGHT);
    item.addActionListener(menuListener);
    item.addMouseListener(hover);


    popup.setBounds(x, y, popup.getPreferredSize().width, popup.getPreferredSize().height);
    popup.show(Start.form.connectionList, x,y);
  }

  // An inner class to check whether mouse events are the popup trigger
  private class MouseHoverListener extends MouseAdapter {
    @Override
    public void mouseEntered(MouseEvent e) {
      e.getComponent().setBackground(Color.blue);
    }
    @Override
    public void mouseExited(MouseEvent e) {
      e.getComponent().setBackground(null);
    }
  }
}
