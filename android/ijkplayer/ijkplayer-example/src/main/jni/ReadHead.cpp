/*
#include <jni.h>
#include <string>

#include <iostream>
#include <fstream>
#include <sstream>
#include <iomanip>
#include <time.h>
#include <android/log.h>

#include <opencv2/opencv.hpp>
#include <readsense_head_tracking.h>

#include "include/cn_readsense_head_ReadHead.h"
#include "include/readsense_head_tracking.h"


#ifdef __cplusplus
extern "C" {
#endif

#define LOG_TAG "DLog_lib_head"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))

using namespace std;
using namespace cv;

jstring getPackageName(JNIEnv *env, jobject context);
void rotate_90n(cv::Mat &src, cv::Mat &dst, int angle);

string date_out = "20180701";
string package_name = "readsense.face.yuequ";

XHandle hController = NULL;

#define FA_FORMAT_NV21 1
#define FA_FORMAT_YV12 2

#define MAX_HEAD_DETECT_NUM 50

JNIEXPORT jlong JNICALL Java_cn_readsense_head_ReadHead_nativeCreateObject
        (JNIEnv *env, jclass clazz, jobject context) {
    jstring jstrPackageName = getPackageName(env, context);
    string packageName = env->GetStringUTFChars(jstrPackageName, 0);

    if (package_name != packageName) {
        return -3;
    }

#ifdef ENABLE_LICENSE_MANAGER

#else
    time_t now = time(NULL);
    tm *ltm = localtime(&now);

    char tmp_date[100];
    sprintf(tmp_date, "%d%02d%02d", (1900 + ltm->tm_year), (1 + ltm->tm_mon), ltm->tm_mday);
    string today;
    today.assign(tmp_date, strlen(tmp_date));

    if (today > date_out) {
        LOGD("fa: error date expire");
        return -2;
    }
#endif

    XReturn res = RS_HeadTracking_Init(&hController);
    if (res != 0) {
        LOGD("init failed");
        return -1;
    }
    LOGD("init : 0");
    return 0;

}

JNIEXPORT jlong JNICALL Java_cn_readsense_head_ReadHead_nativeCreateObjectWithLicense
        (JNIEnv *, jclass, jobject, jstring, jstring) {
    return -1;
}

JNIEXPORT void JNICALL Java_cn_readsense_head_ReadHead_nativeDestroyObject
        (JNIEnv *, jclass) {

    if (hController) {
        RS_HeadTracking_UnInit(&hController);
        hController = NULL;
    }

}

JNIEXPORT jlong JNICALL Java_cn_readsense_head_ReadHead_nativeTrack
        (JNIEnv *env, jclass clazz, jbyteArray data, jint format, jint w, jint h, jint ori,
         jobject obj) {
    jbyte *_j_arr = env->GetByteArrayElements(data, NULL);
    if (_j_arr == NULL) {
        LOGD("Error : input data is null");;
        return -1;
    }

    int nHeadDetectNum = 0;
    XRect rtHeadDetectBoxs[MAX_HEAD_DETECT_NUM];
    float fHeadDetectScores[MAX_HEAD_DETECT_NUM];

    XNetImage imgEngine;
    imgEngine.nWidth = w;
    imgEngine.nHeight = h;
    imgEngine.nFormat = format;
    switch (imgEngine.nFormat) {
        case IMAGE_FORMAT_RGB24_B8G8R8:
            imgEngine.nPitch = LINE_BYTES(imgEngine.nWidth, 24);
            imgEngine.pData = (unsigned char *)data;
            break;
        case IMAGE_FORMAT_NV21:
            imgEngine.nPitch = LINE_BYTES(imgEngine.nWidth, 8);
            imgEngine.pData = (unsigned char *)data;;
            break;
        default:
            break;
    }

    env->ReleaseByteArrayElements( data, _j_arr, JNI_ABORT);
    XHeadTrackResult engineResult = { 0 };
    XRect sXRect;
    XReturn res = RS_HeadTracking_Run(hController, &imgEngine, &sXRect, 1, &engineResult);
    if (res != 0) {
        LOGD("RS_ColorDetect_Run failed");
        return -1;
    }

    jclass list = env->GetObjectClass(obj);
    jmethodID list_get = env->GetMethodID(list,"get","(I)Ljava/lang/Object;");
    jmethodID list_size = env->GetMethodID(list,"size","()I");
    jmethodID list_add = env->GetMethodID(list, "add", "(Ljava/lang/Object;)Z");


    jclass class_head = env->FindClass("cn/readsense/head/Head");
    jmethodID construct_head = env->GetMethodID(class_head,"<init>","()V");

    jmethodID user_setRect = env->GetMethodID(class_head, "setRect", "([F)V");
    jmethodID user_setnHeadNum = env->GetMethodID(class_head, "setnHeadNum", "(I)V");
    jmethodID user_setpHeadTypes = env->GetMethodID(class_head, "setpHeadTypes", "(I)V");
    jmethodID user_setpHeadIDs = env->GetMethodID(class_head, "setpHeadIDs", "(I)V");
    Mat tResRect(1,4,CV_32F);
    for (int i = 0; i < engineResult.nHeadNum; ++i) {
        tResRect.at<float>(0,0) = engineResult.prtHeadRects->left;
        tResRect.at<float>(0,1) = engineResult.prtHeadRects->top;
        tResRect.at<float>(0,2) = engineResult.prtHeadRects->right - engineResult.prtHeadRects->left;
        tResRect.at<float>(0,3) = engineResult.prtHeadRects->bottom - engineResult.prtHeadRects->top;
        jobject obj_user = env->NewObject(class_head, construct_head,"");
        jfloatArray _j_rect = env->NewFloatArray(4);
        env->SetFloatArrayRegion(_j_rect, 0, 4, (float *) (tResRect.data));
        env->CallVoidMethod(obj_user, user_setRect, _j_rect);
        env->DeleteLocalRef(_j_rect);
        env->CallVoidMethod(obj_user, user_setnHeadNum, engineResult.nHeadNum);
        env->CallVoidMethod(obj_user, user_setpHeadTypes, engineResult.pHeadTypes);
        env->CallVoidMethod(obj_user, user_setpHeadIDs, engineResult.pHeadIDs);

        env->CallBooleanMethod(obj, list_add, obj_user);
        env->DeleteLocalRef(obj_user);
    }

    return 0;
}


JNIEXPORT jlong JNICALL Java_cn_readsense_head_ReadHead_nativeDetect
        (JNIEnv *, jclass, jbyteArray, jint, jint, jint, jint, jobject) {
    return -1;
}


JNIEXPORT jstring JNICALL Java_cn_readsense_head_ReadHead_nativeSDKVersion
        (JNIEnv *env, jclass clazz) {
    XVersion *pVersionInfo;
    pVersionInfo = (XVersion *) RS_HeadTracking_GetVersion();
    stringstream version_info;
    version_info << "Code: " << pVersionInfo->strVersion << "#Build: "
                 << pVersionInfo->strBuildDate;

    return env->NewStringUTF((version_info.str() + "#Date: " + date_out).c_str());
}


JNIEXPORT void JNICALL Java_cn_readsense_head_ReadHead_nativeReset
        (JNIEnv *, jclass) {}

jstring getPackageName(JNIEnv *env, jobject context) {

    jclass native_clazz = env->GetObjectClass(context);

    jmethodID methodID_func = env->GetMethodID(native_clazz,
                                               "getPackageManager",
                                               "()Landroid/content/pm/PackageManager;");
    jobject package_manager = env->CallObjectMethod(context, methodID_func);

    jclass pm_clazz = env->GetObjectClass(package_manager);
    jmethodID methodID_pm = env->GetMethodID(pm_clazz, "getPackageInfo",
                                             "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");

    jmethodID methodID_packagename = env->GetMethodID(native_clazz,
                                                      "getPackageName", "()Ljava/lang/String;");
    jstring name_str = static_cast<jstring>(env->CallObjectMethod(context,
                                                                  methodID_packagename));

    return name_str;
}

#ifdef __cplusplus
}
#endif

*/
