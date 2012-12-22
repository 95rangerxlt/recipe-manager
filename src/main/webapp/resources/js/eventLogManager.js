$(document).ready(function () {
	$("#logoutButton").button();
	$("#logoutButton").click(function() { location.href = 'j_spring_security_logout'; });
	$("#recipesButton").button();
	$("#recipesButton").click(function() { location.href = 'recipeManager'; });
	$("#usersButton").button();
	$("#usersButton").click(function() { location.href = 'userManager'; });
    $("#myAccountButton").button();
    $("#myAccountButton").click(function() { location.href = 'myAccountManager'; });

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
    var newRow = eventLogsTable.dataTable().fnAddData([eventLog.actor,
                                                   	   new Date(eventLog.logDate),
                                                   	   eventLog.logType,
                                                   	   eventLog.logData]); 
}
