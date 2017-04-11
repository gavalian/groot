package org.jlab.groot.ui;


import java.io.PrintStream;

public class ProgressBar {
	    private StringBuilder progress;
	    PrintStream out;
	    /**
	     * initialize progress bar properties.
	     */
	    public ProgressBar() {
	        init();
	    }

	    /**
	     * called whenever the progress bar needs to be updated.
	     * that is whenever progress was made.
	     *
	     * @param done an int representing the work done so far
	     * @param total an int representing the total work
	     */
	    public void setOut(PrintStream out){
	    	this.out = out;
	    }
	    public void update(int done, int total) {
	        char[] workchars = {'|', '/', '-', '\\'};
	        String format = "\r%3d%% %s";

	        int percent = (++done * 100) / total;
	        int extrachars = (percent / 2) - this.progress.length();
	       // int blanks = this.progress.length() - extrachars;
	        //this.progress.setCharAt(0, '[');
	        //this.progress.setCharAt(progress.length()-1, ']');
	        while (extrachars-- > 0) {
	            progress.append('#');
	        }
	        /*while(blanks-- > 0){
	        	progress.append(' ');
	        }*/

	        out.printf(format, percent, progress);

	        if (done == total) {
	        	out.flush();
	        	out.println();
	            init();
	        }
	    }

	    private void init() {
	    	out = System.out;
	        this.progress = new StringBuilder(60);
	    }
}
