/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import org.jlab.groot.base.AxisAttributes;
import org.jlab.groot.base.FontProperties;
import org.jlab.groot.base.PadMargins;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Dimension2D;
import org.jlab.groot.math.Dimension3D;
import org.jlab.groot.math.Func1D;
import org.jlab.groot.ui.LatexText;
import org.jlab.groot.ui.PaveText;

/**
 *
 * @author gavalian
 */
public class EmbeddedPad {
 
	 Dimension2D            padDimensions  = new Dimension2D();
     GraphicsAxisFrame          axisFrame  = new GraphicsAxisFrame();
     Color                backgroundColor  = Color.WHITE;
     private  PadMargins  padMargins       = new PadMargins();
     List<IDataSetPlotter> datasetPlotters = new ArrayList<IDataSetPlotter>();
     
     //Dimension3D           fixedRange      = new Dimension3D();
     
     private FontProperties statBoxFont    = new FontProperties();
     private boolean		isSelected     = false;
     int statBoxOffsetX = 0;
     int statBoxOffsetY = 0;
     PaveText statBox = null;
     
     String title = "";
     int titleFontSize = 12;
     String titleFont = "Avenir";
     
     public EmbeddedPad(){
         
     }
    
    public EmbeddedPad(int x, int y, int width, int height){
        this.setDimension(x, y, width, height);        
    }
    
    public final EmbeddedPad setDimension(int x, int y, int width, int height){
        this.padDimensions.getDimension(0).setMinMax(x,x+width);
        this.padDimensions.getDimension(1).setMinMax(y,y+height);
        axisFrame.setFrameDimensions(
                padDimensions.getDimension(0).getMin(),
                padDimensions.getDimension(0).getMax(),
                padDimensions.getDimension(1).getMin(),
                padDimensions.getDimension(1).getMax()
        );
        //System.out.println(padDimensions);
        return this;
    }
    
    public void setAxisRange(double xmin, double xmax, double ymin, double ymax){
    	this.getAxisX().setRange(xmin, xmax);
    	this.getAxisY().setRange(xmin, xmax);
    }
    
    public void setMargins(PadMargins margins){
        this.padMargins.copy(margins);
        //System.out.println(" PAD - > " + padMargins);
    }
    
    public GraphicsAxisFrame  getAxisFrame(){
        return this.axisFrame;
    }
    public void paint(Graphics g){
    	this.draw((Graphics2D)g);
    }
    /**
	 * @return the datasetPlotters
	 */
	public List<IDataSetPlotter> getDatasetPlotters() {
		return datasetPlotters;
	}

	/**
	 * @param datasetPlotters the datasetPlotters to set
	 */
	public void setDatasetPlotters(List<IDataSetPlotter> datasetPlotters) {
		this.datasetPlotters = datasetPlotters;
	}
	
	/*
	public void drawScreenshot(Graphics g){
		EmbeddedCanvas can = new EmbeddedCanvas();
		can.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
		this.setDimension(5, -5, 362,218);
		this.draw((Graphics2D)g);
		this.setDimension((int)x1, (int)y1, (int)x2, (int)y2);
	}*/
	

