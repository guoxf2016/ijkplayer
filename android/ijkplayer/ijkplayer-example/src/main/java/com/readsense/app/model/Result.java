package com.readsense.app.model;

import java.util.List;

/**
 * Created by guoxiaofei on 2018/3/22.
 */

public class Result {

    /**
     * id : 279357
     * person_id : b88041ec6a9e86e6e679dd5d00fb2dbd
     * device_id : 20
     * customer_id : 69774
     * event_type : in
     * original_face : {"url":"http://hscs.qiniudn.com/uploads/face/capture_face/834869/1521690093771.jpg?e=1521720696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:Kinr2kbIJXNQIW6cfcqxt-kaE1Q="}
     * status : recongnized
     * capture_at : 2018-03-22T11:41:33.000+08:00
     * created_at : 2018-03-22T11:41:35.202+08:00
     * device_name : 郭晓飞手机
     * shop_id : 3
     * shop_name : 愚园路店
     * track_id : 1
     * best_face : {"url":"http://hscs.qiniudn.com/uploads/face/capture_face/834869/1521690093771.jpg?e=1521720696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:Kinr2kbIJXNQIW6cfcqxt-kaE1Q="}
     * first_face : {"id":834869,"best_face_event_id":279357,"first_face_event_id":279357,"face_uuid":"d5d111e21899dba1118822a6bd649653","capture_face":{"url":"http://hscs.qiniudn.com/uploads/face/capture_face/834869/1521690093771.jpg?e=1521720696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:Kinr2kbIJXNQIW6cfcqxt-kaE1Q="},"age":24,"gender":1,"face_score":0.4,"capture_at":"2018-03-22T11:41:33.000+08:00","created_at":"2018-03-22T11:41:35.206+08:00","updated_at":"2018-03-22T11:41:35.849+08:00"}
     * customer : {"id":69774,"name":"gxf","avatar":{"url":"http://hscs.qiniudn.com/uploads/customer/avatars/69774/%E9%83%AD%E6%99%93%E9%A3%9E.jpg?e=1521720696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:eY1ugu13IGg8W2BrCyF58JtGU-I="},"original_face":{"url":"http://hscs.qiniudn.com/uploads/face/capture_face/835977/face20180322-502-cj0bjg.jpg?e=1521709704&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:GuHFsV2Gj4VOOZeXq0cxSi8mEz0="},"events_count":22,"capture_at":"2018-03-22T16:08:30.000+08:00","last_capture_at":"2018-03-22T13:54:53.000+08:00","person_id":"b88041ec6a9e86e6e679dd5d00fb2dbd","comment":null,"customer_groups_name":["阅面员工新算法"],"last_event_shop_name":"愚园路店","last_event_device_name":"新的设备2"}
     * candidates : [{"id":409,"person_id":"b88041ec6a9e86e6e679dd5d00fb2dbd","confidence":0.584014534950256,"face_url":"http://hscs.qiniudn.com/uploads/face/capture_face/834869/1521690093771.jpg?e=1521693696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:0zt5-_D-75G5N2QOM7JJLr4EQUA="}]
     */

    private int id;
    private String person_id;
    private int device_id;
    private int customer_id;
    private String event_type;
    private OriginalFaceBean original_face;
    private String status;
    private String capture_at;
    private String created_at;
    private String device_name;
    private int shop_id;
    private String shop_name;
    private String track_id;
    private BestFaceBean best_face;
    private FirstFaceBean first_face;
    private CustomerBean customer;
    private List<CandidatesBean> candidates;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public OriginalFaceBean getOriginal_face() {
        return original_face;
    }

