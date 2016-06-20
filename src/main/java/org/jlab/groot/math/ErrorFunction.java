/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.math;

/**
 *
 * @author gavalian
 */
public class ErrorFunction {
     // fractional error in math formula less than 1.2 * 10 ^ -7.
    // although subject to catastrophic cancellation when z in very close to 0
    // from Chebyshev fitting formula for erf(z) from Numerical Recipes, 6.2
    public static double erf(double z) {
        double t = 1.0 / (1.0 + 0.5 * Math.abs(z));

        // use Horner's method
        double ans = 1 - t * Math.exp( -z*z   -   1.26551223 +
                                            t * ( 1.00002368 +
                                            t * ( 0.37409196 + 
                                            t * ( 0.09678418 + 
                                            t * (-0.18628806 + 
                                            t * ( 0.27886807 + 
                                            t * (-1.13520398 + 
                                            t * ( 1.48851587 + 
                                            t * (-0.82215223 + 
                                            t * ( 0.17087277))))))))));
        if (z >= 0) return  ans;
        else        return -ans;
    }

    public static double erfc(double z){
        return 1.0-ErrorFunction.erf(z);
    }
    
    public static double erf(double p0, double p1, double p2, double p3,double x){
        return p0 + p1*ErrorFunction.erfc((x-p2)/(p3*Math.sqrt(2.0)));
    }
    // fractional error less than x.xx * 10 ^ -4.
    // Algorithm 26.2.17 in Abromowitz and Stegun, Handbook of Mathematical.
    public static double erf2(double z) {
        double t = 1.0 / (1.0 + 0.47047 * Math.abs(z));
        double poly = t * (0.3480242 + t * (-0.0958798 + t * (0.7478556)));
        double ans = 1.0 - poly * Math.exp(-z*z);
        if (z >= 0) return  ans;
        else        return -ans;
    }

    // cumulative normal distribution
    // See Gaussia.java for a better way to compute Phi(z)
    public static double Phi(double z) {
        return 0.5 * (1.0 + erf(z / (Math.sqrt(2.0))));
    }



   /***************************************************************************
    *  Test client
    ***************************************************************************/
    public static void main(String[] args) { 
        double x = Double.parseDouble(args[0]);

        System.out.println("erf(" + x + ")  = " + ErrorFunction.erf(x));
        System.out.println("erf2(" + x + ") = " + ErrorFunction.erf2(x));
        System.out.println("Phi(" + x + ")  = " + ErrorFunction.Phi(x));
        System.out.println();
    }
}
