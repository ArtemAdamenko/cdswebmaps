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

       /*форматирование даты под запрос*/
        function parseDate(date){
            var parts = date.split(" ");
            var calendarDate = parts[0].split(".");
            if (parts[1] === undefined || parts[1] === ""){
                var dt = new Date();
                parts[1] = dt.getHours() + ":" + dt.getMinutes();
            }
            var dayTime = parts[1].split(":");
            var newDate = calendarDate[1] + "-" + calendarDate[0] + " " + dayTime[0] + ":" + dayTime[1] + ":00";
            newDate = calendarDate[2] + "-" + newDate;
            return newDate;
        }

function convTime(date){
    var half = date.substring(date.length-2, date.length);
    
    if(half == "PM"){
        var parts = date.split(" ");
        var time = parts[3].split(":");
        var newHour;
        if (time[0] == "12")
            newHour = "12";
        if (time[0] == "1")
            newHour = "13";
        if (time[0] == "2")
            newHour = "14";
        if (time[0] == "3")
            newHour = "15";
        if (time[0] == "4")
            newHour = "16";
        if (time[0] == "5")
            newHour = "17";
        if (time[0] == "6")
            newHour = "18";
        if (time[0] == "7")
            newHour = "19";
        if (time[0] == "8")
            newHour = "20";
        if (time[0] == "9")
            newHour = "21";
        if (time[0] == "10")
            newHour = "22";
        if (time[0] == "11")
            newHour = "23";
        var newTime = parts[0] + " " + parts[1] + " " + parts[2] + " " + newHour + ":" + time[1] + ":" + time[2];
        return newTime;
    }
    return date = date.substring(0, date.length-2);
    
}

function convDate(month){
    if(month == "Jul"){
        return "Июль";
    }
    if(month == "Jun"){
        return "Июнь";
    }
    if(month == "Aug"){
        return "Август";
    }
    if(month == "Sep"){
        return "Сентябрь";
    }
    if(month == "Oct"){
        return "Октябрь";
    }
    if(month == "Nov"){
        return "Ноябрь";
    }
    if(month == "Dec"){
        return "Декабрь";
    }
    if(month == "Jan"){
        return "Январь";
    }
    if(month == "Feb"){
        return "Февраль";
    }
    if(month == "Mar"){
        return "Март";
    }
    if(month == "Apr"){
        return "Апрель";
    }
    if(month == "May"){
        return "Май";
    }
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



