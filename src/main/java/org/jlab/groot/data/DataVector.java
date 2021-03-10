/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * This class provides a data vector which can be filled with data, and
 * various methods are imeplemented for calculating minimum maximum , mean 
 * and rms for the data. This is essential class for data sets graphs and 
 * histograms.
 */

package org.jlab.groot.data;

import java.util.ArrayList;
import java.util.List;
import org.jlab.jnp.readers.TextFileReader;

/**
 *
 * @author gavalian
 */
public class DataVector {
    
    private final ArrayList<Double>  datavec = new ArrayList<Double>();
    private Boolean isVectorOrdered = true;
    private Boolean isFixedLength   = false;
    
    
    public DataVector(int size){
        isFixedLength = true;
        for(int i = 0; i < size; i++){
            datavec.add(0.0);
        }
    }
    
    public DataVector(){
        
    }
    
    public DataVector(double[] data){
        this.set(data);
    }
    
    public DataVector(List<Double> data){
        set(data);
    }
    /**
     * Initialize the array with given values.
     * @param data initial data
     */
    public final void set(double[] data){
        datavec.clear();
        for(int loop = 0; loop < data.length; loop++){
            if(loop!=0) if(data[loop]<data[loop-1]) isVectorOrdered = false;
            datavec.add(data[loop]);
        }
    }
    
    public final void set(List<Double> data){
        this.datavec.clear();
        for(Double item : data){
            this.add(item);
        }
    }
    
    public int  size(){ return datavec.size();}
    public void clear() { datavec.clear();}
    
    public void addDataVector(DataVector vec){
        if(vec.getSize()!=this.getSize()){
            System.out.println("[addDataVector] error adding vectors. sizes are different");
            return;
        }
        
        for(int i = 0; i < this.getSize(); i++){
            this.datavec.set(i, this.getValue(i)+vec.getValue(i));
        }
    }
    /**
     * Add value to the vector and ensure that the vector is ordered. If the value
     * does not appear to be in ordered mode, then set the flag ordered to FALSE.
     * @param value next value to the data vector.
     */
    public void add(double value) {
        if(this.isFixedLength==false){
            /**
             * If the vector is ordered at this point check if the next element
             * is compliant with the order, if not set the ordered flag to FALSE.
             * No checks will be performed from that point on.
             */
            if(isVectorOrdered==true){
                if(!datavec.isEmpty()) 
                    if(value<datavec.get(datavec.size()-1))
                        isVectorOrdered = false;
            }
            datavec.add(value); 
        } else {
            System.out.println("[DataVector] error : add function does not work with fixed length vectors");
        }
    }
    /**
     * calculate the minimum value in the data.
     * @return minimum of all data points
     */
    public double getMin(){
        if(datavec.size()<1) return 0.0;
        double min = datavec.get(0);
        for(Double value : datavec){
            if(value<min) min = value;
        }
        return min;
    }
    /**
     * calculate maximum value in the data
     * @return maximum value of the data.
     */
    public double getMax(){
        if(datavec.size()<1) return 0.0;
        double max = datavec.get(0);
        for(Double value : datavec){
            if(value>max) max = value;
        }
        return max;
    }
    
    public int  getBinSuggestion(){
        return 100;
    }
    
    public void copy(DataVector vec){
        this.datavec.clear();
        for(int loop = 0; loop < vec.getSize(); loop++){
            this.add(vec.getValue(loop));
        }
    }
    
    public boolean isOrdered(){ return isVectorOrdered;}
    
    public int findBin(double value){
        return this.findBin(value,0);
    }
    
    public int findBin(double value, int start){
        if(start>=this.getSize()) return -1;
        
        for(int loop = start; loop < this.getSize(); loop++){
            //System.err.println("comparing " + value + " " + this.getValue(loop));
            if(this.getValue(loop)>value) return loop;
        }
        return -1;
    }
    
