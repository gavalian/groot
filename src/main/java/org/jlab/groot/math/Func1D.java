/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.math;

import org.jlab.groot.base.AttributeType;
import org.jlab.groot.base.Attributes;
import org.jlab.groot.data.IDataSet;

/**
 *
 * @author gavalian
 */
public class Func1D implements IDataSet {
    
    UserParameters  userPars = new UserParameters();
    String          funcName = "f1d";
    private int   defaultDrawingPoints = 250;
    private Dimension1D  functionRange = new Dimension1D();
    private Attributes   funcAttr      = new Attributes();
        
    
    public Func1D(String name){
        this.funcName = name;
        initAttributes();
        //FunctionFactory.registerFunction(this);
    }
    
    public Func1D(String name, double min, double max){
        this.funcName = name;
        //FunctionFactory.registerFunction(this);
        this.setRange(min, max);
        initAttributes();
    }
    
    private void initAttributes(){
        funcAttr.add(AttributeType.LINE_COLOR, 2);
        funcAttr.add(AttributeType.LINE_WIDTH, 2);
        funcAttr.add(AttributeType.LINE_STYLE, 1);
    }
    
    public final void setRange(double min, double max){
        this.functionRange.setMinMax(min, max);
    }
    
    public void addParameter(String name){
        this.userPars.getParameters().add(new UserParameter(name,0.0));
    }
    
    public void addParameter(UserParameter par){
        this.userPars.getParameters().add(par);
    }
    
    @Override
    public  void setName(String name){ this.funcName = name;}
    
    @Override
    public String getName(){return this.funcName;}
    
    public void setParameters(double[] values){
        this.userPars.setParameters(values);
    }
    
    public void setParameter(int i, double value){
        userPars.getParameter(i).setValue(value);
    }
    
    public UserParameter  parameter(int index){
        return this.userPars.getParameter(index);
    }
    public int  getNPars(){
        return userPars.getParameters().size();
    }
    
    public void setParLimits(int par, double min, double max){
        userPars.getParameters().get(par).setLimits(min, max);
    }
    
    public double getParameter(int index){ 
        return userPars.getParameter(index).value();
    }
    
    public double evaluate(double x){
        return 1;
    }
    
    public Dimension1D getRange(){
        return this.functionRange;
    }
    
    public double getMin(){
        return this.functionRange.getMin();
    }
    
    public double getMax(){
        return this.functionRange.getMax();
    }
    
    public void show(){
        System.out.println(userPars.toString());
    }

    @Override
    public int getDataSize(int axis) {
        return this.defaultDrawingPoints;
    }

    @Override
    public Attributes getAttributes() {
        return this.funcAttr;
    }

    @Override
    public double getDataX(int bin) {
        double length   = functionRange.getLength();
        double fraction = ((double) bin )/this.defaultDrawingPoints;
        return functionRange.getMin() + fraction*length;
    }

    @Override
    public double getDataY(int bin) {
        double x = getDataX(bin);
        return evaluate(x);
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
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("FUNCTION (%s) RANGE %9.4f %9.4f\n", getName(),getMin(),getMax()));
        str.append("-----\n");
        str.append(userPars.toString());
        return str.toString();
    }
    
    public int getLineColor(){
        return this.funcAttr.get(AttributeType.LINE_COLOR);
    }
    
    public int getLineWidth(){
        return this.funcAttr.get(AttributeType.LINE_WIDTH);
    }
    
    public int getLineStyle(){
        return this.funcAttr.get(AttributeType.LINE_STYLE);
    }
    
    public void setLineColor(int color){
        this.funcAttr.add(AttributeType.LINE_COLOR, color);
    }
    public void setLineWidth(int width){
        this.funcAttr.add(AttributeType.LINE_WIDTH, width);
    }
    public void setLineStyle(int style){
        this.funcAttr.add(AttributeType.LINE_STYLE, style);
    }

}
