$(function () {
    $("#btn-send").click(function () {
        //获取文本框内容
        var replyText = $("#replyText").val();
        console.log("回复的内容为："+replyText);

        //获取回复人id
        var replyId = $("replyText").data("rid");

        if (replyId == undefined || replyId == null || replyId=="") {
            replyId = 0;
        }

        var data = new Object();
        data["content"] = replyText;
        data["rid"] = replyId;
        data["aid"] = aid;

        var commentSaveVo = JSON.stringify(data);
        console.log("commentSaveVo = " + commentSaveVo);
        console.log("data = " + data);
        $.ajax({
            url: "/article/comment/save",
            contentType: "application/json",
            type: 'post',
            dataType: 'json',
            data: commentSaveVo
        }).done(function (re) {
            if (!re.success) {
                alert(re.message);
            }

            alert(re.message);
            //填充评论栏
            console.log("开始填充评论栏");


        });

    });


});