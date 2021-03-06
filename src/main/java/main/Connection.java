package main;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import util.ConfigManager;
import util.Dialogs.ErrorDialog;
import util.Dialogs.InputDialog;
import util.Keyboards;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.ArrayList;

import static main.Connection.Type.rdp;
import static main.MainForm.*;

public class Connection {
  
  public boolean console = false;
  public boolean compression = false;
  public boolean fitToScreen = true;
  
  public String label = "";
  public String address = "";
  public String username = "";
  public String domain = "";
  public String password = "";
  public String resolution = "1280x720";
  public String note = "";
  private ArrayList<Drive> drives = new ArrayList<>();
  public String keyboardCode = "";
  public Type type = rdp;
  
  public enum Type {
    rdp, http, ftp
  }
  
  public Connection() {
  }
  
  public Connection(Connection another) {
    this.label = another.label;
    this.address = another.address;
    this.username = another.username;
    this.password = another.password;
    this.domain = another.domain;
    this.resolution = another.resolution;
    this.compression = another.compression;
    this.console = another.console;
    this.note = another.note;
    this.type = another.type;
    this.drives = another.drives;
    this.keyboardCode = another.keyboardCode;
  }
  
  public void openRDP() {
    // Open a new RDP-Connection
    ArrayList<String> parameter = new ArrayList<>();
    
    // executable itself
    parameter.add(MainForm.xfreerdpHome + executableName);
    
    // parameters
    if (this.console) {
      parameter.add("/admin");
    }
    if (this.compression) {
      parameter.add("/cb_compression-level:0");
    }
    if (!this.domain.equals("")) {
      parameter.add("/d:" + this.domain);
    }
    if (!this.username.equals("")) {
      parameter.add("/u:" + this.username);
    }
    if (!this.password.equals("")) {
      parameter.add("/p:" + decrypt(this.password));
    } else {
      parameter.add("/p:" + new InputDialog().show("Please enter a password."));
    }
    if (!this.fitToScreen && !this.resolution.equals("")) {
      parameter.add("/size:" + this.resolution);
    } else if (this.fitToScreen) {
      int height = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height - MainForm.frame.getInsets().top; // Title bar
      int width = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
      parameter.add("/size:" + width + "x" + height);
    }
    
    if (!this.keyboardCode.equals("")) {
      parameter.add("/kbd:" + this.keyboardCode);
    } else if (useStandardKeyboardLayout) {
      parameter.add("/kbd:" + standardKeyboardLayout);
    }
    
    // TODO Broken on current build, binary file has bugs. Will always use default screen
    if (MainForm.primaryScreen == 0) {
      System.out.println(getCurrentMonitor());
      parameter.add("/monitors:" + getCurrentMonitor());
    } else {
      parameter.add("/monitors:" + primaryScreen);
    }
    
    parameter.add("/cert-ignore");
    parameter.add("+clipboard");
    parameter.add("/drive:Home," + System.getenv("HOME"));
    parameter.add("/t:" + this.label);
    parameter.add("/v:" + this.address);
    
    
    // Convert Arraylist<String> to String-Array
    String listString = "";
    for (String s : parameter) {
      listString += s + "\n";
    }
    
    String[] paramArray = listString.split("\n");
    
    execute(paramArray);
  }
  
  private int getCurrentMonitor() {
    GraphicsConfiguration config = frame.getGraphicsConfiguration();
    GraphicsDevice myScreen = config.getDevice();
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    // AFAIK - there are no guarantees that screen devices are in order...
    // but they have been on every system I've used.
    GraphicsDevice[] allScreens = env.getScreenDevices();
    for (int i = 0; i < allScreens.length; i++) {
      if (allScreens[i].equals(myScreen))
      {
        return i;
      }
    }
    
    return 0;
  }
  
  private void execute(String[] params) {
    
    // Thread to get output
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          
          ProcessBuilder ps = new ProcessBuilder(params);
          
          Process p = ps.start();
          
          if (MainForm.useDebug) {
            ConnectionDebug debug = new ConnectionDebug("Debugger");
            
            debug.debugger.append(ps.command().toString().replace("[", "").replace("]", "").replace(", ", " "));
            debug.debugger.append(System.getProperty("line.separator"));
            debug.debugger.repaint();
            
            Thread stdout = new Thread(new Runnable() {
              @Override
              public void run() {
                try {
                  BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                  String line;
                  while ((line = reader.readLine()) != null) {
                    debug.debugger.append("# - ");
                    debug.debugger.append(line);
                    debug.debugger.append(System.getProperty("line.separator"));
                    debug.debugger.repaint();
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
            stdout.start();
            
            Thread stderr = new Thread(new Runnable() {
              @Override
              public void run() {
                try {
                  BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                  String line;
                  while ((line = reader.readLine()) != null) {
                    debug.debugger.append("x - ");
                    debug.debugger.append(line);
                    debug.debugger.append(System.getProperty("line.separator"));
                    debug.debugger.repaint();
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
            stderr.start();
  
            // Close debugging-window automatically after process finishes
            if (useDebug && closeDebugAutomatically) {
              try {
                p.waitFor();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
  
              debug.frame.setVisible(false);
            }
          }
        } catch (IOException e) {
          e.printStackTrace();
          new ErrorDialog(e);
        }
      }
    });
    
    t.start();
  }
  
  @Override
  public String toString() {
    if (domain.isEmpty()) {
      return label;
    } else {
      return domain + "\\" + label;
    }
  }
  
  public void startEdit(MainForm form) {
    System.out.println("Starting editing");
    ConfigManager.startEdit = true;
    
    form.currentEdit = this;
    
    EditForm editForm = new EditForm(this, form.connectionList);
  
    editForm.txt_label.setText(label);
    editForm.txt_address.setText(address);
    editForm.txt_username.setText(username);
    editForm.txt_password.setText(decrypt(password));
    editForm.txt_domain.setText(domain);
    editForm.txt_resolution.setText(resolution);
    
    editForm.cb_fitToScreen.setSelected(fitToScreen);
    editForm.cb_console.setSelected(console);
    editForm.cb_compression.setSelected(compression);
  
    editForm.txt_note.setText(note);
    
    editForm.drp_keyboards.setSelectedIndex(Keyboards.indexOf(this.keyboardCode));
    
    ConfigManager.startEdit = false;
  }
  
  private static final String ALGORITHM = "AES";
  
  public String encrypt(String valueToEnc) {
    return encrypt(valueToEnc, secrethash);
  }
  
  public static String encrypt(String valueToEnc, String inputKey) {
    
    if (valueToEnc.equals("") || master.equals("disabled")) {
      return valueToEnc;
    } else {
      try {
        Key key = generateKey(inputKey);
        
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        return new BASE64Encoder().encode(encValue);
      } catch (Exception e) {
        e.printStackTrace();
        new ErrorDialog(e);
      }
      
      return null;
    }
  }
  
  public String decrypt(String encryptedValue) {
    if (master.equals("disabled") || this.password.equals("")) {
      return this.password;
    } else {
      try {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
      } catch (Exception e) {
        e.printStackTrace();
        new ErrorDialog(e);
      }
      return null;
    }
  }
  
  private static Key generateKey() {
    return generateKey(secrethash);
  }
  
  private static Key generateKey(String input) {
    return new SecretKeySpec(input.getBytes(), ALGORITHM);
  }
  
  private class Drive {
    private final String label;
    private final String path;
    
    public Drive(String label, String path) {
      this.label = label;
      this.path = path;
    }
    
    public String label() {
      return label;
    }
    
    public String path() {
      return path;
    }
  }
  
}