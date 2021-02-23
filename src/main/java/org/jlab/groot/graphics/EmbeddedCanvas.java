/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.pdf.PDFDocument;
import org.jfree.pdf.PDFGraphics2D;
import org.jfree.pdf.Page;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;
import org.jlab.groot.base.GStyle;
import org.jlab.groot.base.PadMargins;
import org.jlab.groot.base.TColorPalette;
import org.jlab.groot.data.*;
import org.jlab.groot.fitter.ParallelSliceFitter;
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.Dimension1D;
import org.jlab.groot.math.F1D;
import org.jlab.groot.math.FunctionFactory;
import org.jlab.groot.ui.FitPanel;
import org.jlab.groot.ui.LatexText;
import org.jlab.groot.ui.OptionsPanel;
import org.jlab.groot.ui.TransferableImage;

/**
 *
 * @author gavalian
 */
@SuppressWarnings("serial")
public class EmbeddedCanvas extends JPanel implements MouseMotionListener, MouseListener, ActionListener {

    IDataSet selectedDataset = null;
    private Timer updateTimer = null;
    private Long numberOfPaints = (long) 0;
    private Long paintingTime = (long) 0;
    private Long paintingTimeSum = (long) 0;
    private Long samples = (long) 0;
    private JPopupMenu popup = null;
    private int popupPad = 0;
    private List<EmbeddedPad> canvasPads = new ArrayList<EmbeddedPad>();
    private ArrayList<EmbeddedCanvas> children = new ArrayList<EmbeddedCanvas>();
    
    public List<EmbeddedPad> getCanvasPads() {
        return canvasPads;
    }

    public void setCanvasPads(List<EmbeddedPad> canvasPads) {
        this.canvasPads = canvasPads;
    }
    private int ec_COLUMNS = 1;
    private int ec_ROWS = 1;
    // private PadMargins           canvasPadding = new PadMargins();
    private int activePad = 0;
    private boolean isChild = false;
    private boolean showFPS = false;
    private int fitSlicesMode = 0;

    public EmbeddedCanvas() {
        super();
        Locale.setDefault(Locale.US);
        //this.setSize(500, 400);
        this.setPreferredSize(new Dimension(500, 400));

        canvasPads.add(new EmbeddedPad());
        this.divide(1, 1);
        this.createPopupMenu();
        this.initMouse();
    }

    public EmbeddedCanvas(int xsize, int ysize) {
        super();
        Locale.setDefault(Locale.US);
        //this.setSize(500, 400);
        this.setPreferredSize(new Dimension(xsize, ysize));
        this.setSize(xsize, ysize);
        canvasPads.add(new EmbeddedPad());
        this.divide(1, 1);
        this.createPopupMenu();
        this.initMouse();
    }
       
    public EmbeddedCanvas(EmbeddedPad pad) {
        super();
        Locale.setDefault(Locale.US);
        this.setPreferredSize(new Dimension(500, 400));
        this.createPopupMenu();
    }

    public final void initMouse() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    public final void divide(int columns, int rows) {
        ec_COLUMNS = columns;
        ec_ROWS = rows;
        for (int i = 0; i < columns * rows; i++) {
            if (i > (canvasPads.size() - 1)) {
                canvasPads.add(new EmbeddedPad());
            }
        }
        if (canvasPads.size() > columns * rows) {
            for (int i = canvasPads.size() - 1; i >= columns * rows; i--) {
                canvasPads.remove(i);
            }
        }
        this.update();
        for (int i = 0; i < canvasPads.size(); i++) {
            if (canvasPads.get(i).getDatasetPlotters().size() == 0) {
                cd(i);
                break;
            }
            if (i == canvasPads.size() - 1) {
                cd(0);
            }
        }
    }

    /**
     * changes current active drawing pad to index=pad
     *
     * @param pad index of the pad
     * @return returns canvas object
     */
    public EmbeddedCanvas cd(int pad) {
        if (pad < 0) {
            activePad = 0;
        } else if (pad >= this.canvasPads.size()) {
            activePad = 0;
        } else {
            activePad = pad;
        }
        return this;
    }

    /**
     * Clears content of all pads.
     */
    public void clear() {
        /*
        for (EmbeddedPad pad : this.canvasPads) {
            pad.clear();
        }*/
        this.canvasPads.clear();
        this.canvasPads.add(new EmbeddedPad());
        cd(0);
        this.update();
    }

    /**
     * returns all objects plotted on current canvas
     *
     * @return Map<String,IDataSet> of objects
     */
    public Map<String, IDataSet> getObjectMap() {
        Map<String, IDataSet> canvasObjects = new LinkedHashMap<String, IDataSet>();
        for (int i = 0; i < this.canvasPads.size(); i++) {
            Map<String, IDataSet> objects = this.canvasPads.get(i).getObjectMap();
            for (Map.Entry<String, IDataSet> entry : objects.entrySet()) {
                canvasObjects.put(entry.getKey(), entry.getValue());
            }
        }
        return canvasObjects;
    }

