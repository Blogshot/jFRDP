package util;

import main.Customer;
import main.MainForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static main.MainForm.customers;
import static util.ConfigManager.saveConnections;

public class CustomerPopupMenu extends JPanel {

  public CustomerPopupMenu(MainForm form, Customer customer, int x, int y) {

    JPopupMenu popup = new JPopupMenu();
    @SuppressWarnings("Convert2Lambda") ActionListener menuListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        System.out.println("Popup menu item [" + event.getActionCommand() + "] was pressed.");
        switch (event.getActionCommand()) {
          case "Delete":
            customers.remove(customer);
            break;
          case "Rename":
  
            String newName = JOptionPane.showInputDialog(null, "Enter a new name", customer.getName());
            
            if (!newName.equals("")) {
              customer.setName(newName);
            }
            break;
          case "Duplicate":
            customers.add(new Customer(customer));
            break;
        }

        saveConnections();
        form.connectionList.updateUI();
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
    popup.show(form.connectionList, x,y);
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
