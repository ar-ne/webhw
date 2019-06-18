function formSubmit(form = 'form', button = null, callbefore = null, callafter = null) {
    form = $('#' + form);
    if (button !== null) $('#' + button).attr("disabled", "");
    if (callbefore !== null) callbefore();
    doSubmit(form);
    if (callafter !== null) callafter();
    if (button !== null) $('#' + button).removeAttr("disabled");
    return false;
}

function doSubmit(form) {
    $.ajax({
        type: form.attr("method"),
        url: form.attr("action"),
        data: form.serialize(),
        dataType: "xhr",
        async: true,
        cache: false,
        complete: function (xhr, textStatus) {
            console.log(xhr);
            switch (xhr.status) {
                case 200:
                    Turbolinks.visit(xhr.responseText);
                    break;
                case 201:
                    Turbolinks.visit(xhr.responseText, {action: "replace"});
                    break;
                case 202:
                    window.location.href = xhr.responseText;
                    break;
                case 203:
                    eval(xhr.responseText.toString());
                    break;
                case 204:
                    showSuccessAlert();
                    break;
                case 205:
                    showSuccessAlert(xhr.responseText.toString());
                    break;
                default:
                    if (xhr.responseText !== null && xhr.responseText !== "")
                        showFailAlert(xhr.responseText);
                    else showFailAlert();
                    break;
            }
        }
    });
}

function showSuccessAlert(msg = null, time = -1) {
    showAlert(msg === null ? "成功/完成" : msg, time);

}

function showFailAlert(msg = null, time = 3000) {
    showAlert(msg === null ? "错误/失败" : msg, time, "danger", "clear");
}

let alertCount = 0;

function showAlert(message, time = -1, type = 'success', icon = 'check') {
    type = "alert-" + type;
    const current = alertCount++;
    $("#alert").append("<div class='alert " + type + " alert-dismissible fade mb-0' role='alert'>\n" +
        "                <button id='alertCount-" + current + "' type=\"button\" class='close' data-dismiss='alert' aria-label='Close'>\n" +
        "                    <span aria-hidden='true'>×</span>\n" +
        "                </button>\n" +
        "                <i class='material-icons'>" + icon + "</i>\n" + message +
        "</div>");
    setTimeout(function () {
        $("#alert > div").addClass("show")
    }, 10);
    if (time !== -1)
        setTimeout(function () {
            dismiss(current);
        }, time)
}

function dismiss(id) {
    $("#alertCount-" + id).click();
}

function dismissAll() {
    $("#alert > div > button").click();
}

function generateTables(jURL, table, container = 'table') {
    console.log("gentable: " + table);
    $.get(jURL, function (data, status) {
        if (status !== "success") return;
        jsonTable = data;
        var col = [];
        for (var key in jsonTable[0]) {
            if (col.indexOf(key) === -1) {
                col.push({
                    field: key.toString(),
                    title: null,
                    sortable: true
                });
            }
        }
        $.ajax({
            type: "POST",
            url: "/api/i18n/tableCol/" + table,
            dataType: 'json',
            headers: {'X-CSRF-TOKEN': $("meta[name='_csrf']").attr("content")},
            async: false,
            contentType: "application/json",
            data: JSON.stringify(col),
            success: function (data) {
                data.splice(0, 0, {checkbox: true});
                $('#' + container).bootstrapTable({
                    locale: 'zh-CN',
                    pagination: true,
                    search: true,
                    silentSort: false,
                    maintainSelected: true,
                    clickToSelect: true,
                    columns: data,
                    data: jsonTable,
                    theadClasses: 'bg-light',
                    classes: 'table mb-0',
                    checkboxHeader: false,

                });

                //modify table checkbox
                // let cbList = $("#" + container + " > tbody > tr >td.bs-checkbox");
                // for (let i = 0; i < cbList.length; i++) {
                //     cbList[i].innerHTML = "";
                //     cbList[i].innerHTML = ("<div class='custom-control custom-checkbox'>" +
                //         "<input type='checkbox' class='custom-control-input' name='btSelectItem' data-index='" + i + "'>" +
                //         "<label class=\"custom-control-label\"></label></div>")
                // }
            }
        });
    });
}

function zzz_test(tablename) {
    console.log(tablename);
    $.ajax({
        type: "post",
        async: false,
        url: "/api/sample/" + tablename,
        dataType: "json",

        success: function (data) {
            console.log(data);

            for (key in data) {
                console.log(key);
                if ($("[name='key']").val() != null) {
                    data[key] = $("[name=key]").val();
                }
                console.log(data[key]);
            }
            console.log(data);

        }
    });
}

function goBack() {
    history.back(-1);
}

function init() {
    initPopper();
}

function initPopper() {
    var t = {duration: 270, easing: "easeOutSine"};
    $(":not(.main-sidebar--icons-only) .dropdown").on("show.bs.dropdown", function () {
        $(this).find(".dropdown-menu").first().stop(!0, !0).slideDown(t)
    }), $(":not(.main-sidebar--icons-only) .dropdown").on("hide.bs.dropdown", function () {
        $(this).find(".dropdown-menu").first().stop(!0, !0).slideUp(t)
    }), $(".toggle-sidebar").click(function (t) {
        $(".main-sidebar").toggleClass("open")
    })
}

function sleep(time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}