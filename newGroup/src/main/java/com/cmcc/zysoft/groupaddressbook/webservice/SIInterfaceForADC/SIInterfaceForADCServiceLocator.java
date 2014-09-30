/**
 * SIInterfaceForADCServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADC;

public class SIInterfaceForADCServiceLocator extends org.apache.axis.client.Service implements com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADC.SIInterfaceForADCService {

    public SIInterfaceForADCServiceLocator() {
    }


    public SIInterfaceForADCServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SIInterfaceForADCServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SIInterfaceForADC
    private java.lang.String SIInterfaceForADC_address = "http://120.209.131.146/webcloud/services/SIInterfaceForADC";

    public java.lang.String getSIInterfaceForADCAddress() {
        return SIInterfaceForADC_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SIInterfaceForADCWSDDServiceName = "SIInterfaceForADC";

    public java.lang.String getSIInterfaceForADCWSDDServiceName() {
        return SIInterfaceForADCWSDDServiceName;
    }

    public void setSIInterfaceForADCWSDDServiceName(java.lang.String name) {
        SIInterfaceForADCWSDDServiceName = name;
    }

    public com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADC.SIInterfaceForADC getSIInterfaceForADC() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SIInterfaceForADC_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSIInterfaceForADC(endpoint);
    }

    public com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADC.SIInterfaceForADC getSIInterfaceForADC(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADC.SIInterfaceForADCSoapBindingStub _stub = new com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADC.SIInterfaceForADCSoapBindingStub(portAddress, this);
            _stub.setPortName(getSIInterfaceForADCWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSIInterfaceForADCEndpointAddress(java.lang.String address) {
        SIInterfaceForADC_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADC.SIInterfaceForADC.class.isAssignableFrom(serviceEndpointInterface)) {
                com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADC.SIInterfaceForADCSoapBindingStub _stub = new com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADC.SIInterfaceForADCSoapBindingStub(new java.net.URL(SIInterfaceForADC_address), this);
                _stub.setPortName(getSIInterfaceForADCWSDDServiceName());
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
        if ("SIInterfaceForADC".equals(inputPortName)) {
            return getSIInterfaceForADC();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://120.209.131.146/webcloud/services/SIInterfaceForADC", "SIInterfaceForADCService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://120.209.131.146/webcloud/services/SIInterfaceForADC", "SIInterfaceForADC"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
	public void setEndpointAddress(java.lang.String portName,
			java.lang.String address) throws javax.xml.rpc.ServiceException {

		if ("SIInterfaceForADC".equals(portName)) {
			setSIInterfaceForADCEndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(
					" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
