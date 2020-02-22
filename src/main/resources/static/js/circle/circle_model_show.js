$(function () {

    //点击圈子设置弹窗的关闭按钮关闭弹窗
    $("#btn-setting-close").click(function () {
        $("#circle-setting-model").hide();
    });

    //点击举报按钮跳出举报弹窗
    $("#btn-report").click(function () {
        $("#circle-report-model").show();
    });

    //点击举报弹窗的关闭按钮关闭弹窗
    $("#btn-report-close").click(function () {
        $("#circle-report-model").hide();
    });

    //点击举报弹窗的取消按钮关闭弹窗
    $("#btn-report-cancel").click(function () {
        $("#circle-report-model").hide();
    });

    //点击设置弹窗的退出按钮跳出退出确定弹出框
    $("#btn-circle-quit").click(function () {
        $("#circle-quit-model").show();
    });

    //点击退出弹框的取消按钮关闭弹框
    $("#btn-quit-cancel").click(function () {
        $("#circle-quit-model").hide();
    });


    //点击圈子列表按钮，显示圈子列表
    $("#btn-circle-menu").click(function () {
        $("#circle-list").show();
    });

    //点击圈子列表返回按钮，返回圈子选择
    $("#btn-circle-return").click(function () {
        $("#circle-list").hide();

    });

    $("#admin-back").click(function () {
        window.location.href = "/circle/admin/index?cid=" + cid;
    });
});
