$(function () {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 1500
    });
    const knowledgeArrayList = getKnowledgeArrayListFromServer();
    let saveData = null;
    $("#search1").attr("placeholder","题目搜索");
    $("#search2").attr("placeholder","知识点搜索");
    $("#search_button").click(function () {
        let search_problem = $("#search1").val()+""
        let search_knowledgeList = $("#search2").val()+""

        let $jsGrid = $("#jsGrid");
        let data = null
        if(saveData == null){
            data = $jsGrid.data().JSGrid.data;
            saveData = data;
        }else{
            data = saveData;
        }
        let newData = []
        for(let i=0;i<data.length;++i){
            let tag1 = search_problem == "" ? true : data[i].problem.includes(search_problem)
            let tag2 = search_knowledgeList == "" ? true : hasArrayIncludes(data[i].knowledgeList,search_knowledgeList)
            // console.log("search_problem:" +search_problem)
            // console.log("search_knowledgeList:" + search_knowledgeList)
            // console.log(tag1)
            // console.log(tag2)
            // console.log("----------------------------")
            if(tag1 && tag2){
                newData.push(data[i])
            }
        }
        $jsGrid.data().JSGrid.data = newData;
        $jsGrid.jsGrid("refresh");

    })
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
            width: 20,
            height: 80,
            align: "center",
            css: "overHidden",
            filtering: true
        },{
            name: "useNum",
            title: "参与组卷次数",
            type: "text",
            width: 5,
            height: 80,
            align: "center",
            filtering: true
        }/*,{
            name: "correctRate",
            title: "正确率",
            type: "text",
            width: 5,
            height: 80,
            align: "center",
            filtering: true
        }*/,{
            name: "difficultyVal",
            title: "难度值",
            type: "text",
            width: 5,
            height: 80,
            align: "center",
            filtering: true
        },{
            name: "knowledgeList",
            title: "考察知识点",
            type: "text",
            width: 25,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value,item) {
                let knowledgeList = "";
                $.each(item.knowledgeList,function (idx,val) {
                    if((idx+1) == item.knowledgeList.length){
                        knowledgeList=knowledgeList.concat(val)
                    }else{
                        knowledgeList=knowledgeList.concat(val+"$ ")
                    }
                });
                return knowledgeList;
            }
        },{
            title: "<button class='jsgrid-button jsgrid-insert-button' id='openInsertModal'></button>",
            width: 20,
            align: "center",
            sorting: false,
            itemTemplate: function (value, item) {
                let d = JSON.stringify(item);
                return '<span class="autoChangeCode" data=\'' + d + '\'><i class="fa fa-repeat" aria-hidden="true"></i></span>&nbsp;&nbsp;' +
                    '<button class="jsgrid-button jsgrid-edit-button openEditModal" data=\'' + d + '\'></button>' +
                    '<button class="jsgrid-button jsgrid-delete-button openDeleteConfirm" data=\'' + d + '\'></button>';
            }

        }]
    });

    /*edit for problem*/
    $(document).on("click",".openEditModal",function () {
        console.log("启动编辑...");
        let data = JSON.parse($(this).attr("data"));
        reInitEditClasses();
        //给模态框赋初值
        $("#edit_uuid").val(data.uuid);
        $("#edit_problem").val(data.problem);
        $("#edit_allNum").val(data.allNum);
        $("#edit_useNum").val(data.useNum);
        $("#edit_correctNum").val(data.correctNum);
        $("#edit_correctRate").val(data.correctRate);
        $("#edit_analysis").val(data.analysis);
        $("#edit_difficultyVal").val(data.difficultyVal);
        $("#edit_videoPath").html('<label for="edit_videoPath">解析视频</label><br>')
        if(data.videoPath == undefined || data.videoPath == "" || data.videoPath.trim() == ""){
            //<a id="" target="_blank" href="">播放解析视频</a>
            $("#edit_videoPath").append($('<span style="color: red">无解析视频</span>'))
        }else{
            $("#edit_videoPath").append($('<a target="_blank" href="'+ data.videoPath+'">播放解析视频</a>'))
        }
        $("#edit_knowledgeList").html("");
        $("#edit_newVideoPath").val("")
        $.each(knowledgeArrayList,function (idx,itm) {
            $.each(itm.knowledgeList,function (idx,itm){
                if(data.knowledgeList.includes(itm)){
                    $("#edit_knowledgeList").append('<option selected="selected" value="'+itm+'">'+ itm +'</option>');
                }else{
                    $("#edit_knowledgeList").append('<option value="'+itm+'">'+ itm +'</option>');
                }
            })
        })
        //设置ioList
        for (let i = 1; i <= 10; i++) {
            $("#edit_i_"+i).val("");
            $("#edit_o_"+i).val("");
        }
        let ioList = data.ioList;
        for (let i = 1; i <= ioList.length; i++) {
            $("#edit_i_"+i).val(ioList[i-1].input);
            $("#edit_o_"+i).val(ioList[i-1].output);
        }
        $("#edit_knowledgeList").select2({
            placeholder:"请选择考察知识点"
        });
        //  $("#edit_knowledgeList").bootstrapDualListbox({
        //     filterTextClear: "",
        //     infoText: "选中option共 {0} 项",
        //     infoTextEmpty: "空",
        //     moveSelectedLabel: '添加选中的知识点'
        // })
        $("#editModal").modal('show');
    })
    /*editProblem_server for problem to server*/
    $("#editProblem_server").click(function () {
        //check the valid of data of problem info
        if(!checkProblemInfo()){
            // check false
            $(document).Toasts('create', {
                class: 'bg-danger',
                autohide: true,
                title: '提交失败原因',
                body: "题目信息不正确",
                width: "200"
            })
        }else{
            let problemData = getEditProblemData()
            let formData = new FormData();
            formData.append("program",JSON.stringify(problemData))
            if($("#edit_newVideoPath")[0].files[0] != undefined){
                formData.append("file",$("#edit_newVideoPath")[0].files[0])
            }

            $.ajax({
                url: "/teacher/updateProgramInfo",
                type: "post",
                processData: false,
                contentType: false,
                data: formData,
                success: function (data) {
                    if(data.code == 200){
                        $(document).Toasts('create', {
                            class: 'bg-success',
                            autohide: true,
                            title: '修改状态',
                            body: "修改成功",
                            width: "200"
                        })
                        //reInit jsGrid Table of knowledgeArray List...
                        $("#edit").modal("hide")
                        problemData.useNum = $("#edit_useNum").val()
                        problemData.allNum = $("#edit_allNum").val()
                        problemData.correctNum = $("#edit_correctNum").val()
                        problemData.correctRate = $("#edit_correctRate").val()
                        problemData.videoPath = data.data;
                        problemData.knowledgeList =  problemData.knowledgeList;
                        editJsJridData($("#jsGrid"),$("#edit_uuid").val(),problemData)
                    }else{
                        $(document).Toasts('create', {
                            class: 'bg-danger',
                            autohide: true,
                            title: '修改失败',
                            body: data.msg,
                            width: "200"
                        })
                    }

                }
            })
        }
        $("#editModal").modal('hide');
    })


    /*delete the problem when click the openDeleteConfirm button*/
    $(document).on("click",".openDeleteConfirm",function () {
        if(confirm("确认删除此题吗?")){
            let data = JSON.parse($(this).attr("data"));
            let uuid = data.uuid;
            $.ajax({
                url: "/teacher/deleteProgramByUuid",
                data: {data:uuid},
                type: "post",
                success: function (data) {
                    if(data.code == 200 || data.code == "200"){
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

    $("#openInsertModal").click(function () {
        let $insert_knowledgeList = $("#insert_knowledgeList")
        $.each(knowledgeArrayList,function (idx,itm) {
            $.each(itm.knowledgeList,function (idx,itm){
                $insert_knowledgeList.append('<option value="'+itm+'">'+ itm +'</option>');
            })
        })
        $insert_knowledgeList.select2({
            placeholder:"请选择考察知识点"
        });
        $("#insertModal").modal("show");
    })
    $("#openInsertByExcelModal").click(function () {
        $("#insertByExcelModal").modal("show");
    })
    $("#insert_submitExcel").click(function () {
        let file = $("#insertByExcel")[0].files[0]
        let formData = new FormData()
        formData.append("excelFile",file)
        if(file != undefined){
            $.ajax({
                url: "/teacher/insertProgramByExcel",
                data: formData,
                type: "post",
                processData: false,
                contentType: false,
                success: function (data) {
                    if(data.code == 200){
                        $("#insertByExcelModal").modal("hide");
                        $(document).Toasts('create', {
                            class: 'bg-success',
                            autohide: true,
                            title: '操作状况',
                            body: data.msg+"\n"+data.data,
                            width: "200",
                            delay: 2000
                        })
                        window.location.reload()
                    }
                }
            })

        }
    })
    $("#saveProblem_server").click(function () {
        if($("#insert_problem").val().trim() != ""){
            let formData = getInsertProblemData()
            $.ajax({
                url:"/teacher/insertProgram",
                type:"post",
                processData: false,
                contentType: false,
                data: formData
            }).done(function (data) {
                $("#insertModal").modal('hide')
                if(data.code == 200){
                    $(document).Toasts('create', {
                        class: 'bg-success',
                        autohide: true,
                        title: '操作状况',
                        body: "添加成功",
                        width: "200",
                        delay: 3000
                    })
                    formData.forEach((v,k)=>{
                        if(k == "program"){
                            let json = JSON.parse(v);
                            json.allNum = 0
                            json.correctNum = 0
                            json.correctRate = 0
                            json.useNum = 0
                            json.uuid = data.data
                            // json.videoPath = data.data[1]
                            insertJsJridData($("#jsGrid"),JSON.stringify(json))
                        }
                    })

                }else{
                    $(document).Toasts('create', {
                        class: 'bg-danger',
                        autohide: true,
                        title: '操作状况',
                        body: data.msg,
                        width: "200",
                        delay: 3000
                    })
                }

            })
        }

        $("#insertModal").modal("show")
    })

})
function reInitEditClasses() {
    $("#edit_problem").attr("class","form-control");
    $("#edit_difficultyVal").attr("class","form-control");
}
function appendAnswerOption(choice,$select,option) {
    console.log($(choice))
    if($(choice).val().trim() != ""){
        $select.append('<option value="'+ option +'">'+ option +'</option>');
    }
}

/*get KnowledgeArrayList from server to order to complete the edit of problem's knowledgeArray*/
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
/*check data of problem info*/
function checkProblemInfo() {
    let tag = true;
    //check problem
    if($("#edit_problem").val().trim() == ""){
        tag = false;
        $("#edit_problem").attr("class","form-control is-invalid");
    }else{
        $("#edit_problem").attr("class","form-control is-valid");
    }
    return tag;
}
/*get has edit complete of problem, to order to submit to server*/
function getEditProblemData() {
    let problem = {};
    problem.uuid = $("#edit_uuid").val();
    problem.problem = $("#edit_problem").val();
    problem.analysis = $("#edit_analysis").val()
    problem.difficultyVal = $("#edit_difficultyVal").val()
    problem.knowledgeList = $("#edit_knowledgeList").val()
    problem.videoPath = $("#edit_videoPath").children('a').attr("href")
    let ioList = [];
    for (let i = 1; i <= 10; i++) {
        let op = {};
        op.input = $("#edit_i_"+i).val();
        op.output = $("#edit_o_"+i).val();
        if(op.input.trim() != "" || op.output.trim() != ""){
            ioList.push(op);
        }
    }
    problem.ioList = ioList;
    //解析视频 还没弄
    return problem;
}
/*check the array has includes the str*/
function hasArrayIncludes(array,str) {
    for(let i=0;i<array.length;++i){
        if(array[i].includes(str)) return true;
    }
    return false;
}
/*get insert problem data*/
function getInsertProblemData() {
    let formData = new FormData()
    let problemData = {}
    problemData['problem'] = $("#insert_problem").val()
    problemData['answer'] = $("#insert_answer").val()
    let ioList = [];
    for (let i = 1; i <= 10; i++) {
        let op = {};
        op.input = $("#insert_i_"+i).val();
        op.output = $("#insert_o_"+i).val();
        if(op.input.trim() != "" && op.output.trim() != ""){
            ioList.push(op);
        }
    }
    problemData["ioList"] = JSON.stringify(ioList);
    problemData['analysis'] = $("#insert_analysis").val();
    problemData['knowledgeList'] = JSON.stringify($("#insert_knowledgeList").val())
    problemData['difficultyVal'] = $("#insert_difficultyVal").val()
    let video = $("#insert_videoPath")[0].files[0]
    if(video != undefined)
        formData.append('video',video);
    formData.append("program",JSON.stringify(problemData));
    return formData;
}
