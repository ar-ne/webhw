function generateTables(jsonStr, container) {
    var jsonTable = (jsonStr);
    var col = [];
    var translate =[];
    col.push({checkbox: true});
    for (var key in jsonTable[0]){
        translate.push({
            "key":key.toString(),
            "trains":null
        });
    }

    for (var key in jsonTable[0]) {
        if (col.indexOf(key) === -1) {
            col.push({
                field: key.toString(),
                title: key.toString()+"TITLE",
                sortable: true
            });
        }
    }
    $(container).bootstrapTable({
        pagination: true,
        search: true,
        silentSort: false,
        maintainSelected: true,
        clickToSelect: true,
        columns: col,
        data: jsonTable
    })
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