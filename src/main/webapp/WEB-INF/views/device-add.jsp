<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>   
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>添加伞架</title>
    <meta name="description" content="添加伞架">
    <meta name="keywords" content="index">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="icon" type="image/png" href="resources/assets/i/favicon.png">
    <link rel="apple-touch-icon-precomposed" href="resources/assets/i/app-icon72x72@2x.png">
    <meta name="apple-mobile-web-app-title" content="Amaze UI"/>
    <link rel="stylesheet" href="resources/assets/css/amazeui.min.css"/>
    <link rel="stylesheet" href="resources/assets/css/amazeui.datatables.min.css"/>
    <link rel="stylesheet" href="resources/assets/css/app.css">
    <script src="resources/assets/js/jquery.min.js"></script>
    <script type="text/javascript"
            src="http://api.map.baidu.com/api?v=2.0&ak=8579ee3b967425dc22f0c3953825ec95"></script>

    <!--<script src="http://api.map.baidu.com/getscript?v=2.0&ak=8579ee3b967425dc22f0c3953825ec95&services=&t=20170506025723"></script>-->
</head>

<body data-type="login" class="theme-white">
	 <c_rt:choose>
  <c_rt:when test="${status}">
  <script src="resources/assets/js/theme.js"></script>
<div >
    <!-- 风格切换 -->
  
    <div class="tpl-login">
        <div class="tpl-login-content" >
            <div class="tpl-login-title">添加伞架</div>
            <span class="tpl-login-content-info">
               		   添加一个伞架，对应的伞架才允许与云端数据交互
              </span>


            <form class="am-form tpl-form-line-form" action="device-add"  method="POST">
                <span th:text="${addInfo}"></span>
                <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
                <div class="am-form-group">
                    <input type="text" name="name" class="tpl-form-input" id="name" placeholder="伞架名称">
                </div>

                <div class="am-form-group">
                    <input type="text" name="devId" class="tpl-form-input" id="devId" placeholder="设备编号">
                </div>

                <div class="am-input-group" style="border-bottom:1px solid #c2cad8;height: 32px;margin-bottom: 18px; ">
                    <input type="text" name="X" class="input-form-field" style="border: none; padding: 6px 0px;padding-bottom: 4px" id="X" placeholder="设备经度">
                    <span class="am-input-group-label" style="border:none;background: white; height:32px;line-height: 32px;">
                        <i class="am-icon-map-marker"  style="line-height: 32px;" onclick="selectPointInMap()" ></i>
                    </span>
                </div>
                <div class="am-input-group" style="border-bottom:1px solid #c2cad8; height: 32px;margin-bottom: 18px; ">
                    <input type="text" name="Y" class="input-form-field" style="border: none; padding: 6px 0px;padding-bottom: 4px" id="Y" placeholder="设备纬度">
                    <span class="am-input-group-label" style="border:none;background: white; height:32px;line-height: 32px;">
                      
                    </span>
                </div>

                    <div class="am-form-group">
                        <input type="text" name="desc" class="tpl-form-input" id="desc" placeholder="描述（选择填写）">
                    </div>
                  

                    <div class="am-form-group">
                        <button type="submit"
                                class="am-btn am-btn-primary  am-btn-block tpl-btn-bg-color-success  tpl-login-btn">提交
                        </button>
                    </div>
            </form>
        </div>

        <div id="doc-modal-1" class="am-modal am-modal-prompt" tabindex="-1">
            <div class="am-modal-dialog">
                <div class="am-modal-hd">在地图上选取坐标
                    <a href="javascript: void(0)" class="am-close am-close-spin"
                       data-am-modal-close>&times;</a>
                </div>
                <div class="am-modal-hd">
                    城市名称：<input id="address" onKeyUp="keyup(event)"/><input type="button" value="获取一下"
                                                                            onclick="getLocalByAddr()"/>
                    <div id="allmap" style="width:100%; height: 600px"></div>
                </div>
                <div class="am-modal-footer">
                    百度坐标：<input id="localtion"/>
                    <span class="am-modal-btn" data-am-modal-confirm>选好了</span>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="resources/assets/js/amazeui.min.js"></script>
