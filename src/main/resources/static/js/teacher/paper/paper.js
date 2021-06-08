$(function () {
    $("#jsGrid").jsGrid({
        width: "100%",
        sorting: true,
        paging: true,
        pageIndex: 1,
        pageSize: 17,
        pageButtonCount: 5,
        // pageLoading: true,
        autoload: true,
        pagerFormat: "页数: {first} {prev} {pages} {next} {last}    第 {pageIndex} 页,共 {pageCount} 页",
        pagePrevText: "←",
        pageNextText: "→",
        pageFirstText: "第一页",
        pageLastText: "最后一页",
        confirmDeleting: true,
        noDataContent: "没有数据...",
        loadMessage: "正在加载数据，请稍候......",
        dataType: "json",
        controller: {
            loadData: function (filter) {
                let localData;
                $.ajax({
                    url: "/teacher/selectMyAllPaper",
                    dataType: "json",
                    data: filter,
                    async: false,
                    success: function (data) {
                        localData = data;
                    }
                });
                return localData;
            }
        },
        fields:[{
            name: "paperName",
            title: "试卷名",
            type: "text",
            width: 20,
            height: 80,
            align: "center",
            css: "overHidden",
            filtering: true
        },{
            name: "begin",
            title: "开考时间",
            type: "text",
            width: 20,
            height: 80,
            align: "center",
            css: "overHidden",
            filtering: true,
            itemTemplate: function (value,item) {
                return new Date(item.begin).toLocaleString();
            }
        },{
            name: "end",
            title: "结考时间",
            type: "text",
            width: 20,
            height: 80,
            align: "center",
            css: "overHidden",
            filtering: true,
            itemTemplate: function (value,item) {
                return new Date(item.end).toLocaleString();
            }
        },{
            title: "考试时间",
            type: "text",
            width: 20,
            height: 80,
            align: "center",
            css: "overHidden",
            filtering: true,
            itemTemplate: function (value,item) {
                return item.during+"分钟";
            }
        },{
            name: "sort",
            title: "是否乱序",
            type: "text",
            width: 5,
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value,item) {
                return item.sort == true ? "是" : "否";
            }
        },{
            name: "openFaceIdentity",
            title: "人脸识别监控",
            type: "text",
            width: 5,
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value,item) {
                return item.openFaceIdentity == true ? "是" : "否";
            }
        },{
            title: "考试课程",
            type: "text",
            width: 5,
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value,item) {
                return item.course.courseName;
            }
        },{
            title: "考试班级",
            type: "text",
            width: 5,
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value,item) {
                return item.cla.claName;
            }
        },{
            title: "",
            width: 20,
            align: "center",
            sorting: false,
            itemTemplate: function (value, item) {
                let d = JSON.stringify(item);
                return '<span class="paper_info" data=\'' + d + '\'><i class="fa fa-search-plus" aria-hidden="true"></i></span>&nbsp;&nbsp;&nbsp;&nbsp;' +
                    '<button class="jsgrid-button jsgrid-edit-button openEditModal" data=\'' + d + '\'></button>&nbsp;&nbsp;&nbsp;&nbsp;' +
                    '<a target="_blank" href="/teacher/paperpdf/'+item.uuid+'"><i class="fa fa-download" aria-hidden="true"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;' +
                    '<button class="jsgrid-button jsgrid-delete-button openDeleteConfirm" data=\'' + d + '\'></button>';
            }

        }]
    });
    $("input[data-bootstrap-switch]").each(function(){
        $(this).bootstrapSwitch({
            onSwitchChange:function(){
                if($(this).attr("state") == "false"){
                    $(this).attr("state","true");
                }else{
                    $(this).attr("state","false");
                }
            }
        });
    });

    $(document).on("click",".paper_info",function (){
        let paper = JSON.parse($(this).attr("data"));
        let problemStrategyList = null;
        $.ajax({
            url:"/teacher/selectPaperStrategyByPaperStrategy_uuid",
            data:{paperStrategy_uuid:paper.paperStrategy_uuid},
            async:false,
            success:function (data){
                problemStrategyList = JSON.parse(data.problemStrategyList);
            }
        })
        let paperInfo = getPaperInfoNode(paper.paperInfo,problemStrategyList);
        $("#paper_info_display").html(paperInfo);
        $("#paper_info_Modal").modal("show")
    })
    $(document).on("click",".openEditModal",function () {
        let data = JSON.parse($(this).attr("data"));
        $("#sort").bootstrapSwitch("state",data.sort);
        $("#loadAll").bootstrapSwitch("state",data.loadAll);
        $("#needRoom").bootstrapSwitch("state",data.needRoom);
        $("#visible").bootstrapSwitch("state",data.visible);
        $("#uuid").val(data.uuid);
        $("#reservationtime").daterangepicker({
            timePicker: true,
            timePickerIncrement: 10,
            minYear: moment().format("YYYY"),
            startDate: moment(data.begin),
            endDate:moment(data.end),
            // minDate: moment(),
            autoApply: true,
            timePicker24Hour: true,
            locale: {
                format: 'MM/DD/YYYY hh:mm A'
            }
        })
        $("#during").val(data.during);
        $("#editModal").modal('show');
    })
    $("#editPaper_server").click(function () {
        let split = $("#reservationtime").val().split("-");
        let begin_time = new Date(split[0]).getTime();
        let end_time = new Date(split[1]).getTime();
        let uuid = $("#uuid").val();
        let sort = $("#sort").val();
        let openFaceIdentity = $("#openFaceIdentity").attr("state") == "true" ? true : false;
        let visible = $("#visible").attr("state") == "true" ? true : false;
        let during = $("#during").val();
        $.ajax({
            url: "/teacher/updatePaperInfo",
            type: "post",
            data:{
                uuid: uuid,
                sort: sort,
                openFaceIdentity: openFaceIdentity,
                visible: visible,
                begin_time: begin_time,
                end_time: end_time,
                during: during
            },
            success: function (data) {
                if(data.code == 200){
                    $(document).Toasts('create', {
                        class: 'bg-success',
                        autohide: true,
                        title: '修改成功',
                        body: data.msg,
                        width: "200"
                    })
                    let $jsGrid = $("#jsGrid");
                    let paper = getJSGridData($jsGrid,uuid);
                    paper.sort = sort;
                    paper.openFaceIdentity = openFaceIdentity;
                    paper.visible = visible;
                    paper.begin = begin_time;
                    paper.end = end_time;
                    paper.during = during;
                    console.log(paper)
                    editJsJridData($jsGrid,$("#uuid").val(),paper);
                }
            }
        })
        $("#editModal").modal('hide');
    })


    /*delete the problem when click the openDeleteConfirm button*/
    $(document).on("click",".openDeleteConfirm",function () {
        if(confirm("确认删除此题吗?")){
            let data = JSON.parse($(this).attr("data"));
            let uuid = data.uuid;
            $.ajax({
                url: "/teacher/deletePaperByUuid",
                data: {uuid:uuid},
                type: "post",
                success: function (data) {
                    if(data.code == 200){
                        //delete problem success
                        let $jsGrid = $("#jsGrid")
                        deleteJsJridData($jsGrid,getJSGridIdx($jsGrid,uuid))
                        $(document).Toasts('create', {
                            class: 'bg-success',
                            autohide: true,
                            title: '删除状况',
                            body: "删除成功",
                            width: "200"
                        })
                    }else{
                        //delete problem error
                        $(document).Toasts('create', {
                            class: 'bg-danger',
                            autohide: true,
                            title: '删除状况',
                            body: data.msg,
                            width: "200"
                        })
                    }
                }
            })
        }
    })
})

