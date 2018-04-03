function getLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(showPosition);
     // return "120.106787, 30.291509";
  }else{
    // x.innerHTML="该浏览器不支持获取地理位置。";
  }
}
// function showPosition(position){
//   // var gps = "纬度: " + position.coords.latitude + 
//   // "<br>经度: " + position.coords.longitude;
//   alert("latitude:" + position.coords.latitude + "," + "longitude:" + position.coords.longitude);
//   var ggPoint = new BMap.Point(position.coords.latitude, position.coords.longitude);	
//   alert("123");

//   BMap.Convertor.translate(gpsPoint,0,translateCallback);
//   // dataSave("latitude", position.coords.latitude);
//   //  dataSave("longitude", position.coords.longitude);
  
// }

// translateCallback = function (data){ 

//   alert("latitude:" + data[0][0] + "," + "longitude:" + data[0][1]);
//    dataSave("latitude", data[0][0]);
//    dataSave("longitude", data[0][1]);
// }


 // //坐标转换完之后的回调函数
 //    translateCallback = function (data){
 //      if(data.status === 0) {
 //        var marker = new BMap.Marker(data.points[0]);
 //        bm.addOverlay(marker);
 //        var label = new BMap.Label("确转换后的百度坐标（正）",{offset:new BMap.Size(20,-10)});
 //        marker.setLabel(label); //添加百度label
 //        bm.setCenter(data.points[0]);
 //      }
 //    }

 //    setTimeout(function(){

 //        var gpsPoint=new BMap.Point(Array[i], Array[i + 1]);
 //        var convertor = new BMap.Convertor();
 //        var pointArr = [];
 //        pointArr.push(ggPoint);
 //        convertor.translate(pointArr, 1, 5, translateCallback)
 //    }, 1000);

//30.288009,120.090399

function showPosition(position) {//测试
	dataRemove("latitude");
    dataRemove("latitude");
  // function getLocation(position) {
  // var ggPoint=new BMap.Point(120.090399,  30.288009);//测试
	// alert("longitude:" + position.coords.longitude + ",latitude:" + position.coords.latitude);
  var ggPoint=new BMap.Point(position.coords.longitude,  position.coords.latitude);
  //GPS转百度
  // BMap.Convertor.translate(gpsPoint,0,translateCallback);
  // var ggPoint=new BMap.Point(120.090399,  30.288009);//测试
  var convertor = new BMap.Convertor();
  var pointArr = [];
  pointArr.push(ggPoint);
  convertor.translate(pointArr, 1, 5, translateCallback)

}

 //坐标转换完之后的回调函数
translateCallback = function (data){
  if(data.status === 0) {
	  // alert("longitude:" + data.points[0].lng + ",latitude:" + data.points[0].lat);
    point = new BMap.Point(data.points[0].lng,data.points[0].lat);
    var mySelf_point = new BMap.Icon("../../images/map/mySelf_point.png", new BMap.Size(17,17));
    var marker = new BMap.Marker(point, {icon:mySelf_point});  // 创建标注
    map.addOverlay(marker);
    // map.centerAndZoom(point, 13); 
    
   dataSave("latitude", data.points[0].lat);
   dataSave("longitude", data.points[0].lng);
  }
}
// lat: 30.291845146582
// lng: 120.1017056705