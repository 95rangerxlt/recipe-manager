<%@ include file="/resources/frameworks/topFramework.html" %>
<script src="resources/js/recipeManager.js" type="text/javascript"></script>
<script src="resources/js/userInfoForm.js" type="text/javascript"></script>

<div id="content" style="width:90%;">
    <h2>Recipe Administration</h2>
	<div style="float:left;margin:10px;"><button id="logoutButton">Logout</button></div>  
	<div style="float:left;margin:10px;"><button id="eventLogButton">Event Log</button></div>  
	<div style="float:left;margin:10px;"><button id="usersButton">Users</button></div>  
	<div style="float:left;margin:10px;"><button id="myAccountButton">My Account</button></div> 	
    <div style="float:right;margin:10px;"><button id="addRecipeButton">Add Recipe</button></div>  

	<table border="0" class="display" id="recipesTable" >
	  <caption class="fg-toolbar ui-widget-header"><h2 style="margin:5px;">Recipes</h2></caption>
	  <thead>
	    <tr>
	      <th width="10%">Contributer</th>
	      <th width="12%">Title</th>
	      <th>Description</th>
	      <th width="20%">URL</th>
	      <th width="25%">Notes</th>
	      <th width="5%">Action</th>
	    </tr>
	  </thead>
	  <tbody>
	</tbody>
	</table>
</div>


<style type="text/css">
  .ui-widget { font-family: segoe ui, Arial, sans-serif; font-size: .95em; }
</style>




<%@ include file="/resources/frameworks/bottomFramework.html" %>
<%@ include file="/resources/frameworks/recipeForms.html" %>
<%@ include file="/resources/frameworks/userInfoForm.html" %>
<%@ include file="/resources/frameworks/emailMessageForm.html" %>
