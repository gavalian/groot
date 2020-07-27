/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import org.jlab.jnp.graphics.attr.AttributeCollection;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.graphics.attr.FontAttributes;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.graphics.base.NodeRegion2D;

/**
 *
 * @author gavalian
 */
public class DataRegion extends Node2D {
    
    private boolean trackMouseMovement = false;
    private String mouseCoordinatesText = "0x0";
    private GraphicsAxis  embeddedPadAxis = null;
    private final AttributeCollection axisAttributes = 
            new AttributeCollection(new AttributeType[] { 
                AttributeType.PAD_MARGIN_TOP, AttributeType.PAD_MARGIN_LEFT,
                AttributeType.PAD_MARGIN_RIGHT, AttributeType.PAD_MARGIN_BOTTOM
            }, new String[] {"10","40","40","10"});
    
    public DataRegion(String name){
        super(100,100);
        setName(name);
        embeddedPadAxis   = new GraphicsAxis();
        embeddedPadAxis.setBoundsBind(0, 0, 1.0, 1.0);
        embeddedPadAxis.alignMode(Node2D.ALIGN_RELATIVE);
        embeddedPadAxis.setTranslation(0.0, 0.0, 1.0, 1.0);
        addNode(embeddedPadAxis);
        axisAttributes.append(embeddedPadAxis.getAttributes());
        axisAttributes.addListener(embeddedPadAxis.getAttributes());
    }
    
    public DataRegion(){
        super(100,100);
        embeddedPadAxis   = new GraphicsAxis();
        embeddedPadAxis.setBoundsBind(0, 0, 1.0, 1.0);
        embeddedPadAxis.alignMode(Node2D.ALIGN_RELATIVE);
        embeddedPadAxis.setTranslation(0.0, 0.0, 1.0, 1.0);
        addNode(embeddedPadAxis);
    }
    
    
    public void addGraphicsAxis(){
        
    }
    
    public AttributeCollection getAttributes(){
        return this.axisAttributes;
    }
    
    public GraphicsAxis getGraphicsAxis(){
        return this.embeddedPadAxis;
    }
    
    public void setAxisFont(String fontname, int fontsize, int fontface){
        this.getGraphicsAxis().getAxisX().setAxisFont(fontname, fontsize, fontface);
        this.getGraphicsAxis().getAxisY().setAxisFont(fontname, fontsize, fontface);
        FontAttributes.setMarginsByFont(this.embeddedPadAxis.getInsets(), fontsize);
    }
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){ 
        NodeRegion2D bounds = getBounds();
        /*
        int offset = 5; int round = 12;        
        g2d.setColor(new Color(200,200,200));
        g2d.drawRoundRect( (int) (bounds.getX() + offset) , 
               (int) (bounds.getY() + offset),
               (int) (bounds.getWidth() - 2*offset),
               (int) (bounds.getHeight() - 2*offset),
              round,round
                );
        */
        
        if(this.getChildren().size()>0){
            for(Node2D node : this.getChildren()){
                //System.out.println("[Embedded Pad] children : " + node.getName());
            }
        }
        
        drawChildren(g2d,layer);
        
        if(this.trackMouseMovement==true){
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("SansSerif",Font.PLAIN,18));
            g2d.drawString(mouseCoordinatesText, (int) (bounds.getX() + bounds.getWidth()*0.5),
                    (int) (bounds.getY()+bounds.getHeight()*0.5));
        }
    }
    
    public void trackMouse(boolean flag){
        this.trackMouseMovement = flag;
    }
    
    @Override
    public boolean mouseMoved(double X,double Y){
        if(trackMouseMovement==true){
            NodeRegion2D bounds = getBounds();
            if(bounds.getBounds().contains(X, Y)==true){
                this.mouseCoordinatesText = String.format("%dx%d",(int) X, (int) Y);
                return true;
            } else {
                this.mouseCoordinatesText = "";
                return true;
            }
            //return true;
        }
        return false;
    }
}
