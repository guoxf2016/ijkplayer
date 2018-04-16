package cn.readsense.body;

/**
 * Created by dou on 2017/8/3.
 */

public class Body {

    private float[] rect;

    private int isTrack;

    public float[] getRect() {
        return rect;
    }

    public void setRect(float[] rect) {
        this.rect = rect;
    }

    public boolean isTrack() {
        return isTrack == 1;
    }

    public void setIsTrack(int isTrack) {
        this.isTrack = isTrack;
    }
}
