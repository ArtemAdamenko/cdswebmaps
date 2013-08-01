Ext.define('CWM.view.RouteOptions', {
    alias: 'widget.routeOptions', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Параметры маршрута',
    width: 300,
    height: 200,
    items: [],
    id: 'routes',

    initComponent: function () {
        
        var me = this; 
        me.from_time=0;
        me.to_time=0;
        me.tbar=[{
            xtype:'button',
            text:'Проложить маршрут',
            listeners:{
                    click: me.routing
            }
        }];
        var datefieldFrom = {
            xtype: 'datefield',
            anchor: '100%',
            fieldLabel: 'Начало',
            id: 'from_date',
            maxValue: new Date()  // limited to the current date or prior
        };
        var datefieldTo = {
            xtype: 'datefield',
            anchor: '100%',
            fieldLabel: 'Конец',
            id: 'to_date',
            maxValue: new Date()  // limited to the current date or prior
        };
        
        var timeFrom = Ext.create('Ext.slider.Single', {
            renderTo: Ext.getBody(),
            hideLabel: true,
            width: 250,
            increment: 1,
            minValue: 0,
            id: 'from_time',
            maxValue: 1440,
            tipText: function(thumb){
                me.from_time = me.convertTime(thumb.value);
                return Ext.String.format('<b>{0}</b>', me.from_time);
            }
        });
        
        var timeTo = Ext.create('Ext.slider.Single', {
            renderTo: Ext.getBody(),
            hideLabel: true,
            width: 250,
            increment: 1,
            minValue: 0,
            id: 'to_time',
            maxValue: 1440,
            tipText: function(thumb){   
                me.to_time = me.convertTime(thumb.value);
                return Ext.String.format('<b>{0}</b>', me.to_time);
            }
        });
        me.items.push(datefieldFrom);
        me.items.push(timeFrom);
        me.items.push(datefieldTo);
        me.items.push(timeTo);
        me.callParent(arguments);

    },
    
    routing: function(){
        var widget = Ext.getCmp('routes');
        /*начало интервала времени*/
        var from_date = datef("YYYY-MM-dd", Ext.getCmp('from_date').getValue());
        var from_time = widget.from_time + ":00";
        /*конец интервала*/
        var to_date = datef("YYYY-MM-dd", Ext.getCmp('to_date').getValue());debugger;   
        var to_time = widget.to_time + ":00";
        /*создаем даты со временем*/
        var fullDateFrom = from_date + " " + from_time;
        var fullDateTo = to_date + " " + to_time;

        var menu = this.findParentByType('main');debugger; 
        var men;debugger; 
    },
    convertTime: function(time){
        var hours = parseInt(time / 60);
        if (hours < 10)
            hours = "0" + hours;
        var min = parseInt(time % 60);
        if (min < 10)
            min = "0" + min;
        return hours + ":" + min;
    }
    
    
});