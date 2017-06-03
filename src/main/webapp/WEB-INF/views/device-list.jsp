<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>   
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="Bookmark" href="/favicon.ico" >
<link rel="Shortcut Icon" href="/favicon.ico" />
<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5shiv.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css" href="resources/static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="resources/static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="resources/lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="resources/static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="resources/static/h-ui.admin/css/style.css" />
<!--[if IE 6]>
<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->

<script type="text/javascript" >
/* $(document).ready(function() {
	alert("点击了");
	jQuery(document).on('click', ".updateState", function() {
		
		var cur=$(this);
		cur.prev().text("获取中");
		var data={
				uuid:cur.attr("data-uuid")
		}
		jQuery.ajax({
			type: 'POST',
			 url: "getDeviceState",
			 data:data,
			 dataType: 'json',
			 success: function(json) { 
				if(json.status==0){
					if(json.redirectUrl=="在线")cur.prev().html("<b style='color:#0b4'>在线</b>");
					else cur.prev().text(json.redirectUrl);
					}
				} 
			});


	 });
	setInterval(function(){
		 $(".updateState").click();
	},1000 * 5);
	
}); */
</script>
<title>管理员列表</title>
</head>
<body>


	 <span class="r">共有伞架：<strong>${device}</strong> 套</span> </div>
	<table class="table table-border table-bordered table-bg">
		<thead>
			<tr>
				<th scope="col" colspan="9">设备列表</th>
			</tr>
			<tr class="text-c">
				<th width="25"><input type="checkbox" name="" value=""></th>
				<th width="40">序号</th>
				<th width="150">伞架名称</th>
				<th width="90">伞架编号</th>
				<th width="150">伞架注册时间</th>
				<th width="130">伞架状态</th>
				<th width="100">伞架雨伞状态</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<c_rt:forEach items="${umbrellas}" var="umbrella">
			<tr class="text-c">
				<td><input type="checkbox" value="1" name=""></td>
				<td>${umbrella.id}</td>
				<td>${umbrella.name}</td>
				<td>${umbrella.device_uuid}</td>
				<td>${umbrella.time}</td>
				<c_rt:choose>
					<c_rt:when test="${umbrella.status}"><td class="td-status"><span class="label label-success radius">在线</span><a href="javascript:void(0)" class="updateState" data-uuid="${umbrella.device_uuid}">&nbsp;&nbsp;刷新</a></td></c_rt:when>
					<c_rt:otherwise><td class="td-status"><span class="label radius">离线</span><a href="javascript:void(0)" class="updateState" data-uuid="">&nbsp;&nbsp;刷新</a></td></c_rt:otherwise>
				</c_rt:choose>	
				<td></a> 
				<a  href="javascript:;" onclick="umbrella_list('雨伞状态','umbrella-list','1','800','670','${umbrella.device_uuid}')"  style="text-decoration:none">				
				点击查看详情</a></td>			
				<td class="td-manage">
				<a style="text-decoration:none" onClick="admin_stop(this,'10001')" href="javascript:;" title="停用">
				<i class="Hui-iconfont">&#xe631;</i>
				</a> 
				<a title="编辑" href="javascript:;" onclick="admin_edit('管理员编辑','admin-add.html','1','800','500')" class="ml-5" style="text-decoration:none">
				<i class="Hui-iconfont">&#xe6df;</i>
				</a>
				 <a title="删除" href="javascript:;" onclick="admin_del(this,'1')" class="ml-5" style="text-decoration:none">
				 <i class="Hui-iconfont">&#xe6e2;</i>
				 </a></td>
			</tr>
		</c_rt:forEach>	
		</tbody>
	</table>
</div>


<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="resources/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="resources/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="resources/static/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="resources/static/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="resources/static/lib/My97DatePicker/4.8/WdatePicker.js"></script> 
<script type="text/javascript" src="resources/static/lib/datatables/1.10.0/jquery.dataTables.min.js"></script> 
<script type="text/javascript" src="resources/static/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript">
/*
	参数解释：
	title	标题
	url		请求的url
	id		需要操作的数据id
	w		弹出层宽度（缺省调默认值）
	h		弹出层高度（缺省调默认值）
*/
/*管理员-增加*/
function umbrella_list(title,url,id,w,h,uuid){

	layer_show(title,url+'?id='+uuid,w,h);
}
/*管理员-删除*/
function admin_del(obj,id){
	layer.confirm('确认要删除吗？',function(index){
		$.ajax({
			type: 'POST',
			url: '',
			dataType: 'json',
			success: function(data){
				$(obj).parents("tr").remove();
				layer.msg('已删除!',{icon:1,time:1000});
			},
			error:function(data) {
				console.log(data.msg);
			},
		});		
	});
}

/*管理员-编辑*/
function admin_edit(title,url,w,h,id){
	
	layer_show(title,url+'?id='+id,w,h);
}
/*管理员-停用*/
function admin_stop(obj,id){
	layer.confirm('确认要停用吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		
		$(obj).parents("tr").find(".td-manage").prepend('<a onClick="admin_start(this,id)" href="javascript:;" title="启用" style="text-decoration:none"><i class="Hui-iconfont">&#xe615;</i></a>');
		$(obj).parents("tr").find(".td-status").html('<span class="label label-default radius">已禁用</span>');
		$(obj).remove();
		layer.msg('已停用!',{icon: 5,time:1000});
	});
}

/*管理员-启用*/
function admin_start(obj,id){
	layer.confirm('确认要启用吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		
		
		$(obj).parents("tr").find(".td-manage").prepend('<a onClick="admin_stop(this,id)" href="javascript:;" title="停用" style="text-decoration:none"><i class="Hui-iconfont">&#xe631;</i></a>');
		$(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">已启用</span>');
		$(obj).remove();
		layer.msg('已启用!', {icon: 6,time:1000});
	});
}
</script>
</body>
</html>