    /**
     * draws data set on current pad and advances active pad by one.
     *
     * @param ds data set to be drawn
     */
    public void drawNext(IDataSet ds) {
        draw(ds, "");
        cd(this.activePad + 1);
    }
    /**
     * Moves active pad to the next pad
     */
    public EmbeddedCanvas next(){
        cd(this.activePad+1);
        return this;
    }
    /**
     * returns the active pad number
     * @return active pad
     */
    public int pwd(){
        return this.activePad;
    }
    /**
     * draws data set on current pad and advances active pad by one.
     *
     * @param ds data set to be drawn
     * @param options draw options
     */
    public void drawNext(IDataSet ds,String options) {
        draw(ds, options);
        if(options.contains("same")==false&&options.contains("SAME")==false){
            cd(this.activePad + 1);
        }
    }
    
    public EmbeddedCanvas draw(DataLine line){
        //System.out.println("--- canvas adding line");
        this.getPad(activePad).draw(line); return this;
    }
    /**
     * draws dataset on current active pad with no options
     *
     * @param ds data set to be drawn
     * @return 
     */
    public EmbeddedCanvas draw(IDataSet ds) {
        draw(ds, ""); return this;    
    }

    public void draw(LatexText text) {
        this.getPad(activePad).addLatex(text);
    }
    /**
     * draws data set on current active pad with given options
     *
     * @param ds data set to be drawn
     * @param options drawing options
     * @return 
     */
    public EmbeddedCanvas draw(IDataSet ds, String options) {
        this.getPad(activePad).draw(ds, options);
        update();
        return this;
    }
    
    public EmbeddedCanvas draw(List<H1F> hList, String options){
        for(int i = 0; i < hList.size(); i++){
            this.draw(hList.get(i), options);
            if(options.contains("same")==false){
                this.next();
            }
        }
        return this;
    }
    /**
     * updates pad margins when the canvas is resized and sets boundaries for
     * each pad
     *
     * @param w width of current canvas
     * @param h height of current canvas
     */
    private void updateCanvasPads(int w, int h) {
        int pcounter = 0;
        int startX = 5;
        int minY = 5;
        int rW = w - startX;
        int rH = h - minY;

        for (int ir = 0; ir < ec_ROWS; ir++) {
            for (int ic = 0; ic < ec_COLUMNS; ic++) {
                double x = ic * (rW / ((double) ec_COLUMNS));
                double xe = (ic + 1) * (rW / ((double) ec_COLUMNS));
                double y = ir * (rH / ((double) ec_ROWS));
                double ye = (ir + 1) * (rH / ((double) ec_ROWS));
                // System.out.println("PAD " + pcounter + " " + x + " " + xe );
                //System.out.printf("Pad: "+pcounter+" %d %d %d %d\n",(int) x + startX, (int) y - minY,
                //        (int) (xe-x), (int) (ye-y));
                canvasPads.get(pcounter).setDimension((int) x + startX, (int) y - minY,
                        (int) (xe - x), (int) (ye - y));

                pcounter++;
            }
        }
    }

    public int getNColumns() {
        return ec_COLUMNS;
    }

    public int getNRows() {
        return ec_ROWS;
    }