	public void draw(Graphics2D g2d){        
        //axisFrame.updateMargins(g2d);
        //axisFrame.setAxisMargins(padMargins);
        //axisFrame.updateMargins(g2d);
        g2d.setColor(backgroundColor);
        /*g2d.fillRect( 
                (int) padDimensions.getDimension(0).getMin(),
                (int) padDimensions.getDimension(1).getMin(),
                (int) ( padDimensions.getDimension(0).getMax() 
                        - padDimensions.getDimension(0).getMin()),
                (int) (padDimensions.getDimension(1).getMax()
                        -padDimensions.getDimension(1).getMin())
                );
        */

        
        //axisFrame.getAxisY().setTitle(datasetPlotters.get(0).getDataSet().getAttributes().getYTitle());
        //axisFrame.getAxisX().setTitle(datasetPlotters.get(0).getDataSet().getAttributes().getXTitle());
        //axisFrame.updateMargins(g2d);
        //System.out.println(padMargins);
       // padMargins = axisFrame.getFrameMargins();
      // System.out.println(padMargins);

        Dimension3D  axis = new Dimension3D();
        axis.set(0.0, 1.0, 0.0, 1.0, 0.0, 1.0);
        if(this.datasetPlotters.size()>0){
            axis.copy(datasetPlotters.get(0).getDataRegion());
            for(IDataSetPlotter plotter : this.datasetPlotters){
                Dimension3D d3d = plotter.getDataRegion();
                axis.combine(d3d);
            }
        
        
        if(this.getAxisX().isAutoScale()==false){
            axis.getDimension(0).copy(this.getAxisX().getRange());
        }else{
        	axisFrame.getAxisX().setRange(
                    axis.getDimension(0).getMin(),
                    axis.getDimension(0).getMax()
            );
        	axisFrame.getAxisX().getAttributes().setAxisAutoScale(true);
        }
        
        double sum = 0.0;
        for(int i=0; i<datasetPlotters.get(0).getDataSet().getDataSize(1); i++){
        	sum += datasetPlotters.get(0).getDataSet().getDataY(i);
        }
        
        if(this.getAxisY().isAutoScale()==false|| sum==0.0){
            axis.getDimension(1).copy(this.getAxisY().getRange());
        }else{
        	axisFrame.getAxisY().setRange(
                    axis.getDimension(1).getMin(),
                    axis.getDimension(1).getMax()
            );
        	axisFrame.getAxisY().getAttributes().setAxisAutoScale(true);
        }
        
        if(this.getAxisZ().isAutoScale()==false){
            axis.getDimension(2).copy(this.getAxisZ().getRange());
        }else{
        	axisFrame.getAxisZ().setRange(
                    axis.getDimension(2).getMin(),
                    axis.getDimension(2).getMax()
            );
        	axisFrame.getAxisZ().getAttributes().setAxisAutoScale(true);
        }
        
        
        
        Rectangle2D rect = new Rectangle2D.Double(
                axisFrame.getFrameDimensions().getDimension(0).getMin() + padMargins.getLeftMargin(),
                axisFrame.getFrameDimensions().getDimension(1).getMin() + padMargins.getTopMargin(),
                axisFrame.getFrameDimensions().getDimension(0).getLength() 
                        - padMargins.getLeftMargin() - padMargins.getRightMargin(),
                axisFrame.getFrameDimensions().getDimension(1).getLength() - 
                        padMargins.getTopMargin() - padMargins.getBottomMargin()
        );
        
        g2d.setClip(rect);
        
        for(IDataSetPlotter plotter : this.datasetPlotters){
            plotter.draw(g2d, axisFrame);
        }
        g2d.setClip(null);
        //System.out.println("PLOTTERS SIZE = " + this.datasetPlotters.size());
        axisFrame.drawAxis(g2d, padMargins);
    	List<List<LatexText>> toBeDrawn = new ArrayList< List<LatexText>>();
        for(int i = 0; i<this.datasetPlotters.size(); i++){
        	List<List<LatexText>> currentStats = this.datasetPlotters.get(i).getDataSet().getStatBox().getPaveTexts();
        	int optStat =  this.datasetPlotters.get(i).getDataSet().getAttributes().getOptStat();
        	int tempOpt = optStat;
        	int counter = 0;
        	while(tempOpt>=1 && counter<currentStats.size()){
        		//System.out.println("Counter:"+counter);
        		if(tempOpt%10!=0){
        			toBeDrawn.add(currentStats.get(counter));
        			//System.out.print("counter:"+counter);
        			/*for(LatexText text : currentStats.get(counter)){
        				System.out.print(" "+text.getTextString());
        			}*/
        		}
        		tempOpt = tempOpt/10;
        		counter++;
        	}
        }
        if(toBeDrawn.size()>0){
	        statBox = new PaveText(2);
	        statBox.setPaveTexts(toBeDrawn);
	    	statBox.setFont(this.statBoxFont.getFontName());
	        statBox.setFontSize(this.statBoxFont.getFontSize());
	        
	        statBox.updateDimensions(g2d);
	        
	        int x = (int) (axisFrame.getFrameDimensions().getDimension(0).getMax() - statBox.getBounds().getDimension(0).getLength()-5) +statBoxOffsetX;
	        int y = (int) (axisFrame.getFrameDimensions().getDimension(1).getMin()+5) +statBoxOffsetY;
	        statBox.setPosition(x-padMargins.getRightMargin(), y+padMargins.getTopMargin());
	        statBox.drawPave(g2d, x-padMargins.getRightMargin(), y+padMargins.getTopMargin());
        }
        
        /*
        if(this.optStat>0){

            if(this.datasetPlotters.get(0).getDataSet() instanceof H1F){
                List<List<LatexText>> toBeDrawn = new ArrayList< List<LatexText>>();
            	List<List<LatexText>> currentStats = this.datasetPlotters.get(0).getDataSet().getStatBox().getPaveTexts();
            	int tempOpt = optStat;
            	int counter = 0;
            	while(tempOpt>=1){
            		//System.out.println("Counter:"+counter);
            		if(tempOpt%10!=0){
            			toBeDrawn.add(currentStats.get(counter));
            			//System.out.print("counter:"+counter);
            			for(LatexText text : currentStats.get(counter)){
            				System.out.print(" "+text.getTextString());
            			}
            		}
            		tempOpt = tempOpt/10;
            		counter++;
            	}
            	
                //PaveText statBox = this.datasetPlotters.get(0).getDataSet().getStatBox();
                PaveText statBox = new PaveText(2);
                statBox.setPaveTexts(toBeDrawn);
            	statBox.setFont(this.statBoxFont.getFontName());
                statBox.setFontSize(this.statBoxFont.getFontSize());
                
                statBox.updateDimensions(g2d);
                
                int x = (int) (axisFrame.getFrameDimensions().getDimension(0).getMax() - statBox.getBounds().getDimension(0).getLength()-5);
                int y = (int) (axisFrame.getFrameDimensions().getDimension(1).getMin()+5) ;
                statBox.setPosition(x-padMargins.getRightMargin(), y+padMargins.getTopMargin());
                statBox.drawPave(g2d, x-padMargins.getRightMargin(), y+padMargins.getTopMargin());
                
                PaveText statBox = this.datasetPlotters.get(0).getDataSet().getStatBox();
                statBox.setFont(this.statBoxFont.getFontName());
                statBox.setFontSize(this.statBoxFont.getFontSize());
                
                statBox.updateDimensions(g2d);
                
                int x = (int) (this.padDimensions.getDimension(0).getMax() - statBox.getBounds().getDimension(0).getLength()-10);
                int y = (int) (this.padDimensions.getDimension(1).getMin() + 10) ;
                statBox.setPosition(x, y);
                statBox.drawPave(g2d, x, y);
                
            }

        }*/
        }
    }
    
