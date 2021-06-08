function editJsJridData($jsGrid,account,data) {
    let idx = getJSGridIdx($jsGrid,account)
    data["classAndCourseLists"] = $jsGrid.data().JSGrid.data[idx].classAndCourseLists
    $jsGrid.data().JSGrid.data[idx] = data;
    $jsGrid.jsGrid("refresh");
}
function deleteJsJridData($jsjrid,idx) {
    $jsjrid.data().JSGrid.data.splice(idx,1);
    $jsjrid.jsGrid("refresh");
}
function insertJsJridData($jsGrid,data) {
    let d = JSON.parse(data);
    $jsGrid.data().JSGrid.data.splice(0,0,d);
    $jsGrid.jsGrid("refresh");
}
function getJSGridIdx($jsGrid,account) {
    //得到idx
    let jsGridData = $jsGrid.data().JSGrid.data
    for(let i=0;i<jsGridData.length;++i){
        if(account == jsGridData[i].account)
            return i;
    }
    return -1;
}
function refreshJSGridData($jsGrid,data) {
    $jsGrid.data().JSGrid.data = data;
    $jsGrid.jsGrid("refresh");
}