sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/core/routing/History"
], function (Controller, History) {
    "use strict";
    var navBackTo = 'appHome';
    return Controller.extend("com.groggystuff.sap.ui.restApp.controller.BaseController", {
        getRouter: function () {
            return sap.ui.core.UIComponent.getRouterFor(this);
        },
        onNavBack: function (oEvent) {
            var oHistory, sPreviousHash, navTo = navBackTo;
            if (arguments[1] !== undefined && arguments[1] !== navTo) {
                navTo = arguments[1];
            }
            oHistory = History.getInstance();
            sPreviousHash = oHistory.getPreviousHash();
            if (sPreviousHash !== undefined) {
                window.history.go(-1);
            } else {
                this.getRouter().navTo(navTo, {}, true /*no history*/);
            }
        },
        /**
         * Convenience method for getting the view model by name.
         * @public
         * @param {string} [sName] the model name
         * @returns {sap.ui.model.Model} the model instance
         */
        getViewModel: function (sName) {
            return this.getView().getModel(sName);
        },

        /**
         * Convenience method for setting the view model.
         * @public
         * @param {sap.ui.model.Model} oModel the model instance
         * @param {string} sName the model name
         * @returns {sap.ui.mvc.View} the view instance
         */
        setViewModel: function (oModel, sName) {
            return this.getView().setModel(oModel, sName);
        },
        getComponentModel: function (sName) {
            return this.getOwnerComponent().getModel(sName);
        },
        setComponentModel: function (oModel, sName) {
            return this.getOwnerComponent().setModel(oModel, sName);
        },
        /**
         * Convenience method for accessing the event bus.
         * @public
         * @returns {sap.ui.core.EventBus} the event bus for this component
         */
        getEventBus: function () {
            return this.getOwnerComponent().getEventBus();
        },
        /**
         * Getter for the resource bundle.
         * @public
         * @returns {sap.ui.model.resource.ResourceModel} the resourceModel of the component
         */
        getResourceBundle: function () {
            return this.getComponentModel("i18n").getResourceBundle();
        },
        onLogout: function () {
            var baseCntrl = this;
            jQuery.ajax({
                method: "GET",
                url: "/logout",
                statusCode: {
                    401: function () {
                        baseCntrl._showLogin();
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    // silence
                },
                success: function (data, textStatus, jqXHR) {
                    baseCntrl._showLogin();
                }
            });
        },
        _showLogin: function () {
            setTimeout(function () {
                window.location.replace(serverContextPath + "/login/auth");
            }, 500);
        }
    });
});