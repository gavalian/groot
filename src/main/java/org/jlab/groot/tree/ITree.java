/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jlab.groot.tree;

import java.util.List;

/**
 *
 * @author gavalian
 */
public interface ITree {
    String        getName();
    List<String>  getListOfBranches();
    Branch        getBranch(String name);
    int           getEntries();
    int           readEntry(int entry);
    void          reset();
    boolean       readNext();
    void          configure();
}
