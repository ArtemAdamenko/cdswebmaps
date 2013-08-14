Ext.define('CWM.view.RouteOptions', {
    alias: 'widget.routeOptions', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Параметры маршрута',
    width: 200,
    height: 200,
    items: [],
    id: 'routes',
    config:{
        proj:'',
        obj:''
    },

      constructor: function() {
        this.callParent(arguments);
    },

    initComponent: function () {
        var date = new Date();
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
        var labelStart = {
            xtype: 'label',
            forId: 'myFieldId',
            text: 'Начало',
            margin: '0 0 0 10'
        };
        var labelEnd = {
            xtype: 'label',
            forId: 'myFieldId',
            text: 'Конец',
            margin: '0 0 0 10'
        };
        var datefieldFrom = {
            xtype: 'datefield',
            anchor: '100%',
            id: 'from_date',
            maxValue: new Date()
        };
        var datefieldTo = {
            xtype: 'datefield',
            anchor: '100%',
            id: 'to_date',
            value: new Date(),
            maxValue: new Date()
        };
        var timeFrom = Ext.create('Ext.form.field.Time', {
                id: 'from_time',
                minValue: '00:00',
                maxValue: '23:30',
                format: 'H:i',
                increment: 30
        });
        var timeTo = Ext.create('Ext.form.field.Time', {
                id: 'to_time',
                minValue: '00:00',
                maxValue: '23:30',
                format: 'H:i',
                increment: 30,
                value: date,
        });
        me.items.push(labelStart);
        me.items.push(datefieldFrom);
        me.items.push(timeFrom);
        me.items.push(labelEnd);
        me.items.push(datefieldTo);
        me.items.push(timeTo);
        me.callParent(arguments);

    },
    
    
    
    routing: function(){
        //Собираем данные для запроса 
        var widget = Ext.getCmp('routes');
        //начало интервала времени
        var from_date = datef("YYYY-MM-dd", Ext.getCmp('from_date').getValue());
        var from_time = Ext.getCmp('from_time').value;
        from_time.getHours() < 10? hour = "0" + from_time.getHours() : hour = from_time.getHours();
        from_time.getMinutes() < 10? min = "0" + from_time.getMinutes() : min = from_time.getMinutes();
        from_time = hour + ":" + min + ":00";debugger;
        //конец интервала
        var to_date = datef("YYYY-MM-dd", Ext.getCmp('to_date').getValue());
        var to_time = Ext.getCmp('to_time').value;
        to_time.getHours() < 10? hour = "0" + to_time.getHours() : hour = to_time.getHours();
        to_time.getMinutes() < 10? min = "0" + to_time.getMinutes() : min = to_time.getMinutes();
        to_time = hour + ":" + min + ":00";debugger;
        //создаем даты со временем
        var fullDateFrom = from_date + " " + from_time;
        var fullDateTo = to_date + " " + to_time;
        
        var myRoute;
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
                routes = JSON.parse(response.responseText);
                if (routes.length === 0){
                    Ext.MessageBox.alert('Ошибка', 'Данные отсутствуют на данный интервал времени');
                    return 0;
                }else{
                    document.getElementById("map-canvas").innerHTML = "";
                }
                ymaps.ready(function init() {
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
                            mass[i] =  new Object({
                                point: [convert(routes[i].LON_), convert(routes[i].LAT_)],
                                type: 'viaPoint'
                            });
                        }
                        ymaps.route(mass,{ mapStateAutoApply: true}).then(function (route) {
                            myRoute = route;
                            myMap.geoObjects.add(route);
                        });
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
                                                                myRoute && myMap.geoObjects.remove(myRoute);
                                                                myRoute = null;
                                                            }                                                
                                                            if (Placemarks.length !== 0){
                                                                //удаляем предыдущую метку ТС
                                                                myMap.geoObjects.remove(Placemarks[0]);
                                                                Placemarks = new Array();
                                                            }
                                                            var Placemark = new ymaps.Placemark(mass[value].point, {                                 
                                                                hintContent: routes[value].last_time_
                                                            }, {
                                                                // Своё изображение иконки метки.
                                                                iconImageHref: '/CdsWebMaps/images/car.png',
                                                                // Размеры метки.
                                                                iconImageSize: [40, 40],
                                                                // Смещение левого верхнего угла иконки относительно
                                                                // её "ножки" (точки привязки).
                                                                iconImageOffset: [-3, -42]
                                                            });
                                                            //Ставим метку положения ТС
                                                            Placemarks.push(Placemark);
                                                            myMap.geoObjects.add(Placemark);
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
    convertTime: function(time){
        var hours = parseInt(time / 60);
        if (hours < 10)
            hours = "0" + hours;
        var min = parseInt(time % 60);
        if (min < 10)
            min = "0" + min;
        return hours + ":" + min;
    }
    
    
});