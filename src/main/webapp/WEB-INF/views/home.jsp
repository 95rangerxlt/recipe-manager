<%@ include file="/resources/frameworks/topFramework.html" %>
<script src="resources/js/recipeManager.js" type="text/javascript"></script>

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
	      <th width="15%">Title</th>
	      <th>Description</th>
	      <th width="20%">URL</th>
	      <th width="15%">Notes</th>
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

<div class="demo_jui" style="margin:20px;" >
<div id="recipeForm" title="" style="display:none;margin-top:5px;">
  <input type="text" style="display:none" value="" id="recipeContributerId" />
  <div id="recipeFormErrors" style="display:none;margin:0px;"><div class="error"></div></div>
  <table width="100%" class="display" cellspacing="0" cellpadding="0"><tbody>
    <tr><td><b>Title</b></td><td><b>URL</b></td></tr>
    <tr>
      <td valign="top"><input type="text" required id="recipeTitle" size="55" value=""/></td>
      <td valign="top"><input type="url" id="recipeUrl" placeholder="http://www.baltimoreravens.com" size="60" value=""/></td>
    </tr>
    <tr style="height:10px;"><td colspan="3"></td></tr>
    <tr><td colspan="3"><b>Notes</b></td></tr>
    <tr><td colspan="3"><textarea rows="3" cols="100" title="Enter a brief description"  id="recipeNotes"  value=""></textarea></td></tr>
    <tr style="height:10px;"><td colspan="3"></td></tr>
    <tr><td colspan="3"><b>Description</b></td></tr>
    <tr><td colspan="3"><textarea rows="8" cols="100" title="Enter a brief description"  id="recipeDescription"  value=""></textarea></td></tr>
  </tbody></table>
</div>
</div>

<div id="recipeDeleteForm" title="" style="display:none;margin-top:5px;">
  <input type="text" style="display:none" id="recipeId" value="0" />
  <div id="recipeDeleteFormErrors" style="display:none;margin:0px;"></div>
</div>


<%@ include file="/resources/frameworks/bottomFramework.html" %>
 


