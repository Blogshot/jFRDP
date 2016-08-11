package util;

import util.Dialogs.InputDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static util.ConfigManager.saveConfig;
import static util.ConfigManager.saveConnections;

public class CustomMenuBar {

  public CustomMenuBar() {

  }

  public void create(JFrame frame) {
    JMenuBar menuBar = new JMenuBar();
    //Hinzufügen von Menüs
    JMenu menuFile =
        new JMenu("Datei");
    JMenu menuEdit =
        new JMenu("Bearbeiten");
    JMenu menuHelp =
        new JMenu("Hilfe");

    menuBar.add(menuFile);
    menuBar.add(menuEdit);
    menuBar.add(menuHelp);

    //Hinzufügen von Untermenüs
    JMenu menuFileNew = new JMenu("Neu");

    menuFile.add(menuFileNew);

    //Hinzufügen von Menüeinträgen in das Dateimenü
    JMenuItem menuItemFileSave = new JMenuItem(new AbstractAction("Save") {
      public void actionPerformed(ActionEvent e) {
        saveConfig();
        saveConnections();
      }
    });
    JMenuItem menuItemFileSaveAs = new JMenuItem(new AbstractAction("Save as...") {
      public void actionPerformed(ActionEvent e) {
        saveConfig(new InputDialog().show("Enter file name for the config"));
        saveConnections(new InputDialog().show("Enter file name for the connections"));
      }
    });

    menuFile.add(menuItemFileSave);
    menuFile.add(menuItemFileSaveAs);


    frame.setJMenuBar(menuBar);
  }
}
