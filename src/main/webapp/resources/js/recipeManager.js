 // globals
 var recipesTable;
 var eventSource;
 
 $(document).ready(function () {
	  // only call from the recipe admin page, not from recipe details page
	  if ($("#recipesTable").length == 0)
		  return;

	 
	  $("#addRecipeButton").button();
	  $("#addRecipeButton").click(function() { showRecipeForm(0); return false; });
	  $("#eventLogButton").button();
	  $("#eventLogButton").click(function() { location.href = 'eventLogManager'; });
	  $("#logoutButton").button();
	  $("#logoutButton").click(function() { location.href = 'j_spring_security_logout'; });
	  $("#usersButton").button();
	  $("#usersButton").click(function() { location.href = 'userManager'; });
	  $("#myAccountButton").button();
	  $("#myAccountButton").click(function() { location.href = 'myAccountManager'; });
	  recipeFormInit();
	
	  recipesTable = $('#recipesTable').dataTable({
	    "bJQueryUI": true,
	    "iDisplayLength": 25,
	    "sPaginationType": "full_numbers",
	    "sDom": '<"fg-toolbar ui-toolbar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix"lfr>t<"fg-toolbar ui-toolbar ui-widget-header ui-corner-bl ui-corner-br ui-helper-clearfix"ip>'
	  });

	$.get('recipes', function(data) {processRecipeList(data);});  
	
});
 
function startEventListeners() {
	// start listening for recipe events
	eventSource = new EventSource('sse');
	
	eventSource.addEventListener('Recipe modified', function(e) {
		//console.log("received Recipe Modified event: " + e.data);
		updateRecipeRow($.parseJSON(e.data));  
	}, false);
	
	eventSource.addEventListener('Recipe created', function(e) {
		//console.log("received Recipe created event: " + e.data);
		addRecipeRow($.parseJSON(e.data));  
	}, false);

	eventSource.addEventListener('Recipe deleted', function(e) {
		//console.log("received Recipe deleted event: " + e.data);
		deleteRecipeRow($.parseJSON(e.data));  
	}, false);	
}

function processRecipeList(data) {	
	$.each(data, function(i, recipe) {
		addRecipeRow(recipe);
	});

	startEventListeners();
}

function addRecipeRow(recipe) {
	if ($("#" + recipe.recipeId + "_contributer").length > 0)
		return;
    var clink = recipe.contributerUserName != "" ? '<div style="margin-top:3px;"><a href="javascript:showUserInfoForm(' + "'" + recipe.contributerUserName +  "','" + recipe.title.replace(/'/g, '')  + "'" + ');">' + recipe.contributerUserName + '</a></div>' : "";
	var tlink = "<a href=\"recipeDetails?recipeId=" + recipe.recipeId + "\">" + recipe.title + "<a>";
    var mlink = '<div style="margin-top:3px;"><a href="javascript:showRecipeForm(' + "'" + recipe.recipeId +  "'" + ');">Modify</a></div>';
    var dlink = '<div style="margin-top:3px;"><a href="javascript:showRecipeDeleteForm(' + "'" + recipe.recipeId + "'" + ');">Delete</a></div>';
    var newRow = recipesTable.dataTable().fnAddData([clink,
                                                     tlink,
                                                     recipe.description,
                                                     '<a target="_blank" href="' + recipe.url + '">' + recipe.url + '</a>',
                                                     recipe.notes,
                                                     mlink + dlink]); 
    
    // update the dom for this new tr so we have the ids set correctly
    var newTr = recipesTable.fnSettings().aoData[ newRow[0] ].nTr;
    var idp = recipe.recipeId + "_";
    newTr.cells[0].id = idp + "contributer";
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
	  $("#recipeDeleteForm").dialog("option", "title", "Delete Recipe");
	  $('#recipeDeleteForm').dialog('open');
}

function processGetRecipeResults(recipe){
	 $("#recipeId").val(recipe.recipeId);
	 if (recipe.contributer)
		 $("#recipeContributerId").val(recipe.contributer.id);
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

function updateRecipeRow (recipe) {
    var clink = recipe.contributerUserName != "" ? '<div style="margin-top:3px;"><a href="javascript:showUserInfoForm(' + "'" + recipe.contributerUserName +  "','" + recipe.title.replace(/'/g, '') + "'"  + ');">' + recipe.contributerUserName + '</a></div>' : "";
	var idp = recipe.recipeId + "_";
	$("#" + idp + "contributer").html(clink);
	$("#" + idp + "title").html(recipe.title);
	$("#" + idp + "url").html('<a target="_blank" href="' + recipe.url + '">' + recipe.url + '</a>');
	$("#" + idp + "description").html(recipe.description);
	$("#" + idp + "notes").html(recipe.notes);	
}
function processSubmitResults(recipe, type) {	
	$('#recipeForm').dialog('close');

	if (type == 'PUT') 
		updateRecipeRow(recipe);
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
        success: function(data) {deleteRecipeRow(data);},
        error : function(request, status, error) {console.log("Failed: " + error);}
	});			
}

function deleteRecipeRow(recipe) {
	$('#recipeDeleteForm').dialog('close');

	// remove this recipe from the table
	var searchId = recipe.recipeId + "_title";
	var allTrs = recipesTable.fnGetNodes();
	for (var i=0; i<allTrs.length ; i++ ) {
	  if (allTrs[i].cells[1].id == searchId) {
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