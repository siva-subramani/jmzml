package uk.ac.ebi.jmzml.xml.jaxb.adapters;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import uk.ac.ebi.jmzml.model.mzml.*;
import uk.ac.ebi.jmzml.xml.Constants;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.UnmarshallerFactory;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.cache.AdapterObjectCache;
import uk.ac.ebi.jmzml.xml.jaxb.unmarshaller.filters.MzMLNamespaceFilter;
import uk.ac.ebi.jmzml.xml.xxindex.MzMLIndexer;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.transform.sax.SAXSource;
import java.io.StringReader;

/**
 * User: rcote
 * Date: 11-Jun-2008
 * Time: 14:36:37
 * $Id: $
 */
public abstract class AbstractResolvingAdapter<ValueType, BoundType> extends XmlAdapter<ValueType, BoundType> {

    protected static final Logger logger = Logger.getLogger(AbstractResolvingAdapter.class);

    protected MzMLIndexer index = null;
    protected AdapterObjectCache cache = null;

    /**
     * the presence of a constructor forces all subclasses to provide a valid indexer
     * for reference resolving. This will also prevent jaxb from using the no-argument
     * constructor versions of these Adapters.
     *
     * @param index
     */
    protected AbstractResolvingAdapter(MzMLIndexer index, AdapterObjectCache cache) {
        this.index = index;
        this.cache = cache;
    }

    public <BoundType> BoundType unmarshal(String refId, Constants.ReferencedType refType) {

        logger.debug("AbstractResolvingAdapter.unmarshal for id: " + refId);

        String xml = index.getXmlString(refId, refType);

        if (logger.isDebugEnabled()){
            logger.debug("read xml is = " + xml);
        }

        Class cls;
        switch (refType) {

            case CV:
                cls = CV.class;
                break;
            case DataProcessing:
                cls = DataProcessing.class;
                break;
            case InstrumentConfiguration:
                cls = InstrumentConfiguration.class;
                break;
            case ReferenceableParamGroup:
                cls = ReferenceableParamGroup.class;
                break;
            case Sample:
                cls = Sample.class;
                break;
            case Software:
                cls = Software.class;
                break;
            case SourceFile:
                cls = SourceFile.class;
                break;
            case Spectrum:
                cls = Spectrum.class;
                break;
            default:
                throw new IllegalStateException("Unkonwn cache type: " + refType);
        }

        try {

            //required for the addition of namespaces to top-level objects
            MzMLNamespaceFilter xmlFilter = new MzMLNamespaceFilter();
            //initializeUnmarshaller will assign the proper reader to the xmlFilter
            //this also propagates the cache so that any associated IDREF calls
            //are handled efficiently if possible
            Unmarshaller unmarshaller = UnmarshallerFactory.getInstance().initializeUnmarshaller(index, xmlFilter, cache);

            //need to do it this way because snippet does not have a XmlRootElement annotation
            JAXBElement<BoundType> holder = unmarshaller.unmarshal(new SAXSource(xmlFilter, new InputSource(new StringReader(xml))), cls);
            return holder.getValue();

        } catch (JAXBException e) {
            logger.error("AbstractResolvingAdapter.unmarshal", e);
            throw new IllegalStateException("Could not unmarshall refId: " + refId + " of type: " + refType);
        }

    }

}
