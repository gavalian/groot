/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.base;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jlab.groot.graphics.GraphicsAxis;
import org.jlab.groot.graphics.GraphicsAxisFrame;
import org.jlab.groot.ui.LatexText;
import org.jlab.groot.ui.PaveText;


/**
 *
 * @author gavalian
 */
public class GraphicsTests extends JPanel {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7128170889587576863L;
	ColorPalette palette = new ColorPalette();
    GraphicsAxis    xaxis   = new GraphicsAxis(GraphicsAxis.AXISTYPE_HORIZONTAL);
    GraphicsAxis    yaxis   = new GraphicsAxis(GraphicsAxis.AXISTYPE_VERTICAL);
    
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
        g2d.setRenderingHint( RenderingHints.  KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint( RenderingHints.  KEY_STROKE_CONTROL,
               RenderingHints.VALUE_STROKE_PURE);
        
        //g2d.setRenderingHint( RenderingHints.  KEY_RENDERING, 
        //        RenderingHints.VALUE_RENDER_QUALITY);
        
        int xT = 100;
        int yT = 200;
        g2d.setColor(Color.red);
        g2d.drawOval(xT-4, yT-4, 8,8);
        g2d.setColor(Color.blue);
        LatexText  text = new LatexText("Rotation Test");
        
        
        text.setFontSize(18);
        
        text.drawString(g2d, xT,yT,LatexText.ALIGN_CENTER,LatexText.ALIGN_TOP,LatexText.ROTATE_LEFT);
        text.drawString(g2d, xT,yT,1,1);
        System.out.println("Pave text definition");
        PaveText pave = new PaveText(2);
        pave.setPosition(300, 300);
        pave.addText("Entries","124500");

        pave.addText("RMS","0.047+/-1.2345");
        for(int i = 0; i < 1; i++){
            pave.addText("Mean","0.567");
            pave.addText("#chi^2/NDF","126.34/57");
        }
        pave.addText("Underflow","3456");
        pave.addText("Overflow","23");
        pave.drawPave(g2d, 200,200);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(200,200, 40, 40);        
        g2d.fillOval(203,203, 34, 34);
        
        WobbleStroke  stroke = new WobbleStroke(1f,6f);
        g2d.setStroke(stroke);
        g2d.drawLine(0, 0, 200, 200);
//this.drawGraphicsFrame(g2d);
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
