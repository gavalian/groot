/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jlab.groot.base.PadMargins;
import org.jlab.groot.data.GraphErrors;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.FunctionFactory;
import org.jlab.groot.ui.OptionsPanel;
import org.jlab.groot.ui.TransferableImage;

/**
 *
 * @author gavalian
 */
public class EmbeddedCanvas extends JPanel implements MouseMotionListener,MouseListener, ActionListener {
    IDataSet selectedDataset = null;
    private Timer        updateTimer = null;
    private Long numberOfPaints  = (long) 0;
    private Long paintingTime    = (long) 0;
    private JPopupMenu popup = null;
    private int popupPad = 0;
    private List<EmbeddedPad>    canvasPads  = new ArrayList<EmbeddedPad>();
    private int                  ec_COLUMNS  = 1;
    private int                  ec_ROWS     = 1;
    private PadMargins           canvasPadding = new PadMargins();
    private int                  activePad     = 0; 
    private boolean isChild = false;
    
    public EmbeddedCanvas(){
        super();
        //this.setSize(500, 400);
        this.setPreferredSize(new Dimension(500,400));        
        canvasPads.add(new EmbeddedPad());
        this.divide(1, 1);
        this.createPopupMenu();
        this.initMouse();
    }
    
    public EmbeddedCanvas(EmbeddedPad pad){
        this.setPreferredSize(new Dimension(500,400));
        this.createPopupMenu();
    }
    
    public final void initMouse(){
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }
    
    public final void divide(int columns, int rows){
        canvasPads.clear();
        ec_COLUMNS = columns;
        ec_ROWS    = rows;
        for(int i = 0; i < columns*rows; i++){
            canvasPads.add(new EmbeddedPad());
        }
        activePad = 0;
    }
    
    public void cd(int pad){
        if(pad<0){
            activePad = 0;
        } else if (pad>=this.canvasPads.size()) {
            activePad = 0;
        } else {
            activePad = pad;
        }         
    }
    
    
    public void draw(IDataSet ds){
        draw(ds,"");
    }
    
    public void draw(IDataSet ds, String options){
        this.getPad(activePad).draw(ds, options);
    }
    
    private void updateCanvasPads(int w, int h){
        int pcounter = 0;
        int startX   = 5;
        int minY     = 5;
        int rW       = w - startX;
        int rH       = h - minY;
        
        for(int ir = 0; ir < ec_ROWS; ir++){
            for(int ic = 0; ic < ec_COLUMNS; ic++){
                double x   = ic * (rW/((double) ec_COLUMNS ));
                double xe  = (ic+1) * (rW/((double) ec_COLUMNS ));
                double y   = ir * ( rH/((double) ec_ROWS) );
                double ye  = (ir+1) * ( rH/((double) ec_ROWS));
               // System.out.println("PAD " + pcounter + " " + x + " " + xe );
                //System.out.printf("Pad: "+pcounter+" %d %d %d %d\n",(int) x + startX, (int) y - minY,
                //        (int) (xe-x), (int) (ye-y));
                canvasPads.get(pcounter).setDimension((int) x + startX, (int) y - minY,
                        (int) (xe-x), (int) (ye-y));

                pcounter++;
            }
        }
    }
    