<script src="resources/assets/js/app.js"></script>
<script>

		$(function () {
			　	　$('form').bind('submit',function () {  //给form标签绑定submit事件
			　　　	　var i=0;
			　
			　　　　var t=$('#name').val();  //判断textarea标签是否填写
			　　　　if (t=='') {
			　　　　　　alert('请填写伞架名称');
		　　　　		return false;
			　　　　}
				  var t=$('#devId').val();  //判断textarea标签是否填写
				　　　　if (t=='') {
				　　　　　　alert('请填写设备编号');
						return false;
				　　　　}
				
				  var t=$('#X').val();  //判断textarea标签是否填写
				　　　　if (t=='') {
				　　　　　　alert('请选择伞架放置地点信息');
						return false;
				　　　　}
			　　　
			　　});
		
			});


    function selectPointInMap() {
        $('#doc-modal-1').modal({
            relatedTarget: this,
            onConfirm: function () {

            },
            onCancel: function () {

            }
        });
    }
</script>
<script type="text/javascript">
    var myGeo;
    var map;
    $(document).ready(function () {
        // 百度地图API功能
        map = new BMap.Map("allmap", {enableMapClick: false}); //关闭默认地图POI事件
        var redPoint;
        var g_point;
        //滚轮缩放功能
        map.enableScrollWheelZoom(true);
        var point = new BMap.Point(116.331398, 39.897445);
        map.centerAndZoom(point, 12);

        map.addControl(new BMap.NavigationControl());
        map.addControl(new BMap.ScaleControl());
        map.addControl(new BMap.OverviewMapControl());
        // 创建地址解析器实例
        myGeo = new BMap.Geocoder();
        // 将地址解析结果显示在地图上,并调整地图视野
        getLocalByAddr();
        //单击获取点击的经纬度
        map.addEventListener("click", function (e) {
            //alert(e.point.lng + "," + e.point.lat);
            writeLocalIntoInput(e.point);
            map.removeOverlay(redPoint);
            redPoint = new BMap.Marker(e.point);
            map.addOverlay(redPoint);
            g_point = e.point;
            //var infoWindow = new BMap.InfoWindow(e.point.lat+','+e.point.lng, opts);  // 创建信息窗口对象
            //map.openInfoWindow(infoWindow,e.point); //开启信息窗口
        });

        navigator.geolocation.getCurrentPosition(function (position) {
            console.log("location " + position);
            translatePoint(position);
        });
    });

    function translatePoint(position) {
        var currentLat = position.coords.latitude;
        var currentLon = position.coords.longitude;
        var gpsPoint = new BMap.Point(currentLon, currentLat);
        BMap.Convertor.translate(gpsPoint, 0, function (point) {
            map.centerAndZoom(point, 15);
        }); //转换坐标
    }

    //根据地址获取坐标点信息，并将坐标点作为中心点
    function getLocalByAddr() {
        var address = document.getElementById("address").value;
        console.log(address);
        myGeo.getPoint(address, function (point) {
            writeLocalIntoInput(point);
            if (point) {
                map.centerAndZoom(point, 16);
                map.removeOverlay(redPoint);
                redPoint = new BMap.Marker(point);
                map.addOverlay(redPoint);
                g_point = point;
                //var infoWindow = new BMap.InfoWindow(point.lat+','+point.lng, opts);  // 创建信息窗口对象
                //map.openInfoWindow(infoWindow,point); //开启信息窗口
            } else {
                alert("您选择地址没有解析到结果!");
            }
        }, "北京市");
    }
    //将坐标点写到框中
    function writeLocalIntoInput(p) {
        if (p) {
            document.getElementById("X").value = p.lng;//经度
            document.getElementById("Y").value = p.lat;//纬度
            document.getElementById("localtion").value = p.lat + ',' + p.lng;
            //callGpsspg(p.lat+','+p.lng);
        } else {
            document.getElementById("X").value = '';
            document.getElementById("Y").value = '';

        }
    }
    //监听地址输入框的回车事件
    function keyup(event) {
        if (event.keyCode == "13") {
            //回车执行查询
            getLocalByAddr();
        }
    }
    //复制到剪切板
    function copyToClipboard() {
        var value = document.getElementById("localtion").value;
        if (window.clipboardData) {
            window.clipboardData.setData("text", value);
            alert(window.clipboardData.getData('text'))
        } else {
            alert("只支持IE");
        }
    }
    var opts = {
        width: 400,     // 信息窗口宽度
        height: 60,     // 信息窗口高度
        title: "", // 信息窗口标题
        enableMessage: true,//设置允许信息窗发送短息
        message: ""
    }

    /////////////////////////////////////////////////////////////////////////
    //调用gpsspg网站，jsonp形式，确定，每日每个oid和key只有访问2000次

    /*参数lat_lng 可以传递 lat或者lng中的一个*/
    function baiduToGPS(lat_lng) {
        var abs_lat_lng = Math.abs(lat_lng);
        //a_  意为all，含有整数和小数部分。
        //o_  意为only，只有小数部分
        var du = parseInt(abs_lat_lng); //度(不含小数部分)
        var o_du = lat_lng - du;   //度(只有小数部分)
        var a_fen = o_du * 60;  //分(含有整数和小数部分)
        var fen = parseInt(a_fen);  //分(不含小数部分)
        var o_fen = a_fen - fen;  //分(只有小数部分)
        var a_miao = o_fen * 60;   //秒(含有整数和小数部分)
        var miao = a_miao.toFixed(2);      //秒(取两位小数)
        var value = du + '°' + fen + '′' + miao + '″';
        console.log(value);
        return value;
    }

    //请求gpsspg网站的回调函数
    function jsoncallback(data) {
        console.log('转换状态:' + data.status);
        //jsoncallback&&jsoncallback({"status":"200","result":[{"lat":30.99881860,"lng":108.69433701}],"match":[1]})
        console.log('GPS坐标:' + data.result[0].lat + ',' + data.result[0].lng);
        var lat = data.result[0].lat;
        var lng = data.result[0].lng;
        var lat_value = baiduToGPS(lat);
        var lng_value = baiduToGPS(lng);
        var gps = '';
        if (lat >= 0) {
            gps = gps + '北纬N' + lat_value;
        } else {
            gps = gps + '南纬S' + lat_value;
        }
        gps = gps + ',';
        if (lng >= 0) {
            gps = gps + '东经E' + lng_value;
        } else {
            gps = gps + '西经W' + lng_value;
        }
        console.log(gps);
        //写到框中
        document.getElementById("gpsLocaltion").value = gps;
        var infoWindow = new BMap.InfoWindow('百度坐标：' + g_point.lat + ',' + g_point.lng + '<br/>' + 'GPS坐标：' + gps, opts);  // 创建信息窗口对象
        map.openInfoWindow(infoWindow, g_point); //开启信息窗口
    }

    //第一种方式   getJSON形式
    function callGpsspg(latAndLng) {
        $.getJSON("http://api.gpsspg.com/convert/latlng/?jsoncallback=?",
            {
                oid: "1905",
                key: "A14DE785B8BB2FB60F684ED7E2CF0269",
                from: "2",
                to: "0",
                //latlng: "31.0026777169,108.7051969740",
                latlng: latAndLng,
                output: "jsonp",
                callback: "jsoncallback"
            }
        );
    }
    ///////////////////////////////////////////////////////////////

</script>
		
  </c_rt:when>
  
  <c_rt:otherwise>
  		<% response.sendRedirect("login"); %>
  		<%-- <jsp:forward page="home.jsp"></jsp:forward>  --%>
  </c_rt:otherwise>
  </c_rt:choose>


</body>

</html>