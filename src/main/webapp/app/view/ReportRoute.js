Ext.define('CWM.view.ReportRoute', {
    alias: 'widget.reportRoute', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Отчет по выходу ТС на маршрут',
    width: 900,
    height: 700,
    id: 'reportRoute',
    config:{
        routes:''
    },
    
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
        var route = records[0].data.route;
        Ext.Ajax.request({
                url:'ReportRoute',
                method: 'POST',
                params: {
                    route: route, 
                    date: date
                },
                success:function(response){
                    var report = Ext.getCmp('reportRoute');
                    var view = report.createReport(response.responseText);
                    var repId = Ext.getCmp('report');
                    if (repId !== undefined)
                        //очищаем предыдущий компонент отчета
                        repId.destroy();
                    report.add({
                        title       : "Отчет по автобусам на маршруте за день «" + route + "» "+datef("YYYY.MM.dd hh:mm", date),
                        frame       : true,
                        collapsible : true,
                        id: 'report',
                        autoScroll : true,
                        html        : view,
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
               /*основной контент отчета*/  
               var view = '<table id="report_content" align="center" BORDER="1" cellpadding="0" cellspacing="0"><tr id="table_header"><td>№ п/п</td><td>Автобус</td><td>Перевозчик</td><td>Вышел на маршрут</td><td>Ушел с маршрута</td><td>Количество рейсов</td></tr>';
               for (var i = 0; i <= reportData.length-1; i++){
                    view += "<tr>";
                    var j = i;
                    view += "<td  id=\"obj_num\">" + ++j +"</td>";
                    view += "<td  id=\"obj_name\">" + reportData[i].obj_name_ +"</td>";
                    view += "<td  id=\"obj_cbname\">" + reportData[i].proj_name_ +"</td>";
                    view += "<td  id=\"obj_pvname\">" + reportData[i].start_ +"</td>";
                    view += "<td  id=\"obj_rname\">" + reportData[i].end_ +"</td>";
                    view += "<td  id=\"obj_rname\">" + reportData[i].rcount_ +"</td>";
                   view += "</tr>";
               }
               view +="</table>";
               return view;
    }
});

