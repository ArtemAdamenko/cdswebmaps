<%-- 
    Document   : report
    Created on : Jul 4, 2013, 3:29:14 PM
    Author     : Администратор
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.3/jquery.min.js"></script>
        <script>
            
        function report(){
        $.ajax({
            url: 'Report',
            success: function(data) {
               var id = document.getElementsByClassName("report_content")
               id.innerHTML=data;

            }
        });
    }
        </script>
    </head>
    <body onload="report();">
        <div class="report_content"></div>
    </body>
</html>
