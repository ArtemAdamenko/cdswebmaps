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
    <script>
        function logout(name){
            document.cookie = name + "=" + "; expires=Thu, 01 Jan 1970 00:00:01 GMT";
            var url = document.location.href.split("/");
            var redirect = "";
            for (var i = 0; i <= url.length - 2; i++){
                redirect +=url[i];
            }
            document.location.href = redirect;
        }
        function convert(radian){
            var radian = radian.toString();
            var hour = radian.substr(0,2);
            var min = radian.substr(2,2);
            var sec = radian.substr(5,2);
            var sec2 = radian.substr(7,2);
            var sec = sec.toString() + "." + sec2.toString();
            
            var deg = (parseInt(hour) + (parseInt(min) + parseFloat(sec)/60)/60);
            //var deg = deg*Math.PI/180;
           return deg;
        }
        
        function initialize() {
            $.ajax({  
            type: "GET",  
            url: "GetBusesServlet",  
            //data: iduser,  
            success: function(result){
                var res =  JSON.parse(result);
                var myLatlng = new google.maps.LatLng(51.7038,39.1833);
                var mapOptions = {
                  zoom: 12,
                  center: myLatlng,
                  mapTypeId: google.maps.MapTypeId.ROADMAP
                }
                var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

                for (var i = 0; i <= res.length-1; i++)
                {
                  var Coords = new google.maps.LatLng(convert(res[i].last_lon_),convert(res[i].last_lat_));
                  var marker = new google.maps.Marker({
                    position: Coords,
                    map: map,
                    title: res[i].name_ + " " + res[i].last_time_
                    });
                }
            }
            });
        }

      google.maps.event.addDomListener(window, 'load', initialize);

    </script>
  </head>
  <body>
  <div id="map-canvas"></div>
  <input type="submit" value='Выход' id ='logout' onclick="logout('username');">
  </body>
</html>