package org.jlab.groot.data;

public enum SaveType {
    PNG(1), TXT(2), HIPO(3), PDF(4);

    final int type;
    SaveType(int type){
        this.type = type;
    }

}
