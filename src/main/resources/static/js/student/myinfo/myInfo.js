let $name = $("#name");
let $college = $("#college");
let $profession = $("#profession");
let $account = $("#account");
let $newcode1 = $("#newcode1");
let $newcode2 = $("#newcode2");
function init(){
    let url = "/student/myInfo";
    $.ajax({
        url: url,
        success:function (data) {
            if(data.code == 200){
                let teacher = data.data;
                $name.val(teacher.name);
                $account.val(teacher.account);
                $college.val(teacher.college);
                $profession.val(teacher.profession);
            }
        }
    })
}
init();
$(function () {
    $("#update").click(function () {
        let newcode1 = $newcode1.val().trim();
        let newcode2 = $newcode2.val().trim();
        if(newcode1 == newcode2 && newcode1.trim() != ""){
            $.ajax({
                url:"/student/updateMyInfo",
                data:{
                    account:$account.val(),
                    name:$name.val(),
                    code:newcode1
                },
                success:function (data) {
                    if(data.code == 200){
                        alert("修改成功");
                        $newcode1.val("")
                        $newcode2.val("")
                        window.location.reload();
                    }
                }
            })
        }else{
            alert("密码不一致");
        }
    })
})