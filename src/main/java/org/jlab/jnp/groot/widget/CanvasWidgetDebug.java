/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import javax.swing.JFrame;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.graphics.base.Canvas2D;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.graphics.base.NodeInsets;
import org.jlab.jnp.graphics.base.NodeRegion2D;
import org.jlab.jnp.groot.graphics.AxisNode2D;
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
        text.setFont(new Font("Times New Roman",Font.PLAIN,18));
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
    
    public void attributedStringDebug(Graphics2D g2d){
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
        text2.setFont(new Font("Avenir",Font.PLAIN,18));
        String title = "negative 400^2, mean = 0.4567_1";
        
        text2.setText(title);      
        int offset = 50;
        Rectangle2D r = text2.getBounds(g2d);
        for(int i = 0 ; i < 6 ; i++){
            g2d.drawOval(450,100 + i*offset,3,3);
             g2d.setColor(Color.red);
        
             g2d.drawRect((int) (450), 
                (int) (100 + i*offset), (int) r.getWidth(), (int) r.getHeight());
             g2d.drawRect((int) (450), 
                (int) (100 + i*offset), (int) r.getWidth(), (int) (r.getHeight()/2.0));
        }

        text2.drawString( g2d, 450, 100, 
                0, LatexText.ALIGN_TOP);
        
        text2.drawString( g2d, 450, 100 + offset, 
                0, LatexText.ALIGN_BOTTOM);
        
        text2.drawString( g2d, 450, 100 + 2*offset, 
                LatexText.ALIGN_LEFT, LatexText.ALIGN_CENTER);
    }
    
    
    public void axisDebug(Graphics2D g2d){
        AxisNode2D axis = new AxisNode2D(AxisNode2D.AXIS_TYPE_HORIZONTAL);
        
        Node2D     node = new Node2D(0,0,450,450);
        
        node.setBounds(50, 50, 400, 400);
        node.getInsets().set(50, 50, 50, 50);
        node.setTranslation(0.0, 0.0, 1.0, 1.0);
        NodeRegion2D region = new NodeRegion2D();
        region.set(0,0,1.0,1.0);
        axis.getAttributes().changeValue(AttributeType.AXISDRAWBOX, "false");
        axis.setTitle("Mx^2 [GeV]");
        axis.setAxisFont(new Font("Arial",Font.PLAIN,14));
        axis.setAxisTitleFont(new Font("Arial",Font.PLAIN,12));
        axis.setBounds(50, 50, 400, 400);
        
        axis.setParent(node);
        axis.setAxisRegion(region);
        axis.drawLayer(g2d, 1);
    }
    
    public void rotatedText(Graphics2D g2d){
        LatexText text = new LatexText("a",100,100);

        
        g2d.setColor(Color.red);
        g2d.drawLine(100, 100, 100, 200);
        g2d.drawLine(100, 100, 200, 100);
        
        g2d.setColor(Color.black);
        text.setText("axis X graph");
        text.drawString(g2d,150, 100, LatexText.ALIGN_CENTER,LatexText.ALIGN_BOTTOM, LatexText.ROTATE_NONE);
        text.setText("axis Y graph");
        text.drawString(g2d,100, 150, LatexText.ALIGN_CENTER,LatexText.ALIGN_TOP,LatexText.ROTATE_LEFT);
    }
    @Override
    public void drawLayer(Graphics2D g2d, int layer){ 
        
        //Node2D            parent = getParent();
     
        rotatedText(g2d);
        // --> axisDebug(g2d);
        
        //this.stringDebug(g2d);
        //this.attributedStringDebug(g2d);
        
        /*Font font = new Font("Helvetica",Font.ITALIC,14);
        AttributedString as1 = new AttributedString("12345-7890");
        as1.addAttribute(TextAttribute.FAMILY, font.getFontName());
        as1.addAttribute(TextAttribute.SIZE, font.getSize());
        System.out.println("font = " + font.getFontName());
        as1.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, 5, 7);
        g2d.drawString(as1.getIterator(), 15, 60);
        */
        /*Font font = new Font("Helvetica",Font.PLAIN,24);
        g2d.setFont(font);
        
        AttributedString attrS = new AttributedString("Trying The Drawing");
        attrS.addAttribute(TextAttribute.FONT, font);
        g2d.setFont(font);
        g2d.drawString(attrS.getIterator(), 200, 200);*/
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas2D canvas = new Canvas2D();
        canvas.setSize(900, 600);
        CanvasWidgetDebug widget = new CanvasWidgetDebug();
        canvas.addNode(widget);
        frame.add(canvas);
        frame.setSize(900, 600);
        frame.pack();
        frame.setSize(900, 600);
        frame.setVisible(true);
    }
}
