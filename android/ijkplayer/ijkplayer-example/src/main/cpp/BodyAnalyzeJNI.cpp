#include <include/BodyAnalyzeJNI.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/opencv.hpp>

#include <string>
#include <vector>
#include <time.h>
#include <stdlib.h>

#include <android/log.h>

#include <sstream>


#include "readsense_body_sensing.h"
#include "readsense_body_sensing_ex.h"


#define LOG_TAG "BodyAnalyzeJNI"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))

using namespace std;
using namespace cv;

auto i_reference_you =
{
    (void *)cv::dilate,
    (void *)cv::resize,
    (void *)cv::contourArea,
    (void *)cv::medianBlur,
    (void *)cv::cvtColor

};

void inkg(){
    Mat frame = Mat(1, 1, CV_8UC1);
    vector<vector<Point> > contours;
    findContours(frame, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_NONE);
    rand();
}
int nFrameNumber = -1;

void rotate_90n(cv::Mat &src, cv::Mat &dst, int angle)
    {
        dst.create(src.rows, src.cols, src.type());
        if(angle == 270 || angle == -90){
            // Rotate clockwise 270 degrees
            cv::flip(src.t(), dst, 0);
        }else if(angle == 180 || angle == -180){
            // Rotate clockwise 180 degrees
            cv::flip(src, dst, -1);
        }else if(angle == 90 || angle == -270){
            // Rotate clockwise 90 degrees
            cv::flip(src.t(), dst, 1);
        }else if(angle == 360 || angle == 0){
            if(src.data != dst.data){
                src.copyTo(dst);
            }
        }
    }
XHandle hController = NULL;
//initialize memory
JNIEXPORT jlong JNICALL Java_cn_readsense_readbody_BodyAnalyze_nativeCreateObject2(
    JNIEnv *jenv, jclass thiz, jobject context, jstring modelPath)
{
    LOGD("nativeCreateObject enter");
  

    int res = RS_BodySensing_Init(&hController);
    if (res != 0){
        LOGD("nativeCreateObject failed");
        return -1;
    }

    LOGD("nativeCreateObject out");

    return 0;
}

//destroy memory
JNIEXPORT void JNICALL Java_cn_readsense_readbody_BodyAnalyze_nativeDestroyObject2(
    JNIEnv *jenv, jclass _obj)
{
    if(hController)
        RS_BodySensing_UnInit(&hController);
    

}

JNIEXPORT void JNICALL Java_cn_readsense_readbody_BodyAnalyze_nativeBodyReset(
    JNIEnv *jenv, jclass _obj)
{
    // if(hController)
    //     RS_BodySensing_ResetEngine(hController);

    nFrameNumber = -1;

}


void addBody(JNIEnv * jenv, jobject listobj, Rect rect, float conf, int istrack,int var){

    jclass cls_object = jenv->FindClass("cn/readsense/readbody/YMBody");
    jmethodID construct_object = jenv->GetMethodID(cls_object, "<init>", "()V");  
    //new a object  
    jobject obj = jenv->NewObject(cls_object, construct_object, "");

    jmethodID obj_setRect = jenv->GetMethodID(cls_object,"setRect","([F)V"); 
    cv::Mat tResRect(1,4,CV_32F);
    tResRect.at<float>(0,0) = rect.x;
    tResRect.at<float>(0,1) = rect.y;
    tResRect.at<float>(0,2) = rect.width;
    tResRect.at<float>(0,3) = rect.height;
    jfloatArray _j_rect = jenv->NewFloatArray(4);
    jenv->SetFloatArrayRegion( _j_rect, 0, 4, (float*)(tResRect.data) );
    jenv->CallVoidMethod(obj , obj_setRect, _j_rect);


    jmethodID ym_setConf   = jenv->GetMethodID(cls_object,"setConfidence","(I)V");
    jint _j_conf = conf;
    jenv->CallVoidMethod(obj, ym_setConf, _j_conf);

    jmethodID ym_setIsTrack   = jenv->GetMethodID(cls_object,"setIstrack","(I)V"); 
    jint _j_track_id = istrack;
    jenv->CallVoidMethod(obj, ym_setIsTrack, _j_track_id);

    jmethodID ym_setStatuscode   = jenv->GetMethodID(cls_object,"setStatuscode","(I)V"); 
    jint _j_statuscode = var;
    jenv->CallVoidMethod(obj, ym_setIsTrack, _j_statuscode);

     // ArrayList Object
    jclass cls_ArrayList = jenv->FindClass("java/util/ArrayList");  
    jmethodID arrayList_add = jenv->GetMethodID(cls_ArrayList, "add", "(Ljava/lang/Object;)Z");
    jenv->CallBooleanMethod(listobj, arrayList_add, obj); 
}



