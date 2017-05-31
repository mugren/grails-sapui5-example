<!doctype html>
<html>
<head>
    <title>REST-APP UI5</title>
    <script
            id="sap-ui-bootstrap"
            src="static/ui5/resources/sap-ui-core.js"
            data-sap-ui-theme="sap_bluecrystal"
            data-sap-ui-libs="sap.m"
            data-sap-ui-compatVersion="edge"
            data-sap-ui-preload="async"
            data-sap-ui-resourceroots='{ "com.groggystuff.sap.ui.restApp": "static/ui5" }'>
    </script>
    <script>

        sap.ui.getCore().attachInit(function () {
            sap.ui.require([
                "sap/m/Shell",
                "sap/ui/core/ComponentContainer"
            ], function (Shell, ComponentContainer) {
                new Shell({
                    app: new ComponentContainer({
                        name: "com.groggystuff.sap.ui.restApp",
                        height: "100%"
                    })
                }).placeAt("content");
            });
        });
    </script>

    <script>
        var serverContextPath = '${request.getContextPath()}';
    </script>
</head>

<body class="sapUiBody" id="content">

</body>
</html>
