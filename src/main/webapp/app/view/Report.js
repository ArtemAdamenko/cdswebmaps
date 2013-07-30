Ext.define('CWM.view.Report', {
    alias: 'widget.report', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Отчет',
    width: 600,
    height: 700,
    
    initComponent: function () {
        var me = this;       
        me.tbar = [
                    {   
                        xtype: 'button',
                        itemId: 'print',
                        text: 'Печать',
                        action: 'printReport',
                    }
                ];
        // add items to view
        me.items = [];
        
        me.callParent(arguments);
    }    
});
