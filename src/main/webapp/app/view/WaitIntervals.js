Ext.define('CWM.view.WaitIntervals', {
    alias: 'widget.intervals',
    extend: 'Ext.window.Window',
    id: 'intervals',
    items:[],
    width: 1200,
    height: 350,
    title: "Интервалы простоев",
    config:{
        inter: "",
        from: "",
        to: ""
    },
    constructor:function(){
        this.callParent(arguments);
    },
    
    initComponent: function(){
        var me = this; 
        me.items=[];
        me.tbar = [{   
                        xtype: 'button',
                        text: 'Печать',
                        listeners:{
                            click:function(){
                                var data = Ext.getCmp("intervals");
                                var temp = data.getInter();
                                var from = data.getFrom();
                                var to = data.getTo();
                                
                                console.log(temp);
                                var newWin = window.open('','printWindow','Toolbar=0,Location=0,Directories=0,Status=0,Menubar=0,Scrollbars=0,Resizable=0'); 
                                newWin.document.open(); 
                                
                                var view  = "<table style='font-size:30px;width:1200px;font-weight:bold' align='center' BORDER='1' cellpadding='0' cellspacing='0'>";                             
                                view += "<tr><td colspan='4' align='center'>Отчет об остановках " + from + "--- " + to + "</td></tr>";
                                view += "<tr style= 'background-color:grey'><td>Начало</td><td>Конец</td><td>Адрес</td></tr>";
                                for (var i = 0; i <= temp.length-1; i++){
                                    view += "<tr>";
                                    
                                    var date = new Date(temp[i].startInterval);
                                    view += "<td style='width:100px'>" + datef("dd.MM.YYYY hh:mm:ss", date) + "</td>";
                                    
                                    var date = new Date(temp[i].endInterval);
                                    view += "<td style='width:100px'>" + datef("dd.MM.YYYY hh:mm:ss", date) + "</td>";
                                    
                                    view += "<td style='width:300px'>" + temp[i].address + "</td>";
                                    view += "</tr>";
                                }
                                view += "</table>";
                                newWin.document.write(view);
                                newWin.print();
                                newWin.document.close(); 
                            }
                        }
                    }];
        me.callParent(arguments);
    },
    waiting:function(){
        var intervalsComp = Ext.getCmp('intervals');
        var data = intervalsComp.getInter();
        var newData = new Array();
        for (var i = 0; i <= data.length-1; i++){
            newData[i] = new Array();
            
            var date = new Date(data[i].startInterval);
            newData[i][0] = datef("dd.MM.YYYY hh:mm:ss", date);
            
            var date = new Date(data[i].endInterval);
            newData[i][1] = datef("dd.MM.YYYY hh:mm:ss", date);
            newData[i][2] = data[i].address;
        }
        var store = Ext.create('Ext.data.ArrayStore', {
                    fields: [
                       {name: 'startInterval'},
                       {name: 'endInterval'},
                       {name: 'address'}
                    ],
                    data: newData
                });
        // create the Grid
        var grid = Ext.create('Ext.grid.Panel', {
            cls: 'custom-grid',
            store: store,
            id: 'intervalsGrid',
            columns: [
                        {
                            text     : 'Начало',
                            dataIndex: 'startInterval',
                            width: 250
                        },
                        {
                            text     : 'Конец',
                            width    : 250,
                            dataIndex: 'endInterval'
                        },
                        {
                            text     : 'Адрес',
                            width    : 700,
                            sortable : true,
                            dataIndex: 'address'
                        }
                    ]
            });
        intervalsComp.add(grid);
        intervalsComp.doLayout();
    }
    
});