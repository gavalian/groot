package org.jlab.groot.fitter;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.EmbeddedCanvasGroup;
import org.jlab.groot.math.Axis;
import org.jlab.groot.math.F1D;
import org.jlab.groot.ui.ProgressBar;

public class ParallelSliceFitter {
	H2F originalhistogram = null;
	H2F fithistogram = null;
	int nthreads = Runtime.getRuntime().availableProcessors();
	ArrayList<H1F> slices = null;
	ArrayList<FitThread> threads = new ArrayList<FitThread>();
	List<FitResults> fitResults = Collections.synchronizedList(new ArrayList<FitResults>());
	double min = 0.0;
	double max = 0.0;
	int minBin = 0;
	int maxBin = 0;
	double maxChiSquare = Double.MAX_VALUE;
	int minEvents = 10;
	boolean autorangemin = true;
	boolean autorangemax = true;
	boolean autorangeminbin = true;
	boolean autorangemaxbin = true;
	boolean showProgress = true;

	String fitMode = "RN";
	public String getFitMode() {
		return fitMode;
	}

	public void setFitMode(String fitMode) {
		this.fitMode = fitMode;
	}

	Axis axis = null;
	String[] modes = {"[amp]*gaus(x,[mean],[sigma])", "[amp]*gaus(x,[mean],[sigma])+[p0]",
			"[amp]*gaus(x,[mean],[sigma])+[p0]+x*[p1]", "[amp]*gaus(x,[mean],[sigma])+[p0]+x*[p1]+x*x*[p2]",
			"[amp]*gaus(x,[mean],[sigma])+[p0]+x*[p1]+x*x*[p2]+x*x*x[p3]",};
	public static final int NO_BG = -1;
	public static final int P0_BG = 0;
	public static final int P1_BG = 1;
	public static final int P2_BG = 2;
	public static final int P3_BG = 3;
	int mode = 0;

	public H2F getHistogram() {
		return fithistogram;
	}

	public void setHistogram(H2F histogram) {
		this.originalhistogram = histogram;
		this.fithistogram = histogram;
	}

	public int getNthreads() {
		return nthreads;
	}

	public void setNthreads(int nthreads) {
		this.nthreads = nthreads;
	}

	public void setBackgroundOrder(int mode) {
		this.mode = mode + 1;
	}

	public ParallelSliceFitter() {
	}

	public ParallelSliceFitter(H2F histogram) {
		this.originalhistogram = histogram;
		this.fithistogram = histogram;
	}

	public void fitSlicesY(){
		fitSlicesY(1);
	}
	public void fitSlicesY(int nbins) {
		fithistogram = originalhistogram.rebinY(nbins);
		slices = fithistogram.getSlicesY();
		axis = fithistogram.getYAxis();
		if (autorangemin && slices.size() > 0) {
			min = slices.get(0).getxAxis().min();
		}
		if (autorangemax && slices.size() > 0) {
			max = slices.get(0).getxAxis().max();
		}
		if (autorangeminbin && slices.size() > 0) {
			minBin = 0;
		}
		if (autorangemaxbin) {
			maxBin = slices.size();
		}
		fit();
	}
	
	public void fitSlicesX(){
		fitSlicesX(1);
	}

	public void fitSlicesX(int nbins) {
		fithistogram = originalhistogram.rebinX(nbins);
		slices = fithistogram.getSlicesX();
		axis = fithistogram.getXAxis();
		if (autorangemin && slices.size() > 0) {
			min = slices.get(0).getxAxis().min();
		}
		if (autorangemax && slices.size() > 0) {
			max = slices.get(0).getxAxis().max();
		}
		if (autorangeminbin && slices.size() > 0) {
			minBin = 0;
		}
		if (autorangemaxbin ) {
			maxBin = slices.size();
		}
		fit();
	}

	public GraphErrors getChi2Slices() {
		GraphErrors graph = new GraphErrors();
		for (FitResults result : fitResults) {
			if (result.getChiSq() < this.maxChiSquare && result.getData().getIntegral() > this.minEvents) {
				graph.addPoint(result.getPoint(), result.getChiSq(), 0, 0);
				graph.setTitleX(this.slices.get(0).getTitleX());
				graph.setTitleY("#chi^2/ndf from SliceFitter");
			}
		}
		return graph;
	}

