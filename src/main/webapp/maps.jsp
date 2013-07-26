<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>Cds Web Map</title>
    <meta charset="utf-8">
    <link href="css/style.css" rel="stylesheet" type="text/css">  
    <link href="css/bootstrap/css/bootstrap.css" rel="stylesheet" media="screen">   
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
    <script src="//api-maps.yandex.ru/2.0/?load=package.standard,package.route,package.geoObjects&lang=ru-RU" type="text/javascript"></script>
    <script src="js/jquery-ui-1.10.3.custom/js/jquery-1.9.1.js" type="text/javascript"></script>
    <script src="js/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>
    <script src="js/utils.js" type="text/javascript"></script>
    <script src="js/timeaddon.js" type="text/javascript"></script>
    <script src="js/datef.js" type="text/javascript"></script>
    <script src="css/bootstrap/js/bootstrap.js" type="text/javascript"></script>  
    <script>   
        /*хранение маршрута, для его дальнейшего удаления с карты*/
        var router;
        var mass = new Array();
        var Placemarks = new Array();
        var routes;
        
        function route(){
            var fromTime =$( "#datepickerfromTime" ).datetimepicker("getDate");
            var toTime = $( "#datepickertoTime" ).datepicker( "getDate" );
            var check = jQuery("input[type='radio']").filter(":checked");
            //проверка на присутствие нужных данных
            if (check.length === 0 || fromTime === null || toTime === null){
                open_popup('#null_bus_window');
                return 0;
            }
            fromTime = datef("YYYY-MM-dd hh:mm:ss", fromTime);
            toTime = datef("YYYY-MM-dd hh:mm:ss", toTime);

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
                        if (routes.length === 0){
                            open_popup('#null_coord_window');
                            return 0;
                        }
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
                routes = routes.sort(function(obj1, obj2) {
                                        // Сортировка по возрастанию
                                        return obj1.route_name_.localeCompare(obj2.route_name_);
                                        });
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
                    var route_name_ = routes[0].route_name_;
                    var li = document.createElement('li');
                    li.className = 'dropdown-submenu';
                    var a = document.createElement('a');
                    a.tabindex = '-1';
                    a.href = '#';
                    li.appendChild(a);
                    a.innerHTML = "Маршрут "+route_name_;
                    
                    var ul = document.createElement("ul");
                    ul.className = 'dropdown-menu';
                    li.appendChild(ul);
                    
                    
                    for (var i = 0; i <= routes.length-1; i++)
                    {
                        if (route_name_ !== routes[i].route_name_){
                            route_name_ = routes[i].route_name_;
                            li = document.createElement('li');
                            li.className = 'dropdown-submenu';
                            var a = document.createElement('a');
                            a.tabindex = '-1';
                            a.href = '#';
                            li.appendChild(a);
                            a.innerHTML = "Маршрут " + route_name_;

                            ul = document.createElement("ul");
                            ul.className = 'dropdown-menu';
                            li.appendChild(ul);
                        }
                        var lng = convert(routes[i].last_lon_);
                        var lat = convert(routes[i].last_lat_);
                        /*create li for radiobutton*/
                        var liRadio = document.createElement('li');
                        /*create radiobutton*/
                        var radio = document.createElement("input");
                        radio.type = 'radio';
                        radio.name = routes[i].proj_id_;
                        radio.value = routes[i].obj_id_;
                        /*create description for radiobutton*/
                        var span = document.createElement("span");
                        span.innerHTML = routes[i].name_;
                        /*append elements*/
                        liRadio.appendChild(radio);
                        liRadio.appendChild(span);
                        ul.appendChild(liRadio);
                        list.appendChild(li);

                        var address = getGeoLocation(lat,lng);
                        myGeoObject = new ymaps.GeoObject({
                            geometry: {
                                type: "Point",
                                coordinates: [lng, lat]
                            },
                            properties: {
                                iconContent: routes[i].name_,
                                balloonContent: datef("dd.MM.YYYY hh:mm:ss", routes[i].last_time_) + 
                                        "<br>Долгота: " + lng.toFixed(6) + 
                                        " Широта: " + lat.toFixed(6) + 
                                        "<br> Скорость: " + routes[i].last_speed_ + 
                                        " КМ/Ч<br>Время последней остановки: " + datef("dd.MM.YYYY hh:mm:ss", routes[i].last_station_time_) +
                                        "<br> Последняя остановка: " + routes[i].bus_station_ +
                                        "<br>Местоположение: " + address + 
                                        "<br>Маршрут " + route_name_
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

        function getGeoLocation(lat,lng) {
        var res;
            var xhr = new XMLHttpRequest();
            xhr.open('GET', 'GeocodeServlet?lat=' + lat + "&lng=" + lng, false);
            xhr.onreadystatechange = function() {
                if (xhr.readyState !== 4) return;
                    res = xhr.responseText;
            };
            xhr.send(null);
            return res;
        }

    </script>
    <script>
         $(function() {
            $( "#datepickerfromTime" ).datetimepicker();
            $( "#datepickertoTime" ).datetimepicker();
            
         });
    </script>
  </head>
  <body onload="initialize();">
        <div class="btn-group">
            <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                Автобусы
                <span class="caret"></span>
            </a>
            <ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">             
            </ul>
            <button class="btn" onclick="redirect('report.jsp');">Отчет</button>
            <button class="btn" onclick="route();">Посмотреть маршрут</button>
            <button class="btn" onclick="logout('session_id');">Выход</button>   
        </div>
      <div id="slider"></div>
      <div id="null_bus_window" onclick="close_popup('#null_bus_window');" > 
          <p style="font-size: 20px;">Выберите автобус из списка и корректный интервал даты</p><br>
          <p style="font-size: 12px;">Нажмите на любую область страницы</p>
      </div> 
      <div id="background" onclick="close_popup('#null_bus_window');"></div>
      <div id="null_coord_window" onclick="close_popup('#null_coord_window');" > 
          <p style="font-size: 20px;">Данные отсутствуют</p><br>
          <p style="font-size: 12px;">Нажмите на любую область страницы</p>
      </div> 
      <div id="background" onclick="close_popup('#null_coord_window');"></div>

      <div id="map-canvas"></div>      
      <div id="routeTimes">
        <br><p>Промежуток времени:</p><br>
        С&nbsp;&nbsp;&nbsp;<input id ="datepickerfromTime" type="text" value="" size="17"><br> до <input type="text" value="" id="datepickertoTime" size="17">
        <div>
          <p> Вы можете перетаскивать ползунок с помощью указателя мыши или с помощью стрелок на клавиатуре когда ползунок активен.</p>            
        </div>
      </div>    
  </body>
</html>