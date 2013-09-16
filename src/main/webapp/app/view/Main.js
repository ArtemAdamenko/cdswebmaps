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
                zoom: 12,
                type: 'yandex#hybrid'
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

            createYMap: function () {
                var me = this;
                me.update('<div style="width: ' + me.getEl().getWidth() + 'px; height: ' + me.getEl().getHeight() + 'px;" id="' + me.yMapId + '"></div>');
                Ext.Ajax.request({
                    url: 'GetBusesServlet',
                    success: function(response){
                        
                        if (response.responseText === undefined || response.responseText === null){
                            Ext.Msg.alert('Ошибка', 'Потеряно соединение с сервером');
                            return 0;
                        }
                        if (response.responseText.length === 0){
                            Ext.Msg.alert('Предупреждение', 'Данные пусты');
                            return 0;
                        }
                        var routes =  JSON.parse(response.responseText);
                        ymaps.ready(function () {
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

                                    var lng = convert(routes[i].last_lon_);
                                    var lat = convert(routes[i].last_lat_);
                                    
                                    //проверка тс на активность и соответствующий маркер
                                    var now = new Date().valueOf() - 600000;
                                    var marker = "twirl#blackStretchyIcon";
                                    var lastBusDate = new Date(routes[i].last_time_).valueOf();
                                    if (lastBusDate > now){
                                        //жирный шрифт для выделения активных автобусов
                                        marker = "twirl#greenStretchyIcon";
                                    }
                                    
                                    var address = me.getGeoLocation(lat,lng);
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
                                                    "<br>Маршрут " + routes[i].route_name_
                                        }
                                    }, {
                                            preset: marker
                                        });
                                    me.yMap.geoObjects.add(myGeoObject);
                                }
                        });
                    }
                });
            },
            getGeoLocation:function getGeoLocation(lat,lng) {
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
});