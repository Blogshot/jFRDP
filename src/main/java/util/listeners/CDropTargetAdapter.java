package util.listeners;

import main.Connection;
import main.Customer;
import main.MainForm;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;

import static util.ConfigManager.saveConnections;

public class CDropTargetAdapter extends DropTargetAdapter {

  private final JTree connectionList;
  private final MainForm form;

  public CDropTargetAdapter(MainForm form, JTree connectionList) {

    this.form = form;
    this.connectionList = connectionList;
  }

  @Override
  public void drop(DropTargetDropEvent dtde) {

    TreePath selectionPath = connectionList.getSelectionPath();
    TreePath sourcePath = selectionPath.getParentPath();

    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectionPath
        .getLastPathComponent();

    Point dropLocation = dtde.getLocation();
    TreePath targetPath = connectionList.getClosestPathForLocation(
        dropLocation.x, dropLocation.y);

    System.out.println("###################");

    System.out.println("srcPath: " + sourcePath);
    System.out.println("targetPath: " + targetPath);
    System.out.println("selectedNode: " + selectedNode);


    DefaultMutableTreeNode targetParentNode = (DefaultMutableTreeNode) targetPath
        .getLastPathComponent();
    DefaultMutableTreeNode sourceParentNode = (DefaultMutableTreeNode) sourcePath
        .getLastPathComponent();

    if (isDropAllowed(sourceParentNode, targetParentNode, selectedNode)) {
      System.out.println("drop accept");
      sourceParentNode.remove(selectedNode);
      targetParentNode.add(selectedNode);

      ((Customer) targetParentNode.getUserObject()).connections.add((Connection) selectedNode.getUserObject());
      ((Customer) sourceParentNode.getUserObject()).connections.remove(selectedNode.getUserObject());

      dtde.dropComplete(true);
      connectionList.updateUI();

      saveConnections();
      form.fillGUI();

    } else {
      System.out.println("drop: reject");
      dtde.rejectDrop();
      dtde.dropComplete(false);
    }
  }

  private boolean isDropAllowed(DefaultMutableTreeNode sourceNode,
                                DefaultMutableTreeNode targetNode,
                                DefaultMutableTreeNode selectedNode) {

    return sourceNode.isLeaf() && !targetNode.isLeaf() && selectedNode.isLeaf();
  }
}
