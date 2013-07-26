<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Главная</title>
        <link href="css/style.css" rel="stylesheet" type="text/css">      
        <script src="js/jquery-ui-1.10.3.custom/js/jquery-1.9.1.js" type="text/javascript"></script> 
        <script src="js/utils.js" type="text/javascript"></script>
        <script type="text/javascript">      
            $(document).ready(function() {	
                $(document).mouseup(function() {
                    $("#loginform").mouseup(function() {
                        return false;
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

                $("form#signin").submit(function() {
                    return false;
                });

                $("input#cancel_submit").click(function(e) {
                    $("#loginform").hide();
                    $(".lock").fadeIn();
                });	
            });
            function auth(){
                var userName = document.forms["signin"].elements["username"].value;
                var pass = document.forms["signin"].elements["password"].value;
                $.ajax({
                    url: 'AuthorizationServlet',
                    data : { username: userName, pass: pass },
                    success: function(data) {
                        if (data === "access done"){
                            var url = document.location.href; // юрл в котором происходит поиск
                            var regV = /maps/;     // шаблон
                            var result = url.match(regV); 
                            if (result)
                                document.location.href = document.location.href;
                            else
                                document.location.href = document.location.href+"maps.jsp";
                        }else {
                            open_popup('#modal_window');
                        }
                    }
                });
            }
        </script>
    </head>
<body>
    <div id="modal_window" onclick="close_popup('#modal_window');" > 
        <p>Вы ввели неверный логин или пароль</p><br>
        <p style="font-size: 10px;">Нажмите на любую область страницы</p>
    </div> 
    <div id="background" onclick="close_popup('#modal_window');"></div>
    <div id="cont">
        <div class="box lock"> </div>
        <div id="loginform" class="box form">
            <h2>Авторизация на сервисе</h2>
            <div class="formcont">
                <fieldset id="signin_menu">
                    <span class="message">Пожалуйста введите данные для входа</span>
                    <form method="post" id="signin" action="">
                        <label for="username">Имя пользователя</label>
                        <input id="username" name="username" value="" title="username" tabindex="4" type="text">
                        <p>
                            <label for="password">Пароль</label>
                            <input id="password" name="password" value="" title="password" tabindex="5" type="password">
                        </p>
                        <p class="clear"></p>
                        <p class="remember">
                            <input id="signin_submit" value="Войти" tabindex="6" type="submit" onclick="auth();">
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
                    <td><img src="images/bg.jpg" alt=""> </td>
                </tr>
            </table>
        </div>
    </div>
</body>
</html>
