$(function () {
    $(".form-inline").remove();//移除搜索框
    $(".main-header").find('ul[class="navbar-nav"]').append('<button id="openCreateNewPaperStrategyModal" class="btn btn-primary">添加</button>')

    $.ajax({
        url: "/teacher/selectMyAllPaperStrategy",
        type: "post",
        success: function (data) {
            $.each(data,function (idx,item) {
                item.problemStrategyList = JSON.parse(item.problemStrategyList)
                $("#paperStrategyPlace").append(getProblemStrategyNode(item))
            })
        }
    })

    $(document).on("click","#openCreateNewPaperStrategyModal",function () {
        console.log("添加新策略");
        $(".modal-title").text("修改")
        $("#savePaperStrategy_server").removeAttr("uuid");
        $("#nested").html("");//清空策略内容
        $("#paperStrategyName").val("");//清空策略名
        $("#paperAllScore").val("");//清空总分
        $("#modal").modal('show')
    })

    $("#createNewPaperStrategy").click(function () {
        if($("#nested").children().length >= 6){
            $(document).Toasts('create', {
                class: 'bg-success',
                title: '操作状态',
                autohide:true,
                body: "无法创建更多的题型",
                width: "200",
                delay: 2500
            })
            return
        }

        $("#nested").append('<div class="list-group-item nested-2">' +
            '                          <i class="fa fa-arrows-alt handle"></i>&nbsp;&nbsp;' +
            '<select class="form-control reSizeInput">' +
            '   <option value="单选题">单选题</option>' +
            '   <option value="多选题">多选题</option>' +
            '   <option value="判断题">判断题</option>' +
            '   <option value="填空题">填空题</option>' +
            '   <option value="简答题">简答题</option>' +
            '   <option value="编程题">编程题</option>' +
            '</select>' +
            '<input class="form-control reSizeInput problemNum" onkeyup="checkProblemNum(this)" placeholder="小题数量">' +
            '<input class="form-control reSizeInput problemScore" onkeyup="checkProblemScore(this)" placeholder="每小题分数">' +
            '<input class="form-control reSizeInput" disabled placeholder="总分">' +
            '<i style="float: right" class="fa fa-trash delete_nested_2"></i>' +
            '</div>')
        new Sortable($(".nested-sortable")[0],{
            group:"new",
            animation:150,//排序时间，毫秒
            handle:".handle"
        })
    })
    $("#saveNewPaperStrategy").click(function () {

    })
    $(document).on("click",".delete_nested_2",function () {
        $(this).parent().remove()
    })

    $(document).on("input propertychange",".problemNum",function () {
        let next = $(this).next();
        let problemScore = next.val();
        next.next().val($(this).val() * problemScore)
        refreshPaperTotalScore()
    })
    $(document).on("input propertychange",".problemScore",function () {
        let prev = $(this).prev();
        let problemNum = prev.val();
        if(problemNum)
        $(this).next().val($(this).val() * problemNum)
        refreshPaperTotalScore()
    })
    $("#savePaperStrategy_server").click(function () {
        if(checkProblemStrategy()){
            let problemStrategyData = getProblemStrategyData();
            let paperStrategy = JSON.stringify(problemStrategyData);
            let uuid = $(this).attr("uuid");
            if(uuid == undefined || uuid == null || uuid.trim() == ""){
               //添加
                $.ajax({
                    url: "/teacher/insertPaperStrategy",
                    type: "post",
                    data: {paperStrategy:paperStrategy},
                    success: function (data) {
                        if(data.code == 200){
                            $(document).Toasts('create', {
                                class: 'bg-success',
                                title: '操作状态',
                                autohide: true,
                                body: "保存成功",
                                width: "200",
                                delay: 2500
                            })
                            $("#modal").modal('hide');
                            problemStrategyData.uuid = data.data;
                            $("#paperStrategyPlace").append(getProblemStrategyNode(problemStrategyData))
                        }
                    }
                })
            }else{
                //修改
                problemStrategyData.uuid = uuid;
                paperStrategy = JSON.stringify(problemStrategyData);
                $.ajax({
                    url: "/teacher/updatePaperStrategyInfo",
                    type: "post",
                    data: {paperStrategy:paperStrategy},
                    success: function (data) {
                        if(data.code == 200){
                            $(document).Toasts('create', {
                                class: 'bg-success',
                                title: '操作状态',
                                autohide: true,
                                body: "修改成功",
                                width: "200",
                                delay: 2500
                            })
                            let self = $("span[uuid='"+uuid+"']").parent().parent().parent();
                            self.after(getProblemStrategyNode(problemStrategyData));
                            self.remove();
                            $("#modal").modal('hide');
                        }
                    }
                })
            }

        }
    })

    $(document).on("click",".deletePaperStrategy",function () {
        let uuid = $(this).parent().children()[0].getAttribute("uuid");
        if(uuid != null || uuid.trim()!=""){
            if(confirm("确认删除吗？")){
                $.ajax({
                    url: "/teacher/deletePaperStrategy",
                    type: "post",
                    data:{uuid:uuid},
                    success: function (data) {
                        if(data.code == 200){
                            $("input[value="+uuid+"]").parent().parent().parent().remove();
                            $(document).Toasts('create', {
                                class: 'bg-success',
                                title: '操作状态',
                                autohide: true,
                                body: "删除成功",
                                width: "200",
                                delay: 2500
                            })
                        }else{
                            $(document).Toasts('create', {
                                class: 'bg-danger',
                                title: '操作状态',
                                autohide: true,
                                body: data.msg,
                                width: "200",
                                delay: 2500
                            })
                        }
                    }
                })
            }
        }
    })
    $(document).on("click",".editPaperStrategy",function () {
        $("#nested").html("");
        $(".modal-title").text("修改")
        let data = $(this).prev().prev().prev().prev().attr("data");
        let paperStrategyData = JSON.parse(data);
        $("#paperStrategyName").val(paperStrategyData.paperStrategyName);
        $("#paperAllScore").val(paperStrategyData.allScore);
        $.each(paperStrategyData.problemStrategyList,function (idx,item) {
            $("#nested").append('<div class="list-group-item nested-2">' +
                '                          <i class="fa fa-arrows-alt handle"></i>&nbsp;&nbsp;' +
                '<select id="temp_select" class="form-control reSizeInput" disabled="disabled">' +
                '   <option value="单选题">单选题</option>' +
                '   <option value="多选题">多选题</option>' +
                '   <option value="判断题">判断题</option>' +
                '   <option value="填空题">填空题</option>' +
                '   <option value="简答题">简答题</option>' +
                '   <option value="编程题">编程题</option>' +
                '</select>' +
                '<input class="form-control reSizeInput problemNum" value="'+item.problemNum+'" disabled="disabled" onkeyup="checkProblemNum(this)" placeholder="小题数量">' +
                '<input class="form-control reSizeInput problemScore" value="'+item.problemScore+'" disabled="disabled" onkeyup="checkProblemScore(this)" placeholder="每小题分数">' +
                '<input class="form-control reSizeInput" disabled value="'+item.problemAllScore+'" placeholder="总分">' +
                '<i style="float: right" class="fa fa-trash delete_nested_2"></i>' +
                '</div>')
            $("#temp_select").val(item.problemType);
            $("#temp_select").removeAttr("id");
        })
        new Sortable($(".nested-sortable")[0],{
            group:"new",
            animation:150,//排序时间，毫秒
            handle:".handle"
        })
        $("#savePaperStrategy_server").attr("uuid",paperStrategyData.uuid)
        $("#modal").modal('show');
    })

})
function checkProblemNum(input) {
    if((input.value+"").trim() == "0"){
        input.value = "";
        return;
    }
    if(isNaN(input.value)){
        document.execCommand('undo')
    }else{
        if(input.value > 100 ||String(input.value).indexOf('.') != -1){
            document.execCommand('undo')
        }
    }
}
function checkProblemScore(input) {
    if(input.value.trim() == "0"){
        return;
        input.value = "";
    }
    if(isNaN(input.value)){
        document.execCommand('undo')
    }else{
        let idx = String(input.value).indexOf('.')
        if(idx != -1){
            if(String(input.value).length-1-idx > 1){//小数点数量，支持小数点一位
                document.execCommand('undo')
            }
        }
    }
}
function refreshPaperTotalScore() {
    let totalScore = 0;
    let children = $("#nested").children()
    for(let i=0;i<children.length;i++){
        let score = children[i].children[4].value
        if((score+"").trim() != ""){
            totalScore = (totalScore-0)+(score-0)
        }
    }
    $("#paperAllScore").val(totalScore)
}
function checkProblemStrategy() {
    let tag = true;
    let paperStrategyName = $("#paperStrategyName").val().trim()
    if(paperStrategyName == ""){
        tag = false;
        $(document).Toasts('create', {
            class: 'bg-danger',
            title: '操作状态',
            autohide: true,
            body: "策略名为空",
            width: "200",
            delay: 2500
        })
    }
    let nestedChildren = $("#nested").children();
    let problemType = [];
    for(let i=0;i<nestedChildren.length;i++){
        let type = nestedChildren[i].children[1].value
        let problemNum = nestedChildren[i].children[2].value
        let problemScore = nestedChildren[i].children[3].value
        if(problemNum.trim() == "" || problemScore.trim() == ""){
            $(document).Toasts('create', {
                class: 'bg-danger',
                title: '操作状态',
                autohide: true,
                body: "题型未设置数量或分数",
                width: "200",
                delay: 2500
            })
        }
        if(problemType.includes(type)){
            tag = false;
            $(document).Toasts('create', {
                class: 'bg-danger',
                title: '操作状态',
                autohide: true,
                body: "题型重复",
                width: "200",
                delay: 2500
            })
            break;
        }else{
            problemType.push(type)
        }
    }
    return tag;
}
function getProblemStrategyData() {
    let paperStrategy = {};
    paperStrategy.paperStrategyName = $("#paperStrategyName").val().trim();
    paperStrategy.allScore = $("#paperAllScore").val();
    let problemStrategyList = [];
    let nestedChildren = $("#nested").children();
    for(let i=0;i<nestedChildren.length;i++){
        let problemStrategy = {};
        let problemType = nestedChildren[i].children[1].value
        let problemNum = nestedChildren[i].children[2].value
        let problemScore = nestedChildren[i].children[3].value
        let problemAllScore = problemNum * problemScore;
        problemStrategy.problemType = problemType;
        problemStrategy.problemNum = problemNum;
        problemStrategy.problemScore = problemScore;
        problemStrategy.problemAllScore = problemAllScore;
        problemStrategyList.push(problemStrategy);
    }
    paperStrategy.problemStrategyList = problemStrategyList;
    return paperStrategy;
}
function getBigNumber(idx) {
    if(idx == 1) return "一"
    if(idx == 2) return "二"
    if(idx == 3) return "三"
    if(idx == 4) return "四"
    if(idx == 5) return "五"
    if(idx == 6) return "六"
}
function getProblemStrategyNode(problemStrategyData) {
    let body = "";
    $.each(problemStrategyData.problemStrategyList,function (idx,item) {
        body+='<span class="red">第'+getBigNumber(idx+1)+'题：&nbsp;&nbsp;&nbsp;</span><span>'+item.problemType+'&nbsp;&nbsp;&nbsp;</span><span>'+item.problemNum+'</span><span>道&nbsp;&nbsp;&nbsp;</span><span>'+item.problemAllScore+'</span><span>分</span><br>'
    })
    let node = '<div class="col-xl-3 col-sm-4">' +
        '                    <div class="card card-default">' +
        '                        <div class="card-header">' +
        '<span style="display: none" uuid="'+ problemStrategyData.uuid +'" data=\''+JSON.stringify(problemStrategyData)+'\'></span>' +
        '                            <span class="red">总&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;分:&nbsp;&nbsp;&nbsp;&nbsp;</span><span>'+problemStrategyData.allScore+'</span>' +
        '                            <i style="float: right" class="fa fa-trash deletePaperStrategy"></i>&nbsp;&nbsp;&nbsp;&nbsp;' +
        '                            <i style="float: right" class="fa fa-edit editPaperStrategy"></i>&nbsp;&nbsp;&nbsp;&nbsp;<br>' +
        '                            <span class="red">策略名：</span><span>'+problemStrategyData.paperStrategyName+'</span>' +
        '                               ' +
        '                        </div>' +
        '                        <div class="card-body">' + body +
        '                        </div>' +
        '                    </div>' +
        '                </div>'
    return node;

}