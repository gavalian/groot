/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.fitter;

import org.freehep.math.minuit.FCNBase;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Func1D;


/**
 *
 * @author gavalian
 */
public class FitterFunction implements FCNBase {
   
    private Func1D    function = null;
    private IDataSet  dataset  = null;
    private String    fitOptions = "E";
    private int       numberOfCalls = 0;
    
    public FitterFunction(Func1D func, IDataSet data){        
        dataset  = data;
        function = func;
        if(data instanceof H1F){
            H1F h = (H1F) data;
            h.setFunction(func);
        }
    }
    
    public FitterFunction(Func1D func, IDataSet data,String options){
        dataset    = data;
        function   = func;
        fitOptions = options; 
        if(data instanceof H1F){
            H1F h = (H1F) data;
            h.setFunction(func);
        }
    }
    
    public Func1D getFunction(){return function;}
    
    @Override
    public double valueOf(double[] pars) {
        double chi2 = 0.0;
        function.setParameters(pars);        
        chi2 = getChi2(pars,fitOptions);
        numberOfCalls++;
        this.function.setChiSquare(chi2);
        /*
        if(numberOfCalls%10==0){
            System.out.println("********************************************************");
            System.out.println( " Number of calls =  " + numberOfCalls + " CHI 2 " + chi2);
            function.show();
        }*/
        //function.show();
        //System.err.println("\n************ CHI 2 = " + chi2);
        return chi2;        
    }
    
    
    public double getChi2(double[] pars, String options){
        
        double chi2 = 0.0;
        int npoints = dataset.getDataSize(0);
        function.setParameters(pars);
        int ndf = 0;
        for(int np = 0; np < npoints; np++){
            double x = dataset.getDataX(np);
            double y = dataset.getDataY(np);
            if(x>=function.getMin()&&x<=function.getMax()){
                double yv = function.evaluate(x);
                double normalization = 1.0;
                if(options.contains("R")==true){
                    normalization = yv;
                }
                if(options.contains("N")==true){
                    normalization = y;
                }
                
                
                if(normalization>0.000000000001){
                    chi2 += (yv-y)*(yv-y)/normalization;
                    ndf++;
                }
            }
        }
        int npars = function.getNPars();
        this.function.setNDF(ndf-npars);
        return chi2;
    }
}
