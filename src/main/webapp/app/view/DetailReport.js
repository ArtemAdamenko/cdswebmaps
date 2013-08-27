Ext.define('CWM.view.DetailReport', {
    alias: 'widget.detailReport', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Подробный отчет',
    width: "60%",
    height: "70%",
    id: 'detailReport',
    layout: 'border',
    bodyBorder: false,
    config:{
        routes:''
    },
    
    constructor: function() {
        this.callParent(arguments);
    },
            
    initComponent: function () {
        var me = this; 
        var date = new Date();
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
                        },
                        labelWidth: 55,
                        id: 'combobox_routes'
                    },
                    {   
                        xtype: 'datefield',
                        fieldLabel:'Дата',
                        labelWidth: 35,
                        name: 'startDate',
                        id: 'startDate',
                       // vtype: 'daterange',
                        endDateField: 'endDate',
                        value: date
                    },{
                        xtype: 'timefield',
                        id: 'startTime',
                        fieldLabel:'Время',  
                        labelWidth: 30,
                        minValue: '00:00',
                        maxValue: '23:30',
                        format: 'H:i',
                        increment: 30,
                        value: date.getHours()-1 + ":" + date.getMinutes()
                    },{   
                        xtype: 'datefield',              
                        name: 'endDate',
                        id: 'endDate',
                       // vtype: 'daterange',
                        startDateField: 'startDate',
                        value: date
                    },{
                        xtype: 'timefield',
                        id: 'endTime',
                        minValue: '00:00',
                        maxValue: '23:30',
                        format: 'H:i',
                        increment: 30,
                        value: date.getHours() + ":" + date.getMinutes()
                    }
                ];
        // add items to view
        me.items = [];
        me.callParent(arguments);
    },
    
    //Построение списка автобусов с комбобоксами
    openTree: function(combo, records, eOpts){
        var route = records[0].data.route;
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
                
                var window = Ext.getCmp('detailReport');
                var treePanel = Ext.getCmp('tree_panel');
                /*Само дерево*/
                var panel = Ext.create('Ext.tree.Panel', {
                    title: 'Автобусы',
                    width: 200,
                    height: 350,
                    store: store,
                    rootVisible: false,
                    region: 'west',
                    id: 'tree_panel'
                });
                if (treePanel !== undefined){
                    window.remove('tree_panel');
                    window.doLayout();
                }
                window.add(panel);
                window.doLayout();
            }
    });},
    
    //Получение данных для подробного отчета
    constructDetailReport: function(){
        var win = Ext.getCmp('tree_panel');
        var records = win.getChecked(),
            names = new Array();
    
        //запись выбранных автобусов в массив
        Ext.Array.each(records, function(rec){
            names.push(new Object({
                            obj_id_: rec.raw.itemId,
                            proj_id_: rec.raw.name
                        }));
        });
        //записываем название маршрута
        var route = Ext.getCmp('combobox_routes').value;
        //выбранный интервл времени
        //начало по времени
        var fromTime = Ext.getCmp('startTime').value;
        fromTime.getHours() < 10? hour = "0" + fromTime.getHours() : hour = fromTime.getHours();
        fromTime.getMinutes() < 10? min = "0" + fromTime.getMinutes() : min = fromTime.getMinutes();
        fromTime = hour + ":" + min + ":00";
        //конец по времени
        var toTime = Ext.getCmp('endTime').value;
        toTime.getHours() < 10? hour = "0" + toTime.getHours() : hour = toTime.getHours();
        toTime.getMinutes() < 10? min = "0" + toTime.getMinutes() : min = toTime.getMinutes();
        toTime = hour + ":" + min + ":00";
        //начало по дате
        var fromDate = datef("YYYY-MM-dd",Ext.getCmp('startDate').value);
        //конец по дате
        var toDate = datef("YYYY-MM-dd",Ext.getCmp('endDate').value);
        
        //дата и время интервалов
        var from = fromDate + " " + fromTime;
        var to = toDate + " " + toTime;
        
        //выбранные автобусы
        var objects = Ext.JSON.encode(names);
        Ext.Ajax.request({
            url: 'DetailReport',
            method: 'POST',
            params:{
                objects: objects, 
                from: from, 
                to: to,
                route: route
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
                var detailReportCmp = Ext.getCmp("detailReport");
                var resultViewReport = detailReportCmp.createReport(routes, route, from, to);
                var reportViewCmp = Ext.getCmp('report');
                if (reportViewCmp !== undefined)
                    //очищаем предыдущий компонент отчета
                    reportViewCmp.destroy();
                var panel = Ext.create('Ext.panel.Panel', {
                    width: 200,
                    layout: 'fit',
                    region: 'center',
                    autoScroll : true,
                    margins: '5 0 0 0',
                    html: resultViewReport,
                    id: 'report'
                });
                detailReportCmp.add(panel);
                detailReportCmp.doLayout();

            },
            failure: function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }
        });
    },
    /*Формирование таблицы отчета*/
    createReport:function(data, route, from, to){
               var header = "<div id='report_header'>Подробный отчет о движении транспорта по маршруту №" + route + " за период времени с " + from + " по " + to + "</div>";
               /*основной контент отчета*/  
               var view = header;
               var bus = data[0].oname_;
               var j = 1;
               view += "<table id='report_content' align='center' BORDER='1' cellpadding='0' cellspacing='0'><tr><td colspan='4'>Данные о прохождении остановок транспортным средством с ГосНомером : " + bus + "</td></tr>";
               view += "<tr id='table_header'><td>№ п/п</td><td>Название остановки</td><td>Дата/время</td><td>Примечание</td></tr>";
               for (var i = 0; i <= data.length-1; i++){
                   if (data[i].oname_ !== bus){
                       view +="</table>";
                       bus = data[i].oname_;
                       view += "<table id='report_content' align='center' BORDER='1' cellpadding='0' cellspacing='0'><tr><td colspan='4'>Данные о прохождении остановок транспортным средством с ГосНомером : " + bus + "</td></tr>";
                       view += "<tr id='table_header'><td>№ п/п</td><td>Название остановки</td><td>Дата/время</td><td>Примечание</td></tr>";
                       j = 1;
                   }
                   view += "<tr>";
                   view += "<td  id=\"obj_num\">" + j++ +"</td>";
                   view += "<td  id=\"obj_name\">" + data[i].bsname_ +"</td>";
                   var myDate = data[i].dt.replace(/(\d+)-(\d+)-(\d+)/, '$1/$2/$3');
                   var date = new Date(myDate.substring(0, myDate.length - 2));
                   view += "<td  id=\"obj_cbname\">" +  datef("YYYY.MM.dd hh:mm:ss",date) +"</td>";
                   var bscontrol = "";
                   if (data[i].bscontrol_ === "1")
                       bscontrol = "Конечная";
                   else
                       bscontrol = "";
                   view += "<td  id=\"obj_pvname\">" + bscontrol + "</td>";
                   view +="</tr>";
               }
               return view;
    } 
});
