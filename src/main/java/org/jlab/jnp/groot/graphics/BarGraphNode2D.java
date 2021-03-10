/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.jlab.groot.data.BarGraph;
import org.jlab.groot.data.IDataSet;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class BarGraphNode2D extends DataNode2D {
    private BarGraph graph = null;
    
    public BarGraphNode2D(BarGraph gr){
        graph = gr;
    }
    
    
    @Override
    public IDataSet getDataSet(){ return graph;}
    
    @Override
    public Rectangle2D getDataBounds( Rectangle2D dataBounds){
        int count = graph.getCount();
        dataBounds.setRect(0.4, 0.0, 
                count+0.2,graph.getMax()*1.15);
        return dataBounds;
    }
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){ 
        
        Node2D parent = this.getParent();
        if(parent==null){
            System.out.println("[data node] error, has no parent\n");
            return;
        }
        
        
        //GeneralPath path = new GeneralPath();
        //double xp = h1d.getXaxis().getBinCenter(0) - h1d.getXaxis().getBinWidth(0)*0.5;
        //double yp = 0.0;
        
        //path.moveTo(parent.transformX(xp), parent.transformY(yp));
        
        GRootColorPalette palette = GRootColorPalette.getInstance();
        
        double  dataMin = graph.getMin();
        double  dataMax = graph.getMax();        
        int     entries = graph.getDataSize(0);        
        int     barsPerEntry = graph.getDataSize(1);
        int     barWidth     = graph.getBarWidth();
        
        int     entryWidth = 10;
        int     padding = graph.getPadding();
        
        for(int bin = 0; bin < entries; bin++){
        
            for(int bar = 0; bar < barsPerEntry; bar++){
                
                int barFillColor = graph.getFillColor(bar);                
                int barLineColor = graph.getFillColor(bar);
                
                double value = graph.getVector(bin).getValue(bar);
                double ycLow  = parent.transformY(0.0);
                double ycHigh = parent.transformY(value);
                double xcLow  = parent.transformX(0.5 + bin);
                double xcHigh = parent.transformX(0.5 + bin + 1);
                double width  = (xcHigh-xcLow) - 2*graph.getPadding();
                int    step   = (int) (width/barsPerEntry);
                
                //System.out.printf("bin = %d, bar = %d, value = %8.4f , x position = %8.4f\n",
                //        bin,bar,value,xcLow);
                int    xLow   = padding + step*bar + ((int) (xcLow));//((int) xcLow) + bar*barWidth;
                int    xHigh  = xLow + step;
                
                int    yLow   = (int) ycHigh;
                int    yHigh   = (int) ycLow;
                
                //System.out.printf("bin = %d, bar = %d, step = %d , xLow = %d\n",
                //        bin,bar,step,xLow);
                g2d.setColor(palette.getColor(barFillColor));
                g2d.fillRect(xLow, yLow, xHigh-xLow, yHigh-yLow);
                
                g2d.setColor(Color.DARK_GRAY);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRect(xLow, yLow, xHigh-xLow, yHigh-yLow);
                
            }


            
            //double yHigh = parent.transformY(graph.getVector(bin).);
            //g2d.setColor(c);
            //int w = (int) (pxe-pxs+2);
            //int h = (int)  Math.abs(pye-pys+2);
            //g2d.fillRect((int) pxs, (int) pys, w, h);
            /*System.out.printf("bin (%3d, %3d) = %5d %5d x %5d %5d , Color = ",
            xc,yc, (int) pxs, (int) pys,w,h);
            System.out.println(c);*/
            /*
            yp = h1d.getBinContent(i);
            path.lineTo(parent.transformX(xp), parent.transformY(yp));
            xp = h1d.getXaxis().getBinCenter(i) + h1d.getXaxis().getBinWidth(i)*0.5;
            path.lineTo(parent.transformX(xp), parent.transformY(yp));
            */            
        }
        //g2d.setColor(Color.red);
        //g2d.setStroke(new BasicStroke(2));
    }
}
