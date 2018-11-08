package com.readsense.app.model.heartbeat;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

/*
  POST /CameraDetect/DetectData.asmx HTTP/1.1
 Host: 210.5.155.249
 Content-Type: text/xml; charset=utf-8
 Content-Length: length
 SOAPAction: "http://tempuri.org/pushDataByJson"

 <?xml version="1.0" encoding="utf-8"?>
 <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
 <soap:Body>
 <pushDataByJson xmlns="http://tempuri.org/">
 <json>string</json>
 </pushDataByJson>
 </soap:Body>
 </soap:Envelope>
 */

@Root(name = "soapenv:Envelope")
@NamespaceList({
        @Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi"),
        @Namespace(reference = "http://www.w3.org/2001/XMLSchema", prefix = "xsd"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/encoding/", prefix = "enc"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soapenv")
})

public class RequestEnvelope {
    @Element(name = "soapenv:Body", required = false)
    public RequestBody body;
}
