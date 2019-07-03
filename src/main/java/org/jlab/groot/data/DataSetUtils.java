/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

/**
 *
 * @author gavalian
 */
public class DataSetUtils {
    public static double dataFraction(double value, double min, double max){
        if( (max-min)<1e-12) return 0.0;
        return (value-min)/(max-min);
    }
    
    public static double dataFraction(double value, double min, double max, boolean log){
        if(log==false) return DataSetUtils.dataFraction(value, min, max);
        double log_value = Math.log10(value);
        double log_min = Math.log10(min);
        double log_max = Math.log10(max);
        return DataSetUtils.dataFraction(log_value, log_min, log_max);
    }
}
