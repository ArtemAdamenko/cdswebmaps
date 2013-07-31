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





