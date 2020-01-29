$(function () {
    $("#province").change(function () {
            //获取被选中的省份id
            var provinceValue = $(this).find("option:selected").val();
            if (provinceValue == 0) {
                $("#city").html("");
                $("#city").append("<option value='0'>请选择城市</option>");
                return;
            }
            $.ajax(
                {
                    url: 'getCity/' + provinceValue,
                    contentType: "application/json",
                    type: 'get',
                    dataType: 'json',
                }).done(function (re) {
                    $("#city").html("")
                    $("#city").append("<option value='0'>请选择城市</option>")
                    for (var i = 0; i < re.length; i++) {
                        $("#city").append("<option value='" + re[i].id + "'>" + re[i].name + "</option>")
                    }
                }
            );
        }
    );

    $("#select_circle_img").change(function () {
        console.log("选择图片");
        var name = this.value;
        var fileName = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        if(fileName !="jpg" && fileName!="jpeg" && fileName !="png" && fileName!="gif" && fileName !="bmp"){
            alert("您上传的不是图片请重新上传");
            $("#select_circle_img").val("");    //将上传文件设置为空
            $("#circle_img").attr("src", "image/avatar.png");    //将头像设置为初始头像
            $("#circle_img").css("z-index","0");
            return false;
        }

        //判断文件是否上传过大
        if (this.files[0].size>10*1024*1024){
            alert("图片不能大于10M");
            $("#select_circle_img").val("");    //将上传文件设置为空
            $("#circle_img").attr("src", "image/avatar.png");    //将头像设置为初始头像
            $("#circle_img").css("z-index","0");
            return false;
        }
        //更换头像
        var circle_img = $("#circle_img");
        circle_img.attr("src", getObjectURL(this.files[0]));    //将头像设置为初始头像
        circle_img.css("z-index","3");
    })

    $("#circle_img").click(function () {
        $("#select_circle_img").click();
    });


    function getObjectURL(file) {
        var url = null ;
        if (window.createObjectURL!=undefined) { // basic
            url = window.createObjectURL(file) ;
        } else if (window.URL!=undefined) { // mozilla(firefox)
            url = window.URL.createObjectURL(file) ;
        } else if (window.webkitURL!=undefined) { // webkit or chrome
            url = window.webkitURL.createObjectURL(file) ;
        }
        return url ;
    }
    function checkCircleMessage() {

    }

    $("#btn_create").click(function () {

        console.log("提交事件触发");
        alert("提交事件触发");
        $("#create-circle-form").ajaxSubmit(
            {
                url: "createCircle",
                type:"post",
                dataType:"json",
                success:function (data) {
                    alert("message = " + data.message);
                },
                cleanForm: false,
                resetForm: false,
            }
        )
        return false;
    });

})