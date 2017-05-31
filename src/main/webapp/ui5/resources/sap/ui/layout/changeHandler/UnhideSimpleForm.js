/*!
 * UI development toolkit for HTML5 (OpenUI5)
 * (c) Copyright 2009-2016 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(['jquery.sap.global',"sap/ui/fl/changeHandler/JsControlTreeModifier"],function(q,J){"use strict";var U={};U.applyChange=function(c,C,p){var m=p.modifier;var v=p.view;var a=p.appComponent;var o=c.getDefinition();var b=m.bySelector(o.content.elementSelector||o.content.sUnhideId,a,v);var d=m.getAggregation(C,"content");var s=-1;if(o.changeType==="unhideSimpleFormField"){d.some(function(f,i){if(f===b){s=i;m.setVisible(f,true);}if(s>=0&&i>s){if((m.getControlType(f)==="sap.m.Label")||(m.getControlType(f)==="sap.ui.core.Title")){return true;}else{m.setVisible(f,true);}}});}return true;};U.completeChangeContent=function(c,s,p){var C=c.getDefinition();if(s.sUnhideId){var u=sap.ui.getCore().byId(s.sUnhideId);C.content.elementSelector=J.getSelector(u,p.appComponent);}else{throw new Error("oSpecificChangeInfo.sUnhideId attribute required");}};return U;},true);
