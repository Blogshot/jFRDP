package util.Dialogs;

import javax.swing.*;

public class SecretInputDialog extends JOptionPane{
    
  public String show(String message) {
    // Pressing cancel generates null
    return JOptionPane.showInputDialog(new JFrame(), message);

  }
}