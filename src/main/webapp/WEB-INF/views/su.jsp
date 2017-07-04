<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>   
<!DOCTYPE html>
<!-- saved from url=(0108)http://www.17sucai.com/preview/605665/2016-10-13/web%E8%8E%B7%E5%8F%96%E9%AA%8C%E8%AF%81%E7%A0%81/index.html -->
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">

<title>无标题文档</title>
<link rel="stylesheet" href="resources/web/phone.css">
<link rel="stylesheet" type="text/css" href="resources/web/weui.min.css">
<link rel="stylesheet" type="text/css" href="resources/web/jquery-weui.css">
<link rel="stylesheet" type="text/css" href="resources/web/demos.css">
<link rel="stylesheet" href="resources/css/bootstrap.min.css"/>
<script type="text/javascript" src="resources/lib/layer/2.4/layer.js"></script>
<style type="text/css">
.tb960x90 {display:none!important;display:none}</style>
</head>

<body>
<form style="margin:8px" action="http://www.17sucai.com/preview/605665/2016-10-13/web%E8%8E%B7%E5%8F%96%E9%AA%8C%E8%AF%81%E7%A0%81/index.html#" method="post">
  <h3 class="demos-title" style="margin-bottom:50px; margin-top:50px">手机号绑定</h3>
		<div class="weui_cell">
			<div class="weui_cell_hd">
				<lab el="" class="weui_label">设备编号： </lab>
			</div>
			<div class="weui_cell_bd weui_cell_primary">
				<input class="weui_input" type="tel" id="tell" name="tell"
					placeholder="请输入设备编号">
			</div>
		</div>
		<div class="weui_cell">
			<div class="weui_cell_hd">
				<lab el="" class="weui_label">选择列表： </lab>
			</div>
			<div class="weui_cell_bd weui_cell_primary">
				<select class="form-control">
					<option>1</option>
					<option>2</option>
					<option>3</option>
					<option>4</option>
					<option>5</option>
					<option>6</option>
					<option>7</option>
					<option>8</option>
					<option>9</option>
					<option>10</option>
					<option>11</option>
					<option>12</option>
				</select>
			</div>
		</div>

		<div class="weui_cell">
    <div class="weui_cell_hd">
      <lab el="" class="weui_label">手机号：
    </lab></div>
    <div class="weui_cell_bd weui_cell_primary">
      <input class="weui_input" type="tel" id="tell" name="tell" placeholder="请输入手机号">
    </div>
  </div>
  <div class="weui_cell">
    <div class="weui_cell_hd">
      <label el="" class="weui_label">验证码：</label>
    </div>
    <div class="weui_cell_bd weui_cell_primary">
      <input class="weui_input" type="certifycode" id="certifycode" name="certifycode" placeholder="请输入验证码">
    </div>
    <div class="weui_cell_ft"> 
    
    <input style="width:117px;" type="button" class="weui_btn weui_btn weui_btn_mini weui_btn_primary" value="获取验证码" onclick="clickButton(this)">  </div>
  </div>
   <div class="weui_cell"></div>
  <div class="weui_btn_area" style="margin-top:80px" align="center">
   <button align="center" type="button" class="btn btn-success borrow" oper="borrow" key="${umbrellaSta.key}" >借</button>
	&nbsp;&nbsp; 
   <button align="center" type="button" class="btn btn-danger reback"  oper="reback" key="${umbrellaSta.key}">还</button>
  </div>
</form>
<script type="text/javascript" src="resources/web/jquery-3.1.0.min.js"></script> 
<script type="text/javascript">
function clickButton(obj){
    var obj = $(obj);
    obj.attr("disabled","disabled");/*按钮倒计时*/
    var data={  		
       		userName:"18817677902",		
       	 }
       
       	 jQuery.ajax({
       		 type: 'POST',
       		 url: "register-verifycode",
       		 data:data,
       		 dataType: 'json',
       		 success: function(json) { 
       	   		
       			if(json.status==1){ 			
       				cur.attr("umbrella-sta",false)			
       			 	layer.msg("验证码已发出");	
       			/* 	var td = cur.parents("tr").find(".td-status");
       				td.find("span").text("已借出"); 
       			 	location.reload();*/     				
       			}else{
       			 	layer.msg(json.content);
       			}
       		} 
       	});
    var time = 60;
    var set=setInterval(function(){
    obj.val(--time+"(s)");
    }, 1000);/*等待时间*/
    setTimeout(function(){
    obj.attr("disabled",false).val("重新获取验证码");/*倒计时*/
    clearInterval(set);
    }, 60000);
}
jQuery(document).on('click', ".borrow", function() {
	
	   
	var cur = $(this);	

	/*  var tr1 = this.parentNode.parentNode;  
     alert(tr1.rowIndex);  
     alert(tr1.cells[0].childNodes[0].value);
     alert(tr1.cells[1].innerText);   */  
	var data={  		
   		devUuid:"${uuid}",
   		umId:cur.attr("key"),
   		operate:cur.attr("oper"), 		
   	 }
   
   	 jQuery.ajax({
   		 type: 'POST',
   		 url: "web-bar",
   		 data:data,
   		 dataType: 'json',
   		 success: function(json) { 
   	   		
   			if(json.status==1){ 			
   				cur.attr("umbrella-sta",false)			
   			 	layer.msg("借伞成功");	
   				var td = cur.parents("tr").find(".td-status");
   				td.find("span").text("已借出");
   			 	location.reload();
   				
   			}else{
   			 	layer.msg("借伞失败");
   			}
   		} 
   	});
});	
/*
 * 还伞
 */
jQuery(document).on('click', ".reback", function() {
	
	var cur = $(this);
	if(cur.attr("umbrella-sta")=="true"){
		layer.msg("该伞位已经有伞，清选择其它伞位");
		return;
	}	
	
	var data={
			devUuid:"${uuid}",
	   		umId:cur.attr("key"),
	   		operate:cur.attr("oper"), 
   	 }
   	 jQuery.ajax({
   		 type: 'POST',
   		 url: "barUmAdmin-mobile",
   		 data:data,
   		 dataType: 'json',
   		 success: function(json) { 
   			if(json.status==1){
   				cur.attr("umbrella-sta",true)			
   			 	
   				var td = cur.parents("tr").find(".td-status");
   				td.find("span").text("未借出");
   			  				
   			 	layer.msg("还伞成功");	
   			    location.reload();
   			}else{
   			 	layer.msg("还伞失败");
   			}
   		} 
   	});
});	

</script>


</body></html>