$(function () {
    initialUserName = $("#userName").val();


    $("#btn-avatar").click(function () {
        $("#input-avatar").click();
    });
    $("#userName").change(function () {
        var btn_save = $("#btn-save");
        var name = $(this).val();

        if (name == initialUserName) {
            console.log("变为不可点击");
            btn_save.attr("disabled",true);
            btn_save.css("color", "#9b9b9b");
        }else{
            console.log("变为可点击");
            btn_save.attr("disabled",false);
            btn_save.css("color", "#ff3049");
        }
    });

    $("#input-avatar").change(function () {
        $("#btn-save").css("color","#ff3049");
        $("#btn-save").attr("disabled",false);
        console.log("选择图片");
        var name = this.value;
        var fileName = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
        if (fileName != "jpg" && fileName != "jpeg" && fileName != "png" && fileName != "gif" && fileName != "bmp") {
            alert("您上传的不是图片请重新上传");
            $("#input-avatar").val("");    //将上传文件设置为空
            $("#img-avatar").attr("src", "");    //将头像设置为初始头像
            $("#img-avatar").css("z-index", "0");
            return false;
        }

        //判断文件是否上传过大
        if (this.files[0].size > 10 * 1024 * 1024) {
            alert("图片不能大于10M");
            $("#input-avatar").val("");    //将上传文件设置为空
            $("#img-avatar").attr("src", "");    //将头像设置为初始头像
            $("#img-avatar").css("z-index", "0");
            return false;
        }
        //更换头像
        var img_avatar = $("#img-avatar");
        img_avatar.attr("src", getObjectURL(this.files[0]));    //将头像设置为初始头像
        img_avatar.css("z-index", "3");

    });

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


    /*检验并提交保存*/
    $("#btn-save").click(function () {


        //对数据进行检查
        var avatar = $("#input-avatar").val();
        console.log(avatar);
        var name = $("#userName").val();
        console.log(name);
        if (name == "") {
            alert("用户名未输入");
            return false;
        }

        if (initialUserName == name && (avatar == null || avatar =="")) {
            return false;
        }

        console.log("提交事件触发");
        alert("提交事件触发");

        $("#user-info").ajaxSubmit(
            {
                url: "/user/info/check",
                type: "post",
                dataType: "json",
                success: function (data) {
                    alert(data.message);
                    var btn_save = $("#btn-save");
                    btn_save.css("color","#9b9b9b");
                    btn_save.attr("disabled",true);
                    initialUserName = name;
                    console.log("按钮变成不可点击");
                },
                cleanForm: false,
                resetForm: false,
            }
        )
        return false;


    });

    $("#btn-changePassword").click(function () {
        window.location.href = "/user/password/change";
    });


    $("#item-user-info").click(function () {
        var isChoice = $(this).hasClass("nuxt-link-active");
        if (!isChoice) {
            window.location.href = "/user/info";
        }
    });

    $("#item-user-myArticle").click(function () {
        var isChoice = $(this).hasClass("nuxt-link-active");
        if (!isChoice) {
            window.location.href = "/user/myArticle";
        }
    });

    $("#item-user-myStar").click(function () {
        var isChoice = $(this).hasClass("nuxt-link-active");
        if (!isChoice) {
            window.location.href = "/user/myStar";
        }
    });

    $("#item-user-myLike").click(function () {
        var isChoice = $(this).hasClass("nuxt-link-active");
        if (!isChoice) {
            window.location.href = "/user/myLike";
        }
    });
})