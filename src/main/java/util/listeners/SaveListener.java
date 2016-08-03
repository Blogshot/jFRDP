package util.listeners;

import main.Connection;
import main.MainForm;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Field;

import static util.ConfigManager.saveConnections;

public class SaveListener extends FocusAdapter {

  private final String attribute;
  private final Connection currentEdit;
  private final MainForm form;

  public SaveListener(MainForm form, String attribute) {

    this.form = form;
    this.currentEdit = form.currentEdit;
    this.attribute = attribute;
  }

  @Override
  public void focusLost(FocusEvent e) {

    if (currentEdit != null) {

      Object o = currentEdit; // The object you want to inspect
      Class<?> c = o.getClass();

      try {
        Field attribute = c.getDeclaredField(this.attribute);

        attribute.setAccessible(true);

        try {
          String input = ((JTextField) e.getComponent()).getText();

          switch (this.attribute) {
            case "password":
              // Only encrypt if there is a difference
              attribute.set(currentEdit, currentEdit.encrypt(input));

              break;
            case "address":
              if (input.startsWith("http")) {
                currentEdit.type = Connection.Type.http;
              }
              attribute.set(currentEdit, ((JTextField) e.getComponent()).getText());
              break;
            default:
              attribute.set(currentEdit, ((JTextField) e.getComponent()).getText());
              break;
          }

          form.fillGUI();


        } catch (IllegalAccessException e1) {
          e1.printStackTrace();
        }
      } catch (NoSuchFieldException e1) {
        e1.printStackTrace();
      }

      saveConnections();
    }

    super.focusLost(e);
  }
}