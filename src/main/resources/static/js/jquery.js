$(document).on("change", "#stationLast", ReturnTicketsTableByStations)

$(document).on("change", "#stationFirst", ReturnTicketsTableByStations)

function ReturnTicketsTableByStations() {
    var checkRun = document.querySelector('input[name="radioRun"]:checked').value;
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
        $("#tableTrains").html("<h1>First station equals last station. Change your choice.</h1>");
        $("#tableTickets").html("");
        $("#tableTicketsTwo").html("");
    }
    else if(stationFirst == "" || stationLast == ""){
        $("#tableTrains").html("");
        $("#tableTickets").html("");
        $("#tableTicketsTwo").html("");
    } else {
        if(checkRun == "direct") {
            $.get('/getTableTickets',
                {
                    stationFirst: stationFirst,
                    stationLast: stationLast
                }).done(
                function (data) {
                    console.log(data);
                    if (data != null) {
                        //if(data.length = 0){
                        //    $("#tableTrains").html("No trains, sorry...");
                        //}else {
                        let table = "<table class='tableOnDiv' id='route_table' border = '1'>" +
                            "<thead><tr>" +
                            "<th>Departure station</th>" +
                            "<th>Arrival station</th>" +
                            "<th>Departure time</th>" +
                            "<th>Arrival time</th>" +
                            "</tr></thead><tbody>";
                        for (i = 0; i < data.length; i++) {
                            table = table + "<tr>" +
                                "<td class='stationFirstT'>" + data[i].stationFirst + "</td>" +
                                "<td class='stationLastT'>" + data[i].stationLast + "</td>" +
                                "<td class='timeDepartureT'>" + data[i].timeDeparture + "</td>" +
                                "<td class='timeArrivalT'>" + data[i].timeArrival + "</td>" +
                                "</tr>";
                        }

                        table = table + "</tbody></table> " +
                            "<script type='text/javascript'>highlight_Table_Rows('route_table', 'hover_Row', 'clicked_Row');</script>";
                        $("#tableTrains").html(table);
                        $("#tableTickets").html("");
                        $("#tableTicketsTwo").html("");
                        $("#tableTicketsPassangerInfo").html("");
                        $("#tableTicketsOneTrain").html("");
                        $("#tableTicketsPassangerInfoOneTrain").html("");
                    }
                });
        } else {
            $.get('/getTableTicketsTransfer',
                {
                    stationFirst: stationFirst,
                    stationLast: stationLast
                }).done(
                function (data) {
                    console.log(data);
                    if (data != null) {
                        //if(data.length = 0){
                        //    $("#tableTrains").html("No trains, sorry...");
                        //}else {
                        let table = "<table class='tableOnDiv' id='two_routes_table' border = '1'>" +
                            "<thead><tr>" +
                            "<th>Departure time (1 train)</th>" +
                            "<th>Departure station  (1 train)</th>" +
                            "<th>Arrival station  (1 train)</th>" +
                            "<th>Arrival time  (1 train)</th>" +
                            "<th>Departure time (2 train)</th>" +
                            "<th>Departure station  (2 train)</th>" +
                            "<th>Arrival station  (2 train)</th>" +
                            "<th>Arrival time  (2 train)</th>" +
                            "</tr></thead><tbody>";
                        for (i = 0; i < data.length; i++) {
                            table = table + "<tr>" +
                                "<td>" + data[i].timeBeginningFirstTrain + "</td>" +
                                "<td>" + data[i].nameFirstStationFirstTrain + "</td>" +
                                "<td>" + data[i].nameSecondStationFirstTrain + "</td>" +
                                "<td>" + data[i].timeEndFirstTrain + "</td>" +
                                "<td>" + data[i].timeBeginningSecondTrain + "</td>" +
                                "<td>" + data[i].nameFirstStationSecondTrain + "</td>" +
                                "<td>" + data[i].nameSecondStationSecondTrain + "</td>" +
                                "<td>" + data[i].timeEndSecondTrain + "</td>" +
                                "</tr>";
                        }

                        table = table + "</tbody></table> " +
                            "<script type='text/javascript'>highlight_Table_Rows('two_routes_table', 'hover_Row', 'clicked_Row');</script>";
                        $("#tableTrains").html(table);
                        $("#tableTickets").html("");
                        $("#tableTicketsTwo").html("");
                        $("#tableTicketsPassangerInfo").html("");
                        $("#tableTicketsOneTrain").html("");
                        $("#tableTicketsPassangerInfoOneTrain").html("");
                        //}
                    }
                });
        }
    }
}


