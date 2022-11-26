
$(document).on("change", "#stationLast", function (){
    var selectFirst = document.getElementById("stationFirst");
    var stationFirst = selectFirst.options[selectFirst.selectedIndex].value;
    var selectLast = document.getElementById("stationLast");
    var stationLast = selectLast.options[selectLast.selectedIndex].value;
    $.get('/getTableTickets',
        {stationFirst: stationFirst,
        stationLast: stationLast}).done(
        function (data) {
        console.log(data);
        if (data != null) {
            let table = "<table border = '1'>" +
                "<thead><tr>" +
                "<th>Choosen</th>" +
                "<th>Departure station</th>" +
                "<th>Arrival station</th>" +
                "<th>Departure time</th>" +
                "<th>Arrival time</th>" +
                "</tr></thead><tbody>";
                for(i = 0; i < data.length; i++) {
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
})
