Ext.define('CWM.view.DetailReport', {
    alias: 'widget.detailReport', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Подробный отчет',
    width: 900,
    height: 700,
    id: 'detailReport',
    config:{
        routes:''
    },
    
    constructor: function() {
        this.callParent(arguments);
    },
            
    initComponent: function () {
        var me = this; 
        me.tbar = [
                    {   
                        xtype: 'button',
                        text: 'Построить',
                        handler: me.constructDetailReport
                    },{
                        xtype: 'combobox',
                        displayField:'route',
                        store:{
                            fields: ['route'],
                            data: me.routes
                        },
                        fieldLabel: 'Маршруты',
                        listeners:{
                            select: me.openTree
                        }
                    },
                    {   
                        xtype: 'datefield',
                        fieldLabel:'Начало',
                        name: 'startDate',
                        id: 'startDate',
                       // vtype: 'daterange',
                        endDateField: 'endDate'
                    },{
                        xtype: 'timefield',
                        id: 'startTime',
                        //fieldLabel: 'Время',
                        minValue: '00:00',
                        maxValue: '23:30',
                        format: 'H:i',
                        increment: 30
                    },{   
                        xtype: 'datefield',
                        fieldLabel:'Конец',
                        name: 'endDate',
                        id: 'endDate',
                       // vtype: 'daterange',
                        startDateField: 'startDate',
                        //value: new Date()
                    },{
                        xtype: 'timefield',
                        id: 'endTime',
                        //fieldLabel: 'Время',
                        minValue: '00:00',
                        maxValue: '23:30',
                        format: 'H:i',
                        increment: 30
                    }
                ];
        // add items to view
        me.items = [];
        me.callParent(arguments);
    },
    
    //Построение списка автобусов с комбобоксами
    openTree: function(combo, records, eOpts){
        var route = records[0].data.route;debugger;
        var fromTime = Ext.getCmp('startTime').value;
        fromTime.getHours() < 10? hour = "0" + fromTime.getHours() : hour = fromTime.getHours();
        fromTime.getMinutes() < 10? min = "0" + fromTime.getMinutes() : min = fromTime.getMinutes();
        fromTime = hour + ":" + min + ":00";
        
        var toTime = Ext.getCmp('endTime').value;
        toTime.getHours() < 10? hour = "0" + toTime.getHours() : hour = toTime.getHours();
        toTime.getMinutes() < 10? min = "0" + toTime.getMinutes() : min = toTime.getMinutes();
        toTime = hour + ":" + min + ":00";
        
        
        var fromDate = datef("YYYY-MM-dd",Ext.getCmp('startDate').value);
        var toDate = datef("YYYY-MM-dd",Ext.getCmp('endDate').value);

        var from = fromDate + " " + fromTime;
        var to = toDate + " " + toTime;
        Ext.Ajax.request({
            url: 'GetRouteBuses',
            method: 'POST',
            params:{
                route: route, 
                from: from,
                to: to
            },
            success: function(response){
                if (response.responseText === undefined || response.responseText === null){
                    Ext.Msg.alert('Ошибка', 'Потеряно соединение с сервером');
                    return 0;
                }
                var buses =  JSON.parse(response.responseText);
                var objects = new Array();
                /*формируем объекты для дерева*/
                for (var i=0; i<= buses.length-1; i++) {
                    objects.push(new Object({
                        text: buses[i].name_,
                        itemId:buses[i].obj_id_,
                        name:buses[i].proj_id_,
                        leaf: true,
                        checked: true
                    }));                
                }
                /*store для дерева*/
                var store = Ext.create('Ext.data.TreeStore', {
                    root: {
                        expanded: true,
                        children: objects
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
                });
                var window = Ext.getCmp('chart');
                window.add(panel);
                window.doLayout();
            }
    })},
    
    constructDetailReport: function(){
        var win = Ext.getCmp('treePanel');
        var records = win.getChecked(),
            names = new Array();
                   
        Ext.Array.each(records, function(rec){
            names.push(new Object({
                            obj_id_: rec.get('itemId'),
                            proj_id_: rec.get('name')
                        }));
        });
        var fromTime = Ext.getCmp('startTime').value;
        fromTime.getHours() < 10? hour = "0" + fromTime.getHours() : hour = fromTime.getHours();
        fromTime.getMinutes() < 10? min = "0" + fromTime.getMinutes() : min = fromTime.getMinutes();
        fromTime = hour + ":" + min + ":00";
        
        var toTime = Ext.getCmp('endTime').value;
        toTime.getHours() < 10? hour = "0" + toTime.getHours() : hour = toTime.getHours();
        toTime.getMinutes() < 10? min = "0" + toTime.getMinutes() : min = toTime.getMinutes();
        toTime = hour + ":" + min + ":00";
        
        
        var fromDate = datef("YYYY-MM-dd",Ext.getCmp('startDate').value);
        var toDate = datef("YYYY-MM-dd",Ext.getCmp('endDate').value);
        var from = fromDate + " " + fromTime;
        var to = toDate + " " + toTime;
        Ext.Ajax.request({
            url: 'DetailReport',
            method: 'POST',
            params:{
                objects: JSON.create(names), 
                from: from, 
                to: to,
                route: names.projId
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

            },
            failure: function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }
        });
    },
    /*Формирование таблицы отчета*/
    createReport:function(data){
               var parseData = data.split("@");
               var headData = JSON.parse(parseData[0]);
               var currentDate = new Date();
               var reportData = JSON.parse(parseData[1]);
               var header = "<div id='report_header'>Отчет по перевозчику «" + headData.NAME_ + "» по состоянию на «"+datef("YYYY.MM.dd hh:mm", currentDate)+"»<br>Всего записей " + reportData.length + "</div>";
               /*основной контент отчета*/  
               var view = header; 
               view += '<table id="report_content" align="center" BORDER="1" cellpadding="0" cellspacing="0"><tr id="table_header"><td>№ п/п</td><td>ГосНомерТС</td><td>Марка ТС</td><td>Установщик</td><td>Маршрут следования</td><td>Время прохождения последней остановки</td><td>Время последнего отклика</td> </tr>';
               var lastStationTime = "";
               var lastTime = "";
               for (var i = 0; i <= reportData.length-1; i++){
                    view += "<tr>";
                    var j = i;
                    view += "<td  id=\"obj_num\">" + ++j +"</td>";
                    view += "<td  id=\"obj_name\">" + reportData[i].NAME_ +"</td>";
                    view += "<td  id=\"obj_cbname\">" + reportData[i].CBNAME_ +"</td>";
                    view += "<td  id=\"obj_pvname\">" + reportData[i].PVNAME +"</td>";
                    view += "<td  id=\"obj_rname\">" + reportData[i].RNAME_ +"</td>";
                    
                    if (reportData[i].LAST_STATION_TIME_ !== undefined)
                        lastStationTime = datef("YYYY.MM.dd hh:mm",reportData[i].LAST_STATION_TIME_);
                    else
                        lastStationTime = "Дата неизвестна";
                    view += "<td id=\"obj_lastStationTime\">" + lastStationTime +"</td>";
                    if (reportData[i].LAST_TIME_ !== undefined)
                        lastTime = datef("YYYY.MM.dd hh:mm",reportData[i].LAST_TIME_);
                    else
                        lastTime = "Дата неизвестна";   
                    view += "<td id=\"obj_lastTime\">" + lastTime +"</td>";
                   view += "</tr>";
               }
               view +="</table>";
               return view;
    }
    
});
