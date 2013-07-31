Ext.define('CWM.view.Report', {
    alias: 'widget.report', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Отчет',
    width: 900,
    height: 800,
    
    initComponent: function () {
        var me = this; 
        Ext.Ajax.request({
            url: 'Report',
            success: function(response){
                if (response.responseText === undefined || response.responseText === null){
                    Ext.Msg.alert('Ошибка', 'Потеряно соединение с сервером');
                    return 0;
                }
                var view = me.createReport(response.responseText);
                me.add({
                   //title       : 'Child number ',
                   //frame       : true,
                  // collapsible : true,
                   //collapsed   : true,
                   autoScroll: true,
                   html        : view
                });
                me.doLayout();

            },
            failure: function () {
                Ext.MessageBox.alert('Ошибка', 'Потеряно соединение с сервером');
            }
        });
              
        me.tbar = [
                    {   
                        xtype: 'button',
                        //itemId: 'print',
                        text: 'Печать',
                        //action: 'printReport',
                        listeners:{
                            click:function(){console.log("ddd");}
                        }
                    }
                ];
        // add items to view
        me.items = [];
        //me.Id = Ext.id();
        //me.Id.update("ddddd");
        me.callParent(arguments);
    },
    createReport:function(data){
               var parseData = data.split("@");
               var headData = JSON.parse(parseData[0]);
               var currentDate = new Date();
               var reportData = JSON.parse(parseData[1]);
               var header = "<div id='report_header'>Отчет по перевозчику «" + headData.NAME_ + "» по состоянию на «"+datef("YYYY.MM.dd hh:mm", currentDate)+"»<br>Всего записей " + reportData.length + "</div>";
               //var id = document.getElementById("report_header");
               //id.innerHTML=header;
               /*основной контент отчета*/  
               var view = header; 
               view += '<table id="report_content"><tr id="table_header"><td>№ п/п</td><td>ГосНомерТС</td><td>Марка ТС</td><td>Установщик</td><td>Маршрут следования</td><td>Время прохождения последней остановки</td><td>Время последнего отклика</td> </tr>';
               var lastStationTime = "";
               var lastTime = "";
               for (var i = 0; i <= reportData.length-1; i++){
                    view += "<tr>";
                    var j = i;
                    view += "<td  id=\"obj_num\">" + ++j +"</td>";
                    view += "<td  id=\"obj_name\">" + reportData[i].NAME_ +"</td>";
                    view += "<td  id=\"obj_cbname\">" + reportData[i].CBNAME_ +"</td>";
                    view += "<td  id=\"obj_pvname\">" + reportData[i].PVNAME +"</td>";
                    view += "<td  id=\"obj_rname\">" + reportData[i].RNAME_ +"</td>";
                    
                    if (reportData[i].LAST_STATION_TIME_ !== undefined)
                        lastStationTime = datef("YYYY.MM.dd hh:mm",reportData[i].LAST_STATION_TIME_);
                    else
                        lastStationTime = "Дата неизвестна";
                    view += "<td id=\"obj_lastStationTime\">" + lastStationTime +"</td>";
                    if (reportData[i].LAST_TIME_ !== undefined)
                        lastTime = datef("YYYY.MM.dd hh:mm",reportData[i].LAST_TIME_);
                    else
                        lastTime = "Дата неизвестна";   
                    view += "<td id=\"obj_lastTime\">" + lastTime +"</td>";
                   view += "</tr>";
               }
               view +="</table>";
               return view;
    }
    
});
