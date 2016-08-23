package org.jlab.groot.data;

import java.util.Map;
import java.util.TreeMap;
import org.jlab.groot.base.AttributeType;
import org.jlab.groot.base.Attributes;
import org.jlab.groot.base.DatasetAttributes;
import org.jlab.groot.math.Axis;
import org.jlab.groot.math.Func1D;
import org.jlab.groot.math.StatNumber;
import org.jlab.groot.ui.PaveText;



/**
 * Defines the class to create a basic 1D Histogram
 * 
 * @author Erin Kirby
 * @version 062614
 */
public class H1F  implements IDataSet {

    Axis  xAxis;
    Axis  yAxis;
    float[]   histogramData;
    float[]   histogramDataError;
    String     histTitle  = "";
    String     histXTitle = "";
    String     histYTitle = "";
    String     histName   = "";
    int        histogramUnderFlow = 0;
    int        histogramOverFlow  = 0;
    int        histogramEntries   = 0;
    DatasetAttributes hAttr     = new DatasetAttributes();
    
    Func1D     fittedFunction = null;
    
    /**
     * The default constructor, which creates a Histogram1D object with the Name "default", 
     * the Title "default", no Axis titles, and sets the minimum xAxis value to 0, the maximum x
     *  value to 1, and creates 1 bin. 
     */
    public H1F() {
    	setName("default");
    	setTitle("default");
    	set(1,0.0,1.0);
    	initDataStore(1);
        this.initAttributes();
    }
    
    /**
     * Creates a Histogram1D object using the specified name, axis titles, number of bins, 
     * and minimum and maximum x axis values.
     * 
     * @param hName		the desired name of the 1-D Histogram
     * @param xTitle	the desired x-axis title
     * @param yTitle	the desired y-axis title
     * @param bins		the desired number of bins
     * @param xMin		the desired minimum value on the x axis
     * @param xMax		the desired maximum value on the x axis
     */
    public H1F(String hName, String xTitle, String yTitle, int bins, double xMin, double xMax) {    	
    	setName(hName);
    	set(bins, xMin, xMax);
    	initDataStore(bins);
        this.initAttributes();
        setXTitle(xTitle);
        setYTitle(yTitle);
    }
    
    /**
     * Creates a 1-D Histogram with the specified name, minimum and maximum x-axis values,
     * and the bin heights
     * 
     * @param name			the desired name of the histogram
     * @param xMin			the desired minimum x-axis value
     * @param xMax			the desired maximum x-axis value
     * @param binHeights	a double array of the heights of the bins
     */
    public H1F(String name, double xMin, double xMax, float[] binHeights) {
    	setName(name);
    	set(binHeights.length, xMin, xMax);
    	for (int i = 0; i < binHeights.length; i++) {
    		histogramData[i] = binHeights[i];
    	}
        this.initAttributes();
    }
    
    /**
     * Creates a 1-D Histogram with the specified name, number of bins, and minimum and maximum
     * x-axis values
     * 
     * @param name		The desired name of the histogram
     * @param bins		The desired number of bins
     * @param xMin		The desired minimum x-axis value
     * @param xMax		The desired maximum x-axis value
     */
    public H1F(String name, int bins, double xMin, double xMax) {
    	setName(name);
    	set(bins, xMin, xMax);
        this.initAttributes();
    }
    
    /**
     * Creates a 1-D Histogram with the specified name, title, number of bins, and minimum
     * and maximum x-axis values
     * 
     * @param name		The desired name of the histogram
     * @param title		The desired title of the histogram
     * @param bins		The desired number of bins
     * @param xMin		The desired minimum x-axis value
     * @param xMax		The desired maximum x-axis value
     */
    public H1F(String name, String title, int bins, double xMin, double xMax) {
    	setName(name);
    	setTitle(title);
    	set(bins, xMin, xMax);
        this.initAttributes();        
    }
    
    /**
     * Creates a new histogram using the name, title, minimum, and maximum.
     * 
     * @param name		The desired name of the histogram
     * @param title		The desired title of the histogram
     * @param xMin		The desired minimum x-axis value
     * @param xMax		The desired maximum x-axis value
     */
    public H1F(String name, String title, double xMin, double xMax)  {
    	setName(name);
    	setTitle(title);
    	set((int)(xMax - xMin), xMin, xMax);
        this.initAttributes();
        
    }
    
