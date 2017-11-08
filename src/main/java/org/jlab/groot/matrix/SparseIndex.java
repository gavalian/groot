/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.matrix;

import org.jlab.jnp.hipo.data.HipoByteUtils;


/**
 *
 * @author dmriser
 */
public class SparseIndex {
    
    int[] binsPerAxis; 
    int[] bitsPerAxis;
    int[] offset;
    int rank;
    
    public SparseIndex(int[] binsPerAxis) {
        this.binsPerAxis = binsPerAxis;
        this.rank = binsPerAxis.length;
        bitsPerAxis = new int[rank];
        offset = new int[rank];     
        calculateBitsPerAxis();
        calculateOffset();
    }

    private void calculateBitsPerAxis(){   
        for(int axis = 0; axis < rank; axis++){
            bitsPerAxis[axis] = Integer.SIZE - Integer.numberOfLeadingZeros(binsPerAxis[axis]);
        }
    }
    
    private void calculateOffset(){
        int totalOffset = 0; 
        for (int iaxis = 0; iaxis < rank; iaxis++){
            offset[iaxis] = totalOffset; 
            totalOffset += bitsPerAxis[iaxis]; 
        }
    }
    
    public long getKey(int [] bin){        
        if (bin.length != rank){ throw new ArrayStoreException(); }
        
        long key = 0;        
        for (int iaxis = 0; iaxis < rank; iaxis++){
            key = HipoByteUtils.writeLong(key,bin[iaxis],offset[iaxis],offset[iaxis]+bitsPerAxis[iaxis]);
        }
        return key;
    }
    /**
     * Checks two SparseIndex classes for compatability. The
     * rank as well as the number of bins in each dimension have
     * to be equal for the SparseIndex to be compatible !
     * @param sindex SparseIndex class
     * @return true is compatible, false otherwise
     */
    public boolean isCompatible(SparseIndex sindex){
        if(this.getRank()!=sindex.getRank()) return false;
        int[] bins = sindex.getBinsPerAxis();
        for(int i = 0; i < bins.length; i++)
            if(bins[i]!=this.binsPerAxis[i]) return false;
        return true;
    }
    // Assume the array size is correct. 
    public long getKeyFast(int [] bin){        
        long key = 0;        
        for (int axis = 0; axis < rank; axis++){
            key = HipoByteUtils.writeLong(key,bin[axis],offset[axis],offset[axis]+bitsPerAxis[axis]);
        }
        return key;
    }
    
    public void getIndex(long key, int[] bin) {       
        int index = 0; 
        for (int iaxis = 0; iaxis < rank; iaxis++){
            bin[iaxis] = HipoByteUtils.readLong(key, index, index+bitsPerAxis[iaxis]-1);
            index += bitsPerAxis[iaxis]; 
        }   
    }
    
    public int getOffset(int axis){
        return offset[axis];
    }
    
    public int getRank(){
        return rank;
    }
    
    public int [] getBinsPerAxis(){
        return binsPerAxis; 
    }
    
    public int [] getBinsPerAxisCopy(){
        int[] bins = new int[binsPerAxis.length];
        for(int i = 0; i < bins.length; i++) bins[i] = this.binsPerAxis[i];
        return bins; 
    }
    
    public int [] getBitsPerAxis(){
        return bitsPerAxis; 
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{binsPerAxis, bitsPerAxis} = ");
        
        for (int iAxis = 0; iAxis < rank; iAxis++){
            builder.append(String.format("{%d,%d} ",binsPerAxis[iAxis],bitsPerAxis[iAxis])); 
        }
        return builder.toString();
    }
  
}
