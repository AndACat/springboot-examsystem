$(function () {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 1500
    });
    let $search1 = $("#search1");
    $search1.parent().html("<button class=\"btn btn-success\" id=\"createRandomPaper\">生成试卷</button>&nbsp;&nbsp;&nbsp;&nbsp;<button class=\"btn btn-success\" id=\"saveRandomPaper\">保存试卷</button>");


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
        if($(this).prop('checked')){
            $(this).bootstrapSwitch("state",true);
        }else{
            $(this).bootstrapSwitch("state",false);
        }
    });


    $.ajax({
        url: "/teacher/selectMyAllCourse",
        type: "post",
        success: function (data) {
            let $course_uuid = $("#course_uuid");
            $.each(data,function (idx,item) {
                $course_uuid.append('<option  value="'+item.uuid+'">'+item.cla.claName+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+item.courseName+'</option>');
            })
        }
    });

    $('#reservationtime').daterangepicker({
        timePicker: true,
        timePickerIncrement: 10,
        minYear: moment().format("YYYY"),
        startDate: moment().startOf('day'),
        minDate: moment(),
        autoApply: true,
        timePicker24Hour: true,
        locale: {
            format: 'MM/DD/YYYY hh:mm A'
        }
    })

    $.ajax({
        url: "/teacher/selectMyAllPaperStrategy",
        type: "post",
        success: function (data) {
            let $paperStrategy_uuid = $("#paperStrategy_uuid");
            $paperStrategy_uuid.append('<option shouldRemove selected>请选择策略</option>');
            $.each(data,function (idx,item) {
                $paperStrategy_uuid.append('<option value=\''+JSON.stringify(item)+'\'>'+item.paperStrategyName+'</option>');
            })
        }
    })
    let knowledgeArrayList;
    let knowledgeArrayListSelectNode = '<select class="select2" multiple="multiple" style="width: 100%;">';
    $.ajax({
        url: "/teacher/selectAllKnowledgeArray",
        type: "post",
        success: function (data) {
            knowledgeArrayList = data;
            $.each(data,function (idx,item) {
                $.each(item.knowledgeList,function (id,itm) {
                    knowledgeArrayListSelectNode += '<option value="'+itm+'">'+itm+'</option>'
                })
            })
            knowledgeArrayListSelectNode += '</select>';
        }
    })

    $("#paperStrategy_uuid").change(function () {
        $(this).find('option[shouldRemove]').remove();
        let problemStrategyData = JSON.parse($(this).val());
        problemStrategyData.problemStrategyList = JSON.parse(problemStrategyData.problemStrategyList);
        let $paperStrategy = $("#paperStrategy");
        $paperStrategy.html("");
        $paperStrategy.append(getProblemStrategyNode(problemStrategyData,knowledgeArrayListSelectNode));
        $(".select2").select2({
            placeholder:"请选择考察知识点"
        });
    })
    //初始化工作结束

    //监听进度条
    let $minDifficultyVal = $("#minDifficultyVal");
    let $maxDifficultyVal = $("#maxDifficultyVal");
    let $minAdaptationDegree = $("#minAdaptationDegree");
    let $maxEvolutionNum = $("#maxEvolutionNum");
    let $populationInitMaxRunCount = $("#populationInitMaxRunCount");
    let $mutationRate = $("#mutationRate");
    let $tournamentSize = $("#tournamentSize");
    let $populationSize = $("#populationSize");

    let $minDifficultyVal_display = $("#minDifficultyVal_display");
    let $maxDifficultyVal_display = $("#maxDifficultyVal_display");
    let $minAdaptationDegree_display = $("#minAdaptationDegree_display");
    let $maxEvolutionNum_display = $("#maxEvolutionNum_display");
    let $populationInitMaxRunCount_display = $("#populationInitMaxRunCount_display");
    let $mutationRate_display = $("#mutationRate_display");
    let $tournamentSize_display = $("#tournamentSize_display");
    let $populationSize_display = $("#populationSize_display");
    $minDifficultyVal.on("input change propertychange",function () {
        let minDifficultyVal = $(this).val();
        $minDifficultyVal_display.text(minDifficultyVal/10);
        let maxDifficultyVal = $maxDifficultyVal.val();
        if(minDifficultyVal == "100"){
            $maxDifficultyVal.val(minDifficultyVal).trigger("change");
            return;
        }
        if(minDifficultyVal > maxDifficultyVal){
            $maxDifficultyVal.val(minDifficultyVal).trigger("change");
        }
    })
    $maxDifficultyVal.on("input change propertychange",function () {
        let maxDifficultyVal = $(this).val();
        $maxDifficultyVal_display.text(maxDifficultyVal/10);
        let minDifficultyVal = $minDifficultyVal.val();
        if(maxDifficultyVal == "100") return;
        if(maxDifficultyVal < minDifficultyVal){
            $minDifficultyVal.val(maxDifficultyVal).trigger("change");
        }
    })
    $minAdaptationDegree.on("input change propertychange",function () {
        let minAdaptationDegree = $(this).val();
        $minAdaptationDegree_display.text(minAdaptationDegree+"%");
    })
    $maxEvolutionNum.on("input change propertychange",function () {
        let maxEvolutionNum = $(this).val();
        $maxEvolutionNum_display.text(maxEvolutionNum+"次");
    })
    $populationInitMaxRunCount.on("input change propertychange",function () {
        $populationInitMaxRunCount_display.text($(this).val()+"次")
    })
    $mutationRate.on("input change propertychange",function () {
        $mutationRate_display.text($(this).val()/1000+"%")
    })
    $tournamentSize.on("input change propertychange",function () {
        $tournamentSize_display.text($(this).val()+"个")
    })
    $populationSize_display.on("input change propertychange",function () {
        $populationSize_display.text($(this).val()+"个")
    })

    //提交试卷信息
    $(document).on("click","#createRandomPaper",function () {
        let paperStrategy_notJson = $("#paperStrategy_uuid").val();
        if(paperStrategy_notJson == "请选择策略"){
            $(document).Toasts('create', {
                class: 'bg-danger',
                autohide: true,
                title: '提交失败原因',
                body: "未选择策略",
                width: "200"
            })
            return;
        }
        let paperStrategy_json = JSON.parse(paperStrategy_notJson);
        let paperStrategy_uuid = paperStrategy_json.uuid;
        let knowledgeMap = {};
        let problemStrategyList = JSON.parse(paperStrategy_json.problemStrategyList);
        let $select = $("select[multiple]");
        let minDifficultyVal = $minDifficultyVal.val()/10;//float 0-10
        let maxDifficultyVal = $maxDifficultyVal.val()/10;//float 0-10
        let minAdaptationDegree = $minAdaptationDegree.val()/100;//0%-100%
        let populationInitMaxRunCount = $populationInitMaxRunCount.val();//int
        let maxEvolutionNum = $maxEvolutionNum.val();
        let populationSize = $populationSize.val();
        let mutationRate = $mutationRate.val()/100000;//float
        let elitism = $("#elitism").attr("state") == "true" ? true : false;//boolean
        let tournamentSize = $tournamentSize.val();//int
        let copulation = $("#copulation").attr("state") == "true" ? true : false;//boolean
        $.each(problemStrategyList,function (idx,item) {
            let problemType = item.problemType;
            let problem_knowledge_array = $($select[idx]).val();
            knowledgeMap[problemType] = problem_knowledge_array;
        })
        $.ajax({
            url: "/teacher/inheritanceAlgorithmPaper",
            type: "post",
            data:{
                minDifficultyVal:minDifficultyVal,
                maxDifficultyVal:maxDifficultyVal,
                populationSize: populationSize,
                min_adaptationDegree:minAdaptationDegree,
                populationInitMaxRunCount:populationInitMaxRunCount,
                mutationRate:mutationRate,
                elitism:elitism,
                tournamentSize:tournamentSize,
                copulation:copulation,
                paperStrategy_uuid: paperStrategy_uuid,
                maxEvolutionNum:maxEvolutionNum,
                knowledgeMap: JSON.stringify(knowledgeMap)
            },
            success: function (data) {
                if(data.code == 403){
                    $(document).Toasts('create', {
                        class: 'bg-danger',
                        autohide: false,
                        title: '提交失败原因',
                        body: data.msg,
                        width: "200"
                    })
                }
                if(data.code == 200){
                    let paper = data.data;
                    let paperStrategy = JSON.parse($("#paperStrategy_uuid").val());
                    appendPaperInfo(paper,JSON.parse(paperStrategy.problemStrategyList));
                    $(document).Toasts('create', {
                        class: 'bg-success',
                        autohide: true,
                        title: '操作状态',
                        body: data.msg,
                        width: "200",
                        delay:10000
                    })
                }
            }
        })
    })

    //保存试卷
    $(document).on("click","#saveRandomPaper",function () {
        let course_uuid = $("#course_uuid").val().trim();
        let during = $("#during").val();
        let sort = $("#sort").attr("state") == "true" ? true : false;
        let openFaceIdentity = $("#openFaceIdentity").attr("state") == "true" ? true : false;
        let split = $("#reservationtime").val().split("-");
        let begin_time = new Date(split[0]).getTime();
        let end_time = new Date(split[1]).getTime();
        let paperName = $("#paperName").val();
        let visible = $("#visible").attr("state") == "true" ? true : false;
        let tag = true;
        if(during == ""){
            $(document).Toasts('create', {
                class: 'bg-danger',
                autohide: true,
                title: '提交失败原因',
                body: "未填写考试时长",
                width: "200"
            })
            tag = false;
        }
        if(paperName == ""){
            $(document).Toasts('create', {
                class: 'bg-danger',
                autohide: true,
                title: '提交失败原因',
                body: "未填写试卷名称",
                width: "200"
            })
            tag = false;
        }
        if(tag){
            $.ajax({
                url: "/teacher/saveInheritanceAlgorithmPaper",
                type: "post",
                data:{
                    course_uuid: course_uuid,
                    paperName: paperName,
                    during: during,
                    sort: sort,
                    begin_time: begin_time,
                    end_time: end_time,
                    openFaceIdentity: openFaceIdentity,
                    visible: visible
                },
                success: function (data) {
                    if(data.code == 403){
                        $(document).Toasts('create', {
                            class: 'bg-danger',
                            autohide: true,
                            title: '操作状态',
                            body:"保存失败,试卷已保存或试卷未生成",
                            width: "200"
                        })
                    }
                    if(data.code == 200){
                        $(document).Toasts('create', {
                            class: 'bg-success',
                            autohide: true,
                            title: '操作状态',
                            body:"保存成功",
                            width: "200"
                        })
                    }
                }
            })
        }
    })
})

