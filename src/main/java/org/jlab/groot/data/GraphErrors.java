/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

import org.jlab.groot.base.AttributeType;
import org.jlab.groot.base.Attributes;
import org.jlab.groot.base.DatasetAttributes;
import org.jlab.groot.ui.PaveText;

/**
 *
 * @author gavalian
 */
public class GraphErrors implements IDataSet {
    
    private final DataVector dataX = new DataVector();
    private final DataVector dataY = new DataVector();
    private final DataVector dataEX = new DataVector();
    private final DataVector dataEY = new DataVector();
    private String graphName = "graphErrors";
    private DatasetAttributes  graphAttr = new DatasetAttributes();
    
    
    public GraphErrors(){
        initAttributes();
    }
    
    public GraphErrors(String name, double[] x, double y[], double[] ex, double[] ey){
        setName(name);
        for(int i = 0; i < x.length; i++){
            this.addPoint(x[i], y[i], ex[i], ey[i]);
        }
    }
    
    public GraphErrors(String name, double[] x, double y[]){
        setName(name);
        for(int i = 0; i < x.length; i++){
            this.addPoint(x[i], y[i], 0.0,0.0);
        }
    }
    
    private void initAttributes(){
        /*graphAttr.add(AttributeType.LINE_COLOR,   1);
        graphAttr.add(AttributeType.LINE_WIDTH,   1);
        graphAttr.add(AttributeType.LINE_STYLE,   1);
        graphAttr.add(AttributeType.MARKER_COLOR, 1);
        graphAttr.add(AttributeType.MARKER_SIZE,  6);
        graphAttr.add(AttributeType.MARKER_STYLE, 1);
                */
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
}
