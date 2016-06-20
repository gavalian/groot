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
import javafx.scene.text.FontWeight;
import org.jlab.groot.math.Dimension2D;

/**
 *
 * @author gavalian
 */
public class EmbeddedPad {

    Dimension2D      padDimensions = new Dimension2D();
    GraphicsAxisFrame    axisFrame = new GraphicsAxisFrame();
    Color                backgroundColor = Color.WHITE;
    
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
        update(g2d);
        g2d.setColor(Color.BLACK);                
        axisFrame.draw(g2d, padDimensions);        
    }
    
    public void update(Graphics2D g2d){
        axisFrame.getAxis(0).setRange(4, 5);
        axisFrame.getAxis(1).setRange(27,32);
    }
}
