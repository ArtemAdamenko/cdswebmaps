/*редирект на заданную страницу*/
function redirect(page){
    var url = document.location.href.split("/");
    var redirect = "";
    for (var i = 0; i <= url.length - 2; i++){
        redirect +=url[i]+"/";
    }
    document.location.href = redirect+page;
}
/*удаление сессии*/
function logout(name){
    document.cookie = name + "=" + "; expires=Thu, 01 Jan 1970 00:00:01 GMT";
    var url = document.location.href.split("/");
    var redirect = "";
    for (var i = 0; i <= url.length - 2; i++){
        redirect +=url[i]+"/";
    }
    document.location.href = redirect;
}
/*обновление страницы*/
function fresh() {
    location.reload();
}
/*конвертирование радиан в градусы*/
function convert(radian){
    var radian = radian.toString();
    var hour = radian.substr(0,2);
    var min = radian.substr(2,2);
    var sec = radian.substr(5,2);
    var sec2 = radian.substr(7,2);
    var sec = sec.toString() + "." + sec2.toString();       
    var deg = (parseInt(hour) + (parseInt(min) + parseFloat(sec)/60)/60);
    return deg;
}
        
/*Функция преобразования даты*/
function getMyDate(date){
    var converted = Date.parse(date);
    var myDate = new Date(converted);
    var year = myDate.getFullYear();
    var month = myDate.getMonth();
    if (month < 10)
        month = "0"+month;
    var day = myDate.getDay();
    if (day < 10)
        day = "0" + day;
    var hour = myDate.getHours();
    if (hour < 10)
        hour = "0" + hour;
    var min = myDate.getMinutes();
    if (min < 10)
        min = "0" + min;
    return day + "." + month + "." + year + " " + hour + ":" + min;
}

/* Открываем модальное окно: */
            function open_popup(box) { 
              $("#background").show(); 
              $(box).centered_popup(); 
              $(box).delay(100).show(1); 
            } 

            /* Закрываем модальное окно: */
            function close_popup(box) { 
              $(box).hide(); 
              $("#background").delay(100).hide(1); 
            } 

            $(document).ready(function() { 
              /* Позиционируем блочный элемент окна по центру страницы: */
              $.fn.centered_popup = function() { 
                this.css('position', 'absolute'); 
                this.css('top', ($(window).height() - this.height()) / 2 + $(window).scrollTop() + 'px'); 
                this.css('left', ($(window).width() - this.width()) / 2 + $(window).scrollLeft() + 'px'); 
              } 

            });



