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
    console.log(elem);
    var stationFirst = elem.parentNode.childNodes[0].textContent;
    var stationLast = elem.parentNode.childNodes[1].textContent;
    var timeDeparture = elem.parentNode.childNodes[2].textContent;
    var timeArrival = elem.parentNode.childNodes[3].textContent;

    var txtDate = document.getElementById("dateTicket").value;
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
                    var sched = data[i].schedule;
                    var train = data[i].trainId;
                    var schedName = data[i].trainNumber + "_-&gt;_" + data[i].schedule;
                    tablePassenger = tablePassenger +
                        "<tr><td>Passenger name</td><td><input type='text' id='passengerName' name='passengerName' value=" + data[i].passengerName + "></td></tr>" +
                        "<tr><td>Passenger surname</td><td><input type='text' id='passengerSurname' name='passengerSurname' value=" + data[i].passengerSurname + "></td></tr>" +
                        "<tr><td>Passenger DoB</td><td><input type='date' id='passengerDate' name='passengerDate' value=" + data[i].passengerDate + "></td></tr>" +
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
                        "<tr><td>Schedule</td><td><input type='text' id='scheduleFirstTicket' name='scheduleFirstTicket' readonly value=" + schedName + "></td></tr>" +
                        "<tr><td>Wagon</td><td>" + showWagonList(data[i].trainId, "FirstTicket") + "</td></tr>" +
                        "<tr><td>Seat</td><td><div id='divForSeats'>" + showSeatsList(sched, 1, txtDate, "FirstTicket", train) + "</div></td></tr>";
                }
                tablePassenger = tablePassenger + "</tbody></table> <div align='center'><br><br><button type='submit'>Confirm</button></div>";
                ticketsInfo = ticketsInfo + "</tbody></table>";
                $("#tableTicketsOneTrain").html(ticketsInfo);
                $("#tableTicketsPassangerInfoOneTrain").html(tablePassenger);
                $("#tableTicketsPassangerInfo").html("");
                $("#tableTickets").html("");
                $("#tableTicketsTwo").html("");
            }
        });
}

function getTableForTicket(stationFirst, stationLast, timeDeparture, timeArrival, numM, distance){
    var request = new XMLHttpRequest();
    var txtDate = document.getElementById("dateTicket").value;
    var params = 'stationFirst=' + encodeURIComponent(stationFirst.replace('_', ' ')) +
                '&stationLast=' + encodeURIComponent(stationLast.replace('_', ' ')) +
                '&timeDeparture=' + timeDeparture +
                '&timeArrival=' + timeArrival;
    request.open('GET', '/getTicketsInfo?' + params, false);
    request.send(null);
    let ticketsInfo = "";
    if (request.status === 200) {
        var dataR = JSON.parse(request.response);
        //console.log(dataR);
        if (dataR != null) {
            console.log(dataR);
            ticketsInfo += "<table id='ticket_table' border = '1'>" +
                "<tbody>";
            for (i = 0; i < dataR.length; i++) {
                var numForSeat = i;
                let opositeGender = (dataR[i].passengerGender = "MALE") ? "<option value='FEMALE'>FEMALE</option>" : "<option value='FEMALE'>MALE</option>";
                var sched = dataR[i].schedule;
                var train = dataR[i].trainId;
                var trainNumber = dataR[i].trainNumber;
                console.log(trainNumber);
                var schedName = dataR[i].trainNumber + "_-&gt;_" + dataR[i].schedule;
                ticketsInfo += "<tr><td>Station first</td><td><input type='text' id='stationFirstN" + numM + "' name='stationFirstN" + numM + "' readonly value=" + dataR[i].stationFirst.replace(' ', '_') + "></td></tr>" +
                    "<tr><td>Time departure</td><td><input type='text' id='timeDepartureN" + numM + "' name='timeDepartureN" + numM + "' readonly value=" + dataR[i].timeDeparture + "></td></tr>" +
                    "<tr><td>Station last</td><td><input type='text' id='stationLastN" + numM + "' name='stationLastN" + numM + "' readonly value=" + dataR[i].stationLast.replace(' ', '_') + "></td></tr>" +
                    "<tr><td>Time arrival</td><td><input type='text' id='timeArrivalN" + numM + "' name='timeArrivalN" + numM + "' readonly value=" + dataR[i].timeArrival + "></td></tr>" +
                    "<tr><td>Train</td><td><input type='text' id='trainN" + numM + "' name='trainN" + numM + "' readonly value=" + dataR[i].trainNumber + "></td></tr>" +
                    "<tr><td>Schedule</td><td><input type='text' id='scheduleN" + numM + "' name='scheduleN" + numM + "' readonly value=" + schedName + "></td></tr>" +
                    "<tr><td>Wagon</td><td>" + showWagonList(dataR[i].trainId, numM) + "</td></tr>" +
                    "<tr><td>Seat</td><td><div id='divForSeats" + numM + "'>" + showSeatsList(sched, 1, txtDate, numM, train) + "</div></td></tr>" +
                    "<tr><td>Distance</td><td><input type='text' id='distanceN" + numM + "' name='distanceN" + numM + "' readonly value=" + distance + "></td></tr>" +
                    "<tr><td>Price</td><td><input type='text' id='priceN" + numM + "' name='priceN" + numM + "' readonly value=" + showPrice(trainNumber, distance, 0) + "></td></tr>";
            }
            ticketsInfo += "</tbody></table><br>";
        }
        return ticketsInfo
    }
}

