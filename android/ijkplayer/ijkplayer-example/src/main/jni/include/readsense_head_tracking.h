#ifndef _READSENSE_HEAD_TRACKING_H_
#define _READSENSE_HEAD_TRACKING_H_

#include "xtype.h"

#define HEAD_FRONT      1
#define HEAD_BACK       2

#ifdef __cplusplus
extern "C" {
#endif

    typedef struct  
    {
        int nHeadNum;
        XRect *prtHeadRects;
        int *pHeadTypes; // FRONT or BACK
        int *pHeadIDs;
    } XHeadTrackResult;
    
    XReturn RS_HeadTracking_Init(XHandle *phHeadTracker);
    XReturn RS_HeadTracking_Run(XHandle hHeadTracker, const XNetImage *pImage, 
        XRect *prtDetections, int nDetectNum, XHeadTrackResult *pResults);
    XReturn RS_HeadTracking_UnInit(XHandle *phHeadTracker);
    const XVersion *RS_HeadTracking_GetVersion();

    XReturn RS_HeadTracking_SetReID(XHandle hHeadTracker, int nSetReID);
    XReturn RS_HeadTracking_RemoveObject(XHandle hHeadTracker, int nObjectId);
    XReturn RS_HeadTracking_SetValidRegion(XHandle hHeadTracker, XRect *prtROI);

#ifdef __cplusplus
}
#endif

#endif
