$(function () {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 1500
    });
    $("#search").attr("placeholder","搜索");
    let claListData;
    //加载学院列表
    let collegeList;
    $.ajax({
        url:"/selectAllCollege",
        method:"post",
        async: false
    }).done(function (data) {
        collegeList = data;
    });
    //加载manager用户的学院
    let managerCollege;
    $.ajax({
        url:"/manager/getManagerCollege",
        method:"post",
        async: false
    }).done(function (data) {
        managerCollege = data;
    });
    let $insert_college = $("#insert_college");
    let $insert_profession = $("#insert_profession");
    $.each(collegeList,function (idx,item) {
        if(item.name == managerCollege){
            $insert_college.append("<option>"+managerCollege+"</option>");
            $.each(item.professionList,function (id,itm) {
                $insert_profession.append('<option value="'+itm+'">'+ itm +'</option>')
            })
            return;
        }
    })
    //jsGrid初始化
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
                    url: "/manager/selectMyCollegeAllCla",
                    dataType: "json",
                    type: "post",
                    data: filter,
                    async: false,
                    success: function (data) {
                        localData = data;
                        claListData = data;
                    }
                });
                return localData;
            }
        },
        fields:[{
            name: "claName",
            title: "班级名",
            type: "text",
            width: 20,
            height: 80,
            align: "center",
            css: "overHidden",
            filtering: true
        },{
            name: "profession",
            title: "所属专业",
            type: "text",
            width: 20,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true
        },{
            title: "学生数量",
            type: "text",
            width: 20,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value,item) {
                return item.studentList.length;
            }
        },{
            title: "开设课程",
            type: "text",
            width: 20,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value,item) {
                if(item.courseList.length == 0){
                    return "无开设课程";
                }
                let body = null;
                $.each(item.courseList,function (idx,val) {
                    body+='<option>'+val.courseName+'</option>';
                })
                let select = '<select class="form-control">'+ body+'</select>';
                return select;
            }
        },{
            title: "<button class='jsgrid-button jsgrid-insert-button' id='openInsertModal'></button>",
            width: 20,
            align: "center",
            sorting: false,
            itemTemplate: function (value, item) {
                let d = JSON.stringify(item);
                return '<button class="jsgrid-button jsgrid-edit-button openEditModal" data=\'' + d + '\'></button>' +
                    '<button class="jsgrid-button jsgrid-delete-button openDeleteConfirm" data=\'' + d + '\'></button>';
            }

        }]
    });

    $(document).on("click",".openEditModal",function () {
        /*设置数据*/
        let data = $(this).attr("data");
        let jsonData = JSON.parse(data);
        $("#edit_uuid").val(data.uuid);
        let $edit_studentList = $("#edit_studentList");
        $edit_studentList.html("");
        $.each(jsonData.studentList,function (idx,item) {
            $edit_studentList.append('<option selected="selected" value=\''+JSON.stringify(item)+'\'>' + item.name + "&emsp;&emsp;&emsp;" + item.sno + '</option>')
        })
        $("#edit_submitClaInfo").attr("data",data);//放置当前编辑状态的整体数据
        // if($edit_studentList.prev().length != 0){
        //     $edit_studentList.destroy();
        //     $edit_studentList.attr("style","");
        // }
        $.ajax({
            url: "/manager/selectAllNoClaStudentByProfession",
            async: false,
            data:{profession:jsonData.profession},
            type: "post",
            success: function (data) {
                $.each(data,function (idx,item) {
                    $edit_studentList.append('<option value=\''+JSON.stringify(item)+'\'>' + item.name + "&emsp;&emsp;&emsp;" + item.sno + '</option>')
                })
                //edit_studentList
                $edit_studentList.bootstrapDualListbox({
                    filterTextClear: "",
                    infoText: "选中option共 {0} 项",
                    infoTextEmpty: "空",
                    moveSelectedLabel: '添加选中的学生',
                    nonSelectedListLabel: '未加入班级学生列表',
                    selectedListLabel: '已选择学生列表',
                });
                $edit_studentList.bootstrapDualListbox('refresh');
                $("#bootstrap-duallistbox-nonselected-list_").attr("style","height:450px");
                $("#bootstrap-duallistbox-selected-list_").attr("style","height:450px");
                $("#edit_Modal").modal('show')
            }
        })

    })

    $(document).on("click",".openDeleteConfirm",function () {
        let d = JSON.parse($(this).attr("data"));
        if(confirm("确认删除吗?")){
            $.ajax({
                url:"/manager/deleteClaByUuid",
                data:{uuid:d.uuid},
                type: "post",
                success: function (data) {
                    if(data.code == 200 || data.code == "200"){
                        /*提示框*/
                        Toast.fire({
                            type: 'success',
                            title: '删除成功'
                        })
                        let $jsGrid = $("#jsGrid");
                        deleteJsJridData($jsGrid,getJSGridIdx($jsGrid,d.uuid))
                    }else{
                        Toast.fire({
                            type: 'error',
                            title: '删除失败'
                        })
                    }

                },
                error: function () {
                    Toast.fire({
                        type: 'error',
                        title: '删除失败'
                    })
                }
            })
        }
    })

    $("#edit_submitClaInfo").click(function () {
        let studentList_notJson = $("#edit_studentList").val();
        let student_list = [];//学生的集合 所有选中的
        $.each(studentList_notJson,function (idx,item) {
            student_list.push(JSON.parse(item));
        })
        let data = $("#edit_submitClaInfo").attr("data");
        let jsonData = JSON.parse(data);//原先班级的信息
        let uuids = [];//要提交的uuid 信息
        $.each(student_list,function (idx,item) {
            uuids.push(item.uuid);
        })
        let cla = {};
        cla.student_uuid_list = uuids;
        cla.uuid = jsonData.uuid;
        $.ajax({
            url: "/manager/updateClaInfoByUuid",
            data:{cla:JSON.stringify(cla)},
            type: "post",
            success: function (data) {
                if(data.code == 200){
                    jsonData.studentList = student_list;
                    // $("#jsGrid").jsGrid($("input[value = ]"),jsonData);
                    editJsJridData($("#jsGrid"),jsonData.uuid,jsonData);
                    /*提示框*/
                    Toast.fire({
                        type: 'success',
                        title: "更新成功"
                    })
                    $(".modal").modal('hide');
                    // window.location.reload();
                }
            }
        })
    })

    //添加班级
    $("#openInsertModal").click(function () {
        $("#insert_claName").val("");
        $("#insert_Modal").modal('show');
    })

    //添加班级 上传服务器
    /*when insert user button has click*/
    $("#insert_submitTeacherInfo").click(function () {
        let d = getInsertClaData();
        if(d == null){
            $(document).Toasts('create', {
                class: 'bg-danger',
                title: '操作失败',
                body: "信息不合格",
                width: "200"
            })
            return
        }
        $.ajax({
            url: "/manager/insertCla",
            type: "post",
            data: {cla:d},
        }).done(function (data) {
            let dd = JSON.parse(d);
            dd.uuid = data.data;
            dd.student_uuid_list = [];
            dd.studentList = [];
            dd.course_uuid_list = [];
            dd.courseList = [];
            if(data.code == 200){
                insertJsJridData($("#jsGrid"),dd)
                $(document).Toasts('create', {
                    class: 'bg-success',
                    title: '操作成功',
                    body: "保存成功",
                    width: "200",
                    autohide: true
                })
                $("#insert_Modal").modal('hide');

            }else{
                $(document).Toasts('create', {
                    class: 'bg-danger',
                    title: '操作失败',
                    body: "保存失败,请刷新页面重试",
                    width: "200"
                })
            }
        })
    })
    $("#search").bind("input propertychange",function () {
        $("#search_button").click();
    })
    $("#search_button").click(function () {
        let searchProblem = $("#search").val()
        let newData = [];
        if(searchProblem.trim().length == 0){
            refreshJSGridData($("#jsGrid"),claListData)
            return;
        }
        $.each(claListData,function (idx,item) {
            if(item.claName.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.college.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.profession.includes(searchProblem)){
                newData.push(item);
                return true;
            }
        })
        refreshJSGridData($("#jsGrid"),newData);
    })

})
function getInsertClaData() {
    if(checkInsertAll() == false) return null;
    let cla = {};
    cla.claName = $("#insert_claName").val();
    cla.profession = $("#insert_profession").val();
    return JSON.stringify(cla);
}
function checkInsertAll() {
    let tag = true;
    if($("#insert_claName").val().trim() == ""){
        $(document).Toasts('create', {
            class: 'bg-danger',
            title: '操作失败',
            body: "请填写班级名",
            width: "200"
        })
        tag = false;
    }
    if($("#insert_profession").val().trim() == ""){
        $(document).Toasts('create', {
            class: 'bg-danger',
            title: '操作失败',
            body: "请选择专业名",
            width: "200"
        })
        tag = false;
    }
    return tag;
}