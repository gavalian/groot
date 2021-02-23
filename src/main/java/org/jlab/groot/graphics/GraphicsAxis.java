/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jlab.groot.base.AxisAttributes;
import org.jlab.groot.base.FontProperties;
import org.jlab.groot.base.GStyle;
import org.jlab.groot.base.TColorPalette;
import org.jlab.groot.math.Dimension1D;
import org.jlab.groot.ui.LatexText;

/**
 *
 * @author gavalian
 */
public class GraphicsAxis {

    private AxisAttributes attr = new AxisAttributes(AxisAttributes.X);
    public static int AXISTYPE_COLOR = 1;
    public static int AXISTYPE_HORIZONTAL = 2;
    public static int AXISTYPE_VERTICAL = 3;
    private int axisType = 2;

    // private final  Dimension1D                  axisRange   = new Dimension1D();
    private int numberOfMajorTicks = 10;
    private Boolean isLogarithmic = false;
    private Boolean isVertical = false;
    private Boolean isColorAxis = false;
    private final GraphicsAxisTicks axisTicks = new GraphicsAxisTicks();
    private TColorPalette palette = new TColorPalette();

    /**
     * default
     *
     * @param AXISTYPE
     */
    public GraphicsAxis(int AXISTYPE) {
        this.axisType = AXISTYPE;
        if (axisType == GraphicsAxis.AXISTYPE_COLOR) {
            isVertical = true;
            isColorAxis = true;
        }
        if (axisType == GraphicsAxis.AXISTYPE_VERTICAL) {
            isVertical = true;
        }

        this.setDimension(0, 100);
        this.setRange(0.0, 1.0);
        initAttributes();
    }

