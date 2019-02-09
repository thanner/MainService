var processElementsColors = {
    ACTIVITY: "blue",

    STARTEVENT: "lime",
    INTERMEDIATEEVENT: "gold",
    ENDEVENT: "red",

    XORSPLIT: "darkblue",
    XORJOIN: "darkblue",
    ANDSPLIT: "darkgreen",
    ANDJOIN: "darkgreen",
    ORSPLIT: "goldenrod",
    ORJOIN: "goldenrod",
    EVENTBASEDGATEWAY: "darkred",

    default: "black"
};

function getProcessElementColor(processElementType) {
    if (isProcessElementStructured(processElementType)) {
        var color = processElementsColors[processElementType];
        return color === null ? processElementsColors.default : color;
    } else {
        return processElementsColors.default;
    }
}

function isProcessElementStructured(processElementType) {

    if (processElementType === "ACTIVITY") {
        return document.getElementById("hasMarkActivity").checked;
    }

    if (processElementType === "STARTEVENT") {
        return document.getElementById("hasMarkStartEvent").checked;
    }
    if (processElementType === "INTERMEDIATEEVENT") {
        return document.getElementById("hasMarkIntermediateEvent").checked;
    }
    if (processElementType === "ENDEVENT") {
        return document.getElementById("hasMarkEndEvent").checked;
    }

    if (processElementType === "XORSPLIT") {
        return document.getElementById("hasMarkXorSplit").checked;
    }
    if (processElementType === "XORJOIN") {
        return document.getElementById("hasMarkXorJoin").checked;
    }
    if (processElementType === "ANDSPLIT") {
        return document.getElementById("hasMarkAndSplit").checked;
    }
    if (processElementType === "ANDJOIN") {
        return document.getElementById("hasMarkAndJoin").checked;
    }
    if (processElementType === "ORSPLIT") {
        return document.getElementById("hasMarkOrSplit").checked;
    }
    if (processElementType === "ORJOIN") {
        return document.getElementById("hasMarkOrJoin").checked;
    }
    if(processElementType === "EVENTBASEDGATEWAY") {
        return document.getElementById("hasMarkEventBasedGateway").checked;
    }

    return false;
}

function changeColorCheckBoxes() {
    document.getElementById("activityBox").style.backgroundColor = processElementsColors.ACTIVITY;

    document.getElementById("startEventBox").style.backgroundColor = processElementsColors.STARTEVENT;
    document.getElementById("intermediateEventBox").style.backgroundColor = processElementsColors.INTERMEDIATEEVENT;
    document.getElementById("endEventBox").style.backgroundColor = processElementsColors.ENDEVENT;

    document.getElementById("xorSplitBox").style.backgroundColor = processElementsColors.XORSPLIT;
    document.getElementById("xorJoinBox").style.backgroundColor = processElementsColors.XORJOIN;
    document.getElementById("andSplitBox").style.backgroundColor = processElementsColors.ANDSPLIT;
    document.getElementById("andJoinBox").style.backgroundColor = processElementsColors.ANDJOIN;
    document.getElementById("orSplitBox").style.backgroundColor = processElementsColors.ORSPLIT;
    document.getElementById("orJoinBox").style.backgroundColor = processElementsColors.ORJOIN;
    document.getElementById("eventBasedGatewayBox").style.backgroundColor = processElementsColors.EVENTBASEDGATEWAY;
}

function changeCheckedCheckBoxes() {
    var isCheck = document.getElementById("all-checkbox").checked;

    document.getElementById("hasMarkActivity").checked = isCheck;

    document.getElementById("hasMarkStartEvent").checked = isCheck;
    document.getElementById("hasMarkIntermediateEvent").checked = isCheck;
    document.getElementById("hasMarkEndEvent").checked = isCheck;

    document.getElementById("hasMarkXorSplit").checked = isCheck;
    document.getElementById("hasMarkXorJoin").checked = isCheck;
    document.getElementById("hasMarkAndSplit").checked = isCheck;
    document.getElementById("hasMarkAndJoin").checked = isCheck;
    document.getElementById("hasMarkOrSplit").checked = isCheck;
    document.getElementById("hasMarkOrJoin").checked = isCheck;
    document.getElementById("hasMarkEventBasedGateway").checked = isCheck;
}

function changeCheckedAllCheckbox() {
    document.getElementById("all-checkbox").checked = isAllCheckboxesChecked();
}

function isAllCheckboxesChecked() {
    var checkboxes = document.getElementsByClassName("checkbox-marked-process-element");
    for (checkboxId = 0; checkboxId < checkboxes.length; checkboxId++) {
        var checkbox = checkboxes[checkboxId];
        if (!checkbox.checked) {
            return false;
        }
    }
    return true;
}