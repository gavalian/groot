/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.fitter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.freehep.math.minuit.FunctionMinimum;
import org.freehep.math.minuit.MnMigrad;
import org.freehep.math.minuit.MnScan;
import org.freehep.math.minuit.MnUserParameters;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.math.Func1D;
import org.jlab.groot.math.Func2D;
import org.jlab.groot.math.UserParameter;

/**
 *
 * @author gavalian
 */
public class DataFitter2D {
    public static Boolean FITPRINTOUT = true;
    
    public DataFitter2D(){
        
    }
    
    public static void fit(Func2D func, IDataSet  data, String options){
        
        ByteArrayOutputStream pipeOut = new ByteArrayOutputStream();
        PrintStream  outStream = System.out;
        PrintStream  errStream = System.err;
        
        if(options.contains("Q")==true){
            DataFitter.FITPRINTOUT = false;
        } else {
            DataFitter.FITPRINTOUT = true;
        }
        
        if(DataFitter.FITPRINTOUT==false){
            PrintStream pipeStream = new PrintStream(pipeOut);
            System.setOut(pipeStream);
            System.setErr(pipeStream);
        }
        try{
	        FitterFunction2D funcFitter = new FitterFunction2D(func,
	                data,options);
	        
	        int npars = funcFitter.getFunction().getNPars();
	        
	        MnUserParameters upar = new MnUserParameters();
	        for(int loop = 0; loop < npars; loop++){
	            UserParameter par = funcFitter.getFunction().parameter(loop);
	            upar.add(par.name(),par.value(),0.0001);
	            if(par.getStep()<0.0000000001){
	                upar.fix(par.name());
	            }
	            if(par.min()>-1e9&&par.max()<1e9){
	                upar.setLimits(par.name(), par.min(), par.max());
	            }
	        }
	        
	        
	        MnScan  scanner = new MnScan(funcFitter,upar);
	        FunctionMinimum scanmin = scanner.minimize(); 
	        /*
	        System.err.println("******************");
	        System.err.println("*   SCAN RESULTS  *");
	        System.err.println("******************");
	        System.out.println("minimum : " + scanmin);
	        System.out.println("pars    : " + upar);
	        System.out.println(upar);
	        System.err.println("*******************************************");
	        */
	        MnMigrad migrad = new MnMigrad(funcFitter, upar);
	        
	        FunctionMinimum min = migrad.minimize();
	        
	        MnUserParameters userpar = min.userParameters();
	        
	        for(int loop = 0; loop < npars; loop++){
	            UserParameter par = funcFitter.getFunction().parameter(loop);
	            par.setValue(userpar.value(par.name()));
	            par.setError(userpar.error(par.name()));
	        }
	        
	        if(options.contains("V")==true){
	            System.out.println(upar);
	            System.err.println("******************");
	            System.err.println("*   FIT RESULTS  *");
	            System.err.println("******************");
	            
	            System.err.println(min);
	        }
	        
	        //System.out.println(funcFitter.getBenchmarkString());
        }catch(Exception e){
	       e.printStackTrace();
	       if(DataFitter.FITPRINTOUT==false){
	            System.setOut(outStream);
	            System.setErr(errStream);
	        }
        }
        if(DataFitter.FITPRINTOUT==false){
            System.setOut(outStream);
            System.setErr(errStream);
        }
    }
}
