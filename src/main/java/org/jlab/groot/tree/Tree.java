/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author gavalian
 */
public class Tree implements ITree {
    
    private String treeName = "tree";
    
    private  Map<String,Branch>     treeBranches    = new LinkedHashMap<String,Branch>();
    private  TreeSelector           defaultSelector = new TreeSelector();
    private  static Map<String,ImageIcon>  treeNodeIcons   = Tree.initTreeIcons();
            
    public Tree(String name){
        this.treeName = name;
    }
    /**
     * returns the name of the tree
     * @return 
     */
    @Override
    public String getName(){return this.treeName;}
    
    public void addBranch(Branch br){
        treeBranches.put(br.getName(), br);
    }
    
    public void addBranch(String name, String desc, String unit){
        addBranch(new Branch(name,desc,unit));
    }

    @Override
    public List<String> getListOfBranches() {
        List<String> listOfBranches = new ArrayList<String>();
        for(Map.Entry<String,Branch> entry : treeBranches.entrySet()){
            listOfBranches.add(entry.getKey());
        }
        return listOfBranches;
    }

    public static Map<String,ImageIcon>  getTreeIcons(){
        return treeNodeIcons;
    }
    
    public static Map<String,ImageIcon>  initTreeIcons(){
        Map<String,ImageIcon> iconMap = new HashMap<String,ImageIcon>();
        try {
            ImageIcon leafIcon = new ImageIcon(Tree.class.getClassLoader().getResource("icons/tree/leaf_t.png"));
            ImageIcon dirIcon  = new ImageIcon(Tree.class.getClassLoader().getResource("icons/tree/tree_t.png"));            
            UIManager.put("Tree.closedIcon", dirIcon);
            UIManager.put("Tree.openIcon",   dirIcon);
            UIManager.put("Tree.leafIcon",  leafIcon);
            UIManager.put("Tree.paintLines", Boolean.TRUE);
        } catch(Exception e){
            
        }
        return iconMap;
    }
    
    @Override
    public Branch getBranch(String name) {
        return this.treeBranches.get(name);
    }

    @Override
    public void reset() {
        
    }

    @Override
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

    @Override
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
    
    public boolean hasBranch(String name){
        return this.treeBranches.containsKey(name);
    }
    
    public DefaultMutableTreeNode getTree() {

        DefaultMutableTreeNode root         = new DefaultMutableTreeNode(getName());
        DefaultMutableTreeNode rootbranch   = new DefaultMutableTreeNode("Branches");
        DefaultMutableTreeNode rootcuts     = new DefaultMutableTreeNode("Selector");
        root.add(rootbranch);
        root.add(rootcuts);
        
        List<String> branches = getListOfBranches();
        for(String item : branches){
            rootbranch.add(new DefaultMutableTreeNode(item));
        }
        
        Map<String,TreeCut> cuts = this.defaultSelector.getSelectorCuts();
        for(Map.Entry<String,TreeCut> entry : cuts.entrySet()){
            rootcuts.add(new DefaultMutableTreeNode(entry.getKey()));
        }
        return root;
    }        
        
}
