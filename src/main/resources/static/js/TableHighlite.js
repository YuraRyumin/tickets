$(document).on("change", "#wagonFirstTicket", ReturnSeatsByWagon)

function highlight_Table_Rows(table_Id, hover_Class, click_Class, multiple) {
    var table = document.getElementById(table_Id);
    if (typeof multiple == 'undefined') multiple = true;

    if (hover_Class) {
        var hover_Class_Reg = new RegExp("\\b"+hover_Class+"\\b");
        table.onmouseover = table.onmouseout = function(e) {
            if (!e) e = window.event;
            var elem = e.target || e.srcElement;
            while (!elem.tagName || !elem.tagName.match(/td|th|table/i))
                elem = elem.parentNode;

            if (elem.parentNode.tagName == 'TR' &&
                elem.parentNode.parentNode.tagName == 'TBODY') {
                var row = elem.parentNode;
                if (!row.getAttribute('clicked_Row'))
                    row.className = e.type=="mouseover"?row.className +
                        " " + hover_Class:row.className.replace(hover_Class_Reg," ");
            }
        };
    }

    if (click_Class) table.onclick = function(e) {
        //let ticketsInfo1 = "<h1>Test</h1>";
        //$("#tableTickets").html("<h1>" + elem + "</h1>");

        if (!e) e = window.event;
        var elem = e.target || e.srcElement;
        while (!elem.tagName || !elem.tagName.match(/td|th|table/i))
            elem = elem.parentNode;


        if (elem.parentNode.tagName == 'TR' &&
            elem.parentNode.parentNode.tagName == 'TBODY') {
            if(table_Id == "route_table") {
                SetDivOneTicket(table_Id, elem);
            } else {
                SetDivTwoTicket(table_Id, elem);
            }
        }
    };
}

function SetDivOneTicket(table_Id, elem) {
    var stationFirst = elem.parentNode.childNodes[0].textContent;
    var stationLast = elem.parentNode.childNodes[1].textContent;
    var timeDeparture = elem.parentNode.childNodes[2].textContent;
    var timeArrival = elem.parentNode.childNodes[3].textContent;

    var txtDate = document.getElementById("dateTicket").getAttribute("value");
    console.log(txtDate);

    $.get('/getTicketsInfo',
        {
            stationFirst: stationFirst,
            stationLast: stationLast,
            timeDeparture: timeDeparture,
            timeArrival: timeArrival
        }).done(
        function (data) {
            console.log(data);
            if (data != null) {
                let ticketsInfo = "<table id='ticket_table' border = '1'>" +
                    "<tbody>";
                let tablePassenger = "<table id='passenger_table'>" +
                    "<tbody>";
                for (i = 0; i < data.length; i++) {
                    let opositeGender = (data[i].passengerGender = "MALE") ? "<option value='FEMALE'>FEMALE</option>" : "<option value='FEMALE'>MALE</option>";

                    tablePassenger = tablePassenger +
                        "<tr><td>Passenger name</td><td><input type='text' id='passengerName' name='passengerName' value=" + data[i].passengerName + "></td></tr>" +
                        "<tr><td>Passenger surname</td><td><input type='text' id='passengerSurname' name='passengerSurname' value=" + data[i].passengerSurname + "></td></tr>" +
                        "<tr><td>Passenger gender</td><td><select class='js-select1' id='passengerGender' name='passengerGender' placeholder='Choose gender'>" +
                        "<option value=" + data[i].passengerGender + ">" + data[i].passengerGender + "</option>" +
                        opositeGender +
                        "</select></td></tr>" +
                        "<tr><td>Passenger passport</td><td><input type='text' id='passengerPassport' name='passengerPassport' value=" + data[i].passengerPassport + "></td></tr>";
                    console.log(data[i].stationLast);
                    ticketsInfo = ticketsInfo +
                        "<tr><td>Station first</td><td><input type='text' id='stationFirstFirstTicket' name='stationFirstFirstTicket' readonly value=" + data[i].stationFirst.replace(' ', '_') + "></td></tr>" +
                        "<tr><td>Time departure</td><td><input type='text' id='timeDepartureFirstTicket' name='timeDepartureFirstTicket' readonly value=" + data[i].timeDeparture + "></td></tr>" +
                        "<tr><td>Station last</td><td><input type='text' id='stationLastFirstTicket' name='stationLastFirstTicket' readonly value=" + data[i].stationLast.replace(' ', '_') + "></td></tr>" +
                        "<tr><td>Time arrival</td><td><input type='text' id='timeArrivalFirstTicket' name='timeArrivalFirstTicket' readonly value=" + data[i].timeArrival + "></td></tr>" +
                        "<tr><td>Train</td><td><input type='text' id='trainFirstTicket' name='trainFirstTicket' readonly value=" + data[i].trainNumber + "></td></tr>" +
                        "<tr><td>Schedule</td><td><input type='text' id='scheduleFirstTicket' name='scheduleFirstTicket' readonly value=" + data[i].schedule + "></td></tr>" +
                        "<tr><td>Wagon</td><td>" + showWagonList(data[i].trainId, "FirstTicket") + "</td></tr>" +
                        "<tr><td>Seat</td><td><div id='divForSeats'>" + showSeatsList(8, 1, "2022-06-11", "FirstTicket") + "</div></td></tr>";
                }
                tablePassenger = tablePassenger + "</tbody></table> <div align='center'><button type='submit'>Confirm</button></div>";
                ticketsInfo = ticketsInfo + "</tbody></table>";
                $("#tableTicketsOneTrain").html(ticketsInfo);
                $("#tableTicketsPassangerInfoOneTrain").html(tablePassenger);
                $("#tableTicketsPassangerInfo").html("");
                $("#tableTickets").html("");
                $("#tableTicketsTwo").html("");
            }
        });
}

