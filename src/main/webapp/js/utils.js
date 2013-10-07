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

//проверка ответа сервера
function checkResponseServer(response){
    //строковые константы для ответа
    var SERVER_ERROR = 'Потеряно соединение с сервером';
    var EMPTY_RESPONSE = 'Данные пусты';

    if (response.responseText === undefined || response.responseText === null){
        return SERVER_ERROR;
    }
    if (response.responseText.length-1 === 0 || response.responseText === "[]"){
        return EMPTY_RESPONSE;
    }
}

//TODO DESTROY 
/*конвертирование радиан в градусы*/
function convert(radian){
    var radian = radian.toString();
    var hour = radian.substr(0,2);
    var min = radian.substr(2,2);
    var sec = radian.substr(5,4);
    sec = sec.substr(0,2) + "." +  sec.substr(2,2);
    var desdeg = parseInt(hour) + parseInt(min)/60 + parseInt(sec)/3600;
    var temp = desdeg.toFixed(7);
    return temp;
}

//приводит время к правильному виду
function parseTime(time){
    time.getHours() < 10? hour = "0" + time.getHours() : hour = time.getHours();
    time.getMinutes() < 10? min = "0" + time.getMinutes() : min = time.getMinutes();
    time = hour + ":" + min + ":00";
    return time;
}

function temp(x){
    x = x.toString();
    var one = x.substr(0,2);
    var two = x.substr(2,2);
    return one + "."+two;
}

function azmth(i){
                   if (i < 0)
                        i = 0;
                   else i = Math.round(i/15)+1;
               if (i>=24)
                    i = 1;
               return i;
}

function WGS84MToStr(x){
    var i = 0,
        y = 0;
    var Result='';
    if (x==0)
        return 0;
    y=x;
    i=0;
    while (y >=1){
        i++;
        y=y/10;
    }
    if (i==5){
        y=Math.floor(x/1000);
        x=x-y*1000;
    }
    else{
        y=Math.floor(x/100);
        x=x-y*100;
    }
    x=(y+Math.floor(x)/60+(x-Math.floor(x))/60)*Math.PI/180;
    x=x*180/Math.PI;
    Result=Math.floor(x)+'.';
    x=x-Math.floor(x);
    Result=Result+Math.floor(x*60)+'';
    x=x-Math.floor(x*60)/60;
    Result=Result+Math.floor(x*3600)+''+'';
    return Result;
}

function RadToString(x){
    var Result = '';
     if ((x == null) || (x == 0)){
        Result='';
        return 0;
     }
     x = x*180/Math.PI;
     Result = Math.floor(x)+'°';
     x = x-Math.floor(x);
     Result = Result+Math.floor(x*60)+'';
     x = x-Math.floor(x*60)/60;
     Result = Result+Math.floor(x*3600)+''+'';
     return Result;
}

function BlockToDataMap(x){
    var y = 0,
        i = 0;
    var Result = 0;
   if ((x == null) || (x == 0)){
      Result = 0;
      return 0;
   }
   y = x;
   i = 0;
   while (y>=1){
       i++;
       y = y/10;
   }
   if (i=5){
              y = Math.floor(x/1000);
              x = x-y*1000;
   }
          else
            {
              y = Math.floor(x/100);
              x = x-y*100;
            }
   y = y+Math.floor(x)/60+(x-Math.floor(x))/60;
   Result = Math.Round(y*1000000);
   return Result;
}

function DataMapToBlock(x){
  var y = 0,
      i = 0;
      var Result = 0;
    if ((x == null) || (x == 0)){
     Result = 0;
     return 0;
    }
    x = x/1000000;
    y = x;
    i = 0;
    while (y>=1){
        i++;
        y = y/10;
    }
    if (i==3){

               y = Math.floor(x)*1000;
               x = (x-Math.floor(x))*60;
    }
           else
             {
               y = Math.floor(x)*100;
               x = (x-Math.floor(x))*60;
             }
    y = y+x;
    Result = y;
    return Result;
}

function RadToDataMap(x){
    var Result = 0;
    if ((x == null)||(x == 0)){
       Result = 0;
       return 0;
    }
     x = x*180/Math.PI;
    Result = Math.Round(x*1000000);
    return Result;
   }

   function DataMapToString(x){
   var Result = 0;
     if ((x == null)||(x == 0)){
       Result = 0;
       return 0;
    }
    if (x < 0)
       x = -x;
    Result = Math.floor(x/1000000)+'°';
    x = (Math.floor(x) % 1000000)/1000000;
    Result = Result+Math.floor(x*60)+'';
    x = x-Math.floor(x*60)/60;
    Result = Result+Math.floor(x*3600)+'';
    return Result;
}

function DataMapToRad(x){
        var Result = 0
        if ((x == null)||(x == 0)){
               Result = 0;
               return 0;
            }
         if (x < 0)
               x = -x;
         Result = Math.floor(x/1000000);
         x = (Math.floor(x) % 1000000)/1000000;
         Result = Result+Math.floor(x*60)/60;
         x = x-Math.floor(x*60)/60;
         Result = Result+Math.floor(x*3600)/3600;
         Result = Result/180*Math.PI;
         return Result;
}

function BlockToRad(x){
  var i = 0,
      y = 0, Result = 0;
  if (x==0){
               Result = 0;
               return 0;
            }
  else{
      y = x;
      i = 0;
      while (y>=1){
         i++;
         y = y/10;
      }
      if (i==5){
          y = Math.floor(x/1000);
          x = x-y*1000;
      }
      else
        {
          y = Math.floor(x/100);
          x = x-y*100;
        }
       Result = (y+Math.floor(x)/60+(x-Math.floor(x))/60)*Math.PI/180;
       return Result;
    }
}

function geoToMercator(x,y) {
    var d = x * Math.PI / 180;
    var m = y * Math.PI / 180;
    var l = 6378137;
    var k = 0.0818191908426;
    var f = k * Math.sin(m);
    var h = Math.tan(Math.PI / 4 + m / 2), j = Math.pow(Math.tan(Math.PI / 4 + Math.asin(f) / 2), k), i = h / j;
    // return new DoublePoint(Math.round(l * d), Math.round(l * Math.log(i)));
    return [l * d, l * Math.log(i)];
}


function RadToBlock(x){
    var Result = 0;
  x = x/Math.PI*180;
  Result = Math.floor(x)*100+(x-Math.floor(x))*60;
  return Result;
}

function RadToInt(x){
 x = x*180/Math.PI;
 var g = Math.floor(x);
 var m = Math.floor((x-Math.floor(x))*60);
 var s = Math.Round((((x-Math.floor(x))*60-Math.floor((x-Math.floor(x))*60))*60));
 return g +""+ m +""+ s;
}

function StrToRad(s){
    var i = 0,j = 0,k = 0,
        l = 0, Result = 0;
    i = s.indexOf('°');
    j = s.trim();

    i =s.indexOf('');
    k = s.trim();

    i =s.indexOf('');
    l = s.trim();
    Result = ((j*Math.PI+k*Math.PI/60+l*Math.PI/3600)/180);
}

function DistOfRad(x1,y1,x2,y2){
    var Result = 0;
    Result = Math.cos(y1)*Math.cos(y2)*Math.cos(x2-x1)+Math.sin(y1)*Math.sin(y2);
  if (Math.abs(Result) <= 1){
    Result = Math.acos(Result)*6371*1000;
}
  else if (Result > 0)
       Result = 0;
  else Result = Math.PI;
  return Result;
}