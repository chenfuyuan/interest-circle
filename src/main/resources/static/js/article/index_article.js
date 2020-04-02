$(function () {
    //初始展示的帖子
    sort = "create_time";
    type = "normal";
    pageNum = 1;
    search = "";

    function initTopic() {
        //初始化置顶栏
        $.get('/article/sticky/get?cid=' + cid, function (re) {
            $(".topic-top-each").remove();
            console.log("初始化置顶栏");
            console.log(re);
            var stickys = re;

            var topic = $(".topic-top");
            if (stickys == undefined || stickys == null || stickys.length == 0) {
                topic.hide();
                return;
            }
            topic.show();


            //填充置顶栏的查看全部数额
            $("#sticky-num").text(stickys.length);

            //填充置顶栏数据
            for (var i = 0; i < re.length && i < 3; i++) {
                var sticky = stickys[i];
                topic.append("<div class='topic-top-each unread' data-aid='"+sticky.id+"'>" +
                    "<div class='topic-read'></div><div class='notice-content'>" + sticky.title + "</div></div>");
            }

            $(".topic-top-each").unbind("click");
            $(".topic-top-each").click(function () {
                var aid = $(this).data("aid");
                window.location.href = "/article/detail/"+aid+"?pageNum="+cPageNum;
            });

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
            var isSticky = article.sticky == 1;
            var isEssence = article.type == 2;
            var stickyItemStr = "置顶";
            var essenceItemStr = "加精";
            var isStar = article.star;
            var starItemStr = "收藏";
            if (isSticky) {
                stickyItemStr = "取消置顶";
            }

            if (isEssence) {
                essenceItemStr = "取消加精";
            }
            var src;
            if (isLike) {
                src = "/static/image/like.png";
            } else {
                src = "/static/image/noLike.png";
            }
            var starSrc;
            if (isStar) {
                starItemStr = "取消收藏";
                starSrc = "/static/image/articleStar.svg";
            } else {
                starItemStr = "收藏";
                starSrc = "/static/image/articleNoStar.svg";
            }

            var dropmenuStart ="<div class='more-dropdown-menu topic-cell-dropdown-menu' style='display: none'><div" +
                " class='more-dropdown-item-list' data-aid='"+aid+"'>";
            var dropmenuEnd = "</div></div>";
            var dropmenu = dropmenuStart;
            //生成菜单
        // <div class='more-dropdown-item'><span>修改标签</span></div> <div class='more-dropdown-item'><span>收藏</span></div> <!----> <div class='more-dropdown-item'><span>删除话题</span></div>
            //如果用户的权限为管理员,生成管理员的菜单，置顶，加精，
            if (identity == 1 || identity == 2) {
                var adminMenuItem = "";
                //置顶
                adminMenuItem+="<div class='more-dropdown-item item-sticky'><div class='handle-icon-wrapper'><img" +
                " src='/static/image/adminSticky.svg' class='handle-icon'></div> <span>"+stickyItemStr+"</span></div>";
                //加精
                adminMenuItem += "<div class='more-dropdown-item item-essence'><div class='handle-icon-wrapper'><img" +
                    " src='/static/image/adminEssence.svg' class='handle-icon'></div> <span>"+essenceItemStr+"</span></div>";
                dropmenu += adminMenuItem;
            }
            //收藏
            dropmenu+="<div class='more-dropdown-item item-star'><div class='handle-icon-wrapper'><img" +
                " src='"+starSrc+"' class='handle-icon'></div> <span>"+starItemStr+"</span></div>";
            if(user.id ==uid || identity == 1 || identity == 2) {
                //如果是发帖人，显示删除菜单
                dropmenu += "<div class='more-dropdown-item item-delete'><div class='handle-icon-wrapper'><img" +
                    " src='/static/image/articleDelete.svg' class='handle-icon'></div> <span>删除帖子</span></div>";
            }else{
                //如果不是发帖人，显示举报
                dropmenu+="<div class='more-dropdown-item item-report'><div class='handle-icon-wrapper'><img" +
                    " src='/static/image/articleReport.svg' class='handle-icon'></div> <span>举报</span></div>";
            }

            dropmenu += dropmenuEnd;
            console.log(dropmenu);
            var str = "<div class='post-list-item' data-aid='" + aid + "'><div class='wrap' data-aid='" + aid + "'><div class='user-info'><div class='left-side'><div class='user-avatar'><img src='" + user.avatarPath + "' alt='' class='avatar-head'></div>" +
                "<div><p class='user-name'><span class=''>" + user.name + "</span></p>" +
                "<p class='post-time'>" + date + "</p></div></div><div>" +
                "<div class='right-side'><span class='more-dropdown-box more-dropdown'><div" +
                " class='user-img'><div class='article-operation-menu-btn'><i" +
                " class='more-icon-option option-icon icon-btn glyphicon glyphicon-list'></i></div>" + dropmenu + "</div></span></div></div></div>" +
                "<div class='topic-text'><div style='left: 0px; position: static; width: 480px;'><h3>" + article.title + "</h3>" + article.content + "</div></div><div" +
                " isopenwindow='true'>" +
                "<div class='reactions'><span class='likes' data-aid='" + aid + "'><img src='" + src + "'" +
                " class='more-icon-appreciation icon-btn'/><span" +
                " class=''>赞 <span class='likes-num'>" + article.likeNum + "</span></span></span>" +
                "             <span class='comments'>" +
                "<i class='more-icon-comment icon-btn glyphicon glyphicon-comment'></i>" +
                "                         <span class='comments-count'>评论 <span class='comments-num'>" + article.commentNum + "</span></span>" +
                "                     </span>" +
                "             <span class='views-count'></span></div></div></div></div>";


            post_list.append(str);

        }


        addArticleClickListener();

    }
    function addArticleClickListener() {

        //点赞与取消点赞，事件
        $(".likes img").unbind("click");
        $(".likes img").click(function () {
            var img_likes = $(this);
            var src = img_likes.attr("src");
            var aid = img_likes.parent().data("aid");
            console.log("点击了点赞按钮,aid="+aid);
            if (src == "/static/image/noLike.png") {
                $.get("/article/like/" + aid+"?cid="+cid, function (re) {
                    if (re.success) {
                        img_likes.attr("src", "/static/image/like.png");
                        var likes_num = img_likes.next().children(".likes-num");
                        var num = parseInt(likes_num.html())+1;
                        console.log("num=" + num);
                        likes_num.text(num);
                    }
                    console.log(re.message);

                });
            }else if (src == "/static/image/like.png") {
                $.get("/article/like/cancel/"+aid+"?cid="+cid,function (re) {
                    if(re.success){
                        img_likes.attr("src", "/static/image/noLike.png");
                        var likes_num = img_likes.next().children(".likes-num");
                        var num = parseInt(likes_num.html())-1;
                        console.log("num=" + num);
                        likes_num.text(num);
                    }
                    console.log(re.message);

                })

            }
        })


        //帖子操作菜单
        $(".article-operation-menu-btn").unbind("click");
        $(".article-operation-menu-btn").click(function () {
            $(this).siblings(".more-dropdown-menu").toggle();
            var list = $(this).parents(".post-list-item");
            list.siblings().find(".more-dropdown-menu").hide();
        });
        //各个操作

        //点击帖子置顶项
        $(".item-sticky").unbind("click");
        $(".item-sticky").click(function () {
            var stickyBtn = $(this);
            var aid = stickyBtn.parent().data("aid");
            console.log("要置顶的帖子是"+aid);
            var itemStickySpan = stickyBtn.children("span");
            var itemStickyStr = itemStickySpan.text();
            console.log("itemStickyStr=" + itemStickyStr);
            if (itemStickyStr == "置顶") {
                $.get("/article/sticky/" + aid+"?cid="+cid, function (sticky) {
                    if (sticky == null || sticky == undefined) {
                        alert("置顶失败");
                    }
                    alert("置顶成功");
                    itemStickySpan.text("取消置顶");
                    $(".topic-top").show();
                    var size = $(".topic-top-each").length;

                    if (size < 3) {
                        $(".topic-top").append("<div class='topic-top-each unread'data-aid='"+sticky.id+"'>" +
                            "<div class='topic-read'></div><div class='notice-content'>" + sticky.title + "</div></div>");}
                    $(".topic-top-each").unbind("click");
                    $(".topic-top-each").click(function () {
                        var aid = $(this).data("aid");
                        window.location.href = "/article/detail/"+aid+"?pageNum="+cPageNum;
                    });
                    var stickyNum = parseInt($("#sticky-num").html())+1;
                    console.log("置顶后，置顶帖子数=" + stickyNum);
                    $("#sticky-num").text(stickyNum);
                });
            } else {
                $.get("/article/sticky/cancel/"+aid+"?cid="+cid,function (re) {
                    if (!re.success) {
                        alert(re.message);
                    }
                    alert(re.message);
                    //删除该条置顶记录
                    itemStickySpan.text("置顶");
                    $(".topic-top-each[data-aid='" + aid + "']").remove();
                    var num = parseInt($("#sticky-num").html())-1;
                    $("#sticky-num").text(num);
                    if (num < 1) {
                        $(".topic-top").hide();
                    }

                    var size = $(".topic-top-each").length;
                    if (size < 3 && num>=3) {
                        initTopic();
                    }
                })
            }

            stickyBtn.parents(".more-dropdown-menu").hide();
        });

        //点击帖子加精项
        $(".item-essence").unbind("click");
        $(".item-essence").click(function () {
            var essenceBtn = $(this);
            var aid = essenceBtn.parent().data("aid");

            //获取加精列表项的文字，判断是点赞，还是取消点赞
            var essenceSpan = essenceBtn.children("span");
            var essenceSpanStr = essenceSpan.text();

            if(essenceSpanStr =="加精") {
                //加精
                $.get("/article/essence/" + aid+"?cid="+cid, function (re) {
                    if (re.success) {
                        alert("加精成功");
                        //更换span文字
                        essenceSpan.text("取消加精");
                    } else {
                        alert(re.message);
                    }
                });
            }else if (essenceSpanStr == "取消加精") {
                //取消加精
                $.get("/article/essence/cancel/"+aid+"?cid="+cid,function (re) {
                    if (re.success) {
                        alert("取消加精成功");
                        //更换span文字
                        essenceSpan.text("加精");

                        //判断是否为精华帖
                        if (type == "essence") {
                            // 删除该帖
                            $(".post-list-item[data-aid='" + aid + "']").remove();

                        }
                    } else {
                        alert(re.message());
                    }
                })
            }
        });

        //点击帖子收藏项
        $(".item-star").unbind("click");
        $(".item-star").click(function () {
            var starBtn = $(this);
            var starItemSpan = starBtn.children("span");
            var starItemStr = starItemSpan.text();
            var starImg = starBtn.find(".handle-icon");
            //获取帖子aid
            var aid = starBtn.parents(".post-list-item").data("aid");
            //判断是收藏还是取消收藏
            if (starItemStr == "收藏") {
                $.get("/article/star/" + aid+"?cid="+cid,function (re) {
                    if (!re.success) {
                        alert(re.message);
                        return;
                    } else {
                        alert(re.message);

                        //变换span的文本
                        starItemSpan.text("取消收藏");
                        starImg.attr("src", "/static/image/articleStar.svg");
                    }
                });
            } else if (starItemStr == "取消收藏") {
                $.get("/article/star/cancel/"+aid+"?cid="+cid,function (re) {
                    if (!re.success) {
                        alert(re.message);
                        return;
                    } else {
                        alert(re.message);
                        starItemSpan.text("收藏");
                        starImg.attr("src", "/static/image/articleNoStar.svg");
                    }
                })
            }

        });

        //点击帖子删除项
        $(".item-delete").unbind("click");
        $(".item-delete").click(function () {
            var deleteBtn = $(this);
            var aid = deleteBtn.parents(".post-list-item").data("aid");
            $("#btn-delete-confirm").data("aid", aid);
            $("#article-delete-model").show();
        });
        $("#btn-delete-cancel").unbind("click");
        $("#btn-delete-cancel").click(function () {
            $("#article-delete-model").hide();
        });
        $("#btn-delete-confirm").unbind("click");
        $("#btn-delete-confirm").click(function () {
            var aid = $(this).data("aid");
            $.get("/article/delete/"+aid+"?cid="+cid,function (re) {
                if (re.success) {
                    alert(re.message);
                    //将帖子删除显示
                    console.log("将删除的帖子id=" + aid);
                    $(".post-list-item[data-aid='" + aid + "']").remove();
                } else {
                    alert(re.message);
                }
                $(".more-dropdown-menu ").hide();
                $("#article-delete-model").hide();
            })
        });



        //点击帖子举报项
        $(".item-report").unbind("click");
        $(".item-report").click(function () {
            var reportBtn = $(this);
            var aid = reportBtn.parents(".post-list-item").data("aid");
            console.log("点击帖子举报按钮，举报的帖子为:" + aid);
            //让举报栏显示
            $("#article-report-model").show();
            $("#btn-article-report-confirm").data("aid",aid);
        });

        $("#btn-article-report-cancel").unbind("click");
        $("#btn-article-report-confirm").click(function () {
            $("#article-report-model").hide();
        });

        $("#btn-article-report-confirm").unbind("click");
        $("#btn-article-report-confirm").click(function () {
            //获取aid
            var aid = $(this).data("aid");
            //获取单选框的
            var report = $("#article-report-model input[name='article-flag']:checked").val();

            if (report == null || report == undefined || report == "") {
                alert("请选择举报理由");
                return;
            }

            $.get("/article/report/"+aid+"?report="+report+"&cid="+cid,function (re) {
                alert(re.message);
                $("#article-report-model").hide();
            })

        });


        //帖子跳转
        $(".topic-text").click(function () {
            var aid = $(this).parents(".post-list-item").data("aid");
            window.location.href = "/article/detail/" + aid + "?pageNum=" + cPageNum;
        });

    }

    $(".hot-article-item").click(function () {
        var aid = $(this).data("aid");
        window.location.href = "/article/detail/" + aid + "?pageNum=" + cPageNum;
    });

    //从后台获取帖子列表，并填充显示
    function getArticleList() {
        var data = new Object();
        data["sort"] = sort;
        data["type"] = type;
        data["pageNum"] = pageNum;
        data["cid"] = cid;
        data["search"] = search;
        var jsonData = JSON.stringify(data);    //转化成json数据
        $.ajax({
            url: '/article/get',
            contentType: "application/json",
            type: 'post',
            dataType: 'json',
            data: jsonData,
        }).done(function (re) {
            console.log("search = " + search);
            var articles = re.list;
            total = re.total;
            pageSize = re.pageSize;
            console.log("共有"+total+"个帖子,一页"+pageSize+"个")
            fillHtml(articles);
        });
    }


    //初始化置顶栏
    initTopic();
    //初始化帖子列表
    getArticleList();


    //判断浏览器是否在最底部
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


    ///点击精华按钮
    $(".essence").click(function () {
        //清空数据
        console.log("切换为精华贴");
        post_list.html("");
        type = "essence";
        pageNum = 1;
        getArticleList();
        $(this).addClass("active");
        $(".normal").removeClass("active");
    });


    //点击动态按钮
    $(".normal").click(function () {
        console.log("切换为动态");
        post_list.html("");
        type = "normal";
        pageNum = 1;
        getArticleList();
        $(this).addClass("active");
        $(".essence").removeClass("active");

    });

    //点击排序菜单按钮
    $("#sort").click(function () {
        $('#sort-menu').toggle();
    });

    //点击排序菜单按钮项
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


    $("#btn-article-saerch").click(function () {
        //获取搜索内容
        var inputVal = $("#input-aricle-search").val();
        search = inputVal;
        post_list.html("");
        pageNum = 1;
        getArticleList();

    });


    $("#btn-stick-all").click(function () {
        window.location.href="/article/stick/list?cid="+cid;
    });

});