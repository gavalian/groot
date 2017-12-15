package org.jlab.groot.demo;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.FunctionFactory;

public class Gaus2D {
	public static void main(String[] args) {
		Dimension screensize  = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame("GROOT DEMO");
		frame.setSize((int)(screensize.getHeight()*.75*1.618), (int) (screensize.getHeight()*.75));
		EmbeddedCanvas c1 = new EmbeddedCanvas();
		c1.divide(2,2);

        H2F h2d = FunctionFactory.randomGausian2D(40, 0.4, 7.6, 800000, 3.3, 0.8);
        h2d.setTitleX("Randomly Generated Function");
        h2d.setTitleY("Randomly Generated Function");
        GraphErrors hprofile = h2d.getProfileY();
        H1F h2p =  h2d.projectionX();
        c1.cd(0);
        c1.getPad(0).getAxisZ().setRange(100, 600);
        c1.getPad(0).setTitle("Function with Fixed Axis");
        c1.draw(h2d);
        c1.cd(1);
        c1.getPad(1).getAxisZ().setAutoScale(true);
        c1.getPad(1).setTitle("Function with Autoscaled Axis");
        c1.draw(h2d);
        c1.cd(3);
       // c1.getPad(3).setTitle("X Projection");
        H1F projectionX = h2d.projectionX();
        projectionX.setTitleX("Randomly Generated Function");
        projectionX.setTitleY("Counts");
        c1.draw(projectionX);
        c1.getPad(3).setTitle("X Projection");
        c1.cd(2);
        GraphErrors profileY = h2d.getProfileY();
        profileY.setTitleX("Randomly Generated Function");
        profileY.setTitleY("Counts");
        c1.draw(profileY);
        c1.getPad(2).setTitle("Y Profile");
        
    	frame.add(c1);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		c1.setFont("HanziPen TC");	
		c1.setTitleSize(16);
		c1.setAxisTitleSize(12);
		c1.setAxisLabelSize(12);
                c1.update();
	}
}
