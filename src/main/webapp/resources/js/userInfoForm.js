//globals
var userInfo=null;

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
	
	$('#emailMessageForm').dialog({
		title: 'Send a Message',
	    autoOpen: false,
	    position:['middle',80],
	    width: 500,
	    height: 300,
	    modal: true,
	    resizable: false,
	    buttons: {
	        "Close": function() {
	            $(this).dialog("close");
	        },
	        "Submit": function() {
	            processSubmitEmailMessage();
	        }
	    }
	});
	
}

function showUserInfoForm(userName, recipeTitle) {
	$("#userInfoFormErrors").html("");
	$("#userInfoFormFirstName").html("");
	$("#userInfoFormLastName").html("");
	$("#userInfoFormEmailAddress").html("");
	$("#userInfoFormUserName").html("");
		
	$.ajax({
        type: 'GET',
        url: 'users/userInfo/' + userName,
        success: function(data) {processGetUserInfoResults(data, recipeTitle);},
        error : function(request, status, error) {$("#userInfoFormErrors").html(error); $("#userInfoFormErrors").show();}
	});	
	
	$('#userInfoForm').dialog('open');
}

function showEmailMessageForm(userName, recipeTitle) {
	$("#emailMessageFormErrors").html("");
	$(":button:contains('Submit')").prop("disabled",false).removeClass( 'ui-state-disabled'); 

	var toText = userInfo.firstName + " " + userInfo.lastName + " <" + userInfo.emailAddress + ">";
	$("#emailMessageTo").val(toText);
	$("#emailMessageSubject").val(recipeTitle);
	$("#emailMessageBody").val("");
	$("#emailMessageBody").focus();
	
	$('#emailMessageForm').dialog('open');
}

function processGetUserInfoResults(user, recipeTitle) {
	var elink = '<div style="margin-top:5px;"><a  style="color:black;font-size:1.1em;" href="javascript:showEmailMessageForm(' + "'" + user.userName +  "','" + recipeTitle + "'"  + ');' + '"' + '>send a message to <b>' + user.firstName + '</b></a></div>';

	$("#userInfoFormFirstName").html(user.firstName);
	$("#userInfoFormLastName").html(user.lastName);
	$("#userInfoFormEmailAddress").html(user.emailAddress + elink);
	$("#userInfoFormUserName").html(user.userName);
	
	userInfo = user;
}

function processSubmitEmailMessage() {
	$("#emailMessageFormErrors").show();

	if (! validateEmailMessageForm()) {
	  return;
	}
		
	// got this far, do the post and check for errors 
	$("#emailMessageFormErrors").append("<div class=\"success\">Sending...</div>");
	
	var message = new Object();
	message.subject = $("#emailMessageSubject").val();
	message.body = $("#emailMessageBody").val();
	message.receiverUsername = userInfo.userName;

	$.ajax({
	       type: 'POST',
	       url: 'email',
	       contentType: "application/json; charset=utf-8",
	       dataType: "json",
	       data: JSON.stringify(message),
	       success: function(data) {processSubmitEmailMessageResults(data);},
	       error: function(xhr,err) {
	   	    	$("#emailMessageFormErrors").append("<div class=\"error\">" + xhr.responseText + "</div>");
	   	    	$(":button:contains('Submit')").prop("disabled",true).addClass("ui-state-disabled");	
	    	}
	});			    

	
}

function processSubmitEmailMessageResults(emailMessageResponseStatus) {
	var html = emailMessageResponseStatus.sucess == true ? "<div class=\"success\">" : "<div class=\"error\">";	
	$("#emailMessageFormErrors").append(html + emailMessageResponseStatus.message + "</div>");
	$(":button:contains('Submit')").prop("disabled",true).addClass( 'ui-state-disabled'); 
}

function validateEmailMessageForm() {
	  $("#emailMessageFormErrors").html("");
	  var returnVal = true;
	  
	  if ($("#emailMessageSubject").val() == "") {
	    $("#emailMessageFormErrors").append("<div class=\"error\">Please enter text into the <b>Subject</b> field.</div>");
	    returnVal = false;
	  }
	  if ($("#emailMessageBody").val() == "") {
	    $("#emailMessageFormErrors").append("<div class=\"error\">Please enter text into the <b>Message</b> field.</div>");
	    returnVal = false;
	  }
	  return returnVal;
}

