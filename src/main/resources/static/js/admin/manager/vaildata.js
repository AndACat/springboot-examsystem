$("#edit_name").on("input",function () {
    checkEditName();
});
function checkEditName(){
    let $d = $("#edit_name");
    let val = $d.val();
    if(val.length < 8 && val.length > 1){
        $d.attr("class","form-control is-valid");
        return true;
    }else{
        $d.attr("class","form-control is-invalid");
        return false;
    }
}
$("#edit_account").on("input",function () {
    checkEditAccount()
});
function checkEditAccount(){
    let $d = $("#edit_account");
    let val = $d.val();
    if(val.length > 5 && val.length < 15){
        $d.attr("class","form-control is-valid");
        return true;
    }else{
        $d.attr("class","form-control is-invalid");
    }
}
$("#edit_birthday").on("input",function () {
    checkEditBirthday();
});
function checkEditBirthday(){
    let $birthday = $("#edit_birthday");
    let val = $birthday.val();
    if(val == ""){
        $birthday.attr("class","form-control is-invalid");
        return false;
    }
    let date;
    try{
        date = new Date(val);
    }catch (e) {
        $birthday.attr("class","form-control is-invalid");
        return false;
    }
    if(date == "Invalid Date"){
        $birthday.attr("class","form-control is-invalid");
        return false;
    }
    $birthday.attr("class","form-control is-valid");
    return true;
}
$("#edit_mno").on("input",function () {
    checkEditMno()
});
function checkEditMno(){
    let $d = $("#edit_mno");
    let val = $d.val();
    if(!isNaN(val) && val.length <= 20 && val.length >=3){
        $d.attr("class","form-control is-valid");
        return true;
    }else{
        $d.attr("class","form-control is-invalid");
        return false;
    }
}
$("#edit_email").on("input",function () {
    checkEditEmail();
});
function checkEditEmail(){
    let $d = $("#edit_email");
    let val = $d.val();
    if(/^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(val)){
        $d.attr("class","form-control is-valid");
        return true;
    }else{
        $d.attr("class","form-control is-invalid");
        return false;
    }
        
}

$("#edit_phone").on("input",function () {
    checkEditPhone()
});
function checkEditPhone(){
    let $d = $("#edit_phone");
    let val = $d.val();
    if(/^1(3|4|5|6|7|8|9)\d{9}$/.test(val)){
        $d.attr("class","form-control is-valid");
        return true;
    }else{
        $d.attr("class","form-control is-invalid");
        return false;
    }
        
}
$("#edit_code").on("input",function () {
    checkEditCode()
});
function checkEditCode() {
    let $d = $("#edit_code");
    let val = $d.val();
    if(val.length >= 3 && val.length <= 20){
        $d.attr("class","form-control is-valid");
        return true;
    }else{
        $d.attr("class","form-control is-invalid");
        return false;
    }
}


function checkEditAll() {
    let a = checkEditMno()
    let b = checkEditAccount()
    let c = checkEditName()
    let d =checkEditBirthday()
    // let e = checkCode()
    let f = checkEditEmail()
    let g = checkEditPhone()
    let tag = a && b && c && d && f && g;
    console.log("checkEditAll()结果为"+tag)
    return tag;
}
/*头像*/
/*人脸图片*/


/*校验插入用户的*/
function checkInsertName() {
    let $name = $("#insert_name")
    if($name.val().trim() == ""){
        $name.attr("class","form-control is-invalid")
        return false;
    }
    $name.attr("class","form-control is-valid")
    return true;
}
function checkInsertAccount() {
    let $account = $("#insert_account")
    if($account.val().trim() == "" || $account.val().trim().length < 6){
        $account.attr("class","form-control is-invalid")
        return false;
    }
    $account.attr("class","form-control is-valid")
    return true;
}
function checkInsertBirthday() {
    let $birthday = $("#insert_birthday")
    if($birthday.val().trim() == "" || new Date($birthday.val().trim()) == "Invalid Date"){
        $birthday.attr("class","form-control is-invalid")
        return false
    }
    $birthday.attr("class","form-control is-valid")
    return true
}
function checkInsertSex() {
    let $sex = $("#insert_sex")
    if($sex.val() != "1" && $sex.val() != "0"){
        window.location.reload()
    }
    return true
}
function checkInsertMno() {
    let $mno = $("#insert_mno")
    if($mno.val().trim() == "" || $mno.val().trim().length < 6){
        $mno.attr("class","form-control is-invalid")
        return false
    }
    $mno.attr("class","form-control is-valid")
    return true
}
function checkInsertEmail() {
    let $email = $("#insert_email")
    if(!$email.val().trim().match(/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/)){
        $email.attr("class","form-control is-invalid")
        return false
    }
    $email.attr("class","form-control is-valid")
    return true
}
function checkInsertPhone() {
    let $phone = $("#insert_phone")
    if(!$phone.val().trim().match(/^1(3|4|5|6|7|8|9)\d{9}$/)){
        $phone.attr("class","form-control is-invalid")
        return false
    }
    $phone.attr("class","form-control is-valid")
    return true
}
$("#insert_account").on("input",function () {
    checkInsertAccount()
})
$("#insert_name").on("input",function () {
    checkInsertName()
})
$("#insert_birthday").on("input",function () {
    checkInsertBirthday()
})
$("#insert_mno").on("input",function () {
    checkInsertMno()
})
$("#insert_sex").on("input",function () {
    checkInsertSex()
})
$("#insert_email").on("input",function () {
    checkInsertEmail()
})
$("#insert_phone").on("input",function () {
    checkInsertPhone()
})
function checkInsertAll() {
    let name = checkInsertName()
    let account = checkInsertAccount()
    let birthday = checkInsertBirthday()
    let sex = checkInsertSex()
    let mno = checkInsertMno()
    let email = checkInsertEmail()
    let phone = checkInsertPhone()
    return name && account && sex && mno && birthday && email && phone
}


