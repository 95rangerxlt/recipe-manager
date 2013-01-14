<%@ include file="/resources/frameworks/topFramework.html" %>

Test of streaming server sent events

<div id="sseContent"></div>

<%@ include file="/resources/frameworks/bottomFramework.html" %>

<script type="text/javascript">

var eventSource = null;

$(document).ready(function () {
	eventSource = new EventSource(window.location.pathname + 'sse');
	
	eventSource.onmessage = function (e) {
		console.log("onmessage fired");
		console.log(e);
    	$("#sseContent").append(event.data + "<br>");

	}
	
	/***
	eventSource.onopen = function(e) {
		console.log("onopen fired");
		console.log(e);
	}
	
	eventSource.onerror = function(e) {
		console.log("onerror fired");
		console.log("ready state is " + e.target.readyState);		
	}
	***/
	
	eventSource.addEventListener('Recipe modified', function(e) {
		$("#sseContent").append("Recipe modified event received, data: " + e.data + "<br>");
	}, false);


	eventSource.addEventListener('User modified', function(e) {
		$("#sseContent").append("User modified event received, data: " + e.data + "<br>");
	}, false);

});
	



</script>
 


