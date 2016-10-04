package main;

import util.ConfigManager;
import util.CustomScreen;
import util.Dialogs.InputDialog;
import util.Keyboards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static util.ConfigManager.saveConfig;
import static util.ConfigManager.setNewMaster;


public class Settings {
  private JPanel root;
  private JTabbedPane tabView;
  private JComboBox drp_languages;
  private JCheckBox cb_useStandardKeyboardLayout;
  private JCheckBox cb_useDebug;
  private JButton btn_masterkey;
  private JCheckBox cb_closeAutomatically;
  private JComboBox drp_primaryScreen;
  
  public Settings() {
  
    JFrame frame = new JFrame("Settings");
    frame.setContentPane(this.root);
    frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    frame.setPreferredSize(new Dimension(500, 400));
    frame.pack();
  
    frame.setVisible(true);
  
    initGeneral();
    initSecurity();
  }
  
  private void initGeneral() {
    for (Keyboards.Keyboard keyboard : Keyboards.getKeyboards()) {
      drp_languages.addItem(keyboard);
    }
  
    drp_primaryScreen.addItem("Auto");
    for (CustomScreen screen : getMonitors()) {
      System.out.println(screen);
      drp_primaryScreen.addItem(screen);
    }
    
    // set saved entries
    cb_useDebug.setSelected(ConfigManager.loadConfig("useDebug").getAsBoolean());
    cb_closeAutomatically.setSelected(ConfigManager.loadConfig("closeDebugAutomatically").getAsBoolean());
    cb_useStandardKeyboardLayout.setSelected(ConfigManager.loadConfig("useStandardKeyboardLayout").getAsBoolean());
    drp_languages.setEnabled(ConfigManager.loadConfig("useStandardKeyboardLayout").getAsBoolean());
    drp_languages.setSelectedIndex(Keyboards.indexOf(ConfigManager.loadConfig("standardKeyboardLayout").getAsString()));
    drp_primaryScreen.setSelectedIndex(ConfigManager.loadConfig("primaryScreen").getAsInt());
    
    drp_languages.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        MainForm.standardKeyboardLayout = ((Keyboards.Keyboard)drp_languages.getSelectedItem()).code;
        saveConfig();
      }
    });
    
    drp_primaryScreen.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        System.out.println(drp_primaryScreen.getSelectedIndex() + "");
        MainForm.primaryScreen = drp_primaryScreen.getSelectedIndex();
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
  
        cb_closeAutomatically.setEnabled(cb_useDebug.isSelected());
        
        saveConfig();
      }
    });
  
    cb_closeAutomatically.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        MainForm.closeDebugAutomatically = cb_closeAutomatically.isSelected();
      
        saveConfig();
      }
    });
  }
  
  private void initSecurity() {
    btn_masterkey.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
      
        setNewMaster(new InputDialog().show("Enter a master-key (leave blank to disable encryption)"));
      
        saveConfig();
      }
    });
  }
  
  private ArrayList<CustomScreen> getMonitors() {
    
    ArrayList<CustomScreen> result = new ArrayList<>();
    
    GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] devices = g.getScreenDevices();
        
    for (int i=0; i< devices.length; i++) {
      GraphicsDevice device = devices[i];
      
      result.add(new CustomScreen(
          i,
          device.getDisplayMode().getWidth(),
          device.getDisplayMode().getHeight()
      ));
    }
    
    return result;
    
  }
}
