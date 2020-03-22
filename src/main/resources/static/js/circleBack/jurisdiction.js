$(function () {
    var pageNum = 1;
    var ulMemberList = $("#ul-member-list");    //成员列表
    var ulChoiceMemberList = $("#choice-member-list");    //被被选择的列表
    var ulAdminList = $("#admin-list");    //管理员列表
    var liNumber = 0;
    var maxPageNum = undefined;
    var search = "";
    var choiceMemberNum = 0;
    var MAXCHOICEMEMBERNUM = 5;
    var deleteAdminArray = new Array();
    var addAdminArray = new Array();

    //使添加窗口显示
    function modelAddAdminShow() {
        $("#model-add-admin").show();
        $("#model-bgd").show();
    }

    //使添加窗口隐藏
    function modelAddAdminClose() {
        $("#model-add-admin").hide();
        $("#model-bgd").hide();
    }

    /**
     * 判断是否存在在原有的管理员列表
     * @param choiceUid
     * @returns {boolean}
     */
    function isInAdminArray(choiceUid) {
        for (var i in adminIdArray) {
            if (adminIdArray[i] == choiceUid) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除添加列表的指定数据
     * @param choiceUid 数据
     */
    function deleteInAddAdminArray(choiceUid) {
        if (addAdminArray == undefined || addAdminArray == null || addAdminArray.length == 0) {
            console.log("添加列表无数据");
            return;
        }
        for (var i in addAdminArray) {
            if (addAdminArray[i] == choiceUid) {
                for (var j = i ;j<addAdminArray.length-1;j++) {
                    addAdminArray[j] = addAdminArray[j + 1];
                }
                //删除最后一个元素
                addAdminArray.pop();
                break;
            }
        }
        console.log("addAdminArray = " + addAdminArray);

    }

    /**
     * 删除删除列表的指定数据
     * @param choiceUid 数据
     */
    function deleteInDeleteAdminArray(choiceUid) {
        if (deleteAdminArray == undefined || deleteAdminArray == null || deleteAdminArray.length == 0) {
            console.log("添加列表无数据");
            return;
        }
        for (var i in deleteAdminArray) {
            if (deleteAdminArray[i] == choiceUid) {
                for (var j = i ;j<deleteAdminArray.length-1;j++) {
                    deleteAdminArray[j] = deleteAdminArray[j + 1];
                }
                //删除最后一个元素
                deleteAdminArray.pop();
            }
        }
    }

    //填充成员列表
    function appendMemberList(memberList) {
        if (memberList != undefined && memberList != null && memberList.length != 0)
            for (var i = 0; i < memberList.length; i++,liNumber++) {
                var type = memberList[i].type;
                var member = memberList[i].user;
                var isAdmin = memberList[i].type == 2;
                var str = "<li class='member-list-item' data-uid='"+member.id+"'><label for='"+liNumber+"'><div" +
                    " class='head'>" +
                    "<div class='custom-transition'style='position: relative;'>" +
                    "<div>" +
                    "<img  src='"+member.avatarPath+"' alt='' class='head-img member-avatar'></div>" +
                    "</div>" +
                    "</div>" +
                    "<h2 class='title'>"+member.name+"</h2>" +
                    "<div class='radio' data-uid='"+member.id+"' data-avatar='"+member.avatarPath+"' data-name='"+member.name+"'></div>" +
                    "</label></li>";
                ulMemberList.append(str);

                if (type == 2) {
                    $(".radio[data-uid=" + member.id + "]").addClass("radio-check");
                }

                //添加选中事件
                $(".radio[data-uid=" + member.id + "]").click(function () {
                    var tempMember = new Object();
                    var choiceUid = $(this).data("uid");
                    var choiceAvatarPath = $(this).data("avatar");
                    var choiceName = $(this).data("name");

                    //填充数据
                    tempMember["id"] = choiceUid;
                    tempMember["avatarPath"] = choiceAvatarPath;
                    tempMember["name"] = choiceName;

                    var thisRadio = $(this);
                    console.log("选中了uid = " + choiceUid + "的用户");
                    //变换样式
                    var isCheck = thisRadio.hasClass("radio-check");
                    console.log("isCheck = " + isCheck);
                    var inAdminArray = isInAdminArray(choiceUid);
                    if (isCheck) {
                        thisRadio.removeClass("radio-check");
                        if (inAdminArray) {
                            //加入删除列表
                            deleteAdminArray.push(choiceUid);
                        } else {
                            //删除添加列表数据
                            deleteInAddAdminArray(choiceUid);
                        }
                        choiceMemberNum--;
                        //删除已选列表对应项
                        console.log("删除已选列表对应项");
                        console.log("choiceUid = "+ choiceUid);
                        $(".choice-list-item[data-uid=" + choiceUid + "]").remove();
                        //已选数字-1
                        var spanChoiceNum = $("#span-choice-num");
                        spanChoiceNum.text(parseInt(spanChoiceNum.text()) - 1);
                    } else {
                        if (choiceMemberNum >= MAXCHOICEMEMBERNUM) {
                            alert("管理员最多任命" + MAXCHOICEMEMBERNUM + "位");
                            return;
                        }
                        //检测原本列表中是否有该数据
                        if (isInAdminArray()) {
                            //删除删除列表数据
                            deleteInDeleteAdminArray(choiceUid);
                        } else {
                            //添加到添加列表
                            addAdminArray.push(choiceUid);
                        }
                        //添加到已选
                        appendChoiceList(tempMember);
                        thisRadio.addClass("radio-check");
                        //已选数字+1
                        var spanChoiceNum = $("#span-choice-num");
                        spanChoiceNum.text(parseInt(spanChoiceNum.text()) + 1);
                    }
               });
            }
    }

    //获取成员列表
    function getUserList() {
        $.get("/circle/admin/get/memberList?cid=" + cid + "&pageNum=" + pageNum+"&search="+search, function (result) {
            console.log("result = " + result.list);
            if (result == undefined && result !== null && result.list.length != 0) {
                return false;
            }
            maxPageNum = result.pages;
            console.log("共" + maxPageNum + "页，现在第"+result.pageNum+"页。");
            var memberList = result.list;
            //填充数据
            appendMemberList(memberList);

        });
    }

    /**
     * 初始化成员列表
     */
    function initMemberList() {
        ulMemberList.html("");
        pageNum = 1;
        liNumber = 0;
        maxPageNum = undefined;
        getUserList();
    }
    //填充已选列表
    function appendChoiceList(admin) {
        var str = "<li class='choice-list-item' data-uid='"+admin.id+"'>" +
            "<div class='head'>" +
            "<img src='" + admin.avatarPath + "' alt=''></div>" +
            "<h2 class='name'>"+admin.name+"</h2>" +
            "</li>";
        //已选人数加1
        choiceMemberNum++;
        //添加到页面
        ulChoiceMemberList.append(str);
    }
    /**
     * 初始化已选列表
     */
    function initChoiceMemberList() {
        /*清空已选列表*/
        console.log("初始化已选列表");
        ulChoiceMemberList.html("");
        choiceMemberNum = 0;
        deleteAdminArray = new Array();
        addAdminArray = new Array();
        adminIdArray = new Array();
        $.get("/circle/get/adminList?cid="+cid,function (adminList) {
            if (adminList == undefined || adminList == null || adminList.length == 0) {
                console.log("该圈子未有管理员，请任命");
                return;
            }
            for (var i = 0; i < adminList.length; i++) {
                var admin = adminList[i];
                appendChoiceList(admin);
                adminIdArray[i] = admin.id;
            }
            console.log(adminIdArray);
            $("#span-choice-num").text(choiceMemberNum);
        })
        
    }


    /*使新增模块显示并添加数据*/
    $("#btn-add-admin").click(function () {
        modelAddAdminShow();
        //初始化圈子成员列表
        initMemberList();
        //初始化已选列表
        initChoiceMemberList();

    });

    /*判断是否列表是否到达底部，到达底部加载新的成员*/
    ulMemberList.scroll(function () {
        var divHeight = $(this).height();
        var nScrollHeight = $(this)[0].scrollHeight;
        var nScrollTop = $(this)[0].scrollTop;
        //是否到达底部
        if (nScrollTop + divHeight >= nScrollHeight) {
            console.log("到达页面最底部");
            if (maxPageNum != undefined && pageNum < maxPageNum) {
                pageNum++;
                getUserList();
            }
        }
    });

    //关闭窗口点击事件
    $("#btn-close-model").click(function () {
        modelAddAdminClose();
        //清除搜索记录
        search = "";
    });

    /*删除监听键*/
    $(".btn-delete-admin").click(function () {
        //获取要删除的用户id
        var thisUserLiBtn = $(this);
        var deleteUid = $(this).data("uid");
        var thisUser = thisUserLiBtn.parents(".admin-list-li");
        //请求删除
        $.get("/circle/back/delete/admin?uid=" + deleteUid + "&cid=" + cid, function (result) {
            console.log("删除成功");
            //删除该项
            if (result.success) {
                thisUser.remove();
            }
        });
    });


    /*进行搜索*/
    $("#btn-member-search").click(function () {
        search = $("#input-text-search").val().trim();
        initMemberList();
    });

    /**
     * 添加按钮点击事件监听
     */
    $("#btn-add-confirm").click(function () {
        if (deleteAdminArray.length == 0 && addAdminArray.length == 0) {
            console.log("管理员列表未作出改变");
            $("#btn-close-model").click();
            return;
        }

        var addAdminVO = new Object();
        addAdminVO["cid"] = cid;
        addAdminVO["deleteAdminArray"] = deleteAdminArray;
        addAdminVO["addAdminArray"] = addAdminArray;
        var data = JSON.stringify(addAdminVO);

        $.ajax({
            url: '/circle/back/add/admin',
            contentType: "application/json",
            type: 'post',
            dataType: 'json',
            data: data
        }).done(function (result) {
            if (result.success) {
                alert("添加成功");
                //改变页面的管理员列表
                //删除 删除列表中包含的管理员
                for (i in deleteAdminArray) {
                    $(".admin-list-li[data-uid='" + deleteAdminArray[i] + "']").remove();
                }
                //添加 添加列表中的包含的管理员
                for (i in addAdminArray) {
                    var adminId = addAdminArray[i];
                    var liMember = $(".member-list-item[data-uid='" + adminId + "']");
                    var avatarPath = liMember.find(".member-avatar").attr("src");
                    var name = liMember.find(".title").text();
                    
                    var str = "<li class='admin-list-li' data-uid='"+adminId+"'>" +
                        "<div class='head'>" +
                        "<img src='"+avatarPath+"' alt=''></div>" +
                        "<div class='name'>"+name+"</div>" +
                        "<div class='delete'>" +
                        "<span data-uid='"+adminId+"' class='glyphicon glyphicon-trash el-icon-delete" +
                        " btn-delete-admin'></span></div></li>";

                    ulAdminList.append(str);
                }
            } else {
                alert(result.message);
            }
            $("#btn-close-model").click();
        });
    });

})