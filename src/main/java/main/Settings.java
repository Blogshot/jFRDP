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
  
    // set saved entry
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
        saveConfig();
      }
    });
  }
}
