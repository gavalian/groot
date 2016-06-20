/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import java.awt.Graphics2D;
import java.util.List;
import org.jlab.groot.base.FontProperties;
import org.jlab.groot.math.Dimension1D;

/**
 *
 * @author gavalian
 */
public class GraphicsAxis {
    
    private final  Dimension1D                  axisRange = new Dimension1D();
    private final  Dimension1D              axisDimension = new Dimension1D();
    private final  GraphicsAxisLabels          axisLabels = new GraphicsAxisLabels();
    private        int                 numberOfMajorTicks = 10;
    private        Boolean                  isLogarithmic = false;
    
    private final  FontProperties           axisLabelFont     = new FontProperties();
    private final  FontProperties           axisTitleFont     = new FontProperties();
        
    /**
     * default 
     */
    public GraphicsAxis(){        
        this.setDimension( 0, 100);
        this.setRange( 0.0, 1.0);
        this.axisLabels.setFontName("Avenir");
        this.axisLabels.setFontSize(12);
    }
    /**
     * sets the dimension for the axis for plotting.
     * @param xmin
     * @param xmax
     * @return 
     */
    public final GraphicsAxis setDimension(int xmin, int xmax){
        this.axisDimension.setMinMax(xmin, xmax);
        return this;
    }
    
    public Dimension1D getDimension(){
        return this.axisDimension;
    }
    /**
     * Sets logarithmic scale for the axis
     * @param logflag 
     */
    public void setLog(boolean logflag){
        this.isLogarithmic = logflag;
        this.axisRange.setLog(logflag);
    }
    /**
     * returns the logarithmic properties of the axis
     * @return 
     */
    public boolean getLog(){
        return this.isLogarithmic;
    }
    /**
     * Sets the range of the axis, it also updates the axis labels.
     * @param min
     * @param max
     * @return 
     */
    public final GraphicsAxis setRange(double min, double max){
        this.axisRange.setMinMax(min, max);
    
        List<Double> ticks = axisRange.getDimensionTicks(this.numberOfMajorTicks);
        /*
        System.out.println("--->  list size = " + ticks.size());
        for(Double t : ticks){
            System.out.println(" *  " + t);
        }*/
        axisLabels.update(ticks);
        return this;
    }

    public Dimension1D  getRange(){
        return this.axisDimension;
    }
    /**
     * prints string representation of the axis.
     */
    public void show(){
        System.out.println(this.toString());
    }
    /**
     * returns position along the axis, the dimension of the 
     * axis has to be set.
     * @param value
     * @return 
     */
    public double getAxisPosition(double value){
        double fraction = this.axisRange.getFraction(value);
        return this.axisDimension.getPoint(fraction);        
    }
    /**
     * returns properties of the title font
     * @return 
     */
    public FontProperties   getTitleFont(){
        return this.axisTitleFont;
    }
    /**
     * returns properties for the label fonts.
     * @return 
     */
    public FontProperties   getLabelFont(){
        return this.axisLabelFont;
    }
    
    
    public int  getSize(Graphics2D g2d, boolean vertical){
        if(vertical==true){
            return this.axisLabels.getAxisMaxWidth(g2d);
        } else {
            return this.axisLabels.getAxisMaxHeight(g2d);
        }
    }
    
    public int   getAxisDivisions(){
        return this.numberOfMajorTicks;
    }
    
    public void  setAxisDivisions(int ndiv){
        this.numberOfMajorTicks = ndiv;
        List<Double> ticks = axisRange.getDimensionTicks(this.numberOfMajorTicks);
        this.axisLabels.update(ticks);
    }
    
    public List<Double>  getAxisTicks(){
        return this.axisLabels.getTicks();
    }
    
    public double  getLabelFraction(Graphics2D g2d, boolean isVertical){
        double totalWidth = 0.0;
        if(isVertical==true){
            totalWidth = this.axisLabels.getAxisLabelsHeight(g2d);
        } else {
            totalWidth = this.axisLabels.getAxisLabelsWidth(g2d);
        }
        return totalWidth/axisDimension.getLength();
    }
    
    public List<LatexText>  getAxisLabels(){
        return this.axisLabels.getLabels();
    }
    /**
     * returns string representation of the axis.
     * @return 
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("Axis : ");
        str.append(String.format("(%4d x %4d ) : ->  ", (int) axisDimension.getMin(), 
                (int) axisDimension.getMax()));
        str.append(axisLabels.toString());
        return str.toString();
    }
    /**
     * main program for testing the axis
     * @param args 
     */
    public static void main(String[] args){
        GraphicsAxis  axis = new GraphicsAxis();
        axis.setDimension(20, 100);
        axis.setRange(0.45, 0.87);
        axis.show();
        
    }
}
