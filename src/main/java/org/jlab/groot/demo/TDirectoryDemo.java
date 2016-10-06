/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.demo;

import java.util.List;
import java.util.Set;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.TDirectory;
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.FunctionFactory;
import org.jlab.groot.ui.TCanvas;

/**
 *
 * @author gavalian
 */
public class TDirectoryDemo {
    
    public static void main(String[] args){
        TDirectory dir = new TDirectory();
        H1F  h1    = FunctionFactory.randomGausian( 80, 0.1, 5.0,  8000, 2.2, 0.5);
        H1F  h2    = FunctionFactory.randomGausian(120, 0.1, 5.0, 20000, 3.5, 0.4);
        H1F  h3    = FunctionFactory.randomGausian(160, 0.1, 5.0, 14000, 2.6, 0.3);
        H1F  h4    = FunctionFactory.randomGausian( 80, 0.1, 5.0,  8000, 2.2, 0.5);
        H1F  h5    = FunctionFactory.randomGausian(160, 0.1, 5.0, 14000, 2.6, 0.3);
        
        h1.setName("h1");
        h2.setName("h2");
        h3.setName("h3");
        h4.setName("h4");
        h5.setName("h5");
        
        h1.setFillColor(33);
        h2.setFillColor(34);
        h3.setFillColor(36);
        h4.setFillColor(35);
        h5.setFillColor(38);
        
        dir.mkdir("/calibration/FTOF");
        dir.mkdir("/calibration/ECAL");
        dir.cd("/calibration/FTOF");
        
        dir.addDataSet(h1,h2,h3);
        
        dir.cd("/calibration/ECAL");
        dir.addDataSet(h4,h5);
        TCanvas c1 = new TCanvas("c1",800,500);
        c1.draw(h1);
        
        dir.addGroupDescriptor("FTOFCALIB", 2, 2);
        
        dir.addGroup("FTOFCALIB", 0, "/calibration/FTOF/h1");
        dir.addGroup("FTOFCALIB", 0, "/calibration/FTOF/h2");
        dir.addGroup("FTOFCALIB", 1, "/calibration/FTOF/h3");
        dir.addGroup("FTOFCALIB", 2, "/calibration/ECAL/h4");
        dir.addGroup("FTOFCALIB", 3, "/calibration/ECAL/h5");
        dir.addGroup("FTOFCALIB", 3, "/calibration/FTOF/h2");
        dir.addGroup("FTOFCALIB", 3, "/calibration/FTOF/h1");
        
        List<String> dirObjects = dir.getCompositeObjectList(dir);
        
        Set<String> nodes = dir.getChildrenList("calibration", dirObjects, 2);
        System.out.println(" SIZE = " + nodes.size());
        for(String obj : nodes){
            System.out.println(" --- " + obj);
        }
        DataGroup group = dir.getDataGroup("FTOFCALIB");
        
        c1.getCanvas().draw(group);
        
        dir.writeFile("dirDemo.hipo");
        
        //dir.addDataSet(h2);
        //dir.addDataSet(h3);
    }
}