	public GraphErrors getMeanSlices() {
		GraphErrors graph = new GraphErrors();
		for (FitResults result : fitResults) {
			if (result.getChiSq() < this.maxChiSquare && result.getData().getIntegral() > this.minEvents) {
				graph.addPoint(result.getPoint(), result.getFunction().getParameter(1), 0,
						result.getFunction().parameter(1).error());
				graph.setTitleX(this.slices.get(0).getTitleX());
				graph.setTitleY("Gaussian Mean from SliceFitter");
			}
		}
		return graph;
	}

	public GraphErrors getSigmaSlices() {
		GraphErrors graph = new GraphErrors();
		for (FitResults result : fitResults) {
			if (result.getChiSq() < this.maxChiSquare && result.getData().getIntegral() > this.minEvents) {
				graph.addPoint(result.getPoint(), result.getFunction().getParameter(2), 0,
						result.getFunction().parameter(2).error());
				graph.setTitleX(this.slices.get(0).getTitleX());
				graph.setTitleY("Gaussian #sigma from SliceFitter");
			}
		}
		return graph;
	}

	public GraphErrors getAmpSlices() {
		GraphErrors graph = new GraphErrors();
		for (FitResults result : fitResults) {
			if (result.getChiSq() < this.maxChiSquare && result.getData().getIntegral() > this.minEvents) {
				graph.addPoint(result.getPoint(), result.getFunction().getParameter(0), 0,
						result.getFunction().parameter(0).error());
				graph.setTitleX(this.slices.get(0).getTitleX());
				graph.setTitleY("Gaussian Amplitude from SliceFitter");
			}
		}
		return graph;
	}

	public GraphErrors getP0Slices() {
		GraphErrors graph = new GraphErrors();
		for (FitResults result : fitResults) {
			if (result.getChiSq() < this.maxChiSquare && result.getData().getIntegral() > this.minEvents) {
				graph.addPoint(result.getPoint(), result.getFunction().getParameter(3), 0,
						result.getFunction().parameter(3).error());
				graph.setTitleX(this.slices.get(0).getTitleX());
				graph.setTitleY("Background P0 from SliceFitter");
			}
		}
		return graph;
	}

	public GraphErrors getP1Slices() {
		GraphErrors graph = new GraphErrors();
		for (FitResults result : fitResults) {
			if (result.getChiSq() < this.maxChiSquare && result.getData().getIntegral() > this.minEvents) {
				graph.addPoint(result.getPoint(), result.getFunction().getParameter(4), 0,
						result.getFunction().parameter(4).error());
				graph.setTitleX(this.slices.get(0).getTitleX());
				graph.setTitleY("Background P1 from SliceFitter");
			}
		}
		return graph;
	}

	public GraphErrors getP2Slices() {
		GraphErrors graph = new GraphErrors();
		for (FitResults result : fitResults) {
			if (result.getChiSq() < this.maxChiSquare && result.getData().getIntegral() > this.minEvents) {
				graph.addPoint(result.getPoint(), result.getFunction().getParameter(5), 0,
						result.getFunction().parameter(5).error());
				graph.setTitleX(this.slices.get(0).getTitleX());
				graph.setTitleY("Background P2 from SliceFitter");
			}
		}
		return graph;
	}

	public GraphErrors getP3Slices() {
		GraphErrors graph = new GraphErrors();
		for (FitResults result : fitResults) {
			if (result.getChiSq() < this.maxChiSquare && result.getData().getIntegral() > this.minEvents) {
				graph.addPoint(result.getPoint(), result.getFunction().getParameter(6), 0,
						result.getFunction().parameter(6).error());
				graph.setTitleX(this.slices.get(0).getTitleX());
				graph.setTitleY("Background P3 from SliceFitter");
			}
		}
		return graph;
	}

	public void setMin(double min) {
		this.min = min;
		this.autorangemin = false;
	}

	public void setMax(double max) {
		this.max = max;
		this.autorangemax = false;
	}

	public void setRange(double min, double max) {
		this.min = min;
		this.max = max;
		this.autorangemin = false;
		this.autorangemax = false;
	}

	public void setMinBin(int min) {
		this.minBin = min;
		this.autorangeminbin = false;
	}

	public void setMaxBin(int max) {
		this.maxBin = max;
		this.autorangemaxbin = false;
	}

	public void setBinRange(int min, int max) {
		this.minBin = min;
		this.maxBin = max;
		this.autorangemin = false;
		this.autorangemax = false;
	}