    public final void initAttributes(){
        this.hAttr.setLineWidth(1);
        this.hAttr.setLineColor(1);
        this.hAttr.setLineStyle(1);
        this.hAttr.setFillColor(-1);
        this.hAttr.setFillStyle(0);
        this.hAttr.setDatasetType(DatasetAttributes.HISTOGRAM);
        //this.hAttr.setMarkerSize(6);
        //this.hAttr.setMarkerStyle(1);
        //this.hAttr.setMarkerColor(2);
        //this.hAttr.addString(AttributeType.STRING_TITLE_X, "");
        //this.hAttr.addString(AttributeType.STRING_TITLE_Y, "");  
    }
    
    public final void setTitle(String title){
        this.hAttr.setTitle(title);
    }
    
    /**
     * Sets the x-axis title to the specified parameter
     * @param xTitle		The desired title of the x-axis
     */
    public final void setXTitle(String xTitle) {
        this.hAttr.setXTitle(xTitle);
        //this.getXaxis().setTitle(xTitle);
        //this.hAttr.addString(AttributeType.STRING_TITLE_X, xTitle);
    }
    
    /**
     * Sets the y-axis title to the specified parameter
     * 
     * @param yTitle		The desired title of the y-axis
     */
    public final void setYTitle(String yTitle) {
        this.hAttr.setYTitle(yTitle);
        //this.getYaxis().setTitle(yTitle);
        //this.hAttr.addString(AttributeType.STRING_TITLE_Y, yTitle);
    }
    
    public static H1F  create(String name, int bins, DataVector vec){
        double min = vec.getMin();
        double max = vec.getMax();
        H1F h = new H1F(name,"",bins,min,max);
        for(int i = 0; i < vec.getSize(); i++){
            h.fill(vec.getValue(i));
        }
        return h;
    }
    /**
     * The getter for the histogram title.
     * @return Title of the histogram.
     */
    public String getTitle(){
        //return this.histTitle;
        return "";
    }
    /**
     * The getter for the x-axis title.
     * 
     * @return		The title of the x-axis as a string
     */
    public String getXTitle() {
        return this.hAttr.getXTitle();
    	//return this.getXaxis().getTitle();
    }
    
    /**
     * The getter for the y-axis title.
     * 
     * @return		The title of the y-axis as a string
     */
    public String getYTitle() {
        return this.hAttr.getYTitle();
        //return this.getYaxis().getTitle();
    }
    
    /**
     * Sets the specified parameter as the name of the histogram
     * 
     * @param name		The desired name of the histogram
     */
    @Override
    public final void setName(String name) {
    	histName = name;
    }
    
    /**
     * Returns the title of the histogram
     * 
     * @return		the title of the histogram as a string
     */
    public String title() {
    	return this.hAttr.getTitle();
    }
    
    /**
     * Returns the name of the histogram
     * 
     * @return		the name of the histogram as a string
     */
    public String name() {
    	return histName;
    }
    
    /**
     * Resets all bins to 0
     */
    public void reset(){
        this.histogramEntries = 0;
        this.histogramOverFlow = 0;
        this.histogramUnderFlow = 0;
        for(int loop = 0; loop < this.histogramData.length;loop++){
            this.histogramData[loop] = 0.0f;
            if(this.histogramDataError.length==this.histogramData.length){
                this.histogramDataError[loop] = 0.0f;
            }
        }
    }
    
    public int getEntries(){
        int entries = 0;
        for(int loop = 0; loop < this.histogramData.length; loop++){
            entries += (int) this.histogramData[loop];
        }
        return entries;
    }
    /**
     * Calculates the mean of the data in the histogram
     * 
     * @return		the mean of the histogram data as a double
     */
    public double getMean() {
        double mean  = 0.0;
        double summ  = 0.0;
        double count = 0; 
        for(int i = 0; i < this.getAxis().getNBins(); i++){
            double bincontent =  this.getBinContent(i);
            //System.err.println(" bin count = " + count + " content summ = " + bincontent);
            if(bincontent!=0){
                summ  += this.getAxis().getBinCenter(i)*this.getBinContent(i);
                count += this.getBinContent(i);
            }
        }
        
        if(count!=0){
            mean = summ/count;
        }
        
        return mean;
    }
    
