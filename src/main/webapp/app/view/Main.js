// main view
Ext.define('CWM.view.Main', {
    alias: 'widget.main', // alias (xtype)
    extend: 'Ext.panel.Panel',
    title: 'Панель управления МБУ ЦДС "Веб карты" BETA',
    id:'main',
   // map instance
            yMap: null,

            // map cfg
            ymapConfig: {
                center: [51.7038, 39.1833], // default Москва
                zoom: 12,
                type: 'yandex#hybrid'
            },

            initComponent: function () {
                var me = this;
                me.tbar = [
                    {
                        itemId: 'MainMenuItem',
                        text: 'Маршруты',
                        menu:[]
                    },{
                        itemId: 'ReportItem',
                        text: 'Отчеты',
                        //action: 'openReport',
                        menu:[{
                                xtype:'button',
                                text:'Отчет 1',
                                action: 'openReport',
                        },{
                                xtype:'button',
                                text:'Рейсы',
                                action: 'openReport',
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
                        var routes =  JSON.parse(response.responseText);
                        ymaps.ready(function () {
                            me.yMap = new ymaps.Map(document.getElementById(me.yMapId), me.ymapConfig);
                            console.log('Map created: ', me.yMap);
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
                                            preset: 'twirl#redStretchyIcon'
                                        });
                                    me.yMap.geoObjects.add( myGeoObject);
                                }
                        });
                    }
                })
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