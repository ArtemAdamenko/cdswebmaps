<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>Cds Web Map</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="css/reportStyle.css">
     <script src="//api-maps.yandex.ru/2.0/?load=package.standard,package.route&lang=ru-RU" type="text/javascript"></script>
     <script src="http://yandex.st/jquery/1.6.4/jquery.min.js" type="text/javascript"></script>
    <script src="js/utils.js"></script>
    <script>   
        function route(){
            //очищаем карту
            document.getElementById('map-canvas').innerHTML = "";
            var check = jQuery("input[type='radio']").filter(":checked");
            //получаем данные для запроса маршрута
            var proj = check[0].name;
            var busId = check[0].value;
            $.ajax({  
                type: "GET",
                data : "proj="+proj+"&bus="+busId,
                url: "GetRoute",  
                success: function(result){
                    var routes =  JSON.parse(result);
                    ymaps.ready(init);
                    function init() {
                        myMap = new ymaps.Map("map-canvas", {
                            center: [51.7038, 39.1833],
                            zoom: 13
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
                            obj.type = 'wayPoint';
                            mass[i] = obj;
                        }
                        ymaps.route(mass).then(function (route) {
                            myMap.geoObjects.add(route);
                        });
                    }
                }
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
                        zoom: 13
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
      <div style="position: relative;margin-left: 1600px;">
      <input type="submit" value="Отчет" onclick="redirect('report.jsp');">
      <input type="submit" value='Выход' id ='logout' onclick="logout('session_id');">
      <input type="submit" value="Посмотреть маршрут" onclick="route();"></div>
      <div id="control_panel" style="float:right;width:10%;height: 90%;text-align:left;padding-top:20px;overflow: scroll; overflow-x: no-display;">
      
      </div>     
     
  </body>
</html>