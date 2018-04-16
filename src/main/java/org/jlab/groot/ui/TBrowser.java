/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.IDataSet;
import org.jlab.groot.data.TDirectory;
import org.jlab.groot.graphics.EmbeddedCanvas;
import org.jlab.groot.graphics.EmbeddedCanvasTabbed;
import org.jlab.groot.math.FunctionFactory;

/**
 *
 * @author gavalian
 */
public class TBrowser extends JFrame implements ActionListener {
    
    JPanel  mainPanel = new JPanel();
    private JMenuBar menuBar = null;
    private JTree           canvasTree;
    private JSplitPane      splitPane;
    private int             lastCanvasNumber = 0;
    private Map<String,EmbeddedCanvas>  canvasMap = new LinkedHashMap<String,EmbeddedCanvas>();
    private EmbeddedCanvasTabbed        canvasTabbed = null;
    
    private TDirectory      browserDir = null;
    
    JTabbedPane tabbedPane;
    
    List<EmbeddedCanvas>  canvasList = new ArrayList<EmbeddedCanvas>();
    
    public TBrowser(){
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.initUI();
        this.initMenu();
        this.addCanvas();
        this.addCanvas();
        this.initTree();
        this.setSize(600,400);
        this.setVisible(true);
    }
    
     public TBrowser(TDirectory dir){
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.initUI();
        this.initMenu();
        this.addCanvas();
        this.addCanvas();
        this.initTree();
        this.setDirectory(dir);
        this.setSize(600,400);
        this.setVisible(true);
    }
    
    public final void setDirectory(TDirectory dir){
        this.browserDir = dir;
        DefaultMutableTreeNode root = dir.getTreeNode();
        this.updateTreeModel(root);
    }
    
    
    private void initMenu(){
        menuBar = new JMenuBar();
        JMenu menuFile   = new JMenu("File");
        JMenuItem miOpen = new JMenuItem("Open");
        miOpen.addActionListener(this);
        menuFile.add(miOpen);
        menuBar.add(menuFile);
        this.setJMenuBar(menuBar);
    }
    
    private void initUI(){
        splitPane = new JSplitPane();
        splitPane.setDividerLocation(200);
        canvasTree = new JTree();
        
        JScrollPane scroll = new JScrollPane(canvasTree);
        splitPane.setLeftComponent(scroll);
        
        tabbedPane = new JTabbedPane();
        canvasTabbed = new EmbeddedCanvasTabbed();
        splitPane.setRightComponent(this.canvasTabbed);
        
        mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(splitPane,BorderLayout.CENTER);
        this.add(mainPanel);
    }
    
    
    private void initTree(){
        canvasTree.addMouseListener(new MouseAdapter() 
        {
             @Override
             public void mouseClicked(MouseEvent me) {
                 doMouseClicked(me);
             }
             
        });
    }
    
    public  void updateTreeModel(DefaultMutableTreeNode node){
        this.canvasTree.setModel(new DefaultTreeModel(node));
    }
    
   
    
    public void doMouseClicked(MouseEvent me){
        if(me.getClickCount()==2){
            TreePath tp = canvasTree.getPathForLocation(me.getX(), me.getY());
            if (tp != null){
                System.out.println(" path -> : " + tp.toString());
                int nelements = tp.getPathCount();
                StringBuilder str = new StringBuilder();
                for(int i = 1; i < nelements; i++){
                    str.append("/");
                    str.append( tp.getPathComponent(i).toString());
                }
                String objectPath = str.toString();
                if(browserDir!=null){
                    if(browserDir.getObject(objectPath)!=null){
                        System.out.println("--> " + objectPath);
                        IDataSet data = browserDir.getObject(objectPath);
                        canvasTabbed.getCanvas().drawNext(data);
                        
                        canvasTabbed.getCanvas().update();
                        
                        //canvasList.get(0).drawNext(data);
                        //canvasList.get(0).update();
                    }
                }
            }
        }
    }
    
    public final void addCanvas(){
        String name = "Canvas " + lastCanvasNumber;
        lastCanvasNumber++;
        EmbeddedCanvas  c1 = new EmbeddedCanvas();
        c1.setName(name);
        c1.divide(2, 2);
        canvasList.add(c1);
        tabbedPane.addTab(name, c1);        
    }
    
    public static void main(String[] args){
       
        if(args.length>0){
            String filename = args[0];
            
            TDirectory dir = new TDirectory();
            
            dir.readFile(filename);
            
            TBrowser browser = new TBrowser(dir);
        } else {
        
        
        TBrowser br = new TBrowser();
        TDirectory dir = new TDirectory();
        dir.mkdir("/calibration/FTOF");
        dir.mkdir("/calibration/ECAL");
        dir.mkdir("/calibration/PCAL");
        
        H1F  h1    = FunctionFactory.randomGausian( 80, 0.1, 5.0,  8000, 2.2, 0.5);
        H1F  h2    = FunctionFactory.randomGausian(120, 0.1, 5.0, 20000, 3.5, 0.4);
        H1F  h3    = FunctionFactory.randomGausian(160, 0.1, 5.0, 14000, 2.6, 0.3);
        H1F  h4    = FunctionFactory.randomGausian( 80, 0.1, 5.0,  8000, 2.2, 0.5);
        H1F  h5    = FunctionFactory.randomGausian(160, 0.1, 5.0, 14000, 2.6, 0.3);
        H1F  h6    = FunctionFactory.randomGausian(160, 0.1, 5.0, 14000, 2.6, 0.3);
        
        h1.setName("h1");
        h2.setName("h2");
        h3.setName("h3");
        h4.setName("h4");
        h5.setName("h5");
        h6.setName("h6");
        
        h1.setFillColor(33);
        h2.setFillColor(34);
        h3.setFillColor(36);
        h4.setFillColor(35);
        h5.setFillColor(38);
        
        dir.cd("/calibration/FTOF");
        dir.addDataSet(h1,h2);
        dir.cd("/calibration/ECAL");
        dir.addDataSet(h3,h4);
        dir.cd("/calibration/PCAL");
        dir.addDataSet(h5,h6);
        
        br.setDirectory(dir);
        }
        /*
        DefaultMutableTreeNode node = dir.getTreeNode();
        br.updateTreeModel(node);
                */
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Open")==0){
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                System.out.println("Opening: " + file.getName() + "." + "\n"
                        + " path : " + file.getAbsolutePath());
                TDirectory dir = new TDirectory();
                dir.readFile(file.getAbsolutePath());
                this.setDirectory(dir);
            } else {
                System.out.println("Open command cancelled by user." + "\n");
            }
        }
    }
}
