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

	$('#recipePicDeleteForm').dialog({
	      autoOpen: false,
	      width: 400,
	      height: 350,
	      modal: true,
	      resizable: false,
	      buttons: {
	          "Close": function() {
	              $(this).dialog("close");
	          },
	          "Delete Recipe Pic": function() {
	              processDeleteRecipePicSubmit();
	          }
	      }
	});
	
	recipeFormInit();
	
	recipeId = getURLParameter("recipeId");
	$.get('recipes/' + recipeId, function(data) {addRecipeDetailsRow(data);});  
	$.get('recipes/recipePics/' + recipeId, function(data) {addRecipePics(data);});  
	
});

function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
}

function addRecipeDetailsRow(recipe) {
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
	$.each(cfObjects, function(i, cfObject) {addRecipePic(cfObject)});
}

function addRecipePic(cfObject) {
    if ($("#recipePicsTable img").length %5 == 0)
		$("#recipePicsTable").append("<tr></tr>");
    
    var basename = cfObject.name.split("/");
	$("#recipePicsTable tr:last").append('<td id=\"' + cfObject.name + '\"><a target=\"_blank\" href=\"' + cfObject.cdnurl + '\"><img title=\"' + cfObject.name + '\" src="' + cfObject.cdnurl + '"' + ' height=300 width=300/></a><div><a target=\"_blank\" href=\"' + cfObject.cdnurl + '\">' + basename[basename.length-1]+ '</a></div><div><a href=\"javascript:showDeleteRecipePicForm(\'' + cfObject.name + '\', \'' + cfObject.cdnurl + '\');\">Delete</a></div></td>');	
}



function showDeleteRecipePicForm(objectName, cdnurl) {
	$("#recipedPicDeleteId").val(objectName); 
    var basename = objectName.split("/");

	$("#recipePicDeleteFormImage").attr("src",cdnurl);
	
	$("#recipePicDeleteFormErrors").html("<div class=\"error\">You are about to delete this Recipe Pic <b>" + basename[basename.length-1] + "<p style=\"margin-top:10px;font-weight:bold;\"><h2>Are you sure?</h2></p></div>");
	
	$("#recipePicDeleteFormErrors").show();
	$("#recipePicDeleteForm").dialog("option", "title", "Delete Recipe Pic");
	$('#recipePicDeleteForm').dialog('open');

}

function processDeleteRecipePicSubmit() {
	var cfObject = new Object;
	cfObject.name = $("#recipedPicDeleteId").val();
		
    $.ajax({
        type: 'DELETE',
        url: 'recipes/recipePics/',
        contentType: "application/json",
        dataType: "json",
        data:JSON.stringify(cfObject),
        success: function(cfObject) {processDeleteRecipePicResponse(cfObject);},
        error : function(request, status, error) {$("#recipePicDeleteFormErrors").html("<div class=\"error\">Please enter the Recipe <b>Title</b>.</div>");}
	});			
}

function processDeleteRecipePicResponse(cfObject) {
	$('#recipePicDeleteForm').dialog('close');
	var td = document.getElementById(cfObject.name);
	td.parentNode.removeChild(td);
}


$(function() {
	$("#uploader").plupload({
		// General settings
		runtimes : 'gears,flash,silverlight,browserplus,html5',
		url : 'recipes/uploadFile',
		max_file_size : '10mb',
		//chunk_size : '1mb',
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

    var uploader = $('#uploader').plupload('getUploader');
    
    uploader.bind('FileUploaded', function(up, file, response) {
    	addRecipePic($.parseJSON(response.response));
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