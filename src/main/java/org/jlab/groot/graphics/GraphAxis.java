/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.base.FontProperties;
import org.jlab.groot.math.Dimension1D;
import org.jlab.groot.math.Dimension2D;
import org.jlab.groot.ui.LatexText;

/**
 *
 * @author gavalian
 */
public class GraphAxis {
    
    private final  Dimension1D                  axisRange   = new Dimension1D();
    private final  Dimension1D              axisDimension   = new Dimension1D();
    
    private        int                 numberOfMajorTicks   = 10;
    private        Boolean                  isLogarithmic   = false;
    private        Boolean                  isVertical      = false;
    
    private        int                      axisTitleOffset = 6;
    private        int                      axisTextOffset  = 5;
    private        int                      axisTicksLength = 8;
    
    private final  FontProperties           axisLabelFont     = new FontProperties();
    private final  FontProperties           axisTitleFont     = new FontProperties();
    private final  LatexText                axisTitle         = new LatexText("");
    private final  GraphicsAxisTicks        axisTicks         = new GraphicsAxisTicks();
    
    /**
     * default 
     */
    public GraphAxis(){        
        this.setDimension( 0, 100);
        this.setRange( 0.0, 1.0);
        //this.axisLabels.setFontName("Avenir");
        //this.axisLabels.setFontSize(12);
    }
    /**
     * sets the dimension for the axis for plotting.
     * @param xmin
     * @param xmax
     * @return 
     */
    public final GraphAxis setDimension(int xmin, int xmax){
        this.axisDimension.setMinMax(xmin, xmax);
        return this;
    }
    
    public Dimension1D getDimension(){
        return this.axisDimension;
    }
   
    /**
     * Sets the range of the axis, it also updates the axis labels.
     * @param min
     * @param max
     * @return 
     */
    public final GraphAxis setRange(double min, double max){
        this.axisRange.setMinMax(min, max);
        if(this.isLogarithmic==true){
            List<Double> ticks = axisRange.getDimensionTicksLog(this.numberOfMajorTicks);
            //axisLabels.updateLog(ticks);
        } else {
            List<Double> ticks = axisRange.getDimensionTicks(this.numberOfMajorTicks);
            //axisLabels.update(ticks);
        }
        return this;
    }

