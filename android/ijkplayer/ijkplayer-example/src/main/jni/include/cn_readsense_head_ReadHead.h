/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class cn_readsense_head_ReadHead */

#ifndef _Included_cn_readsense_head_ReadHead
#define _Included_cn_readsense_head_ReadHead
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     cn_readsense_head_ReadHead
 * Method:    nativeCreateObject
 * Signature: (Landroid/content/Context;)J
 */
JNIEXPORT jlong JNICALL Java_cn_readsense_head_ReadHead_nativeCreateObject
  (JNIEnv *, jclass, jobject);

/*
 * Class:     cn_readsense_head_ReadHead
 * Method:    nativeCreateObjectWithLicense
 * Signature: (Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_cn_readsense_head_ReadHead_nativeCreateObjectWithLicense
  (JNIEnv *, jclass, jobject, jstring, jstring);

/*
 * Class:     cn_readsense_head_ReadHead
 * Method:    nativeDestroyObject
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_cn_readsense_head_ReadHead_nativeDestroyObject
  (JNIEnv *, jclass);

/*
 * Class:     cn_readsense_head_ReadHead
 * Method:    nativeTrack
 * Signature: ([BIIIILjava/util/List;)J
 */
JNIEXPORT jlong JNICALL Java_cn_readsense_head_ReadHead_nativeTrack
  (JNIEnv *, jclass, jbyteArray, jint, jint, jint, jint, jobject);

/*
 * Class:     cn_readsense_head_ReadHead
 * Method:    nativeDetect
 * Signature: ([BIIIILjava/util/List;)J
 */
JNIEXPORT jlong JNICALL Java_cn_readsense_head_ReadHead_nativeDetect
  (JNIEnv *, jclass, jbyteArray, jint, jint, jint, jint, jobject);

/*
 * Class:     cn_readsense_head_ReadHead
 * Method:    nativeSDKVersion
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_cn_readsense_head_ReadHead_nativeSDKVersion
  (JNIEnv *, jclass);

/*
 * Class:     cn_readsense_head_ReadHead
 * Method:    nativeReset
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_cn_readsense_head_ReadHead_nativeReset
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
