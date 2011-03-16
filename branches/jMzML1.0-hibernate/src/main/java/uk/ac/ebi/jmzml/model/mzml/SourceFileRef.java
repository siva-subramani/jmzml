package uk.ac.ebi.jmzml.model.mzml;

import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;
import uk.ac.ebi.jmzml.xml.jaxb.adapters.SourceFileAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;


/**
 * <p>Java class for SourceFileRefType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="SourceFileRefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SourceFileRefType")
public class SourceFileRef
        implements Serializable, MzMLObject {

    private final static long serialVersionUID = 100L;
    @XmlAttribute(name = "ref", required = true)
    @XmlJavaTypeAdapter(SourceFileAdapter.class)
    @XmlSchemaType(name = "IDREF")
    protected SourceFile sourceFile;

    @XmlTransient
    protected long hid;

    /**
     * Gets the value of the sourceFile property.
     *
     * @return possible object is
     *         {@link String }
     */
    public SourceFile getSourceFile() {
        return sourceFile;
    }

    /**
     * Sets the value of the sourceFile property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSourceFile(SourceFile value) {
        this.sourceFile = value;
    }

}