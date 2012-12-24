<%@ include file="/resources/frameworks/topFramework.html" %>
<link rel="stylesheet" href="resources/css/rmLogin.css" />


<div id="container">
  <div id="topnav" class="topnav"> Have an account? <a href="login" class="signin"><span>Sign in</span></a></div>
  <div style="height:200px;">
  <fieldset id="signin_menu">
    <form method="post"id="signin" action="j_spring_security_check">
     <p class="error" id="loginError"></p>
      <p>
      <label for="j_username">Username</label>
      <input required id="j_username" name="j_username" value="" title="Enter your Recipe Manager username" tabindex="4" type="text">
      </p>
      <p>
        <label for="j_password">Password</label>
        <input required id="j_password" name="j_password" value="" title="Enter your Recipe Manager password" tabindex="5" type="password">
      </p>
      <p class="remember">
        <input id="signin_submit" value="Sign in" tabindex="6" type="submit">
        <input id="remember" name="remember_me" value="1" tabindex="7" type="checkbox">
        <label for="remember">Remember me</label>
      </p>
      <p class="forgot"> <a href="javascript:forgotPassword();" id="resend_password_link">Forgot your password?</a></p>
      <p class="forgot-username"> <a href="javascript:forgotUsername();" id="forgot_username_link" title="If you remember your password, try logging in with your email" href="#">Forgot your username?</a></p>
    </form>
  </fieldset>
  </div>
</div>

<div style="text-align:center">
	<h3 style="font-size:2.0em;">Welcome to HerbCooking.net</h3>
    <div><img title="M&C LLC" style="height:45px;" src="resources/images/logo.jpg"/></div>
    <div><b style="vertical-align:bottom; margin-left: 3px;">&copy; M&C LLC Copyright 2011 - 2012</b></div>	
</div>

	</div>
</div>


</body>
</html>

<script type="text/javascript">
$(document).ready(function() {
    $(".signin").click(function(e) {
        e.preventDefault();
        $("fieldset#signin_menu").toggle();
        $(".signin").toggleClass("menu-open");
    });

    $("fieldset#signin_menu").mouseup(function() {
        return false
    });
    $(document).mouseup(function(e) {
        if($(e.target).parent("a.signin").length==0) {
            $(".signin").removeClass("menu-open");
            $("fieldset#signin_menu").hide();
        }
    });            

	var loginError =  getURLParameter("login_error");
	if(loginError) {
		$("#loginError").html("Login Failed, Reason: " + loginError).show();
        $("fieldset#signin_menu").toggle();
        $(".signin").toggleClass("menu-open");
	}
	else
		$("#loginError").html("").hide();
	

});

function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
}

function forgotPassword() {
	alert("Sucks to be you!");
}

function forgotUsername() {
	alert("That really sucks!")
}
</script>

