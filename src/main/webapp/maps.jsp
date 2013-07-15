<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>Cds Web Map</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="css/reportStyle.css">
     <script src="//api-maps.yandex.ru/2.0/?load=package.standard,package.route&lang=ru-RU" type="text/javascript"></script>
     <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
    <script src="js/utils.js"></script>
    <style type="text/css">
        #tabs
        {
            font-size:0.7em;
            height:500px;
        }
        #range
        {
            color:grey;
            font-weight:bold;
            width:250px;
            margin-top:10px;
            /*margin-bottom:10px;*/
        }
    </style>
    <script>   
        /*форматирование даты под запрос*/
        function parseDate(date){
            var parts = date.split(" ");
            var calendarDate = parts[0].split(".");
            var dayTime = parts[1].split(":");
            //var newDate = new Date(calendarDate[2], calendarDate[1] - 1, calendarDate[0], dayTime[0], dayTime[1]);
            var newDate = calendarDate[1] + "-" + calendarDate[1] + " " + dayTime[0] + ":" + dayTime[1] + ":00";
            newDate = calendarDate[2] + "-" + newDate;
            return newDate;
        }
        var routes;
        function route(){
            //очищаем карту
            document.getElementById('map-canvas').innerHTML = "";
            var check = jQuery("input[type='radio']").filter(":checked");
            var fromTime = jQuery("input[type='text']")[0].value;
            var toTime = jQuery("input[type='text']")[1].value;
            fromTime = parseDate(fromTime);
            toTime = parseDate(toTime);
            //var dt = new Date(fromTime);
            //получаем данные для запроса маршрута
            var proj = check[0].name;
            var busId = check[0].value;
            $.ajax({  
                type: "GET",
                data : "proj="+proj+"&bus="+busId+"&fromTime="+fromTime+"&toTime="+toTime,
                url: "GetRoute",  
                success: function(result){
                    routes =  JSON.parse(result);
                    ymaps.ready(init);
                    function init() {
                        myMap = new ymaps.Map("map-canvas", {
                            center: [51.7038, 39.1833],
                            zoom: 12,
                            type: 'yandex#hybrid'
                        });
                        myMap.controls
                        // Кнопка изменения масштаба.
                        .add('zoomControl', { left: 5, top: 5 })
                        // Список типов карты
                        .add('typeSelector')
                        // Стандартный набор кнопок
                        .add('mapTools', { left: 35, top: 5 });
                        var mass = new Array();

                        for (var i = 0; i <= routes.length-1; i++){
                            var obj = new Object();
                            obj.point = [convert(routes[i].LON_), convert(routes[i].LAT_)];
                            obj.type = 'viaPoint';
                            mass[i] = obj;
                            //mass[i] =  [convert(routes[i].LON_), convert(routes[i].LAT_)];
                        }
                        //слайдер для удобного просмотра пути
                        $("#slider").slider({
                                min: 0,
                                max: mass.length-1,
                                change: function(event, ui) {
                                    myMap.geoObjects.remove(routes);
                                    var value = $( "#slider" ).slider( "option", "value" );
                                    alert("Положение: " + value);
                                }

                        });
                        ymaps.route(mass,{ mapStateAutoApply: true}).then(function (route) {
                            myMap.geoObjects.add(route);
                        });
                    }
                }
            });
        }
            function routeChange() {
                value = document.getElementById("value").value;
                ymaps.route(value).then(function (route) {
                    myMap.geoObjects.add(route);
                });
           }
        //setInterval("fresh()",15000);
       
        function initialize() {    
            $.ajax({  
            type: "GET",  
            url: "GetBusesServlet",  
            success: function(result){
                var controlPanel = document.getElementById('control_panel');
                var routes =  JSON.parse(result);               
                var checkboxes = "";
                ymaps.ready(init);
                    function init(){
                    myMap = new ymaps.Map("map-canvas", {
                        center: [51.7038, 39.1833],
                        zoom: 12,
                        type: 'yandex#hybrid'
                    });
                    myMap.controls
                        // Кнопка изменения масштаба.
                        .add('zoomControl', { left: 5, top: 5 })
                        // Список типов карты
                        .add('typeSelector')
                        // Стандартный набор кнопок
                        .add('mapTools', { left: 35, top: 5 });
                    for (var i = 0; i <= routes.length-1; i++)
                    {
                        var lng = convert(routes[i].last_lon_);
                        var lat = convert(routes[i].last_lat_);
                        checkboxes +="<input type='radio' name='"+routes[i].proj_id_+"' value="+routes[i].obj_id_+">"+routes[i].name_+"<br>";
                        myPlacemark = new ymaps.Placemark([lng, lat], {
                            balloonContentHeader: routes[i].name_,
                            balloonContentBody: "Долгота "+  lng.toFixed(4) + " " + "Широта " + lat.toFixed(4),
                            balloonContentFooter: routes[i].last_time_,
                            hintContent: routes[i].name_
                        });
                        myMap.geoObjects.add(myPlacemark);
                    }
                    controlPanel.innerHTML = checkboxes;
                }
            }
            });
        }
    </script>
   
  </head>
  <body onload="initialize();">
      <div id="map-canvas" style="width: 83%; position: absolute;"></div> 
      <div style="position: relative;margin-left: 1600px;font-size: 1.2em;">
        <input type="submit" value="Отчет" onclick="redirect('report.jsp');">
        <input type="submit" value='Выход' id ='logout' onclick="logout('session_id');">
        <input type="submit" value="Посмотреть маршрут" onclick="route();">
        <br><p>Промежуток времени:</p><br>
        С <input id ="fromTime" type="text" value="" size="17"> до <input type="text" value="" id="toTime" size="17">
        <div>
          <p> Вы можете перетаскивать ползунок с помощью указателя мыши или с помощью стрелок на клавиатуре когда ползунок активен.</p>
          <div id="slider"></div> 
        </div>
      </div>
      <div id="control_panel" style="float:right;width:10%;height: 600px;text-align:left;padding-top:20px;overflow: scroll; overflow-x: no-display;">
      
      </div>     
      
     
  </body>
</html>