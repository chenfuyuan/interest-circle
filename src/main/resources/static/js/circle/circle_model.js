$(function () {
    $(".circle-item").click(function () {
        var item = $(this);
        var order = item.data("order");
        alert("点击了第" + order + "个圈子头像");
        window.location.href = "/" + order;
    });
});