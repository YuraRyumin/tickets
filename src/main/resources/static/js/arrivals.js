$(document).on("change", "#stations", ReturnStopsByStation)

function ReturnStopsByStation() {
    var selectStations = document.getElementById("stations");
    if(selectStations == null){
        var stationSelected = "";
    } else {
        var stationSelected = selectStations.options[selectStations.selectedIndex].value;
    }
    console.log(stationSelected);

    $.get('/getArrivalsInfo',
        {
            station: stationSelected
        }).done(
        function (data) {
            console.log(data);
            if (data != null) {
                let ticketsInfo = "<table class='tableOnDiv' id='ticket_table' border = '1'>" +
                    "<thead><tr>" +
                    "<th>Train</th>" +
                    "<th>Time arrival</th>" +
                    "<th>Time departure</th>" +
                    "</tr></thead><tbody>";
                for (i = 0; i < data.length; i++) {
                    ticketsInfo = ticketsInfo + "<tr>" +
                        "<td class='train'>" + data[i].train + "</td>" +
                        "<td class='timeArrivalT'>" + data[i].timeBegining + "</td>" +
                        "<td class='timeDepartureT'>" + data[i].timeEnd + "</td>" +
                        "</tr>";
                }
                ticketsInfo = ticketsInfo + "</tbody></table> ";
                $("#tableArrival").html(ticketsInfo);
            }
        });
}