$(function () {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 1500
    });
    $("input[data-bootstrap-switch]").each(function () {
        $(this).bootstrapSwitch({
            onSwitchChange: function () {
                if ($(this).attr("state") == "false") {
                    $(this).attr("state", "true");
                } else {
                    $(this).attr("state", "false");
                }
            }
        });
        if ($(this).prop('checked')) {
            $(this).bootstrapSwitch("state", true);
        } else {
            $(this).bootstrapSwitch("state", false);
        }
    });


    $.ajax({
        url: "/teacher/selectMyAllCourse",
        type: "post",
        success: function (data) {
            let $course_uuid = $("#course_uuid");
            $.each(data, function (idx, item) {
                $course_uuid.append('<option  value="' + item.uuid + '">' + item.cla.claName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + item.courseName + '</option>');
            })
        }
    })

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
    });

    let hasSelectedProblemTypeArray = [];//已添加进试卷的题目类型
    let currentProblemNum = 1;
    $("#createPaper").click(function () {
        let $problemTypeOption = $("#problemTypeSelect option:selected");
        let hasSelectedProblemTypeVal = $problemTypeOption.val();
        let hasSelectedProblemTypeText = $problemTypeOption.text();
        if(hasSelectedProblemTypeArray.includes(hasSelectedProblemTypeVal)){
            $(document).Toasts('create', {
                class: 'bg-danger',
                autohide: true,
                title: '创建失败',
                body: "无法重复创建题型",
                width: "200"
            })
        }else{//
            hasSelectedProblemTypeArray.push(hasSelectedProblemTypeVal);
            let cardContainer = '<div class="card card-default list-group-item nested-1">' +
                '          <div class="card-header">' +
                '            <h3 class="card-title">'+hasSelectedProblemTypeText+'</h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
                '            <button class="btn btn-sm btn-primary" choseProblem="'+hasSelectedProblemTypeVal+'">选择题目</button>' +
                '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="form-control" style="width: 10%;display: inline" type="number" placeholder="分数" problemType="'+hasSelectedProblemTypeVal+'">' +
                '            <div class="card-tools">' +
                '              <button type="button" class="btn btn-tool" data-card-widget="collapse"><i class="fa fa-minus"></i></button>' +
                '              <button type="button" class="btn btn-tool handle"><i class="fa fa-arrows-alt handle"></i></button>' +
                '              <button type="button" class="btn btn-tool removeProblemType" data-card-widget="remove" data="'+hasSelectedProblemTypeVal+'"><i class="fa fa-times"></i></button>' +
                '            </div>' +
                '          </div>' +
                '          <div class="card-body row" id="info_'+hasSelectedProblemTypeVal+'" style="display: flex!important;">';

            cardContainer += '</div></div>';
            let $paperInfo = $("#paperInfo");
            $paperInfo.append(cardContainer)
            new Sortable($paperInfo[0],{
                group:"nested",
                animation:150,//排序时间，毫秒
                handle:".handle"
            });

        }
    })

    $(document).on("click",".removeProblemType",function () {
        let hasSelectedProblemTypeVal = $(this).attr("data");
        hasSelectedProblemTypeArray.splice(hasSelectedProblemTypeArray.indexOf(hasSelectedProblemTypeVal),1);
        hasModalInit.splice($.inArray(hasSelectedProblemTypeVal,hasModalInit),1);
    })
    let hasModalInit = [];
    $(document).on("click","button[choseProblem]",function () {
        let problemType = $(this).attr("choseProblem");
        if(!hasModalInit.includes(problemType)){
            hasModalInit.push(problemType);
            let sel = "#jsGrid_"+problemType;
            switch (problemType) {
                case "SingleChoice":{
                    $(sel).jsGrid({
                        height: "450",
                        width: "100%",
                        sorting: true,
                        paging: true,
                        pageIndex: 1,
                        pageSize: 13,
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
                                    url: "/teacher/selectMyAll"+problemType,
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
                            name: "problem",
                            title: "题目",
                            type: "text",
                            width: 20,
                            height: 80,
                            align: "center",
                            css: "overHidden",
                            filtering: true
                        },{
                            name: "choice_a",
                            title: "选项A",
                            type: "text",
                            width: 20,
                            height: 80,
                            css: "overHidden",
                            align: "center",
                            filtering: true
                        },{
                            name: "choice_b",
                            title: "选项B",
                            type: "text",
                            width: 20,
                            height: 80,
                            css: "overHidden",
                            align: "center",
                            filtering: true
                        },{
                            name: "choice_c",
                            title: "选项C",
                            type: "text",
                            width: 20,
                            height: 80,
                            align: "center",
                            css: "overHidden",
                            filtering: true
                        },{
                            name: "choice_d",
                            title: "选项D",
                            type: "text",
                            width: 20,
                            height: 80,
                            align: "center",
                            css: "overHidden",
                            filtering: true
                        },{
                            name: "answer",
                            title: "答案",
                            width: 5,
                            height: 80,
                            align: "center",
                            filtering: true,
                            itemTemplate: function (value,item) {
                                return item.answer;
                            }
                        },{
                            title: "",
                            width: 20,
                            align: "center",
                            sorting: false,
                            itemTemplate: function (value, item) {
                                let d = JSON.stringify(item);
                                return "<button class='jsgrid-button jsgrid-insert-button addSingleChoice' data='"+d+"'></button>";
                            }

                        }]
                    });
                    break;
                }
                case "MultipleChoice":{
                    $(sel).jsGrid({
                        height: "450",
                        width: "100%",
                        sorting: true,
                        paging: true,
                        pageIndex: 1,
                        pageSize: 13,
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
                                    url: "/teacher/selectMyAllMultipleChoice",
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
                            name: "problem",
                            title: "题目",
                            type: "text",
                            width: 20,
                            height: 80,
                            align: "center",
                            css: "overHidden",
                            filtering: true
                        },{
                            name: "choice_a",
                            title: "选项A",
                            type: "text",
                            width: 20,
                            height: 80,
                            css: "overHidden",
                            align: "center",
                            filtering: true
                        },{
                            name: "choice_b",
                            title: "选项B",
                            type: "text",
                            width: 20,
                            height: 80,
                            css: "overHidden",
                            align: "center",
                            filtering: true
                        },{
                            name: "choice_c",
                            title: "选项C",
                            type: "text",
                            width: 20,
                            height: 80,
                            align: "center",
                            css: "overHidden",
                            filtering: true
                        },{
                            name: "choice_d",
                            title: "选项D",
                            type: "text",
                            width: 20,
                            height: 80,
                            align: "center",
                            css: "overHidden",
                            filtering: true
                        },{
                            name: "answer",
                            title: "答案",
                            width: 5,
                            height: 80,
                            align: "center",
                            filtering: true,
                            itemTemplate: function (value,item) {
                                let answers = "";
                                $.each(item.answer,function (idx,val) {
                                    if(idx+1 == item.answer.length){
                                        answers += item.answer[idx]
                                    }else{
                                        answers += item.answer[idx]+","
                                    }

                                })
                                return answers;
                            }
                        },{
                            title: "",
                            width: 20,
                            align: "center",
                            sorting: false,
                            itemTemplate: function (value, item) {
                                let d = JSON.stringify(item);
                                return "<button class='jsgrid-button jsgrid-insert-button addMultipleChoice' data='"+d+"'></button>";
                            }

                        }]
                    });
                    break;
                }
                case "Fill":{
                    $("#jsGrid_Fill").jsGrid({
                        height: "650",
                        width: "100",
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
                                    url: "/teacher/selectMyAllFill",
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
                            name: "problem",
                            title: "题目",
                            type: "text",
                            width: 40,
                            height: 80,
                            align: "center",
                            css: "overHidden",
                            filtering: true
                        },{
                            name: "answer",
                            title: "答案",
                            type: "text",
                            width: 5,
                            height: 80,
                            align: "center",
                            css: "overHidden",
                            filtering: true,
                            itemTemplate:function (idx,item) {
                                return item.answer.join("$").toString();
                            }
                        },{
                            title: "",
                            width: 20,
                            align: "center",
                            sorting: false,
                            itemTemplate: function (value, item) {
                                let d = JSON.stringify(item);
                                return "<button class='jsgrid-button jsgrid-insert-button addFill' data='"+d+"'></button>";
                            }

                        }]
                    });
                    break;
                }
                case "Short":{
                    $(sel).jsGrid({
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
                                    url: "/teacher/selectMyAllShort",
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
                            name: "problem",
                            title: "题目",
                            type: "text",
                            width: 40,
                            height: 80,
                            align: "center",
                            filtering: true,
                            css: "overHidden"
                        },{
                            name: "answer",
                            title: "答案",
                            type: "text",
                            width: 10,
                            height: 80,
                            align: "center",
                            filtering: true,
                            css: "overHidden"
                        },{
                            title: "",
                            width: 20,
                            align: "center",
                            sorting: false,
                            itemTemplate: function (value, item) {
                                let d = JSON.stringify(item);
                                return "<button class='jsgrid-button jsgrid-insert-button addShort' data='"+d+"'></button>";
                            }

                        }]
                    });
                    break;
                }
                case "Judge":{
                    $(sel).jsGrid({
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
                                    url: "/teacher/selectMyAllJudge",
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
                            name: "problem",
                            title: "题目",
                            type: "text",
                            width: 40,
                            height: 80,
                            align: "center",
                            filtering: true,
                            css: "overHidden"
                        },{
                            name: "answer",
                            title: "选项A",
                            type: "text",
                            width: 10,
                            height: 80,
                            align: "center",
                            filtering: true,
                            itemTemplate: function (value,item) {
                                return item.answer?"对":"错";
                            }
                        },{
                            title: "",
                            width: 20,
                            align: "center",
                            sorting: false,
                            itemTemplate: function (value, item) {
                                let d = JSON.stringify(item);
                                return "<button class='jsgrid-button jsgrid-insert-button addJudge' data='"+d+"'></button>";
                            }

                        }]
                    });
                    break;
                }
                case "Program":{
                    $(sel).jsGrid({
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
                                    url: "/teacher/selectMyAllProgram",
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
                            name: "problem",
                            title: "题目",
                            type: "text",
                            width: 80,
                            height: 80,
                            align: "center",
                            css: "overHidden",
                            filtering: true
                        },{
                            title: "",
                            width: 20,
                            align: "center",
                            sorting: false,
                            itemTemplate: function (value, item) {
                                let d = JSON.stringify(item);
                                return "<button class='jsgrid-button jsgrid-insert-button addProgram' data='"+d+"'></button>";
                            }
                        }]
                    });
                    break;
                }
            }

        }
        $("#modal_"+problemType).modal('show');
    })
    $(document).on("click",".addSingleChoice",function () {
        let data = JSON.parse($(this).attr("data"));
        addSingleChoice(data);
    })
    $(document).on("click",".addMultipleChoice",function () {
        let data = JSON.parse($(this).attr("data"));
        addMultipleChoice(data);
    })
    $(document).on("click",".addFill",function () {
        let data = JSON.parse($(this).attr("data"));
        addFill(data);
    })
    $(document).on("click",".addShort",function () {
        let data = JSON.parse($(this).attr("data"));
        addShort(data);
    })
    $(document).on("click",".addJudge",function () {
        let data = JSON.parse($(this).attr("data"));
        addJudge(data);
    })
    $(document).on("click",".addProgram",function () {
        let data = JSON.parse($(this).attr("data"));
        addProgram(data);
    })
    $(document).on("click",".remove_SingleChoice",function () {
        let uuid = $(this).attr("uuid");
        let pos = singleChoice_uuid_list.indexOf(uuid);
        singleChoice_uuid_list.splice(pos,1);
        $(this).parent().remove();
    })
    $(document).on("click",".remove_MultipleChoice",function () {
        let uuid = $(this).attr("uuid");
        let pos = multipleChoice_uuid_list.indexOf(uuid);
        multipleChoice_uuid_list.splice(pos,1);
        $(this).parent().remove();
    })
    $(document).on("click",".remove_Fill",function () {
        let uuid = $(this).attr("uuid");
        let pos = fill_uuid_list.indexOf(uuid);
        fill_uuid_list.splice(pos,1);
        $(this).parent().remove();
    })
    $(document).on("click",".remove_Short",function () {
        let uuid = $(this).attr("uuid");
        let pos = short_uuid_list.indexOf(uuid);
        short_uuid_list.splice(pos,1);
        $(this).parent().remove();
    })
    $(document).on("click",".remove_Judge",function () {
        let uuid = $(this).attr("uuid");
        let pos = judge_uuid_list.indexOf(uuid);
        judge_uuid_list.splice(pos,1);
        $(this).parent().remove();
    })
    $(document).on("click",".remove_Program",function () {
        let uuid = $(this).attr("uuid");
        let pos = program_uuid_list.indexOf(uuid);
        program_uuid_list.splice(pos,1);
        $(this).parent().remove();
    })


    //保存试卷
    $("#savePaper").click(function () {
        let singleChoice_uuid_list = [];
        let multipleChoice_uuid_list = [];
        let fill_uuid_list = [];
        let short_uuid_list = [];
        let judge_uuid_list = [];
        let program_uuid_list = [];
        $.each($("button[class='btn remove_SingleChoice']"),function (idx,item) {
            singleChoice_uuid_list.push($(this).attr("uuid"));
        })
        $.each($("button[class='btn remove_MultipleChoice']"),function (idx,item) {
            multipleChoice_uuid_list.push($(this).attr("uuid"));
        })
        $.each($("button[class='btn remove_Fill']"),function (idx,item) {
            fill_uuid_list.push($(this).attr("uuid"));
        })
        $.each($("button[class='btn remove_Short']"),function (idx,item) {
            short_uuid_list.push($(this).attr("uuid"));
        })
        $.each($("button[class='btn remove_Judge']"),function (idx,item) {
            judge_uuid_list.push($(this).attr("uuid"));
        })
        $.each($("button[class='btn remove_Program']"),function (idx,item) {
            program_uuid_list.push($(this).attr("uuid"));
        })
        let course_uuid = $("#course_uuid").val().trim();
        let during = $("#during").val();
        let sort = $("#sort").attr("state") == "true" ? true : false;
        let openFaceIdentity = $("#openFaceIdentity").attr("state") == "true" ? true : false;
        let split = $("#reservationtime").val().split("-");
        let begin_time = new Date(split[0]).getTime();
        let end_time = new Date(split[1]).getTime();
        let paperName = $("#paperName").val();
        let needRoom = $("#needRoom").attr("state") == "true" ? true : false;
        let visible = $("#visible").attr("state") == "true" ? true : false;
        let tag = true;
        let singleChoiceScore = 0;
        let multipleChoiceScore = 0;
        let fillScore = 0;
        let judgeScore = 0;
        let programScore = 0;
        let shortScore = 0;
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
        $.each($("input[problemType]"),function (idx,item) {
            let problemType = $(this).attr("problemType");
            let score = $(this).val();
            if(score == null || score == 0 || score.trim() == ""){
                $(document).Toasts('create', {
                    class: 'bg-danger',
                    autohide: true,
                    title: '提交失败原因',
                    body: "未填写分数",
                    width: "200"
                })
                tag = false;
            }
            if(problemType == "SingleChoice") singleChoiceScore = score;
            else if(problemType == "MultipleChoice") multipleChoiceScore = score;
            else if(problemType == "Fill") fillScore = score;
            else if(problemType == "Judge") judgeScore = score;
            else if(problemType == "Program") programScore = score;
            else if(problemType == "Short") shortScore = score;
        })
        if(tag){
            $.ajax({
                url: "/teacher/saveFixationPaper",
                type: "post",
                data:{
                    course_uuid: course_uuid,
                    paperName: paperName,
                    during: during,
                    sort:sort,
                    openFaceIdentity: openFaceIdentity,
                    begin_time: begin_time,
                    end_time: end_time,
                    needRoom: needRoom,
                    visible: visible,
                    singleChoice_uuid_list:JSON.stringify(singleChoice_uuid_list),
                    multipleChoice_uuid_list:JSON.stringify(multipleChoice_uuid_list),
                    fill_uuid_list:JSON.stringify(fill_uuid_list),
                    judge_uuid_list:JSON.stringify(judge_uuid_list),
                    short_uuid_list:JSON.stringify(short_uuid_list),
                    program_uuid_list:JSON.stringify(program_uuid_list),
                    singleChoiceScore:singleChoiceScore,
                    multipleChoiceScore,multipleChoiceScore,
                    fillScore:fillScore,
                    judgeScore:judgeScore,
                    shortScore:shortScore,
                    programScore:programScore
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
        console.log(singleChoice_uuid_list)
        console.log(multipleChoice_uuid_list)
        console.log(fill_uuid_list)
        console.log(short_uuid_list)
        console.log(judge_uuid_list)
        console.log(program_uuid_list)
    })
})
let singleChoice_uuid_list = [];
let multipleChoice_uuid_list = [];
let fill_uuid_list = [];
let short_uuid_list = [];
let judge_uuid_list = [];
let program_uuid_list = [];
function addSingleChoice(itm) {
    if(singleChoice_uuid_list.includes(itm.uuid)){
        $(document).Toasts('create', {
            class: 'bg-danger',
            autohide: true,
            title: '添加状态',
            body: "无法重复添加题目",
            width: "200"
        })
        return;
    }
    singleChoice_uuid_list.push(itm.uuid);
    $(document).Toasts('create', {
        class: 'bg-success',
        autohide: true,
        title: '添加状态',
        body: "添加成功",
        width: "200"
    })
    let singleChoiceListBody = "";
    singleChoiceListBody += "<div class=\"col-6 nested-1\" style='border: ridge'><span>"+itm.problem+"</span>" +
        "<button type=\"button\" class=\"btn remove_SingleChoice\" uuid='"+itm.uuid+"' style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-times\"></i></button>" +
        "<button type=\"button\" class=\"btn \" style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-arrows-alt handle_SingleChoice\"></i></button><br>";
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
    $("#info_SingleChoice").append(singleChoiceListBody);
    new Sortable($("#info_SingleChoice")[0],{
        group:"nested-SingleChoice",
        animation:150,//排序时间，毫秒
        handle:".handle_SingleChoice"
    });
    console.log(itm);
}
function addMultipleChoice(itm) {
    if(multipleChoice_uuid_list.includes(itm.uuid)){
        $(document).Toasts('create', {
            class: 'bg-danger',
            autohide: true,
            title: '添加状态',
            body: "无法重复添加题目",
            width: "200"
        })
        return;
    }
    multipleChoice_uuid_list.push(itm.uuid);
    $(document).Toasts('create', {
        class: 'bg-success',
        autohide: true,
        title: '添加状态',
        body: "添加成功",
        width: "200"
    })
    let singleChoiceListBody = "";
    // singleChoiceListBody += "<div class=\"col-6 nested-1\" style='border: ridge'><span>"+itm.problem+"</span><button type=\"button\" class=\"btn \" style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-arrows-alt handle_MultipleChoice\"></i></button><br>";
    singleChoiceListBody += "<div class=\"col-6 nested-1\" style='border: ridge'><span>"+itm.problem+"</span>" +
        "<button type=\"button\" class=\"btn remove_MultipleChoice\" uuid='"+itm.uuid+"' style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-times\"></i></button>" +
        "<button type=\"button\" class=\"btn \" style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-arrows-alt handle_MultipleChoice\"></i></button><br>";
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
    $("#info_MultipleChoice").append(singleChoiceListBody);
    new Sortable($("#info_MultipleChoice")[0],{
        group:"nested-MultipleChoice",
        animation:150,//排序时间，毫秒
        handle:".handle_MultipleChoice"
    });
    console.log(itm);
}
function addFill(itm) {
    if(fill_uuid_list.includes(itm.uuid)){
        $(document).Toasts('create', {
            class: 'bg-danger',
            autohide: true,
            title: '添加状态',
            body: "无法重复添加题目",
            width: "200"
        })
        return;
    }
    fill_uuid_list.push(itm.uuid);
    $(document).Toasts('create', {
        class: 'bg-success',
        autohide: true,
        title: '添加状态',
        body: "添加成功",
        width: "200"
    })
    let fillListBody = "";
    fillListBody += "<div class=\"col-6 nested-1\" style='border: ridge'><span>"+itm.problem+"</span>" +
        "<button type=\"button\" class=\"btn remove_Fill\" uuid='"+itm.uuid+"' style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-times\"></i></button>" +
        "<button type=\"button\" class=\"btn \" style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-arrows-alt handle_Fill\"></i></button><br>";
    fillListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+itm.answer+"</span><br>";
    fillListBody+="</div>";
    $("#info_Fill").append(fillListBody);
    new Sortable($("#info_Fill")[0],{
        group:"nested-Fill",
        animation:150,//排序时间，毫秒
        handle:".handle_Fill"
    });
    console.log(itm);
}
function addShort(itm) {
    if(short_uuid_list.includes(itm.uuid)){
        $(document).Toasts('create', {
            class: 'bg-danger',
            autohide: true,
            title: '添加状态',
            body: "无法重复添加题目",
            width: "200"
        })
        return;
    }
    short_uuid_list.push(itm.uuid);
    $(document).Toasts('create', {
        class: 'bg-success',
        autohide: true,
        title: '添加状态',
        body: "添加成功",
        width: "200"
    })
    let fillListBody = "";
    fillListBody += "<div class=\"col-6 nested-1\" style='border: ridge'><span>"+itm.problem+"</span>" +
        "<button type=\"button\" class=\"btn remove_Short\" uuid='"+itm.uuid+"' style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-times\"></i></button>" +
        "<button type=\"button\" class=\"btn \" style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-arrows-alt handle_Short\"></i></button><br>";
    fillListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+itm.answer+"</span><br>";
    fillListBody+="</div>";
    $("#info_Short").append(fillListBody);
    new Sortable($("#info_Short")[0],{
        group:"nested-Short",
        animation:150,//排序时间，毫秒
        handle:".handle_Short"
    });
    console.log(itm);
}
function addJudge(itm) {
    if(judge_uuid_list.includes(itm.uuid)){
        $(document).Toasts('create', {
            class: 'bg-danger',
            autohide: true,
            title: '添加状态',
            body: "无法重复添加题目",
            width: "200"
        })
        return;
    }
    judge_uuid_list.push(itm.uuid);
    $(document).Toasts('create', {
        class: 'bg-success',
        autohide: true,
        title: '添加状态',
        body: "添加成功",
        width: "200"
    })
    let fillListBody = "";
    fillListBody += "<div class=\"col-6 nested-1\" style='border: ridge'><span>"+itm.problem+"</span>" +
        "<button type=\"button\" class=\"btn remove_Judge\" uuid='"+itm.uuid+"' style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-times\"></i></button>" +
        "<button type=\"button\" class=\"btn \" style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-arrows-alt handle_Judge\"></i></button><br>";
    fillListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+(itm.answer ? "对" : "错")+"</span><br>";
    fillListBody+="</div>";
    $("#info_Judge").append(fillListBody);
    new Sortable($("#info_Judge")[0],{
        group:"nested-Judge",
        animation:150,//排序时间，毫秒
        handle:".handle_Judge"
    });
    console.log(itm);
}
function addProgram(itm) {
    if(program_uuid_list.includes(itm.uuid)){
        $(document).Toasts('create', {
            class: 'bg-danger',
            autohide: true,
            title: '添加状态',
            body: "无法重复添加题目",
            width: "200"
        })
        return;
    }
    program_uuid_list.push(itm.uuid);
    $(document).Toasts('create', {
        class: 'bg-success',
        autohide: true,
        title: '添加状态',
        body: "添加成功",
        width: "200"
    });
    let fillListBody = "";
    fillListBody += "<div class=\"col-6 nested-1\" style='border: ridge'><span>"+itm.problem+"</span>" +
        "<button type=\"button\" class=\"btn remove_Program\" uuid='"+itm.uuid+"' style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-times\"></i></button>" +
        "<button type=\"button\" class=\"btn \" style=\"float: right;color: #adb5bd;\"><i class=\"fa fa-arrows-alt handle_Program\"></i></button><br>";
    fillListBody+="<span class=\"text-bold\">参考答案</span>&nbsp;&nbsp;&nbsp;<span>"+itm.answer+"</span><br>";
    fillListBody+="</div>";
    $("#info_Program").append(fillListBody);
    new Sortable($("#info_Program")[0],{
        group:"nested-Program",
        animation:150,//排序时间，毫秒
        handle:".handle_Program"
    });
    console.log(itm);
}
function getBigNumber(idx) {
    if(idx == 1) return "一"
    if(idx == 2) return "二"
    if(idx == 3) return "三"
    if(idx == 4) return "四"
    if(idx == 5) return "五"
    if(idx == 6) return "六"
}