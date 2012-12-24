$(document).ready(function () {
  userInfoFormInit();

	usersTable = $('#usersTable').dataTable({
	    "bJQueryUI": true,
	    "iDisplayLength": 25,
	    "sPaginationType": "full_numbers",
	    "sDom": '<"fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix"lfr>t<"fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix"ip>'
	  });

});

function userInfoFormInit() {
	// auto trim all whitespace on text input fields
	$('input[type=text]').blur(function(){
	    $(this).val($.trim($(this).val()));
	});
	
	$('#userInfoForm').dialog({
		title: 'Recipe User Information',
	    autoOpen: false,
	    position:['middle',20],
	    width: 500,
	    height: 300,
	    modal: true,
	    resizable: false,
	    buttons: {
	        "Close": function() {
	            $(this).dialog("close");
	        },
	    }
	});
}

function showUserInfoForm(userName) {
	$("#userInfoFormErrors").html("");
	$("#userInfoFormFirstName").html("");
	$("#userInfoFormLastName").html("");
	$("#userInfoFormEmailAddress").html("");
	$("#userInfoFormUserName").html("");
		
	$.ajax({
        type: 'GET',
        url: 'users/userInfo/' + userName,
        success: function(data) {processGetUserInfoResults(data);},
        error : function(request, status, error) {$("#userInfoFormErrors").html(error); $("#userInfoFormErrors").show();}
	});	
	
	$('#userInfoForm').dialog('open');
}

function showSendMessageForm(userName) {
	$("#userInfoFormErrors").html("<div class=\"success\">Sending a message to user <b>" + userName + "</b> is not yet implemented!</div>").show();
}

function processGetUserInfoResults(user) {
	var elink = '<div style="margin-top:5px;"><a  style="color:blue;font-size:1.1em;" href="javascript:showSendMessageForm(' + "'" + user.userName +  "'"  + ');' + '"' + '>send a message</a></div>';

	$("#userInfoFormFirstName").html(user.firstName);
	$("#userInfoFormLastName").html(user.lastName);
	$("#userInfoFormEmailAddress").html(user.emailAddress + elink);
	$("#userInfoFormUserName").html(user.userName);
}