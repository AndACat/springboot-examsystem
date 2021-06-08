$(function () {
    /*init for Swal*/
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 1500
    });
    let courseListData;
    let claListData;
    $("#search").attr("placeholder","搜索");
    $.ajax({
        url:"/teacher/selectMyCollegeAllCla",
        method:"post",
        async: false
    }).done(function (data) {
        claListData = data;
    });
    /*init insertUser insert_modal college and profession options*/
    let $insert_cla_uuid = $("#insert_cla_uuid")
    $.each(claListData,function (idx,item) {
        $insert_cla_uuid.append('<option value="'+item.uuid+'">'+ item.claName+'</option>')
    })
    $("#jsGrid").jsGrid({
        height: "650",
        width: "100%",
        sorting: true,
        paging: true,
        pageIndex: 1,
        pageSize: 15,
        pageButtonCount: 5,
        // pageLoading: true,
        autoload: true,
        pagerFormat: "页数: {first} {prev} {pages} {next} {last}    第 {pageIndex} 页,共 {pageCount} 页",
        pagePrevText: "←",
        pageNextText: "→",
        pageFirstText: "第一页",
        pageLastText: "最后一页",
        confirmDeleting: true,
        deleteConfirm: "确认需要删除数据？",
        noDataContent: "没有数据...",
        loadMessage: "正在加载数据，请稍候......",
        dataType: "json",
        controller: {
            loadData: function (filter) {
                $.ajax({
                    url: "/teacher/selectMyAllCourse",
                    dataType: "json",
                    type:"post",
                    data: filter,
                    async: false,
                    success:function (data) {
                        courseListData = data;
                    }
                });
                return courseListData;
            }
        },


        fields: [
            {
                name: "courseName",
                title:"课程名",
                type: "text",
                width: 20,
                height: 80,
                align: "center",
                filtering: true
            },
            {
                title: "班级名",
                type: "text",
                width: 20,
                align: "center",
                itemTemplate: function (idx,item) {
                    return item.cla.claName;
                }
            },
            {
                title: "学生数量",
                type: "text",
                width: 20,
                align: "center",
                itemTemplate: function (idx,item) {
                    return item.studentList.length;
                }
            },{
                title: "<input class='jsgrid-button jsgrid-insert-button' id='insertCourse' type='button'>",
                width: 20,
                align: "center",
                sorting: false,
                itemTemplate: function (value,item) {
                    let d = JSON.stringify(item);
                    return '<button class="jsgrid-button jsgrid-edit-button edit" data='+d+'></button>'+
                        '<button class="jsgrid-button jsgrid-delete-button delete" data='+d+'></button>';
                }

            }
        ]

    });

    $(document).on("click",".edit",function () {
        let data = $(this).attr("data");
        $("#edit_submitCourseInfo").attr("data",data);
        let jsonData = JSON.parse(data);
        let $edit_studentList = $("#edit_studentList");
        $edit_studentList.html("");
        $.each(jsonData.studentList,function (idx,item) {
            $edit_studentList.append('<option selected="selected" value=\''+JSON.stringify(item)+'\'>' + item.name + "&emsp;&emsp;&emsp;" + item.sno + '</option>')
        })
        $("#edit_submitClaInfo").attr("data",data);//放置当前编辑状态的整体数据

        $.ajax({
            url: "/teacher/selectNoSelectedStudentOfCourseOfCla",
            async: false,
            data:{cla_uuid:jsonData.cla_uuid,course_uuid:jsonData.uuid},
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
                    nonSelectedListLabel: '未加入课程学生列表',
                    selectedListLabel: '已选择学生列表',
                });
                $edit_studentList.bootstrapDualListbox('refresh');
                $("#bootstrap-duallistbox-nonselected-list_").attr("style","height:450px");
                $("#bootstrap-duallistbox-selected-list_").attr("style","height:450px");
                $("#edit_Modal").modal('show')
            }
        })
        $("#edit_Modal").modal('show')
    })

    $(document).on("click",".delete",function () {
        console.log("启动删除...");
        let d = JSON.parse($(this).attr("data"));
        if(confirm("确认删除吗?")){
            $.ajax({
                url:"/admin/deleteCourseByUuid",
                data:{uuid:d.uuid},
                type: "post",
                success: function (data) {
                    if(data.code == 200){
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

    $("#edit_submitCourseInfo").click(function () {
        let studentList_notJson = $("#edit_studentList").val();
        let student_list = [];//学生的集合 所有选中的
        $.each(studentList_notJson,function (idx,item) {
            student_list.push(JSON.parse(item));
        })
        let data = $("#edit_submitCourseInfo").attr("data");
        let jsonData = JSON.parse(data);//原先课程的信息
        let uuids = [];//要提交的uuid 信息
        $.each(student_list,function (idx,item) {
            uuids.push(item.uuid);
        })
        $.ajax({
            url: "/teacher/updateCourseInfo",
            data:{student_uuid_list:JSON.stringify(uuids),uuid:jsonData.uuid},
            type: "post",
            success: function (data) {
                if(data.code == 200){
                    jsonData.studentList = student_list;
                    editJsJridData($("#jsGrid"),jsonData.uuid,jsonData);
                    /*提示框*/
                    Toast.fire({
                        type: 'success',
                        title: "更新成功"
                    })
                    $(".modal").modal('hide')

                }
            }
        })
    })
    $(document).on("click","#insertCourse",function () {
        $("#insert_Modal").modal('show')
    })

    /*when insert user button has click*/
    $("#insert_submitCourseInfo").click(function () {
        let d = getInsertCourseData();
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
            url: "/teacher/insertCourse",
            type: "post",
            data: {course:JSON.stringify(d)},
        }).done(function (data) {
            d.student_uuid_list = [];
            d.studentList = [];
            d.uuid = data.data;
            let cla = {};
            cla.uuid = d.cla_uuid;
            cla.claName = $("#insert_cla_uuid").find("option:selected").text();
            d.cla = cla;
            if(data.code == 200){
                insertJsJridData($("#jsGrid"),d)
                $(document).Toasts('create', {
                    class: 'bg-success',
                    title: '操作成功',
                    body: "课程保存成功",
                    width: "200"
                })
                $("#insert_Modal").modal('hide');
            }else{
                $(document).Toasts('create', {
                    class: 'bg-danger',
                    title: '操作失败',
                    body: "用户保存失败,请刷新页面",
                    width: "200"
                })
            }
        })
    })

    $("#search").bind("input propertychange",function () {
        console.log("change")
        $("#search_button").click();
    })
    $("#search_button").click(function () {
        let searchProblem = $("#search").val()
        let newData = [];
        if(searchProblem.trim().length == 0){
            refreshJSGridData($("#jsGrid"),courseListData)
            return;
        }
        $.each(courseListData,function (idx,item) {
            if(item.courseName.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.cla.claName.includes(searchProblem)){
                newData.push(item);
                return true;
            }
        })
        refreshJSGridData($("#jsGrid"),newData);
    })
});
function getInsertCourseData() {
    if(checkInsertAll() == false) return null;
    let course = {};
    course.courseName = $("#insert_courseName").val();
    course.cla_uuid = $("#insert_cla_uuid").val();
    return course;
}
function checkInsertAll() {
    let tag = true;
    if($("#insert_courseName").val().trim() == ""){
        $(document).Toasts('create', {
            class: 'bg-danger',
            title: '操作失败',
            body: "请填写课程名",
            width: "200"
        })
        tag = false;
    }
    if($("#insert_cla_uuid").val().trim() == ""){
        $(document).Toasts('create', {
            class: 'bg-danger',
            title: '操作失败',
            body: "请选择班级",
            width: "200"
        })
        tag = false;
    }
    return tag;
}





