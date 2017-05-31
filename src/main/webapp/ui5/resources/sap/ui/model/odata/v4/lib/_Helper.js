/*!
 * UI development toolkit for HTML5 (OpenUI5)
 * (c) Copyright 2009-2016 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(["jquery.sap.global"],function(q){"use strict";var r=/&/g,a=/\=/g,b=/#/g,c=/\+/g,d=/'/g,H;H={buildPath:function(){var i,p=[],s;for(i=0;i<arguments.length;i++){s=arguments[i];if(s||s===0){p.push(s);}}return p.join("/");},buildQuery:function(p){var k,Q;if(!p){return"";}k=Object.keys(p);if(k.length===0){return"";}Q=[];k.forEach(function(K){var v=p[K];if(Array.isArray(v)){v.forEach(function(i){Q.push(H.encodePair(K,i));});}else{Q.push(H.encodePair(K,v));}});return"?"+Q.join("&");},createError:function(j){var B=j.responseText,C=j.getResponseHeader("Content-Type"),R=new Error(j.status+" "+j.statusText);R.status=j.status;R.statusText=j.statusText;if(j.status===0){R.message="Network error";return R;}if(C){C=C.split(";")[0];}if(j.status===412){R.isConcurrentModification=true;}if(C==="application/json"){try{R.error=JSON.parse(B).error;R.message=R.error.message;}catch(e){q.sap.log.warning(e.toString(),B,"sap.ui.model.odata.v4.lib._Helper");}}else if(C==="text/plain"){R.message=B;}return R;},encode:function(p,e){var E=encodeURI(p).replace(r,"%26").replace(b,"%23").replace(c,"%2B");if(e){E=E.replace(a,"%3D");}return E;},encodePair:function(k,v){return H.encode(k,true)+"="+H.encode(v,false);},formatLiteral:function(v,t){if(v===null){return"null";}switch(t){case"Edm.Binary":return"binary'"+v+"'";case"Edm.Boolean":case"Edm.Byte":case"Edm.Double":case"Edm.Int16":case"Edm.Int32":case"Edm.SByte":case"Edm.Single":return String(v);case"Edm.Date":case"Edm.DateTimeOffset":case"Edm.Decimal":case"Edm.Guid":case"Edm.Int64":case"Edm.TimeOfDay":return v;case"Edm.Duration":return"duration'"+v+"'";case"Edm.String":return"'"+v.replace(d,"''")+"'";default:throw new Error("Unsupported type: "+t);}},isSafeInteger:function(n){if(typeof n!=="number"||!isFinite(n)){return false;}n=Math.abs(n);return n<=9007199254740991&&Math.floor(n)===n;}};return H;},false);
