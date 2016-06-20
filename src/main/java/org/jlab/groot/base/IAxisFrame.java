/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.base;

import org.jlab.groot.math.Dimension1D;

/**
 *
 * @author gavalian
 */
public interface IAxisFrame {
    Dimension1D  getAxis(int axis);
}
