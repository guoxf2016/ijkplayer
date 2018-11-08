package com.readsense.app.model.heartbeat;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;


@Root(name = "HeartbeatResponse")
@Namespace(reference = "http://tempuri.org/")
public class ResponseModel {

    @Attribute(name = "xmlns", empty = "http://tempuri.org/", required = false)
    public String nameSpace;
    /*@Element(name = "HeartbeatResponse")
    public String result;*/

}