    private void initAttributes() {
        if (this.axisType == GraphicsAxis.AXISTYPE_HORIZONTAL) {
            try {
                this.attr = GStyle.getAxisAttributesX().clone();
            } catch (CloneNotSupportedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (this.axisType == GraphicsAxis.AXISTYPE_VERTICAL) {
            try {
                this.attr = GStyle.getAxisAttributesY().clone();
            } catch (CloneNotSupportedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (this.axisType == GraphicsAxis.AXISTYPE_COLOR) {
            try {
                this.attr = GStyle.getAxisAttributesZ().clone();
            } catch (CloneNotSupportedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * sets the dimension for the axis for plotting.
     *
     * @param xmin
     * @param xmax
     * @return
     */
    public final GraphicsAxis setDimension(int xmin, int xmax) {
        this.attr.getAxisDimension().setMinMax(xmin, xmax);
        return this;
    }

    public void setAxisType(int type) {
        if (type == GraphicsAxis.AXISTYPE_COLOR) {
            this.isVertical = true;
            this.isColorAxis = true;
        }
    }

    public Dimension1D getDimension() {
        return this.attr.getAxisDimension();
    }

    /**
     * Sets the range of the axis, it also updates the axis labels.
     *
     * @param min
     * @param max
     * @return
     */
    public final GraphicsAxis setRange(double min, double max) {
        //this.axisRange.setMinMax(min, max);
        this.attr.setAxisAutoScale(false);
        this.attr.setAxisMinimum(min);
        this.attr.setAxisMaximum(max);
        if (this.getAttributes().isLog() == true) {
            List<Double> ticks = this.attr.getRange().getDimensionTicksLog(this.numberOfMajorTicks);
            //axisLabels.updateLog(ticks);
        } else {
            List<Double> ticks = this.attr.getRange().getDimensionTicks(this.numberOfMajorTicks);
            this.axisTicks.init(ticks, min, max);
            //axisLabels.update(ticks);
        }
        return this;
    }

    public void setTitle(String title) {
        attr.setAxisTitleString(title);
    }

    public boolean getLog() {
        return this.getAttributes().isLog();
    }

    public void setLog(boolean flag) {
        this.getAttributes().setLog(flag);
        this.axisTicks.setLog(flag);
    }

    public Dimension1D getRange() {
        return this.attr.getRange();
    }

    /**
     * prints string representation of the axis.
     */
    public void show() {
        System.out.println(this.toString());
    }

    /**
     * returns position along the axis, the dimension of the axis has to be set.
     *
     * @param value
     * @return
     */
    public double getAxisPosition(double value) {
        double fraction = this.attr.getRange().getFraction(value);
        if (this.attr.getAxisDimension().getMin() < this.attr.getAxisDimension().getMax()) {
            //System.out.println("Getting axis position(fraction):"+this.axisType+" "+value+" "+this.attr.getAxisDimension().getPoint(fraction)+" "+fraction);
            return this.attr.getAxisDimension().getPoint(fraction);
        } else {
            //System.out.println("Getting axis position(fraction):"+this.axisType+" "+value+" "+this.attr.getAxisDimension().getPoint(fraction)+" "+fraction+" "+Math.log10(fraction));

            if (this.getLog()) {
                double axisMinimum = this.getDimension().getMin();

                if (axisMinimum < 1E-20) {
                    axisMinimum = 1E-20;
                }
                if (fraction > 1E-20) {
                    return this.attr.getAxisDimension().getMin()
                            - fraction * Math.abs(this.attr.getAxisDimension().getMax() - this.attr.getAxisDimension().getMin());
                } else {
                    return axisMinimum;
                }
            } else {
                return this.attr.getAxisDimension().getMin()
                        - fraction * Math.abs(this.attr.getAxisDimension().getMax() - this.attr.getAxisDimension().getMin());
            }
        }
    }

    /**
     * returns properties of the title font
     *
     * @return
     */
    public FontProperties getTitleFont() {
        return this.attr.getTitleFont();
    }

    /**
     * returns properties for the label fonts.
     *
     * @return
     */
    public FontProperties getLabelFont() {
        return this.attr.getLabelFont();
    }

    public void setAxisFont(String fontname) {
        attr.setLabelFontName(fontname);
        //axisLabelFont.setFontName(fontname);
        axisTicks.updateFont(getLabelFont());
    }

    public void setAxisFontSize(int size) {
        attr.setLabelFontSize(size);
        //axisLabelFont.setFontSize(size);
        axisTicks.updateFont(getLabelFont());
    }

    public int getSize(Graphics2D g2d, boolean vertical) {
        if (vertical == true) {
            //return this.axisLabels.getAxisMaxWidth(g2d);
        } else {
            //return this.axisLabels.getAxisMaxHeight(g2d);
        }
        return 0;
    }

    public int getAxisDivisions() {
        return this.numberOfMajorTicks;
    }

    public void setVertical(boolean flag) {
        this.isVertical = true;
    }

    public void setAxisDivisions(int ndiv) {
        this.numberOfMajorTicks = ndiv;
    }

    public List<Double> getAxisTicks() {
        return null;//this.axisLabels.getTicks();
    }

    public int getAxisBounds(Graphics2D g2d) {

        double axisBounds = 0.0;
        axisTicks.updateFont(getLabelFont());
        List<LatexText> axisTexts = axisTicks.getAxisTexts();

        double maxW = 0.0;
        double maxH = 0.0;

        for (LatexText text : axisTexts) {
            Rectangle2D bounds = text.getBoundsNumber(g2d);
            //System.out.println(" bounds = " + (int) bounds.getHeight() );
            if (bounds.getWidth() > maxW) {
                maxW = bounds.getWidth();
            }
            if (bounds.getHeight() > maxH) {
                maxH = bounds.getHeight();
            }
        }

        if (this.isVertical == true) {
            axisBounds = maxW + this.attr.getLabelOffset();
        } else {
            axisBounds = maxH + this.attr.getLabelOffset();
        }

        if (this.attr.getTitle().getTextString().length() > 1) {
            Rectangle2D rect = this.attr.getTitle().getBounds(g2d);
            axisBounds += rect.getHeight() + this.attr.getTitleOffset();
        }
        //System.out.println( " Axis : " + axisTitle + "  Max = " +  isVertical +"  " + (int) maxW + "  " + (int) maxH  + "  " + (int) axisBounds);
        return (int) axisBounds;
    }

    public void drawAxisGrid(Graphics2D g2d, int x, int y, int height) {

        if (this.isColorAxis == true) {
            return;
        }

        g2d.setColor(new Color(200, 200, 200));
        g2d.setStroke(GStyle.getStroke(2));

        if (this.isVertical == false) {
            List<Double> ticks = axisTicks.getAxisTicks();
            for (double tick : ticks) {
                //System.out.println(" tick = " + tick);
                int xtick = (int) this.getAxisPosition(tick);
                g2d.drawLine(xtick, y, xtick, y - height);
                //g2d.drawLine(x,(int) tick,x+height,(int) tick);
            }
           
        } else {
            List<Double> ticks = axisTicks.getAxisTicks();
            for (double tick : ticks) {
                //System.out.println(" tick = " + tick);
                int ytick = (int) this.getAxisPosition(tick);
                g2d.drawLine(x, ytick, x + height, ytick);
                //g2d.drawLine(x,(int) tick,x+height,(int) tick);
            }
        }

        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(1));
    }

    public void drawAxisMirror(Graphics2D g2d, int x, int y, int height) {
        if (this.isColorAxis == true) {
            return;
        }

        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(1));
        //g2d.setStroke(GStyle.getStroke(2));
        if (this.isVertical == false) {
            List<Double> ticks = axisTicks.getAxisTicks();
            for (double tick : ticks) {
                //System.out.println(" tick = " + tick);
                int xtick = (int) this.getAxisPosition(tick);
                g2d.drawLine(xtick, y - height, xtick, y - height + this.getAttributes().getTickSize()/2);
                //g2d.drawLine(x,(int) tick,x+height,(int) tick);
            }
            
            /*List<Double> ticksMinor = axisTicks.getAxisTicksMinor();
            for (double tick : ticksMinor) {
                //System.out.println(" tick = " + tick);
                int xtick = (int) this.getAxisPosition(tick);
                g2d.drawLine(xtick, y - height, xtick, y - height + (this.getAttributes().getTickSize()/2));
                //g2d.drawLine(x,(int) tick,x+height,(int) tick);
            }*/
        } else {
            List<Double> ticks = axisTicks.getAxisTicks();
            for (double tick : ticks) {
                //System.out.println(" tick = " + tick);
                int ytick = (int) this.getAxisPosition(tick);
                g2d.drawLine(x + height - this.getAttributes().getTickSize()/2, ytick, x + height, ytick);
                //g2d.drawLine(x,(int) tick,x+height,(int) tick);
            }
        }
    }

    public void drawAxis(Graphics2D g2d, int x, int y) {
        this.drawAxis(g2d, x, y, 0);
    }

    public void drawAxis(Graphics2D g2d, int x, int y, int height) {

        if (this.isColorAxis == true) {
            this.drawColorAxis(g2d, x, y);
            return;
        }

        //g2d.setColor(Color.WHITE);
        g2d.setColor(Color.BLACK);
        
        //List<Double>  ticks = axisRange.getDimensionTicks(this.numberOfMajorTicks);
        //axisTicks.init(ticks);
        this.setAxisDivisions(10);
        axisTicks.updateFont(getLabelFont());
        this.updateAxisDivisions(g2d);

        if (height > 5) {
            this.drawAxisMirror(g2d, x, y, height);
            if (this.getAttributes().getGrid() == true) {
                this.drawAxisGrid(g2d, x, y, height);
            }
        }

        List<Double> ticks = axisTicks.getAxisTicks();
        List<LatexText> texts = axisTicks.getAxisTexts();

        int labelOffset = attr.getLabelOffset();
        int titleOffset = attr.getTitleOffset();
        int tickLength = attr.getTickSize();
        double midpoint = getAxisPosition(this.attr.getRange().getMin()) * 0.5 + 0.5 * getAxisPosition(this.attr.getRange().getMax());

        if (this.isVertical == false) {
            
            g2d.drawLine((int) this.attr.getAxisDimension().getMin(), y, (int) this.attr.getAxisDimension().getMax(), y);
            
            for (int i = 0; i < ticks.size(); i++) {
                double tick = this.getAxisPosition(ticks.get(i));
                g2d.drawLine((int) tick, y, (int) tick, y - tickLength);
                texts.get(i).drawString(g2d, (int) tick, y + labelOffset, 1, 0);
            }
            
            List<Double> ticksMinor = axisTicks.getAxisTicksMinor();
            for (double tick : ticksMinor) {
                int xtick = (int) this.getAxisPosition(tick);
                g2d.drawLine((int) xtick, y, (int) xtick, y - tickLength/2);
            }
            
            int axisBounds = (int) texts.get(0).getBoundsNumber(g2d).getHeight();
            if (!attr.getTitle().getTextString().equals(""))
                attr.getTitle().drawString(g2d,
                        (int) midpoint,
                        y + axisBounds + labelOffset + titleOffset, 1, 0);
        } else {
            g2d.drawLine(x, (int) this.attr.getAxisDimension().getMin(), x, 
                    (int) this.attr.getAxisDimension().getMax());
            //g2d.drawString(this.axisTicks.getAxisExponent(), x, (int) this.attr.getAxisDimension().getMax());

            if (!axisTicks.getAxisExponent().getTextString().equals(""))
                axisTicks.getAxisExponent().drawString(g2d, x+5,
                        (int) this.attr.getAxisDimension().getMax() - 5, 0, 2);
            
            
            for (int i = 0; i < ticks.size(); i++) {
                double tick = this.getAxisPosition(ticks.get(i));
                g2d.drawLine(x, (int) tick, x + tickLength, (int) tick);
                texts.get(i).drawString(g2d, x - labelOffset, (int) tick, 2, 1);
            }
            
            List<Double> ticksMinor = axisTicks.getAxisTicksMinor();
            for (double tick : ticksMinor) {
                int ytick = (int) this.getAxisPosition(tick);
                g2d.drawLine(x, (int) ytick, x + tickLength/2, (int) ytick);
            }
            int axisBounds = (int) texts.get(0).getBoundsNumber(g2d).getWidth();
            for (int i = 0; i < texts.size(); i++) {
                if (axisBounds < (int) texts.get(i).getBoundsNumber(g2d).getWidth()) {
                    axisBounds = (int) texts.get(i).getBoundsNumber(g2d).getWidth();
                }
            }

            if (!attr.getTitle().getTextString().equals(""))
                attr.getTitle().drawString(g2d, x - axisBounds - labelOffset - titleOffset - 8 - attr.getTitleFontSize(),
                        (int) midpoint,
                        LatexText.ALIGN_CENTER, LatexText.ALIGN_TOP, LatexText.ROTATE_LEFT);

        }

    }

    private void drawColorAxis(Graphics2D g2d, int x, int y) {

        this.setAxisDivisions(10);
        axisTicks.updateFont(this.attr.getLabelFont());
        this.updateAxisDivisions(g2d);

        List<Double> ticks = axisTicks.getAxisTicks();
        List<LatexText> texts = axisTicks.getAxisTexts();
        g2d.setColor(Color.BLACK);

        g2d.drawLine(x, (int) this.attr.getAxisDimension().getMin(), x, (int) this.attr.getAxisDimension().getMax());

        int xstart = x + 4 + 8;
        int ncolors = palette.getPaletteSize();
        double height = Math.abs(this.attr.getAxisDimension().getLength());
        int tickSize = this.attr.getTickSize() / 2;
        //System.out.println(" Draw Z axis X = " + x + " Y = " + y);

        for (int i = 0; i < ncolors; i++) {
            g2d.setColor(palette.getColor3D(i));

            int yp = (int) (((double) i * height) / ncolors);
            int offset = (int) (((double) (i + 1) * height) / ncolors);
            int length = offset - yp;
            //System.out.println("drawing color  " + i + " yp = " + yp);
            g2d.fillRect(x + 4, (int) (y - offset), 8, length);
        }

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x + 4, (int) this.attr.getAxisDimension().getMax(),
                8, (int) Math.abs(this.attr.getAxisDimension().getLength()));
        if (!axisTicks.getAxisExponent().getTextString().equals(""))
            axisTicks.getAxisExponent().drawString(g2d, x+5,
                        (int) this.attr.getAxisDimension().getMax() - 5, 0, 2);
        for (int i = 0; i < ticks.size(); i++) {
            double tick = this.getAxisPosition(ticks.get(i));
            g2d.drawLine(xstart, (int) tick, xstart + tickSize, (int) tick);
            texts.get(i).drawString(g2d, xstart + tickSize + this.attr.getLabelOffset(), (int) tick, 0, 1);
        }
    }

    private void updateAxisDivisions(Graphics2D g2d) {
        
        List<Double> ticks;
        if (this.getLog()) {
            ticks = this.attr.getRange().getDimensionTicksLog(numberOfMajorTicks);
        } else {
            ticks = this.attr.getRange().getDimensionTicks(numberOfMajorTicks);

        }
        
        axisTicks.init(ticks,attr.getAxisMinimum(),attr.getAxisMaximum());
        axisTicks.updateFont(getLabelFont());

        double heights = 0.0;

        if (this.isVertical == true) {
            heights = axisTicks.getTextsHeight(g2d);
        } else {
            heights = axisTicks.getTextsWidth(g2d);
        }

        double axisLength = this.attr.getAxisDimension().getLength();
        double fraction = heights / axisLength;

        if (fraction > 0.6) {

            int nticks = numberOfMajorTicks;
            while (fraction > 0.6 && nticks > 2) {
                //System.out.println("Oh yeah - " + nticks + "  fraction " + fraction );
                nticks--;
                if (this.getLog()) {
                    ticks = this.attr.getRange().getDimensionTicksLog(nticks);
                } else {
                    ticks = this.attr.getRange().getDimensionTicks(nticks);

                }
                axisTicks.init(ticks,attr.getAxisMinimum(), attr.getAxisMaximum());
                //heights  = axisTicks.getTextsHeight(g2d);
                if (this.isVertical == true) {
                    heights = axisTicks.getTextsHeight(g2d);
                } else {
                    heights = axisTicks.getTextsWidth(g2d);
                }
                fraction = heights / axisLength;
            }

            numberOfMajorTicks = nticks;
        }
        if(this.isVertical==true){
            this.axisTicks.processAxisExponent();
        }
        if(this.isLogarithmic==true){
            this.axisTicks.processAxisLogarithmic();
        }
        
        if(this.attr.isLog()==true){
            this.axisTicks.processAxisLogarithmic();
        }
    }

    /*
    public List<LatexText>  getAxisStrings(){
        return this.axisLabels.getLabels();
    }*/
    /**
     * returns string representation of the axis.
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Axis : ");
        str.append(" LOG = ");
        str.append(this.isLogarithmic);
        str.append(String.format("  (%4d x %4d ) : ->  ", (int) this.attr.getAxisDimension().getMin(),
                (int) this.attr.getAxisDimension().getMax()));
        //str.append(axisLabels.toString());
        return str.toString();
    }

    /**
     * main program for testing the axis
     *
     * @param args
     */
    public static void main(String[] args) {
        GraphicsAxis axis = new GraphicsAxis(GraphicsAxis.AXISTYPE_HORIZONTAL);
        axis.setDimension(20, 100);
        axis.setRange(0.0, 120.0);
        //axis.setLog(true);        
        axis.show();
        for (double d = 0; d < 120; d += 0.5) {
           // System.out.println(d + " fraction = " + axis.getAxisPosition(d));
        }
        
        GraphicsAxisTicks ticks = new GraphicsAxisTicks();
        //List<Double>  numbers = new ArrayList<Double>();
        AxisAttributes attr = new AxisAttributes(AxisAttributes.X);
        attr.setAxisMinimum(0.1);
        attr.setAxisMaximum(10000);
        
        List<Double>  numbers = attr.getRange().getDimensionTicksLog(10);
        
        for(int i = 0; i < numbers.size(); i++){
            System.out.println( i + " -> " + numbers.get(i));
        }
        
        ticks.init(numbers, 0.1, 10000);
        ticks.processAxisLogarithmic();
        
        List<LatexText> texts = ticks.getAxisTexts();
        
        int i = 0;
        for(LatexText text : texts){
            System.out.println( i + " " + text.getTextString());
            i++;
        }
    }

    public static class GraphicsAxisTicks {

        List<LatexText> axisTexts = new ArrayList<LatexText>();
        List<Double>    axisTicks = new ArrayList<Double>();
        List<Double>    axisTicksMinor = new ArrayList<Double>();        
        FontProperties  axisFontProperty = new FontProperties();
        LatexText          axisExponent  = new LatexText("");
        boolean        isLog            = false;
        
        void GraphicsAxisString() {

        }

        public void setLog(boolean flag){
            this.isLog = flag;
        }
        
        public boolean getLog(){
            return this.isLog;
        }
        
        public LatexText getAxisExponent(){
            return this.axisExponent;
        }
        
        public List<Double> getAxisTicks() {
            return this.axisTicks;
        }

        public List<Double> getAxisTicksMinor(){
            return this.axisTicksMinor;
        }
        
        public List<LatexText> getAxisTexts() {
            return this.axisTexts;
        }

        public void updateFont(FontProperties fp) {
            this.axisFontProperty.setFontName(fp.getFontName());
            this.axisFontProperty.setFontSize(fp.getFontSize());
        }

        public double getTextsWidth(Graphics2D g2d) {
            double width = 0;
            for (LatexText text : this.axisTexts) {
                Rectangle2D rect = text.getBoundsNumber(g2d);
                width += rect.getWidth();
            }
            return width;
        }

        public double getTextsHeight(Graphics2D g2d) {
            double width = 0;
            for (LatexText text : this.axisTexts) {
                Rectangle2D rect = text.getBoundsNumber(g2d);
                width += rect.getHeight();
            }
            return width;
        }
        
        private int getTrailingZeros(String number){            
            if(number.compareTo("0")==0) return 30;
            int tz = 0; int pos = number.length()-1;
            while(number.charAt(pos)=='0'&&pos>=0){
                pos--; tz++;
            }
            return tz;
        }
        
        private int getMinZeros(){
            int minZeros = 30;
            for(LatexText tex : this.axisTexts){
                int tz = this.getTrailingZeros(tex.getTextString());
                if(tz<minZeros) minZeros = tz;
            }
            //axisExponent = String.format("x%d", minZeros);
            return minZeros;
        }
        
        public void processAxisLogarithmic(){
            //System.out.println("Processing Logarithmic Scale");
            int nticks = this.axisTicks.size();
            for(int i = 0; i < nticks; i++){
                Integer exponent = (int) Math.log10(this.axisTicks.get(i));
                //System.out.println(" tick = " + 
                //        this.axisTicks.get(i) + "  value = " 
                //+ String.format("10^%d", exponent));
                
                String number = exponent.toString();
                StringBuilder str = new StringBuilder();
                str.append("10");
                for(int k = 0; k < number.length(); k++) 
                    str.append("^").append(number.charAt(k));
                this.axisTexts.get(i).setText(str.toString());
            }
        }
        
        public void processAxisExponent(){
            int minZeros = getMinZeros();
            if(minZeros>2){
                axisExponent.setText(String.format("x%d", minZeros));
                for(LatexText tex : this.axisTexts){                    
                    String value = tex.getTextString();
                    if(value.length()>minZeros){
                        //int pos = 
                        tex.setText(value.substring(0, value.length()-minZeros));
                    }
                }
            } else {
                axisExponent.setText("");
            }            
        }
        
        public void show(){
            System.out.print("TICKS  (" + axisExponent + ") : ");
            for(int i = 0; i < this.axisTexts.size(); i++){
                System.out.print("  "+ this.axisTexts.get(i).getTextString());
            }
            System.out.println();
        }
        
        public void init(List<Double> ticks, double min, double max) {

            axisTexts.clear();
            axisTicks.clear();
            axisTicksMinor.clear();
            
            int significantFigures = this.getSignificantFigures(ticks);

            if (significantFigures < 0) {
                significantFigures = -1;
            }

            axisTicks.addAll(ticks);

            for(int i = 0; i < axisTicks.size()-1; i++){
                double step = 0.2*(axisTicks.get(i+1) - axisTicks.get(i));
                for(int k = 0; k < 4; k++){
                    axisTicksMinor.add( axisTicks.get(i) + step*(k+1));
                }
            }
            
            if(axisTicks.size()>1){
                int size = axisTicks.size();                
                double step = 0.2*(axisTicks.get(size-1) - axisTicks.get(size-2));
                double tick = axisTicks.get(size-1);
                for(int k = 0; k < 4; k++){
                    double tickMinor = tick + step*(k+1);                    
                    if(tickMinor < max)
                        axisTicksMinor.add(tickMinor);
                }
                tick = axisTicks.get(0);
                for(int k = 0; k < 4; k++){
                    double tickMinor = tick - step*(k+1);                    
                    if(tickMinor > min)
                        axisTicksMinor.add(tickMinor);
                }
            }
            
            for (int i = 0; i < axisTicks.size(); i++) {
                LatexText text;
                if (getLog()) {
                    int sigFig =  (int) -Math.floor(Math.log10(axisTicks.get(i)));
                    text = LatexText.createFromDouble(axisTicks.get(i),
                           Math.max(0, sigFig));
                } else {
                    text = LatexText.createFromDouble(axisTicks.get(i),
                            significantFigures + 1);
                }
                text.setFont(this.axisFontProperty.getFontName());
                text.setFontSize(this.axisFontProperty.getFontSize());
                axisTexts.add(text);
            }
        }

        public int getSignificantFigures(List<Double> array) {
            if (array.size() < 2) {
                return 0;
            }
            double min = array.get(0);
            double max = array.get(array.size() - 1);
            double difference = max - min;

            int placeOfDifference = (int) Math.floor(Math.log10(difference));
            return -placeOfDifference;
        }
    }

    public boolean isAutoScale() {
        return attr.isAxisAutoScale();
    }

    public void setAutoScale(boolean b) {
        attr.setAxisAutoScale(b);
    }

    public AxisAttributes getAttributes() {
        return attr;
    }

    public void setAttributes(AxisAttributes attr) {
        this.attr = attr;
    }

    public boolean getGrid() {
        return attr.getGrid();
    }

    public void setGrid(boolean isGrid) {
        attr.setGrid(isGrid);
    }

    public String getTitle() {
        return attr.getAxisTitleString();
    }

    public int getTitleFontSize() {
        return attr.getTitleFontSize();
    }

    public int getLabelFontSize() {
        return attr.getLabelFontSize();
    }

    public double getMin() {
        return attr.getAxisMinimum();
    }

    public double getMax() {
        return attr.getAxisMaximum();
    }

    public void setShowAxis(boolean b) {
        this.attr.setShowAxis(b);
    }

    public boolean isShowAxis() {
        return this.attr.showAxis();
    }

    void setPalette(TColorPalette palette) {
        this.palette = palette;
    }
}
