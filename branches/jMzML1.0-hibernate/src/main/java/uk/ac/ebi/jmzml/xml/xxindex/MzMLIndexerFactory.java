package uk.ac.ebi.jmzml.xml.xxindex;

import org.apache.log4j.Logger;
import psidev.psi.tools.xxindex.*;
import psidev.psi.tools.xxindex.index.IndexElement;
import psidev.psi.tools.xxindex.index.XpathIndex;
import uk.ac.ebi.jmzml.xml.Constants;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: rcote
 * Date: 11-Jun-2008
 * Time: 17:09:40
 * $Id: $
 */
public class MzMLIndexerFactory {

    private static final Logger logger = Logger.getLogger(MzMLIndexerFactory.class);

    // TODO what if we have to index more than one file at a time?
    private static final MzMLIndexerFactory instance = new MzMLIndexerFactory();
    private static final Pattern ID_PATTERN = Pattern.compile("\\sid\\s*=\\s*['\"]([^'\"]*)['\"]", Pattern.CASE_INSENSITIVE);
    private static final Pattern INDEX_PATTERN = Pattern.compile("\\sindex\\s*=\\s*['\"]([^'\"]*)['\"]", Pattern.CASE_INSENSITIVE);

    private MzMLIndexerFactory() {
    }

    public static MzMLIndexerFactory getInstance() {
        return instance;
    }

    /**
     * In case of any new Index instance needed.
     * For example if the user wants to parse more than one mzml file.
     *
     * @return new instance of MzMLIndexerFactory
     *
     */
    public static MzMLIndexerFactory getNewInstance(){
        return new MzMLIndexerFactory();
    }

    public MzMLIndexer buildIndex(File xmlFile) {
        return new MzMlIndexerImpl(xmlFile);
    }

    private class MzMlIndexerImpl implements MzMLIndexer {

        private File xmlFile = null;
        private XpathAccess xpathAccess = null;
        private XmlElementExtractor xmlExtractor = null;
        private XpathIndex index = null;
        private String root = null;
        private String mzMLAttributeXMLString = null;

        // ToDo: replace by generic map of maps based on classes as in the AdapterObjectCache
        private HashMap<String, IndexElement> cvIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> dataProcessingIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> instrConfigIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> refParamGroupIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> sampleIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> softwareIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> sourceFileIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> spectrumIdMap = new HashMap<String, IndexElement>();
        private HashMap<BigInteger, String> spectrumIndexToIDMap = new HashMap<BigInteger, String>();
        private HashMap<String, IndexElement> chromatogramIdMap = new HashMap<String, IndexElement>();
        private HashMap<String, IndexElement> scanSettingsIdMap = new HashMap<String, IndexElement>();

