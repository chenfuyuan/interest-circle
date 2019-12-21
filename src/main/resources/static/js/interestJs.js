$(function () {
    if ($.cookie("total") != undefined && $.cookie("total") != 'NaN' && $.cookie("total") != 'null') {//cookie存在倒计时
        timekeeping();
    } else {//cookie 没有倒计时
        console.log("未倒计时");
        $('#authCode').attr("disabled", false);
    }

    function timekeeping() {
        //把按钮设置为不可以点击
        console.log("按钮设置为不可以点击")
        $('#authCode').attr("disabled", true);
        var interval = setInterval(function () {//每秒读取一次cookie
            //从cookie 中读取剩余倒计时
            total = $.cookie("total");
            //在发送按钮显示剩余倒计时
            $('#authCode').text('请等待' + total + '秒');
            //把剩余总倒计时减掉1
            total--;
            if (total == 0) {//剩余倒计时为零，则显示 重新发送，可点击
                //清除定时器
                clearInterval(interval);
                //删除cookie
                total = $.cookie("total", total, {expires: -1});
                //显示重新发送
                $('#authCode').text('重新发送');
                //把发送按钮设置为可点击
                $('#authCode').attr("disabled", false);
            } else {//剩余倒计时不为零
                //重新写入总倒计时
                $.cookie("total", total);
            }
        }, 1000);
    }

    //绑定发送按钮
    $('#authCode').click(function (event) {
        /* Act on the event */
        //校验手机号码
        var phone = $('#phone').val();
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
})