$(function () {
    /*init for Swal*/
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 1500
    });
    let teacherListData;
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
    /*init insertUser insert_modal college and profession options*/
    let $insert_college = $("#insert_college")
    $.each(collegeList,function (idx,item) {
       $insert_college.append('<option value="'+item.name+'">'+ item.name+'</option>')
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
                    url: "/admin/selectAllTeacher",
                    dataType: "json",
                    data: filter,
                    async: false,
                    success:function (data) {
                        teacherListData = data;
                    }
                });
                return teacherListData;
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
                name: "tno",
                title: "职工号",
                type: "text",
                width: 20,
                align: "center"
            },
            {
                name: "birthday",
                title: "出生年月",
                type: "text",
                width: 10,
                align: "center"
            }, {
                name: "sex",
                title: "性别",
                type: "text",
                width: 10,
                align: "center",
                itemTemplate: function (value,item) {
                    if(item.sex == true || item.sex == "true")
                        return "男";
                    if(item.sex == false || item.sex == "false")
                        return "女";
                    return "未填写";
                }
            }, {
                name: "email",
                title: "邮箱",
                type: "text",
                width: 30,
                align: "center"
            }, {
                name: "phone",
                title: "手机号",
                type: "text",
                width: 30,
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
                    return '<span class="changeTeacherCode" data='+d+'><i class="fa fa-repeat" aria-hidden="true"></i></span>&nbsp;&nbsp;' +
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
        $("#edit_tno").val(data.tno);
        $("#edit_email").val(data.email);
        $("#edit_phone").val(data.phone);
        $("#edit_code").val(data.code);

        $("#edit_classAndCourseLists").html("")
        if(data.classAndCourseLists != undefined && data.classAndCourseLists != null){
            for(let i=0;i<data.classAndCourseLists.length;++i){
                let text = data.classAndCourseLists[i].class_name +" , "+ data.classAndCourseLists[i].course_name;
                $("#edit_classAndCourseLists").append('<option>'+text+'</option>');
            }
        }
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
            if(data.college == val.name){
                $edit_college.append('<option selected value="'+val.name+'">'+val.name+'</option>');
                let professionList = val.professionList;
                $("#edit_profession").html("");
                $.each(professionList,(idx,val) =>{
                    if(data.profession == val){
                        $("#edit_profession").append('<option selected value="'+val+'">'+val+'</option>')
                    }else{
                        $("#edit_profession").append('<option value="'+val+'">'+val+'</option>')
                    }
                })
            }else{
                $edit_college.append('<option value="'+val.name+'">'+val.name+'</option>');
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
        let data = JSON.parse($(this).attr("data"));
        if(confirm("确认删除吗?")){
            $.ajax({
                url:"/admin/deleteTeacherByAccount/"+data.account,
                type: "post",
                success: function (data) {
                    if(data.code == 200 || data.code == "200"){
                        /*提示框*/
                        Toast.fire({
                            type: 'success',
                            title: '删除成功'
                        })

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

    $("#submitTeacherInfo").click(function () {
        if(!checkEditAll()){
            Toast.fire({
                type: 'error',
                title: '修改状态',
                body: "信息不规范,修改失败"
            })
            return;
        }
        let formData = new FormData();
        if($("#edit_InputFile")[0].files != undefined){
            formData.append("faceImg", $("#edit_InputFile")[0].files[0]);
        }
        formData.append("name",$("#edit_name").val());
        formData.append("account",$("#edit_account").val());
        formData.append("code",$("#edit_code").val());
        formData.append("sex",$("#edit_sex").val());
        formData.append("birthday",$("#edit_birthday").val());
        formData.append("tno",$("#edit_tno").val());
        formData.append("email",$("#edit_email").val());
        formData.append("phone",$("#edit_phone").val());
        formData.append("enabled",$("#edit_enabled").val());
        formData.append("college",$("#edit_college").val());
        formData.append("profession",$("#edit_profession").val());
        var jsonData = {};
        formData.forEach((value, key) => jsonData[key] = value);
        $.ajax({
            url: "/admin/updateTeacherInfo",
            processData: false,
            contentType: false,
            data:formData,
            type: "post",
            success: function (data) {
                if(data.code == 200 || data.code == "200"){
                    /*提示框*/
                    Toast.fire({
                        type: 'success',
                        title: data.msg
                    })
                    if(data.data == null || data.data == undefined){
                        jsonData["faceImg"]=$("#edit_faceImg").attr("src");
                    }else{
                        jsonData["faceImg"]=data.data;
                    }
                    editJsJridData($("#jsGrid"),$("#edit_account").val(),jsonData);
                    $(".modal").modal('hide')

                }else if(data.length > 2048){
                    Toast.fire({
                        type: 'error',
                        title: "修改状态",
                        body: "登录"
                    })
                }
            }
        })
    })

    //重设密码
    $(document).on("click",".changeTeacherCode",function () {
    let data = JSON.parse($(this).attr("data"));
    if(confirm("确认重设密码吗？")){
        $.ajax({
            url:"/admin/changeTeacherCode",
            data:{
                account: data.account
            },
            type:"post",
            success:function (data) {
                if(data.code == 200 || data.code == "200"){
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

    //#edit_profession should be set when college has changed
    $("#edit_college").change(() =>{
        let professions;
        let val = $("#edit_college").val();
        for(let i=0;i<collegeList.length;++i){
            if (val == collegeList[i].name){
                professions = collegeList[i].professionList;
                break;
            }
        }
        let $profession = $("#edit_profession");
        $profession.html("")
        $.each(professions,function (idx,val) {
            $profession.append('<option value="'+val+'">'+val+'</option>')
        })
    })

    //#edit_profession should be set when college has changed
    $("#insert_college").change(() =>{
        let professions;
        let val = $("#insert_college").val();
        for(let i=0;i<collegeList.length;++i){
            if (val == collegeList[i].name){
                professions = collegeList[i].professionList;
                break;
            }
        }
        let $profession = $("#insert_profession");
        $profession.html("")
        $.each(professions,function (idx,val) {
            $profession.append('<option value="'+val+'">'+val+'</option>')
        })
    })

    /*when insert user button has click*/
    $("#insert_submitTeacherInfo").click(function () {
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
            url: "/admin/insertTeacher",
            type: "post",
            data: formData,
            processData: false,
            contentType: false
        }).done(function (data) {
            if(data.code == 200 || data.code == "200"){
                insertJsJridData($("#jsGrid"),data.data+"")
                $(document).Toasts('create', {
                    class: 'bg-success',
                    title: '操作成功',
                    body: "用户保存成功",
                    width: "200"
                })
                window.location.reload()
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
            url:"/admin/insertTeacherByExcel",
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
            refreshJSGridData($("#jsGrid"),teacherListData)
            return;
        }
        $.each(teacherListData,function (idx,item) {
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
            if(item.profession.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.birthday.includes(searchProblem)){
                newData.push(item);
                return true;
            }
            if(item.tno.includes(searchProblem)){
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
    $("#edit_tno").attr("class","form-control");
    $("#edit_email").attr("class","form-control");
    $("#edit_phone").attr("class","form-control");
    $("#edit_code").attr("class","form-control");
}
//对图片进行适当的旋转
function rotateImg(Orientation,$faceImg) {
    if(Orientation == undefined){
        console.warn("Orientation: underfined");
        return;
    }
    console.log("Orientation:"+Orientation);
    let height = $faceImg.height;
    let width = $faceImg.width;

    switch (Orientation) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
    }
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
    user.tno = $("#insert_tno").val()
    user.email = $("#insert_email").val()
    user.phone = $("#insert_phone").val()
    user.enabled = $("#insert_enabled").val()
    user.college = $("#insert_college").val()
    user.profession = $("#insert_profession").val()
    formData.append("teacher",JSON.stringify(user))
    let imgFile = $("#insert_InputFile")[0].files[0]
    if(imgFile != undefined){
        formData.append("faceImg",imgFile)
    }
    return formData
}





