/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.tree;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gavalian
 */
public class TreeSelector {
    
    Map<String,TreeCut>  treeCuts = new LinkedHashMap<String,TreeCut>();
    DynamicTree tree = new DynamicTree("Cuts");
    public DynamicTree getTree() {
		return tree;
	}

	public void setTree(DynamicTree tree) {
		this.tree = tree;
	}
	
    public TreeSelector(){
        
    }
    
    public void addCut(String name, String expression, List<String> branches){
        treeCuts.put(name, new TreeCut(name,expression,branches));
        tree.addObject(name);
    }
    
    public void addCut(TreeCut cut){
        this.treeCuts.put(cut.getName(), cut);
        tree.addObject(cut.getName());
    }
    
    public TreeCut getCut(String cut){
        return treeCuts.get(cut);
    }
    
    public boolean isValid(Tree tree){
        for(Map.Entry<String,TreeCut> entry : treeCuts.entrySet()){
            if(entry.getValue().isActive()==true){
                if(entry.getValue().isValid(tree)==false) return false;
            }
        }
        return true;
    }
    
    public Map<String,Integer>  getCuts(Tree tree){
        Map<String,Integer>  cuts = new LinkedHashMap<String,Integer>();
        for(Map.Entry<String,TreeCut> entry : treeCuts.entrySet()){
            if(entry.getValue().isActive()==true){
                if(entry.getValue().isValid(tree)==false){
                    cuts.put(entry.getKey(), 0);
                } else {
                    cuts.put(entry.getKey(), 1);
                }
            } else {
                cuts.put(entry.getKey(), 0);
            }
        }
        return cuts;
    }
    
    public Map<String,TreeCut>  getSelectorCuts(){
        return this.treeCuts;
    }
    
    public void reset(){
        treeCuts.clear();
    }
}
