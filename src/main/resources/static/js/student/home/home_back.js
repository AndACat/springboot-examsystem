$(function () {
    let paperData;
    $.ajax({
        url: "/student/selectSimplePaperForStudentToDisplay",
        success: function (data) {
            paperData = data;
            initTable(data,13,1);
        }
    })
})

function initTable(data,pageNum,currentPage) {
    let $table_body = $("#table_body");
    let $table_foot = $("#table_foot");

    let table_body_nodes="";
    for (let i = 0; i < data.length && i< 9; i++) {
        let paper_info = data[i];
        if(new Date(paper_info.end).getTime() < new Date().getTime()){//已经过了考试时间
            table_body_nodes+="<tr>" +
                "              <td><div style='background-color: #ee5252' class='ui ribbon label'>已结束</div><strong>"+paper_info.paperName+"</strong></td>" +
                "              <td>"+new Date(paper_info.begin).toLocaleString()+"</td>" +
                "              <td>"+new Date(paper_info.end).toLocaleString()+"</td>" +
                "              <td>"+paper_info.during+"分钟</td>" +
                "              <td>"+"考试课程"+"</td>" +
                "              <td><button class='mini ui button disabled'>进入考试</button></td>" +
                "            </tr>"
        }else if(new Date(paper_info.begin).getTime() > new Date().getTime()){
            table_body_nodes+="<tr>" +
                "              <td><div style='background-color: #ecd439' class='ui ribbon label'>尚未开始</div><strong>"+paper_info.paperName+"</strong></td>" +
                "              <td>"+new Date(paper_info.begin).toLocaleString()+"</td>" +
                "              <td>"+new Date(paper_info.end).toLocaleString()+"</td>" +
                "              <td>"+paper_info.during+"分钟</td>" +
                "              <td>"+"考试课程"+"</td>" +
                "              <td><button class='mini ui button disabled'>进入考试</button></td>" +
                "            </tr>"
        }else{
            table_body_nodes+="<tr>" +
                "              <td><div style='background-color: #58ee55' class='ui ribbon label'>进行中</div><strong>"+paper_info.paperName+"</strong></td>" +
                "              <td>"+new Date(paper_info.begin).toLocaleString()+"</td>" +
                "              <td>"+new Date(paper_info.end).toLocaleString()+"</td>" +
                "              <td>"+paper_info.during+"分钟</td>" +
                "              <td>"+"考试课程"+"</td>" +
                "              <td><button class='mini positive ui button'>进入考试</button></td>" +
                "            </tr>"
        }
    }
    $table_body.append(table_body_nodes);
    
    let table_foot_nodes = "";
    let nodes_children="<a class='item' gopage='1'>首页</a>"
    if(pageNum <= 3){
        for (let i = 1; i <= pageNum; i++) {
            nodes_children+="<a class='item' gopage='"+i+"'>"+i+"</a>";
        }
        nodes_children+="<a class='item'gopage='"+pageNum+"'>"+pageNum+"</a><a class=\"item\" gopage='"+pageNum+"'>末页</a>";
        

    }else if(pageNum > 5){
        for (let i = 1; i <= pageNum; i++) {
            if(i<5){
                nodes_children+="<a class='item' gopage='"+i+"'>"+i+"</a>";
            }else if(i ==5 && pageNum>5){
                nodes_children+="<a class='item disabled' gopage='"+i+"'>...</a>";
                break;
            }
        }
        nodes_children+="<a class='item' gopage='"+pageNum+"'>"+pageNum+"</a><a class=\"item\" gopage='"+pageNum+"'>末页</a>";
    }
    console.log(nodes_children)
    table_foot_nodes = "<div class='ui left floated pagination menu'>\n" +
        "                <a class='icon item'>\n" +
        "                  <i class='left chevron icon'></i>\n" +
        "                </a>\n" +
        nodes_children+
        "                <a class='icon item'>\n" +
        "                  <i class='right chevron icon'></i>\n" +
        "                </a>\n" +
        "              </div>" ;
    $table_foot.append(table_foot_nodes);
}