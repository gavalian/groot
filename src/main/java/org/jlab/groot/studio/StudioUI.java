/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.studio;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.jlab.groot.data.DataVector;
import org.jlab.groot.data.H1F;
import org.jlab.groot.tree.RandomTree;
import org.jlab.groot.tree.Tree;
import org.jlab.groot.ui.EmbeddedCanvas;
import org.jlab.groot.ui.HistogramPlotter;

/**
 *
 * @author gavalian
 */
public class StudioUI implements MouseListener {
    
    JSplitPane  splitPane = null;
    JPanel      navigationPane = null;
    EmbeddedCanvas drawCanvas = null;
    JFrame  frame = null;
    Tree    studioTree = null;
     JTree jtree = null;
     
    public StudioUI(Tree tree){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studioTree = tree;
        initUI();
        frame.pack();
        frame.setVisible(true);
    }
    
    private void initUI(){
        splitPane = new JSplitPane();
        navigationPane = new JPanel();
        navigationPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        JPanel canvasPane = new JPanel();
        canvasPane.setLayout(new BorderLayout());
        canvasPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        //canvasPane.setBorder(new EmptyBorder(5,5,5,5));
        drawCanvas     = new EmbeddedCanvas(500,500);
        canvasPane.add(drawCanvas,BorderLayout.CENTER);

        
        splitPane.setLeftComponent(navigationPane);
        splitPane.setRightComponent(canvasPane);

        
        DefaultMutableTreeNode top =
                 studioTree.getTree();

        jtree = new JTree(top);
        jtree.addMouseListener(this);
        JScrollPane treeView = new JScrollPane(jtree);
        navigationPane.setBorder(new EmptyBorder(5,5,5,5));
        navigationPane.setLayout(new BorderLayout());
        navigationPane.add(treeView,BorderLayout.CENTER);
        splitPane.setDividerLocation(0.5);
        frame.add(splitPane);
    }
    
    
    public void scanTreeItem(String item){
        if(this.studioTree.hasBranch(item)==true){
            List<Double> vector = studioTree.getVector(item,studioTree.getSelector());
            DataVector vec = new DataVector(vector);
            H1F  h1d = H1F.create(item, 100, vec);
            h1d.setLineColor(1);
            h1d.setFillColor(43);
            this.drawCanvas.getPad(0).reset();
            this.drawCanvas.getPad(0).addPlotter(new HistogramPlotter(h1d));
            this.drawCanvas.update();
        }        
    }
    
    public static void main(String[] args){
        StudioUI sui = new StudioUI(new RandomTree());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            TreePath path = jtree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                System.out.println(path.getLastPathComponent().toString());
                scanTreeItem(path.getLastPathComponent().toString());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}
