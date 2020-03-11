$(function () {
        //点击圈子设置，弹出圈子设置弹框
        $("#btn-circle-setting").click(function () {
            $("#circle-setting-model").show();
        });

    uid = $("#uid").data("uid");
    console.log("uid = " + uid);
    cid = $("#btn-quit-confirm").data("cid");
    console.log("cid = " + cid);
    identity = $("#uid").data("identity");
    console.log("identity=" + identity);
    cPageNum =$("#uid").data("pagenum");
    console.log("pageNum = " + cPageNum);
    }
);