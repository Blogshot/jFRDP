package main;

import javax.swing.*;

public class Start {

  public static MainForm form;


  public static void main(String[] args) {
    form = new MainForm();
  }

  public static String showSecretInputDialog() {
    return JOptionPane.showInputDialog(new JFrame(), "Enter your master-key to unlock passwords");
  }

  public static String showInputDialog(String text) {

    // prompt the user to enter their master-key

    // get the user's input. note that if they press Cancel, 'secret' will be null
    return JOptionPane.showInputDialog(new JFrame(), text);
  }


}