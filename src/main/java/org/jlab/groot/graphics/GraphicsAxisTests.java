/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 *
 * @author gavalian
 */
public class GraphicsAxisTests extends JPanel {
    
    
    GraphicsAxis  axis = new GraphicsAxis(GraphicsAxis.AXISTYPE_HORIZONTAL);
    
    public GraphicsAxisTests(){
        super();
        this.setPreferredSize(new Dimension(500,500));
        
    }
    
    @Override
    public void paint(Graphics g){ 
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint( RenderingHints.  KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint( RenderingHints.  KEY_STROKE_CONTROL,
               RenderingHints.VALUE_STROKE_PURE);
        int w = this.getWidth();
        int h = this.getHeight();
        g2d.setColor(Color.white);
        
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(20, 200, w-40, 100);
        
        axis.getDimension().setMinMax(20, w-20);
        axis.getRange().setMinMax(0.0, 1.0);
        axis.drawAxis(g2d, 20, 300);
        //axis.drawAxisGrid(g2d, 20,300,100);
        axis.drawAxisMirror(g2d, 20,300,100);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsAxisTests canvas = new GraphicsAxisTests();
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }
}
