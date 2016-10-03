package main;

import util.Keyboards;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static util.ConfigManager.saveConnections;


public class EditForm {
  
  public JTextField txt_resolution;
  public JTextField txt_label;
  public JTextField txt_username;
  public JTextField txt_address;
  public JPasswordField txt_password;
  public JCheckBox cb_console;
  public JCheckBox cb_compression;
  public JTextField txt_domain;
  public JTextField txt_note;
  public JComboBox drp_keyboards;
  public JCheckBox cb_fitToScreen;
  private JPanel root;
  
  public EditForm(Connection connection, JTree connectionList) {
  
    JFrame frame;
    
    for (Keyboards.Keyboard keyboard : Keyboards.getKeyboards()) {
      drp_keyboards.addItem(keyboard);
    }
    
    txt_label.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        connection.label = txt_label.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void removeUpdate(DocumentEvent e) {
        connection.label = txt_label.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void changedUpdate(DocumentEvent e) {
        connection.label = txt_label.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
  
    txt_address.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        connection.address = txt_address.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void removeUpdate(DocumentEvent e) {
        connection.address = txt_address.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void changedUpdate(DocumentEvent e) {
        connection.address = txt_address.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
  
    txt_username.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        connection.username = txt_username.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void removeUpdate(DocumentEvent e) {
        connection.username = txt_username.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void changedUpdate(DocumentEvent e) {
        connection.username = txt_username.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
  
    // TODO deprecation
    txt_password.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        connection.password = connection.encrypt(txt_password.getText());
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void removeUpdate(DocumentEvent e) {
        connection.password = connection.encrypt(txt_password.getText());
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void changedUpdate(DocumentEvent e) {
        connection.password = connection.encrypt(txt_password.getText());
        saveConnections();
        connectionList.updateUI();
      }
    });
  
    txt_domain.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        connection.domain = txt_domain.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void removeUpdate(DocumentEvent e) {
        connection.domain = txt_domain.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void changedUpdate(DocumentEvent e) {
        connection.domain = txt_domain.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
  
    txt_resolution.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        connection.resolution = txt_resolution.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void removeUpdate(DocumentEvent e) {
        connection.resolution = txt_resolution.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void changedUpdate(DocumentEvent e) {
        connection.resolution = txt_resolution.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
  
    txt_note.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        connection.note = txt_note.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void removeUpdate(DocumentEvent e) {
        connection.note = txt_note.getText();
        saveConnections();
        connectionList.updateUI();
      }
    
      @Override
      public void changedUpdate(DocumentEvent e) {
        connection.note = txt_note.getText();
        saveConnections();
        connectionList.updateUI();
      }
    });
  
    cb_fitToScreen.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        txt_resolution.setEnabled(cb_fitToScreen.isSelected());
      
        connection.fitToScreen = cb_fitToScreen.isSelected();
        saveConnections();
      }
    });
  
    cb_console.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        connection.console = cb_compression.isSelected();
        saveConnections();
      }
    });
  
    cb_compression.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        connection.console = cb_compression.isSelected();
        saveConnections();
      }
    });
  
    drp_keyboards.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
      
        connection.keyboardCode = Keyboards.getCodeFromName(drp_keyboards.getSelectedItem().toString());
      
        saveConnections();
      }
    });
  
    frame = new JFrame("Editing " + connection.label);
    frame.setContentPane(this.root);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    
    // Place new window to the right of the main-window plus some space inbetween.
    frame.setBounds(MainForm.frame.getX() + MainForm.frame.getWidth() + 20, MainForm.frame.getY(), 400, 800);
    frame.pack();
    
    frame.setVisible(true);
  }
}
