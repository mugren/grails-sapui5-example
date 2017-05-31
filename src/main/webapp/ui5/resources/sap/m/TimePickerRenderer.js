/*!
 * UI development toolkit for HTML5 (OpenUI5)
 * (c) Copyright 2009-2016 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(['jquery.sap.global','sap/ui/core/Renderer','./InputBaseRenderer','sap/ui/core/ValueStateSupport'],function(q,R,I,V){"use strict";var T=R.extend(I);T.CSS_CLASS="sapMTimePicker";var a="sapMInputVH",b="sapMInputValHelpInner",c="sapMInputValHelp";T.addOuterClasses=function(r,C){r.addClass(T.CSS_CLASS);r.addClass("sapContrastPlus");if(C.getEnabled()&&C.getEditable()){r.addClass(a);}};T.writeInnerContent=function(r,C){var d,A,o,t,s;if(C.getEnabled()&&C.getEditable()){d=[b];A={};A.id=C.getId()+"-icon";A.tabindex="-1";A.title=null;r.write('<div class="'+c+'">');r.writeIcon("sap-icon://time-entry-request",d,A);r.write("</div>");}o=C._oResourceBundle;t=o.getText("TIMEPICKER_SCREENREADER_TAG");s=V.enrichTooltip(C,C.getTooltip_AsString());if(s){t=s+". "+t;}r.write('<span id="'+C.getId()+'-descr" style="visibility: hidden; display: none;">');r.writeEscaped(t);r.write('</span>');};T.writeInnerValue=function(r,C){r.writeAttributeEscaped("value",C._formatValue(C.getDateValue()));};T.writeAccessibilityState=function(r,C){var p={role:"combobox",multiline:false,autocomplete:"none",expanded:false,haspopup:true,owns:C.getId()+"-sliders",describedby:{value:C.getId()+"-descr",append:true}};if(C.getValueState()==sap.ui.core.ValueState.Error){p.invalid=true;}r.writeAccessibilityState(C,p);};return T;},true);
