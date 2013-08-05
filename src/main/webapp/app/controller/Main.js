Ext.define('CWM.controller.Main', {
    extend: 'Ext.app.Controller',
    views: ['CWM.view.Main','CWM.view.Report','CWM.view.RouteOptions'],
    refs: [
        {ref: 'MainView', selector: 'main'} // Reference to main view
    ],

    init: function() {        
        var me = this;
        var itemId = '';
        var name = '';
        /*регистрация кнокпи на функцию*/
        me.control({'button[action=openReport]':{
                            click: me.openReport
                        }
                 });
        me.control({'button[action=getRoute]':{
                            click: me.getRoute
                        }
                 });
        /*запрос на автобусы для отображения на карте*/
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
                   // get test menu button
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

    /*Открытие окна с отчетом*/
    openReport: function(btn){
        var win = Ext.widget('report');
        win.show();
    },
    onPanelRendered: function() {        
    },
    
    getRoute: function(btn){
        var me = this;
        var win = Ext.widget('routeOptions',{proj:btn.name, obj:btn.itemId});
        win.show();
    },
});