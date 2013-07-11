<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Отчет</title>
        <link rel="stylesheet" type="text/css" href="css/reportStyle.css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.3/jquery.min.js"></script>
        <script src="js/utils.js"></script>  
        <script>
        function report(){
        $.ajax({
            url: 'Report',
            success: function(data) {
               /*отображение шапки отчета*/
               var parseData = data.split("@");
               var headData = JSON.parse(parseData[0]);
               var currentDate = new Date();
               var reportData = JSON.parse(parseData[1]);
               var header = "Отчет по перевозчику «" + headData.NAME_ + "» по состоянию на «"+getMyDate(currentDate)+"»<br>Всего записей " + reportData.length;
               var id = document.getElementById("report_header");
               id.innerHTML=header;
               /*основной контент отчета*/           
               var view = "";
               var lastStationTime = "";
               var lastTime = "";
               for (var i = 0; i <= reportData.length-1; i++){
                   view += "<tr>";
                    var j = i;
                    view += "<td  id=\"obj_num\">" + ++j +"</td>";
                    view += "<td  id=\"obj_name\">" + reportData[i].NAME_ +"</td>";
                    view += "<td  id=\"obj_cbname\">" + reportData[i].CBNAME_ +"</td>";
                    view += "<td  id=\"obj_pvname\">" + reportData[i].PVNAME +"</td>";
                    view += "<td  id=\"obj_rname\">" + reportData[i].RNAME_ +"</td>";
                    
                    if (reportData[i].LAST_STATION_TIME_ !== undefined)
                        lastStationTime = getMyDate(reportData[i].LAST_STATION_TIME_);
                    else
                        lastStationTime = "Дата неизвестна";
                    view += "<td id=\"obj_lastStationTime\">" + lastStationTime +"</td>";
                    if (reportData[i].LAST_TIME_ !== undefined)
                        lastTime = getMyDate(reportData[i].LAST_TIME_);
                    else
                        lastTime = "Дата неизвестна";   
                    view += "<td id=\"obj_lastTime\">" + lastTime +"</td>";
                   view += "</tr>";
               }
               var id = document.getElementById("report_content");
               id.innerHTML+=view;
            }
        });
    }
    </script>
    </head>
    <body onload="report();">
        <input type="submit" value="Печать" onclick="javascript: window.print();">
        <input type="submit" value="Карты" onclick="redirect('maps.jsp');">
        <input type="submit" value="Выйти" onclick="logout('session_id');">
        <div id="report_header"></div>
        <table id="report_content" align=center BORDER="1" cellpadding="0" cellspacing="0"> 
            <tr id="table_header">
                <td>№ п/п</td>
                <td>ГосНомерТС</td>
                <td>Марка ТС</td>
                <td>Установщик</td>
                <td>Маршрут следования</td>
                <td>Время прохождения последней остановки</td>
                <td>Время последнего отклика</td> 
       </table>
    </body>
</html>
