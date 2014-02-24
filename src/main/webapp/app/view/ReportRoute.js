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
                                var view  = "<table><tr>";
                                for (var i = 0; i <= data.items.items[0].columns.length-1; i++){
                                    view += "<td style='width:100px'>" + data.items.items[0].columns[i].text + "</td>"
                                }
                                view += "</tr>";
                                var view2 = data.items.items[0].body.dom.outerHTML;
                                newWin.document.write(view2);
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
                //async: false,
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
                    report.createReport(response.responseText);

                },
                failure:function () {
                    Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
                }      
        });
    },
    
     /*Формирование таблицы отчета*/
    createReport:function(data){
                var repId = Ext.getCmp('stateGrid');
                if (repId !== undefined)
                        //очищаем предыдущий компонент отчета
                    repId.destroy();
                var reportData = JSON.parse(data);
               
                var newData = Array();
                for (var j = 0; j <= reportData.length-1; j++){
                    newData[j] = new Array();
                    newData[j][0] = j + 1;
                    newData[j][1] = reportData[j].obj_name_;
                    newData[j][2] = reportData[j].proj_name_;
                   
                    var myDate = reportData[j].start_.replace(/(\d+)-(\d+)-(\d+)/, '$1/$2/$3');
                    var date = new Date(myDate.substring(0, myDate.length - 2));
                    newData[j][3] = datef("dd.MM.YYYY hh:mm", date);
                   
                    var myDate = reportData[j].end_.replace(/(\d+)-(\d+)-(\d+)/, '$1/$2/$3');
                    var date = new Date(myDate.substring(0, myDate.length - 2));
                    newData[j][4] = datef("dd.MM.YYYY hh:mm", date);
                   
                    newData[j][5] = reportData[j].rcount_;
                }
               
                    var store = Ext.create('Ext.data.ArrayStore', {
                    fields: [
                       {name: 'Number',   type:'float'},
                       {name: 'Obj_ID'},
                       {name: 'Proj_ID'},
                       {name: 'Start'},
                       {name: 'End'},
                       {name: 'Count',    type: 'float'}
                    ],
                    data: newData
                });
                // create the Grid
                var grid = Ext.create('Ext.grid.Panel', {
                    store: store,
                    id: 'stateGrid',
                    columns: [
                        {
                            text     : '№',
                            sortable : true,
                            dataIndex: 'Number'
                        },
                        {
                            text     : 'Автобус',
                            sortable : true,
                            dataIndex: 'Obj_ID'
                        },
                        {
                            text     : 'Перевозчик',
                            width    : 150,
                            sortable : true,
                            dataIndex: 'Proj_ID'
                        },
                        {
                            text     : 'Вышел на маршрут',
                            width    : 150,
                            sortable : true,
                            dataIndex: 'Start'
                        },
                        {
                            text     : 'Ушел с маршрута',
                            width    : 150,
                            sortable : true,
                            dataIndex: 'End'
                        },
                        {
                            text     : 'Количество рейсов',
                            sortable : true,                         
                            dataIndex: 'Count'
                        }
                    ]
                });
                var report = Ext.getCmp('reportRoute');
                report.add(grid);
                report.doLayout();
               
    }
});


