package org.jlab.groot.demo;

import java.util.Random;

import javax.swing.JFrame;

import org.jlab.groot.data.H1F;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.math.F1D;
import org.jlab.groot.math.RandomFunc;

public class multiGaus {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Demo JFrame");
		frame.setSize(1000, 650);
		EmbeddedCanvas c1 = new EmbeddedCanvas();
		c1.divide(4, 4);

		Random rand = new Random();
		H1F[] h1 = new H1F[16];
		for (int i = 0; i < h1.length; i++) {
			h1[i] = new H1F("h" + i, "", 200, -5.0, 5.0);
			h1[i].setXTitle("Randomly Generated Function");
			h1[i].setYTitle("Counts");
			F1D f1 = new F1D("f1","[amp]*gaus(x,[mean],[sigma])", -5.0, 5.0);
			f1.setParameter(0, 120.0);
			f1.setParameter(1, (-3.0 + rand.nextDouble() * 6));
			f1.setParameter(2, .4 + (rand.nextDouble() * 1));
			//f1.setParameter(3, 10);
			//f1.setParameter(4, .4 + (rand.nextDouble() * 5 - 2));

			RandomFunc rndm = new RandomFunc(f1);
			for (int j = 0; j < 34000; j++) {
				h1[i].fill(rndm.random());
			}
			h1[i].setLineWidth(2);
			//h1[i].setLineColor(5);
			h1[i].setFillColor(30 + (i % 4) + 2);
			c1.cd(i);
			//c1.getPad(i).getAxisFrame().getAxisX().attr.setTitleOffset(100);
			c1.draw(h1[i]);
		}
		frame.add(c1);
		frame.setVisible(true);

	}

}
