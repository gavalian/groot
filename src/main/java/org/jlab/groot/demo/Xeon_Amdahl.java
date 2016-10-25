package org.jlab.groot.demo;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.jlab.groot.base.GStyle;
import org.jlab.groot.data.DataVector;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.math.F1D;
import org.jlab.groot.tree.TreeTextFile;

public class Xeon_Amdahl {
	
	public static void main(String[] args) {
		GStyle.getGraphErrorsAttributes().setMarkerStyle(0);
		GStyle.getGraphErrorsAttributes().setMarkerColor(3);
		GStyle.getGraphErrorsAttributes().setMarkerSize(7);
		GStyle.getGraphErrorsAttributes().setLineColor(3);
		GStyle.getGraphErrorsAttributes().setLineWidth(0);
		GStyle.getFunctionAttributes().setLineWidth(6);
		GStyle.getAxisAttributesX().setTitleFontSize(32);
		GStyle.getAxisAttributesX().setLabelFontSize(28);
		GStyle.getAxisAttributesY().setTitleFontSize(32);
		GStyle.getAxisAttributesY().setLabelFontSize(28);
		JFrame frame = new JFrame("Xeon Comparison");
		frame.setSize(1400, 1000);
		EmbeddedCanvas comparisonCanvas = new EmbeddedCanvas();
		comparisonCanvas.setPreferredSize(new Dimension(1400,1000));
		
		TreeTextFile broadwell_affinity = new TreeTextFile("T");
		TreeTextFile broadwell = new TreeTextFile("T");
		TreeTextFile haswell = new TreeTextFile("T");
		broadwell.readFile("src/main/resources/sample_data/clararec_analysis/broadwell.txt");
		broadwell_affinity.readFile("src/main/resources/sample_data/clararec_analysis/broadwell_specaffinity.txt");
		haswell.readFile("src/main/resources/sample_data/clararec_analysis/haswell.txt");

		DataVector y_broadwell = broadwell.getDataVector("ab", "");
		double first = y_broadwell.getArray()[0];
		DataVector y_b = broadwell.getDataVector(String.format("ab/%f",first), "");
		DataVector x_b = broadwell.getDataVector("aa", "");
		GraphErrors broadwellGraph = new GraphErrors("Broadwell",x_b.getArray(),y_b.getArray());
		broadwellGraph.setTitleX("Number of Threads");
		broadwellGraph.setTitleY("Speedup (t_parallel/t_serial)");
		comparisonCanvas.draw(broadwellGraph);
		F1D amdahl1 = new F1D("Amdahl_broadwell","1/((1-[p_broadwell])+[p_broadwell]/x)",1,36);
		amdahl1.setParameter(0, .99);
		broadwellGraph.setMarkerColor(4);
		amdahl1.setLineColor(44);
		DataFitter.fit(amdahl1, broadwellGraph, "QW");
		//amdahl1.setRange(1, 36);
		amdahl1.setOptStat("10");
		comparisonCanvas.draw(amdahl1,"same");
		F1D amdahl1_2 = new F1D("Amdahl_broadwell2","1/((1-[p_broadwell2])+[p_broadwell2]/x)",36,72);
		amdahl1_2.setParameter(0, amdahl1.getParameter(0));
		amdahl1_2.setLineStyle(2);
		amdahl1_2.setLineColor(44);
		//amdahl1_2.setRange(1,72);
		comparisonCanvas.draw(amdahl1_2,"same");
		
		DataVector y_haswell = haswell.getDataVector("ab", "");
		first = y_haswell.getArray()[0];
		DataVector y_h = haswell.getDataVector(String.format("ab/%f",first), "");
		DataVector x_h = haswell.getDataVector("aa", "");
		GraphErrors haswellGraph = new GraphErrors("haswell",x_h.getArray(),y_h.getArray());
		comparisonCanvas.draw(haswellGraph,"same");
		F1D amdahl2 = new F1D("Amdahl_haswell","1/((1-[p_haswell])+[p_haswell]/x)",1,24);
		amdahl2.setParameter(0, .99);
		haswellGraph.setMarkerColor(2);
		amdahl2.setLineColor(42);
		DataFitter.fit(amdahl2, haswellGraph, "QW");
		amdahl2.setOptStat("10");
		comparisonCanvas.draw(amdahl2,"same");

		F1D amdahl2_2 = new F1D("Amdahl_haswell2","1/((1-[p_haswell2])+[p_haswell2]/x)",24,48);
		amdahl2_2.setParameter(0, amdahl2.getParameter(0));
		amdahl2_2.setLineStyle(2);
		amdahl2_2.setLineColor(42);
		comparisonCanvas.draw(amdahl2_2,"same");


		
		DataVector y_broadwell_aff = broadwell_affinity.getDataVector("ab", "");
		first = y_broadwell_aff.getArray()[0];
		DataVector y_ba = broadwell_affinity.getDataVector(String.format("ab/%f",first/2.0), "");
		DataVector x_ba = broadwell_affinity.getDataVector("aa", "");
		GraphErrors broadwellaffGraph = new GraphErrors("broadwell_affinity",x_ba.getArray(),y_ba.getArray());
		comparisonCanvas.draw(broadwellaffGraph,"same");
		F1D amdahl3 = new F1D("Amdahl_broadwell_aff","1/((1-[p_broadwell_affinity])+[p_broadwell_affinity]/x)",1,36);
		amdahl3.setParameter(0, .99);
		broadwellaffGraph.setMarkerColor(1);
		amdahl3.setLineColor(41);
		DataFitter.fit(amdahl3, broadwellaffGraph, "QW");
		amdahl3.setRange(1, 36);
		amdahl3.setOptStat("10");
		comparisonCanvas.draw(amdahl3,"same");
		
		F1D amdahl3_2 = new F1D("broadwell_affinity_2","1/((1-[p_broadwell_affinity2])+[p_broadwell_affinity2]/x)",36,72);
		amdahl3_2.setParameter(0, amdahl3.getParameter(0));
		amdahl3_2.setLineStyle(2);
		amdahl3_2.setLineColor(41);
		amdahl3_2.setRange(36,72);
		comparisonCanvas.draw(amdahl3_2,"same");

		F1D amdahl3_3 = new F1D("broadwell_affinity_2","1/((1-[p_broadwell_affinity2])+[p_broadwell_affinity2]/x)",36,72);
		amdahl3_3.setParameter(0, 1.0);
		amdahl3_3.setLineStyle(2);
		amdahl3_3.setLineColor(48);
		amdahl3_3.setRange(1,72);
		//comparisonCanvas.draw(amdahl3_3,"same");
		
		comparisonCanvas.setTitleSize((int)(48*1.5));
		comparisonCanvas.setPadTitles("CLARA Reconstruction Scaling");
		comparisonCanvas.setAxisLabelSize((int)(24*1.5));
		comparisonCanvas.setAxisTitleSize((int)(32*1.5));
		comparisonCanvas.setStatBoxFontSize((int)(24*1.5));
		
		EmbeddedCanvas rateCanvas = new EmbeddedCanvas();
		DataVector ratey1 = broadwell_affinity.getDataVector("ab", "");
		DataVector ratex1 = broadwell_affinity.getDataVector("aa", "");
		GraphErrors rate1 = new GraphErrors("broadwell_affinity",ratex1.getArray(),ratey1.getArray());
		ratey1 = broadwell.getDataVector("ab", "");
		ratex1 = broadwell.getDataVector("aa", "");
		GraphErrors rate2 = new GraphErrors("broadwell",ratex1.getArray(),ratey1.getArray());
		ratey1 = haswell.getDataVector("ab", "");
		ratex1 = haswell.getDataVector("aa", "");
		GraphErrors rate3 = new GraphErrors("broadwell_affinity",ratex1.getArray(),ratey1.getArray());
		
		rate1.setMarkerColor(1);
		rate2.setMarkerColor(4);
		rate3.setMarkerColor(2);
		rate1.setTitleX("Number of Threads");
		rate2.setTitleY("Event Rate [kHz]");
		rateCanvas.draw(rate1);
		rateCanvas.draw(rate2,"same");
		rateCanvas.draw(rate3,"same");

		rateCanvas.setTitleSize((int)(48*1.5));
		rateCanvas.setPadTitles("Event Rate");
		rateCanvas.setAxisLabelSize((int)(24*1.5));
		rateCanvas.setAxisTitleSize((int)(32*1.5));
		rateCanvas.setStatBoxFontSize((int)(24*1.5));

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add(comparisonCanvas,"Scaling All");
		tabbedPane.add(rateCanvas,"Rate");
		frame.add(tabbedPane);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
	}
}
