/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.GraphicsAxis.GraphicsAxisTicks;
import org.jlab.groot.math.F1D;
import org.jlab.groot.math.RandomFunc;
import org.jlab.groot.ui.TCanvas;

/**
 *
 * @author gavalian
 */
public class AxisLabelDemo {
    
    public static void main(String[] args){
       GraphicsAxisTicks ticks; 
       ticks = new GraphicsAxisTicks();
       List<Double> values = Arrays.asList(new Double[]{0.0,20000.0,40000.0,60000.0,80000.0,100000.0});
       ticks.init(values,0.0,2000.0);
       ticks.processAxisExponent();
       ticks.show();
       Random rand = new Random();
       H1F h1 = new H1F("h1" , "Random Histogram", 200, -5.0, 5.0);
       H2F h2 = new H2F("h1" , "Random Histogram", 80, -5.0, 5.0,80,-5.0,5.0);
       
       h1.setTitleX("X TILTE");
       h1.setTitleY("Y TILTE");
       
       F1D f1 = new F1D("f1","[amp]*gaus(x,[mean],[sigma])+[p0]", -5.0, 5.0);
       f1.setParameter(0, 120.0);
       f1.setParameter(1, (-3.0 + rand.nextDouble() * 6));
       f1.setParameter(2, .4 + (rand.nextDouble() * 1));
       f1.setParameter(3, 20.0);

       TCanvas c1 = new TCanvas("groot",500,500);
       c1.draw(h2);
       c1.getCanvas().initTimer(200);
       c1.getCanvas().getPad().getAxisZ().setLog(true);
       RandomFunc rndm = new RandomFunc(f1);
       for (int j = 0; j < 6400000; j++) {
           
           try {
               Thread.sleep(0);
           } catch (InterruptedException ex) {
               Logger.getLogger(AxisLabelDemo.class.getName()).log(Level.SEVERE, null, ex);
           }
           
           h1.fill(rndm.random());
           h2.fill(rndm.random(),rndm.random());
       }
       
    }
}
