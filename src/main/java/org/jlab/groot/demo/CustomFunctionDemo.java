package org.jlab.groot.demo;

import javax.swing.JFrame;

import org.jlab.groot.data.H1F;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.math.RandomFunc;

public class CustomFunctionDemo {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Basic GROOT Demo");
		EmbeddedCanvas canvas = new EmbeddedCanvas();
		frame.setSize(800,500);
		H1F histogram = new H1F("histogram",100,-5.0,5.0); 
		CustomFunction f1 = new CustomFunction("CustomFunction", -5.1, 5.1);
		f1.addParameter("p0");
		f1.setParameter(0, 30.0);
		f1.addParameter("p1");
		f1.setParameter(1, -6.0);
		f1.addParameter("p2");
		f1.setParameter(2, 1.8);
		f1.addParameter("p3");
		f1.setParameter(3, .0001);

		RandomFunc rndm = new RandomFunc(f1);
		for (int j = 0; j < 64000; j++) {
			histogram.fill(rndm.random());
		}
		histogram.setTitleX("Randomly Generated Function");
		histogram.setTitleY("Counts");
		canvas.getPad(0).setTitle("CustomFunction Example");
		histogram.setLineWidth(2);
		histogram.setLineColor(21);
		histogram.setFillColor(34);
		histogram.setOptStat(10);
		
		f1.setLineColor(5);
		f1.setLineWidth(7);
		f1.setOptStat(111110);
		
		//DataFitter.fit(f1, histogram, "Q");
		canvas.draw(histogram);
		canvas.draw(f1,"");
		
		canvas.setFont("Avenir");  
		canvas.setTitleSize(32);
		canvas.setAxisTitleSize(24);
		canvas.setAxisLabelSize(18);
		canvas.setStatBoxFontSize(18);
		frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
