/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.math;

import org.jlab.groot.base.AttributeType;
import org.jlab.groot.base.Attributes;
import org.jlab.groot.base.DatasetAttributes;
import org.jlab.groot.base.GStyle;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.ui.PaveText;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author gavalian
 */
public class Func1D implements IDataSet {
    
    UserParameters  userPars = new UserParameters();
    String          funcName = "f1d";
    private int   defaultDrawingPoints = 250;
    private Dimension1D  functionRange = new Dimension1D();
    private DatasetAttributes   funcAttr      = new DatasetAttributes();
    private double      funcChi2       = 0.0;
    private int         funcNDF        = 0;
    private boolean     isFitValid  = false;
    private String      statBoxFormatString = "%.4f";
    private String      statBoxFormatError = "%.5f";
    
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
    	try {
			this.funcAttr = GStyle.getFunctionAttributes().clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    public final void setFitValid(boolean flag){
        this.isFitValid = flag;
    }
    
    public final boolean isFitValid(){ return this.isFitValid;}
    
    public final void setRange(double min, double max){
        this.functionRange.setMinMax(min, max);
    }
    
    public void setStatBoxFormat(String format){
        this.statBoxFormatString = format;
    }
    
    public void setStatBoxErrorFormat(String format){
        this.statBoxFormatError = format;
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
    
    public void setParStep(int par, double step){
        this.parameter(par).setStep(step);
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
    
    public String getExpression(){
        return "";
    }
    
    public double getIntegral(){
        int nsamples = 400;
        double step = this.getRange().getLength()/nsamples;
        double integral = 0.0;
        for(int i = 0; i < nsamples; i++){
            double x = getRange().getMin() + i * step;
            integral += evaluate(x)*step;
        }
        return integral;
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
    public DatasetAttributes getAttributes() {
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
        str.append(String.format("FUNCTION (%s) RANGE %9.4f %9.4f (exp:%s)\n", 
                getName(),getMin(),getMax(),getExpression()));
        str.append("-----\n");
        str.append(userPars.toString());
        return str.toString();
    }
    
    public int getLineColor(){
        return funcAttr.getLineColor();
    }
    
    public int getLineWidth(){
        return this.funcAttr.getLineWidth();
    }
    
    public int getLineStyle(){
        return this.funcAttr.getLineStyle();
    }
    
    public void setLineColor(int color){
        this.funcAttr.setLineColor(color);
    }
    public void setLineWidth(int width){
        this.funcAttr.setLineWidth(width);
    }
    public void setLineStyle(int style){
        this.funcAttr.setLineStyle(style);
    }

    public double getChiSquare(){
        return this.funcChi2;
    }
    
    public int getNDF(){
        return this.funcNDF;
    }
    
    public void setChiSquare(double chi2){
        this.funcChi2 = chi2;
    }
    
    public void setNDF(int ndf){
        this.funcNDF = ndf;
    }
    
    @Override
    public PaveText getStatBox() {
        PaveText  stat = new PaveText(2);
        stat.addText("Name:",this.getName());
        for(UserParameter par : userPars.getParameters()){
            stat.addText(par.name(),String.format(this.statBoxFormatString+"#pm"+this.statBoxFormatError, 
                    par.value(),par.error()));
        }
        stat.addText("#chi^2/ndf",String.format(this.statBoxFormatString, this.getChiSquare()/(double)this.getNDF()));
        stat.addText("#chi^2",String.format(this.statBoxFormatString, this.getChiSquare()));
        stat.addText("ndf",String.format(this.statBoxFormatString, (double)this.getNDF()));
        return stat;
    }
    
    public void setOptStat(int optStatString) {
		this.getAttributes().setOptStat(""+optStatString);
	}
    
	public void setOptStat(String optStatString) {
		this.getAttributes().setOptStat(optStatString);
	}
	public String getOptStat() {
		return this.getAttributes().getOptStat();
	}
	
	public void estimateParameters(){};
	public double[] getParameterEstimate(){return null;}

    @Override
    public void reset() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void save(String filename) {
        String extension = filename.substring(filename.lastIndexOf("."));
        filename = filename.substring(0, filename.lastIndexOf("."));

        try {
            FileWriter parsfile = new FileWriter(filename + "_pars" + extension);
            parsfile.write("#F1D: " + this.funcName + ", nsamples: " + getDataSize(0) +"\n");
            parsfile.write(this.toString());
            parsfile.close();

            FileWriter file = new FileWriter(filename + extension);
            file.write("#F1D: " + this.funcName + " nsamples: " + getDataSize(0) +"\n");
            file.write("#x,y\n");
            for(int i = 0; i < getDataSize(0); i++) {
                file.write(String.format("%f,%f",
                        getDataX(i), getDataY(i)));
                file.write('\n');
            }
            file.close();

        } catch (IOException e) {
        }
    }

}