function SetDivTwoTicket(table_Id, elem) {
    var timeBeginningFirstTrain = elem.parentNode.childNodes[0].textContent;
    var nameFirstStationFirstTrain = elem.parentNode.childNodes[1].textContent;
    var nameSecondStationFirstTrain = elem.parentNode.childNodes[2].textContent;
    var timeEndFirstTrain = elem.parentNode.childNodes[3].textContent;

    var timeBeginningSecondTrain = elem.parentNode.childNodes[4].textContent;
    var nameFirstStationSecondTrain = elem.parentNode.childNodes[5].textContent;
    var nameSecondStationSecondTrain = elem.parentNode.childNodes[6].textContent;
    var timeEndSecondTrain = elem.parentNode.childNodes[7].textContent;

    $.get('/getTicketsInfo',
        {
            stationFirst: nameFirstStationFirstTrain,
            stationLast: nameSecondStationFirstTrain,
            timeDeparture: timeBeginningFirstTrain,
            timeArrival: timeEndFirstTrain
        }).done(
        function (data) {
            console.log(data);
            if (data != null) {
                let ticketsInfo = "<table id='ticket_table' border = '1'>" +
                    "<tbody>";
                for (i = 0; i < data.length; i++) {
                    let opositeGender = (data[i].passengerGender = "MALE") ? "<option value='FEMALE'>FEMALE</option>" : "<option value='FEMALE'>MALE</option>";
                    ticketsInfo = ticketsInfo +
                        "<tr><td>Station first</td><td><input type='text' id='stationFirstFirstTicket' name='stationFirstFirstTicket' readonly value=" + data[i].stationFirst.replace(' ', '_') + "></td></tr>" +
                        "<tr><td>Time departure</td><td><input type='text' id='timeDepartureFirstTicket' name='timeDepartureFirstTicket' readonly value=" + data[i].timeDeparture + "></td></tr>" +
                        "<tr><td>Station last</td><td><input type='text' id='stationLastFirstTicket' name='stationLastFirstTicket' readonly value=" + data[i].stationLast.replace(' ', '_') + "></td></tr>" +
                        "<tr><td>Time arrival</td><td><input type='text' id='timeArrivalFirstTicket' name='timeArrivalFirstTicket' readonly value=" + data[i].timeArrival + "></td></tr>" +
                        "<tr><td>Train</td><td><input type='text' id='trainFirstTicket' name='trainFirstTicket' readonly value=" + data[i].trainNumber + "></td></tr>" +
                        "<tr><td>Schedule</td><td><input type='text' id='scheduleFirstTicket' name='scheduleFirstTicket' readonly value=" + data[i].schedule + "></td></tr>" +
                        "<tr><td>Wagon</td><td>" + showWagonList(data[i].trainId, "FirstTicket") + "</td></tr>" +
                        "<tr><td>Seat</td><td>" + showSeatsList('07:41', 1, "2022-06-11", "FirstTicket") + "</td></tr>";
                }

                ticketsInfo = ticketsInfo + "</tbody></table><br>";
                $("#tableTickets").html(ticketsInfo);
            }
        });
    $.get('/getTicketsInfo',
        {
            stationFirst: nameFirstStationSecondTrain,
            stationLast: nameSecondStationSecondTrain,
            timeDeparture: timeBeginningSecondTrain,
            timeArrival: timeEndSecondTrain
        }).done(
        function (data) {
            console.log(data);
            if (data != null) {
                let ticketsInfo = "<table id='ticket_table' border = '1'>" +
                    "<tbody>";
                let tablePassenger = "<table id='passenger_table'>" +
                    "<tbody>";
                for (i = 0; i < data.length; i++) {
                    let textResult = "";
                    let opositeGender = (data[i].passengerGender = "MALE") ? "<option value='FEMALE'>FEMALE</option>" : "<option value='FEMALE'>MALE</option>";

                    tablePassenger = tablePassenger +
                        "<tr><td>Passenger name</td><td><input type='text' id='passengerName' name='passengerName' value=" + data[i].passengerName + "></td></tr>" +
                        "<tr><td>Passenger surname</td><td><input type='text' id='passengerSurname' name='passengerSurname' value=" + data[i].passengerSurname + "></td></tr>" +
                        "<tr><td>Passenger gender</td><td><select class='js-select1' id='passengerGender' name='passengerGender' placeholder='Choose gender'>" +
                        "<option value=" + data[i].passengerGender + ">" + data[i].passengerGender + "</option>" +
                        opositeGender +
                        "</select></td></tr>" +
                        "<tr><td>Passenger passport</td><td><input type='text' id='passengerPassport' name='passengerPassport' value=" + data[i].passengerPassport + "></td></tr>";

                    ticketsInfo = ticketsInfo +
                        "<tr><td>Station first</td><td><input type='text' id='stationFirstSecondTicket' name='stationFirstSecondTicket' readonly value=" + data[i].stationFirst.replace(' ', '_') + "></td></tr>" +
                        "<tr><td>Time departure</td><td><input type='text' id='timeDepartureSecondTicket' name='timeDepartureSecondTicket' readonly value=" + data[i].timeDeparture + "></td></tr>" +
                        "<tr><td>Station last</td><td><input type='text' id='stationLastSecondTicket' name='stationLastSecondTicket' readonly value=" + data[i].stationLast.replace(' ', '_') + "></td></tr>" +
                        "<tr><td>Time arrival</td><td><input type='text' id='timeArrivalSecondTicket' name='timeArrivalSecondTicket' readonly value=" + data[i].timeArrival + "></td></tr>" +
                        "<tr><td>Train</td><td><input type='text' id='trainSecondTicket' name='trainSecondTicket' readonly value=" + data[i].trainNumber + "></td></tr>" +
                        "<tr><td>Schedule</td><td><input type='text' id='scheduleSecondTicket' name='scheduleSecondTicket' readonly value=" + data[i].schedule + "></td></tr>" +
                        "<tr><td>Wagon</td><td>" + showWagonList(data[i].trainId, "SecondTicket") + "</td></tr>" +
                        "<tr><td>Seat</td><td>" + showSeatsList(8, 1, "2022-06-11", "SecondTicket") + "</td></tr>";
                }
                tablePassenger = tablePassenger + "</tbody></table> <div align='center'><button type='submit'>Confirm</button></div>";
                ticketsInfo = ticketsInfo + "</tbody></table>";
                $("#tableTicketsTwo").html(ticketsInfo);
                $("#tableTicketsPassangerInfo").html(tablePassenger);
                $("#tableTicketsOneTrain").html("");
                $("#tableTicketsPassangerInfoOneTrain").html("");
            }
        });
}

