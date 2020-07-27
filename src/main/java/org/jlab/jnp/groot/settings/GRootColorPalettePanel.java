/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.groot.settings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author gavalian
 */
public class GRootColorPalettePanel extends JPanel implements MouseInputListener {
    private int WIDTH = 10;
    private int HEIGHT = 10;
    BufferedImage img = null;
    
    public GRootColorPalettePanel(){
        this.addMouseListener(this);
    }
    
    public void loadImage(String file){
        
        try {
            img = ImageIO.read(new File(file));
            WIDTH = img.getWidth();
            HEIGHT = img.getHeight();
            this.setSize(WIDTH, HEIGHT);
        } catch (IOException ex) {
            Logger.getLogger(GRootColorPalettePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void paint(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.drawImage(img, null, 0, 0);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GRootColorPalettePanel panel = new GRootColorPalettePanel();
        panel.loadImage("/Users/gavalian/Desktop/meterial_palette.png");
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        
        Color color = new Color(img.getRGB(x, y));
        System.out.printf("colorPalette.add(new Color(%d,%d,%d));\n",color.getRed(),color.getGreen(),color.getBlue());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
