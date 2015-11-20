
package impl;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ServerSOAPImplService", targetNamespace = "http://impl/", wsdlLocation = "http://localhost:8090/ws/tk1wsshoppingcart?wsdl")
public class ServerSOAPImplService
    extends Service
{

    private final static URL SERVERSOAPIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException SERVERSOAPIMPLSERVICE_EXCEPTION;
    private final static QName SERVERSOAPIMPLSERVICE_QNAME = new QName("http://impl/", "ServerSOAPImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8090/ws/tk1wsshoppingcart?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SERVERSOAPIMPLSERVICE_WSDL_LOCATION = url;
        SERVERSOAPIMPLSERVICE_EXCEPTION = e;
    }

    public ServerSOAPImplService() {
        super(__getWsdlLocation(), SERVERSOAPIMPLSERVICE_QNAME);
    }

    public ServerSOAPImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SERVERSOAPIMPLSERVICE_QNAME, features);
    }

    public ServerSOAPImplService(URL wsdlLocation) {
        super(wsdlLocation, SERVERSOAPIMPLSERVICE_QNAME);
    }

    public ServerSOAPImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SERVERSOAPIMPLSERVICE_QNAME, features);
    }

    public ServerSOAPImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ServerSOAPImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ServerSOAPImpl
     */
    @WebEndpoint(name = "ServerSOAPImplPort")
    public ServerSOAPImpl getServerSOAPImplPort() {
        return super.getPort(new QName("http://impl/", "ServerSOAPImplPort"), ServerSOAPImpl.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ServerSOAPImpl
     */
    @WebEndpoint(name = "ServerSOAPImplPort")
    public ServerSOAPImpl getServerSOAPImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://impl/", "ServerSOAPImplPort"), ServerSOAPImpl.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SERVERSOAPIMPLSERVICE_EXCEPTION!= null) {
            throw SERVERSOAPIMPLSERVICE_EXCEPTION;
        }
        return SERVERSOAPIMPLSERVICE_WSDL_LOCATION;
    }

}
