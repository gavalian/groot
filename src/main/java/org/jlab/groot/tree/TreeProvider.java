/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.tree;

import javax.swing.JDialog;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.jlab.groot.graphics.EmbeddedCanvas;

/**
 *
 * @author gavalian
 */
public interface TreeProvider {
    
    TreeModel   getTreeModel();
    void        actionTreeNode(TreePath path, EmbeddedCanvas canvas, int limit);
    void        setSource(String filename);
    JDialog     treeConfigure();
    
}
