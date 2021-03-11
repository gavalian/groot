/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author gavalian
 */
public class MarkerTools {
    
    public static void drawMarker(Graphics2D g2d, double x, double y, Color fillColor, Color lineColor, int fillSize, int lineSize, int type){
        switch(type){
            case 1: MarkerTools.drawMarkerCyrcle(g2d, x, y, fillColor, lineColor, fillSize, lineSize, type); break;
            case 2: MarkerTools.drawMarkerRectangle(g2d, x, y, fillColor, lineColor, fillSize, lineSize, type); break;
            case 3: MarkerTools.drawMarkerDiamond(g2d, x, y, fillColor, lineColor, fillSize, lineSize, type); break;
            case 4: MarkerTools.drawMarkerTriangle(g2d, x, y, fillColor, lineColor, fillSize, lineSize, type); break;
            case 5: MarkerTools.drawMarkerTriangleUpsideDown(g2d, x, y, fillColor, lineColor, fillSize, lineSize, type); break;
            case 6: MarkerTools.drawMarkerTriangleLeft(g2d, x, y, fillColor, lineColor, fillSize, lineSize, type); break;
            case 7: MarkerTools.drawMarkerTriangleRight(g2d, x, y, fillColor, lineColor, fillSize, lineSize, type); break;
            default: MarkerTools.drawMarkerCyrcle(g2d, x, y, fillColor, lineColor, fillSize, lineSize, type);
        }
    }
    
    public static void drawMarkerCyrcle(Graphics2D g2d, double x, double y, Color fillColor, Color lineColor, int fillSize, int lineSize, int type){
        g2d.setColor(fillColor);
        g2d.fillOval((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);
        if(lineColor!=null){
            g2d.setColor(lineColor);
            g2d.setStroke(new BasicStroke(lineSize));
            g2d.drawOval((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);
        }
    }
    
    public static void drawMarkerRectangle(Graphics2D g2d, double x, double y, Color fillColor, Color lineColor, int fillSize, int lineSize, int type){
        g2d.setColor(fillColor);
        g2d.fillRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);
        if(lineColor!=null){
            g2d.setColor(lineColor);
            g2d.setStroke(new BasicStroke(lineSize));
            g2d.drawRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);
        }
    }
    
    public static void drawMarkerDiamond(Graphics2D g2d, double x, double y, Color fillColor, Color lineColor, int fillSize, int lineSize, int type){
        g2d.setColor(fillColor);
        //g2d.fillRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);

        int[] xPoints = new int[5];
        int[] yPoints = new int[5]
                ;
        yPoints[0] = (int) (y-fillSize/2);
        xPoints[0] = (int) (x);
        
        //System.out.println(" x = " + x + " y = " + y);
        xPoints[1] = (int) (x + fillSize/2);
        yPoints[1] = (int) (y );//+ fillSize/2) ;//(x - fillSize/2);
        
        xPoints[2] = (int) (x );//+ fillSize/2);
        yPoints[2] = (int) (y + fillSize/2) ;//(x - fillSize/2);
        
        yPoints[3] = (int) (y);//-fillSize/2);
        xPoints[3] = (int) (x - fillSize/2);
        
        yPoints[4] = (int) (y - fillSize/2);
        xPoints[4] = (int) (x);//- fillSize/2);
        
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        if(lineColor!=null){
            g2d.setColor(lineColor);
            g2d.setStroke(new BasicStroke(lineSize));
            g2d.drawPolygon(xPoints, yPoints, 4);
            //g2d.drawRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);
        }
    }
    
