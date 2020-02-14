$(function () {

    $(".circle-item").click(function () {
        var item = $(this);
        var order = item.data("order");
        console.log("切换到第" + order + "个圈子");
        window.location.href = "/" + order;
    });

    //点击退出按钮退出
    $("#btn-quit-confirm").click(function () {
        window.location.href = "/circle/quit/" + cid;
    });



    //举报圈子
    $("#btn-report-confirm").click(function () {
        var report = $("input[name='flag']:checked").val();
        if (report == undefined || report == '' || report == null) {
            alert("请选择举报理由");
        } else {
            console.log("举报的圈子=" + cid);
            console.log("举报的内容=" + report);
            $.ajax({
                url: "/circle/report/"+cid+ "?report=" + report,
                contentType: "application/json",
                type: 'get',
                dataType: 'json',
            }).done(function (re) {
                if (re.success) {
                    alert("举报成功");
                    $("#btn-report-close").click();
                }
            })
        }
        console.log("report = " + report);

    });

    $("#edit-entry").click(function () {
        window.location.href = "/article/editor?cid="+cid;
    });
});