/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jlab.groot.data.DataVector;
import org.jlab.groot.data.H1F;
import org.jlab.groot.math.Axis;
import org.jlab.groot.ui.TCanvas;

/**
 *
 * @author gavalian
 */
public class SparseVectorGrid {
    
    SparseIndex               indexer;
    HashMap<Long,DataVector>   binMap;
    private int  vectorSize   = 1;
    private List<Axis>  gridAxis = new ArrayList<Axis>();
    
    public SparseVectorGrid(){
        
    }
    
    public SparseVectorGrid(int[] bins){
        this.indexer = new SparseIndex(bins);
        this.binMap  = new HashMap<Long,DataVector>();
        for(int i = 0; i < bins.length; i++) gridAxis.add(new Axis(bins[i],0.0,1.0));
    }
    
    public SparseVectorGrid(int size, int[] bins){
        this.vectorSize = size;
        this.indexer = new SparseIndex(bins);
        this.binMap = new HashMap<Long,DataVector>();
        for(int i = 0; i < bins.length; i++) gridAxis.add(new Axis(bins[i],0.0,1.0));
    }
    
    public HashMap<Long,DataVector> getGrid(){ return binMap;}
    
    public int getVectorSize(){
        return this.vectorSize;
    }
    
    
    public void addBin(int[] index){
        Long key = indexer.getKey(index);
        binMap.put(key, new DataVector(this.vectorSize));
    }
    
    public void addBin(DataVector vec, int[] index){
        Long key = indexer.getKey(index);
        binMap.put(key, vec);
    }
    
    public Axis getAxis(int index){
        return this.gridAxis.get(index);
    }
    /**
     * sets names for all the axis.
     * @param names array of names to set axis titles
     */
    public void setAxisNames(String[] names){
        if(names.length!=this.gridAxis.size()){
            System.out.println("[setAxisNames] ERROR : Names do not have same dimension as axis");
        } else {
            for(int i = 0; i < names.length; i++) this.gridAxis.get(i).setTitle(names[i]);
        }
    }
    /**
     * fills the array of indices with bin values taken from the axis. 
     * @param values axis values
     * @param index returned index for each dimension
     * @return returns true if all indices are within the bounds, false otherwise
     */
    public boolean getBinsByAxis(double[] values, int[] index){
        boolean flag = true;
        for(int i = 0; i < values.length; i++){
            index[i] = this.gridAxis.get(i).getBin(values[i]);
            if(index[i]<0||index[i]>=this.gridAxis.get(i).getNBins())
                flag = false;
        }  
        return flag;
    }    
    /**
     * fill the grid element 0 with weight of 1.0
     * @param array values along the axis
     */
    public void fill(double[] array){
        this.fill(array,0,1.0);
    }
    /**
     * fill the grid with values from array for element = order
     * with default weight of 1.0
     * @param array values along the axis
     * @param order element index
     */
    public void fill(double[] array, int order){
        this.fill(array,order,1.0);
    }
    /**
     * increment the value of the particular bin, the bin
     * is determined by axis. order is index in the array.
     * @param array values along the axis
     * @param order element index
     * @param weight increment amount
     */
    public void fill(double[] array, int order, double weight){
        int[] index = new int[array.length];
        boolean status = this.getBinsByAxis(array, index);
        if(status==true){
            DataVector vec = getBinVector(index);
            double value = vec.getValue(order);
            vec.set(order, value + weight);
        }
    }
    /**
     * returns a vector for a bin, if the entry does not exist
     * a NULL will be returned. 
     * @param index
     * @return 
     */
    public DataVector getBin(int[] index){
        Long key = indexer.getKey(index);
        if(binMap.containsKey(key)==true) return binMap.get(key);
        return null;
    }
    /**
     * returns vector for particular bin, if the entry does not exist
     * in the map, it creates a new one.
     * @param index
     * @return 
     */
    private DataVector getBinVector(int[] index){
        Long key = indexer.getKey(index);
        if(binMap.containsKey(key)==false){            
            binMap.put(key, new DataVector(vectorSize));
        } 
        return binMap.get(key);
    }
    /**
     * returns ":" separated list of axis names
     * @return string with all axis names
     */
    public String getAxisNamesString(){
        StringBuilder str = new StringBuilder();
        str.append(this.gridAxis.get(0).getTitle());
        for(int i = 1; i < this.gridAxis.size(); i++){
            str.append(":");
            str.append(gridAxis.get(i).getTitle());
        }
        return str.toString();
    }
    
