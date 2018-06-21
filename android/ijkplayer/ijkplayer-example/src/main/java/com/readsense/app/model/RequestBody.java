package com.readsense.app.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "soapenv:Body", strict = false)
public class RequestBody {

    @Element(name = "pushDataByJson", required = false)
    public RequestModel pushDataByJson;
}