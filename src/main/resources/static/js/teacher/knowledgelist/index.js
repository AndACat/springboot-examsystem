$(function () {
    /*init for Swal*/
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 1500
    });
    /*the knowledgeArray of current teacher*/
    let knowledgeArrayList = getKnowledgeArrayListFromServer();//get data from server
    initNestedSortableDivs(knowledgeArrayList);
    /*create knowledgeArray divs where click*/
    $("#createNewKnowledgeArray").click(function () {
        let $newKnowledgeDiv = $("#newKnowledgeDiv");
        let $nested_1 = $('<div style="padding: 0.1rem 1rem;" class="list-group-item nested-1 newNestedSortable"></div>');
        $nested_1.append($('<i class="fa fa-arrows-alt handle"></i>&nbsp;&nbsp;'))
        $nested_1.append('<input type="text" style="width: 50%;display: inline;" class="form-control" placeholder="分类名">' +
            '<button class="btn btn-primary createNewKnowledge">创建新知识点名称</button>' +
            '<button class="btn btn-danger removeKnowledgeArray">删除</button>' +
            '<div class="list-group new-nested-sortable"></div>');
        $newKnowledgeDiv.append($nested_1);
        initNewNestedSortable();
    })
    /*create knowledgeList divs where click*/
    $(document).on("click",".createNewKnowledge",function () {
        console.log("创建新知识点名称");
        let $nested_sortable = $(this).next().next();
        $nested_sortable.append('<div style="padding: 0.1rem 1rem;" class="list-group-item nested-2"><i class="fa fa-arrows-alt handle"></i>&nbsp;&nbsp;<input class="form-control" style="width: 50%;display: inline;" placeholder="知识点名"><button class="btn btn-danger removeKnowledge">删除</button></div>');
        initNewNestedSortable();
    })
    /*remove new knowledgeArray where click*/
    $(document).on("click",".removeKnowledge , .removeKnowledgeArray",function () {
        $(this).parent().remove();
    })
    /*save new (user create) knowledgeArray where click*/
    $("#saveNewKnowledgeArray").click(function () {
        let data = getNewKnowledgeArray();
        if(data.length == 0){
            $(document).Toasts('create', {
                class: 'bg-danger',
                autohide: true,
                title: '',
                body: "保存失败",
                width: "200"
            })
            return;
        }
        $.ajax({
            url:"/teacher/insertKnowledgeArrayList",
            type:"post",
            data:{
                knowledgeArrayList : JSON.stringify(data)
            },
            success:function (data) {
                if(data.code == "200" || data.code==200){
                    //reload the data from the server and reload the homologous divs
                    initNestedSortableDivs(getKnowledgeArrayListFromServer());
                    $(document).Toasts('create', {
                        class: 'bg-success',
                        autohide: true,
                        title: '',
                        body: "保存成功",
                        width: "200"
                    })
                    /*delete the divs in the left of monitor*/
                    window.location.reload()
                }else{
                    $(document).Toasts('create', {
                        class: 'bg-error',
                        title: '',
                        body: "保存失败",
                        width: "200"
                    })
                }
            }
        })
    })
    /*save the alter of server data, in the left of monitor*/
    $("#saveKnowledgeArrayList").click(function () {
        let knowledgeArrayList = getKnowledgeArrayList();
        $.ajax({
            url:"/teacher/updateKnowledgeArrayList",
            method:"post",
            data:{
              knowledgeArrayList:JSON.stringify(knowledgeArrayList)
            },
            async: false,
            success: function (data) {
                if(data.code == 200){
                    // initNestedSortableDivs(getKnowledgeArrayListFromServer());
                    $(document).Toasts('create', {
                        class: 'bg-success',
                        title: '',
                        autohide:true,
                        body: "保存成功",
                        width: "200"
                    })
                }
            }
        })
        return knowledgeArrayList;
    })

    /*listener the class edit_nested_2 and edit_nested_1*/
    $(document).on("click",".edit_nested_2 , .edit_nested_1",function () {
        console.log("edit_nested_2")
        let text = $(this).parent().children('span').text();
        $(this).parent().children('span').replaceWith('<input id="autofocus" value="'+ text +'" onblur="changeTag()" class="form-control" style="width:50%;height:50%;display:inline" >')
        $("#autofocus").focus();
    })
    /*listener the class delete_nested_2 and delete_nested_1*/
    $(document).on("click",".delete_nested_2 , .delete_nested_1",function () {
        if(confirm("确认删除吗？")){
            $(this).parent().remove();
        }
    })
})
/*get knowledgeArray list from server and return the data*/
function getKnowledgeArrayListFromServer() {
    let knowledgeArrayList;
    /*加载当前登录教师的知识点集合*/
    $.ajax({
        url: "/teacher/selectAllKnowledgeArray",
        type: "post",
        async: false
    }).done(function (data) {
        knowledgeArrayList = data;
    })
    return knowledgeArrayList;
}
/*init nested sortable from server to complete the sort of new knowledgeArray divs*/
function initNestedSortableDivs(knowledgeArrayList) {
    let $knowledgeArray_head = $("#knowledgeArray-head");
    let $knowledgeArray = $("#knowledgeArray");
    $knowledgeArray_head.html("");
    $knowledgeArray.html("");
    /*遍历knowledgeArray,给$knowledgeArray添加节点*/
    for(let i=0;i<knowledgeArrayList.length;++i){
        let knowledge = knowledgeArrayList[i];
        let $knowledgeNode = $('<div style="padding: 0.1rem 1rem;"></div>');
        if(i==0){
            $knowledgeNode.append('<span class="dishandle">固定分组:</span>&nbsp;&nbsp;');
        }else{
            $knowledgeNode.append('<i class="fa fa-arrows-alt handle"></i>&nbsp;&nbsp;');
        }
        $knowledgeNode.append('<span>'+knowledge.name+"</span><input style='display: none' value=\""+knowledge.uuid+"\">");
        if(i!=0){//name="默认分组"(it's i equals 0) can't edit and delete
            $knowledgeNode.append('<i style="float: right" class="fa fa-trash delete_nested_1"></i><i style="float: right" class="fa fa-edit edit_nested_1"></i>')
        }
        $knowledgeNode.attr("class","list-group-item nested-1");
        let $nested_sortable = $('<div class="list-group nested-sortable"></div>')
        $knowledgeNode.append($nested_sortable);
        let knowledgeList = knowledge.knowledgeList;
        for(let j=0;j<knowledgeList.length;++j){
            $nested_sortable.append('<div style="padding: 0.1rem 1rem;" class="list-group-item nested-2">' +
                '<i class="fa fa-arrows-alt handle"></i>&nbsp;&nbsp;<span>'+ knowledgeList[j] +'</span><i style="float: right" class="fa fa-trash delete_nested_2"></i><i class="fa fa-edit edit_nested_2" style="float: right"></i></div>');
        }
        if(i==0){
            $knowledgeArray_head.append($knowledgeNode);
        }else{
            $knowledgeArray.append($knowledgeNode);
        }

    }
    let $sortable = $(".nested-sortable");
    for(let i=0;i<$sortable.length;++i){
        let sortable;
        if(i==1){
            sortable = new Sortable($sortable[i],{
                group:"nested-container",
                animation:150,//排序时间，毫秒
                handle:".handle",
                filter:".dishandle"
            });
            continue;
        }
        // if(i==$sortable.length-1){
        //     sortable = new Sortable($sortable[i],{
        //         group:"nested",
        //         animation:150,//排序时间，毫秒
        //         handle:".handle"
        //     });
        //     continue;
        // }
        sortable = new Sortable($sortable[i],{
            group:"nested",
            animation:150,//排序时间，毫秒
            handle:".handle"
        });
    }

}
/*init new knowledgeArray nested sortable in order to complete the sort of new knowledgeArray divs*/
function initNewNestedSortable() {
    let $new_nested_sortable = $(".new-nested-sortable");
    for(let i=0;i<$new_nested_sortable.length;++i){
        if(i==0){
            new Sortable($new_nested_sortable[i],{
                group:"new-nested-body",
                animation:150,//排序时间，毫秒
                handle:".handle"
            })
        }else{
            new Sortable($new_nested_sortable[i],{
                group:"new-nested",
                animation:150,//排序时间，毫秒
                handle:".handle"
            })
        }

    }

}
/*get new knowledgeArray from user custom edit in right of monitor*/
function getNewKnowledgeArray() {
    if(!checkNewKnowledgeArray()){//检查结果为 false
        return [];
    }else{
        //对页面便利得到添加的知识点集合，汇总之后返回
        let knowledgeArrayList = [];//整个知识点集合
        let $newNestedSortable = $(".newNestedSortable");
        for(let i=0;i<$newNestedSortable.length;++i){
            let knowledgeArray = {};//单个知识点集合
            let knowledgeList = [];//单个知识点集合中的知识点群
            let name = $newNestedSortable[i].children[1].value;
            knowledgeArray.name = name;
            let knowledgeListLength = $newNestedSortable[i].children[4].children.length;
            for(let j=0;j<knowledgeListLength;++j){
                let knowledge = $newNestedSortable[i].children[4].children[j].children[1].value;
                knowledgeList.push(knowledge);//push 单个知识点集合中的知识点
            }
            knowledgeArray.knowledgeList = knowledgeList;
            knowledgeArrayList.push(knowledgeArray);
        }
        return knowledgeArrayList;
    }
}
/*check the valid of the data of new knowledgeArray of user custom*/
function checkNewKnowledgeArray() {
    let tag = true;
    let $newNestedSortable = $(".newNestedSortable");
    let $inputs = $newNestedSortable.find("input");
    for(let i=0;i<$inputs.length;++i){
        if($inputs[i].value.trim() == ""){
            console.log($inputs[i].value.trim())
            $inputs[i].setAttribute("class","form-control is-invalid");
            tag = false;//检查有空内容 return false;
        }else{
            $inputs[i].setAttribute("class","form-control is-valid");
            tag = true;//检查无空内容 return true;
        }
    }
    return tag;
}
/*where the tag(input) onblur*/
function changeTag() {
    let event = window.event || arguments.callee.caller.arguments[0];
    //target 就是这个对象
    let target = event.srcElement||event.target;
    console.log(target.parentNode.childNodes[2].tagName);
    if(target.parentNode.childNodes[2].tagName == "INPUT"){
        console.log("come..")
        let value = target.parentNode.childNodes[2].value;
        let span = document.createElement("span");
        span.innerText=value;
        target.parentNode.replaceChild(span,target.parentNode.childNodes[2])
    }
}
/*get data of the left of the monitor*/
function getKnowledgeArrayList() {
    let knowledgeArrayList = [];
    let knowledgeArray_head = {};
    knowledgeArray_head.name = "默认分组";


    //knowledgeArray head
    //start to collective the head knowledgeArray (name equals "默认分组")
    let $knowledgeArray_head = $("#knowledgeArray-head");
    knowledgeArray_head.uuid = $knowledgeArray_head.children(0).children("input").val();
    let $headElement = $knowledgeArray_head.children(0).children('div').children('div');
    let head_length = $headElement.length;//knowledgeArray head's length

    let knowledgeArray_head_body = [];
    for(let i=0;i<head_length;++i){
        knowledgeArray_head_body.push($headElement[i].innerText.trim());
    }
    knowledgeArray_head.knowledgeList = knowledgeArray_head_body;
    knowledgeArrayList.push(knowledgeArray_head);//head knowledgeArray(name "默认分组") has completely collection and it has append to the List

    //knowledgeArray body
    //start to collective the body knowledgeArray (name not equals "默认分组")
    let $knowledgeArray_body_divs = $("#knowledgeArray").children();
    let body_length = $knowledgeArray_body_divs.length;
    for(let i=0;i<body_length;++i){
        let eachKnowledgeArray = {};
        let eachKnowledgeList = [];
        //once used $[i], its has changed from jquery instance to js instance, so can't use jquery method to get something
        let eachKnowledge_name = $knowledgeArray_body_divs[i].children[1].innerText.trim();
        eachKnowledgeArray.name = eachKnowledge_name;
        eachKnowledgeArray.uuid = $knowledgeArray_body_divs[i].children[2].value;
        let eachKnowledge_knowledgeList_divs = $knowledgeArray_body_divs[i].children[5];
        for(let j=0;j<eachKnowledge_knowledgeList_divs.childElementCount;++j){
            eachKnowledgeList.push(eachKnowledge_knowledgeList_divs.childNodes[j].innerText.trim());
        }
        eachKnowledgeArray.knowledgeList = eachKnowledgeList;
        knowledgeArrayList.push(eachKnowledgeArray);
    }
    return knowledgeArrayList;
}


