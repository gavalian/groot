/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.graphics.base.NodeRegion2D;

/**
 *
 * @author gavalian
 */
public class PaveText extends Node2D {
    
    private Font           textFont = new Font("Helvetica", Font.PLAIN, 18);
    private Color         textColor = Color.BLACK;
    private Color       borderColor = new Color(180,180,180);
    private Color  headerBackground = new Color(255,255,255);
    private String       textHeader = "Info";
    private List<String> textStrings = new ArrayList<>();
    
    public PaveText(String text, int x, int y){
        super(x,y);
        //setBackgroundColor(240,240,240);
        setName("pave_text");
        textStrings.add(text);
    }
    
    public void setFont(Font font){
        this.textFont = font;
    }
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){
        
        NodeRegion2D bounds = getBounds();
        //System.out.println("[Pave Text] ---> " + bounds);
        FontMetrics metrics = g2d.getFontMetrics(textFont);
        
        double textHeight = metrics.getHeight();
        double textWidth  = metrics.stringWidth(textStrings.get(0));
        double  width = textWidth + 10;
        double height = textHeight + 5;
        
        g2d.setColor(this.headerBackground);
        g2d.fillRect((int) bounds.getX(),(int) bounds.getY(),(int) width,(int) height);
        
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(borderColor);
        g2d.drawRect((int) bounds.getX(),(int) bounds.getY(),(int) width,(int) height);
        
        g2d.setColor(textColor);
        g2d.setFont(textFont);
        g2d.drawString(textStrings.get(0), (int) (bounds.getX()+5), (int) (bounds.getY()+textHeight-5));
        this.setBounds(bounds.getX(), bounds.getY(), width, height);
        getTextHeight(metrics,textStrings.get(0));
        //System.out.println(bounds.toString());
        //g2d.setColor(Color.BLACK);
        //g2d.setStroke(new BasicStroke(1));
        //g2d.drawRect((int) (bounds.getX()+2), (int) (bounds.getY()+2)
        //        ,(int) (bounds.getWidth()-4), (int) (bounds.getHeight()-4));
        
    }
    
    public int getTextHeight(FontMetrics fm, String text){
        int descend = fm.getDescent();
        int ascend  = fm.getAscent();
        int height  = fm.getHeight();
        //System.out.println(" for text : " + text + String.format(" desc = %5d, ascd = %5d, heigth = %5d \n",
        //        descend,ascend,height));
        return 0;
    }
}
