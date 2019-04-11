/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.math;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

/**
 *
 * @author gavalian
 */
public class F1D extends Func1D {

    Expression expr = null;
    private String expressionString = "";
    private List<String> expressionVariables = new ArrayList<String>();
    
    Function funcLogb = new Function("logb", 2) {
            @Override
            public double apply(double... args) {
                return Math.log(args[0]) / Math.log(args[1]);
            }
    };
    
    Function funcGaus = new Function("gaus", 3) {
            @Override
            public double apply(double... args) {
                return FunctionFactory.gauss(args[0],args[1],args[2]);
            }
    };
    
    Function funcLandau = new Function("landau", 3) {
            @Override
            public double apply(double... args) {
                return FunctionFactory.landau(args[0],args[1],args[2]);
            }
    };
    
    Function funcExp = new Function("exp", 1) {
            @Override
            public double apply(double... args) {
                return Math.exp(args[0]);
            }
    };
    
    Function funcErrf = new Function("erf", 3) {
            @Override
            public double apply(double... args) {
                return ErrorFunction.erf(0.0,1.0,args[1],args[2],args[0]);
            }
    };
    
    public F1D(String name){
        super(name);
    }
    
    public F1D(String name, String expression, double min, double max){
        super(name,min,max);
        parse(expression);
    }
    
    public final void parse(String str){
        expressionString = str;
        //System.out.println("---> starting parsing....");
        int pos_o = str.indexOf("[");
        Set<String>  params = new LinkedHashSet<String>();
        
        //System.out.println(" position = " + pos_o);
        while(pos_o>=0){
            int pos_c = str.indexOf("]", pos_o);
            //System.out.println("brackets from " + pos_o 
            //        + " to " + pos_c);
            String param = str.substring(pos_o+1, pos_c);
            params.add(param);
            pos_o = str.indexOf("[", pos_c);     
        }
        //System.out.println(params);
        String funcString = str;
        //funcString.replaceAll("x", "p");
        String newString = funcString;
        this.expressionVariables.clear();
        for(String par : params){
            String  item = "[" + par + "]";
            expressionVariables.add(par);
            newString = newString.replace(item,par);
        }
        //System.out.println(newString);
        
        int npars = expressionVariables.size();
        String[] expvar = new String[npars+1];
        expvar[0] = "x";
                
        for(int i = 0; i < npars; i++){
            expvar[i+1] = expressionVariables.get(i);
            //System.out.println("adding ---> " + expressionVariables.get(i));
            this.addParameter(expressionVariables.get(i));
        }
        ExpressionBuilder builder = new ExpressionBuilder(newString)
                .function(funcGaus).function(funcLandau).function(funcLogb)
                .function(funcErrf).function(funcExp);
        
        builder.variables(expvar);
        expr = builder.build();
    }
    
    
    @Override
    public String getExpression(){
        return this.expressionString;
    }
    
    @Override
    public double evaluate(double x){
        expr.setVariable("x", x);
        for(int i = 0; i < this.getNPars(); i++){
            UserParameter par = this.parameter(i);
            expr.setVariable(par.name(),par.value());
        }
        return expr.evaluate();
    }
    
    public static void main(String[] args){
        System.out.println("---> starting program");
        F1D func  = new F1D("f1d","[amp]*gaus(x,[mean],[sigma])",0.1,0.8);
        F1D func2 = new F1D("f1d","[a]*gaus(x,[m],[s])+[h]*exp([f])",0.1,0.8);
        //func.parse("[amp]*gaus(x,[mean],[sigma])");
        F1D func3 = new F1D("funcExp","[a]*exp(-[b]*x)",0.0,2.0);
        func3.setParameter(0, 0.64);
        func3.setParameter(1, 1.0);
        
        func2.setParameter(0, 10);
        func2.setParameter(1, 0.5);
        func2.setParameter(2, 0.2);
        func2.setParameter(3, 10);
        func2.setParameter(4, -2.0);
        System.out.println(func2.toString());
        for(double x = func3.getMin(); x < func3.getMax(); x+= 0.05){
            double value = func3.evaluate(x);
            System.out.println("x = " + x + "  value = " + value);
            //func.show();
        }
        
    }
}
