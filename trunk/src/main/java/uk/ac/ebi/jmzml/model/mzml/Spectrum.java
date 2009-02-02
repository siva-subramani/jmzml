//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.01.22 at 06:16:33 PM GMT 
//


package uk.ac.ebi.jmzml.model.mzml;

import java.io.Serializable;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;
import uk.ac.ebi.jmzml.xml.jaxb.adapters.DataProcessingAdapter;
import uk.ac.ebi.jmzml.xml.jaxb.adapters.SourceFileAdapter;


/**
 * The structure that captures the generation of a peak list (including the underlying acquisitions). Also describes some of the parameters for the mass spectrometer for a given acquisition (or list of acquisitions).
 * 
 * <p>Java class for SpectrumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SpectrumType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://psi.hupo.org/ms/mzml}ParamGroupType">
 *       &lt;sequence>
 *         &lt;element name="scanList" type="{http://psi.hupo.org/ms/mzml}ScanListType" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="precursorList" type="{http://psi.hupo.org/ms/mzml}PrecursorListType"/>
 *           &lt;element name="product" type="{http://psi.hupo.org/ms/mzml}ProductType"/>
 *           &lt;element name="neutralLoss" type="{http://psi.hupo.org/ms/mzml}NeutralLossType"/>
 *         &lt;/choice>
 *         &lt;element name="binaryDataArrayList" type="{http://psi.hupo.org/ms/mzml}BinaryDataArrayListType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;pattern value="\S+=\S+( \S+=\S+)*"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="spotID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="defaultArrayLength" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="dataProcessingRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="sourceFileRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpectrumType", propOrder = {
    "scanList",
    "precursorListOrProductOrNeutralLoss",
    "binaryDataArrayList"
})
public class Spectrum
    extends ParamGroup
    implements Serializable
{

    private final static long serialVersionUID = 100L;
    protected ScanList scanList;
    @XmlElements({
        @XmlElement(name = "neutralLoss", type = NeutralLoss.class),
        @XmlElement(name = "product", type = Product.class),
        @XmlElement(name = "precursorList", type = PrecursorList.class)
    })
    protected MzMLObject precursorListOrProductOrNeutralLoss;
    protected BinaryDataArrayList binaryDataArrayList;
    @XmlAttribute(required = true)
    protected String id;
    @XmlAttribute
    protected String spotID;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger index;
    @XmlAttribute(required = true)
    protected int defaultArrayLength;
    @XmlAttribute(name = "dataProcessingRef")
    @XmlJavaTypeAdapter(DataProcessingAdapter.class)
    @XmlSchemaType(name = "IDREF")
    protected DataProcessing dataProcessing;
    @XmlAttribute(name = "sourceFileRef")
    @XmlJavaTypeAdapter(SourceFileAdapter.class)
    @XmlSchemaType(name = "IDREF")
    protected SourceFile sourceFile;

    /**
     * Gets the value of the scanList property.
     * 
     * @return
     *     possible object is
     *     {@link ScanList }
     *     
     */
    public ScanList getScanList() {
        return scanList;
    }

    /**
     * Sets the value of the scanList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScanList }
     *     
     */
    public void setScanList(ScanList value) {
        this.scanList = value;
    }

    /**
     * Gets the value of the precursorListOrProductOrNeutralLoss property.
     * 
     * @return
     *     possible object is
     *     {@link NeutralLoss }
     *     {@link Product }
     *     {@link PrecursorList }
     *     
     */
    public MzMLObject getPrecursorListOrProductOrNeutralLoss() {
        return precursorListOrProductOrNeutralLoss;
    }

    /**
     * Sets the value of the precursorListOrProductOrNeutralLoss property.
     * 
     * @param value
     *     allowed object is
     *     {@link NeutralLoss }
     *     {@link Product }
     *     {@link PrecursorList }
     *     
     */
    public void setPrecursorListOrProductOrNeutralLoss(MzMLObject value) {
        this.precursorListOrProductOrNeutralLoss = value;
    }

    /**
     * Gets the value of the binaryDataArrayList property.
     * 
     * @return
     *     possible object is
     *     {@link BinaryDataArrayList }
     *     
     */
    public BinaryDataArrayList getBinaryDataArrayList() {
        return binaryDataArrayList;
    }

    /**
     * Sets the value of the binaryDataArrayList property.
     * 
     * @param value
     *     allowed object is
     *     {@link BinaryDataArrayList }
     *     
     */
    public void setBinaryDataArrayList(BinaryDataArrayList value) {
        this.binaryDataArrayList = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the spotID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpotID() {
        return spotID;
    }

    /**
     * Sets the value of the spotID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpotID(String value) {
        this.spotID = value;
    }

    /**
     * Gets the value of the index property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIndex(BigInteger value) {
        this.index = value;
    }

    /**
     * Gets the value of the defaultArrayLength property.
     * 
     */
    public int getDefaultArrayLength() {
        return defaultArrayLength;
    }

    /**
     * Sets the value of the defaultArrayLength property.
     * 
     */
    public void setDefaultArrayLength(int value) {
        this.defaultArrayLength = value;
    }

    /**
     * Gets the value of the dataProcessing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public DataProcessing getDataProcessing() {
        return dataProcessing;
    }

    /**
     * Sets the value of the dataProcessing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataProcessing(DataProcessing value) {
        this.dataProcessing = value;
    }

    /**
     * Gets the value of the sourceFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public SourceFile getSourceFile() {
        return sourceFile;
    }

    /**
     * Sets the value of the sourceFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceFile(SourceFile value) {
        this.sourceFile = value;
    }

}