package main;

import javax.swing.*;

public class Start {
  
  public static void main(String[] args) {
    new MainForm();
  }

  public static String showSecretInputDialog() {
    return JOptionPane.showInputDialog(new JFrame(), "Enter your master-key to unlock passwords");
  }
}