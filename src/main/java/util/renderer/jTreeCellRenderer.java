package util.renderer;

import main.Connection;
import main.MainForm;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;


public class jTreeCellRenderer extends DefaultTreeCellRenderer {

  private final ImageIcon rdpIcon;
  private final ImageIcon httpIcon;

  public jTreeCellRenderer(MainForm form) {
    rdpIcon = new ImageIcon(form.getClass().getResource("/rdp.png"));
    httpIcon = new ImageIcon(form.getClass().getResource("/http.png"));
  }

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
        setIcon(rdpIcon);
      }
      if (conn.type == Connection.Type.http) {
        setIcon(httpIcon);
      }
    }


    return c;
  }
}