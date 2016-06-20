/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.List;
import org.jlab.groot.data.IDataSet;

/**
 *
 * @author gavalian
 */
public class TreeAnalyzer {
    
    List<DatasetDescriptor>   datasets = new ArrayList<DatasetDescriptor>();
    
    public TreeAnalyzer(){
        
    }
    
    public void addDescriptor(DatasetDescriptor desc){
        this.datasets.add(desc);
    }
    
    public void process(ITree tree){
        tree.reset();
        
        while(tree.readNext()==true){
            for(DatasetDescriptor desc : datasets){
                //if(desc.getDataSet() instanceof )
            }
        }
    }
    
    public class DatasetDescriptor {
        
        String name = "somename";
        IDataSet       descDataset = null;
        List<String>   descCutList = new ArrayList<String>();
        
        public DatasetDescriptor(String name, int nbins, double min, double max){
            
        }
        
        public void addCut(String name){
            descCutList.add(name);
        }
        
        public List<String>  getCuts(){
            return this.descCutList;
        }
        
        public IDataSet getDataSet(){
            return this.descDataset;
        }
    }
}
