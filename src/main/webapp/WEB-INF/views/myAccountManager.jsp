<%@ include file="/resources/frameworks/topFramework.html" %>
<script src="resources/js/myAccount.js" type="text/javascript"></script>

<div id="content" style="width:90%;">
    <h2>User Administration</h2>
	<div style="float:left;margin:10px;"><button id="logoutButton">Logout</button></div>  
	<div style="float:left;margin:10px;"><button id="eventLogButton">Event Log</button></div>  
	<div style="float:left;margin:10px;"><button id="recipesButton">Recipes</button></div>  
	<div style="float:left;margin:10px;"><button id="usersButton">Users</button></div>  
</div>

<div id="userForm" title="" style="display:none;margin-top:5px;">
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
      <td><input title="Enter a valid email address" type="email" placeholder="ie. johndoe@yahoo.com" id="emailAddress" required size="40" value=""/></td>
    </tr>
    <tr style="height:10px;"><td colspan="3"></td></tr>
    <tr>
      <td><b>User Name:</b></td>
      <td><b>Password:</b></td>
      <td><b>Password Confirm:</b></td>
      <td></td>
    </tr>
    <tr>
      <td><input type="text" id="userName" readonly size="30" pattern="[a-zA-Z0-9]{5,}" title="Minimum 5 letters or numbers." /></td>     
      <td><input id="password" name="password" size="30" type="password" pattern=".{5,}" title="Minmimum 5 letters or numbers." required></td>
      <td><input id="passwordConfirm" name="password" size="30" type="password" pattern=".{5,}" title="Minmimum 5 letters or numbers." required value=""/></td>
      <td></td>
    </tr>
  </tbody></table>
</div>

<%@ include file="/resources/frameworks/bottomFramework.html" %>
