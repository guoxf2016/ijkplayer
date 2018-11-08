package com.readsense.app.model.gettest;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Body")
public class ResponseBodyTest {

    @Element(name = "getTestResponse", required = false)
    public ResponseModelTest getTestResponse;

}