    public void setTitle(String title){
        this.axisTitle.setText(title);
        this.axisTitle.setFont(this.axisTitleFont.getFontName());
        this.axisTitle.setFontSize(this.axisTitleFont.getFontSize());
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
        if(axisDimension.getMin()<axisDimension.getMax()){
            return this.axisDimension.getPoint(fraction);     
        } else {
            return axisDimension.getMin() - 
                    fraction*Math.abs(axisDimension.getMax()-axisDimension.getMin());
        }
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
    
    public void setAxisFont(String fontname){
        axisLabelFont.setFontName(fontname);
        
    }
    public int  getSize(Graphics2D g2d, boolean vertical){
        if(vertical==true){
            //return this.axisLabels.getAxisMaxWidth(g2d);
        } else {
            //return this.axisLabels.getAxisMaxHeight(g2d);
        }
        return 0;
    }
    
    public int   getAxisDivisions(){
        return this.numberOfMajorTicks;
    }
    
    public void setVertical(boolean flag){
        this.isVertical = true;
    }
    
    public void  setAxisDivisions(int ndiv){
        this.numberOfMajorTicks = ndiv;
    }
    
    public List<Double>  getAxisTicks(){
        return null;//this.axisLabels.getTicks();
    }
    
    public int getAxisBounds(Graphics2D g2d){
        
        double axisBounds = 0.0;
        List<LatexText> axisTexts = axisTicks.getAxisTexts();
        
        double maxW = 0.0;
        double maxH = 0.0;
        for(LatexText text : axisTexts){
            Rectangle2D bounds = text.getBounds(g2d);
            if(bounds.getWidth()>maxW) maxW = bounds.getWidth();
            if(bounds.getHeight()>maxH) maxH = bounds.getHeight();
        }
        if(this.isVertical==true){
            axisBounds = maxW + axisTextOffset;
        }  else {
            axisBounds = maxH + axisTextOffset;
        }
        
        return (int) axisBounds;
    }
    
    public void drawAxis(Graphics2D g2d, int x, int y){
        
        g2d.setColor(Color.BLACK);        
        List<Double>  ticks = axisRange.getDimensionTicks(10);
        //GraphicsAxisString  gStrings = new GraphicsAxisString();        
        //gStrings.init(ticks);
        axisTicks.init(ticks);
        
        List<LatexText>  texts = axisTicks.getAxisTexts();
        
        if(this.isVertical==false){
            g2d.drawLine((int)axisDimension.getMin(),y,(int)axisDimension.getMax(),y);
            for(int i = 0; i < ticks.size(); i++){
                double tick = this.getAxisPosition(ticks.get(i));
                g2d.drawLine((int) tick,y,(int) tick,y-this.axisTicksLength);
                texts.get(i).drawString(g2d, (int) tick, y + this.axisTextOffset, 1, 0);                
            }
            double midpoint = axisRange.getMin() + 0.5*this.axisRange.getLength();
            //System.out.println(" Axis midpoint = " + (int) getAxisPosition(midpoint)
            //+ " " + y);
            axisTitle.drawString(g2d, (int) getAxisPosition(midpoint),y,1,0);
        } else {
            g2d.drawLine(x,(int)axisDimension.getMin(),x,(int)axisDimension.getMax());
            for(int i = 0; i < ticks.size(); i++){
                double tick = this.getAxisPosition(ticks.get(i));
                g2d.drawLine(x,(int) tick,x+this.axisTicksLength,(int) tick);
                texts.get(i).drawString(g2d, x-this.axisTextOffset, (int) tick, 2, 1);
            }
        }
        
    }
    /*
    public List<LatexText>  getAxisStrings(){
        return this.axisLabels.getLabels();
    }*/
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
        //str.append(axisLabels.toString());
        return str.toString();
    }
    /**
     * main program for testing the axis
     * @param args 
     */
    public static void main(String[] args){
        GraphAxis  axis = new GraphAxis();
        axis.setDimension(20, 100);
        axis.setRange(0.0, 120.0);
        //axis.setLog(true);        
        axis.show();      
        for(double d = 0; d < 120; d+=0.5){
            System.out.println( d + " fraction = " + axis.getAxisPosition(d));
        }
    }
    
    public static class GraphicsAxisTicks {
        
        List<LatexText>  axisTexts = new ArrayList<LatexText>();
        List<Double>     axisTicks = new ArrayList<Double>();
        FontProperties   axisFontProperty = new FontProperties();
        
        void GraphicsAxisString(){
            
        }
        
        
        public List<Double>  getAxisTicks(){
            return this.axisTicks;
        }
        
        public List<LatexText> getAxisTexts(){
            return this.axisTexts;
        }
        
        public void updateFont(FontProperties fp){
            this.axisFontProperty.setFontName(fp.getFontName());
            this.axisFontProperty.setFontSize(fp.getFontSize());
        }
        
        public void init(List<Double> ticks){
        
            axisTexts.clear();
            axisTicks.clear();
            
            int  significantFigures = this.getSignificantFigures(ticks);
            if(significantFigures<0) significantFigures = -1;
            for(int i = 0; i < ticks.size(); i++){                
                axisTicks.add(ticks.get(i));
            }
            
            
            for(int i = 0; i < axisTicks.size(); i++){
                
                LatexText text = LatexText.createFromDouble(axisTicks.get(i), 
                        significantFigures+1);                                
                text.setFont(this.axisFontProperty.getFontName());
                text.setFontSize(this.axisFontProperty.getFontSize());
                axisTexts.add(text);
            }
        }
        
        public int getSignificantFigures(List<Double> array){
            if(array.size()<2) return 0;
            double min = array.get(0);
            double max = array.get(array.size()-1);
            double difference = max-min;
            int   placeOfDifference = (int) Math.floor(Math.log(difference) / Math.log(10));
            return -placeOfDifference;
        }
    }
}
