var lastResource;
var lastLevel;
var bulletPointIdList;

var paragraphSizeCount;

function createStructureVariables() {
    lastResource = null;
    lastLevel = "0";
    bulletPointIdList = [0];

    paragraphSizeCount = 0;
}

function defaultStructure() {
    /*
    $("#indentation-checkbox").prop('checked', false);
    $("#split-resource-checkbox").prop('checked', false);
    $("#split-at-gateway-checkbox").prop('checked', false);
    $("#keep-branch-together-checkbox").prop('checked', false);
    $("#bullet-point-only-new-paragraph-checkbox").prop('checked', false);

    $("#per-sentence-input-number").prop('checked', true);
    $("#per-sentence-input-number").prop('disabled', false);
    $("#per-sentence-input-number").prop('value', 5);
    */
    blockLineSplitRadios();
    blockTabsToogleButton();
}

function setStructure(sentenceXML, sentenceText) {
    var currentResource = getResourceFirstElement(sentenceXML);
    var currentLevel = sentenceXML.getAttribute("level");

    var isFirstParagraph = getIsFirstParagraph();
    var isNewParagraph = getIsNewParagraph(sentenceXML);
    var newLineAmount = getNewLineAmount(isNewParagraph);
    var tabAmount = getTabAmount(sentenceXML, isFirstParagraph, isNewParagraph);

    sentenceText = setBulletPoint(sentenceXML, sentenceText, isNewParagraph);
    sentenceText = setTabAmount(sentenceText, tabAmount);
    sentenceText = setNewLineAmount(sentenceText, newLineAmount);

    lastResource = currentResource;
    lastLevel = currentLevel;

    return sentenceText;
}

function getIsFirstParagraph() {
    return paragraphSizeCount === 0;
}

function getResourceFirstElement(sentenceXML){
    var snippetXMLList = sentenceXML.getElementsByTagName("snippet");
    snippetXMLList = [].slice.call(snippetXMLList);
    snippetXMLList.sort(function (a, b) {
        return a.getAttribute("startIndex") - b.getAttribute("startIndex")
    });

    if(snippetXMLList.length >= 1){
        var firstSnippet = snippetXMLList[0];
        if(firstSnippet !== null){
            return firstSnippet.getAttribute("resource");
        }
    }
    return "";
}

function getIsNewParagraph(sentenceXML) {
    var sentenceText = sentenceXML.getAttribute("value");
    var newLineType = $("input[type='radio'][name='option-line-split-radio']:checked").val();

    switch (newLineType) {
        case "sentence":
            return isNewParagraphGeneric(sentenceXML, 1, $("#per-sentence-input-number").val());
        case "word":
            return isNewParagraphGeneric(sentenceXML, sentenceText.split(' ').length, $("#per-word-input-number").val());
        case "level":
            return isNewParagraphPerLevel(sentenceXML);
        default:
            return isNewParagraphGeneric(sentenceXML, 1, null);
    }
}

function isNewParagraphPerLevel(sentenceXML) {
    var currentLevel = sentenceXML.getAttribute("level");
    var isLateral = sentenceXML.getAttribute("isLateral");

    return currentLevel !== lastLevel || isLateral === "true";
}

function isNewParagraphGeneric(sentenceXML, auxCount, consideredSize) {
    var currentResource = getResourceFirstElement(sentenceXML);
    var currentLevel = sentenceXML.getAttribute("level");
    var isLateral = sentenceXML.getAttribute("isLateral");
    var isNewLine = false;

    paragraphSizeCount += auxCount;
    if (!isKeepBranchTogether(currentLevel)) {
        if ((consideredSize !== null && paragraphSizeCount > consideredSize) || isSplitResources(currentResource) || isSplitAtTheGateway(currentLevel) || isSplitProcedure(isLateral)) {
            paragraphSizeCount = auxCount;
            isNewLine = true;
        }
    }
    return isNewLine;
}

function getNewLineAmount(isNewLine) {
    return isNewLine ? 1 : 0;
}

