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
import org.jlab.jnp.graphics.base.NodeRegion2D;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class LegendNode2D extends Node2D {
    private Font           textFont = new Font("Avenir", Font.PLAIN, 18);
    private Color         textColor = Color.BLACK;
    private Color       borderColor = new Color(180,180,180);
    private Color  headerBackground = new Color(255,255,255);
    private String       textHeader = "Info";
    
    private List<String>   descriptions = new ArrayList<>();
    private List<GraphErrors>  graphs = new ArrayList<>();
    
    private int          textSpacing = 1;
    private int          boxPaddingX = 15;
    private int          boxPaddingY = 6;
    private int          positionX   = 0;
    private int          positionY   = 0;
    
    public LegendNode2D(int x, int y) {
        super(x, y);
        positionX = x;
        positionY = y;
    }
    
    public void add(GraphErrors graph, String description){
        this.graphs.add(graph);
        this.descriptions.add(description);
    }
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){
        
        GRootColorPalette theme = GRootColorPalette.getInstance();
        
        NodeRegion2D bounds = getBounds();
        //System.out.println("[Pave Text] ---> " + bounds);
        FontMetrics metrics = g2d.getFontMetrics(textFont);
        
       
        double textHeight = getTextHeight(metrics);
        double textWidth  = getTextWidth(metrics);
        
        int boxHeight = boxPaddingY*3 + ((int) textHeight) + (descriptions.size()-1)*textSpacing;
        int boxWidth  = 10 + boxPaddingX*3 + ((int)textWidth); 

        g2d.setColor(this.headerBackground);
        
        g2d.fillRoundRect((int) (bounds.getX() + this.positionX),
                (int) (bounds.getY() + this.positionY),(int) boxWidth,(int) boxHeight, 10,10);
        
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(borderColor);
        
        g2d.drawRoundRect((int)
                (bounds.getX() + positionX),
                (int) (bounds.getY() + positionY),(int) boxWidth,(int) boxHeight,10,10);
        
        int markerSize = 12;
        
        for(int i = 0; i < descriptions.size(); i++){
            
            int color_fill = graphs.get(i).getMarkerColor();
            int color_line = graphs.get(i).getLineColor();
            int marker_size = graphs.get(i).getMarkerSize();
            int line_size   = graphs.get(i).getLineThickness();
            int marker_style = graphs.get(i).getMarkerStyle();
            
            //g2d.drawLine((int) xcl, (int) yerrorLow, (int) xcl, (int) yerrorHigh);
            
            
           
            double tX = positionX + bounds.getX() + markerSize + boxPaddingX*2;
            double tY = positionY + bounds.getY() + boxPaddingY + metrics.getHeight() + (metrics.getHeight() + textSpacing)*i;
            MarkerTools.drawMarker(g2d, (int) (tX-marker_size/2.0-boxPaddingX),
                    (int) (tY-markerSize/2.0), 
                    theme.getColor(color_fill), 
                    theme.getColor(color_line), 
                    marker_size, line_size, marker_style);
            
            g2d.setColor(textColor);
            g2d.setFont(textFont);
            g2d.drawString(descriptions.get(i), (int) tX, (int) tY);
            
        }
       
        
    }
    
    private int getTextWidth(FontMetrics fm){
        int minWidth = 0;
        for(int i = 0; i < descriptions.size();i++){
            int width = fm.stringWidth(descriptions.get(i));
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
