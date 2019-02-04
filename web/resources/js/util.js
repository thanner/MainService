function insertInDropDown(elementList, dropdownListElement) {
    if (elementList.length === 0) {
        $("<li><a class=\"small\" data-value=\"empty\" tabIndex=\"-1\"><span>Empty</span></a></li>").appendTo(dropdownListElement);
    } else {
        elementList.forEach(function (item) {
            $("<li><a class=\"small\" data-value=\"empty\" tabIndex=\"-1\"><input type=\"checkbox\"/><span>" + item + "</span></a></li>").appendTo(dropdownListElement);
        });
    }
}

function captalizeFirstLetterList(elementList) {
    var elementId;
    for (elementId = 0; elementId < elementList.length; elementId++) {
        elementList[elementId] = elementList[elementId].charAt(0).toUpperCase() + elementList[elementId].substr(1);
    }
    return elementList;
}

function isDropDownElementMarked(elementToMark, dropDownName) {
    var elements = document.getElementById(dropDownName).getElementsByTagName("li");
    var k;
    for (k = 0; k < elements.length; k++) {
        var element = elements[k];
        if (element.hasChildNodes("input") && element.hasChildNodes("span")) {
            var inputList = element.getElementsByTagName("input");
            var textList = element.getElementsByTagName("span");
            if (inputList.length > 0 && textList.length > 0) {
                var input = inputList[0];
                var text = textList[0];
                if (input.checked && text.textContent.toLowerCase() === elementToMark.toLowerCase()) {
                    return true;
                }
            }
        }
    }
    return false;
}

function isDropDownMarked(dropDownName) {
    var elements = document.getElementById(dropDownName).getElementsByTagName("li");
    var k;
    for (k = 0; k < elements.length; k++) {
        var element = elements[k];
        if (element.hasChildNodes("input")) {
            var inputList = element.getElementsByTagName("input");
            if (inputList.length > 0) {
                var input = inputList[0];
                if (input.checked) {
                    return true;
                }
            }
        }
    }
    return false;
}