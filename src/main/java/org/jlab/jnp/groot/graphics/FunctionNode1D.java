/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.F1D;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.groot.settings.GRootColorPalette;
import org.jlab.jnp.groot.settings.GRootTheme;

/**
 *
 * @author gavalian
 */
public class FunctionNode1D extends DataNode2D {
    private F1D function   = null;
    private int resolution = 400;
    
    public FunctionNode1D(F1D f1d){
        this.function = f1d;
    }
    public FunctionNode1D(F1D f1d, int res){
        this.function = f1d; resolution = res;
    }
    
    @Override
    public Rectangle2D getDataBounds( Rectangle2D dataBounds){
        
        dataBounds.setRect(
                function.getMin(), 0,
                function.getMax()-function.getMin(),
                100
               );
        return dataBounds;
    }
    
    @Override
    public IDataSet getDataSet(){ return function;}
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){ 
        Node2D parent = this.getParent();
        if(parent==null){
            System.out.println("[data node] error, has no parent\n");
            return;
        }
        int size = 4;
        
        
        GeneralPath path = new GeneralPath();
        double xstart = function.getMin();
        double xstep  = (function.getMax()-function.getMin())/resolution;
        
        for(int i = 0; i < resolution; i++){
            double x = xstart + xstep*i;
            double y = function.evaluate(x);
            //System.out.printf("%12.5f %12.5f\n",x,y);
            double xp = parent.transformX(x);
            double yp = parent.transformY(y);
            if(i==0){ path.moveTo(xp,yp); } else {                 
                path.lineTo(xp, yp);
            }
        }
        GRootColorPalette palette = GRootColorPalette.getInstance();
        int color = function.getLineColor();
        int style = function.getLineStyle();
        int width = function.getLineWidth();
        
        GRootTheme theme = GRootTheme.getInstance();

        g2d.setColor(palette.getColor(color));
        g2d.setStroke(theme.getLineStroke(style, width));
        g2d.draw(path);
    }
}
