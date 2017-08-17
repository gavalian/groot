/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import java.awt.Graphics2D;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavalian
 */
public class GraphicsAxisLabels {
    
    private List<LatexText>          axisLabels   = new ArrayList<LatexText>();
    private List<Double>             axisTicks    = new ArrayList<Double>();
    private String                   axisFontName = "Avenir";
    private Integer                  axisFontSize = 12;
    
    public GraphicsAxisLabels(){
        
    }
    
    public List<Double>  getTicks(){
        return axisTicks;
    }
    
    public List<LatexText> getLabels(){
        return axisLabels;
    }
    
    public void update(double[] numbers, String[] labels){
        if(numbers.length!=labels.length){
            System.out.println("[graphicsAxisLabel] ---> error : number of ticks do not match labels.");
            return;
        }
        this.axisLabels.clear();
        this.axisTicks.clear();
        for(int loop = 0; loop < numbers.length; loop++){
            this.axisTicks.add(numbers[loop]);
            this.axisLabels.add(new LatexText(labels[loop]));
        }
    }
    /**
     * set font size for all axis labels.
     * @param size
     * @return 
     */
    public GraphicsAxisLabels setFontSize(int size){
        //for(LatexText t : this.axisLabels){
        //    t.setFontSize(size);
        //}
        this.axisFontSize = size;
        return this;
    }
    /**
     * set Font name for all axis labels.
     * @param name
     * @return 
     */
    public GraphicsAxisLabels setFontName(String name){
        for(LatexText t : this.axisLabels){
            t.setFont(name);
        }
        return this;
    }
    /**
     * updates the array with given ticks and constructs a 
     * LatexText array with strings for axis labels;
     * @param numbers 
     */
    public void update(double[] numbers){
        this.axisLabels.clear();
        this.axisTicks.clear();
        for(int loop = 0; loop < numbers.length; loop++){
            this.axisTicks.add(numbers[loop]);
        }
        int  significantFigures = this.getSignificantFigures(axisTicks);
        if(significantFigures<0) significantFigures = 0;
        for(int loop = 0; loop < axisTicks.size(); loop++){
            axisLabels.add(LatexText.createFromDouble(axisTicks.get(loop), 
                    significantFigures));
        }
    }
    
    public void updateLog(List<Double> numbers){
        this.axisLabels.clear();
        this.axisTicks.clear();
        //System.out.println(" Axis LAbels Size = " + numbers.size());
        for(int loop = 0; loop < numbers.size(); loop++){
            this.axisTicks.add(numbers.get(loop));
        }
        
        for(int loop = 0; loop < axisTicks.size(); loop++){
            int order = (int) Math.log10(this.axisTicks.get(loop));
            if(order==0){
                axisLabels.add(new LatexText("10"));
            } else {
                axisLabels.add(new LatexText(
                        String.format("10^%d", order)));
            }
        }
    }
    /**
     * updates the array with given ticks and constructs a 
     * LatexText array with strings for axis labels;
     * @param numbers 
     */
    public void update(List<Double> numbers){
        this.axisLabels.clear();
        this.axisTicks.clear();
        for(int loop = 0; loop < numbers.size(); loop++){
            this.axisTicks.add(numbers.get(loop));
        }
        int  significantFigures = this.getSignificantFigures(axisTicks);
        if(significantFigures<0) significantFigures = -1;

        for(int loop = 0; loop < axisTicks.size(); loop++){
            LatexText text = LatexText.createFromDouble(axisTicks.get(loop), 
                    significantFigures+1);
            text.setFontSize(this.axisFontSize);
            axisLabels.add(text);
        }
    }
    /**
     * calculates significant figures between two end points.
     * used to figure out how to construct the string for the labels.
     * @param array
     * @return 
     */
    public int getSignificantFigures(List<Double> array){
        if(array.size()<2) return 0;
        double min = array.get(0);
        double max = array.get(array.size()-1);
        double difference = max-min;
        int   placeOfDifference = (int) Math.floor(Math.log10(difference));
        return -placeOfDifference;
    }
    /**
     * returns combined width of all labels.
     * @param g2d
     * @return 
     */
    public int getAxisLabelsWidth(Graphics2D g2d){
        int length = 0;
        for(LatexText t : axisLabels){
            length += t.getBounds(g2d).getWidth();
        }
        return length;
    }
    
    /**
     * returns combined height of all axis labels.
     * @param g2d
     * @return 
     */
    public int getAxisLabelsHeight(Graphics2D g2d){
        int length = 0;
        for(LatexText t : axisLabels){
            length += t.getBounds(g2d).getHeight();
        }
        return length;
    }
    
    public int getAxisMaxWidth(Graphics2D g2d){
        int maxLength = 0;
        for(LatexText t : axisLabels){
            int len = (int) t.getBounds(g2d).getWidth();
            if( len>maxLength) maxLength = len;
        }
        return maxLength;
    }
    
    public int getAxisMaxHeight(Graphics2D g2d){
        int maxLength = 0;
        for(LatexText t : axisLabels){
            int len = (int) t.getBounds(g2d).getHeight();
            if( len>maxLength) maxLength = len;
        }
        return maxLength;
    }
        
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("Ticks : ( ");
        for(LatexText t : axisLabels){
            str.append(t.getTextString());
            str.append(" , ");
        }
        str.append(" )");
        return str.toString();
    }
}
