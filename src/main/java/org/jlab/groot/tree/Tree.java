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
import org.jlab.groot.data.DataVector;

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
    
    public void resetBranches(double number){
        for(Map.Entry<String,Branch> entry : this.treeBranches.entrySet()){
            entry.getValue().setValue(number);
        }
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
    
    public Map<String,Branch>  getBranches(){
        return this.treeBranches;
    }
    
    public void print(){
        StringBuilder str = new StringBuilder();
        for(Map.Entry<String,Branch> entry : treeBranches.entrySet()){
            str.append(entry.getValue().toString());
            str.append("  ");
            str.append(entry.getValue().getValue());
            str.append("\n");
        }
        System.out.println( str.toString());
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
    
    public DataVector  getDataVector(String expression, String tcut){
        return getDataVector(expression,tcut,-1);
    }
    
    public DataVector  getDataVector(String expression, String tcut, int limit){
        DataVector vec = new DataVector();
        int nentries = getEntries();
        TreeExpression exp = new TreeExpression("oper",expression,getListOfBranches());
        TreeCut        cut = new TreeCut("cut",tcut,getListOfBranches());
        
        for(int i = 0; i < nentries; i++){
            readEntry(i);
            if(cut.isValid(this)==true){
                double result = exp.getValue(this);
                vec.add(result);
            }
            if(limit>0&&i>limit) return vec;
        }
        return vec;
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

    @Override
    public int getEntries() {
        return 0;
    }

    @Override
    public int readEntry(int entry) {
        return 0;
    }
        
}
