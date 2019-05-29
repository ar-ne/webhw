function generateTables(jURL,table, container='table') {
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

function goBack() {
    history.back(-1);
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