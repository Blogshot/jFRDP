package util;


import java.awt.*;

public class CustomScreen {
  
  Rectangle size;
  int index;
  
  
  public CustomScreen(int i, int width, int height) {
    this.index = i;
    this.size = new Rectangle(width, height);
  }
  
  public String toString() {
    return this.index + " " + this.size.width + "x" + this.size.height;
  }
}
