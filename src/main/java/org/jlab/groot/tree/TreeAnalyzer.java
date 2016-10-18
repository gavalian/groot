/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
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
        process(tree,-1);
    }
    
    public void process(ITree tree, int limit){
        tree.reset();
        for(DatasetDescriptor desc : datasets){
            desc.getDataSet().reset();
        }
        int nentries = tree.getEntries();
        
        for(int i = 0; i < nentries; i++){
            tree.readEntry(i);
            if(i>limit&&limit>0) break;
            for(DatasetDescriptor desc : datasets){
                desc.fill(tree);
                //if(desc.getDataSet() instanceof )
            }
        }
    }
    
    public List<DatasetDescriptor>  getDescriptors(){
        return this.datasets;
    }
    
     public DefaultMutableTreeNode getTree() {

        DefaultMutableTreeNode root         = new DefaultMutableTreeNode("Analyzer");
        
        for(DatasetDescriptor desc : datasets){
            //root.add(new DefaultMutableTreeNode(desc.getName()));
        }
        return root;
     }
 
}
