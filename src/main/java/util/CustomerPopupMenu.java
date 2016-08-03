package util;

import main.Connection;
import main.Customer;
import main.Start;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomerPopupMenu extends JPanel {

  public JPopupMenu popup;

  public CustomerPopupMenu(Customer customer, int x, int y) {

    popup = new JPopupMenu();
    ActionListener menuListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        System.out.println("Popup menu item [" + event.getActionCommand() + "] was pressed.");
        switch (event.getActionCommand()) {
          case "Delete":
            Start.customers.remove(customer);
            break;
          case "Rename":
            customer.setName(Start.showInputDialog("Enter a new name"));
            break;
          case "Duplicate":
            Start.customers.add(customer);
            break;
        }

        Start.saveConnections();
        Start.fillGUI();
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

    popup.setBounds(x, y, popup.getPreferredSize().width, popup.getPreferredSize().height);
    popup.show(Start.form.connectionList, x,y);
  }

  // An inner class to check whether mouse events are the popup trigger
  class MouseHoverListener extends MouseAdapter {
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
