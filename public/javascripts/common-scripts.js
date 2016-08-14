function cloneFunction(tableName, cloneTableName, xlsName, removeLastFlag) {
    var source = document.getElementById(tableName);
    var destination = document.getElementById(cloneTableName);
    var copy = source.cloneNode(true);
    copy.setAttribute('id' , 'clonedTable');
    destination.parentNode.replaceChild(copy , destination);
    $("#clonedTable .ng-hide").remove();
    if(removeLastFlag) {
        $('#clonedTable tr').find('th:last-child, td:last-child').remove();
    }
    tableToExcel('clonedTable', 'name', xlsName);
}

var tableToExcel = (function () {
    var uri = 'data:application/vnd.ms-excel;base64,'
    , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta charset="UTF-8"></head><body><table>{table}</table></body></html>'
    , base64 = function (s) { return window.btoa(unescape(encodeURIComponent(s))) }
    , format = function (s, c) { return s.replace(/{(\w+)}/g, function (m, p) { return c[p]; }) }
         return function (table, name, filename) {
        if (!table.nodeType) table = document.getElementById(table)
        var ctx = { worksheet: name || 'Worksheet', table: table.innerHTML }
        document.getElementById("dlink").href = uri + base64(format(template, ctx));
        document.getElementById("dlink").download = filename;
        document.getElementById("dlink").click();
    }
})()

$(document).ready(function() {
    $(".credit-card").each(function() {
        $(this).mask("99999999999999?99");
    });
    $(".phone").each(function() {
        $(this).mask("9999999999");
    });
    $(".zip").each(function() {
        $(this).mask("99999");
    });
});

function checkDate(month, year) {
    var currentDate = new Date();
    var thisMonth = currentDate.getMonth() + 1;
    var thisYear = currentDate.getFullYear();
    if(year == thisYear && month <= thisMonth) {
        return false;
    }
    return true;
}



