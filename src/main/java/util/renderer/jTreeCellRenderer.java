package util.renderer;

import main.Connection;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;


public class jTreeCellRenderer extends DefaultTreeCellRenderer {
  
  @Override
  public Component getTreeCellRendererComponent(JTree tree,
                                                Object value, boolean selected, boolean expanded,
                                                boolean isLeaf, int row, boolean focused) {
    Component c = super.getTreeCellRendererComponent(tree, value,
        selected, expanded, isLeaf, row, focused);
    
    Object o = ((DefaultMutableTreeNode) value).getUserObject();
    
    if (isLeaf && o instanceof Connection) {
      
      Connection conn = ((Connection) ((DefaultMutableTreeNode) value).getUserObject());
      
      if (conn.type == Connection.Type.rdp) {
        setIcon(new ImageIcon(textToImage("RDP")));
      }
      if (conn.type == Connection.Type.http) {
        setIcon(new ImageIcon(textToImage("HTTP")));
      }
      if (conn.type == Connection.Type.ftp) {
        setIcon(new ImageIcon(textToImage("FTP")));
      }
    }
    
    return c;
  }
  
  private BufferedImage textToImage(String text) {
    
    Color FONTCOLOR = Color.BLACK;
    int FONTSIZE = 12;
    Font font = new Font("Arial", Font.PLAIN, FONTSIZE);
    
        /*
           Because font metrics is based on a graphics context, we need to create
    a small, temporary image so we can ascertain the width and height
    of the final image
         */
    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = img.createGraphics();
    g2d.setFont(font);
    
    // get "size" of font
    FontMetrics fm = g2d.getFontMetrics();
    
    // get size of 4-character text
    int width = fm.stringWidth("AAAA");
    int height = fm.getHeight();
    
    // dispose of graphics-object
    g2d.dispose();
    
    // create new image with needed size
    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    g2d = img.createGraphics();
    
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    
    g2d.setFont(font);
    
    fm = g2d.getFontMetrics();
    
    g2d.setColor(FONTCOLOR);
    
    /*
    Center text by setting horizontal offset
    
     [------].width - [ASDF].width = [--] ;[--] / 2 = xOffset
     [-ASDF-]
     */
    int xOffset = (width - fm.stringWidth(text)) / 2;
    
    g2d.drawString(text, xOffset, fm.getAscent());
    g2d.dispose();
    
    return img;
    
  }
}