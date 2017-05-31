sap.ui.define([
    "com/groggystuff/sap/ui/restApp/controller/BaseController"
], function (BaseController) {
    "use strict";
    return BaseController.extend("com.groggystuff.sap.ui.restApp.controller.Home", {
        onDisplayNotFound: function (oEvent) {
            //display the "notFound" target without changing the hash
            this.getRouter().getTargets().display("notFound", {
                fromTarget: "home"
            });
        },
        onNavToTasks: function (oEvent) {
            this.getRouter().navTo("taskList");
        }
    });

});