Ext.define('CWM.view.ReportRoute', {
    alias: 'widget.reportRoute', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Отчет по выходу ТС на маршрут',
    width: 900,
    height: 700,
    id: 'reportRoute',
    config:{routes:''},
    
    constructor: function() {
        this.callParent(arguments);
    },
    
    initComponent: function(){
        var me = this;
  
        me.tbar = [{   
                        xtype: 'button',
                        text: 'Печать',
                    },{
                        xtype: 'combobox',
                        displayField:'route',
                        store:{
                            fields: ['route'],
                            data: me.routes
                        },
                        fieldLabel: 'Маршруты',
                        listeners:{
                            select: me.getReportRoute
                        }
                    },{
                        xtype: 'datefield',
                        anchor: '100%',
                        id: 'date',
                        value: new Date(),
                        maxValue: new Date()
                    }];
        // add items to view
        me.items = [];
        me.callParent(arguments);
    },
    
    getReportRoute: function(combo, records, eOpts){
        var date = datef("YYYY-MM-dd", Ext.getCmp('date').getValue());
        var report = Ext.getCmp('reportRoute');
        Ext.Ajax.request({
                url:'ReportRoute',
                method: 'POST',
                params: {route: records[0].data.route, date: date},
                success:function(response){
                    var report = Ext.getCmp('reportRoute');
                    var view = report.createReport(response.responseText);
                    var repId = Ext.getCmp('report');
                    if (repId !== undefined)
                        repId.destroy();
                    report.add({
                        title       : 'Отчет по выходу ТС на маршрут',
                        frame       : true,
                        collapsible : true,
                        id: 'report',
                        //collapsed   : true,
                        autoScroll : true,
                        html        : view,
                        //height:600
                    });
                report.doLayout();
                },
                failure:function () {
                    Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
                }      
        });
    },
    
     /*Формирование таблицы отчета*/
    createReport:function(data){
               var reportData = JSON.parse(data);
               //var header = "<div id='report_header'>Отчет по перевозчику «" + headData.NAME_ + "» по состоянию на «"+datef("YYYY.MM.dd hh:mm", currentDate)+"»<br>Всего записей " + reportData.length + "</div>";
               //var id = document.getElementById("report_header");
               //id.innerHTML=header;
               /*основной контент отчета*/  
               var view = ""; 
               view += '<table id="report_content" align="center" BORDER="1" cellpadding="0" cellspacing="0"><tr id="table_header"><td>№ п/п</td><td>Автобус</td><td>Перевозчик</td><td>Вышел на маршрут</td><td>Ушел с маршрута</td><td>Количество рейсов</td></tr>';
               var lastStationTime = "";
               var lastTime = "";
               for (var i = 0; i <= reportData.length-1; i++){
                    view += "<tr>";
                    var j = i;
                    view += "<td  id=\"obj_num\">" + ++j +"</td>";
                    view += "<td  id=\"obj_name\">" + reportData[i].obj_name_ +"</td>";
                    view += "<td  id=\"obj_cbname\">" + reportData[i].proj_name_ +"</td>";
                    view += "<td  id=\"obj_pvname\">" + reportData[i].start_ +"</td>";
                    view += "<td  id=\"obj_rname\">" + reportData[i].end_ +"</td>";
                    view += "<td  id=\"obj_rname\">" + reportData[i].rcount_ +"</td>";
                    
                    /*if (reportData[i].LAST_STATION_TIME_ !== undefined)
                        lastStationTime = datef("YYYY.MM.dd hh:mm",reportData[i].LAST_STATION_TIME_);
                    else
                        lastStationTime = "Дата неизвестна";
                    view += "<td id=\"obj_lastStationTime\">" + lastStationTime +"</td>";
                    if (reportData[i].LAST_TIME_ !== undefined)
                        lastTime = datef("YYYY.MM.dd hh:mm",reportData[i].LAST_TIME_);
                    else
                        lastTime = "Дата неизвестна";   
                    view += "<td id=\"obj_lastTime\">" + lastTime +"</td>";*/
                   view += "</tr>";
               }
               view +="</table>";
               return view;
    }
});


