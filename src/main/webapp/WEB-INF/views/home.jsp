<%@ include file="/resources/frameworks/topFramework.html" %>
<div id="content" style="width:90%;">
    <h1>Welcome to My Recipe Manager</h1>
	<div style="float:left;margin:10px;"><button id="logoutButton">Logout</button></div>  
    <div style="float:right;margin:10px;"><button id="addRecipeButton">Add Recipe</button></div>  

	<table border="0" class="display" id="recipesTable" >
	  <caption class="fg-toolbar ui-widget-header"><h2 style="margin:5px;">Recipes</h2></caption>
	  <thead>
	    <tr>
	      <th>ID</th>
	      <th>Title</th>
	      <th>Description</th>
	      <th>URL</th>
	      <th>Notes</th>
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

 <script>
 // globals
 var recipesTable;
 
$(document).ready(function () {
  $("#addRecipeButton").button();
  $("#addRecipeButton").click(function() { showRecipeForm(0); return false; });
  $("#logoutButton").button();
  $("#logoutButton").click(function() { location.href = 'j_spring_security_logout'; });
  recipeFormInit();

	recipesTable = $('#recipesTable').dataTable({
	    "bJQueryUI": true,
	    "iDisplayLength": 25,
	    "sPaginationType": "full_numbers",
	    "sDom": '<"fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix"lfr>t<"fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix"ip>'
	  });

	$.get('recipes', function(data) {processRecipeList(data);});  
});

function processRecipeList(data) {	
	$.each(data, function(i, recipe) {
		addRecipeRow(recipe);
	});

	//console.log(data);
}

function addRecipeRow(recipe) {
    var mlink = '<div style="margin-top:3px;"><a href="javascript:showRecipeForm(' + "'" + recipe.recipeId +  "'" + ');">Modify</a></div>';
    var dlink = '<div style="margin-top:3px;"><a href="javascript:showRecipeDeleteForm(' + "'" + recipe.recipeId + "'" + ');">Delete</a></div>';
    var newRow = recipesTable.dataTable().fnAddData([recipe.recipeId,
                                                     recipe.title,
                                                     recipe.description,
                                                     '<a target="_blank" href="' + recipe.url + '">' + recipe.url + '</a>',
                                                     recipe.notes,
                                                     mlink + dlink]); 

    // update the dom for this new tr so we have the ids set correctly
    var newTr = recipesTable.fnSettings().aoData[ newRow[0] ].nTr;
    var idp = recipe.recipeId + "_";
    newTr.cells[0].id = idp + "recipeId";
    newTr.cells[1].id = idp + "title";
    newTr.cells[2].id = idp + "description";
    newTr.cells[3].id = idp + "url";
    newTr.cells[4].id = idp + "notes";		
}

