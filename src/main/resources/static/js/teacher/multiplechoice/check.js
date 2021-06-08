/**
 * 校验input文本长度
 * minLength:最低长度
 * maxLength：最高长度
 */
function checkInputTextLength($element, minLength, maxLength) {
    let val = $element.val();
    if(val.length <minLength || val.length > maxLength){
        $element.attr("class","form-control is-invalid");
        return false;
    }else{
        $element.attr("class","form-control is-valid")
        return true;
    }
}

/**
 * 校验input为空
 */
function checkInputTextEmpty($element) {
    if($element.val().trim() == ""){
        $element.attr("class","form-control is-invalid");
        return false;
    }else{
        $element.attr("class","form-control is-valid")
        return true;
    }
}
// function checkSelectVal($element, array){
//     let result = false
//     $.each(array,function (idx,item) {
//         if($element.val().includes(item)){
//             result = true
//         }
//     })
// }
let $insert_problem = $("#insert_problem")
let $insert_answer = $("#insert_answer")
let $insert_choice_a = $("#insert_choice_a")
let $insert_choice_b = $("#insert_choice_b")
let $insert_choice_c = $("#insert_choice_c")
let $insert_choice_d = $("#insert_choice_d")
let $insert_choice_e = $("#insert_choice_e")
let $insert_choice_f = $("#insert_choice_f")
let $insert_choice_g = $("#insert_choice_g")
let $insert_choice_h = $("#insert_choice_h")
let $insert_difficultyVal = $("#insert_difficultyVal")
let $insert_analysis = $("#insert_analysis")
let $insert_videoPath = $("#insert_videoPath")


$insert_problem.change(function () {
    checkInputTextEmpty($insert_problem);
})
function checkInsert_Problem() {
    return checkInputTextEmpty($insert_problem);
}

$insert_answer.change(function () {
    checkInputTextEmpty($insert_answer)
})
function checkInsert_Answer() {
    return checkInputTextEmpty($insert_answer)
}

$insert_choice_a.change(function () {
    checkInputTextEmpty($insert_choice_a)
})
function checkInsert_choice_a() {
    return checkInputTextEmpty($insert_choice_a)
}

$insert_choice_b.change(function () {
    checkInputTextEmpty($insert_choice_b)
})
function checkInsert_choice_b() {
    return checkInputTextEmpty($insert_choice_b)
}

$insert_choice_c.change(function () {
    checkInputTextEmpty($insert_choice_c)
})
function checkInsert_choice_c() {
    return checkInputTextEmpty($insert_choice_c)
}

$insert_choice_d.change(function () {
    checkInputTextEmpty($insert_choice_d)
})
function checkInsert_choice_d() {
    return checkInputTextEmpty($insert_choice_d)
}

$insert_choice_e.change(function () {
    checkInputTextEmpty($insert_choice_e)
})
function checkInsert_choice_e() {
    return checkInputTextEmpty($insert_choice_e)
}

$insert_choice_f.change(function () {
    checkInputTextEmpty($insert_choice_f)
})
function checkInsert_choice_f() {
    return checkInputTextEmpty($insert_choice_f)
}

$insert_choice_g.change(function () {
    checkInputTextEmpty($insert_choice_g)
})
function checkInsert_choice_g() {
    return checkInputTextEmpty($insert_choice_g)
}

$insert_choice_h.change(function () {
    checkInputTextEmpty($insert_choice_h)
})
function checkInsert_choice_h() {
    return checkInputTextEmpty($insert_choice_h)
}

function checkInsert_ALl() {
    let problem = checkInsert_Problem()
    // let answer = checkInsert_Answer()

    let tag = true;
    //reInit choice class
    for(let i=97;i<=104;++i){
        let c = String.fromCharCode(i)
        $("#insert_choice_"+c).attr("class","form-control");
    }
    for(let i=104;i>=97;--i){
        let c = String.fromCharCode(i)
        console.log(c)
        if(c.toUpperCase() == $("#insert_answer").val()){

            for(let j=97;j<=c.charCodeAt(0);++j){
                // console.log(c);
                let ch = String.fromCharCode(j)
                if($("#insert_choice_"+ch).val().trim() == ""){
                    tag = false;
                    $("#insert_choice_"+ch).attr("class","form-control is-invalid")
                }else{
                    $("#insert_choice_"+ch).attr("class","form-control is-valid")
                }
            }
            break;
        }
        if(($("#insert_choice_"+c).val().trim() == "")){
            continue;
        }else{

            for(let j=97;j<=c.charCodeAt(0);++j){
                let ch = String.fromCharCode(j)
                if($("#insert_choice_"+ch).val().trim() == ""){
                    tag = false;
                    $("#insert_choice_"+ch).attr("class","form-control is-invalid")
                }else{
                    $("#insert_choice_"+ch).attr("class","form-control is-valid")
                }
            }
            break;
        }
    }
    return problem && tag;
}

