package main;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

import static main.Connection.Type.rdp;

public class Connection {

  private boolean console = true;
  private boolean compression = false;

  public String label = "";
  public String address = "";
  private String username = "";
  private String domain = "";
  public String password = "";
  private String resolution = "";
  private String note = "";
  private ArrayList<Drive> drives = new ArrayList<>();

  public Type type = rdp;

  public enum Type {
    rdp, http
  }


  public Connection() {
    this.label = "Default";
    this.address = "";
    this.username = "";
    this.password = "";
    this.domain = "";
    this.resolution = "1280x720";
    this.compression = false;
    this.console = true;
    this.note = "";
    this.type = rdp;

  }

  public void openRDP() {
    // Open a new RDP-Connection
    String parameter = "";

    if (this.console) {
      parameter += " /admin";
    }
    if (!this.compression) {
      parameter += " /compression-level:0";
    }
    if (!this.domain.equals("")) {
      parameter += " /d:" + this.domain;
    }
    if (!this.username.equals("")) {
      parameter += " /u:" + this.username;
    }
    if (!this.password.equals("")) {
      parameter += " /p:" + decrypt(this.password);
    }
    if (!this.resolution.equals("")) {
      parameter += " /size:" + this.resolution;
    }

    parameter += " /cert-ignore +clipboard /drive:\"Home\",\"" + System.getenv("HOME") + "\"";

    parameter += " /v:" + this.address;

    parameter = parameter.trim();

    String command = "xfreerdp ";

    if (drives.size() > 0) {
      command = "osascript -e 'do shell script \"java -jar jFreeRDP.jar\" with administrator privileges'";
    }


    System.out.println(command + parameter);

    try {
      execute(command + parameter);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void execute(String command) throws IOException, InterruptedException {
    Runtime.getRuntime().exec(command);
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

    form.label.setText(label);
    form.address.setText(address);
    form.user.setText(username);
    form.pass.setText(decrypt(password));
    form.domain.setText(domain);
    form.resolution.setText(resolution);

    form.console.setSelected(console);
    form.compression.setSelected(compression);

    form.txt_note.setText(note);

  }

  private static final String ALGORITHM = "AES";

  public String encrypt(String valueToEnc) {
    try {
      return encrypt(valueToEnc, Start.secrethash);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public String encrypt(String valueToEnc, String inputKey) {
    try {
      Key key = generateKey(inputKey);

      Cipher c = Cipher.getInstance(ALGORITHM);
      c.init(Cipher.ENCRYPT_MODE, key);
      byte[] encValue = c.doFinal(valueToEnc.getBytes());
      return new BASE64Encoder().encode(encValue);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public String decrypt(String encryptedValue) {
    try {
      Key key = generateKey();
      Cipher c = Cipher.getInstance(ALGORITHM);
      c.init(Cipher.DECRYPT_MODE, key);
      byte[] decodedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
      byte[] decValue = c.doFinal(decodedValue);
      return new String(decValue);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static Key generateKey() throws Exception {
    return generateKey(Start.secrethash);
  }

  private static Key generateKey(String input) throws Exception {
    return new SecretKeySpec(input.getBytes(), ALGORITHM);
  }

  private class Drive
  {
    private final String label;
    private final String path;

    public Drive(String label, String path)
    {
      this.label   = label;
      this.path = path;
    }

    public String label()   { return label; }
    public String path() { return path; }
  }
}