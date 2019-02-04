function isHighlightResource(resource) {
    return isDropDownElementMarked(resource, "resourcesdropdownlist");
    /*
    var elements = document.getElementById("resourcesdropdownlist").getElementsByTagName("li");
    for (k = 0; k < elements.length; k++) {
        var element = elements[k];
        if (element.hasChildNodes("input") && element.hasChildNodes("span")) {
            var inputList = element.getElementsByTagName("input");
            var textList = element.getElementsByTagName("span");
            if (inputList.length > 0 && textList.length > 0) {
                var input = inputList[0];
                var text = textList[0];
                if (input.checked && text.textContent === resource) {
                    return true;
                }
            }
        }
    }
    return false;
    */
}

function updateResources() {
    removeResources();
    var resourceSet = getResourceSet();
    var resources = Array.from(resourceSet).sort();
    insertResources(resources);
}

function removeResources() {
    $("#resourcesdropdownlist").empty();
}

function getResourceSet() {
    var resourceSet = new Set();

    var sourceElement = stringToXml(document.getElementById("structuredtextsource").textContent);
    if (sourceElement) {
        var sentenceXMLList = sourceElement.getElementsByTagName("snippet");
        for (sentenceId = 0; sentenceId < sentenceXMLList.length; sentenceId++) {
            var sentenceXML = sentenceXMLList[sentenceId];
            var resource = sentenceXML.getAttribute("resource");

            if (resource) {
                resourceSet.add(resource);
            }
        }
    }

    return resourceSet;
}

function insertResources(resources) {
    insertInDropDown(resources, "#resourcesdropdownlist");
}