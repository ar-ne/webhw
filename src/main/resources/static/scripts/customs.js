function formSubmit(form = 'form', button = 'submit', callback = null) {
    form = $('#' + form);
    button = $('#' + button);
    $.ajax({
        type: form.attr("method"),
        url: form.attr("action"),
        data: form.serialize(),
        dataType: "xhr",
        async: true,
        cache: false,
        complete: function (xhr, textStatus) {
            console.log(xhr);
            console.log(xhr.status);
            if (xhr.status === 403) showFailAlert();
            else if (xhr.status === 201) Turbolinks.visit(xhr.responseText, {action: "replace"});
            else if (xhr.status === 200) Turbolinks.visit(xhr.responseText);
        }
    });
    return false;
}

function showFailAlert() {
    alert("失败/错误")
}

function generateTables(jURL, table, container = 'table') {
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
            contentType: "application/json",
            data: JSON.stringify(col),
            success: function (data) {
                data.splice(0, 0, {checkbox: true});
                $(container).bootstrapTable({
                    locale: 'zh-CN',
                    pagination: true,
                    search: true,
                    silentSort: false,
                    maintainSelected: true,
                    clickToSelect: true,
                    columns: data,
                    data: jsonTable
                })
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