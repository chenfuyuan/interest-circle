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
        console.log("准备发送验证码");
        $.ajax({
            url: '/user/password/authCode/send',
            contentType: "application/json",
            type: 'GET',
            dataType: 'json',
            }
        ).done(function (re) {
            if (re.success){
                $.cookie("total", 60);
                timekeeping();
                alert("短信发送成功，请注意查收");
            } else{
                alert(re.message);
            }
        });
    });

    $("#btn-validate").click(function () {
        console.log("开始修改密码");
        var password = $("#input-password").val();
        var authCode = $("#input-authCode").val();


        if (authCode == "") {
            alert("验证码未输入");
            return;
        }

        if (password == "") {
            alert("密码未输入");
            return;
        }

        var vo = new Object();
        vo["password"] = password;
        vo["authCode"] = authCode;

        var data = JSON.stringify(vo);
        console.log(data);
        $.ajax({
            url: '/user/password/change/check',//服务器发送短信
            contentType: "application/json",
            type: 'post',
            dataType: 'json',
            data: data,
        }).done(function (re) {
            if (re.success) {
                //进行注册成功跳转
                alert("修改成功");
                //清除cookie
                $.cookie("total", total, {expires: -1});
                window.location.href = "/user/info";
                return;
            } else {
                alert(re.message);
            }
        });
    });
})