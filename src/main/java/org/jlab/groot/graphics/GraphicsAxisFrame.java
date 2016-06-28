/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.base.PadMargins;
import org.jlab.groot.math.Dimension2D;

/**
 *
 * @author gavalian
 */
public class GraphicsAxisFrame {
    
    Dimension2D          axisFrameDimension     = new Dimension2D();
    PadMargins           axisFrameMargins       = new PadMargins();
    List<GraphicsAxis>      axisFrameAxis          = new ArrayList<GraphicsAxis>();
    
    
    public GraphicsAxisFrame(){
        axisFrameAxis.add(new GraphicsAxis());
        axisFrameAxis.add(new GraphicsAxis());
        axisFrameAxis.add(new GraphicsAxis());        
        axisFrameAxis.get(1).setVertical(true);
        axisFrameAxis.get(2).setVertical(true);
    }
    
    
    public void setFrameDimensions(double xmin, double xmax, double ymin, double ymax){
        this.axisFrameDimension.getDimension(0).setMinMax(xmin, xmax);
        this.axisFrameDimension.getDimension(1).setMinMax(ymin, ymax);
        this.axisFrameAxis.get(0).setDimension((int) xmin, (int) xmax);
        this.axisFrameAxis.get(1).setDimension((int) ymax, (int) ymin);
    }
    
    public void updateMargins(Graphics2D g2d){
        double  xoffset = axisFrameAxis.get(1).getAxisBounds(g2d);
        double  yoffset = axisFrameAxis.get(0).getAxisBounds(g2d);
        axisFrameMargins.setLeftMargin( (int) xoffset);
        axisFrameMargins.setBottomMargin((int) yoffset);
        axisFrameMargins.setTopMargin(10);
        axisFrameMargins.setRightMargin(15);
    }
    
    public PadMargins  getFrameMargins(){
        return this.axisFrameMargins;
    }
    
    public void setAxisMargins(PadMargins margins){
        double xcorner = axisFrameDimension.getDimension(0).getMin() + margins.getLeftMargin();
        double ycorner = axisFrameDimension.getDimension(1).getMax() - margins.getBottomMargin();
        
        axisFrameAxis.get(0).setDimension((int) xcorner, 
                (int) (axisFrameDimension.getDimension(0).getMax() 
                        - margins.getRightMargin()));
        axisFrameAxis.get(1).setDimension((int) ycorner,
                (int) (axisFrameDimension.getDimension(1).getMin()
                        + margins.getTopMargin())
                );
    }
    
    public void drawAxis(Graphics2D g2d, PadMargins margins){
        
        double xcorner = axisFrameDimension.getDimension(0).getMin() + margins.getLeftMargin();
        double ycorner = axisFrameDimension.getDimension(1).getMax() - margins.getBottomMargin();
        /*
        axisFrameAxis.get(0).setDimension((int) xcorner, 
                (int) (axisFrameDimension.getDimension(0).getMax() 
                        - margins.getRightMargin()));
        axisFrameAxis.get(1).setDimension((int) ycorner,
                (int) (axisFrameDimension.getDimension(1).getMin()
                        + margins.getTopMargin())
                );
        */
        /*
        System.out.println(" GRAPHICS AXIS CORNERS = " + (int) xcorner
                + " - " + (int) ycorner);
        System.out.println("----> X axis " + axisFrameAxis.get(0).getDimension());
        System.out.println("----> Y axis " + axisFrameAxis.get(1).getDimension());
        */
        axisFrameAxis.get(0).drawAxis(g2d, (int) xcorner, (int) ycorner);
        axisFrameAxis.get(1).drawAxis(g2d, (int) xcorner, (int) ycorner);
    }
    
    public GraphicsAxis  getAxisX(){
        return this.axisFrameAxis.get(0);
    }
    
    public GraphicsAxis  getAxisY(){
        return this.axisFrameAxis.get(1);
    }
    
    public GraphicsAxis  getAxisZ(){
        return this.axisFrameAxis.get(2);
    }
    
    public int getAxisPointX(double value){
        return (int) axisFrameAxis.get(0).getAxisPosition(value);
    }
    
    public int getAxisPointY(double value){
        return (int) axisFrameAxis.get(1).getAxisPosition(value);
    }
    
    public int getAxisPointZ(double value){
        return (int) axisFrameAxis.get(2).getAxisPosition(value);
    }
    
}
