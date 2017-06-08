<%@page import="com.su.models.Umbrella"%>

<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>百度地图</title>
    <style type="text/css">
        body, html, #allmap
        {
            width: 100%;
            height: 100%;
            overflow: hidden; 
            margin: 0;
        }
		#img1{
			 width: 100%;
            height: 100%;
            overflow: hidden; 
            margin: 0;
			}
        #l-map
        {
            height: 100%;
            width: 78%;
            float: left;
            border-right: 2px solid #bcbcbc;
        }
        #r-result
        {
            height: 100%;
            width: 20%;
            float: left;
        }
		
    </style>

    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=6c497f51c06477544e5fa6e9bd68f7c3"></script>

</head>
<body>

    <div id="allmap">
    
    </div>

   
</body>
</html>
<script type="text/javascript" src="resources/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="resources/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="resources/static/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="resources/static/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="resources/static/lib/My97DatePicker/4.8/WdatePicker.js"></script> 
<script type="text/javascript" src="resources/static/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
<script type="text/javascript" src="resources/static/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript">

	var map; //Map实例
    var myGeo;//地址解析
	function map_init() {
		  
		map = new BMap.Map("allmap");               // 创建Map实例
		//第1步：设置地图中心点，上海市
		var point = new BMap.Point(121.405603, 31.322716);    // 创建点坐标(经度,纬度)
		//第2步：初始化地图,设置中心点坐标和地图级别。
        map.centerAndZoom(point, 19);
		//第3步：启用滚轮放大缩小
        map.enableScrollWheelZoom(true);
        //启用键盘上下左右移动地图
        map.enableKeyboard();
		
		//第4步：向地图中添加缩放控件
        var ctrlNav = new window.BMap.NavigationControl({
            anchor: BMAP_ANCHOR_TOP_LEFT,
            type: BMAP_NAVIGATION_CONTROL_LARGE
        });
		map.addControl(ctrlNav);
		
		//第5步：向地图中添加缩略图控件
        var ctrlOve = new window.BMap.OverviewMapControl({
        anchor: BMAP_ANCHOR_BOTTOM_RIGHT,
		isOpen: 1
        });
		
        map.addControl(ctrlOve);
		//第7步添加地图类型控件 
		map.addControl(new BMap.MapTypeControl());      //添加地图类型控件
		//第6步：向地图中添加比例尺控件
        var ctrlSca = new window.BMap.ScaleControl({
        anchor: BMAP_ANCHOR_BOTTOM_LEFT
        });
        map.addControl(ctrlSca);
	}	
	window.onload = function () {  
		 
		map_init();
		<c_rt:forEach items="${umbrellas}" var="umbrella">
		
		setLocation(${umbrella.device_lon},${umbrella.device_lat},"${umbrella.device_uuid}",${umbrella.id});
		</c_rt:forEach>	

	} 
	
	 function umbrella_list1(uuid){
				
			layer_show("伞架详情","umbrella-list"+'?id='+uuid,800,600);
	    
	    }
		
	function setLocation(x,y,desc,id){//参数：经纬度
         var point = new BMap.Point(x, y);   
        	//alert(desc);      
         //map.centerAndZoom(point, 19); 
         var marker=new BMap.Marker(point);
         marker.setLabel(new BMap.Label("伞架:"+(id),{offset:new BMap.Size(20,-10)}));	 
         map.addOverlay(marker);   
		 //若要给标注添加信息框，则继续下面的代码：
		//var infoWindow = new BMap.InfoWindow("普通标注");
		//onclick='umbrella_list1("+desc+")' data-uuid="+desc+"  onclick=\"proEdit('".XXXXX."')\"onclick=&quot;umbrella_list1(&quot;"+desc+"&quot;)&quot
	//	var hh = "<a  href=umbrella-list?id="+desc+" class=&quot;watchClass&quot; data-uuid="+desc+" style=&quot;text-decoration:none&quot;>	点击查看详情</a>";					
		var hh = "<a  href=javascript:; class=&quot;watchClass&quot; onclick=umbrella_list1("+"'"+desc+"'"+")  style=text-decoration:none>	点击查看详情</a>";	
		
		var infoWindow = new BMap.InfoWindow(hh);
		
		//给mark添加鼠标单击事件
		marker.addEventListener("click", function(){this.openInfoWindow(infoWindow);});		
      // marker.addEventListener("click", function () { alert('该处经纬度坐标是：'+x+','+y+'。aa');});	
		//marker.addEventListener("click", function(){window.external.PutIntotextBox(x,y)});			
    }
	
	//清除覆盖物
    function removeOverlay() {
		map.clearOverlays();
     }
   
  
</script>

