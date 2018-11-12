package org.jlab.groot.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jlab.groot.data.H1F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.EmbeddedPad;
import org.jlab.groot.graphics.IDataSetPlotter;
import org.jlab.groot.math.F1D;
import org.jlab.groot.math.Func1D;
import org.jlab.groot.ui.ParameterPanel;

public class FitPanel extends JPanel {
	EmbeddedCanvas canvas;
	int index;
	// double xMin, xMax;
	ArrayList<IDataSet> datasets = new ArrayList<IDataSet>();
	ArrayList<String> dataSetNames = new ArrayList<String>();
	IDataSet currentDataset = null;
	ArrayList<F1D> fitAllFitFunctions = new ArrayList<F1D>();
	JComboBox paramEstimationMethods;
	String predefFunctions[] = {"gaus", "gaus+p0", "gaus+p1", "gaus+p2", "gaus+p3", "p0", "p1", "p2", "p3", "exp",
			"a+bcos", "a+bcos+ccos2", "amdahl"};

	String predefFunctionsF1D[] = {"[amp]*gaus(x,[mean],[sigma])", "[amp]*gaus(x,[mean],[sigma])+[p0]",
			"[amp]*gaus(x,[mean],[sigma])+[p0]+x*[p1]", "[amp]*gaus(x,[mean],[sigma])+[p0]+x*[p1]+x*x*[p2]",
			"[amp]*gaus(x,[mean],[sigma])+[p0]+x*[p1]+x*x*[p2]+x*x*x*[p3]", "[p0]", "[p0]+[p1]*x", "[p0]+[p1]*x+[p2]*x*x",
			"[p0]+[p1]*x+[p2]*x*x+[p3]*x*x*x", "[a]*exp(x,[b])", "[a]+[b]*cos(x*[c])",
			"[a]+[b]*cos(x*[d])+[c]*cos(2*x*[e])", "1/((1-[p])+[p]/x)"};

	boolean predef = true;
	JComboBox predefinedFunctionsSelector;
	JPanel fitSettings, fitSwapSettings, fitFunctionPanel, lowerWindow;
	Func1D fitFunction;
	boolean parameterPanelSwapped = false;
	boolean fitSettingsSwapped = false;
	JPanel blankPanel = new JPanel();
	ArrayList<JCheckBox> optionCheckBoxes;
	double xMin = 0.0;
	double xMax = 100.0;
	String options = "";
	// This mess is due to the slider only working with integer values
	int xSliderMin = 0;
	int xSliderMax = 1000000;
	double currentRangeMin = 0.0;
	double currentRangeMax = 100.0;
	ParameterPanel parameterPanel;

