/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import org.jlab.groot.base.AxisAttributes;
import org.jlab.groot.base.PadAttributes;
import org.jlab.groot.base.PadMargins;
import org.jlab.groot.base.TColorPalette;
import org.jlab.groot.base.TStyle;
import org.jlab.groot.data.DataLine;
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

    PadAttributes attr = new PadAttributes();
    Dimension2D padDimensions = new Dimension2D();
    GraphicsAxisFrame axisFrame = new GraphicsAxisFrame();
    List<IDataSetPlotter> datasetPlotters = new ArrayList<IDataSetPlotter>();
    boolean preliminary = true;
    int preliminarySize = 36;
    PaveText statBox = null;
    PaveText legend  = null;
    Point2D  legendPosition = new Point2D.Double(20,10);
    private boolean drawLegend = false;
    
    private List<PaveText>   padTexts = new ArrayList<PaveText>();
    private List<LatexText>  padLatexText = new ArrayList<LatexText>();
    
    private List<DataLine>   padLines = new ArrayList<DataLine>();
    private DataLinePlotter  dataLinePlotter = new DataLinePlotter();
    
    public EmbeddedPad() {

    }

    public EmbeddedPad(int x, int y, int width, int height) {
        
        this.setDimension(x, y, width, height);
    }

    public final EmbeddedPad setDimension(int x, int y, int width, int height) {
        this.padDimensions.getDimension(0).setMinMax(x, x + width);
        this.padDimensions.getDimension(1).setMinMax(y, y + height);
        axisFrame.setFrameDimensions(
                padDimensions.getDimension(0).getMin(),
                padDimensions.getDimension(0).getMax(),
                padDimensions.getDimension(1).getMin(),
                padDimensions.getDimension(1).getMax()
        );
        //System.out.println(padDimensions);
        return this;
    }

    public void addText(PaveText text){
        this.padTexts.add(text);
    }
    
    public void addLatex(LatexText text){
        this.padLatexText.add(text);
    }
    
    public void clear() {
        this.datasetPlotters.clear();
        this.axisFrame.setDrawAxisZ(false);
        this.padTexts.clear();
        this.padLines.clear();
        this.drawLegend = false;
    }

    public Map<String, IDataSet> getObjectMap() {
        Map<String, IDataSet> objects = new LinkedHashMap<String, IDataSet>();
        for (IDataSetPlotter plotter : this.datasetPlotters) {
            objects.put(plotter.getName(), plotter.getDataSet());
        }
        return objects;
    }

    public void setAxisRange(double xmin, double xmax, double ymin, double ymax) {
        this.getAxisX().setRange(xmin, xmax);
        this.getAxisY().setRange(ymin, ymax);
    }

    public void setMargins(PadMargins margins) {
        this.attr.getPadMargins().copy(margins);
        //System.out.println(" PAD - > " + padMargins);
    }

    public GraphicsAxisFrame getAxisFrame() {
        return this.axisFrame;
    }

    public void paint(Graphics g) {
        this.draw((Graphics2D) g);
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
    public void draw(Graphics2D g2d) {
        //axisFrame.updateMargins(g2d);
        //axisFrame.setAxisMargins(padMargins);
        //axisFrame.updateMargins(g2d);
        g2d.setColor(this.attr.getBackgroundColor());
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
        Dimension3D axis = new Dimension3D();
        axis.set(0.0, 1.0, 0.0, 1.0, 0.0, 1.0);
        if (this.datasetPlotters.size() > 0) {

            axis.copy(datasetPlotters.get(0).getDataRegion());
            for (IDataSetPlotter plotter : this.datasetPlotters) {
                Dimension3D d3d = plotter.getDataRegion();
                axis.combine(d3d);
            }

            //System.out.println( "AXIS = "  + axis.toString());
            if (this.getAxisX().isAutoScale() == false) {
                axis.getDimension(0).copy(this.getAxisX().getRange());
                //axis.getDimension(1).copy(this.getAxisY().getRange());
            } else {
                axisFrame.getAxisX().setRange(
                        axis.getDimension(0).getMin(),
                        axis.getDimension(0).getMax()
                );
                axisFrame.getAxisX().getAttributes().setAxisAutoScale(true);
            }

            double sum = 0.0;
            for (int i = 0; i < datasetPlotters.get(0).getDataSet().getDataSize(1); i++) {
                sum += datasetPlotters.get(0).getDataSet().getDataY(i);
            }

            if (this.datasetPlotters.size() > 0) {
                if (this.getDatasetPlotters().get(0) instanceof Histogram2DPlotter) {
                    sum = 1.0;
                }
            }

            if (this.getAxisY().isAutoScale() == false || sum == 0.0) {
                axis.getDimension(1).copy(this.getAxisY().getRange());
                //System.out.println("if 1");            
            } else {
                axisFrame.getAxisY().setRange(
                        axis.getDimension(1).getMin(),
                        axis.getDimension(1).getMax()
                );
                axisFrame.getAxisY().getAttributes().setAxisAutoScale(true);
                //System.out.println("if 2");
            }
            //System.out.println(" AXIS Y MIN MAX" + axisFrame.getAxisY().getMin() + " " 
            //+ axisFrame.getAxisY().getMax());

            if (this.getAxisZ().isAutoScale() == false) {
                axis.getDimension(2).copy(this.getAxisZ().getRange());
            } else {
                axisFrame.getAxisZ().setRange(
                        axis.getDimension(2).getMin(),
                        axis.getDimension(2).getMax()
                );
                axisFrame.getAxisZ().getAttributes().setAxisAutoScale(true);
            }

            if (this.attr.getTitle() != "") {
                this.attr.getPadMargins().setTopMargin(this.attr.getPadMargins().getTopMargin() + getTitleFontSize());
                axisFrame.setAxisMargins(this.attr.getPadMargins());
                axisFrame.updateMargins(g2d);
            } else {
                /*
        	this.attr.getPadMargins().setTopMargin(this.attr.getPadMargins().getTopMargin()-getTitleFontSize());
        	axisFrame.setAxisMargins(this.attr.getPadMargins());
        	axisFrame.updateMargins(g2d);*/
            }
            /*if(this.getAxisZ().getAttributes().showAxis()){
        	padMargins.setRightMargin(padMargins.getRightMargin()+10);
        	axisFrame
        }*/

            //System.out.println( "Y AXIS DIM = " + axisFrame.getAxisY().toString());
            Rectangle2D rect = new Rectangle2D.Double(
                    axisFrame.getFrameDimensions().getDimension(0).getMin() + this.attr.getPadMargins().getLeftMargin(),
                    axisFrame.getFrameDimensions().getDimension(1).getMin() + this.attr.getPadMargins().getTopMargin(),
                    axisFrame.getFrameDimensions().getDimension(0).getLength()
                    - this.attr.getPadMargins().getLeftMargin() - this.attr.getPadMargins().getRightMargin(),
                    axisFrame.getFrameDimensions().getDimension(1).getLength()
                    - this.attr.getPadMargins().getTopMargin() - this.attr.getPadMargins().getBottomMargin()
            );

            g2d.setClip(rect);
            
            g2d.setColor(Color.white);
            g2d.fill(rect);
            
            for (IDataSetPlotter plotter : this.datasetPlotters) {
                plotter.draw(g2d, axisFrame);
            }
            //System.out.println("--- drawing lines --- size = " + padLines.size());
            for (DataLine line : padLines){
                //System.out.println("--> draw line");
                dataLinePlotter.draw(g2d, line, axisFrame);
            }
            g2d.setClip(null);
            //System.out.println("PLOTTERS SIZE = " + this.datasetPlotters.size());
            axisFrame.getAxisZ().setPalette(attr.getPalette());
            axisFrame.drawAxis(g2d, this.attr.getPadMargins());
            List<List<LatexText>> toBeDrawn = new ArrayList< List<LatexText>>();
            for (int i = 0; i < this.datasetPlotters.size(); i++) {
                List<List<LatexText>> currentStats = this.datasetPlotters.get(i).getDataSet().getStatBox().getPaveTexts();
                String optStat = this.datasetPlotters.get(i).getDataSet().getAttributes().getOptStat();
                int counter = 0;
                for (int j = 0; j < optStat.length() && j < currentStats.size(); j++) {
                    //System.out.println("Counter:"+counter);
                    if (Integer.parseInt("" + optStat.charAt(optStat.length() - 1 - j)) != 0) {
                        toBeDrawn.add(currentStats.get(counter));
                        //System.out.print("counter:"+counter);
                        /*for(LatexText text : currentStats.get(counter)){
        				System.out.print(" "+text.getTextString());
        			}*/
                    }

                    counter++;
                }
            }
            axisFrame.updateMargins(g2d);

            if (toBeDrawn.size() > 0) {
                statBox = new PaveText(2);
                statBox.setPaveTexts(toBeDrawn);
                statBox.setFont(this.attr.getStatBoxFont().getFontName());
                statBox.setFontSize(this.attr.getStatBoxFont().getFontSize());

                statBox.updateDimensions(g2d);
                statBox.setBackground(255, 255, 255, 200);
                int x = (int) (axisFrame.getFrameDimensions().getDimension(0).getMax() - statBox.getBounds().getDimension(0).getLength() - 5) + this.attr.getStatBoxOffsetX();
                int y = (int) (axisFrame.getFrameDimensions().getDimension(1).getMin() + 5) + this.attr.getStatBoxOffsetY();
                statBox.setPosition(x - this.attr.getPadMargins().getRightMargin(), y + this.attr.getPadMargins().getTopMargin());
                statBox.drawPave(g2d, x - this.attr.getPadMargins().getRightMargin(), y + this.attr.getPadMargins().getTopMargin());
            }
            if (!"".equals(this.attr.getTitle())) {
                LatexText titleLatex = new LatexText(this.attr.getTitle());
                titleLatex.setColor(1);
                titleLatex.setFont(this.getTitleFont());
                titleLatex.setFontSize(this.getTitleFontSize());
                this.attr.getPadMargins().setTopMargin(this.attr.getPadMargins().getTopMargin() + getTitleFontSize() + 10);
                titleLatex.drawString(g2d, (int) axisFrame.getAxisX().getAxisPosition((axisFrame.getAxisX().getRange().getMin() + .5 * axisFrame.getAxisX().getRange().getLength())), (int) (axisFrame.getFrameDimensions().getDimension(1).getMin() + this.attr.getTitleOffset()), 1, 0);
            }
            
            
            /*DRAWING THE LEGEND*/
            if(this.padLatexText.size()>0){
                int x = (int) (axisFrame.getFrameDimensions().getDimension(0).getMin() 
                        );
                int y = (int) (axisFrame.getFrameDimensions().getDimension(1).getMin() );
                
                //System.out.println(" XY = " + x + " , " + y);
                for(LatexText pt : this.padLatexText){
                    pt.drawString(g2d, (int) (x + pt.getX()), 
                            (int) ( y + pt.getY()), 0,0);
                }
            }
            
            if(this.drawLegend==true){
                legend = new PaveText(2);
                legend.setXOffset(40);
                //System.out.println(" drawing legend");
                for(IDataSetPlotter data : this.datasetPlotters){
                    IDataSet ds = data.getDataSet();
                    legend.addText(ds.getAttributes().getTitle(),"");
                    /*System.out.println(" data set : " + ds.getName() 
                            + " tile : [" + ds.getAttributes().getTitle()
                    + "]");*/
                    Color col = TStyle.getColor(ds.getAttributes().getLineColor());
                    legend.addColor(col);
                }
                legend.setFont(this.attr.getStatBoxFont().getFontName());
                legend.setFontSize(this.attr.getStatBoxFont().getFontSize());
                legend.updateDimensions(g2d);
                legend.setBackground(255, 255, 255, 160);
                //int x = (int) this.padDimensions.getDimension(0).getMin();
                //int y = (int) this.padDimensions.getDimension(1).getMin();
                //int x = (int) (axisFrame.getFrameDimensions().getDimension(0).getMax() - legend.getBounds().getDimension(0).getLength() - 5) + this.attr.getStatBoxOffsetX();
                int x = (int) (axisFrame.getFrameDimensions().getDimension(0).getMin()
                        + this.attr.getPadMargins().getLeftMargin() 
                        + this.legendPosition.getX());// - legend.getBounds().getDimension(0).getLength() - 5) + this.attr.getStatBoxOffsetX();
                int y = (int) (axisFrame.getFrameDimensions().getDimension(1).getMin() +
                        this.attr.getPadMargins().getTopMargin() - 
                        legend.getBounds().getDimension(1).getLength()+ this.legendPosition.getY());//+this.attr.getPadMargins().getTopMargin());
                legend.setPosition(x , y );
                legend.drawPave(g2d, x , y );
                //this.legend.drawPave(g2d, x, y);
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

    
    public void setLegend(boolean flag){
        this.drawLegend = flag;
        if(drawLegend == false){
            this.legend = null;
        } else {
            legend = new PaveText(1);            
        }
    }
    
    public void setLegendPosition(double xl, double yl){
        this.legendPosition.setLocation(xl, yl);
    }
    
    public void setOptStat(int opts) {
        if (this.getDatasetPlotters().size() > 0) {
            this.getDatasetPlotters().get(0).getDataSet().getAttributes().setOptStat("" + opts);
        }
    }

    public void setOptStat(String opts) {
        if (this.getDatasetPlotters().size() > 0) {
            this.getDatasetPlotters().get(0).getDataSet().getAttributes().setOptStat(opts);
        }
    }

    public String getOptStat() {
        if (this.getDatasetPlotters().size() > 0) {
            return this.getDatasetPlotters().get(0).getDataSet().getAttributes().getOptStat();
        }
        return "0";
    }

    public EmbeddedPad setAutoScale() {
        this.getAxisX().setAutoScale(true);
        this.getAxisY().setAutoScale(true);
        return this;
    }

    public GraphicsAxis getAxisX() {
        return this.axisFrame.getAxisX();
    }

    public GraphicsAxis getAxisY() {
        return this.axisFrame.getAxisY();
    }

    public void setStatBoxFont(String name) {
        this.attr.getStatBoxFont().setFontName(name);
    }

    public void setStatBoxFontSize(int size) {
        this.attr.getStatBoxFont().setFontSize(size);
    }

    public void setAxisFont(String name) {
        this.getAxisFrame().getAxisX().setAxisFont(name);
        this.getAxisFrame().getAxisY().setAxisFont(name);
    }

    public void setAxisFontSize(int size) {
        this.getAxisFrame().getAxisX().setAxisFontSize(size);
        this.getAxisFrame().getAxisY().setAxisFontSize(size);
    }

    public void addPlotter(IDataSetPlotter plotter) {
        this.datasetPlotters.add(plotter);
    }

    public int getWidth() {
        return (int) (padDimensions.getDimension(0).getMax() - padDimensions.getDimension(0).getMin());
    }

    public int getHeight() {
        return (int) (padDimensions.getDimension(1).getMax() - padDimensions.getDimension(1).getMin());
    }

    public void draw(DataLine line){ 
        //System.out.println("--- data pad adding line ---");
        padLines.add(line); 
    }
    
    public void draw(IDataSet ds, String options) {
        
        ds.getAttributes().setDrawOptions(options);
        axisFrame.setDrawAxisZ(false);
        axisFrame.getAxisZ().getAttributes().setShowAxis(false);
        //System.out.println(" drawing data set " + ds.getName());
        //System.out.println(" flag before = " + axisFrame.getAxisZ().getAttributes().showAxis());
        
        if (options.contains("same") == false) {
            this.datasetPlotters.clear();
            axisFrame.setDrawAxisZ(false);
        }
        
        if (datasetPlotters.isEmpty()) {
            axisFrame.setDrawAxisZ(false);
            axisFrame.getAxisY().setTitle(ds.getAttributes().getTitleY());
            axisFrame.getAxisX().setTitle(ds.getAttributes().getTitleX());
            this.setTitle(ds.getAttributes().getTitle());
        } else {
            axisFrame.getAxisY().setTitle(datasetPlotters.get(0).getDataSet().getAttributes().getTitleY());
            axisFrame.getAxisX().setTitle(datasetPlotters.get(0).getDataSet().getAttributes().getTitleX());
        }
        
        if (ds instanceof Func1D) {
            this.addPlotter(new FunctionPlotter(ds));
        }

        if (ds instanceof H1F) {
            this.addPlotter(new HistogramPlotter(ds, options));
            axisFrame.getAxisZ().getAttributes().setShowAxis(false);
            H1F h = (H1F) ds;
            if (h.getFunction() != null) {
                this.addPlotter(new FunctionPlotter(h.getFunction()));
            }
        }
        if (ds instanceof H2F) {
            axisFrame.getAxisZ().getAttributes().setShowAxis(true);
            axisFrame.setDrawAxisZ(true);
            Histogram2DPlotter plotter = new Histogram2DPlotter(ds).setPalette(attr.getPalette());
            plotter.setOptions(options);
            this.addPlotter(plotter);            
        }

        if (ds instanceof GraphErrors) {
            this.addPlotter(new GraphErrorsPlotter(ds));
            GraphErrors gr = (GraphErrors) ds;
            if (gr.getFunction() != null) {
                this.addPlotter(new FunctionPlotter(gr.getFunction()));
            }
        }
        
        //System.out.println(" flag = " + axisFrame.getAxisZ().getAttributes().showAxis());
        //axisFrame.getAxisY().setTitle(datasetPlotters.get(0).getDataSet().getAttributes().getTitleY());
        //axisFrame.getAxisX().setTitle(datasetPlotters.get(0).getDataSet().getAttributes().getTitleX());
    }

    public void remove(IDataSet ds) {
        for (IDataSetPlotter plotter : this.datasetPlotters) {
            if (plotter.getDataSet().equals(ds)) {
                this.datasetPlotters.remove(plotter);
                break;
            }
        }
    }

    /**
     * returns copy of embedded pad with all plotters included.
     *
     * @return
     */
    public EmbeddedPad getCopy() {
        EmbeddedPad pad = new EmbeddedPad();
        pad.setPalette(attr.getPalette().getActivePalette());
        
        //Copy Datasets
        for (int i = 0; i < this.datasetPlotters.size(); i++) {
            IDataSetPlotter plotter = this.datasetPlotters.get(i);
            if (plotter instanceof HistogramPlotter) {
                pad.addPlotter(new HistogramPlotter(plotter.getDataSet()));
            }
            if (plotter instanceof FunctionPlotter) {
                pad.addPlotter(new FunctionPlotter(plotter.getDataSet()));
            }
            if (plotter instanceof Histogram2DPlotter) {
                pad.addPlotter(new Histogram2DPlotter(plotter.getDataSet()).setPalette(pad.attr.getPalette()));
            }
            if (plotter instanceof GraphErrorsPlotter) {
                pad.addPlotter(new GraphErrorsPlotter(plotter.getDataSet()));
                //System.out.println("Graph errors");
            }
        }
        
        //Copy Latex
        for(LatexText text : this.padLatexText){
        	pad.addLatex(text);
        }
        
        //Copy Datalines
        for(DataLine line : this.padLines) {
        	pad.padLines.add(line);
        }
        
        
        // Set Pad Attributes
        try {
            pad.getAxisX().setAttributes((AxisAttributes) this.getAxisX().getAttributes().clone());
            pad.getAxisY().setAttributes((AxisAttributes) this.getAxisY().getAttributes().clone());
            pad.getAxisZ().setAttributes((AxisAttributes) this.getAxisZ().getAttributes().clone());
            pad.setTitle(this.attr.getTitle());
            pad.setTitleOffset(this.attr.getTitleOffset());
            pad.setTitleFontSize(this.getTitleFontSize());
            pad.setTitleFont(this.getTitleFont());
            pad.setStatBoxFont(this.getStatBoxFontName());
            pad.setStatBoxFontSize(this.getStatBoxFontSize());
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pad;
    }

    private String getStatBoxFontName() {
        return this.attr.getStatBoxFont().getFontName();
    }

    public static class EmbeddedPadConfigurationPane extends JDialog {

        EmbeddedPad embeddedPad = null;
        JTabbedPane tabbedPane = null;

        public EmbeddedPadConfigurationPane(EmbeddedPad pad) {
            super();
            tabbedPane = new JTabbedPane();
            this.setEmbeddedPad(pad);
        }

        public final void setEmbeddedPad(EmbeddedPad pad) {
            embeddedPad = pad;
        }

        public void initUI() {
            //embeddedPad.getAxisFrame().getAxisX()
        }

    }

    public GraphicsAxis getAxisZ() {
        return this.axisFrame.getAxisZ();
    }

    public String getTitle() {
        return this.attr.getTitle();
    }

    public int getTitleOffset() {
        return this.attr.getTitleOffset();
    }

    public void setTitleOffset(int titleOffset) {
        this.attr.setTitleOffset(titleOffset);
    }

    public void setTitleFont(String titleFont) {
        this.attr.setTitleFontName(titleFont);
    }

    public void getTitle(String title) {
        this.attr.setTitle(title);
    }

    public int getTitleFontSize() {
        return this.attr.getTitleFontSize();
    }

    public String getTitleFont() {
        return this.attr.getTitleFontName();
    }

    public void setTitleFontSize(int titleFontSize) {
        this.attr.setTitleFontSize(titleFontSize);
    }

    public void getTitleFont(String titleFont) {
        this.attr.setTitleFontName(titleFont);
    }

    public void setAxisTitleFontSize(int parseInt) {
        this.getAxisX().getAttributes().setTitleFontSize(parseInt);
        this.getAxisY().getAttributes().setTitleFontSize(parseInt);
        this.getAxisZ().getAttributes().setTitleFontSize(parseInt);

    }

    public void setAxisLabelFontSize(int parseInt) {
        this.getAxisX().getAttributes().setLabelFontSize(parseInt);
        this.getAxisY().getAttributes().setLabelFontSize(parseInt);
        
        this.getAxisZ().getAttributes().setLabelFontSize( (parseInt * 2)/3);
    }

    public void setFontNameAll(String FontName) {
        this.getAxisX().getAttributes().setLabelFontName(FontName);
        this.getAxisY().getAttributes().setLabelFontName(FontName);
        this.getAxisZ().getAttributes().setLabelFontName(FontName);
        this.getAxisX().getAttributes().setTitleFontName(FontName);
        this.getAxisY().getAttributes().setTitleFontName(FontName);
        this.attr.setTitleFontName(FontName);
        this.attr.getStatBoxFont().setFontName(FontName);
    }

    public int getStatBoxFontSize() {
        return this.attr.getStatBoxFont().getFontSize();
    }

    public void setTitle(String title) {
        this.attr.setTitle(title);
    }

    public void setPalette(String palName) {
        this.attr.setPalette(palName);
    }

    public void setPalette(int palVal) {
        this.attr.setPalette(palVal);
    }

    public void setPalette(TColorPalette.PaletteName pal) {
        this.attr.setPalette(pal);
    }
}
