$(function () {
    /*同意保存*/
    function agreeUpdate() {
        $(".el-button").removeClass("is-disabled");
        console.log("设置能点击");
        $(".el-button").removeAttr("disabled");

    }

    /*禁止保存*/
    function opposeUpdate() {
        $(".el-button").addClass("is-disabled");
        console.log("设置不能点击");
        $(".el-button").attr("disabled", "disabled");
    }

    /**
     * 初始化圈子数据
     */
    function initCircleMessage() {
        circleName = $("#text-circle-name").val().trim();
        circleIntroduce = $("#text-circle-introduce").val();
        circleBgdUrl = $("#img-circle-bgd").css("background-image").replace('url(\"', '').replace('\")', '');
        circleAvatarSrc = $("#img-circle-avatar").attr("src");
        isChange = false;
        console.log("圈子名字 = " + circleName);
        console.log("圈子简介 = " + circleIntroduce);
        console.log("圈子背景 = " + circleBgdUrl);
        console.log("圈子头像 = " + circleAvatarSrc);
        opposeUpdate();
    }


    initCircleMessage();

    function checkChange() {
        var nowName = $("#text-circle-name").val().trim();
        var nowIntroduce = $("#text-circle-introduce").val().trim();
        if (circleName != nowName || circleIntroduce != nowIntroduce) {
            console.log("圈子内容有变化");
            isChange = true;
        }

    }


    //添加文本框改变监听，改变按钮状态
    $("#text-circle-name").change(function () {
        //获取输入框内容
        var changeName = $(this).val().trim();
        //判断是否变化
        if (changeName == circleName) {//判断内容有无变化
            console.log("圈子姓名无变化");
            console.log("检测其他内容是否变化");
            //判断其他内容有无变化
            checkChange();
            if (!isChange) {
                //内容无变化，使保存按钮不可以被点击
                opposeUpdate();
            }
        } else {
            console.log("圈子姓名变化");
            agreeUpdate();
        }
    });

    $("#text-circle-introduce").change(function () {
        //获取文本框内容
        var changeIntroduce = $(this).val().trim();
        //判断是否变化
        if (changeIntroduce == circleIntroduce) {
            //判断内容有无变化
            console.log("圈子姓名无变化");
            console.log("检测其他内容是否变化");
            //判断其他内容有无变化
            checkChange();
            if (!isChange) {
                //内容无变化，使保存按钮不可以被点击
                opposeUpdate();
            }
        } else {
            console.log("圈子姓名变化");
            agreeUpdate();
        }
    });

    /*获取url*/
    function getObjectURL(file) {
        var url = null;
        if (window.createObjectURL != undefined) { // basic
            url = window.createObjectURL(file);
        } else if (window.URL != undefined) { // mozilla(firefox)
            url = window.URL.createObjectURL(file);
        } else if (window.webkitURL != undefined) { // webkit or chrome
            url = window.webkitURL.createObjectURL(file);
        }
        return url;
    }

    //上传封面
    $("#upload-img-bgd").change(function () {
        //获取文件名
        var name = this.value;
        //获取文件类型
        var fileType = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        if (fileType != "jpg" && fileType != "jpeg" && fileType != "png" && fileType != "gif" && fileType != "bmp") {
            alert("您上传的不是图片请重新上传");
            $("#upload-img-bgd").val("");    //将上传文件设置为空
            $("#img-circle-bgd").css("backgroundImage", "url(" + circleBgdUrl + ")");
            return false;
        }

        //判断文件是否上传过大
        if (this.files[0].size > 10 * 1024 * 1024) {
            alert("图片不能大于10M");
            $("#upload-img-bgd").val("");    //将上传文件设置为空
            $("#img-circle-bgd").css("backgroundImage", "url(" + circleBgdUrl + ")");
            return false;
        }

        //更换背景图片
        var circleBgdDiv = $("#img-circle-bgd");
        var changeBgdUrl = getObjectURL(this.files[0]);
        console.log("上传的url = " + changeBgdUrl);
        circleBgdDiv.css("backgroundImage", "url(" + changeBgdUrl + "");

        agreeUpdate();

    });

    //上传头像
    $("#upload-img-avatar").change(function () {
        //获取文件名
        var name = this.value;
        //获取文件类型
        var fileType = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        if (fileType != "jpg" && fileType != "jpeg" && fileType != "png" && fileType != "gif" && fileType != "bmp") {
            alert("您上传的不是图片请重新上传");
            $("#upload-img-avatar").val("");    //将上传文件设置为空
            $("#img-circle-avatar").src(circleAvatarSrc);
            return false;
        }

        //判断文件是否上传过大
        if (this.files[0].size > 10 * 1024 * 1024) {
            alert("图片不能大于10M");
            $("#upload-img-avatar").val("");    //将上传文件设置为空
            $("#img-circle-avatar").src(circleAvatarSrc);
            return false;
        }

        //更换背景图片
        var circleAvatarImage = $("#img-circle-avatar");
        var changeAvatarSrc = getObjectURL(this.files[0]);
        console.log("上传的url = " + circleAvatarSrc);
        circleAvatarImage.attr("src", changeAvatarSrc);

        agreeUpdate();
    });

    /*撤销修改*/
    $("#btn-setting-cancel").click(function () {
        //所有信息恢复到最初信息
        console.log("更改信息");
        $("#text-circle-name").val(circleName);
        $("#text-circle-introduce").val(circleIntroduce);
        $("#img-circle-bgd").css("backgroundImage", "url(" + circleBgdUrl + ")");
        $("#img-circle-avatar").attr("src", circleAvatarSrc);
        opposeUpdate();
    });

    /*上传修改数据*/
    $("#btn-setting-save").click(function () {
        var name = $("#text-circle-name").val();
        if (name == "") {
            alert("圈子名为空，请输入");
            return false;
        }

        $("#form-circle").ajaxSubmit({
            url: "/circle/setting/save",
            type: "post",
            dataType: "json",
            success: function (data) {
                alert(data.message);
                initCircleMessage();
            },
            cleanForm: false,
            resetForm: false,
        })
    });


});