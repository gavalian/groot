package org.jlab.groot.data;

import org.jlab.groot.base.DatasetAttributes;
import org.jlab.groot.base.GStyle;
import org.jlab.groot.math.Axis;
import org.jlab.groot.math.Func1D;
import org.jlab.groot.math.StatNumber;
import org.jlab.groot.ui.PaveText;

import java.io.FileWriter;
import java.io.IOException;
import org.jlab.groot.fitter.DataFitter;


/**
 * Defines the class to create a basic 1D Histogram
 * @author Gagik Gavalian
 * @author Erin Kirby
 * @version 062614
 */
public class H1F  implements IDataSet {

    Axis  xAxis;
    Axis  yAxis;
    
    double[]    histogramData;
    double[]    histogramDataError;
    String     histName   = "";
    long        histogramUnderFlow = 0L;
    long        histogramOverFlow  = 0L;
    long        histogramEntries   = 0L;
    long       histogramUniqueID  = 0L;
    
    DatasetAttributes hAttr     = new DatasetAttributes(DatasetAttributes.HISTOGRAM);
    
    Func1D     fittedFunction = null;
    
    /**
     * The default constructor, which creates a Histogram1D object with the Name "default", 
     * the Title "default", no Axis titles, and sets the minimum xAxis value to 0, the maximum x
     *  value to 1, and creates 1 bin. 
     */
    public H1F() {
    	set(1,0.0,1.0);
    	initDataStore(1);
        this.initAttributes();
    	setName("default");
    	setTitle("default");
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
    	set(bins, xMin, xMax);
    	initDataStore(bins);
        this.initAttributes();
        setTitleX(xTitle);
        setTitleY(yTitle);
    	setName(hName);
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
    	set(binHeights.length, xMin, xMax);
    	for (int i = 0; i < binHeights.length; i++) {
    		histogramData[i] = binHeights[i];
    	}
        this.initAttributes();
    	setName(name);
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
    	set(bins, xMin, xMax);
        this.initAttributes();
    	setName(name);
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
    	set(bins, xMin, xMax);
        this.initAttributes();        
    	setName(name);
    	setTitle(title);
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
    	set((int)(xMax - xMin), xMin, xMax);
        this.initAttributes();
    	setName(name);
    	setTitle(title);
    }
    
    public H1F(String name, double[] bins){
        xAxis = new Axis(bins);
    	yAxis = new Axis();
        this.initAttributes();
        setName(name);
        initDataStore(xAxis.getNBins());
    }
    
    public H1F(String name, String title, double[] bins){
        xAxis = new Axis(bins);
    	yAxis = new Axis();
                
        this.initAttributes();
        setName(name);
        setTitle(title);
    	initDataStore(xAxis.getNBins());
    }
    
    public H1F  setUniqueID(long id){ this.histogramUniqueID = id; return this;}
    public long getUniqueID(){ return this.histogramUniqueID;}

    public long getUnderflow(){ return this.histogramUnderFlow;}
    public long getOverflow() { return this.histogramOverFlow;}
    protected void setOverflow(long over){ histogramOverFlow = over;}
    protected void setUnderflow(long under){ histogramUnderFlow = under;}
    
