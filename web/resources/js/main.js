$(document).ready(function () {

    $("#all-checkbox").click(function () {
        changeCheckedCheckBoxes();
    });

    $(".checkbox-marked-process-element").click(function () {
        changeCheckedAllCheckbox();
    });

    $(".text-line-split-radio").click(function () {
        blockLineSplitRadios();
        blockTabsToogleButton();
    });

    $(".text-modifier").click(function () {
        changeText();
    });

    $(".txtbox-number-filter").keydown(function (event) {
        verifyNumber(event);
        verifyRange(event, $(this));
    });

    $(".txtbox-number-filter").keyup(function () {
        verifyEmpty($(this));
    });

    $(".txtbox-number-filter").on('input', function () {
        changeText();
    });

    $("#text-marker-toogle-button").click(function (e) {
        toggleSideBar(e, $("#text-marker-wrapper"));
    });

    $("#text-structuring-toogle-button").click(function (e) {
        toggleSideBar(e, $("#text-structuring-wrapper"));
    });

    $("#bpmn-verification-toogle-button").click(function (e) {
        toggleSideBar(e, $("#bpmn-verification-wrapper"));
    });

    defaultStructure();
    updateResources();
    changeColorCheckBoxes();
    changeText();
    updateFileType();

    updateVerificationSources();
    updateVerificationTypes();
    changeVerificationErrorsText();
});

function changeText() {
    change(stringToXml(document.getElementById("unstructuredmetatextsource").textContent), document.getElementById("unstructuredmetatexttarget"));
    change(stringToXml(document.getElementById("structuredtextsource").textContent), document.getElementById("structuredtexttarget"));
    changeVerificationErrorsText();
}

function change(sourceElement, targetElement) {
    var sentenceXMLList = sourceElement.getElementsByTagName("sentence");

    createStructureVariables();
    var sentenceId;
    for (sentenceId = 0; sentenceId < sentenceXMLList.length; sentenceId++) {
        var sentenceXML = sentenceXMLList[sentenceId];
        var sentenceText = sentenceXML.getAttribute("value");

        sentenceText = setStyle(sentenceXML, sentenceText);
        sentenceText = setStructure(sentenceXML, sentenceText);

        sentenceXMLList[sentenceId].innerHTML = sentenceText;
    }

    var textElement = convertToString(sourceElement);
    targetElement.innerHTML = textElement;
}

function setStyle(sentenceXML, sentenceText) {
    var snippetXMLList = sentenceXML.getElementsByTagName("snippet");
    snippetXMLList = [].slice.call(snippetXMLList);
    snippetXMLList.sort(function (a, b) {
        return b.getAttribute("startIndex") - a.getAttribute("startIndex")
    });

    var snippetId;
    for (snippetId = 0; snippetId < snippetXMLList.length; snippetId++) {
        var snippetXML = snippetXMLList[snippetId];

        var color = getProcessElementColor(snippetXML.getAttribute("processElementType"));
        var isBold = isHighlightResource(snippetXML.getAttribute("resource"));
        var startIndex = snippetXML.getAttribute("startIndex");
        var endIndex = snippetXML.getAttribute("endIndex");

        var sentenceText1 = sentenceText.substring(0, startIndex);
        var sentenceText2 = sentenceText.substring(startIndex, endIndex);
        var sentenceText3 = sentenceText.substring(endIndex, sentenceText.length + 1);

        if ($("#hasMarkErrors").is(":checked")) {
            var errorsTooltip = getVerificationErrors(snippetXML);
            if (errorsTooltip) {
                sentenceText2 = "<span data-toggle=\"tooltips\" data-placement=\"top\" title=\"" + errorsTooltip + "\">" + "<u>" + sentenceText2 + "</u>" + "</span>";
            }
        }

        if (color.localeCompare(processElementsColors.default)) {
            sentenceText2 = sentenceText2.fontcolor(color);
        }

        if (isBold) {
            sentenceText2 = "<b>" + sentenceText2 + "</b>";
        }

        sentenceText = sentenceText1 + sentenceText2 + sentenceText3;
    }
    return sentenceText;
}

function verifyNumber(event) {
    if ($.inArray(event.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
        // Allow: Ctrl/cmd+A
        (event.keyCode == 65 && (event.ctrlKey === true || event.metaKey === true)) ||
        // Allow: Ctrl/cmd+C
        (event.keyCode == 67 && (event.ctrlKey === true || event.metaKey === true)) ||
        // Allow: Ctrl/cmd+X
        (event.keyCode == 88 && (event.ctrlKey === true || event.metaKey === true)) ||
        // Allow: home, end, left, right
        (event.keyCode >= 35 && event.keyCode <= 39)) {
        // let it happen, don't do anything
        return;
    }

    // Ensure that it is a number and stop the keypress
    if ((event.shiftKey || (event.keyCode < 48 || event.keyCode > 57)) && (event.keyCode < 96 || event.keyCode > 105)) {
        event.preventDefault();
    }
}

function verifyRange(event, element) {
    var currentValue = String.fromCharCode(event.which);
    var finalValue = element.val() + currentValue;
    var minRange = 1;
    var maxRange = 2000;

    element.val(element.val());
    if (finalValue < minRange || finalValue > maxRange) {
        event.preventDefault();
    }
}

function verifyEmpty(element) {
    if (element.val() === "") {
        element.val(1);
    }
}

function updateFileType() {
    var unstructuredText = $("#inputtextelement").innerText;
    $("#filetypecheckbox").prop('checked', isXML(unstructuredText));
}

function toggleSideBar(event, newSideBar) {
    event.preventDefault();
    var sideBarList = [$("#text-marker-wrapper"), $("#text-structuring-wrapper"), $("#bpmn-verification-wrapper")];

    for (sidebarId = 0; sidebarId < sideBarList.length; sidebarId++) {
        sideBar = sideBarList[sidebarId];
        if (sideBar[0] != newSideBar[0]) {
            if (sideBar.hasClass("toggled")) {
                sideBar.removeClass("toggled");
            }
        }
    }

    newSideBar.toggleClass("toggled");
}