$(document).on("change", "#stationLast", ReturnListOfTicketsByStations)

$(document).on("change", "#stationFirst", ReturnListOfTicketsByStations)

$(document).on("change", "#dateTicket", ReturnListOfTicketsByStations)

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
    console.log(stationFirst + "!" + stationLast);
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
                strStationLast: stationLast,
                dateTicket: dateTicket
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
                                "<div class='field'>" +
                                "     <label>Distance:</label>" +
                                "     <input class='js-select4' type='text' id='distanceTicket" + j + "' readonly name='distanceTicket" + j + "' value=" + route[j].distance + ">" +
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



