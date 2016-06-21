/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.studio;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import org.jlab.groot.tree.Tree;

/**
 *
 * @author gavalian
 */
public class StudioToolBar {
    
    private JToolBar  toolbar = null;
    Map<String,JButton>   toolBarButtons = new HashMap<String,JButton>();
            
    public StudioToolBar(ActionListener listener){
        init(listener);
    }
    
    private void init(ActionListener listener){
        ImageIcon leafIcon = new ImageIcon(Tree.class.getClassLoader().getResource("icons/tree/leaf_t.png"));
        ImageIcon dirIcon  = new ImageIcon(Tree.class.getClassLoader().getResource("icons/tree/tree_t.png"));
        toolbar = new JToolBar();
        JButton btnOpen = new JButton(leafIcon);
        JButton btnClose = new JButton(dirIcon);
        btnOpen.setToolTipText("Add Data Descriptor");
        btnClose.setToolTipText("Add Cut");
        
        btnOpen.setActionCommand("Add Descriptor");        
        btnOpen.addActionListener(listener);
        
        btnClose.setActionCommand("Add Cut");
        btnClose.addActionListener(listener);
        
        toolbar.add(btnOpen);
        toolbar.add(btnClose);
        
    }
    
    public JToolBar  getToolBar(){
        return this.toolbar;
    }
}
