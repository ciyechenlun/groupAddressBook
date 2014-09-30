/**
 * SIInterfaceForADCLocator.java
 *
 * This file was auto-generated from com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap2Java emitter.
 */

package com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap;

public class SIInterfaceForADCLocator extends org.apache.axis.client.Service implements com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCX {

    public SIInterfaceForADCLocator() {
    }


    public SIInterfaceForADCLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SIInterfaceForADCLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SIInterfaceForADCSoap12
    private java.lang.String SIInterfaceForADCSoap12_address = "http://221.130.165.108:9010/adcwsinterface/ADCInterfaceForSI.asmx";

    public java.lang.String getSIInterfaceForADCSoap12Address() {
        return SIInterfaceForADCSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SIInterfaceForADCSoap12WSDDServiceName = "ADCInterfaceForSISoap12";

    public java.lang.String getSIInterfaceForADCSoap12WSDDServiceName() {
        return SIInterfaceForADCSoap12WSDDServiceName;
    }

    public void setSIInterfaceForADCSoap12WSDDServiceName(java.lang.String name) {
        SIInterfaceForADCSoap12WSDDServiceName = name;
    }

    public com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap_PortType getSIInterfaceForADCSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SIInterfaceForADCSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSIInterfaceForADCSoap12(endpoint);
    }

    public com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap_PortType getSIInterfaceForADCSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap12Stub _stub = new com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap12Stub(portAddress, this);
            _stub.setPortName(getSIInterfaceForADCSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSIInterfaceForADCSoap12EndpointAddress(java.lang.String address) {
        SIInterfaceForADCSoap12_address = address;
    }


    // Use to get a proxy class for SIInterfaceForADCSoap
    private java.lang.String SIInterfaceForADCSoap_address = "http://221.130.165.108:9010/adcwsinterface/ADCInterfaceForSI.asmx";

    public java.lang.String getSIInterfaceForADCSoapAddress() {
        return SIInterfaceForADCSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SIInterfaceForADCSoapWSDDServiceName = "ADCInterfaceForSISoap";

    public java.lang.String getSIInterfaceForADCSoapWSDDServiceName() {
        return SIInterfaceForADCSoapWSDDServiceName;
    }

    public void setSIInterfaceForADCSoapWSDDServiceName(java.lang.String name) {
        SIInterfaceForADCSoapWSDDServiceName = name;
    }

    public com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap_PortType getSIInterfaceForADCSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SIInterfaceForADCSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSIInterfaceForADCSoap(endpoint);
    }

    public com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap_PortType getSIInterfaceForADCSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap_BindingStub _stub = new com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap_BindingStub(portAddress, this);
            _stub.setPortName(getSIInterfaceForADCSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSIInterfaceForADCSoapEndpointAddress(java.lang.String address) {
        SIInterfaceForADCSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap12Stub _stub = new com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap12Stub(new java.net.URL(SIInterfaceForADCSoap12_address), this);
                _stub.setPortName(getSIInterfaceForADCSoap12WSDDServiceName());
                return _stub;
            }
            if (com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap_BindingStub _stub = new com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.SIInterfaceForADCSoap_BindingStub(new java.net.URL(SIInterfaceForADCSoap_address), this);
                _stub.setPortName(getSIInterfaceForADCSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ADCInterfaceForSISoap12".equals(inputPortName)) {
            return getSIInterfaceForADCSoap12();
        }
        else if ("ADCInterfaceForSISoap".equals(inputPortName)) {
            return getSIInterfaceForADCSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://adc.siinterface.com/", "ADCInterfaceForSI");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://adc.siinterface.com/", "ADCInterfaceForSISoap12"));
            ports.add(new javax.xml.namespace.QName("http://adc.siinterface.com/", "ADCInterfaceForSISoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ADCInterfaceForSISoap12".equals(portName)) {
            setSIInterfaceForADCSoap12EndpointAddress(address);
        }
        else 
if ("ADCInterfaceForSISoap".equals(portName)) {
            setSIInterfaceForADCSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
