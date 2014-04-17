Ext.define('CWM.view.Report', {
    alias: 'widget.report', // alias (xtype)
    extend: 'Ext.window.Window',
    title: 'Отчет',
    width: "50%",
    height: "70%",
    id: 'report',
    
    initComponent: function () {
        var me = this; 
        Ext.Ajax.request({
            url: 'Report',
            success: function(response){

                var ERROR = checkResponseServer(response);
                if (ERROR){
                    Ext.Msg.alert('Ошибка', ERROR);
                    return 0;
                        }
                var view = me.createReport(response.responseText);
                me.add({
                   title       : 'Отчет по перевозчикам',
                   frame       : true,
                   collapsible : true,
                   autoScroll : true,
                   html        : view,
                   height:  600,
                   id: 'reportData'
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
                        text: 'Печать',
                        listeners:{
                            click:function(){
                                var data = Ext.getCmp("reportData");
                                var newWin = window.open('','printWindow','Toolbar=0,Location=0,Directories=0,Status=0,Menubar=0,Scrollbars=0,Resizable=0'); 
                                newWin.document.open(); 
                                newWin.document.write(data.body.el.dom.childNodes[0].innerHTML); 
                                newWin.print();
                                newWin.document.close(); 
                            }
                        }
                    }
                ];
        // add items to view
        me.items = [];
        me.callParent(arguments);
    },
    /*Формирование таблицы отчета*/
    createReport:function(data){
               var parseData = data.split("@");
               var headData = JSON.parse(parseData[0]);
               var currentDate = new Date();
               var reportData = JSON.parse(parseData[1]);
               var header = "<div id='report_header'>Отчет по перевозчику «" + headData.NAME_ + "» по состоянию на «"+datef("YYYY.MM.dd hh:mm", currentDate)+"»<br>Всего записей " + reportData.length + "</div>";
               /*основной контент отчета*/  
               var view = header; 
               view += '<table id="report_content" align="center" BORDER="1" cellpadding="0" cellspacing="0"><tr id="table_header"><td>№ п/п</td><td>ГосНомерТС</td><td>Марка ТС</td><td>Установщик</td><td>Маршрут следования</td><td>Время последнего отклика</td><td>Время прохождения последней остановки</td> </tr>';
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
                    if (reportData[i].LAST_TIME_ !== undefined)
                        lastTime = datef("dd.MM.YYYY hh:mm",reportData[i].LAST_TIME_);
                    else
                        lastTime = "Дата неизвестна";   
                    view += "<td id=\"obj_lastTime\">" + lastTime +"</td>";
                    if (reportData[i].LAST_STATION_TIME_ !== undefined)
                        lastStationTime = datef("dd.MM.YYYY hh:mm",reportData[i].LAST_STATION_TIME_);
                    else
                        lastStationTime = "Дата неизвестна";
                    view += "<td id=\"obj_lastStationTime\">" + lastStationTime +"</td>";
                   view += "</tr>";
               }
               view +="</table>";
               return view;
    }
    
});
