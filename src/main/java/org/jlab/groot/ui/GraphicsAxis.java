/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;
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
        this.setRange(axisRange.getMin(), axisRange.getMax());
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
        if(this.isLogarithmic==true){
            List<Double> ticks = axisRange.getDimensionTicksLog(this.numberOfMajorTicks);
            axisLabels.updateLog(ticks);
        } else {
            List<Double> ticks = axisRange.getDimensionTicks(this.numberOfMajorTicks);
            axisLabels.update(ticks);
        }
        return this;
    }

    public Dimension1D  getRange(){
        return this.axisRange;
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
        this.axisLabels.setFontName(this.axisLabelFont.getFontName());
        this.axisLabels.setFontSize(this.axisLabelFont.getFontSize());
        /* DEBUG */
        //System.out.println(" font size = " + this.axisLabelFont.getFontSize());
        this.numberOfMajorTicks = ndiv;
        if(this.isLogarithmic==true){
            List<Double> ticks = axisRange.getDimensionTicksLog(this.numberOfMajorTicks);
            axisLabels.updateLog(ticks);
        } else {
            List<Double> ticks = axisRange.getDimensionTicks(this.numberOfMajorTicks);
            this.axisLabels.update(ticks);
        }
    }
    
    public List<Double>  getAxisTicks(){
        return this.axisLabels.getTicks();
    }
    
    public List<Double>  getAxisMinorTicks(){
        if(this.isLogarithmic==false){
            return this.getAxisMinorTicksLinear();
        } else {
            return this.getAxisMinorTicksLog();
        }
    }
    
    private List<Double> getAxisMinorTicksLog(){
        List<Double>  minorTicks = new ArrayList<Double>();
        List<Double>  majorTicks = axisLabels.getTicks();
        
        for(int i = 0; i < majorTicks.size()-1; i++){
            for(int k = 2; k < 10; k++){
                double tick =  (majorTicks.get(i)*k);
                minorTicks.add(tick);
            }
        }
        return minorTicks;
    }
    
    private List<Double> getAxisMinorTicksLinear(){
        
        int nticks = this.axisLabels.getTicks().size();
        List<Double>  minorTicks = new ArrayList<Double>();
        if(nticks>=2){
            double cTick = axisLabels.getTicks().get(0);
            double nTick = axisLabels.getTicks().get(1);
            double step  = Math.abs(nTick-cTick)/4.0;
            
            for(int i = 0; i < nticks-1; i++){
                cTick = axisLabels.getTicks().get(i);
                nTick = axisLabels.getTicks().get(i+1);                
                for(int m = 0; m < 3; m++){
                    minorTicks.add(cTick+step*(m+1));
                }
            }
            
            cTick = nTick;
            int ncount = 0;
            while(cTick<this.axisRange.getMax()&&ncount<10){
                cTick += step;
                if(cTick<axisRange.getMax()) minorTicks.add(cTick);
                ncount++;
            }
            
            cTick = axisLabels.getTicks().get(0);
            ncount = 0;
            while(cTick>axisRange.getMin()&&ncount<10){
                cTick -= step;
                if(cTick>axisRange.getMin()) minorTicks.add(cTick);
            }
        }
        return minorTicks;
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
        str.append(" LOG = ");
        str.append(this.isLogarithmic);
        str.append(String.format("  (%4d x %4d ) : ->  ", (int) axisDimension.getMin(), 
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
        axis.setRange(0.0, 120.0);
        axis.setLog(true);        
        axis.show();      
        for(double d = 0; d < 120; d+=0.5){
            System.out.println( d + " fraction = " + axis.getAxisPosition(d));
        }
    }
}
