/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.math;

import java.util.LinkedHashMap;
import java.util.Map;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;

/**
 *
 * @author gavalian
 */
public class FunctionFactory {
    
    public static Map<String,Func1D>  funcionDesk = new LinkedHashMap<String,Func1D>();
    
    public static void registerFunction(Func1D func){
        if(FunctionFactory.funcionDesk.containsKey(func.getName())==true){
            System.out.println("[FunctionFactory] warning : ---> replacing function with name = "
            + func.getName());
        }
        FunctionFactory.funcionDesk.put(func.getName(), func);
    }
    
    
    public static double  gauss(double x, double mean, double sigma){
        return Math.exp(-(x-mean)*(x-mean)/(2.0*sigma*sigma));
    }
    
    public static double getRandMinMax(double min, double max){
        return min + (max-min)*Math.random();
    }
    
    public static double landau(double x, double mean, double sigma){
        double lambda = (x-mean)/sigma;
        return Math.exp(-0.5*(lambda+Math.exp(-lambda)));
    }
    /**
     * generates a histogram with gaussian function
     * @param bins
     * @param min
     * @param max
     * @param stat
     * @param mean
     * @param sigma
     * @return 
     */
    public static H1F  randomGausian(int bins, double min, double max, 
            int stat, double mean, double sigma){
        H1F h = new H1F("RandomGaus",bins,min,max);

        for(int i = 0; i < stat; i++){
            double x = FunctionFactory.getRandMinMax(min, max);
            double w = FunctionFactory.gauss(x, mean, sigma);
            h.fill(x, w);
        }
        return h;
    } 
    
    public static H2F  randomGausian2D(int bins, double min, double max, 
            int stat, double mean, double sigma){
        H2F h = new H2F("RandomGaus",bins,min,max, bins, min,max);
        
        for(int i = 0; i < stat; i++){
            double x = FunctionFactory.getRandMinMax(min, max);
            double y = FunctionFactory.getRandMinMax(min, max);
            double xw = FunctionFactory.gauss(x, mean, sigma);
            double yw = FunctionFactory.gauss(y, mean, sigma);
            h.fill(x,y, xw+yw);
        }
        return h;
    } 
    
    
    public static H1F  createDebugH1F(int bins){
        H1F h1 = new H1F("h1","Debug Histogram",bins,0.5, ((float) bins) + 0.5);
        for(int i =0 ; i < bins; i++){
            h1.setBinContent(i, i+1);
        }
        return h1;
    }
    
    public static H1F     createH1F(int type, int bins, int samples){
        Func1D  func = FunctionFactory.createFunction(type);
        H1F h = new H1F("RandomH1F",bins,0.1,5.0);
        RandomFunc  random = new RandomFunc(func);
        for(int i = 0; i < samples; i++){
            double r = random.random();
            h.fill(r);
        }
        return h;
    }
    
    public static Func1D  createFunction(int type){
        if(type == 0){
            F1D func3 = new F1D("func3","[p0]+[p1]*x+[amp]*gaus(x,[mean],[sigma])",0.0,5.4);
            func3.setParameters(new double[]{15,5,120,2.4,0.45});
            return func3;
        }
        
        if(type == 1){
            F1D func3 = new F1D("func3","[p0]+[p1]*x+[amp]*gaus(x,[mean],[sigma])+[amp2]*gaus(x,[mean2],[sigma2])",0.0,5.4);
            func3.setParameters(new double[]{15,5,120,1.8,0.25, 80,3.2,0.45});
            return func3;
        }
        return null;
    }
}
