Ext.define('CWM.view.WaitIntervals', {
    alias: 'widget.intervals',
    extend: 'Ext.window.Window',
    id: 'intervals',
    items:[],
    width: 600,
    height: 250,
    title: "Интервалы простоев",
    config:{
        inter: ""
    },
    constructor:function(){
        this.callParent(arguments);
    },
    
    initComponent: function(){
        var me = this; 
        me.items=[];
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
            store: store,
            id: 'intervalsGrid',
            columns: [
                        {
                            text     : 'Начало',
                            sortable : true,
                            dataIndex: 'startInterval',
                            width: 150
                        },
                        {
                            text     : 'Конец',
                            width    : 150,
                            sortable : true,
                            dataIndex: 'endInterval'
                        },
                        {
                            text     : 'Адрес',
                            width    : 250,
                            sortable : true,
                            dataIndex: 'address'
                        }
                    ]
            });
        intervalsComp.add(grid);
        intervalsComp.doLayout();
    }
    
});