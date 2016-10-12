/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.List;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import org.jlab.groot.math.FunctionFactory;

/**
 *
 * @author gavalian
 */
public class TreeExpression {
    
    String  cutName = "";
    String  cutExpression = "";
    List<String> expVariables = new ArrayList<String>();
    
    Expression expr = null;
    private boolean  isCutActive = true;
    
    Function funcVec3m = new Function("vec3m", 3) {
            @Override
            public double apply(double... args) {
                return Math.sqrt(args[0]*args[0] + args[1]*args[1] + args[2]*args[2]);
            }
    };
    
    Function funcVec3t = new Function("vec3t", 3) {
            @Override
            public double apply(double... args) {
                if(args[2]==0.0) return 0.0;
                return Math.sqrt(args[0]*args[0] + args[1]*args[1] + args[2]*args[2])/args[2];
            }
    };
    
    Function funcVec3p = new Function("vec3p", 3) {
            @Override
            public double apply(double... args) {
                if(args[2]==0.0) return 0.0;
                return Math.atan2(args[1],args[0]);
            }
    };
    
    Function funcVec3pt = new Function("vec3pt", 3) {
            @Override
            public double apply(double... args) {
                if(args[2]==0.0) return 0.0;
                return Math.sqrt(args[0]*args[0] + args[1]*args[1]);
            }
    };
    
    public TreeExpression(String name, String exp, List<String> branches){
        cutName       = name;
        cutExpression = exp;
        expVariables.clear();
        for(String br : branches){
            expVariables.add(br);
        }
        init();
    }
    
    final void init(){
        String[] variables = new String[expVariables.size()];
        for(int i=0; i < variables.length; i++) variables[i] = expVariables.get(i);        
        ExpressionBuilder builder = new ExpressionBuilder(cutExpression)
                .function(funcVec3m).function(funcVec3p).function(funcVec3t);
        builder.variables(variables);
        expr = builder.build();        
    }
    
    public double getValue(ITree tree){
        for(int i = 0; i < expVariables.size(); i++){
            expr.setVariable(expVariables.get(i), 
                    tree.getBranch(expVariables.get(i)).getValue().doubleValue());
        }
        double result = expr.evaluate();
        return result;
    }
}
