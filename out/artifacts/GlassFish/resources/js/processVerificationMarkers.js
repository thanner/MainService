function getVerificationErrors(snippetXML) {
    var messagesTooltip = "";
    var verificationMessageList = getVerificationMessageList();
    verificationMessageList = filterVerificationMessages(verificationMessageList);

    var messageToolTipId = 1;
    var elementMessageId;
    for (elementMessageId = 0; elementMessageId < verificationMessageList.length; elementMessageId++) {
        if (snippetXML.getAttribute("processElementId") === verificationMessageList[elementMessageId].getAttribute("processElementId")) {
            var messageList = verificationMessageList[elementMessageId].getElementsByTagName("message");
            var messageId;
            for (messageId = 0; messageId < messageList.length; messageId++) {
                var description = messageList[messageId].getAttribute("description");
                messagesTooltip = messagesTooltip + messageToolTipId + ") " + description + "&#10;";
                messageToolTipId = messageToolTipId + 1;
            }
        }
    }

    return messagesTooltip;
}

function changeVerificationErrorsText() {
    var errorstexttarget = "";
    var verificationMessageList = getVerificationMessageList();
    verificationMessageList = filterVerificationMessages(verificationMessageList);

    // errorsList = sortErrorListElements(errorsList);

    var errorId = 1;
    var elementMessageId;
    for (elementMessageId = 0; elementMessageId < verificationMessageList.length; elementMessageId++) {
        var processElementId = verificationMessageList[elementMessageId].getAttribute("processElementId");
        if (!processElementId) {
            processElementId = "None";
        }
        errorstexttarget += "<b>Process Element: " + processElementId + "</b> &#10;";

        var messageList = verificationMessageList[elementMessageId].getElementsByTagName("message");
        var messageId;
        for (messageId = 0; messageId < messageList.length; messageId++) {
            var description = messageList[messageId].getAttribute("description");
            errorstexttarget += "&emsp;&emsp;&emsp;" + errorId + ") " + description + " &#10; ";
            errorId += 1;
        }
    }

    document.getElementById("errorstexttarget").innerHTML = errorstexttarget;
}

function getVerificationMessageList() {
    var errorsSource = stringToXml(document.getElementById("errorssource").textContent);
    return errorsSource.getElementsByTagName("element");
}

function sortErrorListElements(errorList) {
    errorList = [].slice.call(errorList);
    errorList.sort(function (a, b) {
        return b.getAttribute("processElementId") - a.getAttribute("processElementId")
    });

    return errorList;
}

// Source DropDown

function updateVerificationSources() {
    removeVerificationSources();
    var verificationSourcesSet = getVerificationSourceSet();
    var verificationSources = Array.from(verificationSourcesSet).sort();
    verificationSources = captalizeFirstLetterList(verificationSources);
    insertVerificationSources(verificationSources);
}

function removeVerificationSources() {
    $("#verificationsourcesdropdownlist").empty();
}

function getVerificationSourceSet() {
    return getVerificationAttribute("source");
}

function insertVerificationSources(resources) {
    insertInDropDown(resources, "#verificationsourcesdropdownlist");
}

// Type DropwDown

function updateVerificationTypes() {
    removeVerificationTypes();
    var verificationTypeSet = getVerificationTypeSet();
    var verificationTypes = Array.from(verificationTypeSet).sort();
    verificationTypes = captalizeFirstLetterList(verificationTypes);
    insertVerificationType(verificationTypes);
}

function removeVerificationTypes() {
    $("#verificationtypesdropdownlist").empty();
}

function getVerificationTypeSet() {
    return getVerificationAttribute("messageType");
}

function insertVerificationType(resources) {
    insertInDropDown(resources, "#verificationtypesdropdownlist");
}

function getVerificationAttribute(attributeName) {
    var verificationSet = new Set();
    var verificationMessageList = getVerificationMessageList();

    var errorsId;
    for (errorsId = 0; errorsId < verificationMessageList.length; errorsId++) {
        var messageList = verificationMessageList[errorsId].getElementsByTagName("message");
        var messageId;
        for (messageId = 0; messageId < messageList.length; messageId++) {
            var attributeElement = messageList[messageId].getAttribute(attributeName);
            if (attributeElement) {
                verificationSet.add(attributeElement);
            }
        }
    }

    return verificationSet;
}

function filterVerificationMessages(processElementList) {
    var verificationMessageId;
    for (verificationMessageId = processElementList.length - 1; verificationMessageId >= 0; verificationMessageId--) {
        var processElement = processElementList[verificationMessageId];
        var messageList = processElement.getElementsByTagName("message");

        var filterMessages = Array.from(messageList).filter(function (message) {
            return verifyVerificationMessage(message); //isDropDownElementMarked(message.getAttribute("source"), "verificationsourcesdropdownlist") && isDropDownElementMarked(message.getAttribute("messageType"), "verificationtypesdropdownlist");
        });

        var index;
        for (index = messageList.length - 1; index >= 0; index--) {
            var message = messageList[index];
            if (!filterMessages.includes(message)) {
                processElement.removeChild(messageList[index]);
            }
        }

        if (messageList.length === 0) {
            processElement.parentNode.removeChild(processElement);
        }
    }
    return processElementList;
}

function verifyVerificationMessage(message) {
    var isSource = isDropDownMarked("verificationsourcesdropdownlist") ? isDropDownElementMarked(message.getAttribute("source"), "verificationsourcesdropdownlist") : true;
    var isType = isDropDownMarked("verificationtypesdropdownlist") ? isDropDownElementMarked(message.getAttribute("messageType"), "verificationtypesdropdownlist") : true;
    return isSource && isType;
}