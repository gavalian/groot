package org.jlab.groot.demo;

import javax.swing.JFrame;

import org.jlab.groot.base.GStyle;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.graphics.EmbeddedCanvas;

public class BasicDemo2 {

	public static void main(String[] args) {
		GStyle.getGraphErrorsAttributes().setMarkerStyle(0);
		GStyle.getGraphErrorsAttributes().setMarkerColor(3);
		GStyle.getGraphErrorsAttributes().setMarkerSize(7);
		GStyle.getGraphErrorsAttributes().setLineColor(3);
		GStyle.getGraphErrorsAttributes().setLineWidth(3);
		GStyle.getAxisAttributesX().setTitleFontSize(32);
		GStyle.getAxisAttributesX().setLabelFontSize(28);
		GStyle.getAxisAttributesY().setTitleFontSize(32);
		GStyle.getAxisAttributesY().setLabelFontSize(28);
		
	    double barberx[] = {3.95,4.05,4.15,4.25,4.35,4.45,4.55,4.65,4.75};
	    double barbery[] = {.8,3.1,10.0,29.8,31.2,42.5,28.5,39.8,29.7};
	    double barberyerr[] = {0.0,2.0,4.0,6.0,6.5,8.0,6.0,7.0,6.5};
	    double bodenkampx[] = {((5.102+4.740)/2.0),((5.464+5.102)/2.0),((5.464+5.826)/2.0),((5.826+6.188)/2.0),((6.188+6.550)/2.0)};
	    double bodenkampy[] = {75.8,82.1,65.0,89.6,84.9};
	    double bodenkampyerr[] = {13.4,16.7,13.5,17.5,15.7};

	    JFrame frame = new JFrame("Basic GROOT Demo 2");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		EmbeddedCanvas canvas = new EmbeddedCanvas();
		frame.setSize(1200,750);
		
		GraphErrors barber = new GraphErrors();
		GraphErrors bodemkamp = new GraphErrors();

		for(int i=0; i< barberx.length; i++){
			barber.addPoint(barberx[i], barbery[i], 0, barberyerr[i]);
		}
		for(int i=0; i< bodenkampx.length; i++){
			bodemkamp.addPoint(bodenkampx[i], bodenkampy[i], 0, bodenkampyerr[i]);
		}
		
		barber.setTitleX("E_#gamma [GeV]");
		barber.setTitleY("#sigma [nb]");
                
		canvas.getPad(0).setTitle("Total Cross Section #gammap#rarrowppp#bar");
		
		barber.setMarkerColor(2);
		barber.setLineColor(2);
	
		bodemkamp.setMarkerColor(4);
		bodemkamp.setLineColor(4);
		
                barber.setTitle("Cross Section (nb)");
                bodemkamp.setTitle("Assymetry ");
		canvas.draw(barber,"L");
		canvas.draw(bodemkamp,"sameL");

                canvas.getPad().setLegend(true);
                canvas.getPad().setLegendPosition(20, 20);
		canvas.setFont("HanziPen TC");  
		canvas.setTitleSize(48);
		canvas.setStatBoxFontSize(18);
		
		frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
