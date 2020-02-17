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

    function initComment() {
        $.get("/article/comments/get/"+aid+"?pageNum=1",function (comments) {
            console.log(comments);
            //填充

            var str = "<div class='reply-item parent-reply-item'><div class='user-avatar'>" +
                "<img src='/static/image/avatar.png' alt='' class='avatar-head'>" +
                "</div><div class='reply-details'><div class='reply-header'>" +
                "                                                            <div><span class='user-name'>YY</span> <span class='topic-time'>2月14日 23:14</span>" +
                "                                                            </div>" +
                "                                                        </div>" +
                "                                                        <div class='reply-body'>wfasfsafasfsafasfaf</div>" +
                "                                                        <div class='reply-footer'>" +
                "                                                            <span class='text-btn'>回复</span>" +
                "                                                            <span class='seperater-line'></span>" +
                "                                                            <span class='icon-btn more-icon-appreciation'>" +
                "                                                                <img class='like-img' src='/static/image/noLike.png' alt=''>" +
                "                                                                <span>赞</span>" +
                "                                                            </span>" +
                "                                                            <span class='seperater-line'></span>" +
                "                                                            <span class='text-btn'>删除</span>" +
                "                                                        </div>" +
                "                                                    </div>" +
                "                                                </div>"
        })
    }
    initComment();

});