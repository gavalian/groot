/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.scene.text.FontWeight;
import org.jlab.groot.math.Dimension2D;

/**
 *
 * @author gavalian
 */
public class EmbeddedPad {

    Dimension2D            padDimensions = new Dimension2D();
    GraphicsAxisFrame          axisFrame = new GraphicsAxisFrame();
    Color                backgroundColor = Color.WHITE;
    Map<String,IDataSetPlotter>   padDataSets = new LinkedHashMap<String,IDataSetPlotter>();
    
    
    public EmbeddedPad(){
        
    }
    
    public EmbeddedPad(int x, int y, int width, int height){
        this.setDimension(x, y, width, height);        
    }
    
    public EmbeddedPad setDimension(int x, int y, int width, int height){
        this.padDimensions.getDimension(0).setMinMax(x,x+width);
        this.padDimensions.getDimension(1).setMinMax(y, y+height);
        return this;
    }
    
    public void draw(Graphics2D g2d){
        //update(g2d);
        
        
        Dimension2D  dim = new Dimension2D();
        int counter = 0;
        for(Map.Entry<String,IDataSetPlotter>  entry : padDataSets.entrySet()){
            if(counter==0){
                dim.copy(entry.getValue().getDataRegion());
            } else {
                dim.combine(entry.getValue().getDataRegion());
            }
            counter++;
            /*
            Dimension2D region = entry.getValue().getDataRegion();
            axisFrame.getAxis(0).setRange(
                    region.getDimension(0).getMin(),
                    region.getDimension(0).getMax()
                    );
            axisFrame.getAxis(1).setRange(
                    region.getDimension(1).getMin(),
                    region.getDimension(1).getMax()
                    );
            System.out.println(entry.getKey() + " : " + region);
            System.out.println(axisFrame.getAxis(0));*/
            //entry.getValue().draw(g2d, axisFrame);
        }
        
        update(g2d);
        g2d.setColor(Color.BLACK);
        axisFrame.getAxis(0).setRange(dim.getDimension(0).getMin(), dim.getDimension(0).getMax());
        axisFrame.getAxis(1).setRange(dim.getDimension(1).getMin(), dim.getDimension(1).getMax());        
        axisFrame.getDimension().copy(padDimensions);
        //axisFrame.update(g2d);
        axisFrame.draw(g2d, padDimensions);
        for(Map.Entry<String,IDataSetPlotter>  entry : padDataSets.entrySet()){
            entry.getValue().draw(g2d, axisFrame);
        }

    }
    
    public void update(Graphics2D g2d){
        //axisFrame.getAxis(0).setRange(4, 5);
        //axisFrame.getAxis(1).setRange(27,32);
    }
    
    public void addPlotter(IDataSetPlotter ip){
        this.padDataSets.put(ip.getName(), ip);
    }
    
    public void setAxisFontSize(int size){
        this.axisFrame.getAxis(0).getLabelFont().setFontSize(size);
        this.axisFrame.getAxis(1).getLabelFont().setFontSize(size);
    }
    
}
