$(document).ready(function() {
    $(".signin").click(function(e) {
        e.preventDefault();
        $("fieldset#signin_menu").toggle();
        $(".signin").toggleClass("menu-open");
    });

    $("fieldset#signin_menu").mouseup(function() {
        return false
    });
    $(document).mouseup(function(e) {
        if($(e.target).parent("a.signin").length==0) {
            $(".signin").removeClass("menu-open");
            $("fieldset#signin_menu").hide();
        }
    });            

	var loginError =  getURLParameter("login_error");
	if(loginError) {
		$("#loginError").html("Login Failed, Reason: " + loginError).show();
        $("fieldset#signin_menu").toggle();
        $(".signin").toggleClass("menu-open");
	}
	else
		$("#loginError").html("").hide();
	

	$('#forgotAccountForm').dialog({
	    autoOpen: false,
	    position:['middle',20],
	    width: 400,
	    height: 250,
	    modal: true,
	    resizable: false,
	    buttons: {
	        "Close": function() {
	            $(this).dialog("close");
	        },
	        "Submit": function() {
	            processForgotAccountFormSubmit();
	        }
	    }
	});

});

function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
}

function forgotPassword() {
	$("#forgotAccountFormErrors").html("");
	$("#actionType").val("recoverPassword");
	$("#forgotAccountForm").dialog("option", "title", "Recover User Account Login Password");  
	$(":button:contains('Submit')").prop("disabled",false).removeClass( 'ui-state-disabled'); 
	$('#forgotAccountForm').dialog('open');
}

function forgotUsername() {
	$("#forgotAccountFormErrors").html("");
	$("#actionType").val("recoverUsername");
	$("#forgotAccountForm").dialog("option", "title", "Recover User Account Login Name");  
	$(":button:contains('Submit')").prop("disabled",false).removeClass( 'ui-state-disabled'); 
	$('#forgotAccountForm').dialog('open');
}

function processForgotAccountFormSubmit() {
	if ($("#email").val() == "") {
		$("#forgotAccountFormErrors").append("<div class=\"error\">Please enter the <b>Email Address</b>.</div>");
		$("#forgotAccountFormErrors").show();
		return;
	 }	
	
	$.ajax({
	    type: 'GET',
	    url: 'users/getUserByEmailAddress/' + $("#email").val() + "/",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
	    success: function(data) {processGetUserByEmailResults(data, $("#actionType").val());},
	    error: function(xhr,err) {
			$("#forgotAccountFormErrors").html("<div class=\"error\">" + xhr.responseText + "</div>");
			$("#forgotAccountFormErrors").show();
	    }
	});			    
}

function processGetUserByEmailResults(user, actionType) {
	// if we got here, it means the email entered is legit and we need to reqest a temp password or request the users username and send to this user
	var url
	if (actionType == "recoverPassword") 
	    url = "users/recoverAccountPassword";		
	else
		url = "users/recoverAccountUsername";	

	$("#forgotAccountFormErrors").html("");
	
	$.ajax({
	    type: 'POST',
	    url: url,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
	    data: JSON.stringify(user),
	    success: function(data) {processRecoverAccountDetailsResults(data);},
	    error: function(xhr,err) {
			$("#forgotAccountFormErrors").html("<div class=\"error\">" + xhr.responseText + "</div>");
			$("#forgotAccountFormErrors").show();
	    }
	});			    
}

function processRecoverAccountDetailsResults(emailMessageResponseStatus) {
	var html = emailMessageResponseStatus.sucess == true ? "<div class=\"success\">" : "<div class=\"error\">";	
	$("#forgotAccountFormErrors").append(html + emailMessageResponseStatus.message + "</div>");
	$("#forgotAccountFormErrors").show();
	$(":button:contains('Submit')").prop("disabled",true).addClass( 'ui-state-disabled'); 
}