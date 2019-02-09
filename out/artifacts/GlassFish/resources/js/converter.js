function stringToXml(xmlString) {
    return jQuery.parseXML(xmlString);
}

function convertToString(xmlData) {
    var xmlString;
    // IE browser
    if (window.ActiveXObject) {
        xmlString = xmlData.xml;
    }
    // Others
    else {
        xmlString = (new XMLSerializer()).serializeToString(xmlData);
    }
    return xmlString;
}

function isXML(xml) {
    try {
        xmlDoc = $.parseXML(xml); //is valid XML
        return true;
    } catch (err) {
        // was not XML
        return false;
    }
}