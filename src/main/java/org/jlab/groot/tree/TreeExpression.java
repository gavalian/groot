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
        ExpressionBuilder builder = new ExpressionBuilder(cutExpression);                
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
