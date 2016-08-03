package util;


public class MD5Wrapper {

  public static String createHash(String input) {

    try {
      java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
      byte[] array = md.digest(input.getBytes());

      StringBuilder sb = new StringBuilder();
      for (byte anArray : array) {
        sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
      }
      return sb.toString().substring(0, 16);
    } catch (java.security.NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";

  }
}
