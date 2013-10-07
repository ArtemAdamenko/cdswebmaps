Ext.define('CWM.view.ReportRoute', {
    alias: 'widget.reportRoute', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Отчет по выходу ТС на маршрут',
    width: "50%",
    height: "70%",
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
                        listeners:{
                            click:function(){
                                var data = Ext.getCmp("reportRoute");
                                var newWin = window.open('','printWindow','Toolbar=0,Location=0,Directories=0,Status=0,Menubar=0,Scrollbars=0,Resizable=0'); 
                                newWin.document.open(); 
                                newWin.document.write(data.body.el.dom.childNodes[0].innerHTML); 
                                newWin.print();
                                newWin.document.close(); 
                            }
                        }
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
                        },
                        id: 'routes'
                    },{
                        xtype: 'datefield',
                        anchor: '100%',
                        id: 'date',
                        listeners:{
                            select: me.getReportRoute
                        },
                        value: new Date(),
                        maxValue: new Date()
                    }];
        // add items to view
        me.items = [];
        me.callParent(arguments);
    },
    
    getReportRoute: function(combo, records, eOpts){
        var date = datef("YYYY-MM-dd", Ext.getCmp('date').getValue());
        var route = Ext.getCmp("routes").rawValue;
        Ext.Ajax.request({
                url:'ReportRoute',
                method: 'POST',
                params: {
                    route: route, 
                    date: date
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
                    var myDate = reportData[i].start_.replace(/(\d+)-(\d+)-(\d+)/, '$1/$2/$3');
                    var date = new Date(myDate.substring(0, myDate.length - 2));
                    view += "<td  id=\"obj_pvname\">" + datef("dd.MM.YYYY hh:mm", date) +"</td>";
                    myDate = reportData[i].end_.replace(/(\d+)-(\d+)-(\d+)/, '$1/$2/$3');
                    date = new Date(myDate.substring(0, myDate.length - 2));
                    view += "<td  id=\"obj_rname\">" + datef("dd.MM.YYYY hh:mm", date) +"</td>";
                    view += "<td  id=\"obj_rname\">" + reportData[i].rcount_ +"</td>";
                   view += "</tr>";
               }
               view +="</table>";
               return view;
    }
});


