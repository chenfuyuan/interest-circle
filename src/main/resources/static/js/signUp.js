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

    //绑定发送按钮
    $('#btn_authCode').click(function (event) {
        /* Act on the event */
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
        $.ajax({
            url: 'authCode',//服务器发送短信
            contentType: "application/json",
            type: 'GET',
            dataType: 'json',
            data: {phone: phone},
        })
                .done(function (re) {
                    console.log(re.phone);
                    console.log(re.message);

                    if (re.success){
                        $.cookie("total", 60);
                        timekeeping();
                        alert("短信发送成功，请注意查收");
                    } else{
                        alert(re.message);
                    }

            })
            .fail(function () {
                console.log("error");
            })
            .always(function () {
                console.log("complete");
            });
    });

    $("#btn_signUp").click(function () {
        var input = $("input");    //获取输入数据
        var pass = true;    //用于标识注册信息是否通过
        //检测信息是否完整
        var user = new Object();    //用于封装输入框的值
        //遍历 是否输入框是否都输入数据
        input.each(function () {
            var name = $(this).attr("name");    //获取name
            var value = $(this).val();    //获取输入框内容
            if (value==""){
                pass = false;
                return;
            }
            user[name] = value;    //封装数据
        });
        console.log(user);
        if (pass == false) {
            alert("信息未填写完整");
            return;
        }
        var phone = $("#text_phone").val();
        var pre = /^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$/;
        if (!pre.test(phone)) {
            alert("手机号格式错误！")
            return this;
        }
        var password = $("#text_password").val();
        var rpassword = $("#text_confirm").val();
        if(password!=rpassword){
            alert("两次输入密码不一致");
            return this;
        }

        var data = JSON.stringify(user);    //转化成json数据
        $.ajax({
            url: 'checkSignUp',//服务器发送短信
            contentType: "application/json",
            type: 'post',
            dataType: 'json',
            data: data,
        }).done(function (re) {
            if (re.success) {
                //进行注册成功跳转
                alert("注册成功");
                window.location.href = "signin?username"+re.phone;
                return;
            } else {
                alert(re.message);
            }
        });

    });
})