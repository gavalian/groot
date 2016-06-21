/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.math;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavalian
 */
public class UserParameters {
    
    List<UserParameter>  parameters = new ArrayList<UserParameter>();
    
    public UserParameters(){
        
    }
    
    public UserParameter  getParameter(int index){
        return parameters.get(index);
    }
    
    public void setParameters(double[] pars){
        if(pars.length!=parameters.size()){
            System.out.println("UserParameters : error -> wrong number of parameters");
        }
        for(int i = 0; i < pars.length; i++){
            parameters.get(i).setValue(pars[i]);
        }
    }
    
    public List<UserParameter>  getParameters(){
        return this.parameters;
    }
    
    public void reset(){
        //this.parameters.clear();
    }
    
    public void clear(){
        this.parameters.clear();
    }
    
    public void getCopy(UserParameters par){
        this.parameters.clear();
        for(int i = 0; i < par.getParameters().size(); i++){
            this.parameters.add(par.getParameter(i).getCopy());
        }        
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("PARAMETER SET N PARAMS = %d\n**\n", parameters.size()));
        for(int i = 0; i < parameters.size(); i++){
            str.append(parameters.get(i).toString());
            str.append("\n");
        }
        return str.toString();
    }
}
