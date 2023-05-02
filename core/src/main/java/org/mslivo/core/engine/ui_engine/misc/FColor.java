package org.mslivo.core.engine.ui_engine.misc;

import java.io.Serializable;

public class FColor implements Serializable {

    public final float r,g,b,a;

    public FColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}