function getProblemStrategyNode(problemStrategyData,knowledgeArrayListSelectNode) {
    let body = "";
    $.each(problemStrategyData.problemStrategyList,function (idx,item) {
        body+='<div class="row"><div class="col-4"><span class="red">第'+getBigNumber(idx+1)+'题：&nbsp;&nbsp;&nbsp;</span>' +
            '<span>'+item.problemType+'&nbsp;&nbsp;&nbsp;</span>' +
            '<span>'+item.problemNum+'</span><span>道&nbsp;&nbsp;&nbsp;</span>' +
            '<span>'+item.problemAllScore+'</span><span>分</span></div><div class="col-8">' +
            knowledgeArrayListSelectNode+"</div></div><hr>";
    })
    let node = '<div class="col-12">' +
        '                    <div class="card card-default">' +
        '                        <div class="card-header">' +
        '                           <h3 class="card-title"><span class="red">总&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;分:&nbsp;&nbsp;&nbsp;&nbsp;</span><span>'+problemStrategyData.allScore+'</span></h3><span class="red">策略名：</span><span>'+problemStrategyData.paperStrategyName+'</span>' +
        '                           <div class="card-tools">' +
        '                               <button type="button" class="btn btn-tool" data-card-widget="collapse"><i class="fas fa-minus"></i></button>' +
        '                               <button type="button" class="btn btn-tool" data-card-widget="remove"><i class="fas fa-times"></i></button>' +
        '                           </div>' +
        '                           <span style="display: none" uuid="'+ problemStrategyData.uuid +'" data=\''+JSON.stringify(problemStrategyData)+'\'></span>' +
        '                        </div>' +
        '                        <div class="card-body">' + body +
        '                        </div>' +
        '                    </div>' +
        '                </div>'
    return node;
}
function getBigNumber(idx) {
    if(idx == 1) return "一"
    if(idx == 2) return "二"
    if(idx == 3) return "三"
    if(idx == 4) return "四"
    if(idx == 5) return "五"
    if(idx == 6) return "六"
}