function recipeFormInit() {
	  // auto trim all whitespace on text input fields
	  $('input[type=text]').blur(function(){
	      $(this).val($.trim($(this).val()));
	  });

	  $('#recipeForm').dialog({
	      autoOpen: false,
	      position:['middle',20],
	      width: 725,
	      height: 425,
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
	  
	  $('#recipeDeleteForm').dialog({
	      autoOpen: false,
	      width: 400,
	      height: 300,
	      modal: true,
	      resizable: false,
	      buttons: {
	          "Cancel": function() {
	              $(this).dialog("close");
	          },
	          "Delete Recipe": function() {
	              processDeleteRecipeSubmit();
	          }
	      }
	  });
}

function showRecipeForm(recipeId) {
	 $("#recipeId").val(recipeId); 
	 $("#recipeFormErrors").html("");
	 if (recipeId == 0)  {  // show the create form
		 $("#recipeId").val("");
		 $("#recipeTitle").val("");
		 $("#recipeUrl").val("");
		 $("#recipeDescription").val("");
		 $("#recipeNotes").val("");
		 $("#recipeForm").dialog("option", "title", "Add a New Recipe");  
	 }
	 else {            // show the edit form
	   	 $("#recipeForm").dialog("option", "title", "Modify Recipe");
	 	 $.get('recipes/' + recipeId, function(data) {processGetRecipeResults(data);});  
	 }  
	 
	 $('#recipeForm').dialog('open');
}

function showRecipeDeleteForm(recipeId) {
	  $("#recipeId").val(recipeId); 

	  var idp = recipeId + "_";
	  var title = $("#" + idp + "title").html();
	  var description = $("#" + idp + "description").html();
	  var url = $("#" + idp + "url").html();
	  var html = "You are about to delete the Recipe &nbsp;<b>" + recipeId + "</b><table style=\"margin-top:15px;\" border=\"1\" width=\"350\"><tr><th>Title</th><th>Description</th><th>URL</th></tr><tr><td>" + title + "</td><td>" + description + "</td><td>" + url + "</td></tr></table><p style=\"margin-top:10px;font-weight:bold;\"><h2>Are you sure?</h2></p>"; 
	  
	  $("#recipeDeleteFormErrors").html("");
	  $("#recipeDeleteFormErrors").append("<div class=\"error\">" + html + "</div>");

	  $("#recipeDeleteFormErrors").show();
	  $("#recipeDeleteForm").dialog("option", "title", "Delete User");
	  $('#recipeDeleteForm').dialog('open');
}

function processGetRecipeResults(recipe){
	 $("#recipeId").val(recipe.recipeId);
	 $("#recipeTitle").val(recipe.title);
	 $("#recipeUrl").val(recipe.url);
	 $("#recipeDescription").val(recipe.description);
	 $("#recipeNotes").val(recipe.notes);
}

function processSubmit() {
	if (! validate()) {
		$("#recipeFormErrors").show();
	    return;
	}

	recipe = new Object();
	recipeId = $("#recipeId").val();
	recipe.title =  $("#recipeTitle").val();
	recipe.url =  $("#recipeUrl").val();
	recipe.description =  $("#recipeDescription").val();
	recipe.notes =  $("#recipeNotes").val();
	var type = recipeId == "" ? "POST" : "PUT";
	if (type == 'PUT')
		recipe.recipeId = recipeId;
	$.ajax({
        type: type,
        url: 'recipes',
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(recipe),
        success: function(data) {processSubmitResults(data, type);},
        error : function(request, status, error) {console.log(JSON.stringify(recipe)); alert("Failed: " + error);}
	});		
}

function processSubmitResults(recipe, type) {	
	$('#recipeForm').dialog('close');
	
	if (type == 'PUT') {
		var idp = recipe.recipeId + "_";
		$("#" + idp + "title").html(recipe.title);
		$("#" + idp + "url").html('<a target="_blank" href="' + recipe.url + '">' + recipe.url + '</a>');
		$("#" + idp + "description").html(recipe.description);
		$("#" + idp + "notes").html(recipe.notes);
	}
	else 
		addRecipeRow(recipe);
}

function processDeleteRecipeSubmit() {
	var recipeId = $("#recipeId").val();
	
	$.ajax({
        type: 'DELETE',
        url: 'recipes/' + recipeId,
        contentType: "application/json",
        dataType: "json",
        success: function(data) {processDeleteRecipeSubmitResults(data, recipeId);},
        error : function(request, status, error) {alert("Failed: " + error);}
	});			
}

function processDeleteRecipeSubmitResults(data, recipeId) {
	$('#recipeDeleteForm').dialog('close');

	// remove this recipe from the table
	var searchId = recipeId + "_recipeId";
	var allTrs = recipesTable.fnGetNodes();
	for (var i=0; i<allTrs.length ; i++ ) {
	  if (allTrs[i].cells[0].id == searchId) {
	    recipesTable.fnDeleteRow(allTrs[i]);
	    break;
	  }
	}
}

function validate() {
	 $("#recipeFormErrors").html("");
	 var returnVal = true;
	 if ($("#recipeTitle").val() == "") {
	   $("#recipeFormErrors").append("<div class=\"error\">Please enter the Recipe <b>Title</b>.</div>");
	   returnVal = false;
	 }	 
	 return returnVal;
}

 </script>
 


