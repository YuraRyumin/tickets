$(document).on("change", "#dateTicket", getDataAndSetTable)
$(document).on("change", "#trainTickets", getDataAndSetTable)
$(document).on("change", "#scheduleTickets", getDataAndSetTable)

function getDataAndSetTable() {
    var selectTrain = document.getElementById("trainTickets");
    var dateTickets = document.getElementById("dateTicket").value;
    if(selectTrain == null){
        var txtTrain = "";
    } else {
        var txtTrain = selectTrain.options[selectTrain.selectedIndex].value;
    }
    var selectSchedule = document.getElementById("scheduleTickets");
    if(selectSchedule == null){
        var txtSchedule = "";
    } else {
        var txtSchedule = selectSchedule.options[selectSchedule.selectedIndex].value;
    }

    $.get('/getFiltredTicketsTable',
        {
            trainTickets: txtTrain,
            scheduleTickets: txtSchedule,
            dateTickets: dateTickets
        }).done(
        function (data) {
            if (data != null) {
                setTicketsTable(data);
            }
        });
}

function getDataAndSetFullTable() {
    $.get('/getFiltredTicketsTable',
        {
            trainTickets: '',
            scheduleTickets: '',
            dateTickets: ''
        }).done(
        function (data) {
            if (data != null) {
                setTicketsTable(data);
            }
        });
}

function setTicketsTable(data){
    let table = "<table class='tableOnDiv' id='ticket_table' border = '1'>" +
        "<thead>" +
        "   <tr>" +
        "       <th>Passenger</th>" +
        "       <th>Date of ticket</th>" +
        "       <th>Train</th>" +
        "       <th>Wagon</th>" +
        "       <th>Seat</th>" +
        "       <th>Price</th>" +
        "       <th>Schedule</th>" +
        "       <th>Change</th>" +
        "   </tr>" +
        "</thead>" +
        "<tbody>";
    for (i = 0; i < data.length; i++) {
        table += "<tr>" +
            "   <td>" + data[i].passenger + "</td>" +
            "   <td>" + data[i].dateTicket + "</td>" +
            "   <td>" + data[i].train + "</td>" +
            "   <td>" + data[i].wagon + "</td>" +
            "   <td>" + data[i].seat + "</td>" +
            "   <td>" + data[i].price + "</td>" +
            "   <td>" + data[i].schedule + "</td>" +
            "   <td><a  class='btn btn-outline-dark' href='/tickets/" + data[i].id + "'>edit</a></td>" +
            "</tr>";
    }
    table += "</tbody></table>" +
        "<a href='/tickets/new' class='btn btn-outline-dark'>Add new</a>";
    $("#divForTrainsTable").html(table);
}