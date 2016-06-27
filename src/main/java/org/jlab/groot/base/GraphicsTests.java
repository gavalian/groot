/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jlab.groot.graphics.GraphAxis;


/**
 *
 * @author gavalian
 */
public class GraphicsTests extends JPanel {
    
    ColorPalette palette = new ColorPalette();
    GraphAxis    xaxis   = new GraphAxis();
    GraphAxis    yaxis   = new GraphAxis();
    
    public GraphicsTests(){
        super();
        this.setPreferredSize(new Dimension(500,500));
        
    }
    
    @Override
    public void paint(Graphics g){ 

        Long st = System.currentTimeMillis();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = this.getSize().width;
        int h = this.getSize().height;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);
        int axisWidth = palette.getAxisWidth(g2d,20,20,15,h-30,0.0,400.0,true);
        //palette.draw(g2d, w-axisWidth,20,20,h-40,0.0,400.0,true);

        int offsetX = yaxis.getAxisBounds(g2d);
        int offsetY = yaxis.getAxisBounds(g2d);
        
        xaxis.setTitle("X-axis");

        xaxis.setDimension(offsetY, w-40);

        xaxis.setRange(0.0, 1.0);
        
        yaxis.setVertical(true);

        xaxis.setRange(0.0, 1.0);
        yaxis.setDimension(h-offsetX, 40);        

        xaxis.drawAxis(g2d, offsetY,h-offsetX);
        yaxis.drawAxis(g2d, offsetY,h-offsetX);
    }
    
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsTests canvas = new GraphicsTests();
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }
    
}
