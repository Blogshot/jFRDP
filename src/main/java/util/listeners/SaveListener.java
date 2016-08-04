package util.listeners;

import main.Connection;
import main.MainForm;
import util.Keyboards;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Field;

import static util.ConfigManager.saveConnections;

public class SaveListener extends FocusAdapter {

  private final String attribute;
  private final MainForm form;

  public SaveListener(MainForm form, String attribute) {

    this.form = form;
    this.attribute = attribute;
  }

  @Override
  public void focusLost(FocusEvent e) {

    if (form.currentEdit != null) {

      Object o = form.currentEdit; // The object you want to inspect
      Class<?> c = o.getClass();

      try {
        Field attribute = c.getDeclaredField(this.attribute);

        attribute.setAccessible(true);

        try {
          String input;

          switch (this.attribute) {
            case "keyboardCode":
              input = ((Keyboards.Keyboard)form.drp_keyboards.getSelectedItem()).code;
              // Only encrypt if there is a difference
              attribute.set(form.currentEdit, input);

              break;
            case "password":
              input = ((JTextField) e.getComponent()).getText();
              // Only encrypt if there is a difference
              attribute.set(form.currentEdit, form.currentEdit.encrypt(input));

              break;
            case "address":
              input = ((JTextField) e.getComponent()).getText();
              if (input.startsWith("http")) {
                form.currentEdit.type = Connection.Type.http;
              }
              attribute.set(form.currentEdit, input);
              break;
            default:
              attribute.set(form.currentEdit, ((JTextField) e.getComponent()).getText());
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