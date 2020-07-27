/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import org.jlab.groot.data.IDataSet;
import org.jlab.jnp.graphics.attr.AttributeCollection;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.graphics.attr.ColorPalette;
import org.jlab.jnp.graphics.attr.LineStyles;
import org.jlab.jnp.graphics.attr.Theme;
import org.jlab.jnp.graphics.base.Node2D;

/**
 *
 * @author gavalian
 */
public class DataNode2D extends Node2D {
    
    double[] xc = new double[]{ -0.08, 0.025, 0.07, 0.09};
    double[] yc = new double[]{ -0.08, 0.02, 0.075, 0.08};
    private String drawOptions = "";
    
    private AttributeCollection attributes = null;//new AttributeCollection();
    //ColorPalette pallete = new ColorPalette();
    
    public DataNode2D(){
        super(20,20);
        attributes = new AttributeCollection();
    }
    
    public DataNode2D(String options){
        super(20,20);
        drawOptions = options;
        attributes = new AttributeCollection();
    }
    
    public DataNode2D(String options, AttributeType[] attr, String[] data){
       super(20,20);
       drawOptions = options;
       attributes = new AttributeCollection(attr,data);
    }
    
    public IDataSet getDataSet(){ return null;}
    
    public AttributeCollection getAttributes(){ return this.attributes;}
    
    public String getOptions(){ return this.drawOptions;}
    
    public Rectangle2D getDataBounds( Rectangle2D dataBounds){
        dataBounds.setRect(-0.1, -0.1, 0.2, 0.2);
        return dataBounds;
    }
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){ 
        Node2D parent = this.getParent();
        if(parent==null){
            System.out.println("[data node] error, has no parent\n");
            return;
        }
        int size = 4;
        
        GeneralPath path = new GeneralPath();
        for(int i = 0; i < xc.length; i++){
            double xcl = parent.transformX(xc[i]);
            double ycl = parent.transformY(yc[i]);
            if(i==0){ path.moveTo(xcl,ycl); } else {                 
                path.lineTo(xcl, ycl);
            }
        }
        Theme theme = Theme.getInstance();
        g2d.setColor(theme.getPalette().getColor(2));
        g2d.setStroke(LineStyles.getStrokeWidth(3));
        g2d.draw(path);
        
        for(int i = 0; i < xc.length; i++){
            double xcl = parent.transformX(xc[i]);
            double ycl = parent.transformY(yc[i]);
            
            MarkerTools.drawMarkerCyrcle(g2d, xcl, ycl, 
                    theme.getPalette().getColor(1), 
                    theme.getPalette().getColor(2), 
                    8, 1, 1);
            /*
            g2d.setColor(Color.BLUE);
            g2d.fillOval((int) (xcl - size/2),(int) (ycl - size/2), size, size);
            g2d.setColor(Color.BLACK);
            g2d.drawOval((int) (xcl - size/2),(int) (ycl - size/2), size, size);*/
            //System.out.printf("plotting (%2d) : %8.4f %8.4f \n", i, xcl, ycl);
        }
        
    }
}
