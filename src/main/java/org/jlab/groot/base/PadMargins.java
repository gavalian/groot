/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.base;

/**
 *
 * @author gavalian
 */
public class PadMargins {
    int  leftMargin   = 0;
    int  rightMargin  = 0;
    int  topMargin    = 0;
    int  bottomMargin = 0;
    boolean isMarginsFixed    = false;
    
    
    public PadMargins(){
        
    }
    
    public boolean isFixed(){return isMarginsFixed;}
    public void    setFixed(boolean flag){ isMarginsFixed = flag;}
    public int  getLeftMargin(){ return leftMargin;}
    public int  getRightMargin(){ return rightMargin;}
    public int  getTopMargin() { return topMargin;}
    public int  getBottomMargin() { return bottomMargin;}
    public PadMargins setTopMargin(int margin){ 
        if(isMarginsFixed==false) topMargin = margin;
        return this;
    }
    public PadMargins setBottomMargin(int margin){ 
        if(isMarginsFixed==false) bottomMargin = margin;
        return this;
    }
    public PadMargins setLeftMargin(int margin){ 
        if(isMarginsFixed==false) leftMargin = margin;
        return this;
    }
    public PadMargins setRightMargin(int margin){ 
        if(isMarginsFixed==false) rightMargin = margin;
        return this;
    }
    
    public void copy(PadMargins p){
        setTopMargin(p.getTopMargin()).setBottomMargin(p.getBottomMargin());
        setLeftMargin(p.getLeftMargin()).setRightMargin(p.getRightMargin());
        setFixed(p.isFixed());
    }
    
    public void marginFit(PadMargins margin){
        if(margin.getTopMargin()>this.getTopMargin()){
            this.setTopMargin(margin.getTopMargin());
        }
        if(margin.getBottomMargin()>this.getBottomMargin()){
            this.setBottomMargin(margin.getBottomMargin());
        }        
        if(margin.getLeftMargin()>this.getLeftMargin()){
            this.setLeftMargin(margin.getLeftMargin());
        }
        if(margin.getRightMargin()>this.getRightMargin()){
            this.setRightMargin(margin.getRightMargin());
        }
    }
    
    @Override
    public String toString(){
        return String.format("( MARGINS ) TOP %d BOTTOM %d LEFT %d RIGTH %d", 
                this.topMargin, this.bottomMargin, this.leftMargin,
                this.rightMargin);
    }
}