        private MzMlIndexerImpl(File xmlFile) {

            if (xmlFile == null) {
                throw new IllegalStateException("XML File to index must not be null");
            }
            if (!xmlFile.exists()) {
                throw new IllegalStateException("XML File to index does not exist: " + xmlFile.getAbsolutePath());
            }

            try {

                //store file reference
                this.xmlFile = xmlFile;

                //generate XXINDEX
                logger.info("Creating index: ");
                xpathAccess = new StandardXpathAccess(xmlFile, Constants.XML_INDEXED_XPATHS);
                logger.debug("done!");

                //create xml element extractor
                xmlExtractor = new SimpleXmlElementExtractor();
                String encoding = xmlExtractor.detectFileEncoding(xmlFile.toURI().toURL());
                if (encoding != null) {
                    xmlExtractor.setEncoding(encoding);
                }

                //create index
                index = xpathAccess.getIndex();

                //ToDo: !! find different way to do this! this does not work if e.g. searching for /indexedmzML/indexList !!!
                //initialize xpath root
                root = "/mzML";
                // check if the xxindex contains this root
                if (!index.containsXpath(root)) {
                    // if not contained in the xxindex, then maybe we have a indexedzmML file
                    root = "/indexedmzML/mzML";
                    if (!index.containsXpath(root)) {
                        // if neither a indexedmzML, then we are in trouble, we can not handle it!
                        throw new IllegalStateException("Invalid XML - /mzML or /indexedmzML xpaths not found!");
                    }
                    logger.info("We are dealing with an indexedmzML file!");
                }

                //prefetch some elements that will be referenced multiple times
                //note that some of these prefetched elements also reference other elements
                //so need to pass in preconfigured Adapters to deal with the references

                // ToDo: check if we can use the constants for the following xpaths
                // ToDo: maybe even a map of maps that automatically generates a cache for each cachable zpath

                //cv cache
                logger.info("Init CV cache");
                initIdMapCache(cvIdMap, null, "/cvList/cv");

                //dataProcessing cache
                logger.info("Init DataProcessing cache");
                initIdMapCache(dataProcessingIdMap, null, "/dataProcessingList/dataProcessing");

                //instrumentConfig cache
                logger.info("Init InstrumentConfiguration cache");
                initIdMapCache(instrConfigIdMap, null, "/instrumentConfigurationList/instrumentConfiguration");

                //refParamGroup cache
                logger.info("Init ReferenceableParamGroup cache");
                initIdMapCache(refParamGroupIdMap, null, "/referenceableParamGroupList/referenceableParamGroup");

                //sample cache
                logger.info("Init Sample cache");
                initIdMapCache(sampleIdMap, null, "/sampleList/sample");

                //software cache
                logger.info("Init Software cache");
                initIdMapCache(softwareIdMap, null, "/softwareList/software");

                //sourceFile cache
                logger.info("Init SourceFile cache");
                initIdMapCache(sourceFileIdMap, null, "/fileDescription/sourceFileList/sourceFile");

                //spectrum cache
                logger.info("Init Spectrum cache");
                initIdMapCache(spectrumIdMap, spectrumIndexToIDMap, "/run/spectrumList/spectrum");

                //spectrum cache
                logger.info("Init Chromatogram cache");
                initIdMapCache(chromatogramIdMap, null, "/run/chromatogramList/chromatogram");

                //scansettings cache
                logger.info("Init ScanSettings cache");
                initIdMapCache(scanSettingsIdMap, null, "/scanSettingList/scanSetting");

                //extract the MzML attributes from the MzML start tag
                //get start position
                List<IndexElement> ie = index.getElements(root);
                //there is only one root
                IndexElement rootEl = ie.get(0);
                long startPos = rootEl.getStart();

                //get end position - this is the start position of the next tag
                ie = index.getElements(root + "/cvList");
                //there will always be one and only one cvList
                IndexElement cvListEl = ie.get(0);

                long stopPos = cvListEl.getStart() - 1;

                //get mzML start tag content
                mzMLAttributeXMLString = xmlExtractor.readString(startPos, stopPos, xmlFile);
                if (mzMLAttributeXMLString != null){
                    //strip newlines that might interfere with later on regex matching
                    mzMLAttributeXMLString = mzMLAttributeXMLString.replace("\n", "");
                }

            } catch (IOException e) {
                logger.error("MzMLIndexerFactory$MzMlIndexerImpl.MzMlIndexerImpl", e);
                throw new IllegalStateException("Could not generate index file for: " + xmlFile);
            }

        }

        public String getMzMLAttributeXMLString() {
            return mzMLAttributeXMLString;
        }

        /**
         * This method initializes the specified ID (and optionally the specified Index) Map(s)
         * for the given XPath. The index map is optional, because although most elements have an ID,
         * only some have an index.
         *
         * @param idMap HashMap<String, IndexElement> to contain the ID-based index.
         * @param indexMap HashMap<BigInteger, IndexElement> to contain an index to ID lookup map.
         *                  Can be 'null' as the index is not always present for each element. If this
         *                  parameter is 'null', the index attribute is simply ignored.
         * @param xpath teh XPath to index for.
         * @throws IOException  when the reading or parsing of the XML file failed. 
         */
        private void initIdMapCache(HashMap<String, IndexElement> idMap, HashMap<BigInteger, String> indexMap, String xpath) throws IOException {
            List<IndexElement> ranges = index.getElements(root + xpath);
            for (IndexElement byteRange : ranges) {
                String xml = readXML(byteRange);
                // Get the ID.
                String id = getIdFromRawXML(xml);
                if (id != null) {
                    idMap.put(id, byteRange);
                    // See if we also need to get the index translation map.
                    if(indexMap != null) {
                        BigInteger index = getIndexFromRawXML(xml);
                        if (index != null) {
                            indexMap.put(index, id);
                        }
                    }
                }
            }
        }

        private String getIdFromRawXML(String xml) {
            Matcher match = ID_PATTERN.matcher(xml);
            if (match.find()) {
                return match.group(1).intern();
            } else {
                throw new IllegalStateException("Invalid ID in xml: " + xml);
            }
        }

        private BigInteger getIndexFromRawXML(String xml) {
            Matcher match = INDEX_PATTERN.matcher(xml);
            if (match.find()) {
                String result = match.group(1).intern();
                try {
                    BigInteger biResult = new BigInteger(result);
                    return biResult;
                } catch(NumberFormatException nfe) {
                    throw new IllegalStateException("Index attribute could not be parsed into an integer in xml: " + xml);
                }
            } else {
                throw new IllegalStateException("Invalid index in xml: " + xml);
            }
        }
        
        public Set<String> getSpectrumIDs() {
            return spectrumIdMap.keySet();
        }

