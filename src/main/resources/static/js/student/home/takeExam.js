let $residueTime =null;// $("#residueTime");
let interval = null;
let residueTime = 0;
let firstProblemType = "";
const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 1500
});
$(function () {
    $.ajax({
        url: "/student/takeExam_getSimplePaperInfo",
        success: function (map) {
            if(map["errorCode"] != undefined && map["errorCode"]==203){
                window.location.href="/home";
                return;
            }
            initStudentInfoCard(map["studentName"],map["studentSno"],map["begin"],map["end"],map["during"]);
            initProblemList(map["simplePaperInfo"]);//初始化答题卡
            console.log(map);
            initProblemCard(
                -1,
                -1,
               -1,
                map["currentProblemType"],
                map["currentProblemIdx"],
                map["currentProblemData"],
                map["currentTotalProblemIdx"],
                map["nextProblemType"],
                map["nextProblemIdx"],
                map["nextTotalProblemIdx"]
            );
            if(map["openFaceIdentity"]){//如果开启人脸识别
                websocket = new WebSocket(wsServer);
                //监听连接打开
                websocket.onopen = function (evt) {
                    sendMsgServer("student_uuid:"+map["student_uuid"]);
                    sendMsgServer("paper_uuid:"+map["paper_uuid"]);
                };
                //监听服务器数据推送
                websocket.onmessage = function (evt) {
                    if(evt.data != "没有检测到人脸"){
                        try{
                            let face3DAngle = JSON.parse(evt.data);
                            let yaw = face3DAngle.yaw;
                            if(Math.abs(yaw) >= 20){
                                $(document).Toasts('create', {
                                    class: 'bg-danger',
                                    autohide: true,
                                    title: '监控提醒',
                                    body: "系统判定有作弊风险,请坐正考试.",
                                    width: "200"
                                })
                            }
                        }catch (e) {}
                    }else{
                        $(document).Toasts('create', {
                            class: 'bg-danger',
                            autohide: true,
                            title: '监控提醒',
                            body: "系统没有检测到人脸，请回到座位",
                            width: "200"
                        })
                    }
                    console.log(evt.data);
                    endTime = new Date().getTime();
                    console.log((endTime-startTime)/1000 + "秒");
                };
                //监听连接关闭
                websocket.onclose = function (evt) {
                };
                openMedia();
                init();
            }
        }

    })
    $("#lastProblem").click(function () {
        //加载上一题
        let $currentProblem = $("currentProblem");
        let lastTotalProblemIdx = $currentProblem.attr("lastTotalProblemIdx");
        let currentTotalProblemIdx = $currentProblem.attr("currentTotalProblemIdx");
        if(lastTotalProblemIdx+"" == "-1"){
            console.log("没有上一题了");
            return;
        }
        let info = getNextProblemInfo(lastTotalProblemIdx);
        initProblemCard(
            info["lastProblemType"],
            info["lastProblemIdx"],
            info["lastTotalProblemIdx"],
            info["currentProblemType"],
            info["currentProblemIdx"],
            info["currentProblemData"],
            info["currentTotalProblemIdx"],
            info["nextProblemType"],
            info["nextProblemIdx"],
            info["nextTotalProblemIdx"]
            );

    })
    $("#nextProblem").click(function () {
        //加载下一题
        let $currentProblem = $("currentProblem");
        let lastTotalProblemIdx = $currentProblem.attr("lastTotalProblemIdx");
        let currentTotalProblemIdx = $currentProblem.attr("currentTotalProblemIdx");
        let nextTotalProblemIdx = $currentProblem.attr("nextTotalProblemIdx");
        if(nextTotalProblemIdx+"" == "-1"){
            console.log("没有下一题了");
            return;
        }
        let info = getNextProblemInfo(nextTotalProblemIdx);
        initProblemCard(
            info["lastProblemType"],
            info["lastProblemIdx"],
            info["lastTotalProblemIdx"],
            info["currentProblemType"],
            info["currentProblemIdx"],
            info["currentProblemData"],
            info["currentTotalProblemIdx"],
            info["nextProblemType"],
            info["nextProblemIdx"],
            info["nextTotalProblemIdx"]
        );
    })

    $(document).on("click",".loadProblem",function () {
        let totalProblemIdx = $(this).attr("totalProblemIdx");
        let info = getNextProblemInfo(totalProblemIdx);
        initProblemCard(
            info["lastProblemType"],
            info["lastProblemIdx"],
            info["lastTotalProblemIdx"],
            info["currentProblemType"],
            info["currentProblemIdx"],
            info["currentProblemData"],
            info["currentTotalProblemIdx"],
            info["nextProblemType"],
            info["nextProblemIdx"],
            info["nextTotalProblemIdx"]
        );
    })
    // 单选题 判断题 答案提交
    $(document).on("change","input[name='answer']:radio:checked",function () {
        let answer = $(this).val();
        let totalProblemIdx = $("currentProblem").attr("currentTotalProblemIdx");
        $.ajax({
            url: "/student/takeExam_saveAnswer",
            data:{
                totalProblemIdx:totalProblemIdx,
                answer:answer
            },
            success: function (data) {
                if(data.code == 200){
                    let $button = $("button[totalproblemidx='"+totalProblemIdx+"']");
                    let cla = $button.attr("class");
                    $button.attr("class",cla.replace("btn-default","btn-info"));
                }
            }
        })
    })
    //多选题 答案提交
    $(document).on("change","input[name='answer']:checkbox",function () {
        let arrayAnswer = [];
        $("input[name='answer']:checkbox:checked").each(function () {
            arrayAnswer.push($(this).val());
        })
        console.log(arrayAnswer)
        let totalProblemIdx = $("currentProblem").attr("currentTotalProblemIdx");
        $.ajax({
            url: "/student/takeExam_saveAnswer",
            data:{
                totalProblemIdx:totalProblemIdx,
                answer:arrayAnswer.join(',')
            },
            success: function (data) {
                if(data.code == 200){
                    let $button = $("button[totalproblemidx='"+totalProblemIdx+"']");
                    let cla = $button.attr("class");
                    $button.attr("class",cla.replace("btn-default","btn-info"));
                }
            }
        })
    })
    //简答题 答案提交
    $(document).on("change","textarea[name='answer']",function () {
        let totalProblemIdx = $("currentProblem").attr("currentTotalProblemIdx");
        $.ajax({
            url: "/student/takeExam_saveAnswer",
            data:{
                totalProblemIdx:totalProblemIdx,
                answer:$(this).val()
            },
            success: function (data) {
                if(data.code == 200){
                    let $button = $("button[totalproblemidx='"+totalProblemIdx+"']");
                    let cla = $button.attr("class");
                    $button.attr("class",cla.replace("btn-default","btn-info"));
                }
            }
        })
    })
    //填空题 答案提交
    $(document).on("change","input[name='answer'][answerType='fill']",function () {
        let arrayAnswer = [];
        $("input[name='answer'][answerType='fill']").each(function () {
            arrayAnswer.push($(this).val());
        })
        let totalProblemIdx = $("currentProblem").attr("currentTotalProblemIdx");
        $.ajax({
            url: "/student/takeExam_saveAnswer",
            data:{
                totalProblemIdx:totalProblemIdx,
                answer:JSON.stringify(arrayAnswer)
            },
            success: function (data) {
                if(data.code == 200){
                    let $button = $("button[totalproblemidx='"+totalProblemIdx+"']");
                    let cla = $button.attr("class");
                    $button.attr("class",cla.replace("btn-default","btn-info"));
                }
            }
        })
    })
    $("#overExam").click(function () {
        let con = true;
        if(residueTime > 0){
            con = confirm("考试时间未到,确认提交考卷吗?");
        }
        if(con){
            $.ajax({
                url:"/student/takeExam_overExam",
                success:function (data) {
                    if(data.code == 403){
                        $(document).Toasts('create', {
                            class: 'bg-danger',
                            autohide: true,
                            title: '考试提醒',
                            body: data.msg,
                            width: "200"
                        })
                    }
                    if(data.code == 200){
                        $(document).Toasts('create', {
                            class: 'bg-success',
                            autohide: true,
                            title: '考试提醒',
                            body: data.msg,
                            width: "200"
                        })
                        window.location.href = "/home";
                    }
                }
            })
        }
    })
})
//初始化答题卡
function initProblemList(simplePaperInfoList) {//初始化答题卡
    let body = "";
    let totalProblemIdx = 1;
    for (let i = 0; i < simplePaperInfoList.length; i++) {
        let info = simplePaperInfoList[i];
        let problemSize = info.problemSize;
        switch (info.problemType) {
            case "singleChoiceList":{
                body+="<div class=\"list-group-item\"><div class=\"\">单选题</div>";
                for (let i = 1; i <= problemSize; i++) {
                    body+="<button class=\"loadProblem btn btn-default btn-sm custom-btn\" totalProblemIdx='"+totalProblemIdx+++"'>"+i+"</button>";
                }
                body+="</div>";
                break;
            }
            case "multipleChoiceList":{
                body+="<div class=\"list-group-item\"><div class=\"\">多选题</div>";
                for (let i = 1; i <= problemSize; i++) {
                    body+="<button class=\"loadProblem btn btn-default btn-sm custom-btn\" totalProblemIdx='"+totalProblemIdx+++"'>"+i+"</button>";
                }
                body+="</div>";
                break;
            }
            case "fillList":{
                body+="<div class=\"list-group-item\"><div class=\"\">填空题</div>";
                for (let i = 1; i <= problemSize; i++) {
                    body+="<button class=\"loadProblem btn btn-default btn-sm custom-btn\" totalProblemIdx='"+totalProblemIdx+++"'>"+i+"</button>";
                }
                body+="</div>";
                break;
            }
            case "shortList":{
                body+="<div class=\"list-group-item\"><div class=\"\">简答题</div>";
                for (let i = 1; i <= problemSize; i++) {
                    body+="<button class=\"loadProblem btn btn-default btn-sm custom-btn\" totalProblemIdx='"+totalProblemIdx+++"'>"+i+"</button>";
                }
                body+="</div>";
                break;
            }
            case "judgeList":{
                body+="<div class=\"list-group-item\"><div class=\"\">判断题</div>";
                for (let i = 1; i <= problemSize; i++) {
                    body+="<button class=\"loadProblem btn btn-default btn-sm custom-btn\" totalProblemIdx='"+totalProblemIdx+++"'>"+i+"</button>";
                }
                body+="</div>";
                break;
            }
            case "programList":{
                body+="<div class=\"list-group-item\"><div class=\"\">编程题</div>";
                for (let i = 1; i <= problemSize; i++) {
                    body+="<button class=\"loadProblem btn btn-default btn-sm custom-btn\" totalProblemIdx='"+totalProblemIdx+++"'>"+i+"</button>";
                }
                body+="</div>";
                break;
            }
        }
    }
    $("#problemList").html(body);
}
//初始化学生信息卡片
function initStudentInfoCard(studentName,studentSno,begin,end,during){
    residueTime = during*60;
    let body = "<div class=\"list-group-item\"><strong>考生信息:</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>"+studentName+"</span><span>"+studentSno+"</span></div>" +
        "                  <div class=\"list-group-item\"><strong>剩余时间:</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id='residueTime'></span></div>" +
        "                  <div class=\"list-group-item\"><strong>开始时间:</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>"+new Date(begin).toLocaleString()+"</span></div>" +
        "                  <div class=\"list-group-item\"><strong>结束时间:</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>"+new Date(end).toLocaleString()+"</span></div>";
    $("#studentInfo").html(body);
    $residueTime = $("#residueTime");
    interval = setInterval(resetTime,1000);

}
//考试倒计时
function resetTime(){
    if(residueTime <= 0){
        clearInterval(interval);

        return;
    }
    let day = Math.floor(residueTime/(60 * 60 * 24));
    let hour = Math.floor(residueTime / (60 * 60)) - (day * 24);
    let minute = Math.floor(residueTime / 60) - (day * 24 * 60) - (hour * 60);
    let second = Math.floor(residueTime % 60);
    let time_body = day + ":" + hour + ":" + minute + ":" + second;
    $residueTime.html(time_body)
    residueTime-=1;
}
//加载下一道题目
function getNextProblemInfo(totalProblemIdx) {
    let problemInfo = null;
    $.ajax({//按给定序号加载
        url: "/student/takeExam_getProblem",
        async: false,
        data:{
            totalProblemIdx:totalProblemIdx
        },
        success: function (data) {
            problemInfo = data;
        }
    })
    return problemInfo;
}
//加载题目卡片
function initProblemCard(lastProblemType,lastProblemIdx,lastTotalProblemIdx,currentProblemType,currentProblemIdx,currentProblemData,currentTotalProblemIdx,nextProblemType,nextProblemIdx,nextTotalProblemIdx) {
    let problemNode = "";//卡片Dom结点
    let problemChildNode = "";//孩子结点
    switch (currentProblemType) {
        case "单选题":{
            console.info("生成单选题节点...");
            for(let i=97;i<=104;++i){
                let c = String.fromCharCode(i)
                let option = currentProblemData["choice_"+c];
                if(option != ""){
                    problemChildNode+="<div class=\"custom-control custom-radio\">" +
                        "<input class=\"custom-control-input\" name=\"answer\" value='"+c.toUpperCase()+"' id=\"answer"+(i-96)+"\" type=\"radio\">" +
                        "<label for=\"answer"+(i-96)+"\" class=\"custom-control-label\">选项"+c.toUpperCase()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+option+"</label>" +
                        "</div>";
                }else{
                    break;
                }
            }
            problemNode = "<span><strong>"+"题号:"+(currentProblemIdx-0+1)+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+currentProblemData.problem+"</strong></span>" +
                "<div class=\"form-group\">" +problemChildNode+"</div>";
            $("#problemType").text("单选题");
            $("#problemNode").html(problemNode);
            let answer = currentProblemData["answer"];
            if(answer.trim()!=""){
                $("input:radio[value='"+answer+"']").attr("checked",true);
            }
            break;
        }
        case "多选题":{
            console.info("生成多选题节点...");
            for(let i=97;i<=104;++i){
                let c = String.fromCharCode(i)
                let option = currentProblemData["choice_"+c];
                if(option != ""){
                    problemChildNode+="<div class=\"custom-control custom-radio\">" +
                        "<input class=\"custom-control-input\" name=\"answer\" value='"+c.toUpperCase()+"' id=\"answer"+(i-96)+"\" type=\"checkbox\">" +
                        "<label for=\"answer"+(i-96)+"\" class=\"custom-control-label\">选项"+c.toUpperCase()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+option+"</label>" +
                        "</div>";
                }else{
                    break;
                }
            }
            problemNode = "<span><strong>"+"题号:"+(currentProblemIdx-0+1)+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+currentProblemData.problem+"</strong></span>" +
                "<div class=\"form-group\">" +problemChildNode+"</div>";
            $("#problemType").text("多选题");
            $("#problemNode").html(problemNode);
            let answer = currentProblemData["answer"];
            if(answer != null){
                for (const ans of answer) {
                    $("input:checkbox[value='"+ans+"']").attr("checked",true);
                }
            }
            break;
        }
        case "填空题":{
            console.info("生成填空题节点...");
            problemNode = "<span><strong>"+"题号:"+(currentProblemIdx-0+1)+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+fillProblemCombineAnswer(currentProblemData.problem,currentProblemData.answer)+"</strong></span>";
            $("#problemNode").html(problemNode);
            $("#problemType").text("填空题");
            break;
        }
        case "简答题":{
            console.info("生成简答题节点...");
            problemNode = "<span><strong>"+"题号:"+(currentProblemIdx-0+1)+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+currentProblemData.problem+"</strong></span>";
            problemNode+="<textarea name='answer' style='height: 360px' class='form-control'>"+(currentProblemData["answer"] == null ? "" : currentProblemData["answer"])+"</textarea>";
            $("#problemNode").html(problemNode);
            $("#problemType").text("简答题");
            break;
        }
        case "判断题":{
            console.info("生成判断题节点...");
            let problemChildNode = "";
            problemChildNode+="<div class=\"custom-control custom-radio\">" +
                "<input class=\"custom-control-input\" name=\"answer\" value='true' id=\"answer1\" type=\"radio\">" +
                "<label for=\"answer1\" class=\"custom-control-label\">选项:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;对</label>" +
                "</div>";
            problemChildNode+="<div class=\"custom-control custom-radio\">" +
                "<input class=\"custom-control-input\" name=\"answer\" value='false' id=\"answer2\" type=\"radio\">" +
                "<label for=\"answer2\" class=\"custom-control-label\">选项:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;错</label>" +
                "</div>";
            problemNode = "<span><strong>"+"题号:"+(currentProblemIdx-0+1)+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+currentProblemData.problem+"</strong></span>";
            problemNode+="<div class=\"form-group\">" +problemChildNode+"</div>";
            $("#problemNode").html(problemNode);
            console.log(currentProblemData["answer"])
            if(currentProblemData["answer"] == true){
                $("#answer1").prop("checked",true)
            }
            if(currentProblemData["answer"] == false){
                $("#answer2").prop("checked",true)
            }
            $("#problemType").text("判断题");
            break;
        }
        case "编程题":{
            console.info("生成编程题节点...");
            problemNode = "<span><strong>"+"题号:"+(currentProblemIdx-0+1)+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+currentProblemData.problem+"</strong></span>";
            problemNode+="<textarea name='answer' style='height: 360px' class='form-control'>"+(currentProblemData["answer"] == null ? "" : currentProblemData["answer"])+"</textarea>";
            $("#problemNode").html(problemNode);
            $("#problemType").text("编程题");
            break;
        }
    }



    let $currentProblem = $("currentProblem");
    $currentProblem.attr("lastTotalProblemIdx",lastTotalProblemIdx);
    $currentProblem.attr("currentTotalProblemIdx",currentTotalProblemIdx);
    $currentProblem.attr("nextTotalProblemIdx",nextTotalProblemIdx);
}
function getFillInputTableProcessProblem(problem) {//返回将#{}改变为input框的题目
    let answerIdx = 1;
    while(problem.includes("#{}")){
        let input = "<input type='text' style='display: inline;width: 100px' class='form-control' name='answer' id='answer'"+answerIdx+++">";
        problem = problem.replace("#{}",input);
    }
    return problem;
}

