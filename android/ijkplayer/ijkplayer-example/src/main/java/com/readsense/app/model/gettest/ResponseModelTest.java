package com.readsense.app.model.gettest;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;


@Root(name = "getTestResponse")
@Namespace(reference = "http://tempuri.org/")
public class ResponseModelTest {

    @Element(name = "getTestResult")
    public String getTestResult;

}
