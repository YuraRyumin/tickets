$(document).on("change", "#stationLast", ReturnListOfTicketsByStations)//ReturnTicketsTableByStations)

$(document).on("change", "#stationFirst", ReturnListOfTicketsByStations)

$(document).on("change", "#dateTicket", ReturnListOfTicketsByStations)//ReturnTicketsTableByStations)

//$(document).on("change", "#direct", ReturnTicketsTableByStations)

//$(document).on("change", "#transfer", ReturnTicketsTableByStations)

function ReturnListOfTicketsByStations() {
    var selectFirst = document.getElementById("stationFirst");
    var dateTicket = document.getElementById("dateTicket").value;
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
        $.get('/getGraph',
            {
                strStationFirst: stationFirst,
                strStationLast: stationLast
            }).done(
            function (data) {
                if (data != null) {
                    let table = "<h5><a data-toggle='collapse' href='#divForGraph' aria-expanded='false' aria-controls='divForGraph'>Tickets:</a></h5>" +
                        "<div class='collapse' id='divForGraph'>" +
                        "<table class='tableOnDiv' id='div_table' border = '1'>" +
                        "<tbody>";
                    for (i = 0; i < data.length; i++) {
                        var route = data[i].oneTrainTripDTOS;
                        table += "<tr><td><div class='divMainForTrains' id='divMainForTrains" + i + "'>"
                        for (j = 0; j < route.length; j++) {
                            console.log(route[j]);
                            table += //"<div id='divForTrains'>" +
                                "<div class='divForTrainsEmpty' id='divTicketEmpty" + j + "'>&nbsp;&nbsp;&nbsp;</div><div class='divForTrains' id='divTicket" + j + "'>" +
                                "<div class='field'>" +
                                "     <label>Schedule:</label>" +
                                "     <input class='js-select4' type='text' id='scheduleTicket" + j + "' readonly name='scheduleTicket" + j + "' value=" + route[j].schedule + ">" +
                                "</div>" +
                                "<div class='field'>" +
                                "     <label>Station first:</label>" +
                                "     <input class='js-select4' type='text' id='stationFirstTicket" + j + "' readonly name='stationFirstTicket" + j + "' value=" + route[j].stationFirst.replace(' ', '_') + ">" +
                                "</div>" +
                                "<div class='field'>" +
                                "     <label>Time departure:</label>" +
                                "     <input class='js-select4' type='text' id='timeDepartureTicket" + j + "' readonly name='timeDepartureTicket" + j + "' value=" + route[j].timeDeparture + ">" +
                                "</div>" +
                                "<div class='field'>" +
                                "     <label>Station last:</label>" +
                                "     <input class='js-select4' type='text' id='stationLastTicket" + j + "' readonly name='stationLastTicket" + j + "' value=" + route[j].stationLast.replace(' ', '_') + ">" +
                                "</div>" +
                                "<div class='field'>" +
                                "     <label>Time arrival:</label>" +
                                "     <input class='js-select4' type='text' id='timeArrivalTicket" + j + "' readonly name='timeArrivalTicket" + j + "' value=" + route[j].timeArrival + ">" +
                                "</div>" +
                                "<div class='field'>" +
                                "     <label>Train:</label>" +
                                "     <input class='js-select4' type='text' id='trainTicket" + j + "' readonly name='trainTicket" + j + "' value=" + route[j].train + ">" +
                                "</div>" +
                                "</div>";
                        }
                        table += "</div></td></tr></div>";
                    }
                    table += "</tbody></table><script type='text/javascript'>highlight_Table_Rows('div_table', 'hover_Row', 'clicked_Row');</script>";
                    //console.log(table);
                    $("#divForTwoTrains").html(table);
                    $("#tableTrains").html("");
                    $("#divForGraph").collapse("show");
                }
            });
    }

}

function ReturnTicketsTableByStations() {
    var checkRun = document.querySelector('input[name="radioRun"]:checked').value;
    var selectFirst = document.getElementById("stationFirst");
    var dateTicket = document.getElementById("dateTicket").value;
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
                    stationLast: stationLast,
                    dateTicket: dateTicket
                }).done(
                function (data) {
                    if (data != null) {
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
                    stationLast: stationLast,
                    dateTicket: dateTicket
                }).done(
                function (data) {
                    if (data != null) {
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
                    }
                });
        }
    }
}


