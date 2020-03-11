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
        var artilce = editor.txt.html();
        var cid = $(this).data("cid");
        var title = $("#input-editor-title").val();
        if (artilce == "<p><br></p>") {
            alert("请输入帖子内容");
            return;
        }
        if (title == "" || title == undefined || title == null) {
            alert("请输入帖子标题");
            return
        }
        var data = new Object();
        data["content"] = artilce;
        data["cid"] = cid;
        data["title"] = title;
        //封装成json对象
        var editorArticleVo = JSON.stringify(data);
        $.ajax({
            url: '/article/publish',
            contentType: "application/json",
            type: 'post',
            dataType: 'json',
            data: editorArticleVo,
            success: function () {
                window.location.href = "/";
            }
        }).fail(function () {
            window.location.href = "/";
        });
    });


});