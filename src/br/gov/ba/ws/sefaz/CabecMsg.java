
package br.gov.ba.ws.sefaz;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for CabecMsg complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CabecMsg">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Projeto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VersaoXSD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CabecMsg", propOrder = {
    "projeto",
    "versaoXSD"
})
public class CabecMsg {

    @XmlElement(name = "Projeto")
    protected String projeto;
    @XmlElement(name = "VersaoXSD")
    protected String versaoXSD;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the projeto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProjeto() {
        return projeto;
    }

    /**
     * Sets the value of the projeto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProjeto(String value) {
        this.projeto = value;
    }

    /**
     * Gets the value of the versaoXSD property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersaoXSD() {
        return versaoXSD;
    }

    /**
     * Sets the value of the versaoXSD property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersaoXSD(String value) {
        this.versaoXSD = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
