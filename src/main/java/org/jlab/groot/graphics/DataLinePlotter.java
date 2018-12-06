/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import org.jlab.groot.base.TStyle;
import org.jlab.groot.data.DataLine;

/**
 *
 * @author gavalian
 */
public class DataLinePlotter {
    public DataLinePlotter(){
        
    }
    
    public void draw(Graphics2D g2d, DataLine line, GraphicsAxisFrame frame){
        int lineColor = line.getLineColor();
        g2d.setColor(TStyle.getColor(lineColor));
        g2d.setStroke(new BasicStroke(line.getLineWidth()));
        
        int startX = frame.getAxisPointX(line.getOriginX());
        int startY = frame.getAxisPointY(line.getOriginY());
        
        int endX = frame.getAxisPointX(line.getEndX());
        int endY = frame.getAxisPointY(line.getEndY());
        
        
        double angle = Math.atan2(endY-startY, endX - startX);
        double arrowAngleUp   = angle + Math.toRadians(line.getArrowAngle());
        double arrowAngleDown = angle - Math.toRadians(line.getArrowAngle());
        
        
        
        g2d.drawLine(startX, startY, endX, endY);
        
        if(line.getArrowSizeOrigin()>2){
            double offsetX_u = Math.cos(arrowAngleUp)*line.getArrowSizeOrigin();
            double offsetY_u = Math.sin(arrowAngleUp)*line.getArrowSizeOrigin();
            g2d.drawLine(startX, startY,  (int) (startX + offsetX_u), 
                    (int) ( startY + offsetY_u));
            double offsetX_d = Math.cos(arrowAngleDown)*line.getArrowSizeOrigin();
            double offsetY_d = Math.sin(arrowAngleDown)*line.getArrowSizeOrigin();
            g2d.drawLine(startX, startY,  (int) (startX + offsetX_d), 
                    (int) ( startY + offsetY_d));
        }
        
         double offsetX_u = Math.cos(arrowAngleUp)*line.getArrowSizeEnd();
            double offsetY_u = Math.sin(arrowAngleUp)*line.getArrowSizeEnd();
            g2d.drawLine(endX, endY,  (int) (endX - offsetX_u), 
                    (int) ( endY - offsetY_u));
            double offsetX_d = Math.cos(arrowAngleDown)*line.getArrowSizeEnd();
            double offsetY_d = Math.sin(arrowAngleDown)*line.getArrowSizeEnd ();
            g2d.drawLine(endX, endY,  (int) (endX - offsetX_d), 
                    (int) ( endY - offsetY_d));
        
    }
}
