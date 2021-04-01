/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.IDataSet;
import org.jlab.jnp.groot.settings.GRootTheme;

/**
 *
 * @author gavalian
 */
public class Legend extends PaveText {
    
    private List<IDataSet>  datasets = new ArrayList<>();
    
    public Legend(int x, int y){
        super(x,y);
        left(35);
    }
    
    public Legend add(IDataSet ds, String description){
        this.datasets.add(ds);
        this.addLine(description);
        return this;
    }
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){
        
       if(paveStyle == PaveTextStyle.MULTILINE) drawLayerMultiLine(g2d);
       if(paveStyle == PaveTextStyle.ONELINE) drawLayerOneLine(g2d);
       
       List<Point2D.Double> markers = this.getTextPositions();
       GRootTheme theme = GRootTheme.getInstance();

       int markerSize = (int) ( textFont.getSize()*0.7);
                   
       for(int i = 0; i < datasets.size(); i++){
           IDataSet ds = datasets.get(i);
           int linecolor = ds.getAttributes().getLineColor();
           int fillcolor = ds.getAttributes().getFillColor();
           int lineWidth = ds.getAttributes().getLineWidth();

           Color colorLine = theme.getPalette().getColor(linecolor);
           Color colorFill = theme.getPalette().getColor(fillcolor);
           Point2D.Double  position = markers.get(i);
           if(ds instanceof H1F){
               MarkerTools.drawMarkerRectangle(g2d, (int) position.x, position.y, colorFill, colorLine, markerSize, lineWidth, 0);
           }
           
           if(ds instanceof GraphErrors){
               int markerStyle = ((GraphErrors) ds).getMarkerStyle();
               int markerColor = ((GraphErrors) ds).getMarkerColor();
               Color mc   = theme.getPalette().getColor(markerColor);
               MarkerTools.drawMarker(g2d, (int) position.x, position.y, mc, colorLine, markerSize, lineWidth, markerStyle);
           }
           
       }             
       
    }
    
}