    public void setOptStat(int opts){
        if( this.getDatasetPlotters().size()>0){
            this.getDatasetPlotters().get(0).getDataSet().getAttributes().setOptStat(opts);
        }
    }
    
    public int getOptStat(){
    	 if(this.getDatasetPlotters().size()>0){
             return this.getDatasetPlotters().get(0).getDataSet().getAttributes().getOptStat();
         }
        return 0;
    }
    
    public EmbeddedPad setAutoScale(){
        this.getAxisX().setAutoScale(true);
        this.getAxisY().setAutoScale(true);
        return this;
    }
    
 
    public GraphicsAxis getAxisX(){
    	return this.axisFrame.getAxisX();
    }
    
    public GraphicsAxis getAxisY(){
    	return this.axisFrame.getAxisY();
    }
    
    public void setStatBoxFont(String name){
        this.statBoxFont.setFontName(name);
    }
    
    public void setStatBoxFontSize(int size){
        this.statBoxFont.setFontSize(size);
    }
    
    public void setAxisFont(String name){
        this.getAxisFrame().getAxisX().setAxisFont(name);
        this.getAxisFrame().getAxisY().setAxisFont(name);        
    }
    
    public void setAxisFontSize(int size){
        this.getAxisFrame().getAxisX().setAxisFontSize(size);
        this.getAxisFrame().getAxisY().setAxisFontSize(size);
    }
    
    public void addPlotter(IDataSetPlotter plotter){
        this.datasetPlotters.add(plotter);
    }
    public int getWidth(){return (int)(padDimensions.getDimension(0).getMax() -  padDimensions.getDimension(0).getMin());}
    public int getHeight(){return (int)(padDimensions.getDimension(1).getMax() -  padDimensions.getDimension(1).getMin());}

