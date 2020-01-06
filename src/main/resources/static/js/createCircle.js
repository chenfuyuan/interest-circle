$(function () {
    $("#province").change(function () {
            //获取被选中的省份id
            var provinceValue = $(this).find("option:selected").val();


            if (provinceValue == 0) {
                return
            }

            $.ajax(
                {
                    url: 'getCity/'+provinceValue,
                    contentType: "application/json",
                    type: 'get',
                    dataType: 'json',
                }).done(function (re) {
                    $("#city").html("")
                    $("#city").append("<option value='0'>请选择城市</option>")
                    for (var i = 0; i<re.length;i++){
                        $("#city").append("<option value='"+re[i].id+"'>"+re[i].name+"</option>")
                    }
                }
            );
        }
    );
})