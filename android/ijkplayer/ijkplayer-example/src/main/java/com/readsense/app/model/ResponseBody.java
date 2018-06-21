package com.readsense.app.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Body", strict = false)
public class ResponseBody {

    @Element(name = "pushDataByJsonResponse", required = false)
    public ResponseModel pushDataByJsonResponse;

}
