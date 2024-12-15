
package br.gov.ba.ws.sefaz;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.thejavageek.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CabecMsg_QNAME = new QName("http://www.sefaz.ba.gov.br/warr", "CabecMsg");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.thejavageek.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EmitirDocumentoArrecadacao }
     * 
     */
    public EmitirDocumentoArrecadacao createEmitirDocumentoArrecadacao() {
        return new EmitirDocumentoArrecadacao();
    }

    /**
     * Create an instance of {@link EmitirDocumentoArrecadacaoResponse.EmitirDocumentoArrecadacaoResult }
     * 
     */
    public EmitirDocumentoArrecadacaoResponse.EmitirDocumentoArrecadacaoResult createEmitirDocumentoArrecadacaoResponseEmitirDocumentoArrecadacaoResult() {
        return new EmitirDocumentoArrecadacaoResponse.EmitirDocumentoArrecadacaoResult();
    }

    /**
     * Create an instance of {@link EmitirDocumentoArrecadacaoResponse }
     * 
     */
    public EmitirDocumentoArrecadacaoResponse createEmitirDocumentoArrecadacaoResponse() {
        return new EmitirDocumentoArrecadacaoResponse();
    }

    /**
     * Create an instance of {@link CabecMsg }
     * 
     */
    public CabecMsg createCabecMsg() {
        return new CabecMsg();
    }

    /**
     * Create an instance of {@link ObterCamposObrigatoriosResponse }
     * 
     */
    public ObterCamposObrigatoriosResponse createObterCamposObrigatoriosResponse() {
        return new ObterCamposObrigatoriosResponse();
    }

    /**
     * Create an instance of {@link ObterCamposObrigatorios }
     * 
     */
    public ObterCamposObrigatorios createObterCamposObrigatorios() {
        return new ObterCamposObrigatorios();
    }

    /**
     * Create an instance of {@link ObterCamposObrigatoriosResponse.ObterCamposObrigatoriosResult }
     * 
     */
    public ObterCamposObrigatoriosResponse.ObterCamposObrigatoriosResult createObterCamposObrigatoriosResponseObterCamposObrigatoriosResult() {
        return new ObterCamposObrigatoriosResponse.ObterCamposObrigatoriosResult();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CabecMsg }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.sefaz.ba.gov.br/warr", name = "CabecMsg")
    public JAXBElement<CabecMsg> createCabecMsg(CabecMsg value) {
        return new JAXBElement<CabecMsg>(_CabecMsg_QNAME, CabecMsg.class, null, value);
    }

}
