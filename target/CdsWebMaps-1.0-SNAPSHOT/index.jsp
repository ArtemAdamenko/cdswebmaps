<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cds Main</title>
        <link rel="stylesheet" type="text/css" href="css/style.css">
        
    </head>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.3/jquery.min.js"></script>
    
    <body>
        <script type="text/javascript">
         function delCookie(name) {
            document.cookie = name + "=" + "; expires=Thu, 01 Jan 1970 00:00:01 GMT";
        }
        $(document).ready(function() {
		
            $(document).mouseup(function() {
                $("#loginform").mouseup(function() {
                    return false
		});		
                $("a.close").click(function(e){
                    e.preventDefault();
                    $("#loginform").hide();
                    $(".lock").fadeIn();
                });
				
                if ($("#loginform").is(":hidden"))
                {
                    $(".lock").fadeOut();
                } else {
                    $(".lock").fadeIn();
                }				
                $("#loginform").toggle();
            });
			
			
			// I dont want this form be submitted
            $("form#signin").submit(function() {
                return false;
            });
			
			// This is example of other button
            $("input#cancel_submit").click(function(e) {
                $("#loginform").hide();
                $(".lock").fadeIn();
            });	
        });
</script>
        <script>
            function auth(){
                var userName = document.forms["signin"].elements["username"].value;
                var pass = document.forms["signin"].elements["password"].value;
                $.ajax({
                url: 'AuthorizationServlet',
                data : { username: userName, pass: pass },
                success: function(data) {
                    if (data == "access done")
                        document.location.href = document.location.href+"maps.jsp";
  }
});

            }
        </script>

        <div id="cont">
            <div class="box lock"> </div>
            <div id="loginform" class="box form">
                <h2>Авторизация на сервисе <a href="" class="close">Close it</a></h2>
                <div class="formcont">
                    <fieldset id="signin_menu">
                        <span class="message">Пожалуйста введите данные для входа</span>
                        <form method="post" id="signin" action="">
                            <label for="username">Имя пользователя</label>
                            <input id="username" name="username" value="" title="username" class="required" tabindex="4" type="text">
                            <p>
                                <label for="password">Пароль</label>
                                <input id="password" name="password" value="" title="password" class="required" tabindex="5" type="password">
                            </p>
                            <p class="clear"></p>
                            <p class="remember">
                                <input id="signin_submit" value="Войти" tabindex="6" type="submit" onclick="auth();">
                                <input id="cancel_submit" value="Выход" tabindex="7" type="button">
                            </p>
                        </form>
                    </fieldset>
                </div>
                <div class="formfooter"></div>
            </div>
        </div>
          <div id="bg">
            <div>
                <table cellspacing="0" cellpadding="0">
                    <tr>
                        <td><img src="images/bg.jpg" alt=""/> </td>
                    </tr>
                </table>
            </div>
          </div>
    </body>
</html>
