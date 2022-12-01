function tableOnClick(table_Id) {
    var table = document.getElementById(table_Id);
    if (click_Class) table.onclick = function(e) {
        let ticketsInfo1 = "<h1>Test</h1>";
        $("#tableTickets").html(ticketsInfo1);
        if (!e) e = window.event;
        var elem = e.target || e.srcElement;
        while (!elem.tagName || !elem.tagName.match(/td|th|table/i))
            elem = elem.parentNode;

        if (elem.parentNode.tagName == 'TR' &&
            elem.parentNode.parentNode.tagName == 'TBODY') {
            var stationFirst = $.map(elem.parentNode.siblings(), function(el) {
                    return $(el).text();
                });
            var stationLast = stationFirst;

            $.get('/getTicketsInfo',
                {
                    stationFirst: stationFirst,
                    stationLast: stationLast
                }).done(
                function (data) {
                    console.log(data);
                    if (data != null) {
                        let ticketsInfo = "<h1>" + data + "</h1>";
                        $("#tableTickets").html(ticketsInfo);
                    }
                });
        }
    }
}