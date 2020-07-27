/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import org.jlab.jnp.graphics.base.Node2D;

/**
 *
 * @author gavalian
 */
public class GraphicsDebug {
    
    DataCanvas canvas = null;
    
    public void canvasDebug(JFrame frame){
        
        canvas = new DataCanvas();
        //canvas.setDebug(1);
        //canvas.setPadInsets(5, 15, 15, 5);
        /*int count = 5;
        for(int i = 0; i < count; i++){
            EmbeddedPad pad = new EmbeddedPad("pad_"+i);
            pad.getGraphicsAxis().addNode(new DataNode2D());
            pad.getGraphicsAxis().setTranslation(-0.1, -0.1, 0.2005, 0.2005);
            if(i==1) pad.trackMouse(true);
            if(i==0){
                PaveText text = new PaveText("new groot is coming",20,20);
                text.alignMode(Node2D.ALIGN_POSITION);
                text.canMove(true);
                //pad.addNode(text);
            }
            canvas.addNode(pad);
        }
        //canvas.divide(2,2);
        canvas.divide(new int[]{3,2});*/
        canvas.divide(2, 2);
        canvas.divide(new double[][]{{0.6,0.4},{0.333,0.333,0.333},{0.25,0.25,0.25,0.25}});
        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.add(canvas, BorderLayout.CENTER);
        frame.add(pane);
    }
    
    public DataCanvas getCanvas(){return canvas;}
    
    public void save(){
        long startTime = System.currentTimeMillis();
        //for(int i = 0; i < 100; i++)
        canvas.save("test.pdf");
        
        long endTime = System.currentTimeMillis();
        canvas.saveEPS("test.eps");        
        canvas.saveSVG("test.svg");

        System.out.println(String.format("%f", ((double)(endTime-startTime))/1000.0));
    }
    
    public static void main(String[] args){
        
        String pattern = "MM/dd/yyyy-HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        System.out.println("has time = " + pattern.contains("-"));
        Date date;
        try {
            //date = format.parse("08/22/2017-14:42:08");
            date = format.parse("08/22/2017-14:42:08");
            System.out.println(date);
        } catch (ParseException ex) {
            Logger.getLogger(GraphicsDebug.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        GraphicsDebug debug = new GraphicsDebug();
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        debug.canvasDebug(frame);
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save...");
        fileMenu.add(saveItem);
        
        JMenu editMenu = new JMenu("Edit");
        JMenuItem attrItem = new JMenuItem("Attributes...");
        editMenu.add(attrItem);
        
        //JMenu editMenu = new JMenu("Edit");
        JMenuItem resizeItem = new JMenuItem("Resize");
        editMenu.add(resizeItem);
        
        resizeItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               frame.setSize(800, 600);
            }
            
        });
        
        saveItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                debug.save();
            }
            
        });
        attrItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                debug.getCanvas().editAttributes();
            }
            
        });
        
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
        
        debug.getCanvas().setAxisFont("Avenir", 12, 2);
        debug.getCanvas().setAxisTitleFont("Avenir", 12, 0);
        /*debug.getCanvas().getRegion(0).setAxisFont("Times", 14, 0);
        debug.getCanvas().getRegion(1).setAxisFont("Times", 14, 1);
        debug.getCanvas().getRegion(2).setAxisFont("Avenir", 14, 2);*/
        debug.getCanvas().setAxisTicks(6, "XY");
        //debug.getCanvas().getRegion(2).getGraphicsAxis()
        //        .setAxisTicks(10, "Y");
        debug.getCanvas().setAxisTitleOffset(15, "X");
        debug.getCanvas().setAxisTitleOffset(15, "Y");
        debug.getCanvas().right(25).top(10);
        debug.getCanvas().getRegion(0).getGraphicsAxis().addNode(new DataNode2D());
        
        debug.getCanvas().getRegion(3).getGraphicsAxis().addNode(new DataNode2D());
        debug.getCanvas().getRegion(2).getGraphicsAxis().addNode(new DataNode2D());
    }
}