cv::Mat curFrame, preFrame, preFrame2;
//face analyse
JNIEXPORT jlong JNICALL Java_cn_readsense_readbody_BodyAnalyze_nativeBodyTrack2(
    JNIEnv * jenv, jobject thiz, jbyteArray NV21FrameData,
    jint frameWidth, jint frameHeight,jint _j_scale, jint _j_rotation_angle ,jobject listobj)
{
    jlong statuscode = -1;
    jbyte * _j_arr = jenv->GetByteArrayElements(NV21FrameData, NULL);
    if ( _j_arr == NULL ) { LOGD("Error : input data is null"); return -1; }
    Mat yuvFrame(frameHeight + frameHeight/2, frameWidth, CV_8UC1, (unsigned char *)_j_arr);

    cv::Mat colorFrame;
    cv::cvtColor(yuvFrame, colorFrame, CV_YUV420sp2BGR);

    // rotate_90n(bgrFrame, colorFrame, (int)_j_rotation_angle);
    // cv::Mat colorFrame = cv::Mat(_height, _width, CV_8UC1, (unsigned char *)_arr);

    XBodySensingResult engineResult = { 0 };
    // XNetImage imgNet;
    // // imgNet.nWidth = frameWidth;
    // // imgNet.nHeight = frameHeight;
    // // imgNet.nPitch = imgNet.nWidth ;
    // // imgNet.nFormat = IMAGE_FORMAT_NV21;
    // // imgNet.pData = (unsigned char *)_j_arr;

    // imgNet.nWidth = colorFrame.cols;
    // imgNet.nHeight = colorFrame.rows;
    // imgNet.nPitch = imgNet.nWidth * 3;
    // imgNet.nFormat = IMAGE_FORMAT_RGB24_B8G8R8;
    // imgNet.pData = (unsigned char *)colorFrame.data;

    clock_t start_clock = clock();
    // int res = RS_BodySensing_Run(hController, &imgNet, &engineResult);
   
    nFrameNumber++;
    LOGD("nFrameNumber %d ",nFrameNumber);
    if (nFrameNumber == 0) {
        curFrame = colorFrame.clone();
        return statuscode;
    }
    else if (nFrameNumber == 1) {
        preFrame = curFrame.clone();
        curFrame = colorFrame.clone();
        return statuscode;
    }
    else {
        preFrame2 = preFrame.clone();
        preFrame = curFrame.clone();
        curFrame = colorFrame.clone();
    }
    cv::Mat pFrameList[3];
    pFrameList[0] = curFrame; pFrameList[1] = preFrame; pFrameList[2] = preFrame2;
    int res = RS_BodySensing_Run(hController, pFrameList, 3, &engineResult);


    int time_count = (clock() - start_clock)*1000/CLOCKS_PER_SEC;
    int person_status = 0;
    if(res!=0){
        person_status =  -1;
    }
   

     LOGD(" nSignNumber = %d , engineResult.pSensorSign. = %d\n",engineResult.nSignNumber,engineResult.pSensorSign[0]);
    if (engineResult.nSignNumber > 0)
    {
        if (engineResult.pSensorSign[0]) 
        {
            person_status = 1;
        }
    }
     Rect rect_body = RS_BodySensing_GetROI(hController);
    if(rect_body.width>0&&rect_body.height>0){
        addBody(jenv,listobj,rect_body,time_count,0,person_status);
    }

    statuscode = 0;
    jenv->ReleaseByteArrayElements( NV21FrameData, _j_arr, JNI_ABORT);
    return statuscode;
}