    public void addBinContent(int order, double value, int[] index){
        Long key = indexer.getKey(index);
        DataVector  vec = getBinVector(index);
        //System.out.println(" setting value = " +  (value + vec.getValue(order)));
        vec.set(order, value + vec.getValue(order));
    }
    
    public void setBinContent(int order, double value, int[] index){
        Long key = indexer.getKey(index);
        DataVector  vec = getBinVector(index);
        vec.set(order, value);
    }
    
    public double getBinContent(int order, int[] index){
        DataVector vec = this.getBin(index);
        if(vec==null) return 0.0;
        return vec.getValue(order);
    }
    
    public SparseIndex  getIndexer(){
        return indexer;
    }
    
    public double integral(int order){        
        double summ = 0.0;
        for(Map.Entry<Long,DataVector>  entry : this.binMap.entrySet()){
            summ += entry.getValue().getValue(order);
        }
        return summ;
    }
    
    public H1F slice(int dim, int order, int[] bmin, int[] bmax){
        int bins = this.indexer.getBinsPerAxis()[dim];
        H1F h1 = new H1F("h1",bins,0.0,1.0);
        int[] keyBins = new int[indexer.getRank()];
        
        for(Map.Entry<Long,DataVector>  entry : this.binMap.entrySet()){
            boolean doesFill = true;
            
            indexer.getIndex(entry.getKey(), keyBins);
        
            int nbin = keyBins[dim];
            
            for(int i = 0; i < keyBins.length;i++){
                if( (keyBins[i]<bmin[i]||keyBins[i]>bmax[i])&&i!=dim) doesFill = false;
            }
            
            if(doesFill==true){
                /*System.out.print("filling key bins : ");
                for(int i = 0; i < keyBins.length; i++) System.out.print(" " + keyBins[i]);
                System.out.println();*/
                double center = h1.getXaxis().getBinCenter(nbin);
                h1.fill(center,entry.getValue().getValue(order));
            }
        }
        h1.setFillColor(43);
        return h1;
    }
    
    public H1F projection(int dim, int order){
        int bins = this.indexer.getBinsPerAxis()[dim];
        H1F h1 = new H1F("h1",bins,0.0,1.0);
        
        int[] keyBins = new int[indexer.getRank()];
        
        for(Map.Entry<Long,DataVector>  entry : this.binMap.entrySet()){
            indexer.getIndex(entry.getKey(), keyBins);
            int nbin = keyBins[dim];
            double center = h1.getXaxis().getBinCenter(nbin);
            h1.fill(center,entry.getValue().getValue(order));
        }
        h1.setFillColor(43);
        return h1;
    }
    
    public int getNumberOfBins(){
        int nbins = 1;
        for(int i = 0; i < this.gridAxis.size(); i++){
            nbins *= this.gridAxis.get(i).getNBins();
        }
        return nbins;
    }
    
    public void show(){
        
        StringBuilder str = new StringBuilder();        
        int        nbins = this.getNumberOfBins();
        int     nentries = this.binMap.size();
        double populated = ((double) nentries)/nbins;
        
        str.append(String.format("SPARSE GRID  : NBINS = %12d ,  ENTRIES = %12d, POPULATION = %8.1f %%\n", 
                nbins,nentries, 100*populated));
        str.append(String.format("GRID COLUMNS : %-12d \n",this.vectorSize));
        str.append(String.format("INDEXER      : %s\n", this.getIndexer().toString())); 
        for(int i = 0; i < this.vectorSize; i++){
            str.append(String.format("  ORDER %4d : INTEGRAL = %e\n", i,this.integral(i)));
        }
        
        for(int i = 0; i < this.gridAxis.size(); i++){
            str.append(String.format("AXIS  # %2d (%12s) : %5d %12.5f %12.5f\n", i,
                    this.gridAxis.get(i).getTitle(),
                    this.gridAxis.get(i).getNBins(),
                    this.gridAxis.get(i).min(),
                    this.gridAxis.get(i).max()                    
                    ));
        }
        
        System.out.println(str.toString());
    }
    
    public static void main(String[] args){
        
        SparseVectorGrid grid = new SparseVectorGrid(2,new int[]{10,20,30});
        grid.addBinContent(0, 12.0, new int[]{1,2,3});
        grid.addBinContent(1, 24.0, new int[]{1,2,3});
        grid.show();
        
        H1F h1 = grid.projection(2,1);
        TCanvas c1 = new TCanvas("grid",500,500);
        c1.draw(h1);
    }
}
