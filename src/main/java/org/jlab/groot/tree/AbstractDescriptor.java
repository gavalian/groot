/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.data.TDirectory;

/**
 *
 * @author gavalian
 */
public abstract class AbstractDescriptor {
    
    private TDirectory directory = new TDirectory();
    private String descName = "";
    private final List<IDataSet> descriptorDatasets = new ArrayList<IDataSet>();
    
    public AbstractDescriptor(){
        
    }
    
    public final void setName(String name){ this.descName = name;}
    public final String getName(){return this.descName;}
    
    public final void addDataSet(IDataSet ds){
        this.descriptorDatasets.add(ds);
    }
    
    public List<IDataSet> getData(){ return this.descriptorDatasets;}
    
    public abstract void           processTreeEvent(Tree tree);
    public abstract JDialog        edit(Tree tree);
    
}
