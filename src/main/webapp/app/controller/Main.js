Ext.define('CWM.controller.Main', {
    extend: 'Ext.app.Controller',
    views: ['CWM.view.Main','CWM.view.Report','CWM.view.RouteOptions', 'CWM.view.ReportRoute'],
    refs: [
        {ref: 'MainView', selector: 'main'} // Reference to main view
    ],

    init: function() {        
        var me = this;
        //регистрация кнокпи на функцию
        //Отчет по перевозчикам
        me.control({'button[action=openReport]':{
                            click: me.openReport
                }
        });
        //отслеживание маршрута
        me.control({'button[action=getRoute]':{
                            click: me.getRoute
                }
        });
        //отчет по рейсам
        me.control({'button[action=openReportRoute]':{
                            click: me.openReportRoute
                }
        })
        //запрос на автобусы для отображения на карте
        Ext.Ajax.request({
            url: 'GetBusesServlet',
            success: function(response){
                if (response.responseText === undefined || response.responseText === null){
                    Ext.Msg.alert('Ошибка', 'Потеряно соединение с сервером');
                    return 0;
                }
                var routes =  JSON.parse(response.responseText);  
                routes = routes.sort(function(obj1, obj2) {
                                        // Сортировка по возрастанию
                                        return obj1.route_name_.localeCompare(obj2.route_name_);
                                        });
                var w = me.getMainView(),
                t = w.down('#MainMenuItem');
                
                // add menu items
                var route_name_ = routes[0].route_name_;
                var item = {text: route_name_};
                item.menu = [];
                for (i=0; i<= routes.length-1; i++) {
                    if (route_name_ !== routes[i].route_name_){
                        t.menu.add(item);
                        route_name_ = routes[i].route_name_;
                        var item = {text: route_name_};    
                        item.menu = [];
                    }
                    item.menu.push({text: routes[i].name_,
                                    checked: false,
                                    group: route_name_,
                                    handler:me.getRoute,
                                    itemId:routes[i].obj_id_,
                                    name:routes[i].proj_id_
                                    
                    });
                    if (i === routes.length-1)
                         t.menu.add(item);
                }
            },
            failure: function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }
        });
    },

    //Открытие окна с отчетом по перевозчикам
    openReport: function(){
        var win = Ext.widget('report');
        win.show();
    },
    
    getRoute: function(btn){
        var win = Ext.widget('routeOptions',{proj:btn.name, obj:btn.itemId});
        win.show();
    },
    //вызов компонента отчета по рейсам
    openReportRoute: function(){
        var me = this;
        var w = me.getMainView(),
        menu = w.down('#MainMenuItem');
        var items = new Array();
        //Формируем все маршруты текущего перевозчика
        for (var i = 0; i <= menu.menu.items.items.length-1; i++)
            items.push({route:menu.menu.items.items[i].text});
        var reportWin = Ext.widget('reportRoute', {routes:items});
        reportWin.show();
        
    }
});