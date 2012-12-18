 // globals
 var myUserObject;
 
 
$(document).ready(function () {
	$("#logoutButton").button();
	$("#logoutButton").click(function() { location.href = 'j_spring_security_logout'; });
	$("#eventLogButton").button();
	$("#eventLogButton").click(function() { location.href = 'eventLogManager'; });
	$("#recipesButton").button();
	$("#recipesButton").click(function() { location.href = 'recipeManager'; });
	$("#usersButton").button();
	$("#usersButton").click(function() { location.href = 'userManager'; });

	userFormInit();
  
	$.ajax({
	    type: 'GET',
	    url: 'users/currentUser',
	    success: function(data) {myUserObject = data; showUserForm(data);},
	    error : function(request, status, error) {alert("Failed: " + error);}
	});		
});

function userFormInit() {
	// auto trim all whitespace on text input fields
	$('input[type=text]').blur(function(){
	    $(this).val($.trim($(this).val()));
	});
	
	$('#userForm').dialog({
	    autoOpen: false,
	    position:['middle',20],
	    width: 725,
	    height: 375,
	    modal: true,
	    resizable: false,
	    buttons: {
	        "Close": function() {
	            $(this).dialog("close");
	        },
	        "Submit": function() {
	            processSubmit();
	        }
	    }
	});
}


function showUserForm(user) {
	 $("#userFormErrors").html("");
	 $("#userName").prop('disabled', true);
	 $('#userName').val(user.userName);
	 $("#firstName").val(user.firstName);
	 $("#lastName").val(user.lastName);
	 $("#emailAddress").val(user.emailAddress);
	 $("#password").val("");
	 $("#passwordConfirm").val("");
	 $("#userForm").dialog("option", "title", "Modify my User Information");  
		 	 
	 $('#userForm').dialog('open');
}
function processSubmit() {
	if (! validate()) {
	  $("#userFormErrors").show();
	  return;
	}
	
	// got this far, do the post and check for errors 
	$("#userFormErrors").append("<div class=\"success\">Saving...</div>");
	
	user = new Object();
	user.id = myUserObject.id;
	user.password = $("#password").val();
	user.userName = $("#userName").val();
	user.emailAddress = $("#emailAddress").val();
	user.firstName = $("#firstName").val();
	user.lastName = $("#lastName").val();
	user.roles = myUserObject.roles;
			
	$.ajax({
	       type: 'PUT',
	       url: 'users/myAccount',
           contentType: "application/json; charset=utf-8",
           dataType: "json",
	       data: JSON.stringify(user),
	       success: function(data) {processSubmitResults(data);},
	       error: function(xhr,err) {
	    	   alert("post data: " + JSON.stringify(user));
	    	   alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
	    	   alert("responseText: "+xhr.responseText);
	    	   $('#userForm').dialog('close');
	    	}
	       
	});			    
}
function validate() {
	  $("#userFormErrors").html("");
	  var returnVal = true;
	  if ($("#firstName").val() == "") {
	    $("#userFormErrors").append("<div class=\"error\">Please enter the user's <b>First Name</b>.</div>");
	    returnVal = false;
	  }
	  if ($("#lastName").val() == "") {
	    $("#userFormErrors").append("<div class=\"error\">Please enter the user's <b>Last Name</b>.</div>");
	    returnVal = false;
	  }
	  if ($("#emailAddress").val() == "") {
	    $("#userFormErrors").append("<div class=\"error\">Please enter the user's <b>Email Address</b>.</div>");
	    returnVal = false;
	  }
	  if ($("#password").val() == "") {
	    $("#userFormErrors").append("<div class=\"error\">Please enter a valid <b>Password</b>.</div>");
	    returnVal = false;
	  }
	  if ($("#passwordConfirm").val() == "") {
		    $("#userFormErrors").append("<div class=\"error\">Please enter a valid <b>Password Confirm</b>.</div>");
		    returnVal = false;
	  }
	  if ($("#password").val() != $("#passwordConfirm").val()) {
		    $("#userFormErrors").append("<div class=\"error\"><b>Password</b> and <b>Confirm Password</b> values do not match!</div>");
		    returnVal = false;		  
	  }
	  
	  return returnVal;
}

function processSubmitResults(user) {
	$("#userFormErrors").append("<div class=\"success\">Operation Successful...</div>");
	$("#userFormErrors").show();
}
