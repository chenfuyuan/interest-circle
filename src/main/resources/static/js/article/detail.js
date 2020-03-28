$(function () {
    $(".article-operation-menu-btn").click(function () {
        $(".article-dropdown-menu").toggle();
    });

    aid = $(".main-content").data("aid");

    console.log("aid = " + aid);


    //点赞与取消点赞，事件
    $(".likes img").unbind("click");
    $(".likes img").click(function () {
        var img_likes = $(this);
        var src = img_likes.attr("src");
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

    //点击帖子置顶项
    $(".item-sticky").unbind("click");
    $(".item-sticky").click(function () {
        var stickyBtn = $(this);
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
            });
        } else {
            $.get("/article/sticky/cancel/"+aid+"?cid="+cid,function (re) {
                if (!re.success) {
                    alert(re.message);
                }
                alert(re.message);
                //删除该条置顶记录
                itemStickySpan.text("置顶");
            })
        }
        stickyBtn.parents(".more-dropdown-menu").hide();
    });

    //点击帖子加精项
    $(".item-essence").unbind("click");
    $(".item-essence").click(function () {
        var essenceBtn = $(this);

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
                window.location.href = "/";

            } else {
                alert(re.message);
            }




        })
    });



    //点击帖子举报项
    $(".item-report").unbind("click");
    $(".item-report").click(function () {
        var reportBtn = $(this);
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

        $.get("/article/report/"+aid+"?report="+report,function (re) {
            alert(re.message);
            $("#article-report-model").hide();
        })

    });


    $(".hot-article-item").click(function () {
        var aid = $(this).data("aid");
        window.location.href = "/article/detail/" + aid + "?pageNum=" + cPageNum;
    });
})