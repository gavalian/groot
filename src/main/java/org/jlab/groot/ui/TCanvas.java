/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;

/**
 *
 * @author gavalian
 */
public class TCanvas extends JFrame {
    
    JPanel  framePane = null;
    EmbeddedCanvas  canvas = null; 
    JPanel  statusPane  = null;
    JLabel  statusLabel = null;
    
    public TCanvas(String name, int xsize, int ysize){
        super();
        this.setTitle(name);
        this.setSize(xsize, ysize);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new EmbeddedCanvas(xsize,ysize);
    
        framePane= new JPanel();
        framePane.setLayout(new BorderLayout());
        framePane.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(4, 4, 4, 4), new EtchedBorder()));
        //framePane.setBorder(new BevelBorder(BevelBorder.LOWERED));    
        
        statusPane = new JPanel();
        statusPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Avenir",Font.PLAIN,12));
        statusPane.setBorder(new BevelBorder(BevelBorder.LOWERED));        
        statusPane.add(statusLabel);
        
        framePane.add(canvas, BorderLayout.CENTER);
        framePane.add(statusPane,BorderLayout.PAGE_END);
        
        this.add(framePane);
        this.pack();
        this.setVisible(true);
    }
    
    
    public static void main(String[] args){
        new TCanvas("c1",600,400);
    }
}
