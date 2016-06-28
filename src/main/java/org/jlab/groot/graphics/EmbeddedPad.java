/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import org.jlab.groot.base.PadMargins;
import org.jlab.groot.math.Dimension2D;

/**
 *
 * @author gavalian
 */
public class EmbeddedPad {
     Dimension2D            padDimensions = new Dimension2D();
     GraphicsAxisFrame          axisFrame = new GraphicsAxisFrame();
     Color                backgroundColor = Color.WHITE;
     private  PadMargins  padMargins      = new PadMargins();
     
     public EmbeddedPad(){
        
    }
    
    public EmbeddedPad(int x, int y, int width, int height){
        this.setDimension(x, y, width, height);        
    }
    
    public final EmbeddedPad setDimension(int x, int y, int width, int height){
        this.padDimensions.getDimension(0).setMinMax(x,x+width);
        this.padDimensions.getDimension(1).setMinMax(y,y+height);
        axisFrame.setFrameDimensions(
                padDimensions.getDimension(0).getMin(),
                padDimensions.getDimension(0).getMax(),
                padDimensions.getDimension(1).getMin(),
                padDimensions.getDimension(1).getMax()
        );
        return this;
    }
    
    public void setMargins(PadMargins margins){
        this.padMargins.copy(margins);
        //System.out.println(" PAD - > " + padMargins);
    }
    
    public GraphicsAxisFrame  getAxisFrame(){
        return this.axisFrame;
    }
    
    public void draw(Graphics2D g2d){                
        //axisFrame.updateMargins(g2d);
        //axisFrame.setAxisMargins(padMargins);
        //axisFrame.updateMargins(g2d);
        axisFrame.drawAxis(g2d, padMargins);        
    }
}
