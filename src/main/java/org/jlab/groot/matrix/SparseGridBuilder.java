/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.matrix;

import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.math.Axis;

/**
 *
 * @author gavalian
 */
public class SparseGridBuilder {
    
    private List<Axis>    gridAxis = new ArrayList<Axis>();
    private int         vectorSize = 1;
    
    public SparseGridBuilder(){
        
    }
    
    public SparseGridBuilder(int vsize){
        this.vectorSize = vsize;
    }
    
    public SparseGridBuilder axis(String name, int bins, double min, double max){
        Axis a = new Axis(bins,min,max);
        a.setTitle(name);
        gridAxis.add(a);        
        return this;
    }
    
    public SparseGridBuilder axis(Axis a){
        gridAxis.add(a);
        return this;
    }
    
    public SparseGridBuilder axis(String name, double[] binLimits){
        Axis a = new Axis(binLimits);
        a.setTitle(name);
        gridAxis.add(a);        
        return this;
    }
    
    public SparseGridBuilder vectorSize(int vsize){
        this.vectorSize = vsize;
        return this;
    }
    
    public SparseVectorGrid build(){
        
        int[] bins = new int[this.gridAxis.size()];
        for(int i = 0; i < bins.length; i++){
            bins[i] = gridAxis.get(i).getNBins();
        }
        
        SparseVectorGrid grid = new SparseVectorGrid(this.vectorSize,bins);
        
        for(int dim = 0; dim < bins.length; dim++){
            grid.getAxis(dim).set(this.gridAxis.get(dim).getLimits());
            grid.getAxis(dim).setTitle(this.gridAxis.get(dim).getTitle());
        }
        
        return grid;
    }
}
