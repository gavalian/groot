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
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jlab.groot.graphics.GraphicsAxis;
import org.jlab.groot.graphics.GraphicsAxisFrame;
import org.jlab.groot.ui.LatexText;


/**
 *
 * @author gavalian
 */
public class GraphicsTests extends JPanel {
    
    ColorPalette palette = new ColorPalette();
    GraphicsAxis    xaxis   = new GraphicsAxis();
    GraphicsAxis    yaxis   = new GraphicsAxis();
    
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
        
        this.drawGraphicsFrame(g2d);
    }
    
    private void drawGraphicsFrame(Graphics2D g2d){
        
        int w = this.getSize().width;
        int h = this.getSize().height;
        
        PadMargins  customMargins = new PadMargins();
        customMargins.setBottomMargin(60);
        customMargins.setTopMargin(40);
        customMargins.setLeftMargin(70);
        customMargins.setRightMargin(70);
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(Color.BLACK);
        
        GraphicsAxisFrame  frame = new GraphicsAxisFrame();
        frame.setFrameDimensions(0, w, 0, h);
        frame.getAxisX().setRange(0.0, 1.0);
        frame.getAxisY().setRange(200, 400);
        //frame.getAxisX().setAxisFont("Helvetica");
        //frame.getAxisX().setAxisFontSize(18);
        frame.updateMargins(g2d);
        
        frame.setAxisMargins(customMargins);
        frame.drawAxis(g2d, customMargins);
    }
    
    private void drawAxis(Graphics2D g2d){
        int w = this.getSize().width;
        int h = this.getSize().height;
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(Color.BLACK);
        //int axisWidth = palette.getAxisWidth(g2d,20,20,15,h-30,0.0,400.0,true);
        //palette.draw(g2d, w-axisWidth,20,20,h-40,0.0,400.0,true);
        
        int offsetX = xaxis.getAxisBounds(g2d);
        int offsetY = yaxis.getAxisBounds(g2d);
        System.out.println(" AXIS BOUNDAS = " + offsetX + "  " + offsetY);
        xaxis.setTitle("X-axis");
        xaxis.setDimension(offsetY, w-40);
        xaxis.setRange(0.0, 1.0);
        
        
        yaxis.setVertical(true);

        xaxis.setRange(0.0, 1.0);
        yaxis.setDimension(h-offsetX, 40);        

        xaxis.drawAxis(g2d, offsetY,h-offsetX);
        yaxis.drawAxis(g2d, offsetY,h-offsetX);
        
        /*
        LatexText  text = new LatexText("23.456");
        text.setFont("Helvetica");
        text.setFontSize(36);
        Rectangle2D rect = text.getBoundsNumber(g2d);
        g2d.drawRect(100,100,(int) rect.getWidth(),(int) rect.getHeight());
        text.drawString(g2d, 100, 100, 0, 0);
        
        LatexText  text2 = new LatexText("23.456");
        text2.setFont("Avenir");
        text2.setFontSize(36);
        Rectangle2D rect2 = text2.getBoundsNumber(g2d);
        g2d.drawRect(300,100,(int) rect2.getWidth(),(int) rect2.getHeight());
        text2.drawString(g2d, 300, 100, 0, 0);
        */
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
