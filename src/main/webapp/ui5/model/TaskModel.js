sap.ui.define([
    "sap/ui/model/json/JSONModel"
], function (JSONModel) {
    "use strict";
    return JSONModel.extend("com.groggystuff.sap.ui.restApp.model.TaskModel", {
        //declare our new method including two new parameters fnSuccess and fnError, our callback functions
        loadDataNew: function (sURL, oParameters, fnSuccess, fnError, bAsync, sType, bMerge, bCache) {

            var that = this;

            if (bAsync !== false) {
                bAsync = true;
            }
            if (!sType) {
                sType = "GET";
            }
            if (bCache === undefined) {
                bCache = this.bCache;
            }

            // Fire event requestSent to attached listeners.
            this.fireRequestSent({
                url: sURL,
                type: sType,
                async: bAsync,
                info: "cache=" + bCache + ";bMerge=" + bMerge
            });

            jQuery.ajax({
                url: sURL,
                async: bAsync,
                dataType: 'json',
                cache: bCache,
                data: oParameters,
                type: sType,
                success: function (oData) {
                    if (!oData) {
                        jQuery.sap.log.fatal("The following problem occurred: No data was retrieved by service: " + sURL);
                    }
                    that.oDataOrig = {};
                    that.oDataOrig = jQuery.extend(true, {}, that.oDataOrig, oData); // Holds a copy of the original data
                    that.setData(oData, bMerge);
                    that.fireRequestCompleted({
                        url: sURL,
                        type: sType,
                        async: bAsync,
                        info: "cache=false;bMerge=" + bMerge
                    });
                    // call the callback success function if informed
                    if (typeof fnSuccess === 'function') {
                        fnSuccess(oData);
                    }

                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    jQuery.sap.log.fatal("The following problem occurred: " + textStatus, XMLHttpRequest.responseText + ","
                        + XMLHttpRequest.status + "," + XMLHttpRequest.statusText);
                    that.fireRequestCompleted({
                        url: sURL,
                        type: sType,
                        async: bAsync,
                        info: "cache=false;bMerge=" + bMerge
                    });
                    that.fireRequestFailed({
                        message: textStatus,
                        statusCode: XMLHttpRequest.status,
                        statusText: XMLHttpRequest.statusText,
                        responseText: XMLHttpRequest.responseText
                    });
                    // call the callback error function if informed
                    if (typeof fnError === 'function') {
                        fnError({
                            message: textStatus,
                            statusCode: XMLHttpRequest.status,
                            statusText: XMLHttpRequest.statusText,
                            responseText: XMLHttpRequest.responseText
                        });
                    }
                }
            });

        },
        getOrigData: function () {
            return this.oDataOrig;
        },
        discardChanges: function () {
            this.setData(this.oDataOrig);
        },
        commitChanges: function () {
            this.oDataOrig = this.getData();
        },
        postData: function (sURL, oParameters, fnSuccess, fnError, bAsync, sType, bMerge, bCache) {

            var that = this;

            if (bAsync !== false) {
                bAsync = true;
            }
            if (!sType) {
                sType = "POST";
            }
            if (bCache === undefined) {
                bCache = this.bCache;
            }

            // Fire event requestSent to attached listeners.
            this.fireRequestSent({
                url: sURL,
                type: sType,
                async: bAsync,
                info: "cache=" + bCache + ";bMerge=" + bMerge
            });

            jQuery.ajax({
                url: sURL,
                async: bAsync,
                dataType: 'json',
                cache: bCache,
                data: oParameters,
                type: sType,
                success: function (oData) {
                    if (!oData) {
                        jQuery.sap.log.fatal("The following problem occurred: No data was retrieved by service: " + sURL);
                    }
                    that.oDataOrig = {};
                    that.oDataOrig = jQuery.extend(true, {}, that.oDataOrig, oData); // Holds a copy of the original data
                    that.setData(oData, bMerge);
                    that.fireRequestCompleted({
                        url: sURL,
                        type: sType,
                        async: bAsync,
                        info: "cache=false;bMerge=" + bMerge
                    });
                    // call the callback success function if informed
                    if (typeof fnSuccess === 'function') {
                        fnSuccess(oData);
                    }

                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    jQuery.sap.log.fatal("The following problem occurred: " + textStatus, XMLHttpRequest.responseText + ","
                        + XMLHttpRequest.status + "," + XMLHttpRequest.statusText);
                    that.fireRequestCompleted({
                        url: sURL,
                        type: sType,
                        async: bAsync,
                        info: "cache=false;bMerge=" + bMerge
                    });
                    that.fireRequestFailed({
                        message: textStatus,
                        statusCode: XMLHttpRequest.status,
                        statusText: XMLHttpRequest.statusText,
                        responseText: XMLHttpRequest.responseText
                    });
                    // call the callback error function if informed
                    if (typeof fnError === 'function') {
                        fnError({
                            message: textStatus,
                            statusCode: XMLHttpRequest.status,
                            statusText: XMLHttpRequest.statusText,
                            responseText: XMLHttpRequest.responseText
                        });
                    }
                }
            });
        }
    });
});