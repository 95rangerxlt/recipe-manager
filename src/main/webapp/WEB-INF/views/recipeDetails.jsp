<%@ include file="/resources/frameworks/topFramework.html" %>

<script src="resources/js/recipeDetails.js" type="text/javascript"></script>
<script src="resources/js/recipeManager.js" type="text/javascript"></script>
<script src="resources/js/userInfoForm.js" type="text/javascript"></script>

<!-- Load Pluploader Queue widget CSS and jQuery -->
<style type="text/css">@import url(resources/js/plupload/jquery.ui.plupload.css);</style>

<!-- Third party script for BrowserPlus runtime (Google Gears included in Gears runtime now) -->
<script type="text/javascript" src="http://bp.yahooapis.com/2.4.21/browserplus-min.js"></script>

<!-- Load plupload and all it's runtimes and finally the jQuery UI queue widget -->
<script type="text/javascript" src="resources/js/plupload/plupload.full.js"></script>
<script type="text/javascript" src="resources/js/plupload/jquery.ui.plupload.js"></script>


<div id="content" style="width:90%;">
    <h2>Recipe Details</h2>
	<div style="float:left;margin:10px;"><button id="logoutButton">Logout</button></div>  
	<div style="float:left;margin:10px;"><button id="eventLogButton">Event Log</button></div>  
    <div style="float:left;margin:10px;"><button id="recipesButton">Recipes</button></div>  
	<div style="float:left;margin:10px;"><button id="usersButton">Users</button></div>  
	<div style="float:left;margin:10px;"><button id="myAccountButton">My Account</button></div> 	
    <div style="float:right;margin:10px;"><button id="uploadPicsButton">Upload Recipe Pics</button></div>  
    

	<table border="0" class="display" id="recipeDetailsTable" >
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

<style type="text/css">
  .ui-widget { font-family: segoe ui, Arial, sans-serif; font-size: .95em; }
</style>


<div id="uploader">
	<p>You browser doesn't have Flash, Silverlight, Gears, BrowserPlus or HTML5 support.</p>
</div>

<div>
<h3 style="font-size:1.5em;">Uploaded Pics for this Recipe</h3>
<table id ="recipePicsTable"><tbody></tbody></table>
</div>

</div>

<div id="recipePicDeleteForm" title="" style="display:none;margin-top:5px;">
  <input style="display:none;" id = "recipedPicDeleteId"/>
  <div id="recipePicDeleteFormErrors" style="display:none;margin:0px;"></div>
  <div style="text-align:center;"><img height="150" width="150" id="recipePicDeleteFormImage"/></div>
</div>

<%@ include file="/resources/frameworks/recipeForms.html" %>
<%@ include file="/resources/frameworks/bottomFramework.html" %>
<%@ include file="/resources/frameworks/userInfoForm.html" %>
<%@ include file="/resources/frameworks/emailMessageForm.html" %>
 
