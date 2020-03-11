
$(function () {
//    获得所有控件
    $("#btn_signIn").click(function () {
        var phone = $("#text_phone").val();
        var password = $("#text_password").val();
        var rememberPassword = $("#remember_password").prop("checked") == true;    //获取复选框是否被选中
    //    封装成对象
        var data = new Object();
        data["phone"] = phone;
        data["password"] = password;
        data["rememberPassword"] = rememberPassword;
        //封装成json对象
        var signInVo = JSON.stringify(data);
        console.log(signInVo);
        $.ajax(
            {
                url: '/signIn/check',
                contentType: "application/json",
                type: 'post',
                dataType: 'json',
                data: signInVo
            }
        ).done(function (re) {
            if (re.success){
                window.location.href = "/";
            } else{
                alert(re.message);
            }
        });

    });
})