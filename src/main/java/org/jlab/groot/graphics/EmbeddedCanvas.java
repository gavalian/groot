/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author gavalian
 */
public class EmbeddedCanvas extends JPanel {
    
    private List<EmbeddedPad>    canvasPads  = new ArrayList<EmbeddedPad>();
        
        
    public EmbeddedCanvas(){
        super();
        //this.setSize(500, 400);
        this.setPreferredSize(new Dimension(500,400));        
        canvasPads.add(new EmbeddedPad());
        
    }
    /**
     * painting all components on the Graphics2D object.
     * @param g 
     */
    @Override
    public void paint(Graphics g){ 

        Long st = System.currentTimeMillis();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = this.getSize().width;
        int h = this.getSize().height;
        
        
        EmbeddedPad pad = canvasPads.get(0);
        
        pad.setDimension(0, 0, w, h);
        pad.getAxisFrame().updateMargins(g2d);
        
        pad.getAxisFrame().setAxisMargins(pad.getAxisFrame().getFrameMargins());
                
        System.out.println(pad.getAxisFrame().getFrameMargins());
        pad.setMargins(pad.getAxisFrame().getFrameMargins());
        pad.draw(g2d);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        EmbeddedCanvas canvas = new EmbeddedCanvas();
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }
}