function getTabAmount(sentenceXML, isFirstParagraph, isNewParagraph) {
    var currentLevel = sentenceXML.getAttribute("level");
    var tabAmount = 0;

    if (isFirstParagraph || isNewParagraph) {
        var newLineType = $("input[type='radio'][name='option-line-split-radio']:checked").val();

        if (newLineType === "level" && $("#level-tabs-checkbox").is(":checked")) {
            tabAmount = currentLevel;
        } else if ($("#indentation-checkbox").is(":checked")) {
            tabAmount = 1;
        }
    }
    return tabAmount;
}

function isSplitResources(currentResource){
    return $("#split-resource-checkbox").is(":checked") && currentResource !== lastResource && lastResource !== null;
}

function isSplitAtTheGateway(currentLevel) {
    return $("#split-at-gateway-checkbox").is(":checked") && lastLevel === "0" && currentLevel === "1";
}

function isSplitProcedure(isLateral){
    return $("#split-procedure-checkbox").is(":checked") && isLateral === "true";
}

function isKeepBranchTogether(currentLevel) {
    return $("#keep-procedure-together-checkbox").is(":checked") && currentLevel * 1 > 1;
}

function setBulletPoint(sentenceXML, sentenceText, isNewParagraph) {
    var isLateral = sentenceXML.getAttribute("isLateral");

    if (isLateral === "true") {
        if (isNewParagraph || !$("#bullet-point-only-new-paragraph-checkbox").is(":checked")) {
            var bulletPointType = $("input[type='radio'][name='option-bullet-point-radio']:checked").val();
            switch (bulletPointType) {
                case "trace":
                    sentenceText = setBulletPointTrace(sentenceText);
                    break;
                case "number":
                    sentenceText = setBulletPointIds(sentenceXML, sentenceText);
                    break;
            }
        }
    }
    return sentenceText;
}

function setBulletPointTrace(sentenceText) {
    return "- " + sentenceText;
}

function setBulletPointIds(sentenceXML, sentenceText) {
    incrementBulletPointIdList(sentenceXML);
    var pretext = getBulletPointIdList() + ") ";

    if (!$("#bullet-point-only-new-paragraph-checkbox").is(":checked")) {
        pretext = "(" + pretext;
        // sentenceText[sentenceText.length - 2] = ";";
    }
    return pretext + sentenceText;
}

function incrementBulletPointIdList(sentenceXML) {
    var currentLevel = sentenceXML.getAttribute("level");

    if (currentLevel === "1" && lastLevel === "0") {
        // New numeration
        bulletPointIdList = [1];
    } else if (currentLevel === lastLevel) {
        // Increment item
        bulletPointIdList[currentLevel - 1]++;
    } else if (currentLevel > lastLevel) {
        // New subitem
        bulletPointIdList.push(1);
    } else if (currentLevel < lastLevel) {
        // Return subitem
        bulletPointIdList = bulletPointIdList.slice(0, currentLevel);
        bulletPointIdList[currentLevel - 1]++;
    }
}

function getBulletPointIdList() {
    var bulletPointIdStr = bulletPointIdList[0];

    var idBulletPointId;
    for (idBulletPointId = 1; idBulletPointId < bulletPointIdList.length; idBulletPointId++) {
        bulletPointIdStr += "." + bulletPointIdList[idBulletPointId];
    }

    return bulletPointIdStr;
}

function setTabAmount(sentenceText, number) {
    return "\t".repeat(number) + sentenceText;
}


function setNewLineAmount(sentenceText, number) {
    return "\n".repeat(number) + sentenceText;
}

function blockLineSplitRadios() {
    var selectedRadio = $("input[type='radio'][name='option-line-split-radio']:checked").val();
    $("#per-sentence-input-number").prop('disabled', selectedRadio !== "sentence");
    $("#per-word-input-number").prop('disabled', selectedRadio !== "word");
}

function blockTabsToogleButton() {
    var selectedRadio = $("input[type='radio'][name='option-line-split-radio']:checked").val();
    var isEnable = selectedRadio === "level";
    if (isEnable) {
        $(".extra-structuring-checkbox").prop('checked', false);
    } else {
        $("#level-tabs-checkbox").prop('checked', false);
    }
    $(".extra-structuring-checkbox").prop('disabled', isEnable);
    $("#level-tabs-checkbox").prop('disabled', !isEnable);
}