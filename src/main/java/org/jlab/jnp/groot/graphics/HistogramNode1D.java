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
import org.jlab.groot.data.*;
import org.jlab.jnp.graphics.attr.LineStyles;
import org.jlab.jnp.graphics.attr.Theme;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.groot.settings.GRootColorPalette;
/**
 *
 * @author gavalian
 */
public class HistogramNode1D extends DataNode2D {
    private H1F h1d = null;
    
    public HistogramNode1D(H1F data){
        h1d = data;
    }
    
    public Rectangle2D getDataBounds( Rectangle2D dataBounds){
        
        dataBounds.setRect(h1d.getXaxis().min(), 0.0, 
                h1d.getXaxis().max()-h1d.getXaxis().min(),
                h1d.getMax()*1.15);
        return dataBounds;
    }
    
    public IDataSet getDataSet(){ return h1d;}
    
    @Override
    public void drawLayer(Graphics2D g2d, int layer){ 
        Node2D parent = this.getParent();
        if(parent==null){
            System.out.println("[data node] error, has no parent\n");
            return;
        }
        
        
        GeneralPath path = new GeneralPath();
        double xp = h1d.getXaxis().getBinCenter(0) - h1d.getXaxis().getBinWidth(0)*0.5;
        double yp = 0.0;
        
        path.moveTo(parent.transformX(xp), parent.transformY(yp));
        int nbins = h1d.getXaxis().getNBins();
        for(int i = 0; i < nbins; i++){
            yp = h1d.getBinContent(i);
            path.lineTo(parent.transformX(xp), parent.transformY(yp));
            xp = h1d.getXaxis().getBinCenter(i) + h1d.getXaxis().getBinWidth(i)*0.5;
            path.lineTo(parent.transformX(xp), parent.transformY(yp));
            
        }
        GRootColorPalette palette = GRootColorPalette.getInstance();
        int fill_color = h1d.getFillColor();
        int line_color = h1d.getLineColor();
        int line_width = h1d.getLineWidth();
        g2d.setColor(palette.getColor(fill_color));
        g2d.fill(path);
        g2d.setColor(palette.getColor(line_color));
        g2d.setStroke(new BasicStroke(line_width));
        g2d.draw(path);
    }
}
