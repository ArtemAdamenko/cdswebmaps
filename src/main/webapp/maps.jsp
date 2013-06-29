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
            success: function(result){
                result.replace("[","");
                result.replace("]","");
                alert(res);
              //var res =  JSON.parse(result);
            }                
          });  
        }        
    </script>
    <script>
        var map;
        function initialize() {
            var Latlng = new google.maps.LatLng(51.673130,39.261495);
            var mapOptions = {
                zoom: 8,
                center: Latlng,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
            var marker = new google.maps.Marker({
                position: Latlng,
                map: map,
                title: 'Hello World!'
            });
        }
        //google.maps.event.addDomListener(window, 'load', initialize);
    </script>
  </head>
  <body onload="getBuses()">
    <div id="map-canvas"></div>
  </body>
</html>