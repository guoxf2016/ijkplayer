package com.readsense.app.model.pushdatabyjson;



import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;


@Root(name = "pushDataByJsonResponse")
@Namespace(reference = "http://tempuri.org/")
public class ResponseModel {

    @Attribute(name = "xmlns", empty = "http://tempuri.org/", required = false)
    public String nameSpace;
    @Element(name = "pushDataByJsonResult")
    public String result;

}
