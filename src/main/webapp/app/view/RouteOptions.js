Ext.define('CWM.view.RouteOptions', {
    alias: 'widget.routeOptions', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Параметры маршрута',
    width: 200,
    height: 250,
    items: [],
    id: 'routes',
    config:{
        proj:'',
        obj:''
    },

      constructor: function(){
        this.callParent(arguments);
    },

    initComponent: function () {
        var date = new Date();
        //date = parseTime(date);
        date.getHours() < 10? hour = "0" + date.getHours() : hour = date.getHours();
        date.getMinutes() < 10? min = "0" + date.getMinutes() : min = date.getMinutes();
        date = hour + ":" + min;
        
        var me = this; 
        me.tbar=[{
            xtype:'button',
            text:'Проложить маршрут',
            listeners:{
                    click: me.routing
            }
        }];
        me.items=[{
                xtype: 'label',
                forId: 'myFieldId',
                text: 'Начало',
                margin: '0 0 0 10'
        },{
                xtype: 'datefield',
                anchor: '100%',
                id: 'from_date',
                maxValue: new Date()
        },{
                xtype: 'textfield',
                id: 'from_time',
                /*minValue: '00:00',
                maxValue: '23:30',
                format: 'H:i',*/
                increment: 30
        },{
                xtype: 'label',
                forId: 'myFieldId',
                text: 'Конец',
                margin: '0 0 0 10'
        },{
                xtype: 'datefield',
                anchor: '100%',
                id: 'to_date',
                value: new Date(),
                maxValue: new Date()
        },{
                xtype: 'textfield',
                id: 'to_time',
                /*minValue: '00:00',
                maxValue: '23:30',
                format: 'H:i',*/
                increment: 30,
                value: date
        }];
        
        me.callParent(arguments);

    },
            
    /*прорисовка траектории и прохождение по каждой точке*/    
    routing: function(){
        Ext.updateMap = false;
        //Собираем данные для запроса 
        var widget = Ext.getCmp('routes');
        //начало интервала времени
        var from_date = datef("YYYY-MM-dd", Ext.getCmp('from_date').getValue());
        var from_time = Ext.getCmp('from_time').value + ":00";
        
        //конец интервала
        var to_date = datef("YYYY-MM-dd", Ext.getCmp('to_date').getValue());
        var to_time = Ext.getCmp('to_time').value + ":00";
        
        //создаем даты со временем
        var fullDateFrom = from_date + " " + from_time;
        var fullDateTo = to_date + " " + to_time;
        
        var mass = new Array();
        var routes;
        var Placemarks = new Array();
        Ext.Ajax.request({
            url:'GetRoute',
            method: 'POST',
            params: { 
                proj:widget.getProj(),
                bus:widget.getObj(),
                fromTime:fullDateFrom, 
                toTime:fullDateTo
            },
            success:function(response){
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
                routes = JSON.parse(response.responseText);
                ymaps.ready(function init() {            
                        var map = Ext.getCmp("main");
                        map.yMap.geoObjects.each(function(geoObject){
                                    map.yMap.geoObjects.remove(geoObject);
                        });
                        var myRoute;
                        var j = 0;
                        var leng = (routes.length-1)/10;
                        leng = parseInt(leng) * 10;
                        for (var i = 0; i <= leng; i= i+10){
                            mass[j] = new Object({
                                point:[routes[i].LON_, routes[i].LAT_],
                                type: 'viaPoint'
                            });
                            j++;
                        }
                        
                        var Placemarks = new Array();
                        /*
                        ymaps.route(mass,{ mapStateAutoApply: true}).then(function (route) {
                            myRoute = route;
                            // Задание контента меток в начальной и конечной точках
                            var points = route.getViaPoints();
                            console.log(points);
                            points.get(0).properties.set("iconContent", "А");
                            points.get(1).properties.set("iconContent", "Б");
                            console.log(points.get(0));

                            // Добавление маршрута на карту
                            map.yMap.geoObjects.add(route);
                        }, function (error) {
                            alert('Возникла ошибка: ' + error.message);
                        });*/
                        /*ymaps.route(mass).then(function (route) {
                            map.yMap.geoObjects.add(route);

                        }, function (error) {
                            alert('Возникла ошибка: ' + error.message);
                        });*/
                          ymaps.route(mass,{ mapStateAutoApply: true}).then(function (route) {
                            myRoute = route;
                            map.yMap.geoObjects.add(route);
                            console.log(route);
                          })   /*, {
                            // Описываем свойства геообъекта.
                            // Содержимое балуна.
                            balloonContent: "Ломаная линия"
                        }, {
                            // Задаем опции геообъекта.
                            // Отключаем кнопку закрытия балуна.
                            balloonHasCloseButton:false,
                            // Цвет линии.
                            strokeColor: "#000000",
                            // Ширина линии.
                            strokeWidth: 4,
                        });*/
 
                        //map.yMap.geoObjects.add(myPolyline);

                        var slider = Ext.create('Ext.slider.Single', {
                                                    width: 480,
                                                    height: 50,
                                                    increment: 1,
                                                    minValue: 0,
                                                    maxValue: routes.length-1,
                                                    renderTo: Ext.getBody(),
                                                    tipText: function(thumb){ 
                                                        return Ext.String.format('<b>{0}</b>', thumb.value);
                                                    },
                                                    listeners:{
                                                        changecomplete:function(slider, value, thumb, eOpts){
                                                            if (myRoute !== null){
                                                                //Очищаем карту от траектории
                                                                myRoute && map.yMap.geoObjects.remove(myRoute);
                                                                myRoute = null;
                                                            }                                                
                                                            if (Placemarks.length !== 0){
                                                                //удаляем предыдущую метку ТС
                                                                map.yMap.geoObjects.remove(Placemarks[0]);
                                                                Placemarks = new Array();
                                                            }
                                                            var Placemark = new ymaps.Placemark([routes[value].LON_, routes[value].LAT_], {                                 
                                                                iconContent:routes[value].TIME_.split(" ")[1].substr(0, 8)
                                                            },  {
                                                                    preset: "twirl#yellowStretchyIcon",
                                                                    // Балун будем открывать и закрывать кликом по иконке метки.
                                                                    hideIconOnBalloonOpen: false
                                                                });
                                                            //Ставим метку положения ТС
                                                            Placemarks.push(Placemark);
                                                            map.yMap.geoObjects.add(Placemark);
                                                        }
                                                    }
                                               });
                         var win1 = Ext.create('widget.window', { 
                                                    height: 130,
                                                    width: 500,
                                                    autoScroll: true,          
                                                    closeAction: 'hide',     
                                                    title:'Отслеживание перемещения',
                                                    items:[
                                                            slider,
                                                            {
                                                                xtype:'label',
                                                                text: 'Нажмите на ползунок, для отображения метки, для удобства работают стрелочки слево, вправо'
                                                        
                                                            }]
                                                });
                                                win1.show();

                });
            },
            failure: function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }       
        });
    },
});