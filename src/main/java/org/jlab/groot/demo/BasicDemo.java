package org.jlab.groot.demo;

import java.util.Random;
import javax.swing.JFrame;
import org.jlab.groot.data.DataLine;

import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.math.FunctionFactory;
import org.jlab.groot.ui.LatexText;
import org.jlab.groot.ui.PaveText;

public class BasicDemo {

	public static void main(String[] args) {
            
		JFrame frame = new JFrame("Basic GROOT Demo");
		EmbeddedCanvas canvas = new EmbeddedCanvas();
		frame.setSize(800,500);
                
                H2F h2d = FunctionFactory.randomGausian2D(40, 0.4, 7.6, 800000, 3.3, 0.8);
                
		H1F histogram = new H1F("histogram","Some Histogram;Randomly Created;Counts",100,-5,5); 
		Random randomGenerator = new Random();
		for(int i=0; i<50000; i++){
			histogram.fill(randomGenerator.nextGaussian());
		}
		//histogram.setTitleX("Randomly Generated Function");
		//histogram.setTitleY("Counts");
		canvas.getPad(0).setTitle("BasicDemo Test");
		histogram.setLineWidth(2);
		histogram.setLineColor(21);
		histogram.setFillColor(34);
		histogram.setOptStat(1110);
		
                //canvas.draw(histogram);
                //canvas.draw(h2d);
                canvas.draw(histogram);
                
		canvas.setFont("HanziPen TC");  
		canvas.setTitleSize(32);
		canvas.setAxisTitleSize(24);
		canvas.setAxisLabelSize(18);
		canvas.setStatBoxFontSize(18);
                
                
                LatexText text = new LatexText("NDF = 24",100,50);
                text.setFont("HanziPen TC");
                text.setFontSize(28);
                text.setColor(2);
                LatexText textChi = new LatexText("#chi^2 = 0.0567",100,80);
                textChi.setFont("HanziPen TC");
                //textChi.setFont("Times");
                textChi.setFontSize(28);
                textChi.setColor(4);
                //text.setLocation(100, 100);
                canvas.draw(text);
                canvas.draw(textChi);
                
                
                DataLine line = new DataLine(-2.5,1000.0,-2.5,1500);
                line.setLineColor(2);
                line.setLineWidth(2);
                line.setArrowSizeOrigin(15);
                line.setArrowSizeEnd(15);
                line.setArrowAngle(25);
                canvas.draw(line);
                
                DataLine line2 = new DataLine(0.4,550.0,2.5,1400);
                line2.setLineColor(1);
                line2.setLineWidth(2);
                line2.setArrowSizeOrigin(15);
                canvas.draw(line2);
                
                DataLine line3 = new DataLine(1.2,550.0,-1.5,1800);
                line3.setLineColor(3);
                line3.setLineWidth(4);
                line3.setArrowSizeEnd(15);
                line3.setArrowAngle(45.0);
                
                canvas.draw(line3);
                
                frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
