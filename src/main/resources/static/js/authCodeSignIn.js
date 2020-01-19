$(function () {
    if ($.cookie("total") != undefined && $.cookie("total") != 'NaN' && $.cookie("total") != 'null') {//cookie存在倒计时
        timekeeping();
    } else {//cookie 没有倒计时
        console.log("未倒计时");
        $('#btn_authCode').attr("disabled", false);
    }

    function timekeeping() {
        //把按钮设置为不可以点击
        console.log("按钮设置为不可以点击")
        $('#btn_authCode').attr("disabled", true);
        var interval = setInterval(function () {//每秒读取一次cookie
            //从cookie 中读取剩余倒计时
            total = $.cookie("total");
            //在发送按钮显示剩余倒计时
            $('#btn_authCode').text('请等待' + total + '秒');
            //把剩余总倒计时减掉1
            total--;
            if (total == 0) {//剩余倒计时为零，则显示 重新发送，可点击
                //清除定时器
                clearInterval(interval);
                //删除cookie
                total = $.cookie("total", total, {expires: -1});
                //显示重新发送
                $('#btn_authCode').text('重新发送');
                //把发送按钮设置为可点击
                $('#btn_authCode').attr("disabled", false);
            } else {//剩余倒计时不为零
                //重新写入总倒计时
                $.cookie("total", total);
            }
        }, 1000);
    }

    $("#btn_authCode").click(function () {
        //校验手机号码
        var phone = $('#text_phone').val();
        var pre = /^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$/;
        if (phone == '') {
            alert("手机号不能为空！")
            return this;
        } else {
            if (!pre.test(phone)) {
                alert("手机号格式错误！")
                return this;
            }
        }
        //与后台进行ajax
        $.ajax({
            url: 'sendSignInAuthCode',
            contentType: 'application/json',
            type: 'GET',
            dataType: 'json',
            data: {phone: phone},
        }).done(function (re) {
            //处理成功发送的数据
            console.log(re.phone);
            console.log(re.message);

            if (re.success) {
                $.cookie("total", 60);
                timekeeping();
                alert("短信发送成功，请注意查收");
            } else {
                alert(re.message);
            }
        }).fail(function () {
            console.log("error");
        }).always(function () {
                console.log("complete");
            });

    });


    $("#btn_signIn").click(function () {
        var phone = $("#text_phone").val();
        var authCode = $("#text_authCode").val();
        var rememberPassword = $("#remember_password").prop("checked") == true;    //获取复选框是否被选中

        //    封装成对象
        var data = new Object();
        data["phone"] = phone;
        data["authCode"] = authCode;
        data["rememberPassword"] = rememberPassword;

        //封装成json对象
        var signInVo = JSON.stringify(data);
        console.log(signInVo);
        $.ajax(
            {
                url: 'signInByAuthCode',
                contentType: "application/json",
                type: 'post',
                dataType: 'json',
                data: signInVo
            }
        ).done(function (re) {
            if (re.success) {
                $.cookie("total", total, {expires: -1});
                window.location.href = "/";
            } else {
                alert(re.message);
            }
        });

    });
})