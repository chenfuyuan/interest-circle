$(function () {

    var provinces;
    var checkProvince;
    var citys;

    console.log("从后台请求省份数据");
    $.ajax(
        {
            url: '/get/provinces',
            contentType: "application/json",
            type: 'get',
            dataType: 'json'
        }
    ).done(function (re) {
        provinces = re;
        var checkProvinceId = $("#checkProvince").data("pid");
        if (checkProvinceId == 0) {
            $("#city").hide();
        } else {
            citys = provinces[checkProvinceId-1].citys;
        }
    });


    $("#province").click(function () {

        //向后台请求省份数据
        var menu = $("#menu");

        //判断当前菜单是否是province
        var isProvince = menu.hasClass("provinces");
        if (!isProvince) {
            if (provinces != null) {
                //填充menu数据
                console.log("已有数据，直接填充");
                paddingProvinceData();
                menu.attr("class", "cityChild city-child provinces");

                $(".ic-query").click(function () {
                        console.log("点击了连接");
                        var name = $(this).html();
                        console.log("选中的省份为" + name);
                        //改变选中省份
                        $("#checkProvince").html(name);
                        var provinceId = $(this).data("pid");
                        $("#checkProvince").data("pid", provinceId);


                        console.log("省份id = " + provinceId);
                        if (provinceId == "0") {
                            $("#city").hide();
                            menu.hide();
                            $("#checkCity").data("cid", 0);
                            return;
                        } else {
                            $("#city").show();
                            $("#checkCity").html("城市");
                            $("#checkCity").data("cid", 0);
                        }
                        citys = provinces[provinceId - 1].citys;
                        menu.hide();
                    }
                );
            }

        }
        //菜单显示切换
        menu.toggle();

    });

    //填充省份信息
    function paddingProvinceData() {

        var menuList = $("#menu-list");
        menuList.html("");
        menuList.append("<li class='cityItemBox city-child-choise-city'>" +
            "<a href='javascript:void(0);' class='ic-query' data-pid='0'>全国</a></li>");
        for (var i = 0; i < provinces.length; i++) {
            menuList.append("<li class='cityItemBox city-child-choise-city'>" +
                "<a href='javascript:void(0);' class='ic-query' data-pid='" + provinces[i].id + "'>" + provinces[i].name + "</a></li>");
        }
    }

    $("#city").click(function () {
        var menu = $("#menu");

        console.log("向菜单中填充城市数据")
        //判断当前菜单是否是城市列表
        if (!menu.hasClass("city")) {
            //如果不是更换菜单内容
            paddingCityData();
            menu.attr("class", "cityChild city-child city");

            $(".ic-query").click(function () {
                    console.log("点击了连接");
                    var name = $(this).html();
                    console.log("选中的城市为" + name);

                    //改变选中省份
                    $("#checkCity").html(name);

                    var cityId = $(this).data("cid");
                    $("#checkCity").data("cid",cityId);
                    console.log("城市id = " + cityId);
                    menu.hide();
                }
            );
        }
        menu.toggle();

    });

    //填充市信息
    function paddingCityData() {

        var menuList = $("#menu-list");
        menuList.html("");
        for (var i = 0; i < citys.length; i++) {
            menuList.append("<li class='cityItemBox city-child-choise-city'>" +
                "<a href='javascript:void(0);' class='ic-query' data-cid='" + citys[i].id + "'>" + citys[i].name + "</a></li>");
        }
    }


    $("#btn_affirm").click(function () {
        var checkProvinceId = $("#checkProvince").data("pid");;
        var checkCityId = $("#checkCity").data("cid");

        console.log("选择的省份" + checkProvinceId);
        console.log("选择的城市" + checkCityId);

        if (checkProvinceId == 0) {
            window.location.href = "/circle/querySearch";
            return;
        } else {
            if (checkCityId == 0) {
                console.log("未选择城市，直接根据省份跳转，省份=" + checkProvinceId);
                window.location.href = "/circle/querySearchByDId?districtId="+checkProvinceId;
            } else {
                console.log("选择城市 = " + checkCityId);
                window.location.href = "/circle/querySearchByDId?districtId="+checkCityId;
            }
        }


    });

    $(".btn-join-circle").click(function () {
        var circleId = $(this).data("cid");
        console.log("要加入的圈子是" + circleId);

        $.ajax({
            url: "/circle/join/" + circleId,
            contentType: "application/json",
            type: 'get',
            dataType: 'json'
        }).done(function (re) {
            alert(re.message);
            if (re.success) {
                window.location.reload();
            }
        })

    });
})