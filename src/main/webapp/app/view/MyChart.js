Ext.define('CWM.view.MyChart', {
    alias: 'widget.mychart', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Диаграмма',
    id:'chart',
    width: 1050,
    height: 600,
    initComponent: function(){
        var me = this;
        var date = new Date();
        me.tbar = [{
                xtype: 'button',
                text: 'Построить',
                handler: me.constructChart
        },{
                itemId: 'buses',
                text: 'Автобусы',
                menu:[]
        },{
                xtype: 'timefield',
                id: 'from_time',
                fieldLabel: 'Время',
                minValue: '00:00',
                maxValue: '23:00',
                increment: 30
        },{
                xtype: 'timefield',
                id: 'to_time',
                minValue: '00:00',
                maxValue: '23:00',
                //value: date.getHours() + ":" + date.getMinutes(),
                increment: 30
        },{
                xtype: 'datefield',
                anchor: '100%',
                fieldLabel: 'Дата',
                id: 'from_date',
                maxValue: date
        },{
                xtype: 'datefield',
                anchor: '100%',
                id: 'to_date',
                value: date,
                maxValue: date
            }
        ];
        Ext.Ajax.request({
            url: 'GetBusesServlet',
            scope: this,
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

                var tbar = this.getDockedItems();
                // add menu items
                var route_name_ = routes[0].route_name_;
                var item = {text: route_name_};
                item.menu = [];
                //интервал времени для выделения активных автобусов
                var now = new Date().valueOf() - 600000;
                
                for (var i=0; i<= routes.length-1; i++) {
                    if (route_name_ !== routes[i].route_name_){
                        tbar[1].items.items[1].menu.add(item);
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
                                    itemId:routes[i].obj_id_,
                                    name:routes[i].proj_id_,
                                    cls: style
                                    });
                    if (i === routes.length-1)
                         tbar[1].items.items[1].menu.add(item);
                }
            },
            failure: function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }
        });
        me.callParent(arguments);
    },
    
    constructChart: function(){
        var fromTime = Ext.getCmp('from_time').value;
        fromTime.getHours() < 10? hour = "0" + fromTime.getHours() : hour = fromTime.getHours();
        fromTime.getMinutes() < 10? min = "0" + fromTime.getMinutes() : min = fromTime.getMinutes();
        fromTime = hour + ":" + min + ":00";
        
        var toTime = Ext.getCmp('to_time').value;
        toTime.getHours() < 10? hour = "0" + toTime.getHours() : hour = toTime.getHours();
        toTime.getMinutes() < 10? min = "0" + toTime.getMinutes() : min = toTime.getMinutes();
        toTime = hour + ":" + min + ":00";
        
        
        var fromDate = datef("YYYY-MM-dd",Ext.getCmp('from_date').value);
        var toDate = datef("YYYY-MM-dd",Ext.getCmp('to_date').value);
        
        var from = fromDate + " " + fromTime;
        var to = toDate + " " + toTime;
        
        
        var projId = Ext.ComponentQuery.query('menucheckitem[checked=true]')[0].name;
        var objId = Ext.ComponentQuery.query('menucheckitem[checked=true]')[0].itemId;debugger;
        
        Ext.Ajax.request({
            url: 'GetSpeedBus',
            method: 'POST',
            params:{proj: projId, obj: objId, from_: from, to_: to},
            success: function(response){
                if (response.responseText === undefined || response.responseText === null){
                    Ext.Msg.alert('Ошибка', 'Потеряно соединение с сервером');
                    return 0;
                }
                var routes =  JSON.parse(response.responseText);
                if (routes.length === 0){
                    Ext.Msg.alert('Предупреждение', 'Данные пусты');
                    return 0;
                }
                var data = new Array();
                if (routes.length-1 < 100)
                    var temp = 1;
                else
                    var temp = Math.floor((routes.length-1) / 100);
                for (var i = 0; i <= routes.length-1; i = i + temp){
                    data.push(new Object({
                            name: datef("MM.dd hh:mm", routes[i].time_), 
                            data: routes[i].speed_
                        })
                    );
                }
                var store = Ext.create('Ext.data.JsonStore', {
                    fields: ['name', 'data'],
                    data: data
                });
                var chart = Ext.create('Ext.chart.Chart', {
                                        renderTo: Ext.getBody(),
                                        width: 1050,
                                        height: 550,
                                        animate: true,
                                        itemId:'speedChart',
                                        id:'speedChart',
                                        store: store,
                                        axes: [
                                            {
                                                type: 'Numeric',
                                                position: 'left',
                                                fields: ['data'],
                                                /*label: {
                                                    renderer: Ext.util.Format.numberRenderer('0,0')
                                                }*/
                                                title: 'Скорость',
                                                grid: true,
                                                minimum: 0
                                            },{
                                                type: 'Category',
                                                position: 'bottom',
                                                fields: ['name'],
                                                title: 'Дата'
                                            }
                                        ],
                                        series: [
                                            {
                                                type: 'line',
                                                highlight: {
                                                    size: 7,
                                                    radius: 7
                                                },
                                                axis: 'left',
                                                smooth: true,
                                                fill: true,
                                                xField: 'name',
                                                yField: 'data',
                                                markerConfig: {
                                                    type: 'circle',
                                                    size: 4,
                                                    radius: 4,
                                                    'stroke-width': 0
                                                },
                                                listener:{
                                                    click:function(el){
                                                        console.log("ddd");
                                                }
                                            }
                                            }]
                });
                var window = Ext.getCmp('chart');
                var oldChart = Ext.getCmp('speedChart');debugger;
                //oldChart.destroy();
                window.add(chart);
                window.doLayout();
            },
            failure: function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }
        });
    }
});