    /**
     * painting all components on the Graphics2D object.
     * @param g 
     */
    @Override
    public void paint(Graphics g){ 
        try {
            Long st = System.currentTimeMillis();
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = this.getSize().width;
            int h = this.getSize().height;
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, w, h);
            updateCanvasPads(w,h);
            
            PadMargins  margins = new PadMargins();
            
            for(int i = 0; i < canvasPads.size(); i ++){
                EmbeddedPad pad = canvasPads.get(i);
                pad.getAxisFrame().updateMargins(g2d);
                //pad.getAxisFrame().setAxisMargins(pad.getAxisFrame().getFrameMargins());
                margins.marginFit(pad.getAxisFrame().getFrameMargins());
            }
            
            for(int i = 0; i < canvasPads.size(); i ++){
                EmbeddedPad pad = canvasPads.get(i);            
                //pad.setDimension(0, 0, w, h);                        
                //System.out.println("PAD " + i + " " + pad.getAxisFrame().getFrameMargins());
                //pad.getAxisFrame().setAxisMargins(pad.getAxisFrame().getFrameMargins());                
                //System.out.println(pad.getAxisFrame().getFrameMargins());
                pad.getAxisFrame().setAxisMargins(margins);
                pad.setMargins(margins);
                pad.draw(g2d);
            }
            
            Long et = System.currentTimeMillis();
            paintingTime += (et-st);
            numberOfPaints++;
        } catch(Exception e){
            System.out.println("[EmbeddedCanvas] ---> ooops");
            System.out.println(e);
        }
    }
        public EmbeddedPad  getPad(int index){
        return this.canvasPads.get(index);
    }
        
    public void update(){
        this.repaint();       
        //System.out.println(this.getBenchmarkString());
    }
    
    public String getBenchmarkString(){
        StringBuilder str = new StringBuilder();
        double time = (double) paintingTime;
        
        double ms =  (time/numberOfPaints);
        if(numberOfPaints==0) ms = 1000.0;
        
        str.append(String.format("Time = %.2f ms Total Time = %d , Events = %d",
                ms,paintingTime, numberOfPaints));
        return str.toString();
    }
    
    public void setAxisFontSize(int size){
        for(EmbeddedPad pad : canvasPads){
            pad.setAxisFontSize(size);
        }
    }
    
    public void  initTimer(int interval){
        System.out.println("[EmbeddedCanvas] ---->  starting an update timer.");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                update();
                /*for(int i = 0; i < canvasPads.size();i++){
                    System.out.println("PAD = " + i);
                    canvasPads.get(i).show();
                }*/
            }
        };
        updateTimer = new Timer("EmbeddeCanvasTimer");
        updateTimer.scheduleAtFixedRate(timerTask, 30, interval);
        this.paintingTime   = 0L;
        this.numberOfPaints = 0L;
    }
    
    public int getPadByXY(int x, int y){
        int  rowSize = (int) this.getHeight()/this.ec_ROWS;
        int  row = (int) (y/rowSize);
        int  colSize = (int) this.getWidth()/this.ec_COLUMNS;
        int  col = (int) (x/colSize);
        return row*ec_ROWS + col;
    }

    public void draw(DataGroup group){
        int nrows = group.getRows();
        int ncols = group.getColumns();
        this.divide(ncols, nrows);
        
        int nds   = nrows*ncols;
        for(int i = 0; i < nds; i++){
            List<IDataSet> dsList = group.getData(i);
            this.cd(i);
            for(IDataSet ds : dsList){
                this.draw(ds, "same");
            }
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int pad = this.getPadByXY(e.getX(),e.getY());
        //System.out.println("you're hovering over pad = " + pad);
    }
    int fillcolortemp = 1;

    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(e.getClickCount()==2&&!isChild){
            int pad = this.getPadByXY(e.getX(),e.getY());
            double scale = 1.5;
            //System.out.println("you double clicked on " + pad);
            JDialog  dialogWin = new JDialog();
            EmbeddedCanvas can = new EmbeddedCanvas();
            EmbeddedPad embeddedPad = this.getPad(pad).getCopy();
            int xSize = (int)(this.getPad(pad).getWidth());
            int ySize = (int)(this.getPad(pad).getHeight());

            can.setPreferredSize(new Dimension((int)(xSize*scale),(int)(ySize*scale)));
            can.setChild(true);
            ArrayList<EmbeddedPad> pads = new ArrayList<EmbeddedPad>();
            pads.add(embeddedPad);
            can.canvasPads = pads;
            dialogWin.setContentPane(can);
           // dialogWin.setSize(400, 400);
            dialogWin.pack();
            dialogWin.setLocation(new Point(e.getX(),e.getY()));
            dialogWin.setVisible(true);
        }
        if(e.getClickCount()==1&&e.getButton()==1){
        	System.out.println("Left click!");
        	if(selectedDataset!=null){
				selectedDataset.getAttributes().setFillColor(fillcolortemp);
				this.repaint();
			}
        	selectedDataset = null;
        	for(EmbeddedPad pad : this.canvasPads){
        		if(pad.getDatasetPlotters().size()>0){
        			for(IDataSetPlotter plotter : pad.getDatasetPlotters())
        				if(plotter instanceof HistogramPlotter){
        					HistogramPlotter temp = (HistogramPlotter) plotter;
        					if(temp.path.contains(e.getX(), e.getY())){
        						System.out.println("You clicked on:"+temp.getName());
        						if(selectedDataset != temp ){
        							if(selectedDataset!=null){
        								selectedDataset.getAttributes().setFillColor(fillcolortemp);
        							}
            						fillcolortemp = temp.getDataSet().getAttributes().getFillColor();
            						selectedDataset = temp.getDataSet();
            						if(fillcolortemp<10){
            							temp.getDataSet().getAttributes().setFillColor(fillcolortemp+10);
            						}else{
            							temp.getDataSet().getAttributes().setFillColor(fillcolortemp-10);
            						}
            						this.repaint();
        						}
        					}
	        		}
        		}
        	}
        }
       
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    	 if (e.isPopupTrigger()) {
             popupPad = getPadByXY(e.getX(),e.getY());
             //System.out.println("POP-UP coordinates = " + e.getX() + " " + e.getY() + "  pad = " + popupPad);
             popup.show(EmbeddedCanvas.this, e.getX(), e.getY());
         }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    	
    }


    @Override
    public void mouseExited(MouseEvent e) {
    	
    	} 
    
    private void createPopupMenu(){
        this.popup = new JPopupMenu();
        JMenuItem itemCopy = new JMenuItem("Copy Canvas");
        JMenuItem itemCopyPad = new JMenuItem("Copy Pad");
        JMenuItem itemPaste = new JMenuItem("Paste Pad");
        JMenuItem itemSave = new JMenuItem("Save");
        JMenuItem itemSaveAs = new JMenuItem("Save As...");
        JMenuItem itemFitPanel = new JMenuItem("Fit Panel");
        JMenuItem itemOptions = new JMenuItem("Options");
        JMenuItem itemOpenWindow = new JMenuItem("Open in New Window");
        itemCopy.addActionListener(this);
        itemCopyPad.addActionListener(this);
        itemSave.addActionListener(this);
        itemSaveAs.addActionListener(this);
        itemFitPanel.addActionListener(this);
        itemOptions.addActionListener(this);
        itemOpenWindow.addActionListener(this);
        itemPaste.addActionListener(this);
        this.popup.add(itemCopy);
        this.popup.add(itemCopyPad);
        this.popup.add(itemPaste);
        this.popup.add(itemSave);
        this.popup.add(itemSaveAs);
        //this.popup.add(new JSeparator());
        //this.popup.add(itemFitPanel);
        //this.popup.add(new JSeparator());
        this.popup.add(itemOptions);
        //this.popup.add(itemOpenWindow);
        //addMouseListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("action performed " + e.getActionCommand());
        
        if(e.getActionCommand().compareTo("Options")==0){
            this.openOptionsPanel(popupPad);
        }/*
        if(e.getActionCommand().compareTo("Fit Panel")==0){
            this.openFitPanel(popupPad);
        }*/
        if(e.getActionCommand().compareTo("Copy Canvas")==0){
            this.copyToClipboard();
        }
        if(e.getActionCommand().compareTo("Paste Pad")==0){
            this.paste(popupPad);
        }
        if(e.getActionCommand().compareTo("Copy Pad")==0){
            this.copyToClipboard(popupPad);
        }
        if(e.getActionCommand().compareTo("Save")==0){
        	File desktop = new File(System.getProperty("user.home"), "Desktop");
        	DateFormat df = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss_aa");
        	String data = df.format(new Date());
        	this.save(desktop.getAbsolutePath() +File.separator+"Plot_"+data+".png");
        	System.out.println("Saved File:"+desktop.getAbsolutePath() +File.separator+"Plot_"+data+".png");
        }
        if(e.getActionCommand().compareTo("Save As...")==0){
            final JFileChooser fc = new JFileChooser("Save As...");
        	File desktop = new File(System.getProperty("user.home"), "Desktop");
        	DateFormat df = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss_aa");
        	String data = df.format(new Date());
        	this.save(desktop.getAbsolutePath() +File.separator+"Plot_"+data+".png");
            fc.setSelectedFile(new File(desktop.getAbsolutePath() +File.separator+"Plot_"+data+".png"));
            FileFilter filter = new FileNameExtensionFilter("PNG File","png");
            fc.addChoosableFileFilter(filter);
            fc.setFileFilter(filter);
            //In response to a button click:
            int returnVal = fc.showSaveDialog(this);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if(file.exists()==true){
                    JOptionPane.showMessageDialog(this, "Error. The file already esits....");
                } else {
                    //System.out.println("saving file : " + file.getAbsolutePath());
                    this.save(file.getAbsolutePath());
                }
            }
        }/*
        if(e.getActionCommand().compareTo("Open in New Window")==0){
        	this.openInNewWindow(popupPad);
        }*/
        
    }
    private void paste(int popupPad2) {
    	 DataFlavor dmselFlavor = new DataFlavor(EmbeddedPad.class, "EmbeddedPad");
    	 Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
         Transferable clipboardContent = clipboard.getContents(null);

         DataFlavor[] flavors = clipboardContent.getTransferDataFlavors();
         System.out.println("flavors.length = " + flavors.length);
         for (int i = 0; i < flavors.length; i++){
         	if(flavors[i].equals(dmselFlavor)){
         		System.out.println("We have a match!");
         		try{
         		EmbeddedPad pad = (EmbeddedPad) clipboardContent.getTransferData(dmselFlavor);
         		for(int j=0; j<pad.getDatasetPlotters().size(); j++){
         			this.getPad(popupPad2).getDatasetPlotters().add(pad.getDatasetPlotters().get(j).);
         		}
         		this.update();
         		}catch(Exception e){
         			e.printStackTrace();
         		}
         	}
         }
            //System.out.println("flavor[" + i + "] = " + flavors[i]);		
	}

	private void openOptionsPanel(int popupPad2) {
    	JFrame frame = new JFrame("Options Panel");
    	frame.setLayout(new BorderLayout());
    	OptionsPanel mainPanel = new OptionsPanel(this,popupPad2);
    	frame.add(mainPanel);
    	frame.pack();
    	frame.setLocationRelativeTo(this);
    	frame.setVisible(true);
    	
	}

	private BufferedImage getScreenShot(){
        BufferedImage bi = new BufferedImage(
            this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
        this.paint(bi.getGraphics());
        return bi;
    }
    
    private BufferedImage getScreenShot(int pad){
        //BufferedImage bi = new BufferedImage(
        //    (int)this.getPad(index).getWidth(), (int)this.getPad(index).getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
    	//BufferedImage bi = new BufferedImage(
         //       this.getPad(pad).getWidth(), this.getPad(pad).getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
        double scale = 1.0;
        //System.out.println("you double clicked on " + pad);
        //JDialog  dialogWin = new JDialog();
        EmbeddedCanvas can = new EmbeddedCanvas();
        EmbeddedPad embeddedPad = this.getPad(pad).getCopy();
        int xSize = (int)(this.getPad(pad).getWidth());
        int ySize = (int)(this.getPad(pad).getHeight());

        can.setPreferredSize(new Dimension((int)(xSize*scale),(int)(ySize*scale)));
        can.setMinimumSize(new Dimension((int)(xSize*scale),(int)(ySize*scale)));
        can.setSize(new Dimension((int)(xSize*scale),(int)(ySize*scale)));
        can.setChild(true);
        ArrayList<EmbeddedPad> pads = new ArrayList<EmbeddedPad>();
        pads.add(embeddedPad);
        can.canvasPads = pads;
       // dialogWin.setContentPane(can);
       // dialogWin.setSize(400, 400);
        //dialogWin.pack();
        return can.getScreenShot();
    }
    public void copyToClipboard(){
    	TransferableImage trans = new TransferableImage( getScreenShot() );
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents( trans, null );
    }
    
    private void copyToClipboard(int popupPad) {
    	TransferableImage trans = new TransferableImage(getScreenShot(popupPad));
    	trans.setPad(this.getPad(popupPad));
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents(trans, null );
    }

    
    public void save(String filename){    	
    	  File imageFile = new File(filename);
    	    try{
    	        imageFile.createNewFile();
    	        ImageIO.write(getScreenShot(), "png", imageFile);
    	    }catch(Exception ex){
    	    }
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        EmbeddedCanvas canvas = new EmbeddedCanvas();
        canvas.divide(2, 2);
        canvas.setAxisFontSize(14);
        //canvas.getPad(0).getAxisFrame().getAxisX().setAxisFontSize(18);
        //canvas.getPad(1).getAxisFrame().getAxisY().setAxisFontSize(18);
        //canvas.getPad(0).getAxisFrame().setDrawAxisZ(true);
        
        H1F h1  = FunctionFactory.createDebugH1F(6);
        H1F h2  = FunctionFactory.randomGausian(100, 0.4, 5.6, 200000, 2.3, 0.8);
        H1F h2b = FunctionFactory.randomGausian(100, 0.4, 5.6, 80000, 4.0, 0.8);
        H2F h2d = FunctionFactory.randomGausian2D(40, 0.4, 5.6, 800000, 2.3, 0.8);
        
        DataGroup group = new DataGroup(2,1);
        h2b.setName("h2b");
        GraphErrors hprofile = h2d.getProfileX();
        group.addDataSet(h2d, 0);
        group.addDataSet(hprofile, 1);
        canvas.draw(group);
        /*for(int i =0; i < 4; i++){
            canvas.cd(i);
            canvas.draw(h2);
        }*/
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        
    }

	public boolean isChild() {
		return isChild;
	}

	public void setChild(boolean isChild) {
		this.isChild = isChild;
	}

	public void setFont(String fontName) {
		for(EmbeddedPad pad : canvasPads){
			pad.setFontNameAll(fontName);
		}
	}

	public void setTitleSize(int fontSize) {
		for(EmbeddedPad pad : canvasPads){
			pad.setTitleFontSize(fontSize);
		}		
	}

	public void setAxisLabelSize(int fontSize) {
		for(EmbeddedPad pad : canvasPads){
			pad.setTitleFontSize(fontSize);
		}	
	}

	public void setAxisTitleSize(int fontSize) {
		for(EmbeddedPad pad : canvasPads){
			pad.setAxisTitleFontSize(fontSize);
		}			
	}

	public void setStatBoxFontSize(int fontSize) {
		for(EmbeddedPad pad : canvasPads){
			pad.setStatBoxFontSize(fontSize);
		}			
	}

	public void setPadTitles(String title) {
		for(EmbeddedPad pad : canvasPads){
			pad.setTitle(title);
		}			
	}

	public void setPadTitlesX(String title) {
		for(EmbeddedPad pad : canvasPads){
			pad.getAxisY().setTitle(title);
		}			
	}
	
	public void setPadTitlesY(String title) {
		for(EmbeddedPad pad : canvasPads){
			pad.getAxisY().setTitle(title);
		}			
	}

	public void setGridX(boolean isGrid) {
		for(EmbeddedPad pad : canvasPads){
			pad.getAxisX().setGrid(isGrid);
		}			
	}

	public void setGridY(boolean isGrid) {
		for(EmbeddedPad pad : canvasPads){
			pad.getAxisY().setGrid(isGrid);
		}			
	}

   
}
