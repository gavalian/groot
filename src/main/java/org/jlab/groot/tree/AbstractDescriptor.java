/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.tree;

import javax.swing.JDialog;
import org.jlab.groot.data.TDirectory;

/**
 *
 * @author gavalian
 */
public abstract class AbstractDescriptor {
    
    TDirectory directory = new TDirectory();
    
    public AbstractDescriptor(){
        
    }
    
    public abstract void     processTreeEvent(Tree tree);
    public abstract JDialog  edit();
    
}
