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
<title>管理员列表</title>
</head>
<body>
 <c_rt:choose>
  <c_rt:when test="${status}">
  <span class="r">共有伞架：<strong>2</strong> 套</span> </div>
	<table class="table table-border table-bordered table-bg">
		<thead>
			<tr>
				<th scope="col" colspan="9">用户列表</th>
			</tr>
			<tr class="text-c">
				
				<th width="40">用户ID</th>
				<th width="150">用户名</th>
				<th width="90">角色</th>			
				<th width="130">加入时间</th>
				<th width="130">借伞状态</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
		
		<c_rt:forEach items= "${userUnAuth}" var="userUnAuth">
		<tr class="text-c">
			
				<td>${userUnAuth.id}</td>
				<td>${userUnAuth.userName}</td>
				<c_rt:choose>
					<c_rt:when test="${userUnAuth.type}==1000"><td>超级管理员</td></c_rt:when>
					<c_rt:otherwise><td>普通用户</td></c_rt:otherwise>
				</c_rt:choose>			
				<td>${userUnAuth.time}</td>
				<c_rt:choose>
					<c_rt:when test="${userUnAuth.userAuth}"><td class="td-status"><span class="label label-success radius">已授权</span></td></c_rt:when>
					<c_rt:otherwise><td class="td-status"><span class="label radius">未授权</span></td></c_rt:otherwise>
				</c_rt:choose>	
				
				<td class="td-manage">
				
				<c_rt:choose>
					<c_rt:when test="${userUnAuth.userAuth}">
					 <button align="center" type="button" class="btn btn-danger unauth"  oper="unauth"  key="${userUnAuth.userName}">撤销授权</button></c_rt:when>
					<c_rt:otherwise>
					 &nbsp;&nbsp; 
					 <button align="center" type="button" class="btn btn-success auth" oper="auth" key="${userUnAuth.userName}" >授权</button>
				   </c_rt:otherwise>	
				</c_rt:choose>	
				</td>
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
/*
 * 授权操作
 */

 jQuery(document).on('click', ".auth", function() {
 	
 	var cur = $(this);	
 	
 	
 	
 	/*  var tr1 = this.parentNode.parentNode;  
      alert(tr1.rowIndex);  
      alert(tr1.cells[0].childNodes[0].value);
      alert(tr1.cells[1].innerText);   */  
 	var data={  		
    		
    		userName:cur.attr("key"),
    		operate:cur.attr("oper"), 		
    	 }
    
    	 jQuery.ajax({
    		 type: 'POST',
    		 url: "userAuthAjax",
    		 data:data,
    		 dataType: 'json',
    		 success: function(json) { 
    	   		
    			if(json.status==1){ 			
    						
    			 	layer.msg("授权成功");	
    				/* var td = cur.parents("tr").find(".td-status");
    				td.find("span").text("已借出"); */
    			 	location.reload();   				
    			}else{
    			 	layer.msg("授权失败");
    			}
    		} 
    	});
 });	


/*管理员-增加*/
function admin_add(title,url,w,h){
	layer_show(title,url,w,h);
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
function admin_edit(title,url,id,w,h){
	layer_show(title,url,w,h);
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
		
  </c_rt:when>
  
  <c_rt:otherwise>
  		<% response.sendRedirect("login"); %>
  		<%-- <jsp:forward page="home.jsp"></jsp:forward>  --%>
  </c_rt:otherwise>
  </c_rt:choose>

	 
</body>
</html>