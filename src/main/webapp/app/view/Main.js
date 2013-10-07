// main view
Ext.define('CWM.view.Main', {
    alias: 'widget.main', // alias (xtype)
    extend: 'Ext.panel.Panel',
    title: 'Панель управления МБУ ЦДС "Веб карты" BETA версия Текущий пользователь: ' + document.cookie.split(";")[0].split("=")[1],
    id:'main',
   // map instance
            yMap: null,

            // map cfg
            ymapConfig: {
                center: [51.7038, 39.1833],
                zoom: 12
            },

            initComponent: function () {
                var me = this;
                me.tbar = [
                    {
                        itemId: 'MainMenuItem',
                        text: 'Автобусы',
                        menu:[]
                    },{
                        itemId: 'ReportItem',
                        text: 'Отчеты',
                        menu:[{
                                xtype:'button',
                                text:'Отчет по перевозчикам',
                                action: 'openReport'
                        },{
                                xtype:'button',
                                text:'Отчет по выходу ТС на маршрут',
                                action: 'openReportRoute'
                        },{
                                xtype:'button',
                                text:'Подробный отчет',
                                action: 'openDetailReport'
                        },{
                                xtype: 'button',
                                text: 'Контроль движения транспорта',
                                action: 'moveBusControl'
                        }]
                    },{
                        itemId: 'lineChart',
                        text: 'График',
                        action: 'openChart'
                    },{
                        text: 'Маршруты',
                        itemId: 'routes',
                        menu: [{
                                xtype: 'button',
                                text: 'Посмотреть автобусы',
                                action: 'routes'
                                
                        },{
                                xtype: 'checkboxgroup',
                                itemId: 'checkboxes',
                                columns: 1,
                                vertical: true,
                                items: []
                        }]
                    },{
                        text: 'Поиск по автобусам',
                        itemId: 'searchBuses',
                        menu: [{
                                xtype: 'button',
                                text: ' Найти',
                                action: 'search'
                        },{
                                xtype: 'textfield',
                                itemId: 'searchBus',
                                allowBlank: false,
                                fieldLabel: 'Номер'
                        }]
                    },{
                        itemId: 'ExitItem',
                        text: 'Выход',
                        listeners:{
                            click:function(){
                                logout('session_id');
                                redirect('index.html');
                            }
                        }
                    }
                ];
                me.yMapId = "map-canvas";
                me.on('boxready', me.createYMap);
                me.callParent(arguments);
            },
            
            /*Создание веб карты*/
            createYMap: function () {
                var me = this;
                me.update('<div style="width: ' + me.getEl().getWidth() + 'px; height: ' + me.getEl().getHeight() + 'px;" id="' + me.yMapId + '"></div>');
                //Ext.updateMap = false;
                Ext.Ajax.request({
                    url: 'GetBusesServlet',
                    success: function(response){               
                        /*if (response.responseText === undefined || response.responseText === null){
                            Ext.Msg.alert('Ошибка', 'Потеряно соединение с сервером');
                            return 0;
                        }
                        if (response.responseText.length === 0){
                            Ext.Msg.alert('Предупреждение', 'Данные пусты');
                            return 0;
                        }*/
                        var ERROR = checkResponseServer(response);
                        if (ERROR){
                            Ext.Msg.alert('Ошибка', ERROR);
                            return 0;
                        }
                        var routes =  JSON.parse(response.responseText);
                        ymaps.ready(function () {
                            /*var homeLatlng = new google.maps.LatLng(51.7038, 39.183);

                            var myOptions = {
                                zoom: 15,
                                center: homeLatlng,
                                mapTypeId: google.maps.MapTypeId.ROADMAP
                            };

                            var map = new google.maps.Map(document.getElementById(me.yMapId), myOptions);
                            var image = new google.maps.MarkerImage(
                                'http://maps.google.com/mapfiles/ms/micons/green-dot.png',
                                new google.maps.Size(32, 32),   // size
                                new google.maps.Point(0,0), // origin
                                new google.maps.Point(16, 32)   // anchor
                            );

                            var shadow = new google.maps.MarkerImage(
                                'http://maps.google.com/mapfiles/ms/micons/msmarker.shadow.png',
                                new google.maps.Size(59, 32),   // size
                                new google.maps.Point(0,0), // origin
                                new google.maps.Point(16, 32)   // anchor
                            );

                            var homeMarker = new google.maps.Marker({
                                position: homeLatlng, 
                                map: map,
                                title: "Check this cool location",
                                icon: image,
                                shadow: shadow
                            });
                        

                            //google.maps.event.addDomListener(window, 'load', initialize);
                            var directionsService = new google.maps.DirectionsService();*/

                            
                            me.yMap = new ymaps.Map(document.getElementById(me.yMapId), me.ymapConfig, {projection: ymaps.projection.wgs84Mercator });
                            me.yMap.copyrights.add("Разработчик сервиса Адаменко Артем. МБУ ЦДС 'Веб карты'.");
                            me.yMap.controls
                                // Кнопка изменения масштаба.
                                .add('zoomControl', { left: 5, top: 5 })
                                // Список типов карты
                                .add('typeSelector')
                                // Стандартный набор кнопок
                                .add('mapTools', { left: 35, top: 5 });
                                for (var i = 0; i <= routes.length-1; i++)
                                {

                                    var lng = routes[i].last_lon_;
                                    //console.log(lng);
                                    var lat = routes[i].last_lat_;
                                    //console.log(lat);
                                   /* var home = new google.maps.LatLng(lng, lat);
                                    var homeMarker = new google.maps.Marker({
                                            position: home,
                                            map: map,
                                            title: "Check this cool location",
                                            icon: image,
                                            shadow: shadow
                                          });*/
                                    /*console.log("1"+ home);
                                    directionsService.route({origin:home, destination:homeLatlng, travelMode: google.maps.DirectionsTravelMode.DRIVING}, function(response, status) {
                                        if (status == google.maps.DirectionsStatus.OK)
                                        {
                                          var homeMarker = new google.maps.Marker({
                                            position: response.routes[0].legs[0].start_location, 
                                            map: map,
                                            title: "Check this cool location",
                                            icon: image,
                                            shadow: shadow
                                          });
                                          //console.log(homeMarker);
                                        } else {
                                          console.log("2"+home);
                                          var homeMarker = new google.maps.Marker({
                                            position: home,
                                            map: map,
                                            title: "Check this cool location",
                                            icon: image,
                                            shadow: shadow
                                          });
                                          //console.log(homeMarker);
                                        }
                                      });*/
                                    //проверка тс на активность и соответствующий маркер
                                    var now = new Date().valueOf() - 600000;
                                    var marker = "twirl#blackStretchyIcon";
                                    var lastBusDate = new Date(routes[i].last_time_).valueOf();
                                    if (lastBusDate > now){
                                        //жирный шрифт для выделения активных автобусов
                                        marker = "twirl#greenStretchyIcon";
                                    }
                                    
                                    myGeoObject = new ymaps.GeoObject({
                                        geometry: {
                                            type: "Point",
                                            coordinates: [lng, lat]
                                        },
                                        properties: {
                                            iconContent: routes[i].name_,
                                            balloonContent: datef("dd.MM.YYYY hh:mm:ss", routes[i].last_time_) + 
                                                    "<br>Долгота: " + lng + 
                                                    " Широта: " + lat + 
                                                    "<br> Скорость: " + routes[i].last_speed_ + 
                                                    " КМ/Ч<br>Время последней остановки: " + datef("dd.MM.YYYY hh:mm:ss", routes[i].last_station_time_) +
                                                    "<br> Последняя остановка: " + routes[i].bus_station_ +
                                                    "<br>Местоположение: " + routes[i].address + 
                                                    "<br>Маршрут " + routes[i].route_name_
                                        }
                                    }, {
                                            preset: marker
                                        });
                                        
                                        /*var myGeocoder = ymaps.geocode([lng, lat]);
                                        myGeocoder.then(
                                        function (res) {
                                            var coords = res.geoObjects.get(0).geometry.getCoordinates();
                                            var myGeocoder = ymaps.geocode(coords, {kind: 'street', provider: 'yandex#publicMap'});
                                            myGeocoder.then(
                                                function (res) {
                                                    var street = res.geoObjects.get(3);
                                                    console.log(street);
                                                    if (street != null){
                                                    myGeoObject = new ymaps.GeoObject({
                                                        geometry: {
                                                            type: "Point",
                                                            coordinates: [street.properties._T.boundedBy[0][0], street.properties._T.boundedBy[0][1]]
                                                        },
                                                    })
                                                    me.yMap.geoObjects.add(myGeoObject);
                                                    }
                                                    
                                                    //var name = street.properties.get('name');
                                                    // Будет выведено «улица Большая Молчановка»,
                                                    // несмотря на то, что обратно геокодируются
                                                    // координаты дома 10 на ул. Новый Арбат.
                                                    //console.log(name);
                                                }
                                            );
                                    });*/
                                        
                                    /*var result = ymaps.geoQuery(myGeoObject).addToMap(me.yMap);
                                    var closestObject = result.getClosestTo([lng, lat]);
                                    console.log(closestObject);
                                    lng = closestObject.geometry._S._Bb[1][0];
                                    lat = closestObject.geometry._S._Bb[1][1];
                                    myGeoObject = new ymaps.GeoObject({
                                        geometry: {
                                            type: "Point",
                                            coordinates: [lng, lat]
                                        },
                                    })*/
                                   me.yMap.geoObjects.add(myGeoObject);
                                }
                        });
                    },
                    failure:function () {
                        Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
                    }      
                });
            },
});