/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.groot.data;

import org.jlab.groot.base.Attributes;
import org.jlab.groot.ui.PaveText;

/**
 *
 * @author gavalian
 */
public interface IDataSet {
    
    void        setName(String name);
    String      getName();
    int         getDataSize(int axis);
    Attributes  getAttributes();
    double      getDataX(int bin);
    double      getDataY(int bin);
    double      getDataEX(int bin);
    double      getDataEY(int bin);
    double      getData(int xbin, int ybin);
    PaveText    getStatBox();
}
