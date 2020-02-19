$(function () {
    $("#user-img").click(function () {
        $("#header-user-menu").toggle();
    });


    /**
     * 圈子搜索
     */
    $("#btn-circle-search").click(function () {
        var search = $("#input-search").val();
        console.log("圈子搜索 = " + search);
        window.location.href = "/circle/search?search=" + search + "&pageNum=1";
    });
})