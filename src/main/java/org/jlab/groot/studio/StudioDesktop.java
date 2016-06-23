/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.studio;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author gavalian
 */
public class StudioDesktop extends JFrame implements ActionListener {
    
    JDesktopPane  desktop = null;
    
    public StudioDesktop(){
        super();
        initDesktop();
        this.add(desktop);
        initMenu();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(900,700);
        this.setVisible(true);
    }
    
    private void initDesktop(){
        desktop = new JDesktopPane(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }
    
    private void initMenu(){
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem processItem = new JMenuItem("Process File");
        processItem.addActionListener(this);
        fileMenu.add(processItem);
        bar.add(fileMenu);
        
        JMenu pluginMenu = new JMenu("Plugins");
        JMenuItem loadPlugin = new JMenuItem("Tree Viewer");
        loadPlugin.addActionListener(this);
        pluginMenu.add(loadPlugin);
        
        bar.add(pluginMenu);
        
        this.setJMenuBar(bar);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().compareTo("Tree Viewer")==0){
            System.out.println("[Desktop] --> openning a Tree Viewer");
            GROOTStudio studio = new GROOTStudio();
            studio.getFrame().setVisible(true);
            //studio.getFrame().setContentPane(desktop);
            this.desktop.add(studio.getFrame());
        }
    }
    
    public static void main(String[] args){
        StudioDesktop desktop = new StudioDesktop();
    }
}
