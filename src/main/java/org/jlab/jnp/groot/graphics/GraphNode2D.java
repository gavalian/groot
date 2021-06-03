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
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.IDataSet;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class GraphNode2D extends DataNode2D {

    private GraphErrors graph   = null;
    private double rangePadding = 0.1;
    private String  drawOptions = "PE";
    
    public GraphNode2D(GraphErrors gr){
        graph = gr;
    }
    
    public GraphNode2D(GraphErrors gr, String options){
        graph = gr;
        drawOptions = options;
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
    
    
    public void drawShadow(Graphics2D g2d, Node2D parent, Color color){
        int npoints = graph.getDataSize(0)*2+1;
        int[] polygonX = new int[npoints];
        int[] polygonY = new int[npoints];
        
        int counter = 0;
        for(int i = 0; i < graph.getDataSize(0); i++){
            double xcl = parent.transformX(graph.getDataX(i));
            double yerrorHigh = parent.transformY(graph.getDataY(i)+graph.getDataEY(i));
            polygonX[counter] = (int) xcl;
            polygonY[counter] = (int) yerrorHigh;
            counter++;
        }
        
        for(int i = graph.getDataSize(0)-1; i >= 0; i--){
            double xcl = parent.transformX(graph.getDataX(i));
            double yerrorLow = parent.transformY(graph.getDataY(i)-graph.getDataEY(i));
            polygonX[counter] = (int) xcl;
            polygonY[counter] = (int) yerrorLow;
            counter++;
        }
        polygonX[npoints-1] = polygonX[0];        
        polygonY[npoints-1] = polygonY[0];
        
        g2d.setColor(color);
        g2d.fillPolygon(polygonX, polygonY, npoints);
    }
    
    public void drawLine(Graphics2D g2d, Node2D parent, Color color, int lineWidth){
        //int npoints = graph.getDataSize(0)*2+1;
        //int[] polygonX = new int[npoints];
        //int[] polygonY = new int[npoints];
        GeneralPath path = new GeneralPath();
        
        int counter = 0;
        
        for(int i = 0; i < graph.getDataSize(0); i++){
            double xcl = parent.transformX(graph.getDataX(i));
            double ycl = parent.transformY(graph.getDataY(i));
            if(i==0){
                path.moveTo(xcl, ycl);
            } else { path.lineTo(xcl, ycl);}
        }
        g2d.setColor(color);        
       g2d.setStroke(new BasicStroke(lineWidth));
       g2d.draw(path);
    }
    
    
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
        int line_width  = marker_size/3;
        int marker_style = graph.getMarkerStyle();
        
        if(drawOptions.contains("S")==true){
            this.drawShadow(g2d, parent, theme.getColor(color_fill+90));
        }
        
        if(drawOptions.contains("L")==true){
            this.drawLine(g2d, parent, theme.getColor(color_fill),line_width);
        }
        for(int i = 0; i < graph.getDataSize(0); i++){
            double xcl = parent.transformX(graph.getDataX(i));
            double ycl = parent.transformY(graph.getDataY(i));
            
            double  yerrorLow = parent.transformY(graph.getDataY(i)-graph.getDataEY(i));
            double yerrorHigh = parent.transformY(graph.getDataY(i)+graph.getDataEY(i));
            
            g2d.setStroke(new BasicStroke(line_size));
            g2d.setColor(theme.getColor(color_line));
            
            g2d.drawLine((int) xcl, (int) yerrorLow, (int) xcl, (int) yerrorHigh);
            MarkerTools.drawMarker(g2d, xcl, ycl, 
                    theme.getColor(color_fill), 
                    theme.getColor(color_line), 
                    marker_size, line_size, marker_style);
            
        }
                
    }
}
