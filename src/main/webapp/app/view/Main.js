// main view
Ext.define('CWM.view.Main', {
    alias: 'widget.main', // alias (xtype)
    extend: 'Ext.panel.Panel',
    title: 'Панель управления МБУ ЦОДД "Веб карта" 3.0RC версия Текущий пользователь: ' + document.cookie.split(";")[0].split("=")[1],
    id:'main',
   // map instance
            yMap: null,

            // map cfg
            ymapConfig: {
                center: [51.7038, 39.1833],
                zoom: 12
            },

            initComponent: function () {
                var me = this;
                me.tbar = [
                    {
                        itemId: 'MainMenuItem',
                        id: 'MainMenuItem',
                        text: 'Автобусы',
                        menu:[]
                    },{
                        itemId: 'ReportItem',
                        text: 'Отчеты',
                        menu:[{
                                xtype:'button',
                                text:'Отчет по перевозчикам',
                                action: 'openReport'
                        },{
                                xtype:'button',
                                text:'Отчет по выходу ТС на маршрут',
                                action: 'openReportRoute'
                        },{
                                xtype:'button',
                                text:'Подробный отчет',
                                action: 'openDetailReport'
                        },{
                                xtype: 'button',
                                text: 'Контроль движения транспорта',
                                action: 'moveBusControl'
                        }]
                    },{
                        itemId: 'trajectory',
                        text: 'Траектория',
                        action: 'getRoute'
                    },{
                        itemId: 'lineChart',
                        text: 'График',
                        action: 'openChart'
                    },{
                        text: 'Маршруты',
                        itemId: 'routes',
                        menu: [{
                                xtype: 'button',
                                text: 'Посмотреть автобусы',
                                action: 'routes'
                                
                        },{
                                xtype: 'checkboxgroup',
                                itemId: 'checkboxes',
                                columns: 1,
                                vertical: true,
                                items: []
                        }]
                    },{
                        text: 'Поиск по автобусам',
                        itemId: 'searchBuses',
                        menu: [{
                                xtype: 'button',
                                text: ' Найти',
                                action: 'search'
                        },{
                                xtype: 'textfield',
                                itemId: 'searchBus',
                                allowBlank: false,
                                fieldLabel: 'Номер'
                        }]
                    },
                    {
                        itemId: 'traffic',
                        text: 'Пробки',
                        action: 'traffic'
                    },{
                        itemId: 'ExitItem',
                        text: 'Выход',
                        listeners:{
                            click:function(){
                                logout('session_id');
                                redirect('index.html');
                            }
                        }
                    }, {
                        xtype: 'label',
                        itemId: 'realCoords',
                        id: 'realCoords',
                        forId: 'myFieldId',
                        text: '',
                        margin: '0 0 0 10'
                    }
                ];
                me.yMapId = "map-canvas";
                me.on('boxready', me.createYMap);
                me.callParent(arguments);
            },
            
            /*Создание веб карты*/
            createYMap: function () {
                var me = this;
                me.update('<div style="width: ' + me.getEl().getWidth() + 'px; height: ' + me.getEl().getHeight() + 'px;" id="' + me.yMapId + '"></div>');
                  ymaps.ready(function () {

                            me.yMap = new ymaps.Map(document.getElementById(me.yMapId), me.ymapConfig, {projection: ymaps.projection.wgs84Mercator });
                            me.yMap.copyrights.add("Разработчик сервиса Адаменко Артем. МБУ ЦОДД 'Веб карта'.");
                            me.yMap.controls
                                // Кнопка изменения масштаба.
                                .add('zoomControl', { left: 5, top: 5 })
                                // Список типов карты
                                .add('typeSelector')
                                // Стандартный набор кнопок
                                .add('mapTools', { left: 35, top: 5 });
                        
                        me.yMap.events.add('mousemove', function (e) {
                            var label = Ext.getCmp("realCoords");
                            label.setText(e.get('coordPosition') + "");
                        });
                });
            }
});