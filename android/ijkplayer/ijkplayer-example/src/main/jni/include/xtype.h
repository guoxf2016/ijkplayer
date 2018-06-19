#ifndef __XTYPE_H__
#define __XTYPE_H__

#ifdef __cplusplus
extern "C"
{
#endif

typedef int     XReturn;
typedef void*   XHandle;

#define     IMAGE_FORMAT_RGB24_B8G8R8       1
#define     IMAGE_FORMAT_NV12               2
#define     IMAGE_FORMAT_NV21               3
#define     IMAGE_FORMAT_GRAY               4
#define     IMAGE_FORMAT_I420               5

typedef struct tag_XNet_Image
{
    int nWidth;
    int nHeight;
    int nPitch;
    int nFormat;
    unsigned char *pData;
} XNetImage, *LPXNetImage;

typedef struct
{
    const char *strVersion;
    const char *strBuildDate;
} XVersion;

struct XRectA_
{
    XRectA_() : x(0), y(0), width(0), height(0) {}
    XRectA_(int _x, int _y, int _w, int _h) : x(_x), y(_y), width(_w), height(_h) {}

    int x;
    int y;
    int width;
    int height;
};
typedef struct XRectA_ XRectA;

struct XRectF_
{
    XRectF_() : x(0), y(0), width(0), height(0) {}
    XRectF_(float _x, float _y, float _w, float _h) : x(_x), y(_y), width(_w), height(_h) {}

    float x;
    float y;
    float width;
    float height;
};
typedef struct XRectF_ XRectF;

struct XRect_
{
    XRect_() : left(0), top(0), right(0), bottom(0) {}
    XRect_(int _l, int _t, int _r, int _b) : left(_l), top(_t), right(_r), bottom(_b) {}

    int left;
    int top;
    int right;
    int bottom;
};
typedef struct XRect_ XRect;

struct XPoint2f_
{
    XPoint2f_() : x(0), y(0) {}
    XPoint2f_(float _x, float _y) : x(_x), y(_y) {}

    float x;
    float y;
};
typedef struct XPoint2f_ XPoint2f;

struct XPoint_
{
    XPoint_() : x(0), y(0) {}
    XPoint_(int _x, int _y) : x(_x), y(_y) {}

    int x;
    int y;
};
typedef struct XPoint_ XPoint;

struct XSize_
{
    XSize_() : width(0), height(0) {}
    XSize_(int _w, int _h) : width(_w), height(_h) {}

    int width;
    int height;
};
typedef struct XSize_ XSize;

typedef struct
{
    XRectA rect;
    int label;
    float score;
} XRectInfo;

#ifndef NULL
#define NULL 0
#endif

//////////////////////////////////////////////////////////////////////////
#define XNET_MAX(a,b)       ((a)>(b)?(a):(b))
#define XNET_MIN(a,b)       ((a)<(b)?(a):(b))

#define XMAX(a,b)           ((a)>(b)?(a):(b))
#define XMIN(a,b)           ((a)<(b)?(a):(b))

#define SWAP_WITH_TYPE(a, b, t)     {t tmp = (a); (a) = (b); (b) = tmp;}

//////////////////////////////////////////////////////////////////////////
#define LINE_BYTES(w, b)    (((int)(w) * (b) + 31) / 32 * 4)
#define trimBYTE(x)         (unsigned char)((x)&(~255) ? ((-(x))>>31) : (x))

#define yuv_shift           12
#define yuv_fix(x)          (int)((x) * (1 << (yuv_shift)) + 0.5f)
#define yuv_descale(x)      (((x) + (1 << ((yuv_shift)-1))) >> (yuv_shift))
#define yuv_prescale(x)     ((x) << yuv_shift)

#define yuvYr               yuv_fix(0.299f)
#define yuvYg               yuv_fix(0.587f)
#define yuvYb               yuv_fix(0.114f)
#define yuvCr               yuv_fix(0.713f)
#define yuvCb               yuv_fix(0.564f)

#define yuvPr               yuv_fix(1.426f) // 1/(1-Wr)
#define yuvPb               yuv_fix(1.128f) // 1/(1-Wb)

#define yuvRCr              yuv_fix(1.403f)
#define yuvGCr              (-yuv_fix(0.714f))
#define yuvGCb              (-yuv_fix(0.344f))
#define yuvBCb              yuv_fix(1.773f)

#define ET_CAST_8U(t)       (unsigned char)(!((t) & ~255) ? (t) : (t) > 0 ? 255 : 0)
#define ET_YUV_TO_R(y,v)    (unsigned char)(ET_CAST_8U(yuv_descale((y) + yuvRCr * (v))))
#define ET_YUV_TO_G(y,u,v)  (unsigned char)(ET_CAST_8U(yuv_descale((y) + yuvGCr * (v) + yuvGCb * (u))))
#define ET_YUV_TO_B(y,u)    (unsigned char)(ET_CAST_8U(yuv_descale((y) + yuvBCb * (u))))

#ifndef TRIM_UINT8
#define TRIM_UINT8(x)       (unsigned char)((x)&(~255) ? ((-(x))>>31) : (x))
#endif

#ifndef XPI
#define XPI     3.14159265358979
#endif

//////////////////////////////////////////////////////////////////////////

#ifdef __cplusplus
}
#endif

#endif //__XTYPE_H__
