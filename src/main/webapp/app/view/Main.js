// main view
Ext.define('CWM.view.Main', {
    alias: 'widget.main', // alias (xtype)
    extend: 'Ext.panel.Panel',
    title: 'Панель управления МБУ ЦДС "Веб карта" FINAL версия Текущий пользователь: ' + document.cookie.split(";")[0].split("=")[1],
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
                    }, {
                        xtype: 'label',
                        itemId: 'realCoords',
                        id: 'realCoords',
                        forId: 'myFieldId',
                        text: '',
                        margin: '0 0 0 10'
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
                        var ERROR = checkResponseServer(response);
                        if (ERROR){
                            Ext.Msg.alert('Ошибка', ERROR);
                            return 0;
                        }
                        var routes =  JSON.parse(response.responseText);
                        ymaps.ready(function () {

                            me.yMap = new ymaps.Map(document.getElementById(me.yMapId), me.ymapConfig, {projection: ymaps.projection.wgs84Mercator });
                            me.yMap.copyrights.add("Разработчик сервиса Адаменко Артем. МБУ ЦДС 'Веб карта'.");
                            me.yMap.controls
                                // Кнопка изменения масштаба.
                                .add('zoomControl', { left: 5, top: 5 })
                                // Список типов карты
                                .add('typeSelector')
                                // Стандартный набор кнопок
                                .add('mapTools', { left: 35, top: 5 });
                        
                        
                                me.yMap.events.add('mousemove', function (e) {
                                    var label = Ext.getCmp("realCoords");
                                    label.setText(e.get('coordPosition') + "");
                                });
                           
                                for (var i = 0; i <= routes.length-1; i++)
                                {

                                    var lng = routes[i].last_lon_;
                                    var lat = routes[i].last_lat_;
                                    
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