    public static void drawMarkerTriangle(Graphics2D g2d, double x, double y, Color fillColor, Color lineColor, int fillSize, int lineSize, int type){
        g2d.setColor(fillColor);
        //g2d.fillRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);

        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        yPoints[0] = (int) (y-fillSize/2);
        xPoints[0] = (int) (x);
        
        //System.out.println(" x = " + x + " y = " + y);
        xPoints[1] = (int) (x - fillSize/2);
        yPoints[1] = (int) (y + fillSize/2) ;//(x - fillSize/2);
        
        xPoints[2] = (int) (x + fillSize/2);
        yPoints[2] = (int) (y + fillSize/2) ;//(x - fillSize/2);
        
        yPoints[3] = (int) (y-fillSize/2);
        xPoints[3] = (int) (x);
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        if(lineColor!=null){
            g2d.setColor(lineColor);
            g2d.setStroke(new BasicStroke(lineSize));
            g2d.drawPolygon(xPoints, yPoints, 4);
            //g2d.drawRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);
        }
    }
    
    public static void drawMarkerTriangleUpsideDown(Graphics2D g2d, double x, double y, Color fillColor, Color lineColor, int fillSize, int lineSize, int type){
        g2d.setColor(fillColor);
        //g2d.fillRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);

        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        yPoints[0] = (int) (y+fillSize/2);
        xPoints[0] = (int) (x);
        
        //System.out.println(" x = " + x + " y = " + y);
        xPoints[1] = (int) (x - fillSize/2);
        yPoints[1] = (int) (y - fillSize/2) ;//(x - fillSize/2);
        
        xPoints[2] = (int) (x + fillSize/2);
        yPoints[2] = (int) (y - fillSize/2) ;//(x - fillSize/2);
        
        yPoints[3] = (int) (y+fillSize/2);
        xPoints[3] = (int) (x);
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        if(lineColor!=null){
            g2d.setColor(lineColor);
            g2d.setStroke(new BasicStroke(lineSize));
            g2d.drawPolygon(xPoints, yPoints, 4);
            //g2d.drawRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);
        }
    }
    
        public static void drawMarkerTriangleLeft(Graphics2D g2d, double x, double y, Color fillColor, Color lineColor, int fillSize, int lineSize, int type){
        g2d.setColor(fillColor);
        //g2d.fillRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);

        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        
        yPoints[0] = (int) (y);//+fillSize/2);
        xPoints[0] = (int) (x-fillSize/2);
        
        //System.out.println(" x = " + x + " y = " + y);
        xPoints[1] = (int) (x + fillSize/2);
        yPoints[1] = (int) (y - fillSize/2) ;//(x - fillSize/2);
        
        xPoints[2] = (int) (x + fillSize/2);
        yPoints[2] = (int) (y + fillSize/2) ;//(x - fillSize/2);
        
        yPoints[3] = (int) (y);//+fillSize/2);
        xPoints[3] = (int) (x - fillSize/2);
        
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        if(lineColor!=null){
            g2d.setColor(lineColor);
            g2d.setStroke(new BasicStroke(lineSize));
            g2d.drawPolygon(xPoints, yPoints, 4);
            //g2d.drawRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);
        }
    }
    
        public static void drawMarkerTriangleRight(Graphics2D g2d, double x, double y, Color fillColor, Color lineColor, int fillSize, int lineSize, int type){
        g2d.setColor(fillColor);
        //g2d.fillRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);

        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        
        yPoints[0] = (int) (y);//+fillSize/2);
        xPoints[0] = (int) (x+fillSize/2);
        
        //System.out.println(" x = " + x + " y = " + y);
        xPoints[1] = (int) (x - fillSize/2);
        yPoints[1] = (int) (y + fillSize/2) ;//(x - fillSize/2);
        
        xPoints[2] = (int) (x - fillSize/2);
        yPoints[2] = (int) (y - fillSize/2) ;//(x - fillSize/2);
        
        yPoints[3] = (int) (y);//+fillSize/2);
        xPoints[3] = (int) (x + fillSize/2);
        
        g2d.fillPolygon(xPoints, yPoints, 4);
        
        if(lineColor!=null){
            g2d.setColor(lineColor);
            g2d.setStroke(new BasicStroke(lineSize));
            g2d.drawPolygon(xPoints, yPoints, 4);
            //g2d.drawRect((int) (x - fillSize/2), (int) (y-fillSize/2), fillSize, fillSize);
        }
    }
}
