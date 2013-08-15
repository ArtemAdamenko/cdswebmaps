Ext.define('CWM.view.MyChart', {
    alias: 'widget.mychart', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'График скорости',
    id:'chart',
    width: 1050,
    height: 600,
    layout: 'border',
    bodyBorder: false,
    config:{
        projId:'',
        objId:''
    },
    defaults: {
        collapsible: true,
        split: true,
        bodyPadding: 15
    },
    initComponent: function(){
        var me = this;
        var date = new Date();
        //var projId;
        //var objId;
        date.getHours() < 10? hour = "0" + date.getHours() : hour = date.getHours();
        date.getMinutes() < 10? min = "0" + date.getMinutes() : min = date.getMinutes();
        var time = hour + ":" + min;
        me.tbar = [{
                xtype: 'button',
                text: 'Построить',
                handler: me.constructChart
        },/*{
                itemId: 'buses',
                text: 'Автобусы',
                menu:[]
        },*/{
                xtype: 'timefield',
                id: 'from_time',
                fieldLabel: 'Время',
                minValue: '00:00',
                maxValue: '23:30',
                format: 'H:i',
                increment: 30
        },{
                xtype: 'timefield',
                id: 'to_time',
                minValue: '00:00',
                format: 'H:i',
                maxValue: '23:30',
                value: time,
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
                                        
                /*формируем объекты для дерева*/
                var tree = new Array();
                var childrens = new Array();
                var route_name_ = routes[0].route_name_;
                var parent = new Object({
                            text: route_name_, 
                            expanded: false
                        });
                for (var i=0; i<= routes.length-1; i++) {
                    if (route_name_ !== routes[i].route_name_){
                        route_name_ = routes[i].route_name_;
                        parent.children = childrens;
                        childrens = new Array();
                        tree.push(parent);
                        parent = new Object({
                            text: route_name_, 
                            expanded: false
                        });
                    }
                    childrens.push(new Object({
                        text: routes[i].name_,
                        itemId:routes[i].obj_id_,
                        name:routes[i].proj_id_,
                        leaf: true
                    }));
                    if (i === routes.length-1){
                        parent.children = childrens;
                        tree.push(parent);
                    }                  
                }
                /*store для дерева*/
                var store = Ext.create('Ext.data.TreeStore', {
                    root: {
                        expanded: true,
                        children: tree
                    }
                });
                /*Само дерево*/
                var panel = Ext.create('Ext.tree.Panel', {
                    title: 'Автобусы',
                    width: 200,
                    height: 350,
                    store: store,
                    rootVisible: false,
                    region:'west',
                    listeners: {
                            itemclick: function(view,rec,item,index,eventObj){
                                Ext.getCmp('chart').setProjId(rec.raw.name);
                                Ext.getCmp('chart').setObjId(rec.raw.itemId);
                            }
                        }
                });
                var window = Ext.getCmp('chart');
                window.add(panel);
                window.doLayout();

                /*var tbar = this.getDockedItems();
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
                }*/
            },
            failure: function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }
        });
        me.callParent(arguments);
    },
    
    //Построение диаграммы
    constructChart: function(){
        //получение интервала даты и времени
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
        
        //поиск выбранного автобуса из списка radio
        //var projId = Ext.ComponentQuery.query('menucheckitem[checked=true]')[0].name;
        //var objId = Ext.ComponentQuery.query('menucheckitem[checked=true]')[0].itemId;
        var projId = Ext.getCmp('chart').getProjId();
        var objId = Ext.getCmp('chart').getObjId();
        Ext.Ajax.request({
            url: 'GetSpeedBus',
            method: 'POST',
            params:{
                proj: projId, 
                obj: objId, 
                from_: from, 
                to_: to
            },
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
                            name: datef("dd.MM hh:mm", routes[i].time_), 
                            data: routes[i].speed_
                        })
                    );
                };
                var store = Ext.create('Ext.data.JsonStore', {
                    fields: ['name', 'data'],
                    data: data
                });
                var chart = Ext.create('Ext.chart.Chart', {
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
                                                tips: {
                                                    trackMouse: true,
                                                    width: 140,
                                                    height: 28,
                                                    renderer: function(storeItem, item) {
                                                      this.setTitle(storeItem.get('name') + ' ' + storeItem.get('data') + "КМ/Ч");
                                                    }
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
                                                }
                                            }]
                });
                var window = Ext.getCmp('chart');
                var panel = Ext.create('Ext.panel.Panel', {
                    width: 200,
                    layout: 'fit',
                    region: 'center',
                    margins: '5 0 0 0',
                });
                panel.add(chart);
                window.add(panel);
                window.doLayout();
            },
            failure: function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }
        });
    }
});


