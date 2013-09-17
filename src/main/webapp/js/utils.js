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
    //console.log(sec);
    //var temp = sec.substr(0,3) + "." + sec.substr(3,1);
   // console.log(temp);
    //sec = Math.round(temp);
   // console.log(sec);
    //var sec2 = radian.substr(7,10);
    //var sec = sec.toString() + "." + sec2.toString();  
    /*radian = hour + min + sec;
    console.log(radian);*/
    var deg = ((parseInt(hour) + (parseInt(min) + parseFloat(sec)/60)/60));
    var temp = deg.toFixed(7);
    
    /*var dec = hour + (min + sec/60)/60;
    if ( dec>360 ) {
            var m = Math.floor(dec/360);
            dec-=m*360;
    }*/
    //decdeg.SetValue(dec);
    //rads.SetValue(dec*(Math.PI/180));
    
    return temp;
}

//приводит время к правильному виду
function parseTime(time){
    time.getHours() < 10? hour = "0" + time.getHours() : hour = time.getHours();
    time.getMinutes() < 10? min = "0" + time.getMinutes() : min = time.getMinutes();
    time = hour + ":" + min + ":00";
    return time;
}




