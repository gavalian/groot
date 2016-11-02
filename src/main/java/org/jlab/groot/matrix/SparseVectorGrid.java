/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.matrix;

import java.util.HashMap;
import java.util.Map;
import org.jlab.groot.data.DataVector;
import org.jlab.groot.data.H1F;
import org.jlab.groot.ui.TCanvas;

/**
 *
 * @author gavalian
 */
public class SparseVectorGrid {
    
    SparseIndex               indexer;
    HashMap<Long,DataVector>   binMap;
    private int  vectorSize   = 1;
    
    public SparseVectorGrid(){
        
    }
    
    public SparseVectorGrid(int[] bins){
        this.indexer = new SparseIndex(bins);
        this.binMap = new HashMap<Long,DataVector>();
    }
    
    public SparseVectorGrid(int size, int[] bins){
        this.vectorSize = size;
        this.indexer = new SparseIndex(bins);
        this.binMap = new HashMap<Long,DataVector>();
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
    
    public DataVector getBin(int[] index){
        Long key = indexer.getKey(index);
        if(binMap.containsKey(key)==true) return binMap.get(key);
        return null;
    }
    
    private DataVector getBinVector(int[] index){
        Long key = indexer.getKey(index);
        if(binMap.containsKey(key)==false){            
            binMap.put(key, new DataVector(vectorSize));
        } 
        return binMap.get(key);
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
        return h1;
    }
    
    public void show(){        
        StringBuilder str = new StringBuilder();        
        System.out.println(" MATRIX SIZE = " + binMap.size());
        System.out.println(" INDEX = " + this.indexer.toString());
        for(int i = 0; i < this.vectorSize; i++){
            str.append(String.format(" ORDER %4d : INTEGRAL = %15.6f\n", i,this.integral(i)));
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
