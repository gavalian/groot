/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gavalian
 */
public class Tree implements ITree {
    
    private String treeName = "tree";
    
    private  Map<String,Branch>  treeBranches    = new LinkedHashMap<String,Branch>();
    private  TreeSelector        defaultSelector = new TreeSelector();
    
    public Tree(String name){
        this.treeName = name;
    }
    /**
     * returns the name of the tree
     * @return 
     */
    public String getName(){return this.treeName;}
    
    public void addBranch(Branch br){
        treeBranches.put(br.getName(), br);
    }
    
    public void addBranch(String name, String desc, String unit){
        addBranch(new Branch(name,desc,unit));
    }

    public List<String> getListOfBranches() {
        List<String> listOfBranches = new ArrayList<String>();
        for(Map.Entry<String,Branch> entry : treeBranches.entrySet()){
            listOfBranches.add(entry.getKey());
        }
        return listOfBranches;
    }

    public Branch getBranch(String name) {
        return this.treeBranches.get(name);
    }

    public void reset() {
        
    }

    public boolean readNext() {
        return false;
    }    
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<String,Branch> entry : treeBranches.entrySet()){
            str.append(entry.getValue().toString());
            str.append("\n");
        }
        return str.toString();
    }
    
    public void show(){
        System.out.println(toString());
    }

    public void configure() {
        System.out.println("[Tree] --> generic tree class does not require configuring.");
    }
    /**
     * returns default tree selector. cuts can be added to internal selector
     * @return 
     */
    public TreeSelector  getSelector(){
        return this.defaultSelector;
    }
    /**
     * returns a list of values from the tree branch for all rows
     * that pass the cuts described in default tree selector
     * @param branch
     * @return 
     */
    public List<Double>  getVector(String branch){
        return getVector(branch,defaultSelector);
    }
    /**
     * returns a list of values from the tree branch for rows that pass
     * the cuts described by selector.
     * @param branch
     * @param selector
     * @return 
     */
    public List<Double>  getVector(String branch, TreeSelector selector){
        this.reset();
        List<Double> vector = new ArrayList<Double>();
        while(readNext()==true){
            if(selector.isValid(this)==true){
                vector.add(getBranch(branch).getValue().doubleValue());
            }
        }
        return vector;
    }
}
