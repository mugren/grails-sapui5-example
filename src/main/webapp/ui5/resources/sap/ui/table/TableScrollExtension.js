/*!
 * UI development toolkit for HTML5 (OpenUI5)
 * (c) Copyright 2009-2016 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(['jquery.sap.global','./TableExtension','./TableUtils'],function(q,T,a){"use strict";var E={};var b=T.extend("sap.ui.table.TableScrollExtension",{_init:function(t,s,S){this._type=s;this._delegate=E;t.addEventDelegate(this._delegate,t);return"ScrollExtension";},_attachEvents:function(){},_detachEvents:function(){},_debug:function(){this._ExtensionDelegate=E;},destroy:function(){var t=this.getTable();if(t){t.removeEventDelegate(this._delegate);}this._delegate=null;T.prototype.destroy.apply(this,arguments);}});return b;},true);
