sap.ui.define([
    "com/groggystuff/sap/ui/restApp/controller/BaseController",
    "com/groggystuff/sap/ui/restApp/model/TaskModel",
    "sap/ui/model/json/JSONModel",
    "com/groggystuff/sap/ui/restApp/util/formatter",
    "sap/m/MessageToast",
    "sap/m/MessageBox",
    "com/groggystuff/sap/ui/restApp/model/CommentModel",
    "com/groggystuff/sap/ui/restApp/model/CodexModel"
], function (BaseController, TaskModel, JSONModel, formatter, MessageToast, MessageBox, CommentModel, CodexModel) {
    "use strict";
    var navBackTo = 'taskList';
    return BaseController.extend("com.groggystuff.sap.ui.restApp.controller.task.TaskShow", {
        formatter: formatter,
        /* =========================================================== */
        /* lifecycle methods                                           */
        /* =========================================================== */
        onInit: function () {
            var oRouter = this.getRouter();
            this._changeStatusDialog = null;
            this._taskModel = null;
            this._taskStatusModel = null;
            this._commentsModel = null;
            oRouter.getRoute("task").attachMatched(this._onRouteMatched, this);
        },
        /* =========================================================== */
        /* event handlers                                              */
        /* =========================================================== */
        onNavBack: function (oEvent) {
            arguments[1] = navBackTo;
            BaseController.prototype.onNavBack.apply(this, arguments);
        },
        // DIALOG
        onTaskStatusChange: function (oEvent) {
            this._taskStatusModel = new CodexModel();
            this._taskStatusModel.attachRequestCompleted(this._buildTaskStatusChangeDialog, this);
            this._taskStatusModel.getCodex('TaskStatus');
        },
        onPostComment: function (oEvent) {
            var sValue = oEvent.getParameter("value");
            var taskId = this._taskModel.getProperty('/id');
            var that = this;
            var _postCommentCompleted = function (data) {
                MessageToast.show(that.getResourceBundle().getText('task.postComment.success'));
                that._commentsModel.loadComments('Task', taskId);
            };
            var _postCommentFailure = function () {
                MessageBox.error(that.getResourceBundle().getText('task.postComment.failure'));
            };
            this._commentsModel.postComment('Task', taskId, sValue, _postCommentCompleted, _postCommentFailure);
        },
        /* =========================================================== */
        /* internal methods                                            */
        /* =========================================================== */
        _onRouteMatched: function (oEvent) {
            var oArgs;
            oArgs = oEvent.getParameter("arguments");

            this._taskModel = new TaskModel();
            this.setViewModel(this._taskModel, 'taskShow');
            this._taskModel.loadDataNew('/tasks/get/' + oArgs.taskId);
            //
            this._commentsModel = new CommentModel();
            this.setViewModel(this._commentsModel, 'comments');
            this._commentsModel.loadComments('Task', oArgs.taskId);
        },
        _buildTaskStatusChangeDialog: function (oEvent) {
            var oRouter = this.getRouter();
            //The Template to use in the Dialog
            var itemTemplate = new sap.m.StandardListItem({
                title: "{value}",
                description: "{id}"
            });
            // remove the existing value item
            var taskStatusId = this._taskModel.getProperty('/taskStatusId');
            var items = this._taskStatusModel.getProperty('/items');
            var index = null;
            for (var i = 0; i < items.length; i++) {
                if (items[i].id === taskStatusId) {
                    index = i;
                    break;
                }
            }
            if (index !== null) {
                items.splice(index, 1);
            }
            // create the dialog
            this._changeStatusDialog = new sap.m.SelectDialog("csd", {
                title: "Change Task Status",
                confirm: function (oControlEvent) {
                    var selectedItem = oControlEvent.getParameter("selectedItem");
                    var taskStatusId = selectedItem.getDescription();
                    var data = [];
                    data.push({name: 'taskStatus', value: taskStatusId});

                    var tempTaskModel = new TaskModel();
                    var taskId = this._taskModel.getProperty('/id');
                    var updateUrl = '/tasks/update/' + taskId;

                    var that = this;
                    var _taskStatusUpdateCompleted = function (data) {
                        MessageToast.show(that.getResourceBundle().getText('task.taskStatus.update.success'));
                        that._taskModel.loadDataNew('/tasks/get/' + taskId);
                    };
                    var _taskStatusUpdateFailed = function () {
                        MessageBox.error(that.getResourceBundle().getText('task.taskStatus.update.failure'));
                    };
                    // update the task with the new task status !
                    tempTaskModel.postData(updateUrl, data, _taskStatusUpdateCompleted, _taskStatusUpdateFailed);

                    this._changeStatusDialog.destroy();
                    oRouter.navTo("task",
                        {taskId: taskId}
                        , true /*without history*/);
                }.bind(this),
                cancel: function (oEvent) {
                    var taskId = this._taskModel.getProperty('/id');
                    this._changeStatusDialog.destroy();
                    oRouter.navTo("task",
                        {taskId: taskId}
                        , true /*without history*/);
                }.bind(this)
            });
            //Now set the model for Dialog and bind the Aggregration.
            this._changeStatusDialog.setModel(this._taskStatusModel);
            this._changeStatusDialog.bindAggregation("items", "/items", itemTemplate);
            this._changeStatusDialog.open('');
        }
    });
});