    /**
     * painting all components on the Graphics2D object.
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        try {
            Long st = System.currentTimeMillis();
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = this.getSize().width;
            int h = this.getSize().height;
            
            g2d.setColor(GStyle.getCanvasBackgroundColor());
            
            //g2d.setColor(Color.WHITE);
//            g2d.setColor(new Color(230,230,230));
            g2d.fillRect(0, 0, w, h);
            updateCanvasPads(w, h);

            PadMargins margins = new PadMargins();
            //System.out.println(" margins ");
            for (int i = 0; i < canvasPads.size(); i++) {
                EmbeddedPad pad = canvasPads.get(i);
                pad.getAxisFrame().updateMargins(g2d);
                //pad.getAxisFrame().setAxisMargins(pad.getAxisFrame().getFrameMargins());
                margins.marginFit(pad.getAxisFrame().getFrameMargins());
            }

            for (int i = 0; i < canvasPads.size(); i++) {
                EmbeddedPad pad = canvasPads.get(i);
                //pad.setDimension(0, 0, w, h);                        
                //System.out.println("PAD " + i + " " + pad.getAxisFrame().getFrameMargins());
                //pad.getAxisFrame().setAxisMargins(pad.getAxisFrame().getFrameMargins());                
                //System.out.println(pad.getAxisFrame().getFrameMargins());
                pad.getAxisFrame().setAxisMargins(margins);
                pad.setMargins(margins);
                margins.marginFit(pad.getAxisFrame().getFrameMargins());

                //pad.draw(g2d);
            }
            for (int i = 0; i < canvasPads.size(); i++) {
                EmbeddedPad pad = canvasPads.get(i);
                pad.getAxisFrame().setAxisMargins(margins);
                pad.setMargins(margins);
                pad.draw(g2d);
            }

            Long et = System.currentTimeMillis();
            Long paintingTime = (et - st);
            paintingTimeSum += paintingTime;
            samples++;
            double average = (double) paintingTimeSum / (double) samples;
            if (showFPS) {
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, 70, 32);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(0, 0, 70, 32);
                g2d.setColor(Color.BLUE);
                g2d.drawString(String.format("%d FPS", (int) (1.0 / (((double) paintingTime) / 1000.0))), 5, 14);
                g2d.drawString(String.format("%4.2f Avg", (1.0 / ((average) / 1000.0))), 5, 28);

                //System.out.println("Painting time: "+paintingTime+"ms");
            }
            paintingTime += paintingTime;
            numberOfPaints++;
        } catch (Exception e) {
            System.out.println("[EmbeddedCanvas] ---> ooops");
            System.out.println(e);
        }
    }

    /**
     * returns EmbeddedPad object with given index
     *
     * @param index index of the EmbeddedPad
     * @return
     */
    public EmbeddedPad getPad(int index) {
        return this.canvasPads.get(index);
    }

    public EmbeddedPad getPad() {
        return this.canvasPads.get(this.activePad);
    }

    /**
     * forces repaint method for the entire canvas
     */
    public void update() {
        this.repaint();
        if(this.children.size()>0) {
        	for(EmbeddedCanvas child : children){
        		child.update();
        	}
        }
        //System.out.println(this.getBenchmarkString());
    }

    /**
     * returns benchmark string containing number of repaint operations
     * performed as well as the time it took to repaint.
     *
     * @return
     */
    public String getBenchmarkString() {
        StringBuilder str = new StringBuilder();
        double time = (double) paintingTime;

        double ms = (time / numberOfPaints);
        if (numberOfPaints == 0) {
            ms = 1000.0;
        }

        str.append(String.format("Time = %.2f ms Total Time = %d , Events = %d",
                ms, paintingTime, numberOfPaints));
        return str.toString();
    }

    public void setAxisFontSize(int size) {
        for (EmbeddedPad pad : canvasPads) {
            pad.setAxisFontSize(size);
        }
    }

    public void showFPS(boolean benchmark) {
        this.showFPS = benchmark;
    }

    public void initTimer(int interval) {
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
        this.paintingTime = 0L;
        this.numberOfPaints = 0L;
    }

    public int getPadByXY(int x, int y) {
        int rowSize = (int) this.getHeight() / this.ec_ROWS;
        int row = (int) (y / rowSize);
        int colSize = (int) this.getWidth() / this.ec_COLUMNS;
        int col = (int) (x / colSize);
        //System.out.println("x:"+x+" y:"+y+" rowSize:"+rowSize+" colSize:"+colSize+" row:"+row+" col:"+col+" can:"+(row*ec_ROWS + col)+" rows:"+ec_ROWS+" cols:"+ec_COLUMNS);
        return row * ec_COLUMNS + col;
    }

