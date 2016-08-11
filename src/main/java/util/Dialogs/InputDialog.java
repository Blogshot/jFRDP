package util.Dialogs;

import javax.swing.*;

public class InputDialog extends JOptionPane{

  public InputDialog() {
  }
    
  public String show(String message) {
    // Pressing cancel generates null
    String result = showInputDialog(new JFrame(), message);
  
    if (result != null) {
      return result;
    } else {
      return "";
    }
  }
}