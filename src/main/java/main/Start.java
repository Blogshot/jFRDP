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
}