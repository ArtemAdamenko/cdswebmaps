<%-- 
    Document   : maps
    Created on : Jun 28, 2013, 9:19:33 AM
    Author     : Администратор
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>Cds Web Map</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <style>
      html, body, #map-canvas {
        margin: 0;
        padding: 0;
        height: 100%;
      }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.3/jquery.min.js"></script>
    <script type="text/javascript">
        function getBuses(){  

          //$("#loading").hide();
          
          $.ajax({  
            type: "GET",  
            url: "GetBusesServlet",  
            //data: iduser,  
            success: function initialize(result){
              var res =  JSON.parse(result);
              return res;
              /*var map;
              //var Latlng = new google.maps.LatLng(51.673130,39.261495);
              var mapOptions = {
                zoom: 8,
                //center: Latlng,
                mapTypeId: google.maps.MapTypeId.ROADMAP
                };
              map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
              for (var i = 0; i <= res.length-1; i++)
              {
                  print(res[i].last_lat_);
                  print(res[i].last_lon_);
                  var Coords = new google.maps.LatLng(res[i].last_lat_,res[i].last_lon_);
                  var marker = new google.maps.Marker({
                    position: Coords,
                    map: map,
                    title: 'Hello World!'
                });
              }*/
             
            }                
          });  
        }        
         //google.maps.event.addDomListener(window, 'load', getBuses);
    </script>
    <script>
        var map;
        var count = 0;
        function initialize() {
            $.ajax({  
            type: "GET",  
            url: "GetBusesServlet",  
            //data: iduser,  
            success: function(result){
                var res =  JSON.parse(result);
                var Latlng = new google.maps.LatLng(51.557129,39.268555);
                var mapOptions = {
                    zoom: 12,
                    center: Latlng,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
                for (var i = 0; i <= res.length-1; i++)
                {
                    count++;
                    var long = res[i].last_lon_;//число
                    var ceil = Math.floor(res[i].last_lon_).toString();//целая
                    var temp = long - ceil;//дробная часть
                    var long2 = ceil.substr(0,2);//укорачивание
                    var resRes = parseInt(long2) + temp;
                    
                    var lat = res[i].last_lat_;//число
                    var ceillat = Math.floor(res[i].last_lat_).toString();//целая
                    var templat = lat - ceillat;//дробная часть
                    var long2lat = ceillat.substr(0,2);//укорачивание
                    var resReslat = parseInt(long2lat) + templat;
                    var long = res[i].last_lon_/100;
                    var lat = res[i].last_lat_/100;
                    resRes = resRes.toFixed(6);
                    resReslat = resReslat.toFixed(6);
                    
                      var Coords = new google.maps.LatLng(long,lat);
                      var marker = new google.maps.Marker({
                        position: Coords,
                        map: map,
                        title: "имя "+res[i].name_ + " время "+res[i].last_time_
                    });
                }
                alert(count);
            }
            });
            };
       google.maps.event.addDomListener(window, 'load', initialize);
    </script>
  </head>
  <body>
    <div id="map-canvas"></div>
  </body>
</html>