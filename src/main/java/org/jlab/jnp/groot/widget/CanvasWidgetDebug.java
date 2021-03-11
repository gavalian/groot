/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import org.jlab.jnp.graphics.base.Canvas2D;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.graphics.base.NodeInsets;
import org.jlab.jnp.graphics.base.NodeRegion2D;
import org.jlab.jnp.groot.graphics.LatexText;
import org.jlab.jnp.groot.settings.GRootTheme;

/**
 *
 * @author gavalian
 */
public class CanvasWidgetDebug extends Node2D {
    
    LatexText text = new LatexText("a",100,100);
    LatexText text2 = new LatexText("a",100,100);
    
    public CanvasWidgetDebug(){
        super(100,100);
    }
    
    public void stringDebug(Graphics2D g2d){
        GRootTheme        theme  = GRootTheme.getInstance();
        
        NodeRegion2D bounds =  getBounds();
        NodeInsets   insets =  getInsets(); 
        int padding = 50;
        //System.out.println(bounds);
        g2d.drawRect(padding,padding, 
                (int) (bounds.getWidth()-2*padding), 
                (int) (bounds.getHeight()-2*padding)
                );                
        g2d.setColor(Color.red);
        text.setFont(new Font("Avenir",Font.PLAIN,14));
        String title = "negative 400, mean = 0.4567";
        text.setText(title);      
        int offset = 50;
        Rectangle2D r = text.getBounds(g2d);
        for(int i = 0 ; i < 6 ; i++){
            g2d.drawOval(100,100 + i*offset,3,3);
             g2d.setColor(Color.red);
        
             g2d.drawRect((int) (100), 
                (int) (100 + i*offset), (int) r.getWidth(), (int) r.getHeight());
             g2d.drawRect((int) (100), 
                (int) (100 + i*offset), (int) r.getWidth(), (int) (r.getHeight()/2.0));
        }

        text.drawString(title, g2d, 100, 100, 
                0, LatexText.ALIGN_TOP, LatexText.ROTATE_NONE);
        
        text.drawString(title, g2d, 100, 100 + offset, 
                0, LatexText.ALIGN_BOTTOM, LatexText.ROTATE_NONE);
        
        text.drawString(title, g2d, 100, 100 + 2*offset, 
                LatexText.ALIGN_LEFT, LatexText.ALIGN_CENTER, LatexText.ROTATE_NONE);
    }
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){ 
        
        //Node2D            parent = getParent();
        GRootTheme        theme  = GRootTheme.getInstance();
        
        NodeRegion2D bounds =  getBounds();
        NodeInsets   insets =  getInsets(); 
        int padding = 50;
        //System.out.println(bounds);
        g2d.drawRect(padding,padding, 
                (int) (bounds.getWidth()-2*padding), 
                (int) (bounds.getHeight()-2*padding)
                );                
        g2d.setColor(Color.red);
        text.setFont(new Font("SansSerif",Font.PLAIN,18));
        String title = "negative 400^2, mean = 0.4567_1";
        
        text.setText(title);      
        int offset = 50;
        Rectangle2D r = text.getBounds(g2d);
        for(int i = 0 ; i < 6 ; i++){
            g2d.drawOval(100,100 + i*offset,3,3);
             g2d.setColor(Color.red);
        
             g2d.drawRect((int) (100), 
                (int) (100 + i*offset), (int) r.getWidth(), (int) r.getHeight());
             g2d.drawRect((int) (100), 
                (int) (100 + i*offset), (int) r.getWidth(), (int) (r.getHeight()/2.0));
        }

        text.drawString( g2d, 100, 100, 
                0, LatexText.ALIGN_TOP);
        
        text.drawString( g2d, 100, 100 + offset, 
                0, LatexText.ALIGN_BOTTOM);
        
        text.drawString( g2d, 100, 100 + 2*offset, 
                LatexText.ALIGN_LEFT, LatexText.ALIGN_CENTER);
        /*
        text.drawString(title, g2d, 100, 190, 
                LatexText.ALIGN_CENTER, LatexText.ALIGN_CENTER, LatexText.ROTATE_NONE);
        
        text.drawString(title, g2d, 100, 220, 
                LatexText.ALIGN_RIGTH, LatexText.ALIGN_TOP, LatexText.ROTATE_NONE);
        
       */
        
        
        /*
        text2.setFont(new Font("Times",Font.PLAIN,18));
        text2.setText("positive");
        text2.drawString("positive", g2d, 100, 140, 
                LatexText.ALIGN_LEFT, LatexText.ALIGN_BOTTOM
                , LatexText.ROTATE_NONE);
        r = text.getBounds(g2d);
        g2d.setColor(Color.red);
        g2d.drawRect((int) (r.getX()+bounds.getX()+100), 
                (int) (r.getY()+bounds.getY()+140), (int) r.getWidth(), (int) r.getHeight());
*/
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas2D canvas = new Canvas2D();
        canvas.setSize(600, 600);
        CanvasWidgetDebug widget = new CanvasWidgetDebug();
        canvas.addNode(widget);
        frame.add(canvas);
        frame.setSize(600, 600);
        frame.pack();
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}
