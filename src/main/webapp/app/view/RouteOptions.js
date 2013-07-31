Ext.define('CWM.view.RouteOptions', {
    alias: 'widget.routeOptions', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Параметры маршрута',
    width: 300,
    height: 200,
    items: [],

    initComponent: function () {
        var me = this; 
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
            name: 'from_date',
            maxValue: new Date()  // limited to the current date or prior
        }
        var datefieldTo = {
            xtype: 'datefield',
            anchor: '100%',
            fieldLabel: 'Конец',
            name: 'to_date',
            maxValue: new Date()  // limited to the current date or prior
        }
        
        var timeFrom = Ext.create('Ext.slider.Single', {
            renderTo: Ext.getBody(),
            hideLabel: true,
            width: 250,
            increment: 1,
            minValue: 0,
            name: 'from_time',
            maxValue: 1440,
            tipText: function(thumb){
                var hours = parseInt(thumb.value / 60);
                var min = parseInt(thumb.value % 60);
                return Ext.String.format('<b>{0}:{1}</b>', hours, min);
            }
        });
        
        var timeTo = Ext.create('Ext.slider.Single', {
            renderTo: Ext.getBody(),
            hideLabel: true,
            width: 250,
            increment: 1,
            minValue: 0,
            name: 't_time',
            maxValue: 1440,
            tipText: function(thumb){
                var hours = parseInt(thumb.value / 60);
                var min = parseInt(thumb.value % 60);
                return Ext.String.format('<b>{0}:{1}</b>', hours, min);
            }
        });
        me.items.push(datefieldFrom);
        me.items.push(timeFrom);
        me.items.push(datefieldTo);
        me.items.push(timeTo)
        me.callParent(arguments);

    },
    
    routing: function(){
        console.log("routing");
    }
});