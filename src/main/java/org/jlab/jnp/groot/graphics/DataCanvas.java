/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.graphics;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.util.Arrays;
import org.jfree.pdf.PDFDocument;
import org.jfree.pdf.PDFGraphics2D;
import org.jfree.pdf.Page;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.F1D;
import org.jlab.jnp.graphics.attr.AttributeCollection;
import org.jlab.jnp.graphics.attr.AttributeDialog;
import org.jlab.jnp.graphics.attr.AttributeType;
import org.jlab.jnp.graphics.base.Background2D;
import org.jlab.jnp.graphics.base.Canvas2D;
import org.jlab.jnp.graphics.base.Node2D;
import org.jlab.jnp.graphics.base.NodeRegion2D;
import org.jlab.jnp.graphics.base.PopupProvider;

/**
 *
 * @author gavalian
 */
public class DataCanvas extends Canvas2D {
    
    
    private AttributeCollection   attributes = null;
    private int                 activeRegion = 0;
    
    public DataCanvas(){
        super();
        //this.addNode(new GraphicsAxis());
        PopupProvider popup = new PopupProvider();
        this.setPopupProvider(popup);
        attributes = new AttributeCollection(new AttributeType[]{},new String[]{});
        Background2D back = Background2D.createBackground(255, 255, 255);
        setBackground(back);
    }
    
    public void initBackground(int red, int green, int blue){
        Background2D back = Background2D.createBackground(red,green,blue);
        setBackground(back);
    }
    
    public void setPadInsets(int t, int l, int b, int r){
        for(Node2D item : this.getGraphicsComponents()){
            item.getInsets().set(t, l, b, r);
        }
    }

