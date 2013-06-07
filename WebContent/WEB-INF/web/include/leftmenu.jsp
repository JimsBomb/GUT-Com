<!-- 左边栏菜单页 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<ul>
	<li class="menu">
		<h3>
			<a href="javascript:void(0);" title="折叠/展开">系统管理</a>
		</h3>
		<ul>
			<li class="menuitem"><a href="admin/sysconfmgr.do" target="mainFrame">基本设置</a></li>
			<!-- <li class="menuitem"><a href="#" target="mainFrame">计划任务</a></li> -->
			<li class="menuitem"><a href="admin/filtermgr.do" target="mainFrame">内容过滤</a></li>
			<li class="menuitem"><a href="admin/msgmgr.do" target="mainFrame">发布公告</a></li>
		</ul>
	</li>
</ul>
<ul>
	<li class="menu">
		<h3>
			<a href="javascript:void(0);" title="折叠/展开">用户管理</a>
		</h3>
		<ul>
			<li class="menuitem"><a href="admin/usercreate.do" target="mainFrame">添加用户</a></li>
			<li class="menuitem"><a href="admin/usermgr.do" target="mainFrame">管理用户</a></li>
			<li class="menuitem"><a href="admin/userauditmgr.do" target="mainFrame">审核用户</a></li>
		</ul>
	</li>
</ul>
<ul>
	<li class="menu">
		<h3>
			<a href="javascript:void(0);" title="折叠/展开">微博管理</a>
		</h3>
		<ul>
			<li class="menuitem"><a href="admin/weibomgr.do" target="mainFrame">管理微博</a></li>
			<li class="menuitem"><a href="admin/weiboauditmgr.do" target="mainFrame">审核微博</a></li>
			<li class="menuitem"><a href="admin/topicmgr.do" target="mainFrame">管理话题</a></li>
			<li class="menuitem"><a href="admin/wbReportmgr.do" target="mainFrame">管理举报</a></li>
		</ul>
	</li>
</ul>
<ul>
	<li class="menu">
		<h3>
			<a href="javascript:void(0);" title="折叠/展开">数据管理</a>
		</h3>
		<ul>
			<!-- <li class="menuitem"><a href="admin/dbopmgr.do" target="mainFrame">备份/恢复</a></li> -->
			<!-- <li class="menuitem"><a href="admin/cachemgr.do" target="mainFrame">更新缓存</a></li> -->
			<li class="menuitem"><a href="admin/syslogmgr.do" target="mainFrame">管理日志</a></li>
		</ul>
	</li>
</ul>
<ul>
	<li class="menu">
		<h3>
			<a href="javascript:void(0);" title="折叠/展开">登录管理</a>
		</h3>
		<ul>
			<li class="menuitem"><a href="admin/changepwd.do" target="mainFrame">更改密码</a></li>
			<li class="menuitem"><a href="admin/logout.do" target="mainFrame">退出登录</a></li>
		</ul>
	</li>
</ul>