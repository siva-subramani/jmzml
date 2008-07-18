//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.19 at 10:48:08 AM BST
//


package uk.ac.ebi.jmzml.model.mzml;

import uk.ac.ebi.jmzml.model.mzml.params.BinaryDataArrayCVParam;
import uk.ac.ebi.jmzml.model.mzml.params.BinaryDataArrayUserParam;
import uk.ac.ebi.jmzml.model.mzml.utilities.ParamGroupUpdater;
import uk.ac.ebi.jmzml.xml.jaxb.adapters.DataProcessingAdapter;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.math.BigInteger;


/**
 * The structure into which encoded binary data goes. Byte ordering is always little endian
 * (Intel style). Computers using a different endian style must convert to/from little endian when
 * writing/reading mzML
 * <p/>
 * <p/>
 * <p>Java class for BinaryDataArrayType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="BinaryDataArrayType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://psi.hupo.org/schema_revision/mzML_1.0.0}ParamGroupType">
 *       &lt;sequence>
 *         &lt;element name="binary" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *       &lt;attribute name="arrayLength" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="dataProcessingRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="encodedLength" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BinaryDataArrayType", propOrder = {
        "binary"
        })
public class BinaryDataArray
        extends ParamGroup
        implements Serializable {

    private final static long serialVersionUID = 100L;
    @XmlElement(required = true)
    protected byte[] binary;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger arrayLength;
    @XmlAttribute(name = "dataProcessingRef")
    @XmlJavaTypeAdapter(DataProcessingAdapter.class)
    @XmlSchemaType(name = "IDREF")
    protected DataProcessing dataProcessing;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger encodedLength;

    /**
     * Gets the value of the binary property.
     *
     * @return possible object is
     *         byte[]
     */
    public byte[] getBinary() {
        return binary;
    }

    /**
     * Sets the value of the binary property.
     *
     * @param value allowed object is
     *              byte[]
     */
    public void setBinary(byte[] value) {
        this.binary = ((byte[]) value);
    }

    /**
     * Gets the value of the arrayLength property.
     *
     * @return possible object is
     *         {@link BigInteger }
     */
    public BigInteger getArrayLength() {
        return arrayLength;
    }

    /**
     * Sets the value of the arrayLength property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setArrayLength(BigInteger value) {
        this.arrayLength = value;
    }

    /**
     * Gets the value of the dataProcessing property.
     *
     * @return possible object is
     *         {@link String }
     */
    public DataProcessing getDataProcessing() {
        return dataProcessing;
    }

    /**
     * Sets the value of the dataProcessing property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDataProcessing(DataProcessing value) {
        this.dataProcessing = value;
    }

    /**
     * Gets the value of the encodedLength property.
     *
     * @return possible object is
     *         {@link BigInteger }
     */
    public BigInteger getEncodedLength() {
        return encodedLength;
    }

    /**
     * Sets the value of the encodedLength property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setEncodedLength(BigInteger value) {
        this.encodedLength = value;
    }

    private void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        try {
            //update our paramGroup list
            ParamGroupUpdater.updateParamGroupSubclasses(this, BinaryDataArrayCVParam.class, BinaryDataArrayUserParam.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(this.getClass().getName() + ".afterUnmarshall: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(this.getClass().getName() + ".afterUnmarshall: " + e.getMessage());
        }
    }


}

