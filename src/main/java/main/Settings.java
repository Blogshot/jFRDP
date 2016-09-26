package main;

import util.ConfigManager;
import util.Keyboards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static util.ConfigManager.saveConfig;


public class Settings {
  private JPanel root;
  private JTabbedPane tabView;
  private JComboBox drp_languages;
  private JCheckBox cb_useStandardKeyboardLayout;
  private JCheckBox cb_useDebug;
  
  public Settings() {
  
    JFrame frame = new JFrame("Settings");
    frame.setContentPane(this.root);
    frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    frame.setPreferredSize(new Dimension(500, 400));
    frame.pack();
  
    frame.setVisible(true);
  
  
    for (Keyboards.Keyboard keyboard : Keyboards.getKeyboards()) {
      drp_languages.addItem(keyboard);
    }
  
    // set saved entries
    cb_useDebug.setSelected(ConfigManager.loadConfig("useDebug").getAsBoolean());
    cb_useStandardKeyboardLayout.setSelected(ConfigManager.loadConfig("useStandardKeyboardLayout").getAsBoolean());
    drp_languages.setEnabled(ConfigManager.loadConfig("useStandardKeyboardLayout").getAsBoolean());
    drp_languages.setSelectedIndex(Keyboards.indexOf(ConfigManager.loadConfig("standardKeyboardLayout").getAsString()));
    
    drp_languages.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        MainForm.standardKeyboardLayout = ((Keyboards.Keyboard)drp_languages.getSelectedItem()).code;
        saveConfig();
      }
    });
  
    cb_useStandardKeyboardLayout.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        MainForm.useStandardKeyboardLayout = cb_useStandardKeyboardLayout.isSelected();
      
        drp_languages.setEnabled(cb_useStandardKeyboardLayout.isSelected());
      
        saveConfig();
      }
    });
  
    cb_useDebug.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        MainForm.useDebug = cb_useDebug.isSelected();
      
        saveConfig();
      }
    });
  }
}
