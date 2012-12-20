<%@ include file="/resources/frameworks/topFramework.html" %>
<script src="resources/js/eventLogManager.js" type="text/javascript"></script>

<div id="content" style="width:90%;">
    <h2>Event Log Administration</h2>
	<div style="float:left;margin:10px;"><button id="logoutButton">Logout</button></div>  
	<div style="float:left;margin:10px;"><button id="recipesButton">Recipes</button></div>  
	<div style="float:left;margin:10px;"><button id="usersButton">Users</button></div>  
	<div style="float:left;margin:10px;"><button id="myAccountButton">My Account</button></div> 	

	<table border="0" class="display" id="eventLogsTable" >
	  <caption class="fg-toolbar ui-widget-header"><h2 style="margin:5px;">Recent Activity</h2></caption>
	  <thead>
	    <tr>
	      <th>Person</th>
	      <th>When</th>
	      <th>Log Type</th>
	      <th>Log Data</th>
	    </tr>
	  </thead>
	  <tbody>
	</tbody>
	</table>
</div>

<%@ include file="/resources/frameworks/bottomFramework.html" %>
