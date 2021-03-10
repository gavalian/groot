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
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.groot.settings.GRootColorPalette;

/**
 *
 * @author gavalian
 */
public class HistogramNode2D extends DataNode2D {
    private H2F h2d = null;
    
    public HistogramNode2D(H2F data){
        h2d = data;
    }
    
    @Override
    public Rectangle2D getDataBounds( Rectangle2D dataBounds){
        
        dataBounds.setRect(
                h2d.getXAxis().min(),
                h2d.getYAxis().min(), 
                h2d.getXAxis().max()-h2d.getXAxis().min(),
                h2d.getYAxis().max()-h2d.getYAxis().min()
               );
        return dataBounds;
    }
    
    public IDataSet getDataSet(){ return h2d;}
    
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
        
        double dataMin = h2d.getMin();
        double dataMax = h2d.getMax();
        
        int nbinsY = h2d.getYAxis().getNBins();
        int nbinsX = h2d.getXAxis().getNBins();
        for(int yc = 0; yc < nbinsY; yc++){
            for(int xc = 0; xc < nbinsX; xc++){
                double rxc = h2d.getXAxis().getBinCenter(xc);
                double rxw = h2d.getXAxis().getBinWidth(xc);
                double ryc = h2d.getYAxis().getBinCenter(yc);
                double ryw = h2d.getYAxis().getBinWidth(yc);
                
                double pxs  = parent.transformX(rxc-rxw*0.5);
                double pxe  = parent.transformX(rxc+rxw*0.5);
                
                double pys  = parent.transformY(ryc+ryw*0.5);
                double pye  = parent.transformY(ryc-ryw*0.5);
                
                double value = h2d.getBinContent(xc,yc);
                Color c = palette.getColor3D(dataMin, dataMax, value);
                //Color c = palette.getColor3D(0.0, dataMax, value);
                g2d.setColor(c);
                int w = (int) (pxe-pxs+2);
                int h = (int)  Math.abs(pye-pys+2);
                g2d.fillRect((int) pxs, (int) pys, w, h);
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
        }
        //g2d.setColor(Color.red);
        //g2d.setStroke(new BasicStroke(2));
    }
}
