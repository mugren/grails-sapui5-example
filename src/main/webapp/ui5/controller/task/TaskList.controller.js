sap.ui.define([
    "com/groggystuff/sap/ui/restApp/controller/BaseController",
    "com/groggystuff/sap/ui/restApp/model/TaskModel",
    "sap/ui/model/json/JSONModel",
    "com/groggystuff/sap/ui/restApp/util/formatter"
], function (BaseController, TaskModel, JSONModel, formatter) {
    "use strict";
    var taskListViewModel = new JSONModel({
        busy: true,
        delay: 0
    });
    return BaseController.extend("com.groggystuff.sap.ui.restApp.controller.task.TaskList", {
        formatter: formatter,
        /* =========================================================== */
        /* lifecycle methods                                           */
        /* =========================================================== */
        onInit: function () {
            // prepare the model
            var taskModel = new TaskModel();
            this.setViewModel(taskModel, 'taskList');
            taskModel.attachRequestSent(this._onRequestSent, this);
            taskModel.attachRequestCompleted(this._onRequestCompleted, this);
            taskModel.attachRequestFailed(this._onRequestFailed, this);
            taskModel.loadDataNew('/tasks/list');
        },
        /* =========================================================== */
        /* event handlers                                              */
        /* =========================================================== */
        onListItemPressed: function (oEvent) {
            var oItem, oCtx;
            oItem = oEvent.getSource();
            oCtx = oItem.getBindingContext('taskList');
            this.getRouter().navTo("task", {
                taskId: oCtx.getProperty("taskId")
            });
        },
        /* =========================================================== */
        /* internal methods                                            */
        /* =========================================================== */
        _onRequestCompleted: function (oEvent) {
            this.getViewModel('taskListView').setProperty('/busy', false);
        },
        _onRequestFailed: function (oEvent) {
            console.log("You Request failed");
        },
        _onRequestSent: function (oEvent) {
            this.setViewModel(taskListViewModel, 'taskListView');
        }

    });
});