function SetDivTwoTicket(table_Id, elem) {
    if(table_Id == "div_table") {
        //console.log(elem);
        var tickets = [];
        var txtTable = "<div class='tableOnDiv' id='divForAllTickets'>";
        for (n = 0; n < elem.childNodes.length; n++) {
            for (m = 0; m < elem.childNodes[n].childNodes.length; m++) {
                if(elem.childNodes[n].childNodes[m].className == "divForTrainsEmpty"){
                    continue;
                }

                var txtSchedule = elem.childNodes[n].childNodes[m].childNodes[0].childNodes[3].value;
                var txtStationFirstTicket = elem.childNodes[n].childNodes[m].childNodes[1].childNodes[3].value;
                var txtTimeDepartureTicket = elem.childNodes[n].childNodes[m].childNodes[2].childNodes[3].value;
                var txtStationLastTicket = elem.childNodes[n].childNodes[m].childNodes[3].childNodes[3].value;
                var txtTimeArrivalTicket = elem.childNodes[n].childNodes[m].childNodes[4].childNodes[3].value;
                var txtTrainTicket = elem.childNodes[n].childNodes[m].childNodes[5].childNodes[3].value;
                var txtDistanceTicket = elem.childNodes[n].childNodes[m].childNodes[6].childNodes[3].value;

                tickets.push({schedule: txtSchedule,
                    stationFirst: txtStationFirstTicket,
                    timeDeparture: txtTimeDepartureTicket,
                    stationLast: txtStationLastTicket,
                    timeArrival: txtTimeArrivalTicket,
                    train: txtTrainTicket});

                // console.log(txtSchedule + "; "
                //     + txtStationFirstTicket + "; "
                //     + txtTimeDepartureTicket + "; "
                //     + txtStationLastTicket + "; "
                //     + txtTimeArrivalTicket + "; "
                //     + txtTrainTicket + "; ");

                txtTable += "<div class='divForTickets' id='divForAllTickets" + m + "'>" + getTableForTicket(txtStationFirstTicket,
                    txtStationLastTicket,
                    txtTimeDepartureTicket,
                    txtTimeArrivalTicket,
                    m,
                    txtDistanceTicket) + "</div>";

            }
        }
        txtTable += "</div>";
        $("#divForOneTrains").html(txtTable);
        $("#passengerInfo").collapse("show");
        $("#divForGraph").collapse("hide");

        return;
    }
}

function showWagonList(trainId, runNumber){
    var num = runNumber;
    var request = new XMLHttpRequest();
    request.open('GET', '/getWagons?trainId=' + trainId, false);
    request.send(null);
    if (request.status === 200) {
        var dataR = JSON.parse(request.response);

        if (dataR != null) {
            let wagonsInfo = "<select onchange='ReturnSeatsByWagonToElement(" + num + ")' class='js-select1' id='wagonN" + num + "' name='wagonN" + num + "' placeholder='Choose wagon'>";
            for (i = 0; i < dataR.length; i++) {
                wagonsInfo = wagonsInfo + "<option value=" + dataR[i].id + ">" + dataR[i].name + "</option>";
            }
            wagonsInfo = wagonsInfo + "</select>";
            return wagonsInfo;
        }
    }
}

function showSeatsList(scheduleId, wagonId, dateTicket, runNumber, trainId) {
    var num = runNumber;
    var request = new XMLHttpRequest();
    request.open('GET', "/getSeats?schedule=" + scheduleId + "&wagonId=" + wagonId + "&dateTicket=" + dateTicket + "&trainId=" + trainId + "", false);
    request.send(null);
    if (request.status === 200) {
        var dataR = JSON.parse(request.response);
        let seatsInfo = "";
        if (dataR != null && dataR.length != 0) {
            seatsInfo = "<select class='js-select1' id='seatsN" + num + "' name='seatsN" + num + "' placeholder='Choose seat'>";
            for (u = 0; u < dataR.length; u++) {
                seatsInfo = seatsInfo + "<option value=" + dataR[u] + ">" + dataR[u] + "</option>";
            }
            seatsInfo = seatsInfo + "</select>";
        }
        return seatsInfo;
    }
}

function ReturnSeatsByWagon() {
    var txtDate = document.getElementById("dateTicket").value;
    var objWagon = document.getElementById("wagonFirstTicket");
    if(objWagon == null){
        var txtWagon = "";
    } else {
        var txtWagon = objWagon.options[objWagon.selectedIndex].value;
    }
    var txtSchedule = document.getElementById("scheduleFirstTicket").getAttribute("value");

        $("#divForSeats").html(showSeatsList(txtSchedule, txtWagon, txtDate, "FirstTicket", 0));
}

function ReturnSeatsByWagonToElement(num) {
    console.log(num);
    var txtDate = document.getElementById("dateTicket").value;
    var objWagon = document.getElementById("wagonN" + num);
    if(objWagon == null){
        var txtWagon = "";
    } else {
        var txtWagon = objWagon.options[objWagon.selectedIndex].value;
    }
    var txtSchedule = document.getElementById("scheduleN" + num).getAttribute("value");

    $("#divForSeats" + num).html(showSeatsList(txtSchedule, txtWagon, txtDate, num, 0));

    var txtTrain = document.getElementById("trainN" + num).getAttribute("value");
    var txtDistance = document.getElementById("distanceN" + num).getAttribute("value");
    document.getElementById("priceN" + num).setAttribute("value", showPrice(txtTrain, txtDistance, txtWagon));
}

function showPrice(trainNumber, distance, wagonId){
    var price = 0.0;
    var request = new XMLHttpRequest();
    console.log("/getPrice?trainNumber=" + trainNumber + "&distance=" + distance + "&wagonId=" + wagonId);
    request.open('GET', "/getPrice?trainNumber=" + trainNumber + "&distance=" + distance + "&wagonId=" + wagonId, false);
    request.send(null);
    if (request.status === 200) {
        var dataR = JSON.parse(request.response);
        console.log(dataR);
        if (dataR != null && dataR.length != 0) {
            price = dataR;
        }
    }
    return price;
}