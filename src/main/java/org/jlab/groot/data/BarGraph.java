/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.base.DatasetAttributes;
import org.jlab.groot.base.GStyle;
import org.jlab.groot.ui.PaveText;
import org.jlab.jnp.groot.graphics.Legend;

/**
 *
 * @author gavalian
 */
public class BarGraph implements IDataSet {
    
    private List<String>        barLabels = new ArrayList<>();
    private List<DataVector>    barVectors = new ArrayList<>();
    private List<String >    measureLabels = new ArrayList<>();
    private List<Integer>       barFillColors = new ArrayList<>();
    private List<Integer>       barLineColors = new ArrayList<>();
    
    private String              barGraphName = "barGraph";
    
    private int                 barCategoryPadding = 4;
    private int                 barWidth           = 20;
    private int                 barGaps            = 20;
    
    DatasetAttributes hAttr     = new DatasetAttributes(DatasetAttributes.HISTOGRAM);
    
    public BarGraph(){
        try {
    		this.hAttr = GStyle.getH1FAttributes().clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public BarGraph(String[] labels, double[][] data){
        init(labels, data);
        try {
    		this.hAttr = GStyle.getH1FAttributes().clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    protected final void init(String[] labels, double[][] data){
        
        this.barLabels.clear();
        this.barVectors.clear();

        for(int c = 0; c < labels.length; c++){
            barLabels.add(labels[c]);
            DataVector vector = new DataVector();
            for(int r = 0; r < data[c].length; r++){
                vector.add(data[c][r]);
            }
            barVectors.add(vector);                       
        }
        
        int color = 2;        
        for(int i = 0; i < barVectors.get(0).size(); i++){
            barFillColors.add(color);
            barLineColors.add(color);
            int order = i+1;
            measureLabels.add("set " + order);
            color++;
            if(color>=10) color = 2;
        }
    }
        
    public int getCount(){
        return this.barLabels.size();
    }
    
    
    public BarGraph    setPadding(int padding){ 
        barCategoryPadding = padding; return this;
    }
    
    public BarGraph setBarColor(int index, int color){
        this.barFillColors.set(index, color);
        return this;
    }
    
    public BarGraph  setBarWidth(int width){
        barWidth = width; return this;
    }
    
    public BarGraph  setBarGaps(int gap){
        barGaps = gap; return this;
    }
    
    public int getPadding(){return barCategoryPadding;}
    public int getBarWidth(){ return barWidth;}
    public int getBarGaps(){ return barWidth;}
    
    
    public String      getLabel(int index) { return this.barLabels.get(index);}
    public DataVector  getVector(int index){return this.barVectors.get(index);}
    public int         getFillColor(int index){ return this.barFillColors.get(index);}
    public int         getLineColor(int index){ return this.barLineColors.get(index);}
    
    protected int getMaximumLabelLength(){
        int length = 0;
        for(String item : barLabels){
            if(item.length()>length) length = item.length();
        }
        return length;
    }
    
    protected String getLabelFormat(){
        int maxLength = getMaximumLabelLength();
        StringBuilder str = new StringBuilder();
        str.append("%").append(maxLength).append("s");
        return str.toString();
    }
    
    public double getMax(){
        if(barVectors.size()==0) return 0.0;
        double max = barVectors.get(0).getMax();
        for(int i = 0; i < barVectors.size(); i++){
            if(barVectors.get(i).getMax()>max) max = barVectors.get(i).getMax();
        }
        return max;
    }
    
    public double getMin(){
        if(barVectors.size()==0) return 0.0;
        double min = barVectors.get(0).getMax();
        for(int i = 0; i < barVectors.size(); i++){
            if(barVectors.get(i).getMin()<min) min = barVectors.get(i).getMin();
        }
        return min;
    }
    
    
    public String getDataString(){
        StringBuilder str = new StringBuilder();
        String     format = getLabelFormat();
        str.append("#-------------------------------------------------\n");
        for(int i = 0; i < barLabels.size(); i++){
            str.append("#@ ");
            str.append(String.format("%s = (max=%f, sum=%f)\n", 
                    barLabels.get(i),
                    barVectors.get(i).getMax(),
                    barVectors.get(i).sum()
                    ));
        }
        str.append("#-------------------------------------------------\n");
        for(int i = 0; i < barLabels.size(); i++){
            str.append(String.format(format, barLabels.get(i)));
            str.append(" | ");
            str.append(barVectors.get(i).getVectorString()).append("\n");
        }
        return str.toString();
    }
    
    @Override
    public String toString(){
        return getDataString();
    }
    
    public static void main(String[] args){
        BarGraph bar = new BarGraph(new String[]{"CPU","GPU"},
                new double[][]{
                    {0.4,0.5,0.9,0.975},
                    {0.2,0.7,0.2,0.8756}
                });
        System.out.println(bar);
    }

    @Override
    public void setName(String name) {
        this.barGraphName = name;
    }

    @Override
    public void reset() {
        
    }

    @Override
    public String getName() {
        return this.barGraphName;
    }

    @Override
    public int getDataSize(int axis) {
        //if(barVectors.size()==0) return 0;
        if(axis==0){
            //if(barVectors.size()==0) return 0;
            return getCount();
        } 
        
        return barVectors.get(0).size();        
    }

    @Override
    public DatasetAttributes getAttributes() {
        return this.hAttr;
    }

    @Override
    public double getDataX(int bin) {
        return 0.0;
    }

    @Override
    public double getDataY(int bin) {
        return 0.0;
    }

    @Override
    public double getDataEX(int bin) {
        return 0.0;
    }

    @Override
    public double getDataEY(int bin) {
        return 0.0;
    }

    @Override
    public double getData(int xbin, int ybin) {
        return 0.0;
    }

    @Override
    public PaveText getStatBox() {
        return new PaveText(2);
    }

    public Legend getLegend(String[] descriptions){
        Legend legend = new Legend(55,30);
        legend.setStyle(org.jlab.jnp.groot.graphics.PaveText.PaveTextStyle.ONELINE);
        for(int i = 0; i < this.barFillColors.size(); i++){
            H1F h = new H1F("h",1,0,1);
            h.setFillColor(this.barFillColors.get(i));
            h.setLineColor(1);
            h.setLineWidth(1);
            legend.add(h, descriptions[i]);
        }
        return legend;
    }
    
    @Override
    public void save(String filename) {
        // Reserved to save
    }
}
