package com.readsense.app.model.heartbeat;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Body", strict = false)
public class ResponseBody {

    @Element(name = "HeartbeatResponse", required = false)
    public ResponseModel HeartbeatResponse;

}
