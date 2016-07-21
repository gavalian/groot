/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.math;

import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;

/**
 *
 * @author gavalian
 */
public class RandomFunc {
    private GraphErrors dataFunc = null;
    private H1F  randomHIST = null;
    
    public RandomFunc(Func1D func){
        this.cumulative(func, 200);
        this.cummulativeHist(func, 200);
    }
    
    public RandomFunc(Func1D func, int resolution){
        this.cumulative(func, resolution);
        this.cummulativeHist(func, resolution);
    }
    
    private void cumulative(Func1D func, int nsamples){ 
        dataFunc = new GraphErrors();
        double min = func.getMin();
        double max = func.getMax();
        for(int i = 0; i < nsamples; i++){
            double x = min + i*(max-min)/nsamples;
            double y = func.evaluate(x);
            double xprev = 0; 
            double yprev = 0;
            if(i!=0){
                xprev = dataFunc.getDataX(i-1);
                yprev = dataFunc.getDataY(i-1);
            }
            dataFunc.addPoint(x, y + yprev, 0.0, 0.0);
            //System.out.println("adding point " + x + " y " + (y+yprev));
        }
        
        int    nBins    = dataFunc.getDataSize(0);
        double maxValue = dataFunc.getDataY(nBins-1);
        for(int i = 0; i < nBins; i++){
            double x = dataFunc.getDataX(i);
            double y = dataFunc.getDataY(i);
            dataFunc.setPoint(i, x, y/maxValue);
        }
    }
    
    public final void cummulativeHist(Func1D func, int nsamples){
        this.randomHIST = new H1F("RANDOM","",nsamples,func.getMin(),func.getMax());
        double integral = 0.0;
        for(int i = 0; i < nsamples; i++){
            double x = randomHIST.getAxis().getBinCenter(i);
            double y = func.evaluate(x);
            integral += y;
            randomHIST.setBinContent(i, integral);
        }
        randomHIST.divide(integral);
    }
    
    public GraphErrors getGraph(){
        return dataFunc;
    }
    
    public double random(){
        double number = Math.random();
        int bin  = dataFunc.getVectorY().findBin(number);
        bin = bin - 1;
        if(bin<0) bin =1;
        double xlow = dataFunc.getVectorX().getLowEdge(bin);
        double xhi  = dataFunc.getVectorX().getHighEdge(bin);
        return xlow + Math.random()*(xhi-xlow);
        //return 1;
    }

    public double randomh(){
        double number = Math.random();
        int bin  = 0;
        
        for(int i = 0; i < this.randomHIST.getAxis().getNBins(); i++){
            bin = i;
            if(this.randomHIST.getBinContent(bin)>number) break;
        }
        
        double axisX = this.randomHIST.getAxis().getBinCenter(bin);
        double width = this.randomHIST.getAxis().getBinWidth(bin);
        
        return axisX + Math.random()*width - width/2.0;
        //return 1;
    }
    
}
