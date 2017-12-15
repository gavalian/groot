package org.jlab.groot.base;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class PadAttributes {

    private Color backgroundColor = Color.WHITE;
    private Color axisColor       = Color.RED;
    
    private FontProperties statBoxFont = new FontProperties();
    private FontProperties titleFont = new FontProperties();
    private String title = "";
    int titleOffset = 5;
    private PadMargins padMargins = new PadMargins();
    private int statBoxOffsetX = 0;
    private int statBoxOffsetY = 0;
    private int lineStyle = 1;
    private int lineWidth = 1;
    private TColorPalette palette = GStyle.getPalette();

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setAxisColor(Color c){
        this.axisColor = c;
    }
    
    public Color getAxisColor(){return this.axisColor;}
    public FontProperties getStatBoxFont() {
        return statBoxFont;
    }

    public void setStatBoxFont(FontProperties statBoxFont) {
        this.statBoxFont = statBoxFont;
    }

    public PadMargins getPadMargins() {
        return padMargins;
    }

    public void setPadMargins(PadMargins padMargins) {
        this.padMargins = padMargins;
    }

    public int getStatBoxOffsetX() {
        return statBoxOffsetX;
    }

    public void setStatBoxOffsetX(int statBoxOffsetX) {
        this.statBoxOffsetX = statBoxOffsetX;
    }

    public int getStatBoxOffsetY() {
        return statBoxOffsetY;
    }

    public void setStatBoxOffsetY(int statBoxOffsetY) {
        this.statBoxOffsetY = statBoxOffsetY;
    }

    public FontProperties getTitleFont() {
        return titleFont;
    }

    public void setTitleFont(FontProperties titleFont) {
        this.titleFont = titleFont;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitleOffset() {
        return titleOffset;
    }

    public void setTitleOffset(int titleOffset) {
        this.titleOffset = titleOffset;
    }

    public void setTitleFontName(String titleFont2) {
        titleFont.setFontName(titleFont2);
    }

    public int getTitleFontSize() {
        return titleFont.getFontSize();
    }

    public String getTitleFontName() {
        return titleFont.getFontName();
    }

    public void setTitleFontSize(int titleFontSize) {
        this.titleFont.setFontSize(titleFontSize);
    }

    public int getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public static class PadAttributesPane extends JPanel implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub

        }
    }

    public void setPalette(String palName) {
        palette.setPalette(palName);
    }

    public void setPalette(int palVal) {
        palette.setPalette(palVal);
    }

    public void setPalette(TColorPalette.PaletteName pal) {
        palette.setPalette(pal);
    }

    public TColorPalette getPalette() {
        return palette;
    }
}
