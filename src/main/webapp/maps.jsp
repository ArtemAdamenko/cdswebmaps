<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>Cds Web Map</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="css/reportStyle.css">
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.3/jquery.min.js"></script>
    <script src="js/utils.js"></script>
    <script>
        setInterval("fresh()",15000);
        function initialize() {
            $.ajax({  
            type: "GET",  
            url: "GetBusesServlet",  
            success: function(result){
                var res =  JSON.parse(result);
                var myLatlng = new google.maps.LatLng(51.7038,39.1833);
                var mapOptions = {
                  zoom: 13,
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
      <input type="submit" value="Отчет" onclick="redirect('report.jsp');">
      <input type="submit" value='Выход' id ='logout' onclick="logout('session_id');">
      <div id="map-canvas"></div>
  </body>
</html>