    public String[] getStatText(){
        String[] lines = new String[4];
        lines[0] = this.histName;
        lines[1] = String.format("%-14s %9d", "Entries",this.getEntries());
        lines[2] = String.format("%-14s %9.4f", "Mean",this.getMean());
        lines[3] = String.format("%-14s %9.4f", "RMS",this.getRMS());        
        //lines[1] =;
        return lines;
    }
    /**
     * Calculates the root mean square of the histogram data
     * 
     * @return		the root mean square of the histogram data
     */
    public double getRMS() {
        double mean = this.getMean();
        double rms = 0.0;
        double summ  = 0.0;
        int    count = 0; 
        for(int i = 0; i < this.getAxis().getNBins(); i++){
            int bincontent = (int) this.getBinContent(i);
            if(bincontent!=0){
                double variance = this.getAxis().getBinCenter(i) - mean;
                summ  += variance*variance*this.getBinContent(i);
                count += (int) this.getBinContent(i);
            }
        }
        if(count!=0) {
            rms = summ/count;
            return Math.sqrt(rms);
        }
        return rms;
    }
    
    /**
     * Sets the specified number of bins, min and max to the x axis
     * and creates a standard Y axis with a min value of 0 and a max value
     * of 0. Additionally, sets up the axes to store data.
     * 
     * @param bins		the desired number of bins.
     * @param min		the desired minimum x value
     * @param max		the desired maximum y value
     */
    public final void set(int bins, double min, double max) {
    	xAxis = new Axis(bins, min, max);
    	yAxis = new Axis();
    	initDataStore(bins);
    }
    
    public double integral(){
        return this.integral(0, this.histogramData.length-1);
    }
    
    public double integral(int start_bin, int end_bin){
        double integral = 0.0;
        for(int loop = start_bin; loop <= end_bin; loop++){
            integral += this.histogramData[loop];
        }
        return integral;
    }
    /**
     * Initializes the double arrays for the histogram data and data errors.
     * 
     * @param size 		the number of data points to store
     */
    final void initDataStore(int size)
    {
        histogramData      = new float[size];
        histogramDataError = new float[size];
    }
    
    /**
     * Increments the bin corresponding to that value by 1
     * 
     * @param value		the value to increment
     */
    public void fill(double value) {
    	incrementBinContent(xAxis.getBin(value));
    }
    
    /**
     * Increments the bin corresponding to that value by that weight
     * 
     * @param value		the value to increment
     * @param weight	the weight to increment by
     */
    public void fill(double value, double weight) {
    	incrementBinContent(xAxis.getBin(value), weight);
    }
    
    /**
     * Normalizes the histogram data to the specified number
     * 
     * @param number		the value to normalize the data to
     */
    public void normalize(double number) {
        for(int i = 0; i < histogramData.length; i++)
        {
            histogramData[i] /= number;
        }
    }
    
    /**
     * Increments the content in the specified bin by one. The bin is specified in array indexing
     * format (to increment the 1st bin, enter 0, the 2nd, enter 1, ... , the nth, enter n-1)
     * 
     * @param bin		the bin to be incremented, specified in array indexing format.
     */
    public void incrementBinContent(int bin) {
        this.histogramEntries++;
    	if (bin >= 0 && bin < histogramData.length) {
    		histogramData[bin] = (float) (histogramData[bin] + 1.0);
    		histogramDataError[bin] = (float) Math.sqrt(Math.abs(histogramData[bin]));
    	} else {
            if(bin<0){
                this.histogramUnderFlow++;
            } else {
                this.histogramOverFlow++;
            }
        }
    }
    
    /**
     * Increments the content in the specified bin by the entered weight. The bin is specified in array indexing
     * format (to increment the 1st bin, enter 0, the 2nd, enter 1, ... , the nth, enter n-1)
     * 
     * @param bin		the bin to be incremented, specified in array indexing format.
     * @param weight	the weight to increment by
     */
    public void incrementBinContent(int bin, double weight) {
        this.histogramEntries++;
    	if (bin >= 0 && bin < histogramData.length) {
    		histogramData[bin] = (float) (histogramData[bin] + weight);
    		histogramDataError[bin] = (float) Math.sqrt(Math.abs(histogramData[bin]));
    	} else {
            if(bin<0){
                this.histogramUnderFlow++;
            } else {
                this.histogramOverFlow++;
            }
        }
    }
    
