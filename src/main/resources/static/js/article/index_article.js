$(function () {

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


    //初始展示的帖子
    var sort = "publishTime";
    var type = "normal";
    var pageNum = 1;

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



    function fillHtml(articles) {
        var post_list = $(".post-lists");
        for (var i = 0; i < articles.length; i++) {
            var article = articles[i];
            var user = article.user;
            var circle = article.circle;
            var date = new Date(article.createTime).Format("yyyy-MM-dd hh:mm:ss");

            post_list.append("<a class='post-list-item'>"+
                "<div class='wrap'><div class='user-info'><div class='left-side'><div class='user-avatar'>" +
                "<img src='" + user.avatarPath + "' alt='' class='avatar-head'></div>" +
                "<div><p class='user-name'><span class=''>" + user.name + "</span></p>" +
                "<p class='post-time'>" + date + "</p></div></div><div>" +
                "<div class='right-side'><span class='more-dropdown-box more-dropdown'><div class='user-img'><i class='more-icon-option option-icon icon-btn glyphicon glyphicon-list'></i></div></span></div></div></div>" +
                "<div class='topic-text'><div style='left: 0px; position: static; width: 480px;'><h3>" + article.title + "</h3>" + article.content + "</div></div><div" +
                " isopenwindow='true'>" +
                "<div class='reactions'><span class='likes'><i class='more-icon-appreciation icon-btn glyphicon glyphicon-thumbs-up'></i><span class=''>赞</span></span>" +
                "             <span class='comments'>" +
                "<i class='more-icon-comment icon-btn glyphicon glyphicon-comment'></i>" +
                "                         <span class='comments-count'>评论</span>" +
                "                     </span>" +
                "             <span class='views-count'></span></div></div></div></a>");


        }
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
            fillHtml(articles);
        });
    }


    initTopic();
    getArticleList();

});