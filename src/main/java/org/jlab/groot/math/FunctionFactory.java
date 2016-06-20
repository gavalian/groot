/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.math;

import org.jlab.groot.data.H1F;

/**
 *
 * @author gavalian
 */
public class FunctionFactory {
    
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
}
