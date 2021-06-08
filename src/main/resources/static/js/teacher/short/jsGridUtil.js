function editJsJridData($jsGrid,uuid,data) {
    let idx = getJSGridIdx($jsGrid,uuid)
    console.log(data)
    // data["classAndCourseLists"] = $jsGrid.data().JSGrid.data[idx].classAndCourseLists
    $jsGrid.data().JSGrid.data[idx] = data;
    $jsGrid.jsGrid("refresh");
}
function deleteJsJridData($jsjrid,idx) {
    $jsjrid.data().JSGrid.data.splice(idx,1);
    $jsjrid.jsGrid("refresh");
}
function insertJsJridData($jsjrid,data) {
    let d = JSON.parse(data);
    $jsjrid.data().JSGrid.data.splice(0,0,d);
    $jsjrid.jsGrid("refresh");
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