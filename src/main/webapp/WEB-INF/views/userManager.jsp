<%@ include file="/resources/frameworks/topFramework.html" %>
<div id="content" style="width:90%;">
    <h2>User Administration</h2>
	<div style="float:left;margin:10px;"><button id="logoutButton">Logout</button></div>  
	<div style="float:left;margin:10px;"><button id="recipesButton">Recipes</button></div>  
    <div style="float:right;margin:10px;"><button id="addUserButton">Add User</button></div>  

	<table border="0" class="display" id="usersTable" >
	  <caption class="fg-toolbar ui-widget-header"><h2 style="margin:5px;">Users</h2></caption>
	  <thead>
	    <tr>
	      <th>ID</th>
	      <th>Username</th>
	      <th>Full Name</th>
	      <th>EMail </th>
	      <th>Roles</th>
	      <th width="5%">Action</th>
	    </tr>
	  </thead>
	  <tbody>
	</tbody>
	</table>
</div>

<div id="userForm" title="" style="display:none;margin-top:5px;">
  <input type="text" style="display:none" id="userId" value="0" />
  <div id="userFormErrors" style="display:none;margin:0px;"></div>
  <table width="100%" cellspacing="0" cellpadding="0"><tbody>
    <tr><td colspan="3" style="height:20px;color:blue;font-size:1.1em;">General</td></tr>  
    <tr>
      <td><b>First Name:</b></td>
      <td><b>Last Name</b></td>
      <td><b>Email Address</b></td>
    </tr>
    <tr>
      <td><input type="text" id="firstName" size="30" pattern="[a-zA-Z0-9]{3,}" title="Minimum 3 letters or numbers." required value=""/></td>
      <td><input type="text" id="lastName" size="30" pattern="[a-zA-Z0-9]{3,}" title="Minimum 3 letters or numbers." required value=""/></td>
      <td><input title="Enter a valid email address" type="email" placeholder="ie. johndoe@yahoo.com" id="emailAddress" size="40" value=""/></td>
    </tr>
    <tr style="height:10px;"><td colspan="3"></td></tr>
    <tr>
      <td><b>User Name:</b></td>
      <td><b>Password:</b></td>
      <td><b>Password Confirm:</b></td>
      <td></td>
    </tr>
    <tr>
      <td><input type="text" id="userName" size="30" pattern="[a-zA-Z0-9]{5,}" title="Minimum 5 letters or numbers." required value=""/></td>     
      <td><input id="password" name="password" size="30" type="password" pattern=".{5,}" title="Minmimum 5 letters or numbers." required></td>
      <td><input id="passwordConfirm" name="password" size="30" type="password" pattern=".{5,}" title="Minmimum 5 letters or numbers." required value=""/></td>
      <td></td>
    </tr>
    <tr style="height:15px;"><td colspan="3"></td></tr>
    <tr><td colspan="3" style="height:20px;color:blue;font-size:1.1em;">User Access</td></tr>
    <tr><td colspan="3"><table id="userRolesTable" width="100%" cellspacing="0" cellpadding="0"><tbody></tbody></table></td></tr>    
  </tbody></table>
</div>

<%@ include file="/resources/frameworks/bottomFramework.html" %>

 <script>
 // globals
 var usersTable;
 var roles;
 
 
$(document).ready(function () {
  $("#logoutButton").button();
  $("#logoutButton").click(function() { location.href = 'j_spring_security_logout'; });
  $("#recipesButton").button();
  $("#recipesButton").click(function() { location.href = 'recipeManager'; });
  $("#addUserButton").button();
  $("#addUserButton").click(function() { showUserForm("", ""); return false; });
  userFormInit();

	usersTable = $('#usersTable').dataTable({
	    "bJQueryUI": true,
	    "iDisplayLength": 25,
	    "sPaginationType": "full_numbers",
	    "sDom": '<"fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix"lfr>t<"fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix"ip>'
	  });

	$.get('users', function(data) {processUserList(data);}); 
	$.get('users/roles', function(data) {roles = data;}); 
	
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
	        "Cancel": function() {
	            $(this).dialog("close");
	        },
	        "Submit": function() {
	            processSubmit();
	        }
	    }
	});
	
	$('#userDeleteForm').dialog({
	    autoOpen: false,
	    width: 400,
	    height: 300,
	    modal: true,
	    resizable: false,
	    buttons: {
	        "Cancel": function() {
	            $(this).dialog("close");
	        },
	        "Delete User": function() {
	            processDeleteUserSubmit();
	        }
	    }
	});	
}

function processUserList(users) {
	console.log(JSON.stringify(users));
	$.each(users, function(i, user) {
		addUserRow(user);
	});
}

