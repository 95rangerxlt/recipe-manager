<%@ include file="/resources/frameworks/topFramework.html" %>
<div id="content" style="width:90%;">
    <h2>Event Log Administration</h2>
	<div style="float:left;margin:10px;"><button id="logoutButton">Logout</button></div>  
	<div style="float:left;margin:10px;"><button id="recipesButton">Recipes</button></div>  
	<div style="float:left;margin:10px;"><button id="usersButton">Users</button></div>  

	<table border="0" class="display" id="eventLogsTable" >
	  <caption class="fg-toolbar ui-widget-header"><h2 style="margin:5px;">Recent Activity</h2></caption>
	  <thead>
	    <tr>
	      <th>Person</th>
	      <th>When</th>
	      <th>Log Data</th>
	    </tr>
	  </thead>
	  <tbody>
	</tbody>
	</table>
</div>

<%@ include file="/resources/frameworks/bottomFramework.html" %>

 <script>
$(document).ready(function () {
	$("#logoutButton").button();
	$("#logoutButton").click(function() { location.href = 'j_spring_security_logout'; });
	$("#recipesButton").button();
	$("#recipesButton").click(function() { location.href = 'recipeManager'; });
	$("#usersButton").button();
	$("#usersButton").click(function() { location.href = 'userManager'; });

	eventLogsTable = $('#eventLogsTable').dataTable({
	    "bJQueryUI": true,
	    "iDisplayLength": 25,
	    "aaSorting": [[ 1, "desc" ]],
	    "sPaginationType": "full_numbers",
	    "sDom": '<"fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix"lfr>t<"fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix"ip>'
	  });

	$.get('eventLogs', function(data) {processEventLogList(data);}); 	
});

function processEventLogList(eventLogs) {
	$.each(eventLogs, function(i, eventLog) {
		addEventLogRow(eventLog);
	});
}

function addEventLogRow(eventLog) {
    var newRow = eventLogsTable.dataTable().fnAddData([eventLog.person,
                                                   	   new Date(eventLog.logDate),
                                                   	   eventLog.logData]); 
}

</script>