function appendPaperInfo(paper,problemStrategyList){
    let problemIdx = 1;
    let singleChoiceListBody = "";
    let multipleChoiceListBody = "";
    let judgeListBody = "";
    let fillListBody = "";
    let shortListBody = "";
    let programListBody = "";
    // $.each(paper,function (problemType,problemList) {
    //     switch (problemType) {
    //         case "singleChoiceList":{
    //
    //         }
    //         case "multipleChoiceList":{
    //
    //         }
    //         case "judgeList":{
    //
    //         }
    //         case "fillList":{
    //
    //         }
    //         case "shortList":{
    //
    //         }
    //     }
    // })
    let paperHtml = '<div class="card card-default">' +
        '               <div class="card-header">' +
        '                   <h3 class="card-title">随机试卷</h3>\n' +
        '                   <div class="card-tools">\n' +
        '                       <button type="button" class="btn btn-tool" data-card-widget="collapse"><i class="fas fa-minus"></i></button>\n' +
        '                       <button type="button" class="btn btn-tool" data-card-widget="remove"><i class="fas fa-times"></i></button>\n' +
        '                   </div>' +
        '               </div>' +
        '            <div class="card-body">';

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
                fillListBody+="<div class=\"col-12 text-bold text-primary text-lg\"><span>第"+getBigNumber(problemIdx++)+"大题&nbsp;&nbsp;&nbsp;填空题</span></div>";
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
                    programListBody+="</div>";
                })
                paperHtml += "<div class=\"row\" id=\"program\">"+programListBody+"</div>";
                break;
            }
        }
    })
    $("#paper").html(paperHtml);
}
