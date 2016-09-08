package org.jlab.groot.demo;

import java.util.Random;
import javax.swing.JFrame;

import org.jlab.groot.data.H1F;
import org.jlab.groot.graphics.EmbeddedCanvas;

public class BasicDemo {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Basic GROOT Demo");
		EmbeddedCanvas canvas = new EmbeddedCanvas();
		frame.setSize(800,500);
		H1F histogram = new H1F("histogram",100,-5,5); 
		Random randomGenerator = new Random();
		for(int i=0; i<50000; i++){
			histogram.fill(randomGenerator.nextGaussian());
		}
		histogram.setTitleX("Randomly Generated Function");
		histogram.setTitleY("Counts");
		canvas.getPad(0).setTitle("BasicDemo Test");
		histogram.setLineWidth(2);
		histogram.setLineColor(21);
		histogram.setFillColor(34);
		histogram.setOptStat(1110);
		canvas.draw(histogram);
		canvas.setFont("HanziPen TC");  
		canvas.setTitleSize(32);
		canvas.setAxisTitleSize(24);
		canvas.setAxisLabelSize(18);
		canvas.setStatBoxFontSize(18);
		frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
