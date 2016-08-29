package org.jlab.groot.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jlab.groot.data.IDataSet;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.IDataSetPlotter;
import org.jlab.groot.math.Func1D;
import org.root.basic.EmbeddedPad;
import org.root.func.F1D;
import org.root.ui.RangeSlider;

public class FitPanel extends JPanel {
	EmbeddedCanvas canvas;
	int index;
	double xMin, xMax;
	ArrayList<IDataSet> datasets = new ArrayList<IDataSet>();
	ArrayList<String> dataSetNames = new ArrayList<String>();
	IDataSet currentDataset = null;
	String predefFunctions[] = {"gaus", "gaus+p0", "gaus+p1", "gaus+p2", "gaus+p3","landau","landau+p0","landau+p1","landau+p2","landau+p3","p0","p1","p2","p3","erf","exp"};
	JComboBox predefinedFunctionsSelector;
	JPanel fitSettings,fitSwapSettings, fitFunctionPanel, lowerWindow;
	Func1D fitFunction;


	FitPanel(EmbeddedCanvas canvas, int index){
		this.canvas = canvas;
		this.index = index;	
		
		initUI();
	}
	
	private void initUI() {
		xMin = canvas.getPad(index).getAxisX().getMin();
		xMax = canvas.getPad(index).getAxisX().getMax();
		for(IDataSetPlotter plotter : canvas.getPad(index).getDatasetPlotters()){
			datasets.add(plotter.getDataSet());
			dataSetNames.add(plotter.getDataSet().getName());
		}
		JComboBox dataSetBox = new JComboBox(dataSetNames.toArray());
		dataSetBox.setSelectedIndex(0);
		currentDataset = datasets.get(0);
		dataSetBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				currentDataset = datasets.get(dataSetBox.getSelectedIndex());
			}
		});
		predefinedFunctionsSelector = new JComboBox(predefFunctions);
		predefinedFunctionsSelector.setSelectedIndex(0);
		JLabel labelForFunction = new JLabel("Function:");
		JLabel dataSetLabel = new JLabel("Select Dataset:");
		fitFunctionPanel.add(dataSetLabel);
		fitFunctionPanel.add(dataSetBox);
		fitFunctionPanel.add(labelForFunction);
		fitFunctionPanel.add(predefinedFunctionsSelector);
		this.add(fitFunctionPanel,BorderLayout.PAGE_START);
	}
	
	private void initRangeSelector() {
		lowerWindow = new JPanel(new GridLayout(2, 1));
		JPanel rangeSelector = new JPanel();
		JLabel xLabel = new JLabel("X:");
		RangeSlider slider = new RangeSlider();
		slider.setMinimum((int) xSliderMin);
		slider.setMaximum((int) xSliderMax);
		slider.setValue((int) xSliderMin);
		slider.setUpperValue((int) xSliderMax);

		currentRangeMin = slider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin) + xMin;
		currentRangeMax = slider.getUpperValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin) + xMin;
	    JLabel rangeSliderValue1 = new JLabel(""+String.format("%4.2f",currentRangeMin));
	    JLabel rangeSliderValue2 = new JLabel(""+String.format("%4.2f",currentRangeMax));
		fitFunction.setRange(currentRangeMin, currentRangeMax);
		
		rangeSelector.add(xLabel);
	    rangeSelector.add(rangeSliderValue1);
		rangeSelector.add(slider);
	    rangeSelector.add(rangeSliderValue2);
		lowerWindow.add(rangeSelector);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				currentRangeMin = slider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin) + xMin;
				currentRangeMax = slider.getUpperValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin) + xMin;
				rangeSliderValue1.setText(String.valueOf(""+String.format("%4.2f",currentRangeMin)));
				rangeSliderValue2.setText(String.valueOf(""+String.format("%4.2f",currentRangeMax)));
				//System.out.println("currentRangeMin:"+currentRangeMin+" xOffset:"+xOffset);
				//fitFunction.setRange(currentRangeMin, currentRangeMax);
			}
		});

		JButton fit = new JButton("Fit");
		fit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Construct options
				int method = paramEstimationMethods.getSelectedIndex();
				if(method==0){
					options = "ER";
				}else if(method==1){
					options = "NR";
				}else if(method==2){
					options = "PR";
				}else if(method==3){
					options ="R";
				}else if(method==4){
					options ="LR";
				}
				String drawOption = "";
				for(int i=0; i<optionCheckBoxes.size(); i++){
					if(optionCheckBoxes.get(0).isSelected()){
						drawOption = "S";
					}
					if(optionCheckBoxes.get(1).isSelected()){
						options = options+"Q";
					}
				}

				fitFunction.setRange(currentRangeMin, currentRangeMax);
				//histogram.fit(fitFunction,options);
				if(predef&&!parameterPanel.modified()){
				for(int i=0; i<fitFunction.getNParams(); i++){
					if(i==0){
						fitFunction.setParameter(0,getMaxYIDataSet(currentDataset,currentRangeMin, currentRangeMax));
					}else if(i==1){
						fitFunction.setParameter(1,getMeanIDataSet(currentDataset,currentRangeMin, currentRangeMax));
					}else if(i==2){
						fitFunction.setParameter(2,getRMSIDataSet(currentDataset,currentRangeMin, currentRangeMax));
					}else if(i==3){
						fitFunction.setParameter(3,getAverageHeightIDataSet(currentDataset,currentRangeMin, currentRangeMax));
					}else if(i>3){
						fitFunction.setParameter(i, 1.0);
					}
					System.out.println("Paramter "+i+" ="+fitFunction.getParameter(i));

				}
				}
				DataFitter.fit(currentDataset, fitFunction,options);
				//fitFunction.show(); // print on the screen fit results
				fitFunction.setLineColor(2);
				fitFunction.setLineWidth(5);
				fitFunction.setLineStyle(1);
				canvas.cd(index);                                
				canvas.draw(fitFunction,"same"+drawOption);
				/*
				for(int i=0; i<canvas.getPad(index).getDataSetCount(); i++){
					System.out.println("Dataset#:"+i);
					Enumeration<?> blah = canvas.getPad(index).getDataSet(i).getAttributes().getProperties().propertyNames();
					while(blah.hasMoreElements()){
						System.out.println(blah.nextElement());
					}
				}*/
				
				/*
				ArrayList<IDataSet> nonDuplicateDataset = new ArrayList<IDataSet>();
				ArrayList<IDataSet> datasets1 = new ArrayList<IDataSet>();
				for(int i=0; i<canvas.getPad(index).getDataSetCount(); i++){
					datasets1.add(canvas.getPad(index).getDataSet(i));
					if(!nonDuplicateDataset.contains(datasets1.get(i))){
						nonDuplicateDataset.add(datasets1.get(i));
					}
				}
				canvas.getPad(index).
				
				*/
				parameterPanel.updateNewFunction(fitFunction);
			}
		});
		ArrayList<EmbeddedPad> canvasPads = canvas.canvasPads;
		JPanel fitButtons = new JPanel(new GridLayout(1,2));
        
		if(canvasPads.size()>1){
			
			JButton fitAll = new JButton("Fit All");
			fitAll.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Construct options
				int method = paramEstimationMethods.getSelectedIndex();
				if(method==0){
					options = "ER";
				}else if(method==1){
					options = "NR";
				}else if(method==2){
					options = "PR";
				}else if(method==3){
					options ="R";
				}else if(method==4){
					options ="LR";
				}
				String drawOption = "";
				//System.out.println("******************BLAH "+optionCheckBoxes.size());
				for(int i=0; i<optionCheckBoxes.size(); i++){
					//if(optionCheckBoxes.get(0).isSelected()&&hasDrawnStats==false){
					if(optionCheckBoxes.get(0).isSelected()){
						drawOption = "S";
						//hasDrawnStats = true;
					//	System.out.println("Draw stats!");
					}
					if(optionCheckBoxes.get(1).isSelected()){
						options = options+"Q";
						//System.out.println("Draw quietly!");
					}
					//System.out.println("Options: "+optionCheckBoxes.get(i).getName()+ " is "+optionCheckBoxes.get(i).isSelected());
				}
				//System.out.println("******************BLAH2");
				//System.out.println("Fit Options:["+options+"]");
				for(int padCounter=0; padCounter<canvasPads.size();padCounter++){
					double min  = canvas.getPad(padCounter).getAxisX().getMin();
					double max = canvas.getPad(padCounter).getAxisX().getMax();
					if(fitAllFitFunctions.size()!=canvasPads.size()){
						fitAllFitFunctions.add(new F1D(predefFunctions[predefinedFunctionsSelector.getSelectedIndex()], min, max));
					}else{
						fitAllFitFunctions.get(padCounter).initFunction(predefFunctions[predefinedFunctionsSelector.getSelectedIndex()], min, max);
					}
					ArrayList<IDataSet> tempDataset = new ArrayList<IDataSet>();
					int ndataset = canvas.getPad(padCounter).getDataSetCount();
					for(int i = 0; i < ndataset; i++){
						IDataSet ds = canvas.getPad(padCounter).getDataSet(i);
						String name = ds.getName();
						dataSetNames.add(name);
						tempDataset.add(ds);
					}
					IDataSet currentDataset = tempDataset.get(0);
					fitAllFitFunctions.get(padCounter).setRange(min, max);
				
				//histogram.fit(fitFunction,options);
				if(predef){
				for(int i=0; i<fitAllFitFunctions.get(padCounter).getNParams(); i++){
					if(i==0){
						fitAllFitFunctions.get(padCounter).setParameter(0,getMaxYIDataSet(currentDataset,min, max));
					}else if(i==1){
						fitAllFitFunctions.get(padCounter).setParameter(1,getMeanIDataSet(currentDataset,min, max));
					}else if(i==2){
						fitAllFitFunctions.get(padCounter).setParameter(2,getRMSIDataSet(currentDataset,min, max));
					}else if(i==3){
						fitAllFitFunctions.get(padCounter).setParameter(3,getAverageHeightIDataSet(currentDataset,min, max));
					}else if(i>3){
						fitAllFitFunctions.get(padCounter).setParameter(i, 1.0);
					}
					//System.out.println("Paramter "+i+" ="+fitFunction.getParameter(i));

				}}
				fitter.fit(currentDataset, fitAllFitFunctions.get(padCounter),options);
				//fitFunction.show(); // print on the screen fit results
				fitAllFitFunctions.get(padCounter).setLineColor(2);
				fitAllFitFunctions.get(padCounter).setLineWidth(5);
				fitAllFitFunctions.get(padCounter).setLineStyle(1);
				canvas.cd(padCounter);                                
				canvas.draw(fitAllFitFunctions.get(padCounter),"same"+drawOption);
				//parameterPanel.updateNewFunction(fitFunction);
				}
			}
		});
		fitButtons.add(fit);
		fitButtons.add(fitAll);
		lowerWindow.add(fitButtons);
		//lowerWindow.add(button);
		this.add(lowerWindow,BorderLayout.PAGE_END);
		}else{
			lowerWindow.add(fit);
			this.add(lowerWindow,BorderLayout.PAGE_END);
		}
		
	}
}
