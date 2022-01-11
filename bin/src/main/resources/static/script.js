window.onload = function exampleFunction() {
    document.getElementById("uploadBtnSala").onchange = function () {
        document.getElementById("uploadFileSala").value = this.value;
    };
    document.getElementById("uploadBtnHorario").onchange = function () {
        document.getElementById("uploadFileHorario").value = this.value;
    };
}

function updateLabel(elem){
    let label =  $('label[for='+  elem.id  +']')
    label.html(elem.value)
    label.css("cssText", "margin-left: " + ((elem.value * 94.5 / 30)) + "% !important;");
}