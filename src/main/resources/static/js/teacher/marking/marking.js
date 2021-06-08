const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 1500
});
let paper_uuid = "";
let student_uuid = "";
let studentPaperAnswer_uuid ="";
$(function () {
    let $search1 = $("#search1");
    let $paper_select_node = $search1.parent();
    let paper = null;
    let paperStrategy = null;
    let defaultProblemScoreConfig = {visible:false};
    $paper_select_node.html("");
    $paper_select_node.append("<select id='markingPaperSelect' class='form-control' style='width: 300px;'></select><button class='btn btn-info btn-sm' id='markPaper'>阅此卷</button>");
    $.ajax({
        url: "/teacher/getMarkingPaper",
        async: false,
        success:function (data) {
            let $markingPaperSelect = $("#markingPaperSelect");
            $.each(data,function (k,item) {
                let option = "<option value='"+k+"'>"+item+"</option>";
                $markingPaperSelect.append(option);
            })
        }
    })

    $(document).on("click","#markPaper",function () {
        let paper_uuid = $(this).prev().val();
        $.ajax({
            url: "/teacher/selectPaperByPaper_uuid",
            async: false,
            data: {paper_uuid:paper_uuid},
            success: function (data) {
                paper = data;
            }
        });
        $.ajax({
            url: "/teacher/selectPaperStrategyByPaperStrategy_uuid",
            async: false,
            data: {paperStrategy_uuid:paper.paperStrategy_uuid},
            success: function (data) {
                paperStrategy = data;
            }
        });
        let problemScoreConfigs = getProblemScoreConfig(paperStrategy);
        $("#jsGrid").jsGrid({
            height: "650",
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
                        url: "/teacher/selectOneRequiredMarkingPaper",
                        dataType: "json",
                        data: {paper_uuid:paper_uuid},
                        async: false,
                        success: function (data) {
                            localData = data;
                        }
                    });
                    return localData;
                }
            },
            fields:[{
                title: "学生姓名",
                type: "text",
                width: 20,
                height: 80,
                align: "center",
                css: "overHidden",
                filtering: true,
                itemTemplate: function (value,item) {
                    return item.student.name;
                }
            },
                problemScoreConfigs[0],
                problemScoreConfigs[1] == "" ? defaultProblemScoreConfig : problemScoreConfigs[1],
                problemScoreConfigs[2] == "" ? defaultProblemScoreConfig : problemScoreConfigs[2],
                problemScoreConfigs[3] == "" ? defaultProblemScoreConfig : problemScoreConfigs[3],
                problemScoreConfigs[4] == "" ? defaultProblemScoreConfig : problemScoreConfigs[4],
                problemScoreConfigs[5] == "" ? defaultProblemScoreConfig : problemScoreConfigs[5],
                {
                name: "totalScore",
                title: "总分",
                type: "text",
                width: 5,
                height: 80,
                align: "center",
                filtering: true,
                itemTemplate: function (value,item) {
                    return (item.totalScore == undefined || item.totalScore == null) ? "<span class='text-cyan'>未阅卷</span>" : item.totalScore;
                }
            },{
                title: "答题结束时间",
                type: "text",
                width: 25,
                css: "overHidden",
                height: 80,
                align: "center",
                filtering: true,
                itemTemplate: function (value,item) {
                    return new Date(item.sort).toLocaleString();
                }
            },{
                title: "是否及格",
                type: "text",
                width: 25,
                css: "overHidden",
                height: 80,
                align: "center",
                filtering: true,
                itemTemplate: function (value,item) {
                    // return (item.totalScore / item.paperTotalScore) > 0.6 ? "及格" : "<span class='text-navy'>不及格</span>";
                    return item.complete ? item.pass ? "及格" : "<span class='text-orange'>不及格</span>" : "<span class='text-cyan'>未阅卷</span>";
                }
            },{
                title: "是否阅卷完成",
                type: "text",
                width: 25,
                css: "overHidden",
                height: 80,
                align: "center",
                filtering: true,
                itemTemplate: function (value,item) {
                    return item.complete ? "阅卷完成" : "<span class='text-orange'>阅卷未完成</span>";
                }
            },{
                title: "",
                width: 20,
                align: "center",
                sorting: false,
                itemTemplate: function (value, item) {
                    let d = JSON.stringify(item);
                    if(item.complete){
                        return '<button class="btn btn-sm btn-info disabled">阅卷</button>';
                    }else{
                        return '<button class="btn btn-sm btn-info markPaper" data=\''+d+'\'>阅卷</button>';
                    }

                }

            }]
        });
    })

    $(document).on("click",".markPaper",function () {
        let studentPaperAnswer = JSON.parse($(this).attr("data"));
        paper_uuid = studentPaperAnswer.paper_uuid;
        student_uuid = studentPaperAnswer.student_uuid;
        studentPaperAnswer_uuid = studentPaperAnswer.uuid;
        let answers = studentPaperAnswer["answers"];//eg: {1:"D",2:"B",3:"B",4:"B",5:"A"}
        let problemStrategyList = JSON.parse(paperStrategy.problemStrategyList);
        let paperInfo = paper.paperInfo;
        let markPlaceHtml = "";
        let singleChoiceListBody = "";
        let multipleChoiceListBody = "";
        let fillListBody = "";
        let shortListBody = "";
        let judgeListBody = "";
        let programListBody = "";

        let bigProblemIdx = 1;//某某大题
        let problemIdx = 1;//小题序号
        for (const problemStrategy of problemStrategyList) {
            let problemType = problemStrategy.problemType;
            let problemScore = problemStrategy.problemScore;
            switch (problemType) {
                case "单选题":{
                    let problemInfoList = paperInfo["singleChoiceList"];
                    singleChoiceListBody+="<div class='col-12'><div class=\"card card-default\"><div class=\"card-header\"><div class='card-title text-bold text-primary text-lg'>第"+getBigNumber(bigProblemIdx++)+"大题&nbsp;&nbsp;&nbsp;单选题(自动阅卷)</div><div class=\"card-tools\"><button type=\"button\" class=\"btn btn-tool\" data-card-widget=\"collapse\"><i class=\"fas fa-minus\"></i></button></div></div><div class=\"card-body\"><div class='row'>";
                    $.each(problemInfoList,function (idx,itm) {//每道题
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
                        let answer = answers[problemIdx++];
                        singleChoiceListBody+="<span class=\"text-bold\">考生答案</span>&nbsp;&nbsp;&nbsp;<span class='text-orange'>"+answer+"</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                        singleChoiceListBody+="<span class=\"text-red\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+itm.answer+"</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                        singleChoiceListBody+="<span class=\"text-bold\">得分情况</span>&nbsp;&nbsp;&nbsp;<span class='text-primary'>"+(answer == itm.answer ? problemScore+"分" : '0分')+"</span><br>";
                        singleChoiceListBody+="</div>";
                    })
                    singleChoiceListBody+="</div></div></div></div>";
                    console.log(singleChoiceListBody)
                    markPlaceHtml +="<div class=\"row\" id=\"singleChoice\">"+singleChoiceListBody+"</div>";
                    break;
                }
                case "多选题":{
                    let problemInfoList = paperInfo["multipleChoiceList"];
                    multipleChoiceListBody+="<div class='col-12'><div class=\"card card-default\"><div class=\"card-header\"><div class='card-title text-bold text-primary text-lg'>第"+getBigNumber(bigProblemIdx++)+"大题&nbsp;&nbsp;&nbsp;多选题(自动阅卷)</div><div class=\"card-tools\"><button type=\"button\" class=\"btn btn-tool\" data-card-widget=\"collapse\"><i class=\"fas fa-minus\"></i></button></div></div><div class=\"card-body\"><div class='row'>";
                    $.each(problemInfoList,function (idx,itm) {//每道题
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
                        let answer = answers[problemIdx++];
                        multipleChoiceListBody+="<span class=\"text-bold\">考生答案</span>&nbsp;&nbsp;&nbsp;<span class='text-orange'>"+answer.sort().join("").toUpperCase()+"</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                        multipleChoiceListBody+="<span class=\"text-red\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+itm.answer.join("").toUpperCase()+"</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                        multipleChoiceListBody+="<span class=\"text-bold\">得分情况</span>&nbsp;&nbsp;&nbsp;<span class='text-primary'>"+(answer.sort().join("").toUpperCase() == itm.answer.sort().join("").toUpperCase() ? problemScore+"分" : '0分')+"</span><br>";
                        multipleChoiceListBody+="</div>";
                    })
                    multipleChoiceListBody+="</div></div></div></div>";
                    markPlaceHtml +="<div class=\"row\" id=\"multipleChoice\">"+multipleChoiceListBody+"</div>";
                    break;
                }
                case "判断题":{
                    let problemInfoList = paperInfo["judgeList"];
                    judgeListBody+="<div class='col-12'><div class=\"card card-default\"><div class=\"card-header\"><div class='card-title text-bold text-primary text-lg'>第"+getBigNumber(bigProblemIdx++)+"大题&nbsp;&nbsp;&nbsp;判断题(自动阅卷)</div><div class=\"card-tools\"><button type=\"button\" class=\"btn btn-tool\" data-card-widget=\"collapse\"><i class=\"fas fa-minus\"></i></button></div></div><div class=\"card-body\"><div class='row'>";
                    $.each(problemInfoList,function (idx,itm) {//每道题
                        let answer = answers[problemIdx++];
                        judgeListBody += "<div class=\"col-6\"><span class=\"text-cyan\">第"+(idx+1)+"题</span><span>"+itm.problem+"</span><br>";
                        judgeListBody+="<span class=\"text-bold\">考生答案</span>&nbsp;&nbsp;&nbsp;<span>"+(answer == true ? "对" : "错")+"</span><br>";
                        judgeListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+(itm.answer == true ? "对" : "错")+"</span><br>";
                        judgeListBody+="<span class=\"text-bold\">得分情况</span>&nbsp;&nbsp;&nbsp;<span class='text-primary'>"+(answer == itm.answer ? problemScore+"分" : '0分')+"</span><br>";
                        judgeListBody+="</div>";
                    });
                    judgeListBody+="</div></div></div></div>";
                    console.log(judgeListBody)
                    markPlaceHtml +="<div class=\"row\" id=\"judge\">"+judgeListBody+"</div>";
                    break;
                }
                case "简答题":{
                    let problemInfoList = paperInfo["shortList"];
                    shortListBody+="<div class='col-12'><div class=\"card card-default\"><div class=\"card-header\"><div class='card-title text-bold text-primary text-lg'>第"+getBigNumber(bigProblemIdx++)+"大题&nbsp;&nbsp;&nbsp;简答题</div><div class=\"card-tools\"><button type=\"button\" class=\"btn btn-tool\" data-card-widget=\"collapse\"><i class=\"fas fa-minus\"></i></button></div></div><div class=\"card-body\"><div class='row'>";
                    $.each(problemInfoList,function (idx,itm) {//每道题
                        let answer = answers[problemIdx++];
                        shortListBody += "<div class=\"col-6\"><span class=\"text-cyan\">第"+(idx+1)+"题</span><span>"+itm.problem+"</span><br>";
                        shortListBody+="<span class=\"text-bold\">考生答案</span>&nbsp;&nbsp;&nbsp;<span>"+answer+"</span><br>";
                        shortListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+itm.answer+"</span><br>";
                        shortListBody+="<span class=\"text-bold\">自主打分(满分:"+problemScore+"分)</span>&nbsp;&nbsp;&nbsp;<input type='number' id='short"+(idx+1)+"' class='form-control' max='"+problemScore+"' min='0' style='display: inline;width: 100px;height: 25px'><br>";
                        shortListBody+="</div>";
                    });
                    shortListBody+="</div></div></div></div>";
                    markPlaceHtml +="<div class=\"row\" id=\"short\">"+shortListBody+"</div>";
                    break;
                }
                case "填空题":{
                    let problemInfoList = paperInfo["fillList"];
                    fillListBody+="<div class='col-12'><div class=\"card card-default\"><div class=\"card-header\"><div class='card-title text-bold text-primary text-lg'>第"+getBigNumber(bigProblemIdx++)+"大题&nbsp;&nbsp;&nbsp;填空题</div><div class=\"card-tools\"><button type=\"button\" class=\"btn btn-tool\" data-card-widget=\"collapse\"><i class=\"fas fa-minus\"></i></button></div></div><div class=\"card-body\"><div class='row'>";
                    $.each(problemInfoList,function (idx,itm) {//每道题
                        let answer = answers[problemIdx++];
                        fillListBody += "<div class=\"col-12\"><span class=\"text-cyan\">第"+(idx+1)+"题</span><br>";
                        fillListBody += "<span><span class='text-bold'>考生答案</span>:"+fillProblemCombineAnswer(itm.problem,itm.answer)+"</span><br>";
                        fillListBody += "<span><span class='text-bold'>参考答案</span>:"+fillProblemCombineAnswer(itm.problem,answer)+"</span><br>";
                        fillListBody+="<span class=\"text-bold\">自主打分(满分:"+problemScore+"分)</span>&nbsp;&nbsp;&nbsp;<input type='number' id='fill"+(idx+1)+"' class='form-control' max='"+problemScore+"' min='0' style='display: inline;width: 100px;height: 25px'><br>";
                        fillListBody+="</div>";
                    });
                    fillListBody+="</div></div></div></div>";
                    markPlaceHtml +="<div class=\"row\" id=\"fill\">"+fillListBody+"</div>";
                    break;
                }
                case "编程题":{
                    let problemInfoList = paperInfo["programList"];
                    programListBody+="<div class='col-12'><div class=\"card card-default\"><div class=\"card-header\"><div class='card-title text-bold text-primary text-lg'>第"+getBigNumber(bigProblemIdx++)+"大题&nbsp;&nbsp;&nbsp;编程题</div><div class=\"card-tools\"><button type=\"button\" class=\"btn btn-tool\" data-card-widget=\"collapse\"><i class=\"fas fa-minus\"></i></button></div></div><div class=\"card-body\"><div class='row'>";
                    $.each(problemInfoList,function (idx,itm) {//每道题
                        let answer = answers[problemIdx++];
                        let ioList = itm.ioList;
                        let an = [];
                        $.each(ioList,function (idx,itm) {
                            an.push(itm.output);
                        })
                        let score = 0;
                        for (let i = 0; i < answer.length && an.length; i++) {
                            let o = answer[i].replace("\t","").replace("\r","").replace("\n","");
                            let n = an[i].replace("\t","").replace("\r","").replace("\n","");
                            if(o == n){
                                score += problemScore/answer.length;
                            }
                        }
                        programListBody += "<div class=\"col-12\"><span class=\"text-cyan\">第"+(idx+1)+"题</span><br>";
                        programListBody+="<span class=\"text-bold\">考生答案</span>&nbsp;&nbsp;&nbsp;<span>"+answer.join("\t\t").toString()+"</span><br>";
                        programListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+an.join("\t\t").toString()+"</span><br>";
                        programListBody+="<span class=\"text-bold\">得分情况(满分:"+problemScore+"分)</span>&nbsp;&nbsp;&nbsp;<span class='text-primary'>"+(score)+"分</span><br>";
                        programListBody+="</div>";
                    });
                    programListBody+="</div></div></div></div>";
                    markPlaceHtml +="<div class=\"row\" id=\"program\">"+programListBody+"</div>";
                    break;
                }
            }
        }
        $("#markPlace").html(markPlaceHtml);
        $("#markModal").modal('show');
    })

    $("#markPaper_server").click(function () {
        let shortHasMarkAll = true;
        let shortTotalScore = 0;
        let fillHasMarkAll = true;
        let fillTotalScore = 0;
        let programHasMarkAll = true;
        let programTotalScore = 0;
        let shouldSubmit = true;
        for (let i = 1; true ; i++) {
            let $shortAnswerNode = $("#short"+i);
            if($shortAnswerNode.length == 0){
                break;
            }else{
                if($shortAnswerNode.val() == ""){
                    shortHasMarkAll = false;
                    shouldSubmit = false;
                    break;
                }else{
                    shortTotalScore += ($shortAnswerNode.val()-0);
                }
            }
        }
        for (let i = 1; true ; i++) {
            let $fillAnswerNode = $("#fill"+i);
            if($fillAnswerNode.length == 0){
                break;
            }else{
                if($fillAnswerNode.val() == ""){
                    shouldSubmit = false;
                    fillHasMarkAll = false;
                    break;
                }else{
                    fillTotalScore += ($fillAnswerNode.val()-0);
                }
            }
        }
        // for (let i = 1; true ; i++) {
        //     let $programAnswerNode = $("#program"+i);
        //     if($programAnswerNode.length == 0){
        //         break;
        //     }else{
        //         if($programAnswerNode.val() == ""){
        //             shouldSubmit = false;
        //             programHasMarkAll = false;
        //             break;
        //         }else{
        //             programTotalScore += ($programAnswerNode.val()-0);
        //         }
        //     }
        // }
        if(!shouldSubmit){
            $(document).Toasts('create', {
                class: 'bg-danger',
                autohide: true,
                title: '分数提交失败',
                body: "题目未打分",
                width: "200",
                delay: 5000
            })
        }else{
            $.ajax({
                url: "/teacher/submitScoreOfMark",
                data:{
                    fillTotalScore: fillTotalScore,
                    shortTotalScore: shortTotalScore,
                    // programTotalScore: programTotalScore,
                    paper_uuid: paper_uuid,
                    student_uuid:student_uuid,
                },
                success: function (data) {
                    if(data.code == 200){
                        $(document).Toasts('create', {
                            class: 'bg-success',
                            autohide: true,
                            title: '分数提交成功',
                            body: "分数提交成功",
                            width: "200",
                            delay: 3000
                        });
                        let $jsGrid = $("#jsGrid");
                        let idx = getJSGridIdx($jsGrid,studentPaperAnswer_uuid);
                        let data = getJSGridData($jsGrid,studentPaperAnswer_uuid);
                        data.fillScore = fillTotalScore;
                        data.shortScore = shortTotalScore;
                        data.programScore = programTotalScore;
                        data.complete = true;
                        data.totalScore = data.fillScore + data.singleChoiceScore + data.multipleChoiceScore + data.shortScore + data.judgeScore + data.programScore;
                        data.pass = data.totalScore > 0.6 * data.paperTotalScore;
                        editJsJridData($jsGrid,studentPaperAnswer_uuid,data);
                        $("#markModal").modal('hide');
                    }
                }
            })
        }
    })
})
function getProblemScoreConfig(paperStrategy){
    let configs = [];
    let problemStrategyList = JSON.parse(paperStrategy.problemStrategyList);
    $.each(problemStrategyList,function (idx,item) {
        let problemType = item.problemType;
        switch (problemType) {
            case "单选题":{
                configs.push({
                    name: "singleChoiceScore",
                    title: "单选题分数",
                    type: "text",
                    width: 25,
                    css: "overHidden",
                    height: 80,
                    align: "center",
                    filtering: true
                })
                break;
            }
            case "多选题":{
                configs.push({
                    name: "multipleChoiceScore",
                    title: "多选题分数",
                    type: "text",
                    width: 25,
                    css: "overHidden",
                    height: 80,
                    align: "center",
                    filtering: true
                })
                break;
            }
            case "填空题":{
                configs.push({
                    name: "fillScore",
                    title: "填空题分数",
                    type: "text",
                    width: 25,
                    css: "overHidden",
                    height: 80,
                    align: "center",
                    filtering: true
                })
                break;
            }
            case "简答题":{
                configs.push({
                    name: "shortScore",
                    title: "简答题分数",
                    type: "text",
                    width: 25,
                    css: "overHidden",
                    height: 80,
                    align: "center",
                    filtering: true
                })
                break;
            }
            case "判断题":{
                configs.push({
                    name: "judgeScore",
                    title: "判断题分数",
                    type: "text",
                    width: 25,
                    css: "overHidden",
                    height: 80,
                    align: "center",
                    filtering: true
                })
                break;
            }
            case "编程题":{
                configs.push({
                    name: "programScore",
                    title: "编程题分数",
                    type: "text",
                    width: 25,
                    css: "overHidden",
                    height: 80,
                    align: "center",
                    filtering: true
                })
                break;
            }
        }
    })
    configs.push("");
    configs.push("");
    configs.push("");
    configs.push("");
    configs.push("");
    return configs;
}
function getBigNumber(idx) {
    if(idx == 1) return "一"
    if(idx == 2) return "二"
    if(idx == 3) return "三"
    if(idx == 4) return "四"
    if(idx == 5) return "五"
    if(idx == 6) return "六"
}
function fillProblemCombineAnswer(problem,answer) {
    let idx = 0;
    while (problem.includes("#{}")){
        let ans = answer[idx++];
        problem = problem.replace("#{}","<input type='text'  class='form-control' disabled style='height: 30px;display: inline;width: "+(ans.length*16+26)+"px' value='"+ans+"'>");
    }
    return problem;
}