    public void draw(DataGroup group) {

        int nrows = group.getRows();
        int ncols = group.getColumns();
        this.divide(ncols, nrows);

        int nds = nrows * ncols;
        for (int i = 0; i < nds; i++) {
            List<IDataSet> dsList = group.getData(i);
            //System.out.println(" pad = " + i + " size = " + dsList.size());
            this.cd(i);
            for (IDataSet ds : dsList) {
                //System.out.println("\t --> " + ds.getName());
                this.draw(ds, "same");
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                repaint();
            }
        });

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //int pad = this.getPadByXY(e.getX(),e.getY());
        //System.out.println("you're hovering over pad = " + pad);
    }
    int fillcolortemp = 1;

    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (e.getClickCount() == 2 && !isChild) {
            int pad = this.getPadByXY(e.getX(), e.getY());
            double scale = 1.5;
            //System.out.println("you double clicked on " + pad);
            JFrame popoutFrame = new JFrame();
            EmbeddedCanvas can = new EmbeddedCanvas();
            EmbeddedPad embeddedPad = this.getPad(pad).getCopy();
            can.showFPS(this.showFPS);
            this.children.add(can);
            int xSize = (int) (this.getPad(pad).getWidth());
            int ySize = (int) (this.getPad(pad).getHeight());

            can.setPreferredSize(new Dimension((int) (xSize * scale), (int) (ySize * scale)));
            can.setChild(true);
            ArrayList<EmbeddedPad> pads = new ArrayList<EmbeddedPad>();
            pads.add(embeddedPad);
            can.canvasPads = pads;
            popoutFrame.setContentPane(can);
            // dialogWin.setSize(400, 400);
            popoutFrame.pack();
            popoutFrame.setLocation(new Point(e.getX(), e.getY()));
            popoutFrame.setVisible(true);
        }
        if (e.getClickCount() == 1 && e.getButton() == 1) {
            //System.out.println("Left click!");
           /* if (selectedDataset != null) {
                //-- gagik selectedDataset.getAttributes().setFillColor(fillcolortemp);
                this.repaint();
            }
            selectedDataset = null;
            for (EmbeddedPad pad : this.canvasPads) {
                if (pad.getAxisFrame().getFrameDimensions().contains(e.getX(), e.getY())) {
                    //System.out.println("This pad contains the click");
                    if (pad.getDatasetPlotters().size() > 0) {
                        for (IDataSetPlotter plotter : pad.getDatasetPlotters()) {
                            if (plotter instanceof HistogramPlotter) {
                                HistogramPlotter temp = (HistogramPlotter) plotter;
                                if (temp.path.contains(e.getX(), e.getY())) {
                                    System.out.println("You clicked on:" + temp.getName());
                                    if (selectedDataset != temp) {
                                        if (selectedDataset != null) {
                                            //-- gagik selectedDataset.getAttributes().setFillColor(fillcolortemp);
                                        }
                                        fillcolortemp = temp.getDataSet().getAttributes().getFillColor();
                                        selectedDataset = temp.getDataSet();
                                        if (fillcolortemp < 10) {
                                            //-- gagik temp.getDataSet().getAttributes().setFillColor(fillcolortemp + 10);
                                        } else {
                                            //-- gagik temp.getDataSet().getAttributes().setFillColor(fillcolortemp - 10);
                                        }
                                        this.repaint();
                                    }
                                }
                            }
                        }
                    }
                }
            }*/
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            popupPad = getPadByXY(e.getX(), e.getY());
            //System.out.println("POP-UP coordinates = " + e.getX() + " " + e.getY() + "  pad = " + popupPad);
            createPopupMenu();
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

    private void createPopupMenu() {
        this.popup = new JPopupMenu();
        JMenuItem itemCopy = new JMenuItem("Copy Canvas");
        JMenuItem itemCopyPad = new JMenuItem("Copy Pad");
        JMenuItem itemPaste = new JMenuItem("Paste Pad");
        
        JMenuItem itemInvertGray  = new JMenuItem("Gray Background");
        JMenuItem itemInvertWhite = new JMenuItem("White Background");
        
        
        JMenuItem itemSave = new JMenuItem("Save");
        JMenuItem itemSaveAs = new JMenuItem("Save As...");
        JMenuItem itemFitPanel = new JMenuItem("Fit Panel");
        JMenuItem itemOptions = new JMenuItem("Options");
        JMenuItem itemOpenWindow = new JMenuItem("Open in New Window");
        JMenu itemFitSlices = new JMenu("Fit Slices");
        JMenu itemFitSlicesX = new JMenu("X Axis");
        JMenu itemFitSlicesY = new JMenu("Y Axis");
        itemFitSlices.add(itemFitSlicesX);
        itemFitSlices.add(itemFitSlicesY);
        JMenuItem itemFitSlicesgausx = new JMenuItem("gaus");
        JMenuItem itemFitSlicesgausp0x = new JMenuItem("gaus+p0");
        JMenuItem itemFitSlicesgausp1x = new JMenuItem("gaus+p1");
        JMenuItem itemFitSlicesgausp2x = new JMenuItem("gaus+p2");
        JMenuItem itemFitSlicesgausp3x = new JMenuItem("gaus+p3");
        itemFitSlicesX.add(itemFitSlicesgausx);
        itemFitSlicesX.add(itemFitSlicesgausp0x);
        itemFitSlicesX.add(itemFitSlicesgausp1x);
        itemFitSlicesX.add(itemFitSlicesgausp2x);
        itemFitSlicesX.add(itemFitSlicesgausp3x);
        JMenuItem itemFitSlicesgausy = new JMenuItem("gaus");
        JMenuItem itemFitSlicesgausp0y = new JMenuItem("gaus+p0");
        JMenuItem itemFitSlicesgausp1y = new JMenuItem("gaus+p1");
        JMenuItem itemFitSlicesgausp2y = new JMenuItem("gaus+p2");
        JMenuItem itemFitSlicesgausp3y = new JMenuItem("gaus+p3");
        itemFitSlicesY.add(itemFitSlicesgausy);
        itemFitSlicesY.add(itemFitSlicesgausp0y);
        itemFitSlicesY.add(itemFitSlicesgausp1y);
        itemFitSlicesY.add(itemFitSlicesgausp2y);
        itemFitSlicesY.add(itemFitSlicesgausp3y);

        itemCopy.addActionListener(this);
        itemCopyPad.addActionListener(this);
        itemSave.addActionListener(this);
        itemSaveAs.addActionListener(this);
        itemInvertGray.addActionListener(this);
        itemInvertWhite.addActionListener(this);
        itemFitPanel.addActionListener(this);
        itemOptions.addActionListener(this);
        itemOpenWindow.addActionListener(this);
        itemPaste.addActionListener(this);
        
        itemFitSlicesgausp3x.addActionListener(e -> fitSlices(0, ParallelSliceFitter.P3_BG));
        itemFitSlicesgausp2x.addActionListener(e -> fitSlices(0, ParallelSliceFitter.P2_BG));
        itemFitSlicesgausp1x.addActionListener(e -> fitSlices(0, ParallelSliceFitter.P1_BG));
        itemFitSlicesgausp0x.addActionListener(e -> fitSlices(0, ParallelSliceFitter.P0_BG));
        itemFitSlicesgausx.addActionListener(e -> fitSlices(0, ParallelSliceFitter.NO_BG));
        itemFitSlicesgausp3y.addActionListener(e -> fitSlices(1, ParallelSliceFitter.P3_BG));
        itemFitSlicesgausp2y.addActionListener(e -> fitSlices(1, ParallelSliceFitter.P2_BG));
        itemFitSlicesgausp1y.addActionListener(e -> fitSlices(1, ParallelSliceFitter.P1_BG));
        itemFitSlicesgausp0y.addActionListener(e -> fitSlices(1, ParallelSliceFitter.P0_BG));
        itemFitSlicesgausy.addActionListener(e -> fitSlices(1, ParallelSliceFitter.NO_BG));

        JMenu itemPalettes = new JMenu("Set Palette");

        for (String palName : new String[]{"kDefault","kRainBow",
            "kVisibleSpectrum","kDarkBodyRadiator","kInvertedDarkBodyRadiator"} ) {
            JMenuItem palItem = new JMenuItem(palName);
            palItem.addActionListener(e -> {
                this.getPad(popupPad).setPalette(palName);
                repaint();
            });
            itemPalettes.add(palItem);
        }

        JMenu itemExtPalList = new JMenu("Extended list");

        String[] palNames = Stream.of(TColorPalette.PaletteName.values())
                .map(Enum::name)
                .sorted()
                .toArray(String[]::new);
//                .collect(Collectors.toList());
        
        JList<String> palList = new JList(palNames);
        JScrollPane palScroll = new JScrollPane(palList);
        itemExtPalList.add(palScroll);
        itemPalettes.add(itemExtPalList);

        palList.addListSelectionListener(e -> {
            this.getPad(popupPad).setPalette(palList.getSelectedValue());
            repaint();
        });

        List<IDataSetPlotter> plotters = this.getPad(this.popupPad).getDatasetPlotters();
        boolean containsH2F = false;
        for (int i = 0; i < plotters.size(); i++) {
            if (plotters.get(i) instanceof Histogram2DPlotter) {
                containsH2F = true;
                break;
            }
            System.out.println(plotters.get(i).getName());
        }

        this.popup.add(itemCopyPad);
        this.popup.add(itemPaste);
        if (containsH2F) {
            this.popup.add(itemPalettes);
        }

        
        this.popup.add(new JSeparator());
        this.popup.add(itemInvertGray);
        this.popup.add(itemInvertWhite);
        this.popup.add(new JSeparator());
        this.popup.add(itemCopy);
        this.popup.add(itemSave);
        this.popup.add(itemSaveAs);
        this.popup.add(new JSeparator());
        this.popup.add(itemFitPanel);
        if (containsH2F) {
            this.popup.add(itemFitSlices);
        }
        this.popup.add(new JSeparator());
        this.popup.add(itemOptions);
        //this.popup.add(itemOpenWindow);
        //addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("action performed " + e.getActionCommand());

        if (e.getActionCommand().compareTo("Options") == 0) {
            this.openOptionsPanel(popupPad);
        }
        
        if (e.getActionCommand().compareTo("Gray Background") == 0) {
            GStyle.setCanvasBackgroundColor(new Color(230,230,230));
            this.update();
        }
        
        if (e.getActionCommand().compareTo("White Background") == 0) {
            GStyle.setCanvasBackgroundColor(new Color(255,255,255));
            this.update();
        }
        
        if (e.getActionCommand().compareTo("Fit Panel") == 0) {
            this.openFitPanel(popupPad);
        }
        if (e.getActionCommand().compareTo("Copy Canvas") == 0) {
            this.copyToClipboard();
        }
        if (e.getActionCommand().compareTo("Paste Pad") == 0) {
            this.paste(popupPad);
        }
        if (e.getActionCommand().compareTo("Copy Pad") == 0) {
            this.copyToClipboard(popupPad);
        }
        if (e.getActionCommand().compareTo("Save") == 0) {
            File desktop = new File(System.getProperty("user.home"), "Desktop");
            DateFormat df = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss_aa");
            String data = df.format(new Date());
            this.save(desktop.getAbsolutePath() + File.separator + "Plot_" + data + ".png");
            System.out.println("Saved File:" + desktop.getAbsolutePath() + File.separator + "Plot_" + data + ".png");
        }
        if (e.getActionCommand().compareTo("Save As...") == 0) {
            final JFileChooser fc = new JFileChooser("Save As...");
            DateFormat df = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss_aa");
            String data = df.format(new Date());
            //this.save(desktop.getAbsolutePath() +File.separator+"Plot_"+data+".png");

            String path = GStyle.getWorkingDirectory() + File.separator + "Plot_" + data;

            FileFilter filterPNG = new FileNameExtensionFilter("PNG File", "png");
            fc.addChoosableFileFilter(filterPNG);

            FileFilter filterPDF = new FileNameExtensionFilter("PDF File", "pdf");
            fc.addChoosableFileFilter(filterPDF);

            FileFilter filterSVG = new FileNameExtensionFilter("SVG File", "svg");
            fc.addChoosableFileFilter(filterSVG);

            FileFilter filterTXT = new FileNameExtensionFilter("TXT File", "txt");
            fc.addChoosableFileFilter(filterTXT);

            FileFilter filterHIPO = new FileNameExtensionFilter("HIPO File", "hipo");
            fc.addChoosableFileFilter(filterHIPO);

            fc.setSelectedFile(new File(path + ".png"));
            fc.setFileFilter(filterPNG);

            fc.addPropertyChangeListener(JFileChooser.FILE_FILTER_CHANGED_PROPERTY, evt -> {
//                String currentPath = fc.getSelectedFile().getName();
//                System.out.println(currentPath);
//                File selectedFile = fc.getSelectedFile();
//                String currentPath = selectedFile.getPath();
//                currentPath = currentPath.substring(0, currentPath.lastIndexOf("."));

                FileNameExtensionFilter currentFilter = (FileNameExtensionFilter)evt.getNewValue();
                fc.setSelectedFile(new File(path + "." + currentFilter.getExtensions()[0]));
            });

            //In response to a button click:
            int returnVal = fc.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if (file.exists() == true) {
                    //JOptionPane.showMessageDialog(this, "Error. The file already exists....");
                    int result = JOptionPane.showConfirmDialog(this, "File already exists, would you like to overwrite it?",
                            "alert", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        if (fc.getFileFilter() == filterPNG)
                            this.save(file.getAbsolutePath(), SaveType.PNG);
                        if (fc.getFileFilter() == filterTXT)
                            this.save(file.getAbsolutePath(), SaveType.TXT);
                        if (fc.getFileFilter() == filterHIPO)
                            this.save(file.getAbsolutePath(), SaveType.HIPO);
                        if (fc.getFileFilter() == filterPDF)
                            this.save(file.getAbsolutePath(), SaveType.PDF);
                        if (fc.getFileFilter() == filterSVG)
                            this.save(file.getAbsolutePath(), SaveType.SVG);
                        GStyle.setWorkingDirectory(file.getParent());
                    }
                } else {
                    //System.out.println("saving file : " + file.getAbsolutePath());
                    if (fc.getFileFilter() == filterPNG)
                        this.save(file.getAbsolutePath(), SaveType.PNG);
                    if (fc.getFileFilter() == filterTXT)
                        this.save(file.getAbsolutePath(), SaveType.TXT);
                    if (fc.getFileFilter() == filterHIPO)
                        this.save(file.getAbsolutePath(), SaveType.HIPO);
                    if (fc.getFileFilter() == filterPDF)
                        this.save(file.getAbsolutePath(), SaveType.PDF);
                    if (fc.getFileFilter() == filterSVG)
                        this.save(file.getAbsolutePath(), SaveType.SVG);
                    GStyle.setWorkingDirectory(file.getParent());
                }
            }
        }/*
        if(e.getActionCommand().compareTo("Open in New Window")==0){
        	this.openInNewWindow(popupPad);
        }*/

    }

    private void openFitPanel(int popupPad2) {
        JFrame frame = new JFrame("Fit Panel");
        frame.setLayout(new BorderLayout());
        FitPanel mainPanel = new FitPanel(this, popupPad2);
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }

    private void fitSlices(int axis, int mode) {
        List<IDataSetPlotter> plotters = this.getPad(this.popupPad).getDatasetPlotters();
        H2F histogram = null;
        boolean containsH2F = false;
        for (int i = 0; i < plotters.size(); i++) {
            if (plotters.get(i) instanceof Histogram2DPlotter) {
                histogram = (H2F) (plotters.get(i).getDataSet());
            }
        }
        ParallelSliceFitter fitter = new ParallelSliceFitter(histogram);
        Dimension1D range = null;
        if (axis == 0) {
            range = this.getPad(this.popupPad).getAxisY().getRange();
        }
        if (axis == 1) {
            range = this.getPad(this.popupPad).getAxisX().getRange();
        }
        fitter.setRange(range.getMin(), range.getMax());
        fitter.setBackgroundOrder(mode);
        if (axis == 0) {
            fitter.fitSlicesX();
        }
        if (axis == 1) {
            fitter.fitSlicesY();
        }
        fitter.inspectFits();
    }

    private void paste(int popupPad2) {
        DataFlavor dmselFlavor = new DataFlavor(EmbeddedPad.class, "EmbeddedPad");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipboardContent = clipboard.getContents(null);

        DataFlavor[] flavors = clipboardContent.getTransferDataFlavors();
        System.out.println("flavors.length = " + flavors.length);
        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(dmselFlavor)) {
                System.out.println("We have a match!");
                try {
                    EmbeddedPad pad = (EmbeddedPad) clipboardContent.getTransferData(dmselFlavor);
                    if(pad.getDatasetPlotters().size()>0){
                        /*System.out.println("---------------------------- PASTING");
                        this.getPad(popupPad2).getAxisFrame().getAxisZ().getAttributes().setShowAxis(false);
                        
                        this.getPad(popupPad2).draw(pad.getDatasetPlotters().get(0).getDataSet(), "same");
                        */
                        for (int j = 0; j < pad.getDatasetPlotters().size(); j++) {
                            this.getPad(popupPad2).getDatasetPlotters().add(pad.getDatasetPlotters().get(j));
                        }
                    }
                    this.update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (flavors[i].equals(DataFlavor.stringFlavor)) {
                try {
                    String blah = (String) clipboardContent.getTransferData(DataFlavor.stringFlavor);
                    DataParser parser = new DataParser(blah);
                    this.getPad(popupPad2).draw(parser.getGraphErrors(), "same");
                    this.update();
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void openOptionsPanel(int popupPad2) {
        JFrame frame = new JFrame("Options Panel");
        frame.setLayout(new BorderLayout());
        OptionsPanel mainPanel = new OptionsPanel(this, popupPad2);
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);

    }

    public BufferedImage getScreenShot() {
        BufferedImage bi = new BufferedImage(
                this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
        this.paint(bi.getGraphics());
        return bi;
    }

    private BufferedImage getScreenShot(int pad) {
        //BufferedImage bi = new BufferedImage(
        //    (int)this.getPad(index).getWidth(), (int)this.getPad(index).getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
        //BufferedImage bi = new BufferedImage(
        //       this.getPad(pad).getWidth(), this.getPad(pad).getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
        double scale = 1.0;
        //System.out.println("you double clicked on " + pad);
        //JDialog  dialogWin = new JDialog();
        EmbeddedCanvas can = new EmbeddedCanvas();
        EmbeddedPad embeddedPad = this.getPad(pad).getCopy();
        int xSize = (int) (this.getPad(pad).getWidth());
        int ySize = (int) (this.getPad(pad).getHeight());

        can.setPreferredSize(new Dimension((int) (xSize * scale), (int) (ySize * scale)));
        can.setMinimumSize(new Dimension((int) (xSize * scale), (int) (ySize * scale)));
        can.setSize(new Dimension((int) (xSize * scale), (int) (ySize * scale)));
        can.setChild(true);
        ArrayList<EmbeddedPad> pads = new ArrayList<EmbeddedPad>();
        pads.add(embeddedPad);
        can.canvasPads = pads;
        // dialogWin.setContentPane(can);
        // dialogWin.setSize(400, 400);
        //dialogWin.pack();
        return can.getScreenShot();
    }

    public void copyToClipboard() {
        TransferableImage trans = new TransferableImage(getScreenShot());
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents(trans, null);
    }

    private void copyToClipboard(int popupPad) {
        TransferableImage trans = new TransferableImage(getScreenShot(popupPad));
        trans.setPad(this.getPad(popupPad));
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents(trans, null);
    }

    public void save(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        switch(extension) {
            case "pdf":
                save(filename, SaveType.PDF);
                break;
            case "svg":
                save(filename, SaveType.SVG);
                break;
            case "txt":
                save(filename, SaveType.TXT);
                break;
            case "hipo":
                save(filename, SaveType.HIPO);
                break;
            default:
                save(filename, SaveType.PNG);
                break;
        }
    }

    public void save(String filename, SaveType saveType) {

        if (saveType == SaveType.PNG) {
            File imageFile = new File(filename);
            try {
                imageFile.createNewFile();
                ImageIO.write(getScreenShot(), "png", imageFile);
            } catch (Exception ignored) {
            }
        }

        if (saveType == SaveType.TXT) {
            String extension = filename.substring(filename.lastIndexOf("."));
            filename = filename.substring(0, filename.lastIndexOf("."));

            List<IDataSetPlotter> plotters = this.getPad(popupPad).datasetPlotters;
            for (int k = 0; k < plotters.size(); k++) {
                IDataSet data = plotters.get(k).getDataSet();
//            System.out.println(data);
                data.save(filename + "_" + k + "_" + data.getName() + extension);
            }
        }

        if (saveType == SaveType.HIPO) {
            TDirectory tdir = new TDirectory();
            tdir.mkdir("data");
            tdir.cd("data");

            List<IDataSetPlotter> plotters = this.getPad(popupPad).datasetPlotters;
            for (IDataSetPlotter plotter : plotters)
                tdir.addDataSet(plotter.getDataSet());

            tdir.writeFile(filename);
        }

        if (saveType == SaveType.PDF) {
            PDFDocument pdfDoc = new PDFDocument();
            Page page = pdfDoc.createPage(new Rectangle(this.getSize().width, this.getSize().height));
            PDFGraphics2D g2 = page.getGraphics2D();
            this.paint(g2);
            pdfDoc.writeToFile(new File(filename));
        }

        if (saveType == SaveType.SVG) {
            SVGGraphics2D g2 = new SVGGraphics2D(this.getSize().width, this.getSize().height);
            this.paint(g2);
            try {
                SVGUtils.writeToSVG(new File(filename), g2.getSVGElement());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        EmbeddedCanvas canvas = new EmbeddedCanvas();
        //canvas.divide(2, 2);
        canvas.setAxisFontSize(14);
        //canvas.getPad(0).getAxisFrame().getAxisX().setAxisFontSize(18);
        //canvas.getPad(1).getAxisFrame().getAxisY().setAxisFontSize(18);
        //canvas.getPad(0).getAxisFrame().setDrawAxisZ(true);

        //  H1F h1  = FunctionFactory.createDebugH1F(6);
        // H1F h2  = FunctionFactory.randomGausian(100, 0.4, 5.6, 200000, 2.3, 0.8);
        H1F h2b = FunctionFactory.randomGausian(100, 0.4, 5.6, 80000, 4.0, 0.8);
        H2F h2d = FunctionFactory.randomGausian2D(24, 0.4, 5.6, 800000, 2.3, 0.8);
        H2F h2d2 = new H2F("h2d2", 100, -180, 180, 24, -180, 180);
        DataGroup group = new DataGroup(2, 1);
        h2b.setName("h2b");
        GraphErrors hprofile = h2d.getProfileX();
        group.addDataSet(h2d, 0);
        group.addDataSet(hprofile, 1);
        for (int i = 0; i < 20000; i++) {
            h2d2.fill(Math.random() * 360.0 - 180.0, Math.random() * 360.0 - 180.0);
        }
        //canvas.draw(group);
        canvas.draw(h2d2);
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
        for (EmbeddedPad pad : canvasPads) {
            pad.setFontNameAll(fontName);
        }
    }

    public void setTitleSize(int fontSize) {
        for (EmbeddedPad pad : canvasPads) {
            pad.setTitleFontSize(fontSize);
        }
    }

    public void setAxisLabelSize(int fontSize) {
        for (EmbeddedPad pad : canvasPads) {
            pad.setAxisLabelFontSize(fontSize);
        }
    }

    public void setAxisTitleSize(int fontSize) {
        for (EmbeddedPad pad : canvasPads) {
            pad.setAxisTitleFontSize(fontSize);
        }
    }

    public void setStatBoxFontSize(int fontSize) {
        for (EmbeddedPad pad : canvasPads) {
            pad.setStatBoxFontSize(fontSize);
        }
    }

    public void setPadTitles(String title) {
        for (EmbeddedPad pad : canvasPads) {
            pad.setTitle(title);
        }
    }

    public void setPadTitlesX(String title) {
        for (EmbeddedPad pad : canvasPads) {
            pad.getAxisY().setTitle(title);
        }
    }

    public void setPadTitlesY(String title) {
        for (EmbeddedPad pad : canvasPads) {
            pad.getAxisY().setTitle(title);
        }
    }

    public void setGridX(boolean isGrid) {
        for (EmbeddedPad pad : canvasPads) {
            pad.getAxisX().setGrid(isGrid);
        }
    }

    public void setGridY(boolean isGrid) {
        for (EmbeddedPad pad : canvasPads) {
            pad.getAxisY().setGrid(isGrid);
        }
    }

}
