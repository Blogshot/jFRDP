package main;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import util.Dialogs.ErrorDialog;
import util.Keyboards;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.ArrayList;

import static main.Connection.Type.rdp;
import static main.MainForm.master;
import static main.MainForm.secrethash;

public class Connection {
  
  private boolean console = true;
  private boolean compression = false;
  
  public String label = "";
  public String address = "";
  private String username = "";
  private String domain = "";
  public String password = "";
  private String resolution = "1280x720";
  private String note = "";
  private ArrayList<Drive> drives = new ArrayList<>();
  public String keyboardCode = "";
  public Type type = rdp;
  
  public enum Type {
    rdp, http
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
    
    // command
    parameter.add("/usr/local/bin/xfreerdp");
    
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
    }
    if (!this.resolution.equals("")) {
      parameter.add("/size:" + this.resolution);
    }
    if (!this.keyboardCode.equals("")) {
      parameter.add("/kbd:" + this.keyboardCode);
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
  
  private void execute(String[] params) {
    
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          ConnectionDebug debug = new ConnectionDebug("Debugger");
          
          ProcessBuilder ps = new ProcessBuilder(params);
          
          ps.redirectErrorStream(true);
          debug.debugger.append(ps.command().toString().replace("[", "").replace("]", "").replace(", ", " "));
          debug.debugger.append(System.getProperty("line.separator"));
          debug.debugger.repaint();
          
          Process p = ps.start();
          
          BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String line;
          while ((line = reader.readLine()) != null) {
            debug.debugger.append(line);
            debug.debugger.append(System.getProperty("line.separator"));
            debug.debugger.repaint();
            
          }
          
        } catch (Exception e) {
          e.printStackTrace();
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
    form.currentEdit = this;
    
    form.txt_label.setText(label);
    form.txt_address.setText(address);
    form.txt_username.setText(username);
    form.txt_password.setText(decrypt(password));
    form.txt_domain.setText(domain);
    form.txt_resolution.setText(resolution);
    
    form.cb_console.setSelected(console);
    form.cb_compression.setSelected(compression);
    
    form.txt_note.setText(note);
    
    form.drp_keyboards.setSelectedIndex(Keyboards.indexOf(this.keyboardCode));
    
  }
  
  private static final String ALGORITHM = "AES";
  
  public String encrypt(String valueToEnc) {
    return encrypt(valueToEnc, secrethash);
  }
  
  public String encrypt(String valueToEnc, String inputKey) {
    
    if (valueToEnc.equals("")) {
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