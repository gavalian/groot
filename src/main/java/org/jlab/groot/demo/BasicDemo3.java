package org.jlab.groot.demo;

import java.util.Random;

import javax.swing.JFrame;

import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.math.FunctionFactory;

public class BasicDemo3 {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Basic GROOT Demo");
		EmbeddedCanvas canvas = new EmbeddedCanvas();
		frame.setSize(800,500);
		H2F histogram2d = FunctionFactory.randomGausian2D(200, 0.4, 7.6, 80000000, 3.3, 0.8);
		histogram2d.setTitleX("Randomly Generated Function");
		histogram2d.setTitleY("Randomly Generated Function");		
		canvas.getPad(0).setTitle("Histogram2D Demo");
		canvas.draw(histogram2d);
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
