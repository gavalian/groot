/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.widget;

import java.awt.Font;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import org.jlab.groot.data.H1F;

import org.jlab.jnp.graphics.base.Canvas2D;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.groot.graphics.Legend;
import org.jlab.jnp.groot.graphics.PaveText;

/**
 *
 * @author gavalian
 */
public class LegendPaveDebug extends Node2D {
    
    public LegendPaveDebug(){
        super(100,100);
    }
    
    
    public void paveText(Graphics2D g2d){
        Node2D    node = new Node2D(0,0,450,450);
        String[] messages = new String[]{"negative particles","positive particles","combined particles"};

        PaveText  pave = new PaveText(100,100);        
        pave.setParent(node);        
        pave.addLines(messages);
                
        PaveText  pave2 = new PaveText(300,100); 
        pave2.setFont(new Font("Avenir",Font.PLAIN,18));
        pave2.setStyle(PaveText.PaveTextStyle.ONELINE);
        pave2.left(35);
        pave2.setParent(node);        
        pave2.addLines(messages);
                
        
        PaveText  pave3 = new PaveText(100,300);
        pave3.setFont(new Font("Times",Font.BOLD,12));
        pave3.setStyle(PaveText.PaveTextStyle.ONELINE);
        pave3.left(35);
        pave3.setParent(node);
        pave3.addLines(messages);                        
        
        pave.drawLayer(g2d, 0);
        pave2.drawLayer(g2d, 0);
        pave3.drawLayer(g2d, 0);
    }
    
    
    public void legend(Graphics2D g2d){
        
        Node2D    node = new Node2D(0,0,450,450);
                
        Legend legend = new Legend(100,100);
        legend.setStyle(PaveText.PaveTextStyle.ONELINE);
        legend.setParent(node);        
        H1F h1 = new H1F("h1","",100,0.0,1.0);
        H1F h2 = new H1F("h1","",100,0.0,1.0);
        H1F h3 = new H1F("h1","",100,0.0,1.0);
        h1.setFillColor(2);
        h1.setLineColor(1);
        h1.setLineWidth(2);
        
        h2.setFillColor(3);
        h2.setLineColor(4);
        h2.setLineWidth(1);
        
        h3.setFillColor(8);
        h3.setLineColor(2);
        h3.setLineWidth(3);
        
        
        legend.add(h1,"positive tracks").add(h2, "negative tracks").add(h3, "denoised tracks");
        
        legend.drawLayer(g2d, 0);
    }
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){ 
        //this.paveText(g2d);
        this.legend(g2d);
        //Node2D            parent = getParent();
               
        
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas2D canvas = new Canvas2D();
        canvas.setSize(900, 600);
        LegendPaveDebug widget = new LegendPaveDebug();
        canvas.addNode(widget);
        frame.add(canvas);
        frame.setSize(900, 600);
        frame.pack();
        frame.setSize(900, 600);
        frame.setVisible(true);
    }
}
