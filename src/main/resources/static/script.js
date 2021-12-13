window.onload = function exampleFunction() {
    document.getElementById("uploadBtnSala").onchange = function () {
        document.getElementById("uploadFileSala").value = this.value;
    };
    document.getElementById("uploadBtnHorario").onchange = function () {
        document.getElementById("uploadFileHorario").value = this.value;
    };
}
