/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.studio;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import org.jlab.groot.tree.Tree;

/**
 *
 * @author gavalian
 */
public class StudioToolBar {
    private int iconSizeX = 25;
    private int iconSizeY = 25;
    public JToggleButton fastButton;
    private JToolBar  toolbar = null;
    Map<String,JButton>   toolBarButtons = new HashMap<String,JButton>();
            
    public StudioToolBar(ActionListener listener){
        init(listener);
    }
    
    private void init(ActionListener listener){
        ImageIcon histogramIcon = new ImageIcon();
        ImageIcon cutIcon = new ImageIcon();
        ImageIcon grapherrorsIcon = new ImageIcon();
        ImageIcon playIcon = new ImageIcon();
        ImageIcon importIcon = new ImageIcon();
        ImageIcon fastIcon = new ImageIcon();
        ImageIcon operationIcon = new ImageIcon();
        ImageIcon editTreeIcon = new ImageIcon();


        try {
			Image graphErrorsImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/grapherrors.png"));
			Image cutImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/cut.png"));
			Image histogramImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/histogram.png"));
			Image playImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/play.png"));
			Image importImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/import.png"));
			Image fastImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/fast.png"));
			Image operationImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/operations.png"));
			Image editTreeImage = ImageIO.read(Tree.class.getClassLoader().getResource("icons/tree/edit_tree.png"));

			grapherrorsIcon.setImage(graphErrorsImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
	        cutIcon.setImage(cutImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
	        histogramIcon.setImage(histogramImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
	        playIcon.setImage(playImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
	        importIcon.setImage(importImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
	        fastIcon.setImage(fastImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
	        operationIcon.setImage(operationImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
	        editTreeIcon.setImage(editTreeImage.getScaledInstance(iconSizeX, iconSizeY, Image.SCALE_SMOOTH));
	        
		} catch (IOException e) {
			System.out.println("Error: Could not load icons!");
			e.printStackTrace();
		}
        toolbar = new JToolBar();
        JButton histogramButton = new JButton(histogramIcon);
        JButton graphErrorsButton = new JButton(grapherrorsIcon);
        JButton cutButton = new JButton(cutIcon);
        JButton playButton = new JButton(playIcon);
        JButton importButton = new JButton(importIcon);
        fastButton = new JToggleButton(fastIcon);
        JButton operationButton = new JButton(operationIcon);
        JButton editTreeButton = new JButton(editTreeIcon);

        histogramButton.setToolTipText("Add Histogram");
        cutButton.setToolTipText("Add Cut");
        
        histogramButton.setActionCommand("Add Descriptor");
        cutButton.setActionCommand("Add Cut");
        graphErrorsButton.setActionCommand("Add GraphErrors");
        importButton.setActionCommand("Import ASCII");
        fastButton.setActionCommand("Preview Mode");
        playButton.setActionCommand("Play");
        operationButton.setActionCommand("Action Jack");
        editTreeButton.setActionCommand("Edit Tree");
        
        histogramButton.addActionListener(listener);
        cutButton.addActionListener(listener);
        graphErrorsButton.addActionListener(listener);
        importButton.addActionListener(listener);
        fastButton.addActionListener(listener);
        playButton.addActionListener(listener);
        operationButton.addActionListener(listener);
        editTreeButton.addActionListener(listener);

        toolbar.add(importButton);
        toolbar.addSeparator();
        //toolbar.add(new JSeparator());
        toolbar.add(histogramButton);
        toolbar.add(graphErrorsButton);
        toolbar.add(cutButton);
        toolbar.add(operationButton);
        toolbar.addSeparator();
        toolbar.add(editTreeButton);
        toolbar.addSeparator();
        //toolbar.add(new JSeparator());
        toolbar.add(playButton);
        toolbar.add(fastButton);
        

        
    }
    
    public JToolBar  getToolBar(){
        return this.toolbar;
    }
}
