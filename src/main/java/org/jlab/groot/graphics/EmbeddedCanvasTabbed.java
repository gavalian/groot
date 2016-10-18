/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author gavalian
 */
public class EmbeddedCanvasTabbed extends JPanel implements ActionListener {
    
    private JTabbedPane   tabbedPane = null; 
    private JPanel       actionPanel = null;
    private int          canvasOrder = 1;
    
    private Map<String,EmbeddedCanvas>  tabbedCanvases = new LinkedHashMap<String,EmbeddedCanvas>();

    private int isDynamic = 0;
    
    public EmbeddedCanvasTabbed(){
        super();
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(200,200));
        this.setSize(400, 500);
        initUI();
    }
    
    private void initUI(){
        tabbedPane = new JTabbedPane();
        actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout());
        
        this.add(tabbedPane,BorderLayout.CENTER);
        this.add(actionPanel,BorderLayout.PAGE_END);
        this.addCanvas();
        
        JButton buttonAdd = new JButton("+");
        buttonAdd.setActionCommand("add canvas");
        buttonAdd.addActionListener(this);
        
        JButton buttonRemove = new JButton("-");
        buttonRemove.setActionCommand("remove canvas");
        buttonRemove.addActionListener(this);
        
        JButton buttonDivide = new JButton("/");
        buttonDivide.setActionCommand("divide");
        buttonDivide.addActionListener(this);
        
        actionPanel.add(buttonAdd);
        actionPanel.add(buttonRemove);
        actionPanel.add(buttonDivide);
    }
    
    public EmbeddedCanvas getCanvas(){
        int    index = tabbedPane.getSelectedIndex();
        String title = tabbedPane.getTitleAt(index);
        return this.tabbedCanvases.get(title);
    }
    
    public void addCanvas(){ 
        String name = "canvas" + canvasOrder;
        canvasOrder++;
        addCanvas(name);
    }
    
    public void addCanvas(String name){        
        EmbeddedCanvas canvas = new EmbeddedCanvas();
        this.tabbedCanvases.put(name, canvas);
        tabbedPane.addTab(name, canvas);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().compareTo("add canvas")==0){
            addCanvas();
        }
        
        if(e.getActionCommand().compareTo("remove canvas")==0){
            //addCanvas();
            if(JOptionPane.showConfirmDialog(this, 
                                "Are you sure to remove current canvas ?", "Really Removing?", 
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){                            
                int    index = tabbedPane.getSelectedIndex();
                String title = tabbedPane.getTitleAt(index);
                tabbedPane.remove(index);
                this.tabbedCanvases.remove(title);
            }
        }
        
        if(e.getActionCommand().compareTo("divide")==0){
            String[] options = new String[]{"1","2","3","4","5","6","7"};
            JComboBox columns = new JComboBox(options);
            JComboBox    rows = new JComboBox(options);
            Object[] message = {
                "Columns:", columns,
                "Rows:", rows
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Divide Canvas", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String stringCOLS = (String) columns.getSelectedItem();
                String stringROWS = (String) rows.getSelectedItem();
                this.getCanvas().divide(Integer.parseInt(stringCOLS), Integer.parseInt(stringROWS));
                //System.out.println("----> Splitting " + columns.getSelectedItem() + " " + rows.getSelectedItem());
            }
        }
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        EmbeddedCanvasTabbed canvasTab = new EmbeddedCanvasTabbed();
        frame.add(canvasTab);
        frame.pack();
        frame.setVisible(true);
    }

    
}
