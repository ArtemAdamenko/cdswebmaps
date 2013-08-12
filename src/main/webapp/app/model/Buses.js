 Ext.define('CWM.model.Buses', {
     extend: 'Ext.data.Model',
     fields: [
         {name: 'last_time_', type: 'string'},
         {name: 'last_lon_',  type: 'int'},
         {name: 'last_lat_',  type: 'int'},
         {name: 'last_speed_',type: 'int'},
         {name: 'last_station_time_',type: 'string'},
         {name: 'bus_station_',type: 'string'},
         {name: 'route_name_',type: 'string'},
         {name: 'obj_id_',type: 'int'},
         {name: 'proj_id_',type: 'int'},
         {name: 'name',type: 'string'},
     ]
 });

