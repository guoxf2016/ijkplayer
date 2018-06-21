package com.readsense.app.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class RequestModel {
    @Attribute(name = "xmlns")
    public String pushDataByJson;

    @Element(name = "json", required = false)
    public String json;

}
