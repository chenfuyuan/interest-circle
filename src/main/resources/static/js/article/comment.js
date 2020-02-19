$(function () {
    var pageNum = 1;
    var pageSize = 10;
    menu = $(".reply-detail-wrapper");
    function addOnclickListener() {
        //回复按钮点击事件 将回复人姓名传递给输入框，并让输入框显示
        $(".btn-reply").unbind("onclick");
        $(".btn-reply").click(function () {
            var thisBtn = $(this);
            var replyName = thisBtn.data("username");
            console.log("点击回复");
            replyInputStr = "<div class='child-reply-wrapper' style='max-height: 200px;'><div><div" +
                " class='reply-footer page-footer post-page-footer' id='reply-foot-page'> <div class='more-remind'>" +
                " <input" +
                " id='reply-input'" +
                " class='more-remind-input'" +
                "                                                               placeholder='评论'/></div><div" +
                " class='reply-btn'><button class='more-btn' id='btn-send-reply'>发送</button></div></div></div></div>";
            var replyDetail = thisBtn.parents(".comment-details");
            $(".reply-details .child-reply-wrapper").remove();
            replyDetail.append(replyInputStr);
            console.log(replyDetail);
            var replyInput = $("#reply-input");
            console.log("replyName = " + replyName);
            replyInput.attr("placeholder","回复 : "+ replyName);

            var acid = thisBtn.data("acid");
            var ruid = thisBtn.data("ruid");
            console.log("ruid = " + ruid);
            replyVo = new Object();
            replyVo["acid"] = acid;
            replyVo["ruid"] = ruid;


            if (thisBtn.hasClass("btn-reply-comment")) {
                replyVo["type"] = 1;
            } else if (thisBtn.hasClass("btn-reply-reply")) {
                replyVo["type"] = 2;
            }

            console.log("1 replyVo = "+replyVo)


            //添加发送回复监听器
            $("#btn-send-reply").unbind("onclick");
            $("#btn-send-reply").click(function () {
                console.log("点击发送");
                console.log("replyVo = " + replyVo);
                var replyText = $("#reply-input").val();
                replyVo["content"] = replyText;
                replyVo["aid"] = aid;
                console.log("replyVo = " + replyVo);
                var data = JSON.stringify(replyVo);
                console.log("data = " + data);
                var acid = replyVo["acid"];
                console.log("acid = " + acid);
                $.ajax({
                    url: "/article/reply/save",
                    contentType: "application/json",
                    type: 'post',
                    dataType: 'json',
                    data:data,
                }).done(function (re) {
                    console.log("reply.id = "+re.id);
                    console.log("开始填充回复条");
                    var reply_item = $(".reply-item[data-acid='" + acid + "']");
                    var replyMenu = reply_item.find(".child-reply-box");
                    replyMenu.show();
                    addReply(re, replyMenu);
                    $(".child-reply-wrapper").hide();
                });
            });
        });

        $(".btn-delete").unbind("click");
        $(".btn-delete").click(function () {
            console.log("点击删除")
            var thisBtn = $(this);
            var deleteReplyVo = new Object();
            var acid = thisBtn.data("acid");
            deleteReplyVo["acid"] = acid;
            deleteReplyVo["aid"] = aid;
            if (thisBtn.hasClass("btn-delete-comment")) {
                console.log("删除评论 = " + acid);
                var data = JSON.stringify(deleteReplyVo);
                $.ajax({
                    url: "/article/comment/delete",
                    contentType: "application/json",
                    type: 'post',
                    dataType: 'json',
                    data: data
                }).done(function (re) {
                    if (re.success) {
                        alert(re.message);
                        $(".reply-item[data-acid='" + acid + "']").remove();
                        total -= 1;
                    } else {
                        alert(re.message);
                    }
                });
            } else if (thisBtn.hasClass("btn-reply-delete")) {
                var rid = thisBtn.data("rid");
                deleteReplyVo["rid"] = rid;
                console.log("删除回复 = "+rid);
                var data = JSON.stringify(deleteReplyVo);
                console.log("deleteVo = " + data);

                $.ajax({
                    url: "/article/reply/delete",
                    contentType: "application/json",
                    type: "post",
                    dataType: "json",
                    data: data
                }).done(function (re) {
                    if (re.success) {
                        alert(re.message);
                        var replyItem = $(".child-reply-item[data-rid='" + rid + "']");
                        var replyMenu = replyItem.parents(".child-reply-box");
                        replyItem.remove();
                        console.log("html = "+replyMenu.html());
                        console.log("text = "+replyMenu.text());
                        if (replyMenu.html() =="") {
                            replyMenu.hide();
                        }
                    } else {
                        alert(re.message);
                    }

                });

            }

        });
    }

    // 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")   ==> 2006-7-2 8:9:4.18
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }


    function addReply(reply,replysMenu) {
        var user = reply.user;
        var replyUser = reply.replyUser;
        var type = reply.type;
        var replyName = "";
        var replyTime = new Date(reply.createTime).Format("yyyy-MM-dd hh:mm:ss");
        var content = reply.content;
        var rid = reply.id;
        var ruid = user.id;
        var acid = reply.acid;
        var replysMenuItem = "<div class='reply-item child-reply-item' data-rid='"+rid+"'><div" +
            " class='user-avatar'><img" +
            " src='"+user.avatarPath+"' alt='' class='avatar-head'></div><div class='reply-details reply-d'><div" +
            " class='reply-header'><div><span class='user-name'>"+user.name+"</span><span class='topic-time'>"+replyTime+"</span></div></div> <div class='reply-body'><!----> <span>"+content+"</span></div> <!----> <div class='reply-footer'><span class='text-btn btn-reply btn-reply-reply' data-username='"+user.name+"' data-acid='"+acid+"' data-ruid='"+ruid+"'>回复</span> <b class='seperater-line'></b> <span class='text-btn btn-reply-delete btn-delete' data-acid='"+acid+"' data-rid='"+rid+"'>删除</span></div></div></div>";
        replysMenu.append(replysMenuItem);
        if (type != 1) {
            replyName = replyUser.name;
            var replyBody = $(".reply-item[data-rid='" + rid + "'] .reply-body");
            replyBody.prepend("<span>回复<span class='user-name'>"+replyName+"</span> :</span>");
        }

        addOnclickListener();



    }

    function addComment(comment,menu) {
        var acid = comment.id;
        var commentUser = comment.user;
        var content = comment.content;
        var replyNum = comment.replyNum;
        var ruid = commentUser.id;
        var commentDate = new Date(comment.createTime).Format("yyyy-MM-dd hh:mm:ss");
        var item = "<div class='reply-item parent-reply-item' data-acid='"+acid+"'>" +
            "<div class='user-avatar'>" +
            "<img src='" + commentUser.avatarPath + "' alt='' class='avatar-head'></div><div " +
            "class='reply-details comment-details'>" +
            "<div class='reply-header'><div>" +
            "<span class='user-name'>" + commentUser.name + "</span> <span class='topic-time'>" + commentDate + "</span></div></div>" +
            "<div class='reply-body'>"+content+"</div>" +
            "<div class='reply-footer'>" +
            "<span class='text-btn btn-reply-comment btn-reply' data-userName='"+commentUser.name+"' data-acid='"+acid+"' data-ruid='"+ruid+"'>回复" +
            " <span" +
            " class='reply-num'>" + replyNum + "</span></span>" +
            "<span class='seperater-line'></span>" +
            "<!--<span class='icon-btn more-icon-appreciation'>" +
            "<img class='like-img' src='/static/image/noLike.png' alt=''>" +
            "<span>赞</span></span><span class='seperater-line'></span>-->" +
            "<span class='text-btn btn-delete-comment btn-delete' data-acid='"+acid+"'>删除</span></div><div" +
            " class='child-reply-box'" +
            " style='display:none'></div></div></div>" +
            "";
        return item;
    }

    function getComments() {
        $.get("/article/comments/get/" + aid + "?pageNum="+pageNum, function (pageInfo) {
            var comments = pageInfo.list;
            total = pageInfo.total;
            console.log("total = " + total);
            //填充
            for (var i = 0; i < comments.length; i++) {
                var comment = comments[i];
                var item = addComment(comment, menu);
                menu.append(item);
                var replys = comment.replys;
                var acid = comment.id;
                var thisReplyItem = $(".reply-item[data-acid='" + acid + "']");
                if (replys!=undefined && replys != null && replys.length != 0) {
                    var replysMenu = thisReplyItem.find(".child-reply-box");
                    replysMenu.show();
                    for (var j = 0; j<replys.length;j++){
                        var reply = replys[j];
                        addReply(reply, replysMenu);
                    }
                }
            }
            addOnclickListener();
        })
    }

    function initComments() {
        menu.children(".reply-item").remove();
        getComments();
    }


    initComments();


    $("#btn-send").click(function () {
        //获取文本框内容
        var replyText = $("#replyText").val();
        console.log("回复的内容为：" + replyText);

        //获取回复人id
        var replyId = $("replyText").data("rid");

        if (replyId == undefined || replyId == null || replyId == "") {
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
        }).done(function (comment) {
            if (comment != null) {
                console.log(comment);
            }
            //填充评论栏
            console.log("开始填充评论栏");
            var item = addComment(comment,menu);
            menu.prepend(item);
            addOnclickListener();
        });

    });


    //判断浏览器是否在最底部
    $(window).scroll(function(){
        var h=$(document.body).height();//网页文档的高度
        var c = $(document).scrollTop();//滚动条距离网页顶部的高度
        var wh = $(window).height(); //页面可视化区域高度

        if (Math.ceil(wh+c)>=h){
            console.log("已经到达浏览器底部，获取新的评论");
            pageNum = pageNum + 1;
            if ((pageNum - 1) * pageSize >= total) {
                $("#end").show();
                return;
            } else {
                console.log("向后台请求数据");
                getComments();
            }


        }
    })

});