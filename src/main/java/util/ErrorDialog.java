package util;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDialog extends JOptionPane {

  public ErrorDialog(String error) {
    show(error);
  }

  public ErrorDialog(Exception e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);

    show(sw.toString());
  }

  private void show(String message) {
    setMessage(message);
    setMessageType(JOptionPane.INFORMATION_MESSAGE);
    JDialog dialog = createDialog(null, "Width 100");
    dialog.setVisible(true);
  }

  public int getMaxCharactersPerLineCount() {
    return 150;
  }
}
