/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.demo;

import org.jlab.groot.data.H1F;
import org.jlab.groot.ui.TCanvas;

/**
 *
 * @author gavalian
 */
public interface HistogramRangeDemo {
    public static void main(String[] args){
        TCanvas c1 = new TCanvas("c1",600,600);
        c1.getCanvas().initTimer(4000);
        H1F h1 = new H1F("H1",5,0.0,1.0);
        H1F h2 = new H1F("H2",5,0.0,1.0);
        c1.divide(1,2);
        c1.cd(0).draw(h1);
        c1.cd(1).draw(h2);
        
        while(true){
            double r = Math.random();
            
            h1.fill(r);
            h2.fill(r,0.01);
        }
    }
}
