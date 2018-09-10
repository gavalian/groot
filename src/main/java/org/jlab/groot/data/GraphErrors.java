/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

import org.jlab.groot.base.DatasetAttributes;
import org.jlab.groot.base.GStyle;
import org.jlab.groot.io.TextFileReader;
import org.jlab.groot.math.Func1D;
import org.jlab.groot.ui.PaveText;

/**
 *
 * @author gavalian
 */
public class GraphErrors implements IDataSet {
    
    public static final String[] MARKERNAME = {"Circle","Square","Triangle","Inverted Triangle"};
    
    private final DataVector dataX = new DataVector();
    private final DataVector dataY = new DataVector();
    private final DataVector dataEX = new DataVector();
    private final DataVector dataEY = new DataVector();
    
    private String graphName = "graphErrors";
    private DatasetAttributes  graphAttr = null;
    
    private Func1D  fitFunction = null;
    
    public GraphErrors(){
        initAttributes();
    }
    
    public GraphErrors(String name, DataVector grX, DataVector grY){
        setName(name);
        for(int i = 0; i < grX.getSize(); i++){
            this.addPoint(grX.getValue(i), grY.getValue(i), 0.0, 0.0);
        }
        initAttributes();
    }
    
    public GraphErrors(String name, DataVector grX, DataVector grY, DataVector erX, DataVector erY){
        setName(name);
        for(int i = 0; i < grX.getSize(); i++){
            double errX = 0.0;
            if(erX!=null) errX = erX.getValue(i);
            this.addPoint(grX.getValue(i), grY.getValue(i), errX , erY.getValue(i));
        }
        initAttributes();
    }
    public GraphErrors(String name,  DataVector grY){
        setName(name);
        for(int i = 0; i < grY.getSize(); i++){
            this.addPoint((double) (i+1), grY.getValue(i), 0.0, 0.0);
        }
        initAttributes();
    }
    
    public GraphErrors(String name, double[] x, double y[], double[] ex, double[] ey){
        setName(name);
        for(int i = 0; i < x.length; i++){
            this.addPoint(x[i], y[i], ex[i], ey[i]);
        }
        initAttributes();
    }
    
    public GraphErrors(String name, double[] x, double y[]){
        setName(name);
        for(int i = 0; i < x.length; i++){
            this.addPoint(x[i], y[i], 0.0,0.0);
        }
        initAttributes();
    }
    
    public GraphErrors(String name) {
        graphName = name;
        initAttributes();
    }


	private void initAttributes(){
    	try {
			graphAttr = GStyle.getGraphErrorsAttributes().clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
                }
        }
    
        public Func1D getFunction(){return fitFunction;}
        
        public void setFunction(Func1D func){
            fitFunction = func;
            func.getAttributes().setOptStat(this.getAttributes().getOptStat());
        }
        
    public final void addPoint(double x, double y, double ex, double ey){
        dataX.add(x);
        dataY.add(y);
        dataEX.add(ex);
        dataEY.add(ey);
    }
    
    public void setPoint(int point, double x, double y){
        dataX.set(point, x);
        dataY.set(point, y);
    }
    
    public void setError(int point, double ex, double ey){
        dataEX.set(point,ex);
        dataEY.set(point,ey);
    }

    @Override
    public void setName(String name) {
        graphName = name;
    }

    @Override
    public String getName() {
        return graphName;
    }

    @Override
    public int getDataSize(int axis) {
        return dataX.getSize();
    }

    @Override
    public DatasetAttributes getAttributes() {
        return graphAttr;
    }

    @Override
    public double getDataX(int bin) {
        return dataX.getValue(bin);
    }

    @Override
    public double getDataY(int bin) {
        return dataY.getValue(bin);
    }

    @Override
    public double getDataEX(int bin) {
        return dataEX.getValue(bin);
    }

    @Override
    public double getDataEY(int bin) {
        return dataEY.getValue(bin);
    }

    @Override
    public double getData(int xbin, int ybin) {
        return 0.0;
    }

    @Override
    public PaveText getStatBox() {
        return new PaveText(2);
    }
    
    public DataVector getVectorX(){
        return this.dataX;
    }
    
    public DataVector getVectorY(){
        return this.dataY;
    }
    
    public void setMarkerSize(int size){ 
    	this.graphAttr.setMarkerSize(size);
    }
    
    public int getMarkerSize(){ 
    	return this.graphAttr.getMarkerSize();
    }   
    
    public void setMarkerStyle(int style){
    	this.graphAttr.setMarkerStyle(style);
    }

    public int getMarkerStyle(){
    	return this.graphAttr.getMarkerStyle();
    }
    
    public int getMarkerColor(){
    	return this.graphAttr.getMarkerColor();
    }
    
    public void setMarkerColor(int color){
    	this.graphAttr.setMarkerColor(color);
    }
    
    public void setLineColor(int color) {
        this.graphAttr.setLineColor(color);
	}
	
	public int getLineColor() {
        return this.graphAttr.getLineColor();
	}
	
	public void setLineThickness(int thickness){
        this.graphAttr.setLineWidth(thickness);
	}
	
	public int getLineThickness(){
        return this.graphAttr.getLineWidth();
	}
    
    public String getTitle(){
        return this.graphAttr.getTitle();
    }
   
    public String getTitleX() {
        return this.graphAttr.getTitleX();
    }
    
    public String getTitleY() {
        return this.graphAttr.getTitleY();
    }
    
    public void setTitle(String title){
         this.graphAttr.setTitle(title);
    }
   
    public void setTitleX(String title) {
         this.graphAttr.setTitleX(title);
    }
    
    public void setTitleY(String title) {
         this.graphAttr.setTitleY(title);
    }
    
    
    public void copy(GraphErrors gr){
        this.dataEX.clear();
        this.dataEY.clear();
        this.dataX.clear();
        this.dataY.clear();
        for(int i = 0; i < gr.getDataSize(0); i++){
            this.addPoint( gr.getDataX(i),gr.getDataY(i),
                    gr.getDataEX(i),gr.getDataEY(i)
            );
        }
    }

    public void readFile(String filename){
        TextFileReader reader = new TextFileReader();
        reader.openFile(filename);
        this.reset();
        while(reader.readNext()==true){
            if(reader.getDataSize()==2){
                int[] index = new int[]{0,1};
                double[] points = reader.getAsDouble(index);
                this.addPoint(points[0],points[1],0.0,0.0);
            }
            if(reader.getDataSize()==3){
                int[] index = new int[]{0,1,2};
                double[] points = reader.getAsDouble(index);
                this.addPoint(points[0],points[1],0.0,points[2]);
            }
            if(reader.getDataSize()>3){
                int[] index = new int[]{0,1,2,3};
                double[] points = reader.getAsDouble(index);
                this.addPoint(points[0],points[1],points[2],points[3]);
            }
        }
    }

    @Override
    public void reset() {
        this.dataX.clear();
        this.dataY.clear();
        this.dataEX.clear();
        this.dataEY.clear();
    }

    @Override
    public double getMin() {
        return dataY.getMin();
    }

    @Override
    public double getMax() {
        return dataY.getMax();
    }
}