    public void draw(IDataSet ds, String options){
        if(options.contains("same")==false){
            this.datasetPlotters.clear();
        }
        if(ds instanceof Func1D){
            this.addPlotter(new FunctionPlotter(ds));
        }
        
        if(ds instanceof H1F){
            this.addPlotter(new HistogramPlotter(ds,options));
            H1F h = (H1F) ds;
            if(h.getFunction()!=null){
                this.addPlotter(new FunctionPlotter(h.getFunction()));
            }
        }
        if(ds instanceof H2F){
            this.addPlotter(new Histogram2DPlotter(ds));
        }
        if(ds instanceof GraphErrors){
            this.addPlotter(new GraphErrorsPlotter(ds));
        }
        axisFrame.getAxisY().setTitle(datasetPlotters.get(0).getDataSet().getAttributes().getYTitle());
        axisFrame.getAxisX().setTitle(datasetPlotters.get(0).getDataSet().getAttributes().getXTitle());
    }
    
    /**
     * returns copy of embedded pad with all plotters included.
     * @return 
     */
    public EmbeddedPad  getCopy(){
        EmbeddedPad pad = new EmbeddedPad();
        for(int i =0 ; i < this.datasetPlotters.size(); i++){
            IDataSetPlotter plotter = this.datasetPlotters.get(i);
            if(plotter instanceof HistogramPlotter){
                pad.addPlotter(new HistogramPlotter(plotter.getDataSet()));
            }
            if(plotter instanceof FunctionPlotter){
                pad.addPlotter(new FunctionPlotter(plotter.getDataSet()));
            }
            if(plotter instanceof Histogram2DPlotter){
                pad.addPlotter(new Histogram2DPlotter(plotter.getDataSet()));
            }
            if(plotter instanceof GraphErrorsPlotter){
            	pad.addPlotter(new GraphErrorsPlotter(plotter.getDataSet()));
                //System.out.println("Graph errors");
            }
        }
        try {
			pad.getAxisX().setAttributes((AxisAttributes) this.getAxisX().getAttributes().clone());
			pad.getAxisY().setAttributes((AxisAttributes) this.getAxisY().getAttributes().clone());

		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return pad;
    }
    
    
    public static class EmbeddedPadConfigurationPane extends JDialog {

        EmbeddedPad  embeddedPad = null;
        JTabbedPane  tabbedPane  = null;
        
        public EmbeddedPadConfigurationPane(EmbeddedPad pad){
            super();
            tabbedPane = new JTabbedPane();
            this.setEmbeddedPad(pad);
        }
        
        public final void setEmbeddedPad(EmbeddedPad pad){
            embeddedPad = pad;
        }
        
        public void initUI(){
            //embeddedPad.getAxisFrame().getAxisX()
        }
        
    }


	public GraphicsAxis getAxisZ() {
    	return this.axisFrame.getAxisZ();
	}

	public String getTitle() {
		return title;
	}
	public void getTitle(String title) {
		 this.title = title;
	}
	
	public int getTitleFontSize() {
		return this.titleFontSize;
	}
	public String getTitleFont() {
		return this.titleFont;
	}

	public void setTitleFontSize(int titleFontSize) {
		 this.titleFontSize = titleFontSize;
	}
	public void getTitleFont(String titleFont) {
		this.titleFont = titleFont;
	}
	public void setAxisTitleFontSize(int parseInt) {
		this.getAxisX().getAttributes().setTitleFontSize(parseInt);
		this.getAxisY().getAttributes().setTitleFontSize(parseInt);
	}
	public void setAxisLabelFontSize(int parseInt) {
		this.getAxisX().getAttributes().setLabelFontSize(parseInt);
		this.getAxisY().getAttributes().setLabelFontSize(parseInt);
	}

	public void setFontNameAll(String FontName) {
		this.getAxisX().getAttributes().setLabelFontName(FontName);
		this.getAxisY().getAttributes().setLabelFontName(FontName);
		this.getAxisX().getAttributes().setTitleFontName(FontName);
		this.getAxisY().getAttributes().setTitleFontName(FontName);
		this.titleFont = FontName;
		this.statBoxFont.setFontName(FontName);
	}

	public int getStatBoxFontSize() {
		return statBoxFont.getFontSize();
	}

	public void setTitle(String title) {
		this.title = title;		
	}
	
	

}