    public void setAxisFont(String fontname, int fontsize, int fontface){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            region.setAxisFont(fontname, fontsize, fontface);
        }
        repaint();
    }
    
    public void setAxisLimits(double xmin, double xmax, double ymin, double ymax){
        getRegion(this.activeRegion).getGraphicsAxis().setAxisLimits(xmin, xmax, ymin, ymax);
    }
    
    public void setAxisLimits(boolean automatic){
        if(automatic==true){
            getRegion(this.activeRegion).getGraphicsAxis().setAxisAutomatic();
        }
    }
    
    public void setAxisTitleFont(String fontname, int fontsize, int fontface){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            region.getGraphicsAxis().getAxisX().setAxisTitleFont(fontname, fontsize, fontface);
            region.getGraphicsAxis().getAxisY().setAxisTitleFont(fontname, fontsize, fontface);
        }
        repaint();
    }
    public DataCanvas left(int offset){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            region.getGraphicsAxis().getInsets().left(offset);
        }
        repaint();
        return this;
    }
    
    
    public DataCanvas setAxisTicks(int ticks, String axis){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            region.getGraphicsAxis().setAxisTicks(ticks, axis);
        }
        repaint();
        return this;
    }
    
    public DataCanvas setAxisTitleOffset(int offset, String axis){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            region.getGraphicsAxis().setAxisTitleOffset(offset, axis);
        }
        repaint();
        return this;
    }
    
    public DataCanvas right(int offset){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            region.getGraphicsAxis().getInsets().right(offset);
        }
        repaint();
        return this;
    }
    
    public DataCanvas top(int offset){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            region.getGraphicsAxis().getInsets().top(offset);
        }
        repaint();
        return this;
    }
    
    public DataCanvas bottom(int offset){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            region.getGraphicsAxis().getInsets().bottom(offset);
        }
        repaint();
        return this;
    }
    
    public DataCanvas cd(int region){
        if(region>=0&&region<getGraphicsComponents().size()){
            activeRegion = region;
        }
        return this;
    }
    
    public DataCanvas draw(IDataSet ds){
        draw(ds,""); return this;
    }
    
    public DataCanvas setAxisFontSize(int size){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            Font fontX = region.getGraphicsAxis().getAxisX().getAxisFont();
            region.getGraphicsAxis().getAxisX().setAxisFont(new Font(fontX.getFontName(),size,fontX.getStyle()));
            
            Font fontY = region.getGraphicsAxis().getAxisY().getAxisFont();
            region.getGraphicsAxis().getAxisY().setAxisFont(new Font(fontY.getFontName(),size,fontY.getStyle()));            
        }
        return this;
    }
    
    public DataCanvas setAxisTitleFontSize(int size){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            Font fontX = region.getGraphicsAxis().getAxisX().getAxisTitleFont();
            region.getGraphicsAxis().getAxisX().setAxisTitleFont(new Font(fontX.getFontName(),size,fontX.getStyle()));
            
            Font fontY = region.getGraphicsAxis().getAxisY().getAxisTitleFont();
            region.getGraphicsAxis().getAxisY().setAxisTitleFont(new Font(fontY.getFontName(),size,fontY.getStyle()));            
        }
        return this;
    }
    
    public DataCanvas setAxisTitleOffsetX(Integer offset){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            region.getGraphicsAxis().getAxisX().getAttributes().changeValue(AttributeType.AXISTITLEOFFSET, offset.toString());
        }
        return this;
    }
    
    
    public DataCanvas addLegend(Legend leg){
        this.getRegion(this.activeRegion).addNode(leg);
        return this;
    }
    
    public DataCanvas setAxisTitleOffsetY(Integer offset){
        for(Node2D item : this.getGraphicsComponents()){
            DataRegion region = (DataRegion) item;
            region.getGraphicsAxis().getAxisY().getAttributes().changeValue(AttributeType.AXISTITLEOFFSET, offset.toString());
        }
        return this;
    }
    
    public DataCanvas draw(IDataSet ds, String options){
        if(ds instanceof GraphErrors){
            if(options.contains("same")==false)
                getRegion(activeRegion).getGraphicsAxis().reset();
            
            getRegion(activeRegion).getGraphicsAxis().addDataNode(new GraphNode2D((GraphErrors) ds,options));
        }
        
        if(ds instanceof H1F){
            if(options.contains("same")==false)
                getRegion(activeRegion).getGraphicsAxis().reset();
            
            getRegion(activeRegion).getGraphicsAxis().addDataNode(new HistogramNode1D((H1F) ds,options));
        }
        
        if(ds instanceof H2F){
            if(options.contains("same")==false)
                getRegion(activeRegion).getGraphicsAxis().reset();
            
            getRegion(activeRegion).getGraphicsAxis().addDataNode(new HistogramNode2D((H2F) ds));
        }
        
        if(ds instanceof F1D){
            if(options.contains("same")==false)
                getRegion(activeRegion).getGraphicsAxis().reset();            
            getRegion(activeRegion).getGraphicsAxis().addDataNode(new FunctionNode1D((F1D) ds));
        }
                
        return this;
    }
    
    
    
    public DataRegion getRegion(int region){
        return (DataRegion) getGraphicsComponents().get(region);
    }
    
    public void divide(double left, double bottom, int cols, int rows){
        this.getGraphicsComponents().clear();
        for(int i = 0; i < cols*rows; i++){
            DataRegion pad = new DataRegion("canvas_pad_"+i);
            this.addNode(pad);
        }
        this.arrangeWithGap(left, bottom, cols, rows);
    }
    
    @Override
    public void divide(int cols, int rows){
        this.getGraphicsComponents().clear();
        for(int i = 0; i < cols*rows; i++){
            DataRegion pad = new DataRegion("canvas_pad_"+i);
            this.addNode(pad);
        }
        this.arrange(cols, rows);
    }
    
    public void divide(double[][] fractions){
        int ncolumns = fractions.length;
        int size = 0;
        for(int i = 0; i < ncolumns; i++) size += fractions[i].length;
        System.out.println("DIVIDING CANVAS (cols): " + ncolumns + " TOTAL SIZE = " + size);
        this.getGraphicsComponents().clear();
        for(int i = 0; i < size; i++)  addNode(new DataRegion("canvas_pad_"+i));
        arrange(fractions);
    }
    
    public void editAttributes(){
        DataRegion pad = (DataRegion) this.getGraphicsComponents().get(1);
        AttributeDialog dialog = new AttributeDialog(
                Arrays.asList(pad.getGraphicsAxis().getAxisX().getAttributes(),
                        pad.getGraphicsAxis().getAxisY().getAttributes()
                ));
        dialog.pack();
        dialog.setVisible(true);
    }
    
    public void export(String filename){
        PDFDocument pdfDoc = new PDFDocument();
            Page page = pdfDoc.createPage(new Rectangle(this.getSize().width, this.getSize().height));
            PDFGraphics2D g2 = page.getGraphics2D();
            this.paint(g2);
            pdfDoc.writeToFile(new File(filename));
    }
}