function addUserRow(user) {
	var roleString = "";
	$.each(user.roles, function (i, role) {
		roleString += "<div>" + role + "</div>";
	});
	
    var mlink = '<div style="margin-top:3px;"><a href="javascript:showUserForm(' + "'" + user.id +  "'," + "'" + user.userName +  "'" + ');">Modify</a></div>';
    var dlink = '<div style="margin-top:3px;"><a href="javascript:showUserDeleteForm(' + "'" + user.id +  "'," + "'" + user.userName + "'" + ');">Delete</a></div>';
    var newRow = usersTable.dataTable().fnAddData([user.id,
                                                   user.userName,
                                                   user.firstName + " " + user.lastName,
                                                   user.emailAddress,
                                                   roleString,
                                                   mlink + dlink]); 

    // update the dom for this new tr so we have the ids set correctly
    var newTr = usersTable.fnSettings().aoData[ newRow[0] ].nTr;
    var idp = user.id + "_";
    newTr.cells[0].id = idp + "id";
    newTr.cells[1].id = idp + "userName";
    newTr.cells[2].id = idp + "fullName";
    newTr.cells[3].id = idp + "emailAddress";
    newTr.cells[4].id = idp + "roles";		
}

function showUserForm(userId, userName) {
	 $("#userId").val(userId); 
	 $("#userFormErrors").html("");
	 if (userName == "")  {  // show the create form
		$("#userName").prop('disabled', false);
		$("#firstName").val("");
		$("#lastName").val("");
		$("#email").val("");
		$("#password").val("");
		$("#passwordConfirm").val("");
		$("#userName").val("");
		$("#userForm").dialog("option", "title", "Add a New User");  
	 }
	 else {            // show the edit form
	 	$("#userForm").dialog("option", "title", "Modify User");
	
		$.ajax({
	        type: 'GET',
	        url: 'users/' + userName,
	        success: function(data) {processGetUserResults(data);},
	        error : function(request, status, error) {alert("Failed: " + error);}
		});		
	 }  
	  // fill out the customer access table using the data received here
	  $("#userRolesTable > tbody").html("");
	  var count = 0;
	  var htmlToInsert = "";  
	  $.each(roles, function(index, value) { 
	    if (count % 3 == 0)
	      htmlToInsert += "<tr>";
	    htmlToInsert += '<td><input type="checkbox" id=' + '"userRole_' + value + '"' + '/><label style="margin:5px;" for="userRole_' + value + '">' + value + '</label></td>';
	    if (count % 3 == 2)
	      htmlToInsert += "</tr>";
	    count++;
	  });  
	  $("#userRolesTable > tbody").html(htmlToInsert);
	 	 
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
	user.id = $("#userId").val();
	user.password = $("#password").val();
	var roles = [];
	$('input[id^="userRole_"]:checked').each(function (index, value) {
		var idArray = value.id.split("userRole_");
		roles.push(idArray[1]);
	});
	user.roles = roles;
	user.userName = $("#userName").val();
	user.emailAddress = $("#emailAddress").val();
	user.firstName = $("#firstName").val();
	user.lastName = $("#lastName").val();	
	
	console.log(user);
	
	var type = userName == "" ? "POST" : "PUT";
	
	$.ajax({
	       type: type,
	       url: 'users',
	       contentType: "application/json",
	       dataType: "json",
	       data: JSON.stringify(user),
	       success: function(data) {processSubmitResults(data, type);},
	       error : function(request, status, error) {console.log(JSON.stringify(user)); alert("Failed: " + error);}
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
	  if ($("#userName").val() == "") {
	    $("#userFormErrors").append("<div class=\"error\">Please enter the user's <b>User Name</b>.</div>");
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

	  // userName must be at least 6 chars
	  if ($("#userName").val() && $("#userName").val().length < 5) {
	    $("#userFormErrors").append("<div class=\"error\">Minimum of 6 characters required for the user's <b>User Name</b>.</div>");
	    returnVal = false;
	  }
	  
	  return returnVal;
}
function processGetUserResults(user){
	console.log(JSON.stringify(user));
	$("#userId").val(user.id);
	$("#firstName").val(user.firstName);
	$("#lastName").val(user.lastName);
	$("#emailAddress").val(user.emailAddress);
	$("#userName").val(user.userName);
	$("#password").val(user.password);
	$("#passwordConfirm").val(user.password);
	$("#userName").prop('disabled', true);
	 
	$.each(user.roles,function(index, value) {
	  $("#userRole_" + value).attr('checked', true);  
	});
}
function processSubmitResults(user, type) {	
	$('#userForm').dialog('close');
    newTr.cells[0].id = idp + "id";
    newTr.cells[1].id = idp + "userName";
    newTr.cells[2].id = idp + "fullName";
    newTr.cells[3].id = idp + "emailAddress";
    newTr.cells[4].id = idp + "roles";		
	
	if (type == 'PUT') {
		var idp = user.id + "_";
		$("#" + idp + "userName").html(user.userName);
		$("#" + idp + "fullName").html(user.firstName + " " + user.lastName);
		$("#" + idp + "email").html(user.email);
		var roles = "";
		$.each(user.roles,function(index, value) {
			roles += "<div>" + value + "</div>";
		});
		$("#" + idp + "roles").html(roles);
	}
	else 
		addUserRow(user);
}



</script>
