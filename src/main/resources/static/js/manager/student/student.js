$(function () {
    /*init for Swal*/
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 1500
    });
    let studentListData;
    $("#search").attr("placeholder","搜索");
    /*init for bsCustomFileInput*/
    bsCustomFileInput.init();
    /*init for inputmask*/
    $('#edit_birthday').inputmask('yyyy/mm/dd', { 'placeholder': 'yyyy/mm/dd' })
    $("#insert_birthday").inputmask('yyyy/mm/dd', { 'placeholder': 'yyyy/mm/dd' })


    let collegeList;
    $.ajax({
        url:"/selectAllCollege",
        method:"post",
        async: false
    }).done(function (data) {
        collegeList = data;
    });
    let managerCollege;
    $.ajax({
        url:"/manager/getManagerCollege",
        method:"post",
        async: false
    }).done(function (data) {
        managerCollege = data;
    });
    /*init insertUser insert_modal college and profession options*/
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
                    url: "/manager/selectMyCollegeAllStudent",
                    dataType: "json",
                    type: "post",
                    data: filter,
                    async: false,
                    success:function (data) {
                        studentListData = data;
                    }
                });
                return studentListData;
            }
        },


        fields: [
            {
                name: "name",
                title:"姓名",
                type: "text",
                width: 20,
                height: 80,
                align: "center",
                filtering: true
            },
            {
                name: "account",
                title: "账户",
                type: "text",
                width: 20,
                align: "center"
            },
            {
                name: "sno",
                title: "学号",
                type: "text",
                width: 20,
                align: "center"
            },{
                title: "班级",
                type: "text",
                width: 30,
                align: "center",
                itemTemplate: function (value,item) {
                    if(item.cla == null) return "暂未加入班级";
                    return item.cla.claName;
                }
            },{
                title: "学习课程",
                type: "text",
                width: 30,
                align: "center",
                itemTemplate: function (value,item) {
                    if(item.courseList == null || item.courseList.length == 0){
                        return "无学习课程";
                    }
                    let body = null;
                    $.each(item.courseList,function (idx,val) {
                        body+='<option>'+val.courseName+'</option>';
                    })
                    let select = '<select class="form-control">'+ body+'</select>';
                    return select;
                }
            },{
                name: "profession",
                title: "专业",
                type: "text",
                width: 20,
                align: "center"
            },{
                name: "enabled",
                title: "使用权限",
                type: "text",
                width: 20,
                align: "center",
                itemTemplate: function (value,item) {
                    if(item.enabled == true || item.enabled == "true")
                        return "正常使用";
                    if(item.enabled == false || item.enabled == "false")
                        return "锁定中";
                    return "状态未知";
                }
            },
            {
                title: "<input class='jsgrid-button jsgrid-insert-button' id='insertUser' type='button'> <input class='jsgrid-button jsgrid-insert-button' id='insertUserWithExcel' type='button'>",
                width: 20,
                align: "center",
                sorting: false,
                itemTemplate: function (value,item) {
                    let d = JSON.stringify(item);
                    return '<span class="changeStudentCode" data='+d+'><i class="fa fa-repeat" aria-hidden="true"></i></span>&nbsp;&nbsp;' +
                        '<button class="jsgrid-button jsgrid-edit-button edit" data='+d+'></button>'+
                        '<button class="jsgrid-button jsgrid-delete-button delete" data='+d+'></button>';
                }

            }
        ]

    });

    $(document).on("click",".edit",function () {
        console.log("启动编辑...");

        reInitEditClasses();

        /*设置数据*/
        let data = JSON.parse($(this).attr("data"));
        $("#edit_name").val(data.name);
        $("#edit_account").val(data.account);
        $("#edit_birthday").val(data.birthday);
        $("#edit_sex").val(data.sex+"");
        $("#edit_sno").val(data.sno);
        $("#edit_email").val(data.email);
        $("#edit_phone").val(data.phone);
        $("#edit_code").val(data.code);


        //根据EXIF得到的图片的Orientation值，对图片进行适当的旋转
        let $edit_faceImg = $("#edit_faceImg");
        $edit_faceImg.attr("src",data.faceImg);
        // let Orientation;
        // EXIF.getData(faceImg,function () {
        //     Orientation = EXIF.getTag(faceImg,"Orientation");
        // });
        // rotateImg(Orientation,faceImg);//对图片进行旋转

        $("#edit_imgDisplayParent").html('<span style="color:#000b16;">请选择新图片(可选项)</span><br/><span style="color:#bb0008;font-size: 300%">+</span>');

        let $edit_college = $("#edit_college");
        $edit_college.html("");
        $.each(collegeList,(idx,val) => {
            if(managerCollege == val.name){
                $edit_college.append('<option selected value="'+val.name+'">'+val.name+'</option>');
                $("#edit_profession").html("<option selected value='"+data.profession+"'>"+data.profession+"</option>");
                return
            }
        })

        $("#edit_enabled").val(data.enabled+"")

        //清空照片选择
        $("#edit_InputFile").val("");

        $("#edit_imgDisplay").attr("src","");

        $("#edit_Modal").modal('show')

    })

    $(document).on("click",".delete",function () {
        console.log("启动删除...");
        let d = JSON.parse($(this).attr("data"));
        if(confirm("确认删除吗?")){
            $.ajax({
                url:"/manager/deleteStudentByAccount/"+d.account,
                type: "post",
                success: function (data) {
                    if(data.code == 200){
                        /*提示框*/
                        Toast.fire({
                            type: 'success',
                            title: '操作状态',
                            body:"删除成功"
                        })
                        let $jsGrid = $("#jsGrid");
                        deleteJsJridData($jsGrid,getJSGridIdx($jsGrid,d.sno));
                    }else{
                        Toast.fire({
                            type: 'error',
                            title: '操作状态',
                            body:"删除失败"
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

    $("#submitStudentInfo").click(function () {
        if(!checkEditAll()){
            Toast.fire({
                type: 'error',
                title: '修改状态',
                body: "信息不规范,修改失败"
            })
            return;
        }
        let formData = new FormData();
        let d = {};
        if($("#edit_InputFile")[0].files != undefined){
            formData.append("faceImgFile", $("#edit_InputFile")[0].files[0]);
        }
        d.name = $("#edit_name").val();
        d.account = $("#edit_account").val();
        d.sex = $("#edit_sex").val();
        d.birthday = $("#edit_birthday").val();
        d.email = $("#edit_email").val();
        d.phone = $("#edit_phone").val();
        d.enabled = $("#edit_enabled").val();
        formData.append("student",JSON.stringify(d));
        $.ajax({
            url: "/manager/updateStudentInfo",
            processData: false,
            contentType: false,
            data:formData,
            type: "post",
            success: function (data) {
                if(data.code == 200){
                    /*提示框*/
                    Toast.fire({
                        type: 'success',
                        title: '操作状态',
                        body: "修改成功"
                    })
                    if(data.data == null || data.data == undefined){
                        d["faceImg"]=$("#edit_faceImg").attr("src");
                    }else{
                        d["faceImg"]=data.data;
                    }
                    d.sno = $("#edit_sno").val();
                    d.college = managerCollege;
                    d.profession = $("#edit_profession").val();
                    editJsJridData($("#jsGrid"),$("#edit_sno").val(),d);
                    $("#edit_Modal").modal('hide')
                }
            }
        })
    })

    //重设密码
    $(document).on("click",".changeStudentCode",function () {
    let data = JSON.parse($(this).attr("data"));
    if(confirm("确认重设密码吗？")){
        $.ajax({
            url:"/manager/changeStudentCode/"+data.account,
            type:"post",
            success:function (data) {
                if(data.code == 200){
                    $(document).Toasts('create', {
                        class: 'bg-success',
                        title: '请牢记新设置的密码',
                        body: data.data,
                        width: "200"
                    })
                }else{
                    Toast.fire({
                        type: 'error',
                        title: "密码重设失败"
                    })
                }

            }
        })
    }
})

    $("#insertUser").click(function () {
        $("#insert_imgDisplayParent").html('<span style="color:#000b16;">请选择新图片(可选项)</span><br/><span style="color:#bb0008;font-size: 300%">+</span>')
        $("#insert_Modal").modal('show')
    })


    /*when insert user button has click*/
    $("#insert_submitStudentInfo").click(function () {
        let formData = getInsertUserFormData();
        if(formData == null){
            $(document).Toasts('create', {
                class: 'bg-danger',
                title: '操作失败',
                body: "信息不合格",
                width: "200"
            })
            return
        }
        $.ajax({
            url: "/manager/insertStudent",
            type: "post",
            data: formData,
            processData: false,
            contentType: false
        }).done(function (data) {
            if(data.code == 200){
                console.log(data)
                let student = JSON.parse(formData.get("student")+"");
                student.uuid = data.data[0];
                student.faceImg = data.data[2];
                insertJsJridData($("#jsGrid"),student);
                $(document).Toasts('create', {
                    class: 'bg-success',
                    title: '操作状态',
                    body: "用户保存成功",
                    width: "200",
                    autohide: true
                })
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
    $("#insertUserWithExcel").click(function () {
        $("#insertByExcel_Modal").modal('show')
    })
    $("#insert_submitExcel").click(function () {
        let formData = new FormData();
        let excelFile = $("#insertByExcel")[0].files[0];
        formData.append("excelFile",excelFile == undefined ? null : excelFile);
        $.ajax({
            url:"/manager/insertStudentByExcel",
            type:"post",
            processData: false,
            contentType: false,
            data:formData
        }).done(function (data) {
            if(data.code == 200){
                $(document).Toasts('create', {
                    class: 'bg-success',
                    title: '操作信息',
                    body: "用户保存成功",
                    width: "200"
                })
                $("#insertByExcel_Modal").modal('hide')
                if(data.msg != null){
                    $(document).Toasts('create', {
                        class: 'bg-danger',
                        title: '操作状态',
                        body: data.msg,
                        width: "200"
                    })
                }
                if(confirm("是否刷新页面以加载数据？")){
                    window.location.reload();
                }
            }else{
                $(document).Toasts('create', {
                    class: 'bg-danger',
                    title: '操作信息',
                    body: "用户保存失败，请刷新页面",
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
            refreshJSGridData($("#jsGrid"),studentListData)
            return;
        }
        $.each(studentListData,function (idx,item) {
            if(item.name.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.account.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.college.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.claName.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.profession.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.birthday.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.sno.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.email.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            $.each(item.classAndCourseLists,function (k,v) {
                if(v.class_name.includes(searchProblem) ||v.course_name.includes(searchProblem)){
                    newData.push(item);
                    return true;
                }
            })
        })
        refreshJSGridData($("#jsGrid"),newData);
    })
});
/*when the edit imgFile has change, the method should be call, then reload the img Data to <img>#edit_imgDisPlay*/
function edit_custom_file_change() {
    let reader=new FileReader();
    let files = $("#edit_InputFile")[0].files;
    if(files != undefined || files != null){
        reader.readAsDataURL(files[0])
        reader.onload = function(e){
            if($("#edit_imgDisplay") != undefined){
                $("#edit_imgDisplay").remove();
            }
            $("#edit_imgDisplayParent").html('<img id="edit_imgDisplay" src="'+reader.result+'" style="width: 2.5cm;height: 3.5cm">');
            // $("#imgDisplay").attr("src",reader.result);
        }
    }
}
/*when the insert imgFile has change, the method should be call, then reload the img Data to <img>#insert_imgDisPlay*/
function insert_custom_file_change() {
    let reader=new FileReader();
    let files = $("#insert_InputFile")[0].files;
    if(files != undefined || files != null){
        reader.readAsDataURL(files[0])
        reader.onload = function(e){
            $("#insert_imgDisplayParent").html('<img id="insert_imgDisplay" src="'+reader.result+'" style="width: 2.5cm;height: 3.5cm">');
            // $("#imgDisplay").attr("src",reader.result);
        }
    }
}
/*this method is reInit the input[type='text'], that's : reAdd class="form-control" */
function reInitEditClasses() {
    $("#edit_name").attr("class","form-control");
    $("#edit_account").attr("class","form-control");
    $("#edit_age").attr("class","form-control");
    $("#edit_sno").attr("class","form-control");
    $("#edit_email").attr("class","form-control");
    $("#edit_phone").attr("class","form-control");
    $("#edit_code").attr("class","form-control");
}
/*get insertUser formData*/
function getInsertUserFormData() {
    if(checkInsertAll() == false) return null;
    let formData = new FormData()
    let user = {}
    user.name = $("#insert_name").val()
    user.account = $("#insert_account").val()
    user.birthday = $("#insert_birthday").val()
    user.sex = $("#insert_sex").val()
    user.sno = $("#insert_sno").val()
    user.email = $("#insert_email").val()
    user.phone = $("#insert_phone").val()
    user.enabled = $("#insert_enabled").val()
    user.college = $("#insert_college").val()
    user.profession = $("#insert_profession").val()
    formData.append("student",JSON.stringify(user))
    let imgFile = $("#insert_InputFile")[0].files[0]
    if(imgFile != undefined){
        formData.append("faceImg",imgFile)
    }
    return formData
}





