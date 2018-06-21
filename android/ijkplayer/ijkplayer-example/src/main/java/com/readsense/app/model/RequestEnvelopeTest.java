package com.readsense.app.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

/*
  POST /CameraDetect/DetectData.asmx HTTP/1.1
Host: 210.5.155.249
Content-Type: text/xml; charset=utf-8
Content-Length: length
SOAPAction: "http://tempuri.org/getTest"

<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getTest xmlns="http://tempuri.org/" />
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

public class RequestEnvelopeTest {
    @Element(name = "soapenv:Body", required = false)
    public RequestBodyTest body;
}
