$(function () {
    //界面类型
    viewType = $("#content").data("viewtype");
    console.log("viewType = " + viewType);
    //圈子id
    cid = $("#app").data("cid");
    console.log("cid = " + cid);
    $(".el-menu-ele").click(function () {
        var menu_item = $(this);
        var isOpt = menu_item.hasClass("defaultActive");
        if (isOpt) {
            console.log("当前页面就是选择页面，无需变化");
            return;
        }
        var url = "/circle/admin/";
        var type = menu_item.data("type");
        switch (type) {
            case 1:
                url += "index";
                break
            case 2:
                url += "setting";
                break;
            case 3:
                url += "limit";
                break;
        }

        url += "?cid=" + cid;
        window.location.href = url;
    });
});