$(function () {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 1500
    });

    $("#jsGrid").jsGrid({
        width: "100%",
        sorting: true,
        paging: true,
        pageIndex: 1,
        pageSize: 12,
        pageButtonCount: 5,
        // pageLoading: true,
        autoload: true,
        pagerFormat: "页数: {first} {prev} {pages} {next} {last}    第 {pageIndex} 页,共 {pageCount} 页",
        pagePrevText: "<button class='btn btn-primary btn-xs'>上一页</button>",
        pageNextText: "<button class='btn btn-primary btn-xs'>下一页</button>",
        pageFirstText: "<button class='btn btn-primary btn-xs'>首页</button>",
        pageLastText: "<button class='btn btn-primary btn-xs'>末页</button>",
        confirmDeleting: true,
        noDataContent: "没有数据...",
        loadMessage: "正在加载数据，请稍候......",
        dataType: "json",
        controller: {
            loadData: function (filter) {
                let localData;
                $.ajax({
                    url: "/student/selectSimplePaperForStudentToDisplay",
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
            title: "试卷",
            type: "text",
            width: 30,
            height: 80,
            align: "center",
            filtering: true,
            css: "overHidden"
        },{
            name: "begin",
            title: "开考日期",
            type: "text",
            width: 30,
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value,item) {
                return new Date(item.begin).toLocaleString();
            }
        },{
            name: "end",
            title: "结考日期",
            type: "text",
            width: 30,
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value,item) {
                return new Date(item.end).toLocaleString();
            }
        },{
            name: "during",
            title: "考试时间",
            type: "text",
            width: 5,
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value,item) {
                return item.during+"分钟";
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
            title: "操作",
            width: 20,
            align: "center",
            sorting: false,
            itemTemplate: function (value, item) {
                if(new Date(item.end).getTime() < new Date().getTime()) {//已经过了考试时间)
                    return '<button class="btn btn-xs btn-danger disabled">已结束</button>';
                }else if(new Date(item.begin).getTime() > new Date().getTime()){
                    return '<button class="btn btn-xs btn-info disabled">尚未开始</button>';
                }else{
                    return '<button class="btn btn-xs btn-success goToExam" data=\'' + item.uuid + '\' openFaceIdentity=\''+item.openFaceIdentity+'\'>进行中</button>';
                }

            }

        }]
    });

    $(document).on("click",".goToExam",function () {
        let uuid = $(this).attr("data");
        if(confirm("确认参加考试吗？")){
            $.ajax({
                url:"/student/prepareToTakeExam",
                data:{
                    uuid:uuid
                },
                async: false,
                success:function (data) {
                    if(data.code == 200){
                        window.open("/student/takeExam.html");
                    }else {
                        $(document).Toasts('create', {
                            class: 'bg-danger',
                            autohide: true,
                            title: '操作状态',
                            body: "您已参加此轮考试,无须再次参加。",
                            width: "200",
                            delay: 3000
                        })
                    }

                }
            })
        }
    })
})