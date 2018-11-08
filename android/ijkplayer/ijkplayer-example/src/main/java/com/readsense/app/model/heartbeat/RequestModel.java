package com.readsense.app.model.heartbeat;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class RequestModel {
    @Attribute(name = "xmlns")
    public String Heartbeat;

    @Element(name = "cameraID", required = false)
    public String cameraID;

}
