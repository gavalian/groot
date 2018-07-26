/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.EmbeddedCanvasTabbed;
import org.jlab.groot.graphics.EmbeddedPad;

/**
 *
 * @author gavalian
 */
public class TGCanvas extends JFrame implements ActionListener {
    JPanel  framePane = null;
    EmbeddedCanvasTabbed  canvas = null; 
    JPanel  statusPane  = null;
    JLabel  statusLabel = null;
    JMenuBar menuBar = null;
    
     public TGCanvas(String name, String canvasTitle, int xsize, int ysize){
         super();
         
         this.setTitle(name);
         this.setSize(xsize, ysize);
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.initMenu();
         this.setJMenuBar(menuBar);

         if(canvasTitle.contains(":")==true){
            String[] tokens = canvasTitle.split(":");
            canvas = new EmbeddedCanvasTabbed(true,tokens);
         } else {
             canvas = new EmbeddedCanvasTabbed(true,canvasTitle);
         }
    
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
        //this.pack();
        this.setVisible(true);
     }
 private void initMenu(){
        menuBar = new JMenuBar();
        JMenu menuFile   = new JMenu("File");
        JMenuItem mfNew = new JMenuItem("New");
        mfNew.addActionListener(this);
        
        menuFile.add(mfNew);
        
        JMenu menuView   = new JMenu("View");
        JMenuItem mvDivide = new JMenuItem("Divide");
        mvDivide.addActionListener(this);
        menuView.add(mvDivide);
        
        JMenu menuStudio = new JMenu("Studio");
        JMenuItem msInspector = new JMenuItem("Inspector");
        msInspector.addActionListener(this);
        JMenuItem msSaveAll = new JMenuItem("Save All");
        msSaveAll.addActionListener(this);
        
        menuStudio.add(msInspector);
        menuStudio.add(msSaveAll);
        
        menuBar.add(menuFile);
        menuBar.add(menuView);
        menuBar.add(menuStudio);
    }
 
     public EmbeddedCanvas  getCanvas(){
        return this.canvas.getCanvas();
    }
    
    public EmbeddedPad getPad(){
        return this.canvas.getCanvas().getPad();
    }
    
    public void divide(int xsize, int ysize){
        this.canvas.getCanvas().divide(xsize, ysize);
    }
    
    public void cd(int pad){
        this.canvas.getCanvas().cd(pad);
    }
    
    public void draw(IDataSet ds, String options){
        this.canvas.getCanvas().draw(ds, options);
        this.getCanvas().update();
    }
    
    public void draw(LatexText text){
        this.canvas.getCanvas().draw(text);
        this.getCanvas().update();
    }
    
    public void draw(IDataSet ds){
        this.canvas.getCanvas().draw(ds);
        this.getCanvas().update();
    }
    
    public void save(String filename){
    	this.canvas.getCanvas().save(filename);
    }
    
    public TGCanvas addCanvas(String name){
        this.canvas.addCanvas(name); return this;
    }
    
    public void setCanvas(String name){
        this.canvas.setActiveCanvas(name);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    
    
    public static void main(String[] args){
        TGCanvas c1 = new TGCanvas("Calibration GUI","DC:CTOF:CND",600,600);
        c1.addCanvas("ECAL").addCanvas("BST");
        c1.addCanvas("FTOF");

        
        c1.setCanvas("ECAL");
    }
}