        public Set<BigInteger> getSpectrumIndexes() {
            return spectrumIndexToIDMap.keySet();
        }

        public String getSpectrumIDFromSpectrumIndex(BigInteger aIndex) {
            return spectrumIndexToIDMap.get(aIndex);
        }

        public Set<String> getChromatogramIDs() {
            return chromatogramIdMap.keySet();
        }

        public Iterator<String> getXmlStringIterator(String xpathExpression) {
            // check if we are required to provide the indexList of the indexedmzML
            if (xpathExpression.contains("indexList") || xpathExpression.contains("fileChecksum")) {
                // we can not use the root "mzML", since the mzML index list is outside the mzML!
                return xpathAccess.getXmlSnippetIterator("/indexedmzML" + checkRoot(xpathExpression));
            } else {
                // Note: ! root is always the mzML element (even if we are dealing with indexedmzML) !
                return xpathAccess.getXmlSnippetIterator(root + checkRoot(xpathExpression));
            }
        }

        private String checkRoot(String xpathExpression) {
            // since we're appending the root we've already checked, make
            // sure that the xpath doesn't erroneously contain that root

            // get rid of possible '/indexedmzML' root
            String unrootedXpath = xpathExpression;
            if (unrootedXpath.startsWith("/indexedmzML")) {
                unrootedXpath = unrootedXpath.substring("/indexedmzML".length());
                logger.debug("removed /indexedmzML root expression");
            }
            // get rid of possible '/mzML' root
            if (unrootedXpath.startsWith("/mzML")) {
                unrootedXpath = unrootedXpath.substring("/mzML".length());
                logger.debug("removed /mzML root expression");
            }
            return unrootedXpath;
        }

        public String getXmlString(String ID, Constants.ReferencedType type) {

            logger.debug("Getting cached ID: " + ID + " from cache: " + type);

            String xml;
            switch (type) {

                case CV:
                    xml = readXML(cvIdMap.get(ID));
                    break;
                case DataProcessing:
                    xml = readXML(dataProcessingIdMap.get(ID));
                    break;
                case InstrumentConfiguration:
                    xml = readXML(instrConfigIdMap.get(ID));
                    break;
                case ReferenceableParamGroup:
                    xml = readXML(refParamGroupIdMap.get(ID));
                    break;
                case Sample:
                    xml = readXML(sampleIdMap.get(ID));
                    break;
                case Software:
                    xml = readXML(softwareIdMap.get(ID));
                    break;
                case SourceFile:
                    xml = readXML(sourceFileIdMap.get(ID));
                    break;
                case Spectrum:
                    xml = readXML(spectrumIdMap.get(ID));
                    break;
                case Chromatogram:
                    xml = readXML(chromatogramIdMap.get(ID));
                    break;
                case ScanSettings:
                    xml = readXML(scanSettingsIdMap.get(ID));
                    break;
                default:
                    throw new IllegalStateException("Unkonwn cache type: " + type);

            }

            return xml;

        }

        private String readXML(IndexElement byteRange) {
            try {
                if (byteRange != null) {
                    return xmlExtractor.readString(byteRange.getStart(), byteRange.getStop(), xmlFile);
                } else {
                    throw new IllegalStateException("Attempting to read NULL ByteRange");
                }
            } catch (IOException e) {
                logger.error("MzMLIndexerFactory$MzMlIndexerImpl.readXML", e);
                throw new IllegalStateException("Could not extract XML from file: " + xmlFile);
            }
        }

        public int getCount(String xpathExpression){
            int retval = 0;
            List<IndexElement> tmpList = index.getElements(root + checkRoot(xpathExpression));
            if (tmpList != null){
                retval = tmpList.size();
            }
            return retval;
        }

        public String getXmlString(String xpath, long offset) {
            String retVal = null;
            List<IndexElement> indexElements = index.getElements(xpath);
            for (IndexElement indexElement : indexElements) {
                if (indexElement.getStart() == offset) {
                    // found what we are looking for
                    try {
                        retVal = xmlExtractor.readString(indexElement.getStart(), indexElement.getStop(), xmlFile);
                    } catch (IOException ioe) {
                        logger.error("MzMLIndexerFactory$MzMlIndexerImpl.getXmlString(xpath, offset)", ioe);
                        throw new IllegalStateException("Could not extract XML from file: " + xmlFile);
                    }
                    break; // there will only be max one element with a specific offset,
                           // but it does not harm to step out of the loop manually
                }
            }
            return retVal;
        }

        // ToDo: find better way. we don't want to expose this!
        public List<IndexElement> getIndexElements(String xpathExpression) {
            return index.getElements(xpathExpression);
        }

        public Set<String> getXpath() {
            return index.getKeys();
        }

    }


}