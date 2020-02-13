$(function () {
    var E = window.wangEditor
    var editor = new E('#toolbar', '#text');

    // 配置工具栏显示
    editor.customConfig.menus = [
        'head',  // 标题
        'bold',  // 粗体
        'fontSize',  // 字号
        'fontName',  // 字体
        'italic',  // 斜体
        'underline',  // 下划线
        'strikeThrough',  // 删除线
        'foreColor',  // 文字颜色
        'backColor',  // 背景颜色
        'link',  // 插入链接
        'list',  // 列表
        'justify',  // 对齐方式
        'quote',  // 引用
        'emoticon',  // 表情
        'image',  // 插入图片
        'table',  // 表格
        'code',  // 插入代码
        'undo',  // 撤销
        'redo'  // 重复
    ]

    //开启debug
    editor.customConfig.uploadImgServer = '/article/upload/image';
    editor.customConfig.uploadFileName = 'file';
    //editor.customConfig.uploadImgShowBase64 = true
    editor.create();


    $("#btn-publish").click(function () {
        var artilce =  editor.txt.html();

        $.post("/article/publish",{article: artilce},function () {
            window.location.href = "/";
        });
    });


});