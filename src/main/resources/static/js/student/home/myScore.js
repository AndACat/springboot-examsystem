const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 1500
});
let paper_uuid = "";
let studentPaperAnswer_uuid ="";
$(function () {
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
                    url: "/student/getMyScore",
                    dataType: "json",
                    async: false,
                    success: function (data) {
                        localData = data;
                    }
                });
                return localData;
            }
        },
        fields: [{
            title: "试卷",
            type: "text",
            width: 15,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate:function (idx,item) {
                return item.paper.paperName;
            }
        },{
                name: "singleChoiceScore",
                title: "单选题分数",
                type: "text",
                width: 15,
                css: "overHidden",
                height: 80,
                align: "center",
                filtering: true
        },{
            name: "multipleChoiceScore",
            title: "多选题分数",
            type: "text",
            width: 15,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true
        },{
            name: "fillScore",
            title: "填空题分数",
            type: "text",
            width: 15,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true
        },{
            name: "shortScore",
            title: "简答题分数",
            type: "text",
            width: 15,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true
        },{
            name: "judgeScore",
            title: "判断题分数",
            type: "text",
            width: 15,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true
        },{
            name: "programScore",
            title: "编程题分数",
            type: "text",
            width: 15,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true
        }, {
            name: "totalScore",
            title: "考生总分",
            type: "text",
            width: 15,
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value, item) {
                return item.totalScore;
            }
        }, {
            name: "paperTotalScore",
            title: "试卷总分",
            type: "text",
            width: 15,
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value, item) {
                return item.paperTotalScore;
            }
        },{
            title: "答题结束时间",
            type: "text",
            width: 30,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value, item) {
                return new Date(item.sort).toLocaleString();
            }
        }, {
            title: "是否及格",
            type: "text",
            width: 15,
            css: "overHidden",
            height: 80,
            align: "center",
            filtering: true,
            itemTemplate: function (value, item) {
                return item.pass ? "及格" : "<span class='text-orange'>不及格</span>";
            }
        }]
    });
})