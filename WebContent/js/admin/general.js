$(document).ready(function() {
	/* 实现全选/反选 */
	$("#checkAll").click(function() {
		var obj = document.getElementById("checkAll");
		var form = document.getElementById("formList");
		for(var i=0; i<form.length; i++) {
			if (form.elements[i].type == "checkbox") {
				form.elements[i].checked = obj.checked;
			}
		}
	});
	
	/* 检查是否应该勾选全选框 */
	$(".checkbox").click(function() {
		var cb = $(this).get(0);
		var form = cb.form;
		var flg = true;
		for(var i=0; i<form.length; i++) {
			if (form.elements[i].type == "checkbox" && form.elements[i].id != "checkAll") {
				if (form.elements[i].checked != cb.checked) {
					flg = false;
					break;
				}
			}
		}
		var cbAll = $("#checkAll").get(0);
		if (flg == true) {
			cbAll.checked = cb.checked;
		} else {
			cbAll.checked = false;
		}
	});
	
	/* 删除确认框 */
	$(".delete").click(function(){
		return confirm("确认要删除选定的记录？删除后将无法恢复。");
	});
	
	$("#createTime").datepicker({
		defaultDate: "+1w",
		showOn: "both",
		buttonImage: "../images/calendar.gif",
	    buttonImageOnly: true,
		showAnim: "show",
		showButtonPanel: true,
		changeMonth: true,
		changeYear: true
	});
	$( "#startTime" ).datepicker({
		defaultDate: "+1w",
		showOn: "both",
		buttonImage: "../images/calendar.gif",
	    buttonImageOnly: true,
		showAnim: "show",
		showButtonPanel: true,
		changeMonth: true,
		changeYear: true,
		onClose: function( selectedDate ) {
			$( "#endTime" ).datepicker( "option", "minDate", selectedDate );
		}
	});
	$( "#endTime" ).datepicker({
		defaultDate: "+1w",
		showOn: "both",
		buttonImage: "../images/calendar.gif",
	    buttonImageOnly: true,
		showAnim: "show",
		showButtonPanel: true,
		changeMonth: true,
		changeYear: true,
		onClose: function( selectedDate ) {
			$( "#startTime" ).datepicker( "option", "maxDate", selectedDate );
		}
	});
});

/* 检查输入页数是否正确，total为总页数，页数输入框ID必须为txtPage */
function checkPage(total) {
	var txtPage = document.getElementById("txtPage");
	var numcheck = /\d/;
	if(numcheck.test(txtPage.value)==false || txtPage.value<1 || txtPage.value>total) {
		alert("页码无效，请重新输入。");
		return false;
	}
}

/* 跳转文本框Keypress事件 */
function txtPageKeyPress(e)
{
	var keynum;
	/* 获取按下按键的code */
	if(window.event) // IE
	{
		keynum = e.keyCode;
	}
	else if(e.which) // Netscape/Firefox/Opera
	{
		keynum = e.which;
	}

	if(keynum == 13) // 按下回车时
	{
		$("#goto").click(); // 触发跳转按钮点击事件
		return false; // 阻止submit
	}
	else // 按下其它键时
	{
		var keychar = String.fromCharCode(keynum); // 获取按键字符
		var numcheck = /\d/; // 匹配数值的正则表达式
		return numcheck.test(keychar); // 输入非数字时阻止输入
	}
}

function selectChanged(id)
{
	var selector = document.getElementById(id);
	selector.form.submit();
}