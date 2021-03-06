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
import org.jlab.groot.data.GraphErrors;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.graphics.base.NodeInsets;
import org.jlab.jnp.graphics.base.NodeRegion2D;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class StatisticsNode2D extends Node2D {
    
    private Font           textFont = new Font("Avenir", Font.PLAIN, 12);
    private Color         textColor = Color.BLACK;
    private Color       borderColor = new Color(180,180,180);
    private Color  headerBackground = new Color(255,255,255);
    private String       textHeader = "Info";
    
    private List<String>   descriptions = new ArrayList<>();
    private List<String>     statValues = new ArrayList<>();
    
    private int          textSpacing = 0;
    private int          boxPaddingX = 15;
    private int          boxPaddingY = 6;
    private int          positionX   = 0;
    private int          positionY   = 0;
    
    public StatisticsNode2D(int x, int y) {
        super(x, y);
        positionX = x;
        positionY = y;
    }
    
    
    public void add(String desc, String stat){
        descriptions.add(desc);
        statValues.add(stat);
    }
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){
        
        GRootColorPalette theme = GRootColorPalette.getInstance();
        
        NodeRegion2D bounds = this.getParent().getBounds();
        NodeInsets insets = this.getParent().getInsets();
        //System.out.println("[StatBox] ---> " + bounds);
        FontMetrics metrics = g2d.getFontMetrics(textFont);
        
        
        double textHeight = getTextHeight(metrics);
        double textWidth  = getTextWidth(metrics);
        
        int boxHeight = boxPaddingY*3 + ((int) textHeight) + (descriptions.size()-1)*textSpacing;
        int boxWidth  = 10 + boxPaddingX*3 + ((int)textWidth) + 80; 

        int offsetX = (int) (bounds.getX() + bounds.getWidth() - boxWidth);
        int offsetY = (int) (bounds.getY() + bounds.getHeight()- boxHeight);
        g2d.setColor(this.headerBackground);
        
        double boxPositionX = bounds.getX() + bounds.getWidth()  - boxWidth  - 5 - insets.getRight();
        double boxPositionY = bounds.getY() + 5 + insets.getTop();
        
        g2d.fillRoundRect((int) boxPositionX, (int) boxPositionY,(int) boxWidth,(int) boxHeight, 10,10);
        
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(borderColor);
        
        g2d.drawRoundRect((int)             
                (int) boxPositionX, (int) boxPositionY
                ,(int) boxWidth,(int) boxHeight,10,10);
        
        int markerSize = 12;
        
        for(int i = 0; i < descriptions.size(); i++){
                        
            
            //g2d.drawLine((int) xcl, (int) yerrorLow, (int) xcl, (int) yerrorHigh);
            double tX = boxPositionX + boxPaddingX;//positionX + bounds.getX() + boxPaddingX;
            double tY = boxPositionY + boxPaddingY + metrics.getHeight() + (metrics.getHeight() + textSpacing)*i;                       
            g2d.setColor(textColor);
            g2d.setFont(textFont);
            g2d.drawString(descriptions.get(i), (int) tX, (int) tY);
            g2d.drawString(statValues.get(i), (int) (tX+80), (int) tY);            
        }
               
    }
    
    private int getTextWidth(FontMetrics fm){
        int minWidth = 0;
        for(int i = 0; i < descriptions.size();i++){
            int width = fm.stringWidth(descriptions.get(i) + fm.stringWidth(statValues.get(i)));
            if(width > minWidth) minWidth = width;
        }
        return minWidth;
    }
    
    private int getTextHeight(FontMetrics fm){
        int descend = fm.getDescent();
        int ascend  = fm.getAscent();
        int height  = fm.getHeight();
        int totalHeight = 0;
        
        //System.out.println(" for text : " + text + String.format(" desc = %5d, ascd = %5d, heigth = %5d \n",
        //        descend,ascend,height));
        return height*this.descriptions.size();
    }
}