function overExam() {
    $.ajax({
        url:"/student/takeExam_overExam",
        success: function (data) {
            if(data.code == 200){
                window.location.href="";
            }else if(data.code == 403){
                //无法结束考试

            }
        }
    })
}
let startTime = 0;
let endTime = 0;
let mediaStreamTrack=null; // 视频对象(全局)
let video = document.getElementById('video');
function dataURLtoFile (dataurl, filename = 'file') {
    let arr = dataurl.split(',')
    let mime = arr[0].match(/:(.*?);/)[1]
    let suffix = mime.split('/')[1]
    let bstr = atob(arr[1])
    let n = bstr.length
    let u8arr = new Uint8Array(n)
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
    }
    return new File([u8arr], `${filename}.${suffix}`, {type: mime})
}

function openMedia() {
    let constraints = {
        video: { width: 300, height: 300 },
        audio: false
    };
    let promise;
    promise = navigator.mediaDevices.getUserMedia(constraints);
    promise.then((mediaStream) => {
        // mediaStreamTrack = typeof mediaStream.stop === 'function' ? mediaStream : mediaStream.getTracks()[1];
        mediaStreamTrack=mediaStream.getVideoTracks()
        video.srcObject = mediaStream;
        video.play();
        // video.hidden;
    });
}
// 拍照
function getPhoto() {
    let newCanvas = document.createElement("canvas");
    newCanvas.width = 100; //☜
    newCanvas.height = 100;
    newCanvas.getContext('2d').drawImage(video, 0, 0, 100, 100);

    // toDataURL  ---  可传入'image/png'---默认, 'image/jpeg'
    // 这里的img就是得到的图片
    // return newCanvas.toDataURL("image/png");
    return  dataURLtoFile(newCanvas.toDataURL("image/png"),"face");
}
let int;
function init() {
    int=self.setInterval("sendPhotoToServer()",1500);
}
function stop() {
    window.clearInterval(int);
}
// 关闭摄像头
function closeMedia() {
    let stream = document.getElementById('video').srcObject;
    let tracks = stream.getTracks();
    tracks.forEach(function(track) {
        track.stop();
    });
    document.getElementById('video').srcObject = null;
}
let wsServer = 'ws://127.0.0.1:81/faceDetect';
let websocket;// = new WebSocket(wsServer);

function sendPhotoToServer() {
    startTime = new Date().getTime();
    let photo = getPhoto();
    websocket.send(photo);
}
function sendMsgServer(msg) {
    console.log(msg)
    websocket.send(msg);
}
function fillProblemCombineAnswer(problem,answer) {
    answer.push("");
    answer.push("");
    answer.push("");
    answer.push("");
    answer.push("");
    answer.push("");
    let idx = 0;
    while (problem.includes("#{}")){
        problem = problem.replace("#{}","<input type='text' answerType='fill' name='answer' id='answer"+idx+"' class='form-control' style='width: 100px;display: inline' value='"+answer[idx++]+"'>");
    }
    return problem;
}