    public void add(H1F h){
        if(h.getAxis().getNBins()==this.getXaxis().getNBins()){
            for(int loop = 0; loop < this.histogramData.length; loop++){
                this.setBinContent(loop, this.getBinContent(loop)+h.getBinContent(loop));
            }
        } else {
            System.out.println("[warning] ---> histograms have different bin number. not added.");
        }
    }
    
    
    public void divide(double number){
        for(int i = 0; i < this.getAxis().getNBins(); i++)
        {
            this.histogramData[i] = (float) (this.histogramData[i]/number);
        }
    }
    /**
     * Static method for H1D to divide two histograms, the resulting
     * histogram is created, and arguments are untouched.
     * @param h1
     * @param h2
     * @return 
     */
    public static H1F divide(H1F h1, H1F h2){
        if(h1.getXaxis().getNBins()!=h2.getXaxis().getNBins()){
            System.out.println("[H1D::divide] error : histograms have inconsistent bins");
            return null;
        }
        
        H1F h1div = new H1F(h1.getName()+"_DIV",
                h1.getXaxis().getNBins(),
                h1.getXaxis().min(),h1.getXaxis().max());
        StatNumber   result = new StatNumber();
        StatNumber   denom  = new StatNumber();
        for(int bin = 0; bin < h1.getXaxis().getNBins(); bin++){
            double bc = 0;
            result.set(h1.getBinContent(bin), h1.getBinError(bin));
            denom.set(h2.getBinContent(bin), h2.getBinError(bin));
            result.divide(denom);
            h1div.setBinContent(bin, result.number());
            h1div.setBinError(bin, result.error());
        }
        return h1div;
    }
    /**
     * Divides the current histogram object by the parameter 1-D histogram. 
     * Requires that both histograms have the same number of bins.
     * 
     * @param hist		the 1-D histogram object to divide the current object by
     */
    public void divide(H1F hist) {
    	if(hist.getAxis().getNBins() !=this.getAxis().getNBins())
        {
            System.out.println("ERROR: inconsistent bins in histograms");
            return;
        }
        
        StatNumber result = new StatNumber();
        StatNumber hdiv   = new StatNumber();
        for(int i = 0; i < this.getAxis().getNBins(); i++)
        {
            result.set(this.getBinContent(i), this.getBinError(i));
            hdiv.set(hist.getBinContent(i), hist.getBinError(i));
            result.divide(hdiv);
            this.setBinContent(i, result.number());
            this.setBinError(i, result.error());
        }
    }
    
    /**
     * Sets the value to the specified bin. The bin is specified in array indexing format 
     * (i.e. to set the value to the 1st bin, enter 0, to the 2nd bin, enter 1, ... , 
     * the nth bin, enter n-1)
     * 
     * @param bin		the bin to enter the value into, specified in array indexing format
     * @param value		the value to store in the specified bin
     */
    public void setBinContent(int bin, double value) {
    	if ((bin >= 0) && (bin < histogramData.length)) {
    		histogramData[bin] = (float) value;
    		histogramDataError[bin] = (float) Math.sqrt(Math.abs(histogramData[bin]));
    	} else {
            if(bin<0){
                this.histogramUnderFlow++;
            } else {
                this.histogramOverFlow++;
            }
        }
    }
    /**
     * returns a copy of the histogram with different name.
     * @param name
     * @return 
     */
    public H1F histClone(String name){
        H1F hclone = new H1F(name, this.histXTitle, this.histYTitle,
        this.xAxis.getNBins(),this.xAxis.min(),this.xAxis.max());
        for(int loop = 0; loop < this.xAxis.getNBins(); loop++){
            hclone.setBinContent(loop, this.getBinContent(loop));
            hclone.setBinError(loop, this.getBinError(loop));
        }
        return hclone;
    }
    /**
     * Sets the bin error to the specified bin. The bin is specified in array indexing format 
     * (i.e. to set the value to the 1st bin, enter 0, to the 2nd bin, enter 1, ... , 
     * the nth bin, enter n-1)
     * 
     * @param bin		the bin to enter the value of the error into, specified in array indexing format
     * @param value		the error to store in the specified bin
     */
    public void setBinError(int bin, double value) {
    	if (bin >= 0 && bin < histogramDataError.length) {
    		histogramDataError[bin] = (float) value;
    	}
    }
    
