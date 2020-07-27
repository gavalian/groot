/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.IDataSet;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class GraphNode2D extends DataNode2D {

    private GraphErrors graph = null;
    private double rangePadding = 0.1;
    
    public GraphNode2D(GraphErrors gr){
        graph = gr;
    }
    
    public Rectangle2D getDataBounds( Rectangle2D dataBounds){
        
        double x0 = graph.getDataX(0);
        double xn = graph.getDataX(graph.getDataSize(0)-1);
        double y0 = graph.getMin();
        double yn = graph.getMax();
        double xr = (xn - x0);
        double yr = yn - y0;
        dataBounds.setRect( x0 - xr*rangePadding,
                y0 - yr*rangePadding,
                xr + xr*2.0*rangePadding,
                yr + yr*2.0*rangePadding);              
        return dataBounds;
    }
    
    @Override
    public IDataSet getDataSet(){ return graph;}
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){ 
        Node2D parent = this.getParent();
        if(parent==null){
            System.out.println("[data node] error, has no parent\n");
            return;
        }
        GRootColorPalette theme = GRootColorPalette.getInstance();
        int color_fill = graph.getMarkerColor();
        int color_line = graph.getLineColor();
        int marker_size = graph.getMarkerSize();
        int line_size   = graph.getLineThickness();
        
        for(int i = 0; i < graph.getDataSize(0); i++){
            double xcl = parent.transformX(graph.getDataX(i));
            double ycl = parent.transformY(graph.getDataY(i));
            
            MarkerTools.drawMarkerCyrcle(g2d, xcl, ycl, 
                    theme.getColor(color_fill), 
                    theme.getColor(color_line), 
                    marker_size, line_size, 1);
            
        }
                
    }
}
