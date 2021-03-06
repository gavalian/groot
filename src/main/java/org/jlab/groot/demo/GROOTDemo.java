package org.jlab.groot.demo;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.math.F1D;
import org.jlab.groot.math.FunctionFactory;
import org.jlab.groot.math.RandomFunc;

public class GROOTDemo {

	public static void main(String[] args) {
		Dimension screensize  = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame("GROOT DEMO");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize((int)(screensize.getHeight()*.75*1.618), (int) (screensize.getHeight()*.75));
		JTabbedPane tabbedPane = new JTabbedPane();
		EmbeddedCanvas can1 = new EmbeddedCanvas();
		can1.divide(4, 4);
		Random rand = new Random();
		H1F[] h1 = new H1F[16];
		for (int i = 0; i < h1.length-1; i++) {
			h1[i] = new H1F("h" + i, "", 200, -5.0, 5.0);
			h1[i].setTitleX("Randomly Generated Function");
			h1[i].setTitleY("Counts");
			h1[i].setOptStat(0);
			F1D f1 = new F1D("f1","[amp]*gaus(x,[mean],[sigma])+[p0]", -5.0, 5.0);
			f1.setParameter(0, 120.0);
			f1.setParameter(1, (-3.0 + rand.nextDouble() * 6));
			f1.setParameter(2, .4 + (rand.nextDouble() * 1));
			f1.setParameter(3, 20.0);


			RandomFunc rndm = new RandomFunc(f1);
			for (int j = 0; j < 34000; j++) {
				h1[i].fill(rndm.random());
			}
			h1[i].setLineWidth(2);
			h1[i].setLineColor(21);
			h1[i].setFillColor(30 + (i % 4) + 2);
			
			can1.cd(i);
			//can1.getPad(i).setTitle("OptionsPanel Demo");
			String optStatString = "";
			for(int j=0; j<3-i%4; j++){
				optStatString +="1";
			}
			optStatString+="00";
			f1.setOptStat(Integer.parseInt(optStatString));
			
			can1.draw(h1[i]);
			
			//f1.setParameter(0, h1[i].getEntries()); //Due to initial parameter estimates not existing
			DataFitter.fit(f1, h1[i], "Q"); //No options uses error for sigma
			can1.draw(f1,"same");
			f1.setLineColor(30 + (i % 4) + 2);
			f1.setLineWidth(3);
			f1.setLineStyle(i%4);
			can1.setFont("HanziPen TC");	
			can1.setTitleSize(18);
			can1.setAxisTitleSize(14);
			can1.setAxisLabelSize(12);
		}
		
		EmbeddedCanvas can2 = new EmbeddedCanvas();
		can2.divide(3, 2);
		H1F[] h2 = new H1F[6];
		for (int i = 0; i < h2.length; i++) {
			h2[i] = new H1F("h" + i, "", 200, -10.0, 10.0);
			h2[i].setTitleX("Randomly Generated Function");
			h2[i].setTitleY("Counts");
			h2[i].setOptStat(0);
			F1D f1 = new F1D("f1","[amp]*gaus(x,[mean],[sigma])+[amp2]*gaus(x,[mean2],[sigma2])+[p0]+[p1]*x+[p2]*x*x", -10.0, 10.0);
			f1.setParameter(0, 30.0+rand.nextDouble()*90.0);
			f1.setParameter(1, (-6.0 + rand.nextDouble() * 5));
			f1.setParameter(2, 1.8 + (rand.nextDouble() * 1));
			f1.setParameter(3, 30.0+rand.nextDouble()*90.0);
			f1.setParameter(4, (4.0 + rand.nextDouble() * 4));
			f1.setParameter(5, 1.2 + (rand.nextDouble() * 1));
			f1.setParameter(6, 50.0);
			f1.setParameter(7, 1.2);
			f1.setParameter(8, -.2);



			RandomFunc rndm = new RandomFunc(f1);
			for (int j = 0; j < 64000; j++) {
				h2[i].fill(rndm.random());
			}
			h2[i].setLineWidth(2);
			h2[i].setLineColor(21);
			h2[i].setFillColor(30 + (i % 3) + 2);
			
			can2.cd(i);
			can2.getPad(i).setTitle("OptionsPanel Demo2");
			String optStatString = "";
			for(int j=0; j<3-i%3; j++){
				optStatString +="1";
			}
			optStatString+="00";
			f1.setOptStat(optStatString);
			can2.setFont("HanziPen TC");	
			can2.setTitleSize(24);
			can2.setAxisTitleSize(18);
			can2.setAxisLabelSize(18);
			can2.setStatBoxFontSize(18);
			can2.draw(h2[i]);
			
			//f1.setParameter(0, h2[i].getEntries()*.66); //Due to initial parameter estimates not existing
			//f1.setParameter(3, h2[i].getEntries()*.66); //Due to initial parameter estimates not existing

			DataFitter.fit(f1, h2[i], "Q"); //No options uses error for sigma
			
			can2.draw(f1,"same");
			f1.setLineColor(20 + (i % 3) + 2);
			f1.setLineWidth(6);
			f1.setLineStyle(i%4);
			

		}
		
		EmbeddedCanvas can3 = new EmbeddedCanvas();
		can3.divide(2,2);

        H2F h2d  = FunctionFactory.randomGausian2D(40, 0.4, 7.6, 800000, 3.3, 0.8);
        H2F h2d2 = FunctionFactory.randomGausian2D(40, 0.4, 7.6, 800000, 5.2, 0.4);
        h2d.setTitleX("Randomly Generated Function");
        h2d.setTitleY("Randomly Generated Function");
        GraphErrors hprofile = h2d.getProfileY();
        H1F h2p =  h2d.projectionX();
        can3.cd(0);
        can3.getPad(0).getAxisZ().setRange(100, 600);
        can3.getPad(0).setTitle("Function with Fixed Axis");
        can3.draw(h2d,"box");
        can3.draw(h2d2,"samebox");
        can3.cd(1);
        can3.getPad(1).getAxisZ().setAutoScale(true);
        can3.getPad(1).setTitle("Function with Autoscaled Axis");
        h2d.getAttributes().setLineColor(2);
        h2d2.getAttributes().setLineColor(4);
        //can3.draw(h2d2,"box");
        can3.draw(h2d2,"");
        can3.cd(3);
        can3.getPad(3).setTitle("X Projection");
        H1F projectionX = h2d.projectionX();
        projectionX.setTitleX("Randomly Generated Function");
        projectionX.setTitleY("Counts");
        projectionX.setFillColor(44);
        can3.draw(projectionX);      
        can3.cd(2);
        GraphErrors profileY = h2d.getProfileY();
        profileY.setTitleX("Randomly Generated Function");
        profileY.setTitleY("Counts");
        profileY.setMarkerColor(4);
        profileY.setLineColor(4);
        can3.draw(profileY);
        can3.getPad(2).setTitle("Y Profile");
		can3.setFont("HanziPen TC");	
		can3.setTitleSize(24);
		can3.setAxisTitleSize(18);
		can3.setAxisLabelSize(18);
		can3.setStatBoxFontSize(18);
		
		tabbedPane.add("H1D Demo", can1);
		tabbedPane.add("H1D Demo2", can2);
		tabbedPane.add("H2D and GraphErrors", can3);

		
		frame.add(tabbedPane);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}

}