function getPaperInfoNode(paper,problemStrategyList){
    let problemIdx = 1;
    let singleChoiceListBody = "";
    let multipleChoiceListBody = "";
    let judgeListBody = "";
    let fillListBody = "";
    let shortListBody = "";
    let programListBody = "";
    let paperHtml = '<div class="">' +
        '            <div class="">';
    console.log(paper)
    $.each(problemStrategyList,function (idx,item) {
        switch (item.problemType) {
            case "单选题":{
                singleChoiceListBody+="<div class=\"col-12 text-bold text-primary text-lg\"><span>第"+getBigNumber(problemIdx++)+"大题&nbsp;&nbsp;&nbsp;单选题</span></div>";
                $.each(paper["singleChoiceList"],function (idx,itm) {//每道题
                    singleChoiceListBody += "<div class=\"col-6\"><span class=\"text-cyan\">第"+(idx+1)+"题</span><span>"+itm.problem+"</span><br>";
                    if(itm.choice_a != ""){
                        singleChoiceListBody+="<span class=\"text-bold\">选项A</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_a+"</span><br>";
                    }
                    if(itm.choice_b != ""){
                        singleChoiceListBody+="<span class=\"text-bold\">选项B</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_b+"</span><br>";
                    }
                    if(itm.choice_c != ""){
                        singleChoiceListBody+="<span class=\"text-bold\">选项C</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_c+"</span><br>";
                    }
                    if(itm.choice_d != ""){
                        singleChoiceListBody+="<span class=\"text-bold\">选项D</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_d+"</span><br>";
                    }
                    if(itm.choice_e != ""){
                        singleChoiceListBody+="<span class=\"text-bold\">选项E</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_e+"</span><br>";
                    }
                    if(itm.choice_f != ""){
                        singleChoiceListBody+="<span class=\"text-bold\">选项F</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_f+"</span><br>";
                    }
                    if(itm.choice_g != ""){
                        singleChoiceListBody+="<span class=\"text-bold\">选项G</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_g+"</span><br>";
                    }
                    if(itm.choice_h != ""){
                        singleChoiceListBody+="<span class=\"text-bold\">选项H</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_h+"</span><br>";
                    }
                    singleChoiceListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+itm.answer+"</span><br>";
                    singleChoiceListBody+="</div>";
                })
                paperHtml +="<div class=\"row\" id=\"singleChoice\">"+singleChoiceListBody+"</div>";
                break;
            }
            case "多选题":{
                multipleChoiceListBody+="<div class=\"col-12 text-bold text-primary text-lg\"><span>第"+getBigNumber(problemIdx++)+"大题&nbsp;&nbsp;&nbsp;多选题</span></div>";
                $.each(paper["multipleChoiceList"],function (idx,itm) {//每道题
                    multipleChoiceListBody += "<div class=\"col-6\"><span class=\"text-cyan\">第"+(idx+1)+"题</span><span>"+itm.problem+"</span><br>";
                    if(itm.choice_a != ""){
                        multipleChoiceListBody+="<span class=\"text-bold\">选项A</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_a+"</span><br>";
                    }
                    if(itm.choice_b != ""){
                        multipleChoiceListBody+="<span class=\"text-bold\">选项B</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_b+"</span><br>";
                    }
                    if(itm.choice_c != ""){
                        multipleChoiceListBody+="<span class=\"text-bold\">选项C</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_c+"</span><br>";
                    }
                    if(itm.choice_d != ""){
                        multipleChoiceListBody+="<span class=\"text-bold\">选项D</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_d+"</span><br>";
                    }
                    if(itm.choice_e != ""){
                        multipleChoiceListBody+="<span class=\"text-bold\">选项E</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_e+"</span><br>";
                    }
                    if(itm.choice_f != ""){
                        multipleChoiceListBody+="<span class=\"text-bold\">选项F</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_f+"</span><br>";
                    }
                    if(itm.choice_g != ""){
                        multipleChoiceListBody+="<span class=\"text-bold\">选项G</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_g+"</span><br>";
                    }
                    if(itm.choice_h != ""){
                        multipleChoiceListBody+="<span class=\"text-bold\">选项H</span>&nbsp;&nbsp;&nbsp;<span>"+itm.choice_h+"</span><br>";
                    }
                    multipleChoiceListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+itm.answer+"</span><br>";
                    multipleChoiceListBody+="</div>";
                })
                paperHtml +="<div class=\"row\" id=\"multipleChoice\">"+multipleChoiceListBody+"</div>";
                break;
            }
            case "填空题":{
                judgeListBody+="<div class=\"col-12 text-bold text-primary text-lg\"><span>第"+getBigNumber(problemIdx++)+"大题&nbsp;&nbsp;&nbsp;填空题</span></div>";
                $.each(paper["fillList"],function (idx,itm) {//每道题
                    fillListBody += "<div class=\"col-6\"><span class=\"text-cyan\">第"+(idx+1)+"题</span><span>"+itm.problem+"</span><br>";
                    fillListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+itm.answer+"</span><br>";
                    fillListBody+="</div>";
                })
                paperHtml +="<div class=\"row\" id=\"fill\">"+fillListBody+"</div>";
                break;
            }
            case "判断题":{
                judgeListBody+="<div class=\"col-12 text-bold text-primary text-lg\"><span>第"+getBigNumber(problemIdx++)+"大题&nbsp;&nbsp;&nbsp;判断题</span></div>";
                $.each(paper["judgeList"],function (idx,itm) {//每道题
                    judgeListBody += "<div class=\"col-6\"><span class=\"text-cyan\">第"+(idx+1)+"题</span><span>"+itm.problem+"</span><br>";
                    judgeListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+(itm.answer == true ? "对" : "错")+"</span><br>";
                    judgeListBody+="</div>";
                })
                paperHtml += "<div class=\"row\" id=\"fill\">"+judgeListBody+"</div>";
                break;
            }
            case "简答题":{
                shortListBody+="<div class=\"col-12 text-bold text-primary text-lg\"><span>第"+getBigNumber(problemIdx++)+"大题&nbsp;&nbsp;&nbsp;简答题</span></div>";
                $.each(paper["shortList"],function (idx,itm) {//每道题
                    shortListBody += "<div class=\"col-6\"><span class=\"text-cyan\">第"+(idx+1)+"题</span><span>"+itm.problem+"</span><br>";
                    shortListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+itm.answer+"</span><br>";
                    shortListBody+="</div>";
                })
                paperHtml += "<div class=\"row\" id=\"short\">"+shortListBody+"</div>";
                break;
            }
            case "编程题":{
                programListBody+="<div class=\"col-12 text-bold text-primary text-lg\"><span>第"+getBigNumber(problemIdx++)+"大题&nbsp;&nbsp;&nbsp;编程题</span></div>";
                $.each(paper["programList"],function (idx,itm) {//每道题
                    programListBody += "<div class=\"col-6\"><span class=\"text-cyan\">第"+(idx+1)+"题</span><span>"+itm.problem+"</span><br>";
                    programListBody += "</div>";
                })
                paperHtml += "<div class=\"row\" id=\"program\">"+programListBody+"</div>";
                break;
            }
        }
    })
    return paperHtml;
}
function getBigNumber(idx) {
    if(idx == 1) return "一"
    if(idx == 2) return "二"
    if(idx == 3) return "三"
    if(idx == 4) return "四"
    if(idx == 5) return "五"
    if(idx == 6) return "六"
}