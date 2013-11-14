/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function jq(myid) {
   return '#' + myid.replace(/(:|\.)/g,'\\$1');
}

function showComp(id){
    jQuery(jq(id)).show('slow');
}

function hideComp(id, args){
    jQuery(jq(id)).hide('slow');
}

function addCSS(id){

    alert(id);
    jQuery(jq(id)).addClass(ui-widget-overlay);
}