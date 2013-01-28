// globals
var userInfoViewModel;
 
function UserInfoViewModel(){
	id = ko.observable();
	password = ko.observable();
	confirmPassword = ko.observable();
	userName = ko.observable();
	firstName = ko.observable();
	lastName = ko.observable();
	emailAddress = ko.observable();
	roles = ko.observable();	
}


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
	userInfoViewModel = new UserInfoViewModel();
	
    $.getJSON("users/currentUser", function(user) {
	 	userInfoViewModel.id = user.id;
	 	userInfoViewModel.userName = user.userName;
	 	userInfoViewModel.firstName = user.firstName;
	 	userInfoViewModel.lastName = user.lastName;
	 	userInfoViewModel.emailAddress = user.emailAddress;
	 	userInfoViewModel.password = user.password;
	 	userInfoViewModel.confirmPassword = user.password;
	 	userInfoViewModel.roles = user.roles; 

    	$("#userForm").dialog("option", "title", "Modify my User Information");  
	 	$('#userForm').dialog('open');   

		ko.applyBindings(userInfoViewModel, $("#myAccountInfo")[0]);
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
function processSubmit() {
	if (! validate()) {
	  $("#userFormErrors").show();
	  return;
	}
	
	// got this far, do the post and check for errors 
	$("#userFormErrors").append("<div class=\"success\">Saving...</div>");	
	
	var userInfoObject = new Object();
	userInfoObject.id = userInfoViewModel.id;
	userInfoObject.userName = userInfoViewModel.userName;
	userInfoObject.firstName = userInfoViewModel.firstName;
	userInfoObject.lastName = userInfoViewModel.lastName;
	userInfoObject.emailAddress = userInfoViewModel.emailAddress;
	userInfoObject.password = userInfoViewModel.password;
	userInfoObject.roles = userInfoViewModel.roles; 
				
	$.ajax({
	       type: 'PUT',
	       url: 'users/myAccount',
           contentType: "application/json; charset=utf-8",
           dataType: "json",
	       data: ko.toJSON(userInfoObject),
	       success: function(data) {processSubmitResults(data);},
	       error: function(xhr,err) {
	    	   alert("post data: " + ko.toJSON(userInfoObject));
	    	   alert("readyState: "+xhr.readyState+"\nstatus: "+xhr.status);
	    	   alert("responseText: "+xhr.responseText);
	    	   $('#userForm').dialog('close');
	    	}	       
	});			    
}
function validate() {
	  $("#userFormErrors").html("");
	  var returnVal = true;
	  if (userInfoViewModel.firstName == "") {
	    $("#userFormErrors").append("<div class=\"error\">Please enter the user's <b>First Name</b>.</div>");
	    returnVal = false;
	  }
	  if (userInfoViewModel.lastName == "") {
	    $("#userFormErrors").append("<div class=\"error\">Please enter the user's <b>Last Name</b>.</div>");
	    returnVal = false;
	  }
	  if (userInfoViewModel.emailAddress == "") {
	    $("#userFormErrors").append("<div class=\"error\">Please enter the user's <b>Email Address</b>.</div>");
	    returnVal = false;
	  }
	  if (userInfoViewModel.password == "") {
	    $("#userFormErrors").append("<div class=\"error\">Please enter a valid <b>Password</b>.</div>");
	    returnVal = false;
	  }
	  if (userInfoViewModel.confirmPassword == "") {
		    $("#userFormErrors").append("<div class=\"error\">Please enter a valid <b>Password Confirm</b>.</div>");
		    returnVal = false;
	  }
	  if (userInfoViewModel.password != userInfoViewModel.confirmPassword) {
		    $("#userFormErrors").append("<div class=\"error\"><b>Password</b> and <b>Confirm Password</b> values do not match!</div>");
		    returnVal = false;		  
	  }
	  
	  return returnVal;
}

function processSubmitResults(user) {
	$("#userFormErrors").append("<div class=\"success\">Operation Successful...</div>");
	$("#userFormErrors").show();
}
