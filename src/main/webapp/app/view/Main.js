// main view
Ext.define('CWM.view.Main', {
    alias: 'widget.main', // alias (xtype)
    extend: 'Ext.panel.Panel',
    title: 'Панель управления МБУ ЦДС "Веб карты"',
    
   // map instance
            yMap: null,

            // map cfg
            ymapConfig: {
                center: [51.7038, 39.1833], // default Москва
                zoom: 12,
                type: 'yandex#hybrid'
            },

            initComponent: function () {
                var me = this;
                me.tbar = [
                    {
                        itemId: 'MainMenuItem',
                        text: 'Маршруты',
                        menu:[]
                    },{
                        itemId: 'RouteItem',
                        text: 'Отобразить траекторию',
                        action: 'getRoute'
                    },{
                        itemId: 'ReportItem',
                        text: 'Отчеты',
                        //action: 'openReport',
                        menu:[{
                                xtype:'button',
                                text:'Отчет 1',
                                action: 'openReport',
                        },{
                                xtype:'button',
                                text:'Отчет 2',
                                action: 'openReport',
                        }]
                    },{
                        itemId: 'ExitItem',
                        text: 'Выход',
                    }
                ];
                me.yMapId = Ext.id();
                me.on('boxready', me.createYMap);
                me.callParent(arguments);
            },

            createYMap: function () {
                var me = this;

                me.update('<div style="width: ' + me.getEl().getWidth() + 'px; height: ' + me.getEl().getHeight() + 'px;" id="' + me.yMapId + '"></div>');
                ymaps.ready(function () {
                    me.yMap = new ymaps.Map(document.getElementById(me.yMapId), me.ymapConfig);
                    console.log('Map created: ', me.yMap);
                    me.yMap.controls
                        // Кнопка изменения масштаба.
                        .add('zoomControl', { left: 5, top: 5 })
                        // Список типов карты
                        .add('typeSelector')
                        // Стандартный набор кнопок
                        .add('mapTools', { left: 35, top: 5 });
                });
            }
});