function showWagonList(trainId, runNumber){
    var request = new XMLHttpRequest();
    request.open('GET', '/getWagons?trainId=' + trainId, false);
    request.send(null);
    if (request.status === 200) {
        var dataR = JSON.parse(request.response);
        console.log(dataR);

        if (dataR != null) {
            let wagonsInfo = "<select class='js-select1' id='wagon" + runNumber + "' name='wagon" + runNumber + "' placeholder='Choose wagon'>";
            for (i = 0; i < dataR.length; i++) {
                wagonsInfo = wagonsInfo + "<option value=" + dataR[i].id + ">" + dataR[i].name + "</option>";
            }
            wagonsInfo = wagonsInfo + "</select>";
            console.log(wagonsInfo);
            return wagonsInfo;
        }
    }
}

function showSeatsList(scheduleId, wagonId, dateTicket, runNumber) {
    var request = new XMLHttpRequest();
    console.log("/getSeats?schedule='" + scheduleId + "'&wagonId=" + wagonId + "&dateTicket='" + dateTicket + "'");
    request.open('GET', "/getSeats?schedule=" + scheduleId + "&wagonId=" + wagonId + "&dateTicket=" + dateTicket + "", false);
    request.send(null);
    if (request.status === 200) {
        var dataR = JSON.parse(request.response);
        console.log(dataR);
        let seatsInfo = "";
        if (dataR != null && dataR.length != 0) {
            const array = Array(dataR[0].maxSeats);
            seatsInfo = "<select class='js-select1' id='seats" + runNumber + "' name='seats" + runNumber + "' placeholder='Choose seat'>";
            for (i = 1; i <= dataR[0].maxSeats; i++) {
                let wasInResponse = false;
                for (j = 0; j < dataR.length; j++) {
                    if (dataR[j].seat == i) {
                        wasInResponse = true;
                    }
                }
                if (!wasInResponse) {
                    seatsInfo = seatsInfo + "<option value=" + i + ">" + i + "</option>";
                }
            }
            seatsInfo = seatsInfo + "</select>";
        }
        return seatsInfo;
    }
}

function ReturnSeatsByWagon() {
    var txtDate = document.getElementById("dateTicket").getAttribute("value");
    console.log(document.getElementById("dateTicket"));
    var objWagon = document.getElementById("wagonFirstTicket");
    if(objWagon == null){
        var txtWagon = "";
    } else {
        var txtWagon = objWagon.options[objWagon.selectedIndex].value;
    }
    var txtSchedule = document.getElementById("scheduleFirstTicket").getAttribute("value");
    console.log(txtDate + "; " + txtWagon + "; " + txtSchedule)

    $("#divForSeats").html(showSeatsList(txtSchedule, txtWagon, txtDate, "FirstTicket"));
}
