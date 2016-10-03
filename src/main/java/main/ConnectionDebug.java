package main;

import javax.swing.*;
import java.awt.*;


public class ConnectionDebug {
  public JTextArea debugger;
  private JPanel root;
  public JFrame frame;
  
  public ConnectionDebug(String name) {
    frame = new JFrame(name);
    frame.setContentPane(this.root);
    frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    frame.setPreferredSize(new Dimension(600, 100));
    frame.pack();
    
    frame.setVisible(true);
  }
}
