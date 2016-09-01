package org.jlab.groot.demo;

import java.util.Random;

import javax.swing.JFrame;

import org.jlab.groot.data.H1F;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.ui.TCanvas;

public class BasicDemo {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Basic GROOT Demo");
		EmbeddedCanvas canvas = new EmbeddedCanvas();
		frame.setSize(800,600);
		H1F histogram = new H1F("histogram",100,-5,5); 
		Random randomGenerator = new Random();
		for(int i=0; i<5000; i++){
			histogram.fill(randomGenerator.nextGaussian());
		}
		canvas.draw(histogram);
		frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
