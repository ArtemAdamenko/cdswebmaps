Ext.define('CWM.view.RouteOptions', {
    alias: 'widget.routeOptions', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Параметры маршрута',
    width: 300,
    height: 200,
    items: [],
    id: 'routes',
    config:{
        proj:'',
        obj:'',
        fromTime:'',
        toTime:''
    },

      constructor: function() {
        this.callParent(arguments);
    },

    initComponent: function () {
        
        var me = this; 
        me.from_time=0;
        me.to_time=0;
        me.tbar=[{
            xtype:'button',
            text:'Проложить маршрут',
            listeners:{
                    click: me.routing
            }
        }];
        var datefieldFrom = {
            xtype: 'datefield',
            anchor: '100%',
            fieldLabel: 'Начало',
            id: 'from_date',
            maxValue: new Date()
        };
        var datefieldTo = {
            xtype: 'datefield',
            anchor: '100%',
            fieldLabel: 'Конец',
            id: 'to_date',
            value: new Date(),
            maxValue: new Date()
        };
        
        var timeFrom = Ext.create('Ext.slider.Single', {
            renderTo: Ext.getBody(),
            hideLabel: true,
            width: 250,
            increment: 1,
            minValue: 0,
            id: 'from_time',
            maxValue: 1440,
            value: 0,
            tipText: function(thumb){
                return Ext.String.format('<b>{0}</b>', me.convertTime(thumb.value));
            },
            listeners:{
                changecomplete:function(slider, newValue, thumb, eOpts){
                    me.setFromTime(me.convertTime(newValue));
                }
            }
        });
        
        var timeTo = Ext.create('Ext.slider.Single', {
            renderTo: Ext.getBody(),
            labelEl:'Интервал времени',
            width: 250,
            increment: 1,
            minValue: 0,
            id: 'to_time',
            maxValue: 1440,
            value: "00:00",
            tipText: function(thumb){   
                return Ext.String.format('<b>{0}</b>', me.convertTime(thumb.value));
            },
            listeners:{
                changecomplete:function( slider, newValue, thumb, eOpts ){
                    var time = me.convertTime(newValue);
                    me.setToTime(time);
                }
            }
                    
        });
        me.items.push(datefieldFrom);
        me.items.push(timeFrom);
        me.items.push(datefieldTo);
        me.items.push(timeTo);
        me.callParent(arguments);

    },
    
    routing: function(){
        //Собираем данные для запроса 
        var widget = Ext.getCmp('routes');
        //начало интервала времени
        var from_date = datef("YYYY-MM-dd", Ext.getCmp('from_date').getValue());
        var from_time = widget.getFromTime() + ":00";
        //конец интервала
        var to_date = datef("YYYY-MM-dd", Ext.getCmp('to_date').getValue());
        if (widget.getToTime() === 0)
            widget.setToTime('00:00');
        var to_time = widget.getToTime() + ":00";
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
            params: { proj:widget.getProj(),bus:widget.getObj(),fromTime:fullDateFrom, toTime:fullDateTo},
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