    /**
     * Returns the content of the specified bin as a double. The bin is defined in array indexing
     * format (i.e. to retrieve the 1st bin's content, enter 0, for the 2nd bin, enter 1, ... , 
     * for the nth bin, enter n-1)
     * 
     * @param bin		The bin to retrieve the content of, specified in array indexing format
     * @return			The content of the bin entered as a parameter
     */
    public double getBinContent(int bin) {
    	if ((bin >= 0) && (bin < histogramData.length)) {
    		return histogramData[bin];
    	}
    	return 0.0;
    }
    
    /**
     *Returns the error of the specified bin as a double. The bin is defined in array indexing
     * format (i.e. to retrieve the 1st bin's error, enter 0, for the 2nd bin, enter 1, ... , 
     * for the nth bin, enter n-1)
     * 
     * @param bin		The bin to retrieve the error of, specified in array indexing format
     * @return			The error of the bin entered as a parameter
     */
    public double getBinError(int bin) {
        if(bin >= 0 && bin < histogramDataError.length) {
            return histogramDataError[bin];
        }
        return 0.0;
    }
    
    public Axis getXaxis(){return this.xAxis;}
    public Axis getYaxis(){ return this.yAxis;}
    /**
     * Retrieves the x-axis as an Axis object
     * 
     * @return the x-axis of the histogram as an Axis object
     */
    public Axis getxAxis() {
        return xAxis;
    }
    
    /**
     * Retrieves the y-axis as an Axis object
     * 
     * @return the y-axis of the histogram as an Axis object
     */
    public Axis getyAxis() {
        return yAxis;
    }
    
    /**
     * Retrieves the x-axis as an Axis object
     * 
     * @return the x-axis of the histogram as an Axis object
     */
    public Axis getAxis() {
        return xAxis;
    }
    
    /**
     * Overrides the toString method of type Object
     * 
     * @return		a formatted string representation of the content in the histogram
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for(int i = 0; i < xAxis.getNBins(); i++) {
            buffer.append(String.format("%12.6f %12.6f %12.6f\n",
                    xAxis.getBinCenter(i),this.getBinContent(i),
                    this.getBinError(i)));
        }
        return buffer.toString();
    }       
    
    /**
     * Retrieves a graph of the histogram
     * 
     * @return a DataPoints object of the histogram data
     */
    public GraphErrors getGraph(){
        //GraphErrors  graph = new GraphErrors(this.getAxis().getBinCenters(),
        //        this.getData());
        GraphErrors  graph = new GraphErrors();
        
        int npoints = this.getDataSize(0);// this.getDataSize();
        for(int loop = 0; loop < npoints; loop++){
            graph.addPoint(this.getDataX(loop), this.getDataY(loop), 
                    this.getDataEX(loop),this.getDataEY(loop));
        }
        return graph;
    }
    
    /*
    public DataPoints getGraph() {
        DataPoints graph = new DataPoints(this.histName+"_graph",this.histXTitle,this.histYTitle);
        int npoints = this.getAxis().getNBins();
        
        graph.set(npoints);
        for(int i = 0; i < npoints; i++) {
            graph.setPoint(i, 
                    this.getAxis().getBinCenter(i), 
                    this.getBinContent(i));
        }
        return graph;
    }*/
    
    /**
     * 
     * @return		the data in the histogram
     */
    public float[] getData() {
    	return histogramData;
    }
    
