package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.List;

import org.jlab.groot.data.IDataSet;

public class DatasetDescriptor {
    
    String name = "somename";
    IDataSet       descDataset = null;
    List<String>   descCutList = new ArrayList<String>();
    private int nDim;
    private int nBinsX;
    private double xMin;
    private double xMax;
    private int nBinsY;
    private double yMin;
    private double yMax;
    public DatasetDescriptor(String name, int nbins, double min, double max){
    	this.nDim = 1;
    	this.nBinsX = nbins;
    	this.xMin = min;
    	this.xMax = max;
    }
    
    public int getNBinsX(){
    	return this.nBinsX;
    }
    public double getMinX(){
    	return this.xMin;
    }
    public double getMaxX(){
    	return this.xMax;
    }
    
    public int getNBinsY(){
    	return this.nBinsX;
    }
    public double getMinY(){
    	return this.xMin;
    }
    public double getMaxY(){
    	return this.xMax;
    }
    
    public void addCut(String name){
        descCutList.add(name);
    }
    
    public List<String>  getCuts(){
        return this.descCutList;
    }
    
    public IDataSet getDataSet(){
        return this.descDataset;
    }

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}