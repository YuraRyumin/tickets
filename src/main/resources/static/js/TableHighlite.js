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
                for (i = 0; i < data.length; i++) {
                    let opositeGender = (data[i].passengerGender = "MALE") ? "<option value='FEMALE'>FEMALE</option>" : "<option value='FEMALE'>MALE</option>";
                    ticketsInfo = ticketsInfo +
                        "<tr><td>Station first</td><td>" + data[i].stationFirst + "</td></tr>" +
                        "<tr><td>Time departure</td><td>" + data[i].timeDeparture + "</td></tr>" +
                        "<tr><td>Station last</td><td>" + data[i].stationLast + "</td></tr>" +
                        "<tr><td>Time arrival</td><td>" + data[i].timeArrival + "</td></tr>" +
                        "<tr><td>Passenger name</td><td><input type='textPassengerName' name='login' value=" + data[i].passengerName + "></td></tr>" +
                        "<tr><td>Passenger surname</td><td><input type='textPassengerSurname' name='login' value=" + data[i].passengerSurname + "></td></tr>" +
                        "<tr><td>Passenger gender</td><td><select id='passengerGender' name='passengerGender' placeholder='Choose gender'>" +
                        "<option value=" + data[i].passengerGender + ">" + data[i].passengerGender + "</option>" +
                        opositeGender +
                        "</select></td></tr>" +
                        "<tr><td>Passenger passport</td><td><input type='textPassengerPassport' name='login' value=" + data[i].passengerPassport + "></td></tr>";
                }

                ticketsInfo = ticketsInfo + "</tbody></table> <div align='center'><button type='button'>Confirm</button></div>";
                $("#tableTickets").html(ticketsInfo);
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
                        "<tr><td>Station first</td><td>" + data[i].stationFirst + "</td></tr>" +
                        "<tr><td>Time departure</td><td>" + data[i].timeDeparture + "</td></tr>" +
                        "<tr><td>Station last</td><td>" + data[i].stationLast + "</td></tr>" +
                        "<tr><td>Time arrival</td><td>" + data[i].timeArrival + "</td></tr>" +
                        "<tr><td>Passenger name</td><td><input type='textPassengerName' name='login' value=" + data[i].passengerName + "></td></tr>" +
                        "<tr><td>Passenger surname</td><td><input type='textPassengerSurname' name='login' value=" + data[i].passengerSurname + "></td></tr>" +
                        "<tr><td>Passenger gender</td><td><select id='passengerGender' name='passengerGender' placeholder='Choose gender'>" +
                        "<option value=" + data[i].passengerGender + ">" + data[i].passengerGender + "</option>" +
                        opositeGender +
                        "</select></td></tr>" +
                        "<tr><td>Passenger passport</td><td><input type='textPassengerPassport' name='login' value=" + data[i].passengerPassport + "></td></tr>";
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
                for (i = 0; i < data.length; i++) {
                    let opositeGender = (data[i].passengerGender = "MALE") ? "<option value='FEMALE'>FEMALE</option>" : "<option value='FEMALE'>MALE</option>";
                    ticketsInfo = ticketsInfo +
                        "<tr><td>Station first</td><td>" + data[i].stationFirst + "</td></tr>" +
                        "<tr><td>Time departure</td><td>" + data[i].timeDeparture + "</td></tr>" +
                        "<tr><td>Station last</td><td>" + data[i].stationLast + "</td></tr>" +
                        "<tr><td>Time arrival</td><td>" + data[i].timeArrival + "</td></tr>" +
                        "<tr><td>Passenger name</td><td><input type='textPassengerName' name='login' value=" + data[i].passengerName + "></td></tr>" +
                        "<tr><td>Passenger surname</td><td><input type='textPassengerSurname' name='login' value=" + data[i].passengerSurname + "></td></tr>" +
                        "<tr><td>Passenger gender</td><td><select id='passengerGender' name='passengerGender' placeholder='Choose gender'>" +
                        "<option value=" + data[i].passengerGender + ">" + data[i].passengerGender + "</option>" +
                        opositeGender +
                        "</select></td></tr>" +
                        "<tr><td>Passenger passport</td><td><input type='textPassengerPassport' name='login' value=" + data[i].passengerPassport + "></td></tr>";
                }

                ticketsInfo = ticketsInfo + "</tbody></table> <div align='center'><button type='button'>Confirm</button></div>";
                $("#tableTicketsTwo").html(ticketsInfo);
            }
        });
}