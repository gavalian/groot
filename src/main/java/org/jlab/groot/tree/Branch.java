/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.tree;

/**
 *
 * @author gavalian
 */
public class Branch {
    
    private String branchName = "branch";
    private String branchDesc = "generic branch";
    private String branchUnit = "cm";
    private Number branchValue = (Double) 0.0;
    
    public Branch(String name, String desc, String unit){
        setName(name);
        setDescription(desc);
        setUnit(unit);
    }
    
    public Branch(String name){
        setName(name);
    }
    
    public String getName(){ return branchName;}
    public String getDescription(){return branchDesc;}
    public String getUnit(){return branchUnit;}
    
    public final void setName(String name){
        this.branchName = name;
    }
    
    public final void setDescription(String desc){
        this.branchDesc = desc;
    }
    
    public final void setUnit(String unit){
        this.branchUnit = unit;
    }
    
    public void setValue(Number value){
        this.branchValue = value;
    }
    public Number getValue(){
        return this.branchValue;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(String.format("* %-20s ", getName()));
        str.append(String.format("* %-40s ", getDescription()));
        str.append(String.format("* %10s *", getUnit()));
        //str.append();
        return str.toString();
    }
}