	public FitPanel(EmbeddedCanvas canvas, int index) {
		this.canvas = canvas;
		this.index = index;
		this.fitFunction = new F1D("f1", predefFunctionsF1D[0], xMin, xMax);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		int gridy = 0;
		c.gridy = gridy++;
		this.add(initFitFunction(), c);
		c.gridy = gridy++;
		this.add(initFitSettings(), c);
		c.gridy = gridy++;
		this.add(initRangeSelector(), c);
		if (currentDataset != null) {
			if (currentDataset instanceof H1F) {
				this.paramEstimationMethods.setSelectedIndex(1);
			}
		}
	}
	JTabbedPane initFitSettings() {

		JPanel fitMethod = new JPanel(new FlowLayout());
		String labs[] = {"Chi-square", "Chi-square (Neyman)", "Chi-square (Pearson)", "Chi-square (All weights=1)",
				"Binned Extended-MLE"};
		paramEstimationMethods = new JComboBox(labs);
		/*
		 * paramEstimationMethods.addActionListener(new ActionListener() {
		 * public void actionPerformed(ActionEvent e) {
		 * if(paramEstimationMethods.getSelectedIndex()==0){ options = "NRQ";
		 * System.out.print("Option:"+options); }else{ options = "LRQ";
		 * System.out.print("Option:"+options); }
		 * 
		 * } });
		 */

		paramEstimationMethods.setSelectedIndex(0);
		fitMethod.add(new JLabel("Method:"));
		fitMethod.add(paramEstimationMethods);

		JPanel fitOptions = new JPanel(new GridLayout(1, 2));
		String[] options = {"Draw Stats", "Quiet"};
		optionCheckBoxes = new ArrayList<JCheckBox>();
		for (int i = 0; i < options.length; i++) {
			optionCheckBoxes.add(new JCheckBox(options[i]));
			fitOptions.add(optionCheckBoxes.get(i));
		}

		JTabbedPane tabbedPane = new JTabbedPane();
		fitSettings = new JPanel(new GridLayout(2, 1));
		fitSettings.add(fitMethod);
		fitSettings.add(fitOptions);
		parameterPanel = new ParameterPanel(this.canvas, this.index, this.fitFunction);
		tabbedPane.add("Minimizer Settings", fitSettings);
		tabbedPane.add("Parameter Settings", blankPanel);

		tabbedPane.setBorder(new TitledBorder("Minimizer Settings"));
		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (tabbedPane.getSelectedIndex() == 0) {
					tabbedPane.setComponentAt(1, blankPanel);

				}
				if (tabbedPane.getSelectedIndex() == 1) {
					tabbedPane.setComponentAt(1, parameterPanel);
				}
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(tabbedPane);
				topFrame.pack();
			}
		});

		// this.add(tabbedPane, BorderLayout.CENTER);
		return tabbedPane;
		// System.out.println("YES THIS IS THE CORRECT FILE");
	}

	private JPanel initFitFunction() {
		fitFunctionPanel = new JPanel(new GridLayout(2, 1));
		fitFunctionPanel.setBorder(new TitledBorder("Function Settings"));
		xMin = canvas.getPad(index).getAxisX().getMin();
		xMax = canvas.getPad(index).getAxisX().getMax();
		for (IDataSetPlotter plotter : canvas.getPad(index).getDatasetPlotters()) {
			datasets.add(plotter.getDataSet());
			dataSetNames.add(plotter.getDataSet().getName());
		}
		JComboBox dataSetBox = new JComboBox(dataSetNames.toArray());
		dataSetBox.setSelectedIndex(0);
		currentDataset = datasets.get(0);

		dataSetBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentDataset = datasets.get(dataSetBox.getSelectedIndex());
			}
		});
		predefinedFunctionsSelector = new JComboBox(predefFunctions);
		predefinedFunctionsSelector.setSelectedIndex(0);
		parameterPanel = new ParameterPanel(this.canvas, this.index, this.fitFunction);
		predefinedFunctionsSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println(predefinedFunctionsSelector.getSelectedIndex());
				// fitFunction = new
				// F1D(predefFunctions[predefinedFunctionsSelector.getSelectedIndex()],currentRangeMin,currentRangeMax);
				// fitFunction.setFunction(predefFunctions[predefinedFunctionsSelector.getSelectedIndex()]);
				boolean functionSetup = false;
				for (int i = 0; i < predefFunctions.length; i++) {
					if (predefFunctions[predefinedFunctionsSelector.getSelectedIndex()].equals(predefFunctions[i])) {
						fitFunction = new F1D("f1", predefFunctionsF1D[predefinedFunctionsSelector.getSelectedIndex()],
								currentRangeMin, currentRangeMax);
						functionSetup = true;
						predef = true;
					}
				}

				if (predef) {
					for (int i = 0; i < fitFunction.getNPars(); i++) {
						if (i == 0) {
							fitFunction.setParameter(0,
									getMaxYIDataSet(currentDataset, currentRangeMin, currentRangeMax));
						} else if (i == 1) {
							fitFunction.setParameter(1,
									getMeanIDataSet(currentDataset, currentRangeMin, currentRangeMax));
						} else if (i == 2) {
							fitFunction.setParameter(2,
									getRMSIDataSet(currentDataset, currentRangeMin, currentRangeMax));
							if (predefinedFunctionsSelector.getSelectedIndex() < 5)
								fitFunction.setParLimits(2, 0.0, Double.MAX_VALUE);
						} else if (i == 3) {
							fitFunction.setParameter(3,
									getAverageHeightIDataSet(currentDataset, currentRangeMin, currentRangeMax));
						} else if (i > 3) {
							fitFunction.setParameter(i, 1.0);
						}
						System.out.println("Paramter " + i + " =" + fitFunction.getParameter(i));
					}
				}
				parameterPanel.updateNewFunction(fitFunction);
				JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(predefinedFunctionsSelector);
				topFrame.pack();
			}
		});

		JLabel labelForFunction = new JLabel("Function:");
		JLabel dataSetLabel = new JLabel("Select Dataset:");
		fitFunctionPanel.add(dataSetLabel);
		fitFunctionPanel.add(dataSetBox);
		fitFunctionPanel.add(labelForFunction);
		fitFunctionPanel.add(predefinedFunctionsSelector);
		return fitFunctionPanel;
	}

	private JPanel initRangeSelector() {
		lowerWindow = new JPanel(new GridLayout(2, 1));
		JPanel rangeSelector = new JPanel();
		JLabel xLabel = new JLabel("X:");
		RangeSlider slider = new RangeSlider();
		slider.setMinimum((int) xSliderMin);
		slider.setMaximum((int) xSliderMax);
		slider.setValue((int) xSliderMin);
		slider.setUpperValue((int) xSliderMax);

		currentRangeMin = slider.getValue() * (xMax - xMin) / (double) (xSliderMax - xSliderMin) + xMin;
		currentRangeMax = slider.getUpperValue() * (xMax - xMin) / (double) (xSliderMax - xSliderMin) + xMin;
		JLabel rangeSliderValue1 = new JLabel("" + String.format("%4.2f", currentRangeMin));
		JLabel rangeSliderValue2 = new JLabel("" + String.format("%4.2f", currentRangeMax));
		fitFunction.setRange(currentRangeMin, currentRangeMax);

		rangeSelector.add(xLabel);
		rangeSelector.add(rangeSliderValue1);
		rangeSelector.add(slider);
		rangeSelector.add(rangeSliderValue2);
		lowerWindow.add(rangeSelector);

		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				currentRangeMin = slider.getValue() * (xMax - xMin) / (double) (xSliderMax - xSliderMin) + xMin;
				currentRangeMax = slider.getUpperValue() * (xMax - xMin) / (double) (xSliderMax - xSliderMin) + xMin;
				rangeSliderValue1.setText(String.valueOf("" + String.format("%4.2f", currentRangeMin)));
				rangeSliderValue2.setText(String.valueOf("" + String.format("%4.2f", currentRangeMax)));
				// System.out.println("currentRangeMin:"+currentRangeMin+"
				// xOffset:"+xOffset);
				// fitFunction.setRange(currentRangeMin, currentRangeMax);
			}
		});

		JButton fit = new JButton("Fit");
		fit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Construct options
				int method = paramEstimationMethods.getSelectedIndex();
				if (method == 0) {
					options = "ER";
				} else if (method == 1) {
					options = "NR";
				} else if (method == 2) {
					options = "PR";
				} else if (method == 3) {
					options = "R";
				} else if (method == 4) {
					options = "LR";
				}
				String drawOption = "";
				for (int i = 0; i < optionCheckBoxes.size(); i++) {
					if (optionCheckBoxes.get(0).isSelected()) {
						drawOption = "S";
					}
					if (optionCheckBoxes.get(1).isSelected()) {
						options = options + "Q";
					}
				}

				fitFunction.setRange(currentRangeMin, currentRangeMax);
				if (predef && !parameterPanel.modified()) {
					for (int i = 0; i < fitFunction.getNPars(); i++) {
						if (i == 0) {
							fitFunction.setParameter(0,
									getMaxYIDataSet(currentDataset, currentRangeMin, currentRangeMax));
						} else if (i == 1) {
							fitFunction.setParameter(1,
									getMeanIDataSet(currentDataset, currentRangeMin, currentRangeMax));
						} else if (i == 2) {
							fitFunction.setParameter(2,
									getRMSIDataSet(currentDataset, currentRangeMin, currentRangeMax));
						} else if (i == 3) {
							fitFunction.setParameter(3,
									getAverageHeightIDataSet(currentDataset, currentRangeMin, currentRangeMax));
						} else if (i > 3) {
							fitFunction.setParameter(i, 1.0);
						}
						System.out.println("Paramter " + i + " =" + fitFunction.getParameter(i));

					}
				}
				System.out.println("Fitting!");
				DataFitter.fit(fitFunction, currentDataset, options);
				fitFunction.show(); // print on the screen fit results
				fitFunction.setLineColor(2);
				fitFunction.setLineWidth(5);
				fitFunction.setLineStyle(1);
				canvas.cd(index);
				canvas.draw(fitFunction, "same"+drawOption);
				canvas.update();

				/*
				 * ArrayList<IDataSet> nonDuplicateDataset = new
				 * ArrayList<IDataSet>(); ArrayList<IDataSet> datasets1 = new
				 * ArrayList<IDataSet>(); for(int i=0;
				 * i<canvas.getPad(index).getDatasetPlotters(); i++){
				 * datasets1.add(canvas.getPad(index).getDataSet(i));
				 * if(!nonDuplicateDataset.contains(datasets1.get(i))){
				 * nonDuplicateDataset.add(datasets1.get(i)); } }
				 * canvas.getPad(index).
				 */

				parameterPanel.updateNewFunction(fitFunction);
			}
		});
		List<EmbeddedPad> canvasPads = canvas.getCanvasPads();
		JPanel fitButtons = new JPanel(new GridLayout(1, 2));

		if (canvasPads.size() > 1) {

			JButton fitAll = new JButton("Fit All");
			fitAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Construct options

					int method = paramEstimationMethods.getSelectedIndex();
					if (method == 0) {
						options = "ER";
					} else if (method == 1) {
						options = "NR";
					} else if (method == 2) {
						options = "PR";
					} else if (method == 3) {
						options = "R";
					} else if (method == 4) {
						options = "LR";
					}
					String drawOption = "";
					for (int i = 0; i < optionCheckBoxes.size(); i++) {
						if (optionCheckBoxes.get(0).isSelected()) {
							drawOption = "S";

						}
						if (optionCheckBoxes.get(1).isSelected()) {
							options = options + "Q";
						}
					}

					for (int padCounter = 0; padCounter < canvasPads.size(); padCounter++) {
						double min = canvas.getPad(padCounter).getAxisX().getMin();
						double max = canvas.getPad(padCounter).getAxisX().getMax();
						if (fitAllFitFunctions.size() != canvasPads.size()) {
							fitAllFitFunctions.add(new F1D("f1",
									predefFunctionsF1D[predefinedFunctionsSelector.getSelectedIndex()], min, max));
						} else {
							fitAllFitFunctions.set(padCounter, new F1D("f1",
									predefFunctionsF1D[predefinedFunctionsSelector.getSelectedIndex()], min, max));
						}
						ArrayList<IDataSet> tempDataset = new ArrayList<IDataSet>();
						int ndataset = canvas.getPad(padCounter).getDatasetPlotters().size();
						for (int i = 0; i < ndataset; i++) {
							IDataSet ds = canvas.getPad(padCounter).getDatasetPlotters().get(i).getDataSet();
							String name = ds.getName();
							dataSetNames.add(name);
							tempDataset.add(ds);
						}
						if (ndataset > 0) {
							IDataSet currentDataset = tempDataset.get(0);
							fitAllFitFunctions.get(padCounter).setRange(min, max);

							if (predef) {
								for (int i = 0; i < fitAllFitFunctions.get(padCounter).getNPars(); i++) {
									if (i == 0) {
										fitAllFitFunctions.get(padCounter).setParameter(0,
												getMaxYIDataSet(currentDataset, min, max));
									} else if (i == 1) {
										fitAllFitFunctions.get(padCounter).setParameter(1,
												getMeanIDataSet(currentDataset, min, max));
									} else if (i == 2) {
										fitAllFitFunctions.get(padCounter).setParameter(2,
												getRMSIDataSet(currentDataset, min, max));
										if (predefinedFunctionsSelector.getSelectedIndex() < 5)
											fitFunction.setParLimits(2, 0.0, Double.MAX_VALUE);
									} else if (i == 3) {
										fitAllFitFunctions.get(padCounter).setParameter(3,
												getAverageHeightIDataSet(currentDataset, min, max));
									} else if (i > 3) {
										fitAllFitFunctions.get(padCounter).setParameter(i, 1.0);
									}

								}
							}
							DataFitter.fit(fitAllFitFunctions.get(padCounter), currentDataset, options);
							fitAllFitFunctions.get(padCounter).setLineColor(2);
							fitAllFitFunctions.get(padCounter).setLineWidth(5);
							fitAllFitFunctions.get(padCounter).setLineStyle(1);
							canvas.cd(padCounter);
							canvas.draw(fitAllFitFunctions.get(padCounter), "same" + drawOption);
							
							canvas.update();
						}
					}

				}
			});
			fitButtons.add(fit);
			fitButtons.add(fitAll);
			lowerWindow.add(fitButtons);
		} else {
			lowerWindow.add(fit);
		}
		return lowerWindow;

	}
	private double getMeanIDataSet(IDataSet data, double min, double max) {
		int nsamples = 0;
		double sum = 0;
		double nEntries = 0;
		for (int i = 0; i < data.getDataSize(0); i++) {
			double x = data.getDataX(i);
			double y = data.getDataY(i);
			if (x > min && x < max && y != 0) {
				nsamples++;
				sum += x * y;
				nEntries += y;
			}
		}
		return sum / (double) nEntries;
	}

	private double getRMSIDataSet(IDataSet data, double min, double max) {
		int nsamples = 0;
		double mean = getMeanIDataSet(data, min, max);
		double sum = 0;
		double nEntries = 0;

		for (int i = 0; i < data.getDataSize(0); i++) {
			double x = data.getDataX(i);
			double y = data.getDataY(i);
			if (x > min && x < max && y != 0) {
				nsamples++;
				sum += Math.pow(x - mean, 2) * y;
				nEntries += y;
			}
		}
		return Math.sqrt(sum / (double) nEntries);
	}

	private double getAverageHeightIDataSet(IDataSet data, double min, double max) {
		int nsamples = 0;
		double sum = 0;
		for (int i = 0; i < data.getDataSize(0); i++) {
			double x = data.getDataX(i);
			double y = data.getDataY(i);
			if (x > min && x < max && y != 0) {
				nsamples++;
				sum += y;
			}
		}
		return sum / (double) nsamples;
	}

	private double getMaxXIDataSet(IDataSet data, double min, double max) {
		double max1 = 0;
		double xMax = 0;
		for (int i = 0; i < data.getDataSize(0); i++) {
			double x = data.getDataX(i);
			double y = data.getDataY(i);
			if (x > min && x < max && y != 0) {
				if (y > max1) {
					max1 = y;
					xMax = x;
				}
			}
		}
		return xMax;
	}
	private double getMaxYIDataSet(IDataSet data, double min, double max) {
		double max1 = 0;
		double xMax = 0;
		for (int i = 0; i < data.getDataSize(0); i++) {
			double x = data.getDataX(i);
			double y = data.getDataY(i);
			if (x > min && x < max && y != 0) {
				if (y > max1) {
					max1 = y;
					xMax = x;
				}
			}
		}
		return max1;
	}
}
