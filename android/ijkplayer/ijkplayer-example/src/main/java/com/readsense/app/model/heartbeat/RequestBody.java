package com.readsense.app.model.heartbeat;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "soapenv:Body", strict = false)
public class RequestBody {

    @Element(name = "Heartbeat", required = false)
    public RequestModel Heartbeat;
}