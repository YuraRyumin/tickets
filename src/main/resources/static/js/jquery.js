$(document).on("change", "#stationLast", function() {
    var selectFirst = document.getElementById("stationFirst");
    if(selectFirst == null){
        var stationFirst = "";
    } else {
        var stationFirst = selectFirst.options[selectFirst.selectedIndex].value;
    }
    var selectLast = document.getElementById("stationLast");
    if(selectLast == null){
        var stationLast = "";
    } else {
        var stationLast = selectLast.options[selectLast.selectedIndex].value;
    }
    if(stationFirst == stationLast){
        $("#tableTrains").html("First station equals last station. Change your choice.");
    }
    else if(stationFirst == "" || stationLast == ""){
        $("#tableTrains").html("");
    } else {
        $.get('/getTableTickets',
            {
                stationFirst: stationFirst,
                stationLast: stationLast
            }).done(
            function (data) {
                console.log(data);
                if (data != null) {
                    let table = "<table id='route_table' border = '1'>" +
                        "<thead><tr>" +
                        "<th>Choosen</th>" +
                        "<th>Departure station</th>" +
                        "<th>Arrival station</th>" +
                        "<th>Departure time</th>" +
                        "<th>Arrival time</th>" +
                        "</tr></thead><tbody>";
                    for (i = 0; i < data.length; i++) {
                        table = table + "<tr>" +
                            "<td><input name=\"radioStation\" type=\"radio\" id=\"radioStation\"></td>" +
                            "<td>" + data[i].stationFirst + "</td>" +
                            "<td>" + data[i].stationLast + "</td>" +
                            "<td>" + data[i].timeDeparture + "</td>" +
                            "<td>" + data[i].timeArrival + "</td>" +
                            "</tr>";
                    }

                    table = table + "</tbody></table>";
                    $("#tableTrains").html(table);
                }
            });
    }
})

$(document).on("change", "#stationFirst", function() {
    var selectFirst = document.getElementById("stationFirst");
    if(selectFirst == null){
        var stationFirst = "";
    } else {
        var stationFirst = selectFirst.options[selectFirst.selectedIndex].value;
    }
    var selectLast = document.getElementById("stationLast");
    if(selectLast == null){
        var stationLast = "";
    } else {
        var stationLast = selectLast.options[selectLast.selectedIndex].value;
    }
    if(stationFirst == stationLast){
        $("#tableTrains").html("First station equals last station. Change your choice.");
    }
    else if(stationFirst == "" || stationLast == ""){
        $("#tableTrains").html("");
    } else {
        $.get('/getTableTickets',
            {
                stationFirst: stationFirst,
                stationLast: stationLast
            }).done(
            function (data) {
                console.log(data);
                if (data != null) {
                    let table = "<table id='route_table' border = '1'>" +
                        "<thead><tr>" +
                        "<th>Choosen</th>" +
                        "<th>Departure station</th>" +
                        "<th>Arrival station</th>" +
                        "<th>Departure time</th>" +
                        "<th>Arrival time</th>" +
                        "</tr></thead><tbody>";
                    for (i = 0; i < data.length; i++) {
                        table = table + "<tr>" +
                            "<td><input name=\"radioStation\" type=\"radio\" id=\"radioStation\"></td>" +
                            "<td>" + data[i].stationFirst + "</td>" +
                            "<td>" + data[i].stationLast + "</td>" +
                            "<td>" + data[i].timeDeparture + "</td>" +
                            "<td>" + data[i].timeArrival + "</td>" +
                            "</tr>";
                    }

                    table = table + "</tbody></table> " +
                        "<script type='text/javascript'>highlight_Table_Rows('route_table', 'hover_Row', 'clicked_Row');</script>";
                    $("#tableTrains").html(table);
                }
            });
    }
})