	public double getMaxChiSquare() {
		return maxChiSquare;
	}

	public void setMaxChiSquare(double maxChiSquare) {
		this.maxChiSquare = maxChiSquare;
	}

	public int getMinEvents() {
		return minEvents;
	}

	public void setMinEvents(int minEvents) {
		this.minEvents = minEvents;
	}

	public boolean isShowProgress() {
		return showProgress;
	}

	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}

	public void inspectFits() {
		JFrame frame = new JFrame("Fit Inspector");
		frame.add(getInspectFitsPane());
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.pack();
		frame.setSize((int) (screensize.getHeight() * .75 * 1.618), (int) (screensize.getHeight() * .75));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public JTabbedPane getInspectFitsPane() {
		int nHistograms = slices.size();
		int cols = 4;
		int rows = 4;
		// int pages = nHistograms/(cols*rows) +
		// ((nHistograms%(cols*rows)==0)?0:1);
		int pages = nHistograms / (cols * rows);
		if (nHistograms - pages * cols * rows > 0)
			pages++;

		JTabbedPane pane = new JTabbedPane();
		EmbeddedCanvas fitSummary = new EmbeddedCanvas();
		int nPlots = mode + 5;
		fitSummary.divide(3, 2);
		if (nPlots > 6) {
			fitSummary.divide(3, 3);
		} else if (nPlots > 9) {
			fitSummary.divide(4, 3);
		} else if (nPlots > 12) {
			fitSummary.divide(4, 4);
		}
		int counter = 0;
		fitSummary.cd(counter++);
		fitSummary.draw(this.fithistogram);
		fitSummary.cd(counter++);
		fitSummary.draw(this.getMeanSlices());
		fitSummary.cd(counter++);
		fitSummary.draw(this.getSigmaSlices());
		fitSummary.cd(counter++);
		fitSummary.draw(this.getAmpSlices());
		if (mode > 0) {
			fitSummary.cd(counter++);
			fitSummary.draw(this.getP0Slices());
			if (mode > 1) {
				fitSummary.cd(counter++);
				fitSummary.draw(this.getP1Slices());
			}
			if (mode > 2) {
				fitSummary.cd(counter++);
				fitSummary.draw(this.getP2Slices());
			}
			if (mode > 3) {
				fitSummary.cd(counter++);
				fitSummary.draw(this.getP3Slices());
			}
		}
		fitSummary.cd(counter++);
		fitSummary.draw(this.getChi2Slices());
		pane.addTab("Summary", fitSummary);
		EmbeddedCanvasGroup group = new EmbeddedCanvasGroup();
		for(int i=0; i<slices.size();i++){
			slices.get(i).setTitle("Slice#:"+i+" Bin Center:"+String.format("%4.2f",this.axis.getBinCenter(i)));
		}
		ArrayList<IDataSet> data = new ArrayList<IDataSet>(slices);
		group.setData(data);
		pane.add("Individual Fits", group);
		return pane;
	}

	private void fit() {
		List<Callable<Object>> taskList = new ArrayList<Callable<Object>>(slices.size());
		ArrayList<Future<Object>> tasks = new ArrayList<Future<Object>>();
		for (int i = 0; i < slices.size(); i++) {
			System.out.println(i+" "+minBin+" "+maxBin);
			if (i >= minBin && i < maxBin) {
				H1F slice = slices.get(i);
				if (slice.getIntegral() > minEvents) {
					ResultSetter setter = new ResultSetter() {
						public void setResult(H1F histogram, F1D fit, int fitNumber) {
							FitResults result = new FitResults();
							result.setData(histogram);
							result.setFunction(fit);
							result.setPoint(axis.getBinCenter(fitNumber));
							if ((fit.getChiSquare() / (double) fit.getNDF()) < maxChiSquare) {
								fitResults.add(result);
							}
						}
					};
					FitThread temp = new FitThread();
					temp.setN(i);
					temp.setHistogram(slice);
					temp.setResultSetter(setter);
					temp.setOptions( this.fitMode);
					temp.setFitter(new F1D("f1", modes[mode], min, max));
					threads.add(temp);
				}
			}
		}

		ExecutorService threadPool = Executors.newFixedThreadPool(nthreads);
		for (FitThread thread : threads) {
			taskList.add(Executors.callable(thread));
			@SuppressWarnings("unchecked")
			Future<Object> x = (Future<Object>) threadPool.submit(thread);
			tasks.add(x);
		}

		for (FitThread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		awaitTerminationAfterShutdown(threadPool);
		fitResults.sort(Comparator.comparing(FitResults::getPoint));
	}
	
        public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
	    threadPool.shutdown();
	    try {
	        if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
	            threadPool.shutdownNow();
	        }
	    } catch (InterruptedException ex) {
	        threadPool.shutdownNow();
	        Thread.currentThread().interrupt();
	    }
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Basic GROOT Demo");
		EmbeddedCanvas canvas = new EmbeddedCanvas();
		canvas.divide(2, 2);
		frame.setSize(800, 500);
		Random rand = new Random();
		H2F histogram2d = new H2F("h2", "", 1000, 0, 4, 1000, 0, 4);
		for (int i = 0; i < 8000000; i++) {
			double x = 1.5 + rand.nextGaussian();
			double y = 2.5 + 2 * rand.nextGaussian();
			histogram2d.fill(x, y);
		}
		histogram2d.setTitleX("Randomly Generated Function");
		histogram2d.setTitleY("Randomly Generated Function");
		canvas.getPad(0).setTitle("Histogram2D Demo");
		canvas.draw(histogram2d);
		canvas.setFont("HanziPen TC");
		canvas.setTitleSize(32);
		canvas.setAxisTitleSize(24);
		canvas.setAxisLabelSize(18);
		canvas.setStatBoxFontSize(18);

		ParallelSliceFitter fitter = new ParallelSliceFitter(histogram2d);
		fitter.fitSlicesY();
		GraphErrors meanY = fitter.getMeanSlices();
		fitter.fitSlicesX();
		GraphErrors meanX = fitter.getMeanSlices();
		canvas.cd(1);
		canvas.draw(meanY);
		canvas.cd(2);
		canvas.draw(meanX);
		frame.add(canvas);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private class FitResults {
		H1F data = null;
		F1D function = null;

		public H1F getData() {
			return data;
		}
		public void setData(H1F data) {
			this.data = data;
		}
		public F1D getFunction() {
			return function;
		}
		public void setFunction(F1D function) {
			this.function = function;
		}
		public double getPoint() {
			return point;
		}
		public void setPoint(double point) {
			this.point = point;
		}
		public double getChiSq() {
			return this.function.getChiSquare() / (double) this.function.getNDF();
		}
		double point = 0.0;
	}

	private class FitThread extends Thread {
		F1D fitFunction = null;
		private ResultSetter setter;
		private H1F histogram;
		private int n = 0;
		private String options = "NRQ";
		private double[] parameters;

		public void setFitter(F1D fitFunction) {
			this.fitFunction = fitFunction;
		}
		public void setResultSetter(ResultSetter setter) {
			this.setter = setter;
		}

		public void setParameters(double[] params) {
			this.fitFunction.setParameters(params);
		}
		public void setN(int n) {
			this.n = n;
		}
		public void setOptions(String options) {
			this.options = options;
		}
		public void setHistogram(H1F histogram) {
			this.histogram = histogram;
		}

		public void run() {
			IDataSet currentDataset = histogram;
			double currentRangeMin = fitFunction.getMin();
			double currentRangeMax = fitFunction.getMax();

			for (int i = 0; i < fitFunction.getNPars(); i++) {
				if (i == 0) {
					fitFunction.setParameter(0, getMaxYIDataSet(currentDataset, currentRangeMin, currentRangeMax));
				} else if (i == 1) {
					fitFunction.setParameter(1, getMeanIDataSet(currentDataset, currentRangeMin, currentRangeMax));
				} else if (i == 2) {
					fitFunction.setParameter(2, getRMSIDataSet(currentDataset, currentRangeMin, currentRangeMax));
					fitFunction.setParLimits(2, 0.0, Double.MAX_VALUE);
				} else if (i == 3) {
					fitFunction.setParameter(3,
							getAverageHeightIDataSet(currentDataset, currentRangeMin, currentRangeMax));
				} else if (i > 3) {
					fitFunction.setParameter(i, 1.0);
				}
				// System.out.println("Paramter " + i + " =" +
				// fitFunction.getParameter(i));
			}
			DataFitter.fit(fitFunction, currentDataset, options);
			setter.setResult(histogram, fitFunction, n);
		}

		public int getN() {
			return n;
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

	public interface ResultSetter {
		public void setResult(H1F histogram, F1D fit, int fitNumber);
	}
}
