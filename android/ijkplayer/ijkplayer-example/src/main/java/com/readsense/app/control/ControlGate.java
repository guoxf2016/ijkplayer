package com.readsense.app.control;

public class ControlGate {
    static {
        System.loadLibrary("control_gate");
    }
    public static native int sendCmd(String ip, int flag);
}