    public final void initAttributes(){
    	try {
    		this.hAttr = GStyle.getH1FAttributes().clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * divides the content of each bin in the histogram to the 
     * maximum bin.
     */
    public void unit(){
        float max = (float) this.getMax();
        if(max<0e-12){
            System.out.println("[H1F] ** error ** the histogram has very low maximum. can not be set to unit.");
            return;
        }
        for(int i = 0; i < histogramData.length; i++){
            histogramData[i] = histogramData[i]/max;
            histogramDataError[i] = histogramDataError[i]/max;
        }
    }
    /**
     * set Title of the histogram
     * @param title new title
     */
    public final void setTitle(String title){
        
        if(title.contains(";")==true){
           String[] tokens = title.split(";");
           if(tokens.length>0)
               this.hAttr.setTitle(tokens[0]);
           if(tokens.length>1)
               this.hAttr.setTitleX(tokens[1]);
           if(tokens.length>2)
               this.hAttr.setTitleY(tokens[2]);
        } else {
            this.hAttr.setTitle(title);
        }
    }
    
    /**
     * Sets the x-axis title to the specified parameter
     * @param xTitle		The desired title of the x-axis
     */
    public final void setTitleX(String xTitle) {
        this.hAttr.setTitleX(xTitle);
        //this.getXaxis().setTitle(xTitle);
        //this.hAttr.addString(AttributeType.STRING_TITLE_X, xTitle);
    }
    
    /**
     * Sets the y-axis title to the specified parameter
     * 
     * @param yTitle		The desired title of the y-axis
     */
    public final void setTitleY(String yTitle) {
        this.hAttr.setTitleY(yTitle);
        //this.getYaxis().setTitle(yTitle);
        //this.hAttr.addString(AttributeType.STRING_TITLE_Y, yTitle);
    }
    
    public static H1F  create(String name, int bins, DataVector vec, DataVector weight){
        double min = vec.getMin();
        double max = vec.getMax();
        if(min==max){
        	min = .9999*min;
        	max = 1.0001*max; 
        }
        H1F h = new H1F(name,"",bins,min,max);
        for(int i = 0; i < vec.getSize(); i++){
            h.fill(vec.getValue(i), weight.getValue(i));
        }
        h.setFillColor(43);
        return h;
    }
    
    public static H1F  create(String name, int bins, DataVector vec){
        double min = vec.getMin();
        double max = vec.getMax();
        if(min==max){
        	min = .9999*min;
        	max = 1.0001*max; 
        }
        H1F h = new H1F(name,"",bins,min,max);
        for(int i = 0; i < vec.getSize(); i++){
            h.fill(vec.getValue(i));
        }
        h.setFillColor(43);        
        return h;
    }
    
    public static H1F  create(String name, int bins, DataVector vec, double min, double max){
        //double min = vec.getMin();
        //double max = vec.getMax();
        if(min==max){
        	min = .9999*min;
        	max = 1.0001*max; 
        }
        H1F h = new H1F(name,"",bins,min,max);
        for(int i = 0; i < vec.getSize(); i++){
            h.fill(vec.getValue(i));
        }
        h.setFillColor(43);        
        return h;
    }
    /**
     * The getter for the histogram title.
     * @return Title of the histogram.
     */
    public String getTitle(){
        return this.hAttr.getTitle();
    }
    /**
     * The getter for the x-axis title.
     * 
     * @return		The title of the x-axis as a string
     */
    public String getTitleX() {
        return this.hAttr.getTitleX();
    }
    
    /**
     * The getter for the y-axis title.
     * 
     * @return		The title of the y-axis as a string
     */
    public String getTitleY() {
        return this.hAttr.getTitleY();
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
     * Resets all bins to 0
     */
    @Override
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
    
    protected void setEntries(long entries){
        histogramEntries = entries;
    }
    
    public long getEntries(){
        /*int entries = 0;
        for(int loop = 0; loop < this.histogramData.length; loop++){
            entries += (int) this.histogramData[loop];
        }*/
        return histogramEntries;
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
    
    public double getIntegral(){
    	return integral();
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
        histogramData      = new double[size];
        histogramDataError = new double[size];
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
    
    public void fit(Func1D func,String options){
        DataFitter.fit(func, this, options);   
        fittedFunction = func;
    }
    
    public void fit(Func1D func){
        this.fit(func, "");
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
    		histogramData[bin] =  (histogramData[bin] + 1.0);
    		histogramDataError[bin] = Math.sqrt(Math.abs(histogramData[bin]));
    	} else {
            if(bin<-1){
                this.histogramOverFlow++;
            } else {
                this.histogramUnderFlow++;
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
    		histogramData[bin] =  (histogramData[bin] + weight);
    		histogramDataError[bin] = Math.sqrt(Math.abs(histogramData[bin]));
    	} else {
            if(bin<-1){
                this.histogramOverFlow++;
            } else {
                this.histogramUnderFlow++;
            }
        }
    }
    
    public void modify(DataVector vec, double rmin, double rmax){
        int nbins = xAxis.getNBins();
        xAxis.set(nbins, rmin, rmax);
        this.reset();
        for(int loop = 0; loop < vec.getSize(); loop++){
            this.fill(vec.getValue(loop));
        }
    }
    
    public void fill(DataVector vec){
    	 for(int loop = 0; loop < vec.getSize(); loop++){
             this.fill(vec.getValue(loop));
         }
    }
    public void fill(DataVector vec, DataVector weight){
   	 for(int loop = 0; loop < vec.getSize(); loop++){
            this.fill(vec.getValue(loop),weight.getValue(loop));
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
        
        H1F h1div = new H1F(h1.getName()+"_DIV_"+h2.getName(),
                h1.getXaxis().getNBins(),
                h1.getXaxis().min(),h1.getXaxis().max());
        StatNumber   result = new StatNumber();
        StatNumber   denom  = new StatNumber();
        for(int bin = 0; bin < h1.getXaxis().getNBins(); bin++){
            result.set(h1.getBinContent(bin), h1.getBinError(bin));
            denom.set(h2.getBinContent(bin), h2.getBinError(bin));
            result.divide(denom);
            h1div.setBinContent(bin, result.number());
            h1div.setBinError(bin, result.error());
        }
        return h1div;
    }
    /**
     * Adds two histograms. Returns a new histogram.
     * @param h1 first histogram
     * @param h2 second histogram
     * @return sum of two histograms
     */
    public static H1F add(H1F h1, H1F h2){
        if(h1.getXaxis().getNBins()!=h2.getXaxis().getNBins()){
            System.out.println("[H1D::divide] error : histograms have inconsistent bins");
            return null;
        }
        H1F h1div = new H1F(h1.getName()+"_ADD_"+h2.getName(),
                h1.getXaxis().getNBins(),
                h1.getXaxis().min(),h1.getXaxis().max());
        StatNumber   result = new StatNumber();
        StatNumber   denom  = new StatNumber();
        for(int bin = 0; bin < h1.getXaxis().getNBins(); bin++){
            result.set(h1.getBinContent(bin), h1.getBinError(bin));
            denom.set(h2.getBinContent(bin), h2.getBinError(bin));
            result.add(denom);
            h1div.setBinContent(bin, result.number());
            h1div.setBinError(bin, result.error());
        }
        return h1div;
    }
    
    public static H1F add(H1F h1, H1F h2, double c1, double c2){
        
        if(h1.getXaxis().getNBins()!=h2.getXaxis().getNBins()){
            System.out.println("[H1D::divide] error : histograms have inconsistent bins");
            return null;
        }
        H1F h1div = new H1F(h1.getName()+"_ADD_"+h2.getName(),
                h1.getXaxis().getNBins(),
                h1.getXaxis().min(),h1.getXaxis().max());
        StatNumber   result = new StatNumber();
        StatNumber   denom  = new StatNumber();
        
        StatNumber   c1stat = new StatNumber(c1,0.0);
        StatNumber   c2stat = new StatNumber(c2,0.0);
        
        for(int bin = 0; bin < h1.getXaxis().getNBins(); bin++){
            result.set(h1.getBinContent(bin), h1.getBinError(bin));
            denom.set(h2.getBinContent(bin), h2.getBinError(bin));
            result.multiply( c1stat);
            denom.multiply(  c2stat);
            result.add(denom);
            h1div.setBinContent(bin, result.number());
            h1div.setBinError(bin, result.error());
        }
        return h1div;
    }
    
    public static H1F sub(H1F h1, H1F h2){
        if(h1.getXaxis().getNBins()!=h2.getXaxis().getNBins()){
            System.out.println("[H1D::divide] error : histograms have inconsistent bins");
            return null;
        }
        H1F h1div = new H1F(h1.getName()+"_ADD_"+h2.getName(),
                h1.getXaxis().getNBins(),
                h1.getXaxis().min(),h1.getXaxis().max());
        StatNumber   result = new StatNumber();
        StatNumber   denom  = new StatNumber();
        for(int bin = 0; bin < h1.getXaxis().getNBins(); bin++){
            result.set(h1.getBinContent(bin), h1.getBinError(bin));
            denom.set(h2.getBinContent(bin), h2.getBinError(bin));
            result.subtract(denom);
            h1div.setBinContent(bin, result.number());
            h1div.setBinError(bin, result.error());
        }
        return h1div;
    }
    /**
     * Subtract the given histogram from this histogram.
     * @param h reference histogram
     */
    public void sub(H1F h){
        if(h.getAxis().getNBins()==this.getXaxis().getNBins()){
            StatNumber   result = new StatNumber();
            StatNumber   denom  = new StatNumber();
            for(int loop = 0; loop < this.histogramData.length; loop++){
                result.set(this.getBinContent(loop), this.getBinError(loop));
                denom.set(h.getBinContent(loop), h.getBinError(loop));
                result.subtract(denom);
                this.setBinContent(loop, result.number());
                this.setBinError(loop, result.error());
            }
        } else {
            System.out.println("[warning] ---> histograms have different bin number. not added.");
        }
    }
    /**
     * returns a new histogram with a content of h1 where the integral is normalized
     * to histogram h2.
     * @param h1 histogram to normalize
     * @param h2 histogram for normalization 
     * @return histogram with bin content of h1 normalized to h2 integral
     */
    public static H1F normalized(H1F h1, H1F h2){
        H1F h1norm = new H1F(h1.getName()+"_NORM_"+h2.getName(),
                h1.getXaxis().getNBins(),
                h1.getXaxis().min(),h1.getXaxis().max());
        double integral_h1 = h1.integral();
        double integral_h2 = h2.integral();
        double ratio = integral_h2/integral_h1;
        for(int bin = 0; bin < h1.getXaxis().getNBins(); bin++){
            h1norm.setBinContent(bin, h1.getBinContent(bin)*ratio);
        }
        return h1norm;
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
        H1F hclone = new H1F(name, this.getTitleX(), this.getTitleY(),
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
                    this.getDataEX(loop)/2.0,this.getDataEY(loop));
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
        float[] data = new float[histogramData.length];
        for(int i = 0; i < histogramData.length;i++) 
            data[i] = (float) histogramData[i];
    	return data;
    }
    
    /**
     * 
     * @return		the data error in the histogram
     */
    public float[] getDataError() {
        float[] data = new float[histogramDataError.length];
        for(int i = 0; i < histogramDataError.length;i++) 
            data[i] = (float) histogramDataError[i];
    	return data;
    	//return histogramDataError;
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
        stat.addText("Entries",Long.toString(this.getEntries()));
        stat.addText("Mean",String.format("%.3f", this.getMean()));
        stat.addText("RMS",String.format("%.3f", this.getRMS()));
        stat.addText("Underflow",Long.toString(this.histogramUnderFlow));
        stat.addText("Overflow",Long.toString(this.histogramOverFlow));
        stat.addText("Integral",String.format("%.3f", this.getIntegral()));
    
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
        this.hAttr.setOptStat(""+i);
    }
    
    public void setOptStat(String i) {
        this.hAttr.setOptStat(i);
    }

    @Override
    public double getMin() {
        double min = Double.MAX_VALUE;
        for(int ibuff = 0; ibuff < histogramData.length; ibuff++){
            if(histogramData[ibuff] < min){
                min = histogramData[ibuff];
            }
        }
        return min;
    }

    @Override
    public double getMax() {
        double max = Double.MIN_VALUE;
        for(int ibuff = 0; ibuff < histogramData.length; ibuff++){
            if(histogramData[ibuff] > max){
                max = histogramData[ibuff];
            }
        }
        return max;
    }

    @Override
    public void save(String filename) {
        try {
            FileWriter file = new FileWriter(filename);
            file.write("#H1F: " + this.histName + ", nbins: " + xAxis.getNBins() +"\n");
            file.write("#Bin Center X,Bin Value,Bin Error\n");
            for(int i = 0; i < xAxis.getNBins(); i++) {
                file.write(String.format("%f,%f,%f",
                        xAxis.getBinCenter(i), getBinContent(i), getBinError(i)));
                file.write('\n');
            }
            file.close();
        } catch (IOException e) {
        }
    }
    
    
    
    public static H1F getAsym(H1F hpos, H1F hneg, double c2, double dc2){
        
        H1F hnom = H1F.add(hpos,hneg,1,-c2);
        H1F hden = H1F.add(hpos,hneg,1, c2);
        
        H1F asym = H1F.divide(hnom, hden);
        
        //double  c2 = 1.0;
        //double dc2 = 0.0;
        
        int xbins = asym.getxAxis().getNBins();
        for(int i = 0; i < xbins; i++){
            double a = hpos.getBinContent(i);
            double b = hneg.getBinContent(i);
            double bot = hden.getBinContent(i);
            if(bot < 1e-6){} else {
              double dasq  = hpos.getBinError(i);//->GetBinErrorSqUnchecked(bin);
              double dbsq  = hneg.getBinError(i);
              double error = 2*Math.sqrt(a*a*c2*c2*dbsq + c2*c2*b*b*dasq+a*a*b*b*dc2*dc2)/(bot*bot);  
              asym.setBinError(i, error);
            }
        }
        return asym;
    }
    
    public static H1F getAsym(H1F hpos, H1F hneg) {return H1F.getAsym(hpos, hneg, 1, 0);}
    
}