    public void setOriginal_face(OriginalFaceBean original_face) {
        this.original_face = original_face;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCapture_at() {
        return capture_at;
    }

    public void setCapture_at(String capture_at) {
        this.capture_at = capture_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getTrack_id() {
        return track_id;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }

    public BestFaceBean getBest_face() {
        return best_face;
    }

    public void setBest_face(BestFaceBean best_face) {
        this.best_face = best_face;
    }

    public FirstFaceBean getFirst_face() {
        return first_face;
    }

    public void setFirst_face(FirstFaceBean first_face) {
        this.first_face = first_face;
    }

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    public List<CandidatesBean> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<CandidatesBean> candidates) {
        this.candidates = candidates;
    }

    public static class OriginalFaceBean {
        /**
         * url : http://hscs.qiniudn.com/uploads/face/capture_face/834869/1521690093771.jpg?e=1521720696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:Kinr2kbIJXNQIW6cfcqxt-kaE1Q=
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class BestFaceBean {
        /**
         * url : http://hscs.qiniudn.com/uploads/face/capture_face/834869/1521690093771.jpg?e=1521720696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:Kinr2kbIJXNQIW6cfcqxt-kaE1Q=
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class FirstFaceBean {
        /**
         * id : 834869
         * best_face_event_id : 279357
         * first_face_event_id : 279357
         * face_uuid : d5d111e21899dba1118822a6bd649653
         * capture_face : {"url":"http://hscs.qiniudn.com/uploads/face/capture_face/834869/1521690093771.jpg?e=1521720696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:Kinr2kbIJXNQIW6cfcqxt-kaE1Q="}
         * age : 24
         * gender : 1
         * face_score : 0.4
         * capture_at : 2018-03-22T11:41:33.000+08:00
         * created_at : 2018-03-22T11:41:35.206+08:00
         * updated_at : 2018-03-22T11:41:35.849+08:00
         */

        private int id;
        private int best_face_event_id;
        private int first_face_event_id;
        private String face_uuid;
        private CaptureFaceBean capture_face;
        private int age;
        private int gender;
        private double face_score;
        private String capture_at;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getBest_face_event_id() {
            return best_face_event_id;
        }

        public void setBest_face_event_id(int best_face_event_id) {
            this.best_face_event_id = best_face_event_id;
        }

        public int getFirst_face_event_id() {
            return first_face_event_id;
        }

        public void setFirst_face_event_id(int first_face_event_id) {
            this.first_face_event_id = first_face_event_id;
        }

        public String getFace_uuid() {
            return face_uuid;
        }

        public void setFace_uuid(String face_uuid) {
            this.face_uuid = face_uuid;
        }

        public CaptureFaceBean getCapture_face() {
            return capture_face;
        }

        public void setCapture_face(CaptureFaceBean capture_face) {
            this.capture_face = capture_face;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public double getFace_score() {
            return face_score;
        }

        public void setFace_score(double face_score) {
            this.face_score = face_score;
        }

        public String getCapture_at() {
            return capture_at;
        }

        public void setCapture_at(String capture_at) {
            this.capture_at = capture_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public static class CaptureFaceBean {
            /**
             * url : http://hscs.qiniudn.com/uploads/face/capture_face/834869/1521690093771.jpg?e=1521720696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:Kinr2kbIJXNQIW6cfcqxt-kaE1Q=
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    public static class CustomerBean {
        /**
         * id : 69774
         * name : gxf
         * avatar : {"url":"http://hscs.qiniudn.com/uploads/customer/avatars/69774/%E9%83%AD%E6%99%93%E9%A3%9E.jpg?e=1521720696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:eY1ugu13IGg8W2BrCyF58JtGU-I="}
         * original_face : {"url":"http://hscs.qiniudn.com/uploads/face/capture_face/835977/face20180322-502-cj0bjg.jpg?e=1521709704&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:GuHFsV2Gj4VOOZeXq0cxSi8mEz0="}
         * events_count : 22
         * capture_at : 2018-03-22T16:08:30.000+08:00
         * last_capture_at : 2018-03-22T13:54:53.000+08:00
         * person_id : b88041ec6a9e86e6e679dd5d00fb2dbd
         * comment : null
         * customer_groups_name : ["阅面员工新算法"]
         * last_event_shop_name : 愚园路店
         * last_event_device_name : 新的设备2
         */

        private int id;
        private String name;
        private AvatarBean avatar;
        private OriginalFaceBeanX original_face;
        private int events_count;
        private String capture_at;
        private String last_capture_at;
        private String person_id;
        private Object comment;
        private String last_event_shop_name;
        private String last_event_device_name;
        private List<String> customer_groups_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public AvatarBean getAvatar() {
            return avatar;
        }

        public void setAvatar(AvatarBean avatar) {
            this.avatar = avatar;
        }

        public OriginalFaceBeanX getOriginal_face() {
            return original_face;
        }

        public void setOriginal_face(OriginalFaceBeanX original_face) {
            this.original_face = original_face;
        }

        public int getEvents_count() {
            return events_count;
        }

        public void setEvents_count(int events_count) {
            this.events_count = events_count;
        }

        public String getCapture_at() {
            return capture_at;
        }

        public void setCapture_at(String capture_at) {
            this.capture_at = capture_at;
        }

        public String getLast_capture_at() {
            return last_capture_at;
        }

        public void setLast_capture_at(String last_capture_at) {
            this.last_capture_at = last_capture_at;
        }

        public String getPerson_id() {
            return person_id;
        }

        public void setPerson_id(String person_id) {
            this.person_id = person_id;
        }

        public Object getComment() {
            return comment;
        }

        public void setComment(Object comment) {
            this.comment = comment;
        }

        public String getLast_event_shop_name() {
            return last_event_shop_name;
        }

        public void setLast_event_shop_name(String last_event_shop_name) {
            this.last_event_shop_name = last_event_shop_name;
        }

        public String getLast_event_device_name() {
            return last_event_device_name;
        }

        public void setLast_event_device_name(String last_event_device_name) {
            this.last_event_device_name = last_event_device_name;
        }

        public List<String> getCustomer_groups_name() {
            return customer_groups_name;
        }

        public void setCustomer_groups_name(List<String> customer_groups_name) {
            this.customer_groups_name = customer_groups_name;
        }

        public static class AvatarBean {
            /**
             * url : http://hscs.qiniudn.com/uploads/customer/avatars/69774/%E9%83%AD%E6%99%93%E9%A3%9E.jpg?e=1521720696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:eY1ugu13IGg8W2BrCyF58JtGU-I=
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class OriginalFaceBeanX {
            /**
             * url : http://hscs.qiniudn.com/uploads/face/capture_face/835977/face20180322-502-cj0bjg.jpg?e=1521709704&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:GuHFsV2Gj4VOOZeXq0cxSi8mEz0=
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }

    public static class CandidatesBean {
        /**
         * id : 409
         * person_id : b88041ec6a9e86e6e679dd5d00fb2dbd
         * confidence : 0.584014534950256
         * face_url : http://hscs.qiniudn.com/uploads/face/capture_face/834869/1521690093771.jpg?e=1521693696&token=gBlgwMtgrEBgCTpjfXobrI9JN9JJEgqnrgqjGr-3:0zt5-_D-75G5N2QOM7JJLr4EQUA=
         */

        private int id;
        private String person_id;
        private double confidence;
        private String face_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPerson_id() {
            return person_id;
        }

        public void setPerson_id(String person_id) {
            this.person_id = person_id;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
        }
    }
}

