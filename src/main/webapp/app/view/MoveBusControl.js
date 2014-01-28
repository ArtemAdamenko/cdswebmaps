Ext.define('CWM.view.MoveBusControl', {
    alias: 'widget.moveBusControl', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Контроль Движения Транспорта',
    width: "60%",
    height: "70%",
    id: 'moveBusControl',
    layout: 'border',
    bodyBorder: false,
    config:{
        routes:'',
        numberStation:'',
        stationName: ''
    },
    
    constructor: function() {
        this.callParent(arguments);
    },
            
    initComponent: function () {
        var me = this; 
        var date = new Date();
        var time = parseTime(date);
        me.tbar = [
                    {   
                        xtype: 'button',
                        text: 'Построить',
                        handler: me.constructMoveBusControl
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
                        },
                        labelWidth: 55,
                        id: 'combobox_routes'
                    },
                    {   
                        xtype: 'datefield',
                        fieldLabel:'Начало',
                        labelWidth: 35,
                        name: 'startDate',
                        id: 'startDate',
                        endDateField: 'endDate',
                        value: date
                    },{
                        xtype: 'timefield',
                        id: 'startTime',
                        labelWidth: 30,
                        minValue: '00:00',
                        maxValue: '23:30',
                        format: 'H:i:s',
                        increment: 30,
                        value: time
                    },{   
                        xtype: 'datefield',
                        fieldLabel:'Конец',
                        name: 'endDate',
                        id: 'endDate',
                        startDateField: 'startDate',
                        value: date
                    },{
                        xtype: 'timefield',
                        id: 'endTime',
                        minValue: '00:00:00',
                        maxValue: '23:30:00',
                        format: 'H:i:s',
                        increment: 30,
                        value: time
                    }
                ];
        // add items to view
        me.callParent(arguments);
    },
    
    //Построение списка автобусов с комбобоксами
    openTree: function(combo, records, eOpts){
        var routeName = records[0].data.route;

        Ext.Ajax.request({
            url: 'GetBusStations',
            method: 'POST',
            params:{
                routeName: routeName
            },
            success: function(response){
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
                var allStations =  JSON.parse(response.responseText);
                
                //получаем список конечных для маршрута, остановки должны быть отсортированы
                var controls = new Array();
                for (var i = 0; i <= allStations.length-1; i++){
                    if (allStations[i].Control === 1){
                        controls.push(allStations[i]);
                    }
                }
                     
                var objects = new Array();               
                //формируем объекты для дерева
                //достаем попарно конечные
                //те остановки что между конечными, проходит ТС
                for (var i=0; i <= controls.length-1; i = i+2) {
                    //собираем все остановки от начала маршрута до конца
                    var childrens = new Array();
                    for (var j = controls[i].Number-1; j <= controls[i+1].Number-1; j++){
                        childrens.push(new Object({
                            text: allStations[j].Name,
                            itemId:allStations[j].Number,
                            leaf: true
                        }));
                    };
                    //обозначаем начало маршрута и конец и закидываем все его остановки
                    objects.push(new Object({
                        text: controls[i].Name + " => " + controls[i+1].Name,
                        children: childrens
                    }));                
                }
                /*store для дерева*/
                var store = Ext.create('Ext.data.TreeStore', {
                    root: {
                        expanded: false,
                        children: objects
                    }
                });
                
                var window = Ext.getCmp('moveBusControl');
                var treePanel = Ext.getCmp('stations');
                /*Само дерево*/
                var panel = Ext.create('Ext.tree.Panel', {
                    title: 'Остановки',
                    width: 200,
                    height: 350,
                    store: store,
                    rootVisible: false,
                    region: 'west',
                    id: 'stations',
                    listeners: {
                            itemclick: function(view,rec,item,index,eventObj){
                                Ext.getCmp('moveBusControl').setNumberStation(rec.raw.itemId);
                                Ext.getCmp('moveBusControl').setStationName(rec.raw.text);
                            }
                        }
                });
                if (treePanel !== undefined){
                    window.remove('stations');
                    window.doLayout();
                }
                window.add(panel);
                window.doLayout();
            },
            failure:function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }      
    });},
    
    //Получение данных для подробного отчета
    constructMoveBusControl: function(){
        var stationID = Ext.getCmp('moveBusControl').getNumberStation();
        //записываем название маршрута
        var route = Ext.getCmp('combobox_routes').value;
        //выбранный интервал времени
        var fromTime = parseTime(Ext.getCmp('startTime').value);
        var toTime = parseTime(Ext.getCmp('endTime').value);
        
        var fromDate = datef("YYYY-MM-dd",Ext.getCmp('startDate').value);
        var toDate = datef("YYYY-MM-dd",Ext.getCmp('endDate').value);
        
        var from = fromDate + " " + fromTime;
        var to = toDate + " " + toTime;
       
        Ext.Ajax.request({
            url: 'GetMoveBusStationReport',
            method: 'POST',
            params:{
                station: stationID, 
                from: from, 
                to: to,
                routeName: route
            },
            success: function(response){
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
                var data =  JSON.parse(response.responseText);
                var cmp = Ext.getCmp("moveBusControl");
                var reportView = cmp.createReport(data, route, from, to, cmp.getStationName());
                var reportViewCmp = Ext.getCmp('moveBusControlReport');
                if (reportViewCmp !== undefined)
                    //очищаем предыдущий компонент отчета
                    reportViewCmp.destroy();
                var panel = Ext.create('Ext.panel.Panel', {
                    width: 200,
                    layout: 'fit',
                    region: 'center',
                    autoScroll : true,
                    margins: '5 0 0 0',
                    html: reportView,
                    id: 'moveBusControlReport'
                });
                cmp.add(panel);
                cmp.doLayout();
                
            },
            failure: function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }
        });
    },
    /*Формирование таблицы отчета*/
    createReport:function(data, route, from, to, stationName){
               var header = "<div id='report_header'>Отчет о прохождении остановки " + stationName + " маршрутным транспортом за период времени с " + from + " по " + to + "</div>";
               /*основной контент отчета*/  
               var view = header;
               var j = 1;
               
               view += "<table id='report_content' align='center' BORDER='1' cellpadding='0' cellspacing='0'><tr><td colspan='5'>Маршрут '" + route + "' ('" + stationName + "')</td></tr>";
               view += "<tr id='table_header'><td>№ п/п</td><td>ГосНомер ТС</td><td>Время прохождения</td></tr>";
               for (var i = 0; i <= data.length-1; i++){
                   view += "<tr>";
                   view += "<td  id=\"obj_num\">" + j++ +"</td>";
                   view += "<td  id=\"obj_name\">" + data[i].ObjectName +"</td>";
                   var myDate = data[i].Time_.replace(/(\d+)-(\d+)-(\d+)/, '$1/$2/$3');
                   var date = new Date(myDate.substring(0, myDate.length - 2));
                   view += "<td  id=\"obj_cbname\">" +  datef("dd.MM.YYYY hh:mm:ss",date) +"</td>";
                   view +="</tr>";
               }
               return view;
    } 
});
