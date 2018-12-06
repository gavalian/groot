/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

/**
 *
 * @author gavalian
 */
public class DataLine {
    
    private double originX = 0.0;
    private double originY = 0.0;
    private double endX = 0.0;
    private double endY = 0.0;
    private double arrowSizeOrigin = 0.0;
    private double    arrowSizeEnd = 0.0;
    private int          lineColor = 1;
    private int          lineWidth = 2;
    private int          lineStyle = 1;
    private double      arrowAngle = 30.0;
    
    public DataLine(){}
    
    public DataLine(double o_x, double o_y, double e_x, double e_y)
    {
        setOrigin(o_x,o_y); setEnd(e_x,e_y);
    }
    
    public final DataLine setOrigin(double x, double y){ 
        originX = x; originY = y;
        return this;
    }
    
    public final DataLine setEnd(double x, double y){
        endX = x; endY = y;
        return this;
    }
    
    public DataLine setArrowAngle(double angle){arrowAngle = angle; return this;}
    public double getArrowAngle(){ return this.arrowAngle;}
    public double getOriginX(){ return originX; }
    public double getOriginY(){ return originY; }
    public double getEndX(){ return endX; }
    public double getEndY(){ return endY; }
    
    public double getArrowSizeOrigin(){ return arrowSizeOrigin;}
    public double getArrowSizeEnd(){ return arrowSizeEnd;}
    
    public  DataLine setArrowSizeOrigin(double size){ 
        arrowSizeOrigin = size; return this;
    }
    
    public  DataLine setArrowSizeEnd(double size){ 
        arrowSizeEnd = size; return this;
    }
    
    public DataLine setLineColor(int color) { lineColor = color; return this;}
    public DataLine setLineStyle(int style) { lineStyle = style; return this;}
    public DataLine setLineWidth(int width) { lineWidth = width; return this;}
    
    public int getLineColor(){ return lineColor;}
    public int getLineStyle(){ return lineStyle;}
    public int getLineWidth(){ return lineWidth;}
}
