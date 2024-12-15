
package br.gov.ba.ws.sefaz;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Pxml_requisicao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pxmlRequisicao"
})
@XmlRootElement(name = "emitir_documento_arrecadacao")
public class EmitirDocumentoArrecadacao {

    @XmlElement(name = "Pxml_requisicao")
    protected String pxmlRequisicao;

    /**
     * Gets the value of the pxmlRequisicao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPxmlRequisicao() {
        return pxmlRequisicao;
    }

    /**
     * Sets the value of the pxmlRequisicao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPxmlRequisicao(String value) {
        this.pxmlRequisicao = value;
    }

}