    /**
     * 
     * @return		the data error in the histogram
     */
    public float[] getDataError() {
    	return histogramDataError;
    }
    /**
     * Returns bin number with maximum entries.
     * @return 
     */
    public int getMaximumBin(){
        int bin = 0;
        double max = this.histogramData[0];
        for(int loop = 0; loop < this.histogramData.length; loop++){
            if(this.histogramData[loop]>max){
                max = this.histogramData[loop];
                bin = loop;
            }
        }
        return bin;
    }
    /**
     * Changes the bin widths to vary with a set minimum slope to 
     * be allowed as its own bin.
     * 
     * @param	sensitivity			what percentage of the maximum slope
     * 								all bins have to be at minimum to be 
     * 								considered its own distinct bin
     */
    public void fixBinWidths(double sensitivity) {
    	double maxSlope = Math.abs(histogramData[1] - histogramData[0]);
    	for (int i = 1; i < histogramData.length - 1; i++) {
    		double slope = Math.abs(histogramData[i+1] - histogramData[i]);
    		if (slope > maxSlope) {
    			maxSlope = slope;
    		}
    	}
    	
    	double minSlope = maxSlope * sensitivity; //allows bin slope to
    									  		  //be as little as that 
    											  //magnitude of the max
    	double[] histData = new double[histogramData.length];
    	double[] histMargins = new double[xAxis.axisMargins.length];
    	
    	for (int i = 0; i < histData.length; i++) {
    		histData[i] = -1.0;
    	}
    	
    	histData[0] = histogramData[0];
    	histMargins[0] = xAxis.axisMargins[0];
    	int index = 0;
    	for (int i = 0; i < histogramData.length - 1; i++) {
    		if (Math.abs(histogramData[i+1] - histogramData[i]) < minSlope) {
    			if (histData[index] == -1.0) {
    				histData[index] = 0.0;
    			}
    			histData[index] += histogramData[i+1];
    			histMargins[index+1] = xAxis.axisMargins[i+2];
    		}
    		
    		else {
    			histData[index+1] = histogramData[i+1];
    			histMargins[index+1] = xAxis.axisMargins[i+1];
    			index++;
    		}
    	}
    	
    	set(index + 1, xAxis.min(), xAxis.max());
    	
    	for (int i = 0; i < histogramData.length; i++) {
    		setBinContent(i, histData[i]);
    		xAxis.set(histMargins);
    	}
    	
    	for (int i = 0; i < histogramData.length - 1; i++) {
    		if (Math.abs(histogramData[i+1] - histogramData[i]) < minSlope) {
    			fixBinWidths(sensitivity);
    			break;
    		}
    	}
    }
         
    @Override
    public String getName() {
        return this.histName;
    }

    @Override
    public int getDataSize(int axis) {
        return this.xAxis.getNBins();
    }

    @Override
    public DatasetAttributes getAttributes() {
        return this.hAttr;
    }

    @Override
    public double getDataX(int bin) {
        double binCenter = this.xAxis.getBinCenter(bin);
        return binCenter;
    }

    @Override
    public double getDataY(int bin) {
        return this.histogramData[bin];
    }

    @Override
    public double getDataEX(int bin) {
        return this.xAxis.getBinWidth(bin);
    }

    @Override
    public double getDataEY(int bin) {
        return this.histogramDataError[bin];
    }

    @Override
    public double getData(int xbin, int ybin) {
        return 0.0;
    }
    
    /**
     * ROOT COMPATABILITY Functions
     * @param color
     * @return 
     */  
    public H1F setFillColor(int color){
        this.hAttr.setFillColor(color);
        return this;
    }
  
    public int getFillColor(){
        return this.hAttr.getFillColor();
    }
    
    public H1F setLineColor(int color){
       this.hAttr.setLineColor(color);
        return this;
    }
    
    public int getLineColor(){
        return this.hAttr.getLineColor();
    }
    
    public H1F setLineWidth(int width){ 
    	this.hAttr.setLineWidth(width);
        return this;
    }
    
    public int getLineWidth(){
        return this.hAttr.getLineWidth();
    }

    public void setFunction(Func1D f){
        this.fittedFunction = f;
    }
    
    public Func1D getFunction(){
        return this.fittedFunction;
    }
    
    @Override
    public PaveText getStatBox() {
        PaveText stat = new PaveText(2);
        stat.addText("Name",this.getName());
        stat.addText("Entries",Integer.toString(this.getEntries()));
        stat.addText("Mean",String.format("%.3f", this.getMean()));
        stat.addText("RMS",String.format("%.3f", this.getRMS()));
        stat.addText("Underflow",Integer.toString(this.histogramUnderFlow));
        stat.addText("Overflow",Integer.toString(this.histogramOverFlow));
    
        if(this.fittedFunction!=null){
            stat.addText("#chi^2/NDF",
                    String.format("%.3f/%d", 
                            this.fittedFunction.getChiSquare(),this.fittedFunction.getNDF()));
            int npars = this.fittedFunction.getNPars();
            for(int i = 0; i < npars; i++){
                stat.addText(this.fittedFunction.parameter(i).name(),
                        String.format("%.3f/%.4f", 
                                fittedFunction.parameter(i).value(),
                                fittedFunction.parameter(i).error()
                                ));
            }
        }
        return stat;
    }

	public void setOptStat(int i) {
		this.hAttr.setOptStat(i);
	}
}