    /**
     * Multiplies the whole data with the provided number
     * @param norm multiplication factor
     */
    public void mult(double norm){
        for(int loop = 0; loop < datavec.size(); loop ++){
            double newValue = this.getValue(loop)*norm;
            datavec.set(loop, newValue);
        }
    }
    /**
     * Divides the content of the vector by given number.
     * @param norm 
     */
    public void divide(double norm){
        for(int loop = 0; loop < datavec.size(); loop ++){
            double newValue = this.getValue(loop)/norm;
            datavec.set(loop, newValue);
        }
    }
    /**
     * Returns cumulative integral of the vector.
     * @return 
     */
    public DataVector getCumulative(){
        DataVector data = new DataVector();
        double integral = 0.0;
        for(Double value : datavec){
            data.add(integral);
            integral += value;
        }
        return data;
    }
    /**
     * Calculates the normalized mean with the given axis.
     * @param xvec
     * @return 
     */
    public double getMean(DataVector xvec){
        if(datavec.size()<1) return 0.0;
        if(xvec.getSize()!=this.getSize()){
            System.err.println("DataVector::getMean: ** ERROR ** : "
            + " data vectors doe not have the same size.");
            return 0.0;
        }
        if(xvec.isOrdered()==false){
            System.err.println("DataVector::getMean: ** ERROR ** : "
            + " the vector passed to the routine is not ordered.");
            return 0.0;
        }
        double runsumm = 0.0;
        int count = 0;
        for(int loop = 0; loop < this.getSize(); loop++){
            runsumm += this.getValue(loop)*xvec.getValue(loop);
            count++;
        }
        return runsumm/count;
    }
    /**
     * Calculates the mean of the vector. 
     * @return mean value
     */
    public double getMean(){
        if(datavec.size()<1) return 0.0;
        double runsumm = 0.0;
        for(Double value : datavec){
            runsumm += value;
        }
        return (runsumm/datavec.size());
    }
    
    
    public double getRMS(){
        double rms  = 0.0;
        double mean = this.getMean();
        for(Double value : datavec){
            rms += (value-mean)*(value-mean);
        }
        double rms2 = rms/datavec.size();
        return Math.sqrt(rms2);
    }
    
    public double sum(){
        double s = 0.0;
        for(int i = 0; i < getSize(); i++) s += getValue(i);
        return s;
    }
    
    public String getVectorString(){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < getSize(); i++){
            str.append(String.format("%12.6f", getValue(i)));
        }
        return str.toString();
    }
    /**
     * Returns the number of entries in the vector
     * @return size
     */
    public int  getSize() { return datavec.size();}
    
    public double getValue(int index) {
        if(index<0||index>=datavec.size()){
            System.err.println("DataVector:: ** ERROR ** : requested element "
            + index + " in the vector of size = " + datavec.size());
        }
        return datavec.get(index);
    }
    /**
     * Returns the low edge for the bin which is determined by bin width.
     * @param bin bin number
     * @return the low edge for the bin.
     */
    public double getLowEdge(int bin){
        if(isVectorOrdered==false){
            System.err.println("DataVector:: ** ERROR ** : this vector is not ordered."
            + " Can not define low egde for the bin");
            return 0.0;
        }
        double value = this.getValue(bin);
        double dist = 0.0;
        if(bin==0){
            dist = Math.abs(this.getValue(bin+1) - value);
        } else {
            dist = Math.abs(this.getValue(bin-1) - value);
        }
        value = value - 0.5*dist;
        return value;
    }
    
    public double getHighEdge(int bin){
        if(isVectorOrdered==false){
            System.err.println("DataVector:: ** ERROR ** : this vector is not ordered."
            + " Can not define low egde for the bin");
            return 0.0;
        }
        double value = this.getValue(bin);
        double dist = 0.0;
        if(bin==this.getSize()-1){
            dist = Math.abs(this.getValue(bin-1) - value);
        } else {
            dist = Math.abs(this.getValue(bin+1) - value);
        }
        value = value + 0.5*dist;
        return value;
    }
    
    public double getBinWidth(int index){
        if(isVectorOrdered==false){
            System.err.println("DataVector:: ** ERROR ** : this vector is not ordered."
            + " Bin widths do not make sense.");
            return 0.0;
        }
        return this.getHighEdge(index)-this.getLowEdge(index);
    }
    
    
    public double[]  getArray(){
        double[] array = new double[this.getSize()];
        for(int loop = 0; loop < this.getSize();loop++){
            array[loop] = this.getValue(loop);
        }
        return array;
    }
    
    public void set(int index, double value){
        if(index>=0&&index<getSize()){
            this.datavec.set(index, value);
        } else {
            System.out.println("[DataVector] --> warning : vector has size "
                    + getSize() + ". index="+index + " is out of bounds.");
        }
    }
    
    public void setValue(int index, double value){
        if(this.isFixedLength==true){
            if(index>=0&&index<this.datavec.size()){
                datavec.set(index, value);
            }
        } else {
            System.out.println("[DataVector] error : setValue works only for fixed length vectors.");
        }
    }
    
    public static List<DataVector> readFile(String format, String filename, int startPosition){
        TextFileReader reader = new TextFileReader();
        reader.open(filename);
        String[] tokens = format.split(":");
        List<DataVector> vectors = new ArrayList<DataVector>();
        
        for(int i = 0; i < tokens.length; i++){
            vectors.add(new DataVector());
        }
        
        while(reader.readNext()==true){
            double[] values = reader.getAsDoubleArray();
            if(values.length>=tokens.length+startPosition){
                for(int i = 0; i < tokens.length; i++){
                    vectors.get(i).add(values[i+startPosition]);
                }
            }
        }
        return vectors;
    }
    
    public static List<DataVector> readFile(String format, String filename){
        return DataVector.readFile(format, filename, 0);
    }
}
