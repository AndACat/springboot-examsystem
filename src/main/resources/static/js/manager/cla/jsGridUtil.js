
function editJsJridData($jsGrid,uuid,data) {
    let idx = getJSGridIdx($jsGrid,uuid)
    $jsGrid.data().JSGrid.data[idx] = data;
    $jsGrid.jsGrid("refresh");
}
function deleteJsJridData($jsjrid,idx) {
    $jsjrid.data().JSGrid.data.splice(idx,1);
    $jsjrid.jsGrid("refresh");
}
function insertJsJridData($jsGrid,data) {
    $jsGrid.data().JSGrid.data.splice(0,0,data);
    $jsGrid.jsGrid("refresh");
}
function getJSGridIdx($jsGrid,uuid) {
    //得到idx
    let jsGridData = $jsGrid.data().JSGrid.data
    for(let i=0;i<jsGridData.length;++i){
        if(uuid == jsGridData[i].uuid)
            return i;
    }
    return -1;
}
function refreshJSGridData($jsGrid,data) {
    $jsGrid.data().JSGrid.data = data;
    $jsGrid.jsGrid("refresh");
}