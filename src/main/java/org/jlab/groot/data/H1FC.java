/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavalian
 */
public class H1FC {
    
    private List<H1F>            histograms = new ArrayList<H1F>();
    private List<H1FRegion>         regions = new ArrayList<H1FRegion>();
    private List<Integer>      regionColors = new ArrayList<Integer>();
    
    public H1FC(String name, int bins, double min, double max){
        H1F h = new H1F(name,bins,min,max);
        histograms.add(h);
        regions.add(new H1FRegion(min,max));
        for(int i = 0; i < 10; i++){
            regionColors.add(i+1);
        }
    }
    
    public H1FC addCut(double min, double max){
        
        H1FRegion region = new H1FRegion(min,max);
        H1F ref = histograms.get(0);
        H1F h = new H1F(ref.getName(),ref.getXaxis().getNBins(),
                ref.getXaxis().min(), ref.getXaxis().max());
        
        int color = regions.size()+1;
        if(color>10) color = color%10;
        
        h.setLineColor(color);
        h.setFillColor(color+40);
        regions.add(region);
        histograms.add(h);
        return this;
    }
    
    public void setLineColor(int color){
        for(int i = 0; i < histograms.size(); i++){
           histograms.get(i).setLineColor(color);
       } 
    }
    public void setFillColor(int color){
       for(int i = 0; i < histograms.size(); i++){
           histograms.get(i).setFillColor(color+i*10);
       } 
    }
    
    public void fill(double value, double weight){
        int nh = histograms.size();
        for(int i = 0; i < nh; i++){
            if(regions.get(i).contains(value)==true){
                histograms.get(i).fill(value, weight);
            }
        }
    }
    
    public void fill(double value){
        fill(value,1.0);
    }
    
    public boolean   contains(int cut, double value){ return this.regions.get(cut).contains(value);}
    
    public List<H1F> getDataSets(){return histograms;}
    
    public static class H1FRegion {
        double min = 0.0;
        double max = 0.0;
        public H1FRegion(double __min, double __max) {min = __min; max = __max;}
        public boolean contains(double value) { return (value>=min&&value<=max);}
    }
}
