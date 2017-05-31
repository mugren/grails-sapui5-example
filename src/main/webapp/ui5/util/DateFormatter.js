sap.ui.define([
    "sap/ui/base/Object",
    "sap/ui/core/format/DateFormat"
], function (Object, DateFormat) {
    "use strict";
    return Object.extend("com.groggystuff.sap.ui.restApp.util.DateFormatter", {

        constructor: function (oProperties) {
            this.timeFormat = DateFormat.getTimeInstance({
                style: "short"
            }, oProperties.locale);
            this.weekdayFormat = DateFormat.getDateInstance({
                pattern: "EEEE"
            }, oProperties.locale);
            this.dateFormat = DateFormat.getDateInstance({
                style: "medium"
            }, oProperties.locale);
            this.now = oProperties.now;
        },

        format: function (oDate) {
            if (!oDate) {
                return "";
            }
            if (typeof oDate === 'string') {
                oDate = new Date(oDate);
            }
            var iElapsedDays = this._getElapsedDays(oDate);
            // negative elapsed days is the future
            if (iElapsedDays === 0) {
                return this.timeFormat.format(oDate);
            } else if (iElapsedDays === 1) {
                return "Yesterday";
            } else if (iElapsedDays < 7 && iElapsedDays > 0) {
                return "Last " + this.weekdayFormat.format(oDate);
            } else if (iElapsedDays < 0 && iElapsedDays > -7) {
                return this.weekdayFormat.format(oDate);
            } else {
                return this.dateFormat.format(oDate);
            }
        },
        _getElapsedDays: function (oDate) {
            var iElapsedMilliseconds = this.now() - oDate.getTime();
            var fElapsedDays = iElapsedMilliseconds / 1000 / 60 / 60 / 24;
            return Math.floor(fElapsedDays);
        }
    });
});