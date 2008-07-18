package uk.ac.ebi.jmzml.xml.io;

import org.apache.log4j.Logger;
import uk.ac.ebi.jmzml.model.mzml.ModelConstants;
import uk.ac.ebi.jmzml.model.mzml.MzML;
import uk.ac.ebi.jmzml.model.mzml.interfaces.MzMLObject;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.marshaller.MarshallerFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * User: rcote
 * Date: 18-Jun-2008
 * Time: 15:59:15
 * $Id: $
 */
public class MzMLMarshaller {

    private static final Logger logger = Logger.getLogger(MzMLMarshaller.class);

//    jaxb.fragment - value must be a java.lang.Boolean
//        This property determines whether or not document level events will be generated by the Marshaller. If the property is not specified, the default is false. This property has different implications depending on which marshal api you are using - when this property is set to true:
//
//            * marshal(Object,ContentHandler) - the Marshaller won't invoke ContentHandler.startDocument() and ContentHandler.endDocument().
//            * marshal(Object,Node) - the property has no effect on this API.
//            * marshal(Object,OutputStream) - the Marshaller won't generate an xml declaration.
//            * marshal(Object,Writer) - the Marshaller won't generate an xml declaration.
//            * marshal(Object,Result) - depends on the kind of Result object, see semantics for Node, ContentHandler, and Stream APIs
//            * marshal(Object,XMLEventWriter) - the Marshaller will not generate XMLStreamConstants.START_DOCUMENT and XMLStreamConstants.END_DOCUMENT events.
//            * marshal(Object,XMLStreamWriter) - the Marshaller will not generate XMLStreamConstants.START_DOCUMENT and XMLStreamConstants.END_DOCUMENT events.
//

    public <T extends MzMLObject> String marshall(T object, Class cls) {
        StringWriter sw = new StringWriter();
        this.marshall(object, cls, sw);
        return sw.toString();
    }

    public <T extends MzMLObject> void marshall(T object, Class cls, OutputStream os) {
        this.marshall(object, cls, new OutputStreamWriter(os));
    }

    public <T extends MzMLObject> void marshall(T object, Class cls, Writer out) {

        if (object == null) {
            throw new IllegalArgumentException("Cannot marshall a NULL object");
        }

        try {

            Marshaller marshaller = MarshallerFactory.getInstance().initializeMarshaller();

            //Set JAXB_FRAGMENT_PROPERTY to true for all objects that do not have
            //a @XmlRootElement annotation
            if (!(object instanceof MzML)) {
                marshaller.setProperty(Constants.JAXB_FRAGMENT_PROPERTY, true);
                logger.debug("set fragment property to true");
            }

            marshaller.marshal(new JAXBElement(new QName("", ModelConstants.getQNameForClass(cls)), cls, object), out);

        } catch (JAXBException e) {
            logger.error("MzMLMarshaller.marshall", e);
            throw new IllegalStateException("Error while marshalling object:" + object.toString());
        }

    }

}
