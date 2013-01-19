<%@ include file="/resources/frameworks/topFramework.html" %>
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

<div><h3>Uploaded Pics go here</h3></div>

</div>

<%@ include file="/resources/frameworks/bottomFramework.html" %>
<%@ include file="/resources/frameworks/userInfoForm.html" %>
<%@ include file="/resources/frameworks/emailMessageForm.html" %>
 
<script type="text/javascript">
var recipeId;

$(document).ready(function () {
	  $("#eventLogButton").button();
	  $("#eventLogButton").click(function() { location.href = 'eventLogManager'; });
	  $("#recipesButton").button();
	  $("#recipesButton").click(function() { location.href = 'recipeManager'; });
	  $("#logoutButton").button();
	  $("#logoutButton").click(function() { location.href = 'j_spring_security_logout'; });
	  $("#usersButton").button();
	  $("#usersButton").click(function() { location.href = 'userManager'; });
	  $("#myAccountButton").button();
	  $("#myAccountButton").click(function() { location.href = 'myAccountManager'; });
	  $("#uploadPicsButton").button();
	  $("#uploadPicsButton").click(function() { $("#uploader").toggle(); });

	  recipeDetailsTable = $('#recipeDetailsTable').dataTable({
		"bJQueryUI": true,
		"iDisplayLength": 25,
		"sPaginationType": "full_numbers",
		"sDom": '<"fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix"lfr>t<"fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix"ip>'
	  });
	
	$("#uploader").hide();
	  
	recipeId = getURLParameter("recipeId");
	$.get('recipes/' + recipeId, function(data) {addRecipeRow(data);});  
	$.get('recipes/recipePics/' + recipeId, function(data) {addRecipePics(data);});  
	
});

function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
}

function addRecipeRow(recipe) {
	if ($("#" + recipe.recipeId + "_contributer").length > 0)
		return;
	
    var clink = recipe.contributerUserName != "" ? '<div style="margin-top:3px;"><a href="javascript:showUserInfoForm(' + "'" + recipe.contributerUserName +  "','" + recipe.title.replace(/'/g, '')  + "'" + ');">' + recipe.contributerUserName + '</a></div>' : "";
    var mlink = '<div style="margin-top:3px;"><a href="javascript:showRecipeForm(' + "'" + recipe.recipeId +  "'" + ');">Modify</a></div>';
    var dlink = '<div style="margin-top:3px;"><a href="javascript:showRecipeDeleteForm(' + "'" + recipe.recipeId + "'" + ');">Delete</a></div>';
    var newRow = recipeDetailsTable.dataTable().fnAddData([clink,
                                                     	  recipe.title,
                                                     	  recipe.description,
                                                     	  '<a target="_blank" href="' + recipe.url + '">' + recipe.url + '</a>',
                                                     	  recipe.notes,
                                                          mlink + dlink]); 
    
    // update the dom for this new tr so we have the ids set correctly
    var newTr = recipeDetailsTable.fnSettings().aoData[ newRow[0] ].nTr;
    var idp = recipe.recipeId + "_";
    newTr.cells[0].id = idp + "contributer";
    newTr.cells[1].id = idp + "title";
    newTr.cells[2].id = idp + "description";
    newTr.cells[3].id = idp + "url";
    newTr.cells[4].id = idp + "notes";	
}
function addRecipePics(cfObjects) {
	$.each(cfObjects, function(i, cfObject) {
		console.log(cfObject);
	});
}


$(function() {
	$("#uploader").plupload({
		// General settings
		runtimes : 'gears,flash,silverlight,browserplus,html5',
		url : 'recipes/uploadFile',
		max_file_size : '2mb',
		chunk_size : '1mb',
		unique_names : true,
		
	    multipart_params: {'target': recipeId},

		// Specify what files to browse for
		filters : [
			{title : "Image files", extensions : "jpg,gif,png"}
		],
		
	    preinit : {
	        UploadFile: function(up, file) {
	          target = recipeId + "/pics";
	          up.settings.multipart_params = {target: target, basename: file.name};
	        }
	      },


		// Flash settings
		flash_swf_url : 'resources/js/plupload/plupload.flash.swf',

		// Silverlight settings
		silverlight_xap_url : 'resources/js/plupload/plupload.silverlight.xap'
	});

	// Client side form validation
	$('form').submit(function(e) {
        var uploader = $('#uploader').plupload('getUploader');

        // Files in queue upload them first
        if (uploader.files.length > 0) {
            // When all files are uploaded submit form
            uploader.bind('StateChanged', function() {
                if (uploader.files.length === (uploader.total.uploaded + uploader.total.failed)) {
                    $('form')[0].submit();
                }
            });
                
            uploader.start();
        } else
            alert('You must at least upload one file.');

        return false;
    });
});
</script>