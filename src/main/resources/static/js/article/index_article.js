$(function () {
    //初始展示的帖子
    var sort = "create_time";
    var type = "normal";
    var pageNum = 1;


    function initTopic() {
        //初始化置顶栏
        $.get('/article/sticky/get?cid=' + cid, function (re) {
            console.log(re);
            var stickys = re;
            var topic = $(".topic-top");
            if (stickys == undefined || stickys == null || stickys.length == 0) {
                topic.hide();
                return;
            }

            //填充置顶栏的查看全部数额
            $("#sticky-num").html("查看全部 " + stickys.length);

            //填充置顶栏数据
            for (var i = 0; i < re.length && i < 3; i++) {
                var sticky = stickys[i];
                topic.append("<div class='topic-top-each unread'>" +
                    "<div class='topic-read'></div><div class='notice-content'>" + sticky.article.title + "</div></div>");
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


    var post_list = $(".post-lists");
    function fillHtml(articles) {
        for (var i = 0; i < articles.length; i++) {
            var article = articles[i];
            var user = article.user;
            var circle = article.circle;
            var date = new Date(article.createTime).Format("yyyy-MM-dd hh:mm:ss");
            var aid = article.id;
            var isLike = article.like;
            var src;
            if (isLike) {
                src = "/static/image/like.png";
            } else {
                src = "/static/image/noLike.png";
            }


            post_list.append("<a class='post-list-item'>"+
                "<div class='wrap' data-aid='"+aid+"'><div class='user-info'><div class='left-side'><div" +
                " class='user-avatar'>" +
                "<img src='" + user.avatarPath + "' alt='' class='avatar-head'></div>" +
                "<div><p class='user-name'><span class=''>" + user.name + "</span></p>" +
                "<p class='post-time'>" + date + "</p></div></div><div>" +
                "<div class='right-side'><span class='more-dropdown-box more-dropdown'><div class='user-img'><i class='more-icon-option option-icon icon-btn glyphicon glyphicon-list'></i></div></span></div></div></div>" +
                "<div class='topic-text'><div style='left: 0px; position: static; width: 480px;'><h3>" + article.title + "</h3>" + article.content + "</div></div><div" +
                " isopenwindow='true'>" +
                "<div class='reactions'><span class='likes' data-aid='"+aid+"'><img src='"+src+"'" +
                " class='more-icon-appreciation icon-btn'/><span" +
                " class=''>赞 <span class='likes-num'>"+article.likeNum+"</span></span></span>" +
                "             <span class='comments'>" +
                "<i class='more-icon-comment icon-btn glyphicon glyphicon-comment'></i>" +
                "                         <span class='comments-count'>评论 <span class='comments-num'>"+article.commentNum+"</span></span>" +
                "                     </span>" +
                "             <span class='views-count'></span></div></div></div></a>");
        }

        //判断是否被点过赞
        var aids = $(".likes");
        console.log("aids = " + aids);
        //点赞与取消点赞，事件
        $(".likes img").click(function () {
            var img_likes = $(this);
            var src = img_likes.attr("src");
            var aid = img_likes.parent().data("aid");
            console.log("点击了点赞按钮,aid="+aid);
            if (src == "/static/image/noLike.png") {
                $.get("/article/like/" + aid, function (re) {
                    if (re.success) {
                        img_likes.attr("src", "/static/image/like.png");

                        var likes_num = img_likes.next().children(".likes-num");
                        var num = parseInt(likes_num.html());
                        console.log("num=" + num);
                        likes_num.text(num +1);
                    }
                    console.log(re.message);

                });
            }else if (src == "/static/image/like.png") {
                $.get("/article/like/cancel/"+aid,function (re) {
                    if(re.success){
                        img_likes.attr("src", "/static/image/noLike.png");
                        var likes_num = img_likes.next().children(".likes-num");
                        var num = parseInt(likes_num.html());
                        console.log("num=" + num);
                        likes_num.text(num -1);
                    }
                    console.log(re.message);

                })

            }
        })
    }

    function getArticleList() {
        var data = new Object();
        data["sort"] = sort;
        data["type"] = type;
        data["pageNum"] = pageNum;
        data["cid"] = cid;
        var jsonData = JSON.stringify(data);    //转化成json数据
        $.ajax({
            url: '/article/get',//服务器发送短信
            contentType: "application/json",
            type: 'post',
            dataType: 'json',
            data: jsonData,
        }).done(function (re) {
            var articles = re.list;
            total = re.total;
            pageSize = re.pageSize;
            console.log("共有"+total+"个帖子,一页"+pageSize+"个")
            fillHtml(articles);
        });
    }


    initTopic();
    getArticleList();


    $(window).scroll(function(){
        var h=$(document.body).height();//网页文档的高度
        var c = $(document).scrollTop();//滚动条距离网页顶部的高度
        var wh = $(window).height(); //页面可视化区域高度

        if (Math.ceil(wh+c)>=h){
            console.log("已经到达浏览器底部，获取新的帖子");
            pageNum = pageNum + 1;
            if ((pageNum - 1) * pageSize >= total) {
                $("#end").show();
                return;
            } else {
                console.log("向后台请求数据");
                getArticleList();
            }


        }
    })


    $(".essence").click(function () {
        //清空数据
        console.log("切换为精华贴");
        post_list.html("");
        type = "essence";
        pageNum = 1;
        getArticleList();
    });

    $(".normal").click(function () {
        console.log("切换为动态");
        post_list.html("");
        type = "normal";
        pageNum = 1;
        getArticleList();

    });

    $("#sort").click(function () {
        $('#sort-menu').toggle();
    });

    $(".sort-menu-item").click(function () {
        $(".sort-menu-item .check-icon").remove();
        $(this).append("<img src='/static/image/checked.svg' class='check-icon'>");

        $(this).siblings(".sort-menu-item").remove("img");
        sort = $(this).data("sort");
        console.log("sort切换为" + sort);

        var text = $(this).children("span").text();

        //更换内容
        $("#sort").text(text);

        //清空帖子列表
        post_list.html("");
        pageNum = 1;
        getArticleList();

        //隐藏菜单
        $("#sort-menu").toggle();

    });


});