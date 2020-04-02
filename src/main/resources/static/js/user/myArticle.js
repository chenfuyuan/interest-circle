$(function () {
    $(".post-list-item").click(function () {
        var aid = $(this).data("aid");
        var cid = $(this).data("cid");
        window.location.href = "/article/detail?aid="+aid+"&cid="+cid;

    });
})