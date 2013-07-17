<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>Cds Web Map</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="css/style.css">  
    <script src="//api-maps.yandex.ru/2.0/?load=package.standard,package.route,package.geoObjects&lang=ru-RU" type="text/javascript"></script>
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">   
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.9.1.js" type="text/javascript"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js" type="text/javascript"></script>
    <script src="js/utils.js" type="text/javascript"></script>
    <script src="bootstrap/js/bootstrap.js" type="text/javascript"></script>  
    <script>   
        /*форматирование даты под запрос*/
        function parseDate(date){
            var parts = date.split(" ");
            var calendarDate = parts[0].split(".");
            if (parts[1] === undefined || parts[1] === ""){
                var dt = new Date();
                parts[1] = dt.getHours() + ":" + dt.getMinutes();
            }
            var dayTime = parts[1].split(":");
            var newDate = calendarDate[1] + "-" + calendarDate[0] + " " + dayTime[0] + ":" + dayTime[1] + ":00";
            newDate = calendarDate[2] + "-" + newDate;
            return newDate;
        }

        /*хранение маршрута, для его дальнейшего удаления с карты*/
        var router;
        var mass = new Array();
        var Placemarks = new Array();
        var routes;
        function route(){
            //очищаем карту
            
            var check = jQuery("input[type='radio']").filter(":checked");
            var fromTime = jQuery("input[type='text']")[0].value;
            var toTime = jQuery("input[type='text']")[1].value;
            fromTime = parseDate(fromTime);
            toTime = parseDate(toTime);

            //получаем данные для запроса маршрута
            if (check.length === 0){
                open_popup('#modal_window');
                return 0;
            }
            var proj = check[0].name;
            var busId = check[0].value;
            $.ajax({  
                type: "GET",
                data : "proj="+proj+"&bus="+busId+"&fromTime="+fromTime+"&toTime="+toTime,
                url: "GetRoute",  
                success: function(result){
                    routes =  JSON.parse(result);
                    document.getElementById('map-canvas').innerHTML = "";
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

                        for (var i = 0; i <= routes.length-1; i++){
                            var obj = new Object();
                            obj.point = [convert(routes[i].LON_), convert(routes[i].LAT_)];
                            obj.type = 'viaPoint';
                            mass[i] = obj;
                        }
                        //слайдер для удобного просмотра пути
                        $("#slider").slider({
                                min: 0,
                                max: mass.length-1,
                                change: function(event, ui) {
                                    if (router !== null)
                                        router && myMap.geoObjects.remove(router);
                                    router = null;
                                    var value = $( "#slider" ).slider( "option", "value" );
                                    if (Placemarks.length !== 0){
                                        myMap.geoObjects.remove(Placemarks[0]);
                                        Placemarks = new Array();
                                    }
                                    var Placemark = new ymaps.Placemark(mass[value].point, {                                 
                                        hintContent: routes[value].last_time_
                                    }, {
                                        // Опции.
                                        // Своё изображение иконки метки.
                                        iconImageHref: '/CdsWebMaps/images/car.png',
                                        // Размеры метки.
                                        iconImageSize: [40, 40],
                                        // Смещение левого верхнего угла иконки относительно
                                        // её "ножки" (точки привязки).
                                        iconImageOffset: [-3, -42]
                                    });
                                    Placemarks.push(Placemark);
                                    myMap.geoObjects.add(Placemark);
                                    
                                }
                        });
                        ymaps.route(mass,{ mapStateAutoApply: true}).then(function (route) {
                            router = route;
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
                var list = document.getElementsByClassName('dropdown-menu')[0];
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
                        var li = document.createElement('LI');
                        li.innerHTML = "<input type='radio' name='"+routes[i].proj_id_+"' value="+routes[i].obj_id_+">"+routes[i].name_;
                        list.appendChild(li);
                        myGeoObject = new ymaps.GeoObject({
                            geometry: {
                                type: "Point",
                                coordinates: [lng, lat]
                            },
                            properties: {
                                iconContent: routes[i].name_,
                                balloonContent: routes[i].last_time_
                            }
                        }, {
                                preset: 'twirl#redStretchyIcon'
                            });
                        myMap.geoObjects.add( myGeoObject);
                    }
                }
            }
            });
        }
    </script>
  </head>
  <body onload="initialize();">
        <div class="btn-group">
            <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                Автобусы
                <span class="caret"></span>
            </a>
            <ul class="dropdown-menu">             
            </ul>
            <button class="btn" onclick="redirect('report.jsp');">Отчет</button>
            <button class="btn" onclick="route();">Посмотреть маршрут</button>
            <button class="btn" onclick="logout('session_id');">Выход</button>   
        </div>
      <div id="slider"></div>
      <div id="modal_window" onclick="close_popup('#modal_window');" > 
          <p style="font-size: 20px;">Выберите автобус из списка</p><br>
          <p style="font-size: 12px;">Нажмите на любую область страницы</p>
      </div> 
      <div id="background" onclick="close_popup('#modal_window');"></div>
    
      
      <div id="map-canvas"></div>      
      <div id="routeTimes">
        <br><p>Промежуток времени:</p><br>
        С <input id ="fromTime" type="text" value="" size="17"><br> до <input type="text" value="" id="toTime" size="17">
        <div>
          <p> Вы можете перетаскивать ползунок с помощью указателя мыши или с помощью стрелок на клавиатуре когда ползунок активен.</p>            
        </div>
      </div>    
  </body>
</html>