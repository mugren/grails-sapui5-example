<html>
<head>
    <title><g:message code="springSecurity.login.title"/></title>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic&subset=latin,cyrillic'
          rel='stylesheet' type='text/css'>

    <style type='text/css' media='screen'>
    body {
        font-family: "Open Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-weight: 300;
        background-color: #282d37;
        width: 100%;
        height: 95%;
        margin: auto;
    }

    #login {
        margin: 3rem auto;
        text-align: center;
        /*background-color: #eee;*/
        width: 24em;
        /*height: 100%;*/
        padding: 0 0 0 0;
        display: table;
        box-sizing: border-box;
        position: relative;
    }

    #login .inner {
        margin: 0 auto;
        text-align: center;
        border-radius: 0.2em;
        /*background-color: rgba(15, 15, 15, 0.75);*/
        padding-bottom: 1em;
        font-size: 0.8em;

        -moz-box-shadow: 0 0 15px 1px #111;
        -webkit-box-shadow: 0 0 15px 1px #111;
        -khtml-box-shadow: 0 0 15px 1px #111;
        box-shadow: 0 0 15px 1px #111;

        /* Permalink - use to edit and share this gradient: http://colorzilla.com/gradient-editor/#2d323c+0,424959+100 */
        background: #2d323c; /* Old browsers */
        background: -moz-linear-gradient(45deg, #2d323c 0%, #424959 100%); /* FF3.6-15 */
        background: -webkit-linear-gradient(45deg, #2d323c 0%, #424959 100%); /* Chrome10-25,Safari5.1-6 */
        background: linear-gradient(45deg, #2d323c 0%, #424959 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#2d323c', endColorstr='#424959', GradientType=1); /* IE6-9 fallback on horizontal gradient */

    }

    #login .inner .fheader {
        padding: 2em;
        margin: 0 0 0em 0;
        color: #fff;
        font-size: 2rem;
        font-weight: 300;
        text-transform: uppercase;
    }

    #login .inner .login-form .form-element {
        clear: left;
        margin: 0;
        padding: 0.5em 0;
        margin-bottom: 0.75em;
        /*height: 1%;*/
    }

    /*#login .inner .login-form input[type='text'] {*/
    /*width: 120px;*/
    /*}*/

    #login .inner .login-form label {
        padding: 0.3em 0;
        color: #fff;
        /*text-transform: uppercase;*/
        font-weight: 300;
    }

    #login #remember-me-holder-element {
        /*padding-left: 10.5em;*/
    }

    #login-submit-element {
        text-align: center;
    }

    #login #login-submit {
        font-family: "Open Sans", sans-serif;
        font-weight: 300;
        background-color: #009bc9;
        border: 1px solid #009bc9;
        border-radius: 2px;
        color: #fff;
        padding: 0.5em 1em;
        text-transform: uppercase;
        font-size: 1em;
        /*width: 15em;*/
        cursor: pointer;
    }

    #login #login-submit:active {
        top: 1px;
        position: relative;
    }

    #login #remember-me-holder-element label {
        /*float: none;*/
        /*margin-left: 0;*/
        /*text-align: left;*/
        /*width: 200px;*/
    }

    #login .inner .login_message {
        padding: 1em;
        background-color: #c33;
        color: #fff;
        border-top: 1px dashed #fff;
        border-bottom: 1px dashed #fff;
        margin-bottom: 1em;
    }

    #login .inner .input-text {
        border: 1px solid transparent;
        border-radius: 2px;
        background-color: #282d37;
        padding: 1em;
        color: #ffffff;
        font-size: 1em;
        width: 60%;
    }

    #login .inner .chk {
        /*height: 12px;*/
    }

    #additional-links > * {
        padding: 0.3em 0;
    }

    #additional-links a {
        color: #474e60;
        /*color: #FFFFFF;*/
        text-decoration: none;
        /*font-size: 0.8em;*/
    }

    #additional-links a:hover {
        color: #009bc9;
    }

    .form-element > * {
        /*display: block;*/
        margin: auto;
    }

    /*.settings-auth {*/
    /*position: absolute;*/
    /*top: 1em;*/
    /*right: 1em;*/
    /*}*/

    /*.settings-auth select{*/
    /*border: 1px solid transparent;*/
    /*line-height: 1.2em;*/
    /*margin-bottom: 1em;*/
    /*padding: 0;*/
    /*}*/

    .credentials-icon {
        background-color: #474e60;
        color: #282d37;
        padding: 1em;
        border-radius: 2px;
        /*font-size: 0.8em;*/
        margin-right: -3px;
    }

    </style>
</head>

<body>
<div id='login'>
    <div class='inner'>
        <div class='fheader'>
            REST-APP
        </div>

        <g:if test='${flash.message}'>
            <div class='login_message'>${flash.message}</div>
        </g:if>

        <form action='${postUrl}' method='POST' id='loginForm' class='login-form' autocomplete='off'>
            <div id="username-element" class="form-element">
                <span class="credentials-icon"><i class="fa fa-user fa-fw"></i></span>
                <input type='text'
                       placeholder="${message(code: 'springSecurity.login.username.label', default: 'Username')}"
                       class="input-text" name='username' id='username'/>
            </div>

            <div id="password-element" class="form-element">
                <span class="credentials-icon"><i class="fa fa-unlock-alt fa-fw"></i></span>
                <input type='password'
                       placeholder="${message(code: 'springSecurity.login.password.label', default: 'Password')}"
                       class="input-text" name='password' id='password'/>
            </div>

            <div id="remember-me-holder-element" class="form-element">
                <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
                       <g:if test='${hasCookie}'>checked='checked'</g:if>/>
                <label for='remember_me'><g:message code="springSecurity.login.remember.me.label"/></label>
            </div>


            <div id="login-submit-element" class="form-element">
                <input type='submit' id="login-submit" value='${message(code: "springSecurity.login.button")}'/>
            </div>
        </form>

    </div>
</div>
<script type='text/javascript'>
    <!--
    (function () {
        document.forms['loginForm'].elements['username'].focus();
    })();
    // -->
</script>
</body>
</html>
