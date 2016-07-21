/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.math;

/**
 *
 * @author gavalian
 */
public class UserParameter {
    
    String pName;
    double pValue;
    double pMin;
    double pMax;
    double pError;
    double pStep = 0.0001;
    
    boolean isRangeLimited = false;
    
    public UserParameter()
    {
        pName  = "p0";
        pValue = 0.;
        //pMin   = -Double.MAX_VALUE/4000.0;
        //pMax   = Double.MAX_VALUE/4000.0;
        pMin = -1.0e10;
        pMax =  1.0e10;
        pError = 0.0;
    }
    
    public UserParameter(String name, double value)
    {
        pName  = name;
        pValue = value;
        pMin   = -1e10;
        pMax   = 1e10;
        pError = 0.0;
    }
    
    public UserParameter(String name, double value, double min, double max)
    {
        pName  = name;
        pValue = value;
        pMin   = min;
        pMax   = max;
        pError = 0.0;
    }
    
    public void setStep(double step){ this.pStep = step;}
    public void setError(double error){ pError = error;}
    public void setName(String name) { pName = name;}
    public void setLimits(double min, double max){pMin = min; pMax=max; isRangeLimited = true;}
    public boolean isLimited(){ return this.isRangeLimited;}
    
    public void setFree(){
        isRangeLimited = false;
        pMin   = -1e10;
        pMax   = 1e10;
    }
    
    public void multLimits(double factor)
    {
        double range  = Math.abs(pMax-pMin);
        double median = pMin + Math.abs(pMax-pMin)/2.;
        double pMin = median - range*factor;
        double pMax = median + range*factor;
    }
    
    public void set(double value, double min, double max){
        this.setValue(value);
        this.setLimits(min, max);
    }
    
    public void setValue(double value) { pValue = value; };
    public double value(){ return pValue;}
    public String name() { return pName;}
    public double min()  { return pMin;}
    public double max()  { return pMax;}
    public double error() { return pError;}
    
    public void setRandom()
    {
        pValue = pMin + Math.random()*Math.abs(pMax-pMin);
    }
    
    public double getStep(){
        return this.pStep;
    }
    
    public double getRandom()
    {
        return pMin + Math.random()*Math.abs(pMax-pMin);
    }

    public UserParameter getCopy(){
        UserParameter par = new UserParameter(name(),value());
        if(this.isRangeLimited==true) par.setLimits(pMin, pMax);
        return par;
    }
    
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        str.append(String.format("%18s : %18e %18e %18e %18e", this.name(),
                this.value(),this.error(),this.min(),this.max()));
        return str.toString();
    }
}
