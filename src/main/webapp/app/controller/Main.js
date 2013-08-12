Ext.define('CWM.controller.Main', {
    extend: 'Ext.app.Controller',
    views: ['CWM.view.Main','CWM.view.Report','CWM.view.RouteOptions', 'CWM.view.ReportRoute', 'CWM.view.MyChart'],
    refs: [
        {ref: 'MainView', selector: 'main'} // Reference to main view
    ],

    init: function() {  
        var me = this;
        //регистрация кнокпи на функцию
        //Отчет по перевозчикам
        me.control({'button[action=openReport]':{
                            click: me.openReport
                }
        });
        //отслеживание маршрута
        me.control({'button[action=getRoute]':{
                            click: me.getRoute
                }
        });
        //отчет по рейсам
        me.control({'button[action=openReportRoute]':{
                            click: me.openReportRoute
                }
        });
        me.control({'main': {
                afterrender: me.updateMap
            }
        });
        me.control({'button[action=openChart]': {
                click: me.openChart
            }
        });
         
         
        //запрос на автобусы для отображения на карте
        Ext.Ajax.request({
            url: 'GetBusesServlet',
            success: function(response){
                if (response.responseText === undefined || response.responseText === null){
                    Ext.Msg.alert('Ошибка', 'Потеряно соединение с сервером');
                    return 0;
                }
                var routes =  JSON.parse(response.responseText);  
                routes = routes.sort(function(obj1, obj2) {
                                        // Сортировка по возрастанию
                                        return obj1.route_name_.localeCompare(obj2.route_name_);
                                        });
                var w = me.getMainView(),
                t = w.down('#MainMenuItem');
                
                // add menu items
                var route_name_ = routes[0].route_name_;
                var item = {text: route_name_};
                item.menu = [];
                //интервал времени для выделения активных автобусов
                var now = new Date().valueOf() - 600000;
                
                for (var i=0; i<= routes.length-1; i++) {
                    if (route_name_ !== routes[i].route_name_){
                        t.menu.add(item);
                        route_name_ = routes[i].route_name_;
                        var item = {text: route_name_};    
                        item.menu = [];
                    }
                    
                    var style = "";
                    var lastBusDate = new Date(routes[i].last_time_).valueOf();
                    if (lastBusDate > now){
                        //жирный шрифт для выделения активных автобусов
                        style = "cls";
                    }
                    item.menu.push({
                                    text: routes[i].name_,
                                    checked: false,
                                    group: route_name_,
                                    handler:me.getRoute,
                                    itemId:routes[i].obj_id_,
                                    name:routes[i].proj_id_,
                                    cls: style
                                    });
                    if (i === routes.length-1)
                         t.menu.add(item);
                }
            },
            failure: function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }
        });
    },
    
    //обновление маркеров на карте без перезагрузки
    updateMap: function (main) {
        var me = this;
        function func() {
            ymaps.ready(function(){
                
                Ext.Ajax.request({
                    url: 'GetBusesServlet',
                    success: function(response){
                        var routes =  JSON.parse(response.responseText);
                        
                        var objects = new Array();
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
                               objects.push(myGeoObject);
                            
                        }
                        main.yMap.geoObjects.each(function(geoObject){
                            main.yMap.geoObjects.remove(geoObject);
                        });
                        for (var i = 0; i <= objects.length-1; i++){
                            main.yMap.geoObjects.add(objects[i]);
                        }
                        
                    },
                    failure: function(){
                        Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
                    }
                });
            });
        }
        setInterval(func, 30000);
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
            },
    //Открытие окна с отчетом по перевозчикам
    openReport: function(){
        var win = Ext.widget('report');
        win.show();
    },
    
    getRoute: function(btn){
        var win = Ext.widget('routeOptions',{proj:btn.name, obj:btn.itemId});
        win.show();
    },
    //вызов компонента отчета по рейсам
    openReportRoute: function(){
        var me = this;
        var w = me.getMainView(),
        menu = w.down('#MainMenuItem');
        var items = new Array();
        //Формируем все маршруты текущего перевозчика для передачи отчету
        for (var i = 0; i <= menu.menu.items.items.length-1; i++)
            items.push({route:menu.menu.items.items[i].text});
        var reportWin = Ext.widget('reportRoute', {routes:items});
        reportWin.show();
        
    },
            
    openChart: function(){
        var win = Ext.widget('mychart');
        win.show();
    }
});