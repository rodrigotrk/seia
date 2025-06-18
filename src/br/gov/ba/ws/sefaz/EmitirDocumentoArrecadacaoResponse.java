
package br.gov.ba.ws.sefaz;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMixed;
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
 *         &lt;element name="emitir_documento_arrecadacaoResult" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;any/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "emitirDocumentoArrecadacaoResult"
})
@XmlRootElement(name = "emitir_documento_arrecadacaoResponse")
public class EmitirDocumentoArrecadacaoResponse {

    @XmlElement(name = "emitir_documento_arrecadacaoResult")
    protected EmitirDocumentoArrecadacaoResponse.EmitirDocumentoArrecadacaoResult emitirDocumentoArrecadacaoResult;

    /**
     * Gets the value of the emitirDocumentoArrecadacaoResult property.
     * 
     * @return
     *     possible object is
     *     {@link EmitirDocumentoArrecadacaoResponse.EmitirDocumentoArrecadacaoResult }
     *     
     */
    public EmitirDocumentoArrecadacaoResponse.EmitirDocumentoArrecadacaoResult getEmitirDocumentoArrecadacaoResult() {
        return emitirDocumentoArrecadacaoResult;
    }

    /**
     * Sets the value of the emitirDocumentoArrecadacaoResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmitirDocumentoArrecadacaoResponse.EmitirDocumentoArrecadacaoResult }
     *     
     */
    public void setEmitirDocumentoArrecadacaoResult(EmitirDocumentoArrecadacaoResponse.EmitirDocumentoArrecadacaoResult value) {
        this.emitirDocumentoArrecadacaoResult = value;
    }


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
     *         &lt;any/>
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
        "content"
    })
    public static class EmitirDocumentoArrecadacaoResult {

        @XmlMixed
        @XmlAnyElement(lax = true)
        protected List<Object> content;

        /**
         * Gets the value of the content property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the content property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContent().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * {@link Object }
         * 
         * 
         */
        public List<Object> getContent() {
            if (content == null) {
                content = new ArrayList<Object>();
            }
            return this.content;
        }

    }

}
