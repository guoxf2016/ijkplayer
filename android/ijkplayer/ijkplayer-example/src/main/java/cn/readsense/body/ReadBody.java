package cn.readsense.body;

import android.content.Context;

import java.util.List;

/**
 * Created by dou on 2017/8/3.
 */

public class ReadBody {
    static {
        System.loadLibrary("readbody");
    }

    public static native long nativeCreateObject(Context context);

    public static native long nativeCreateObjectWithLicense(Context context,String appid,String appsecret);

    public static native void nativeDestroyObject();

    /**
     * @param yuv   从摄像头获取的yuv数据,支持两种格式，NV21，YV12
     * @param iw    摄像头预览的宽
     * @param ih    摄像头预览的高
     * @param ori   图像需要旋转的角度（只能为0,90,180,270）
     * @param bodys 返回的检测结果
     * @return 0：成功，其他: 失败
     */
    public static native long nativeTrack(byte[] yuv, int imageformat, int iw, int ih, int ori, List<Body> bodys);

    /**
     * @param yuv   从摄像头获取的yuv数据,支持两种格式，NV21，YV12
     * @param iw    摄像头预览的宽
     * @param ih    摄像头预览的高
     * @param ori   图像需要旋转的角度（只能为0,90,180,270）
     * @param bodys 返回的检测结果
     * @return 0：成功，其他: 失败
     */
    public static native long nativeDetect(byte[] yuv, int imageformat, int iw, int ih, int ori, List<Body> bodys);

    public static native String nativeSDKVersion();

    public static native void nativeReset();
}
