package com.open.charview;

import java.io.Serializable;

public class Chart implements Serializable {
    private String leftName;
    private String topName;
    private float rightName;
    private String bottomName;
    private String upperlimit;
    private float ratio;

    public String getLeftName() {
        return leftName;
    }

    public void setLeftName(String leftName) {
        this.leftName = leftName;
    }

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public float getRightName() {
        return rightName;
    }

    public void setRightName(float rightName) {
        this.rightName = rightName;
    }

    public String getBottomName() {
        return bottomName;
    }

    public void setBottomName(String bottomName) {
        this.bottomName = bottomName;
    }

    public String getUpperlimit() {
        return upperlimit;
    }

    public void setUpperlimit(String upperlimit) {
        this.upperlimit = upperlimit;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
}
