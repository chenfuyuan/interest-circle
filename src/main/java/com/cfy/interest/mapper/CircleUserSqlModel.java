package com.cfy.interest.mapper;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class CircleUserSqlModel {
    public String deleteAdminList(Map map) {
        List<Integer> list = (List<Integer>) map.get("deleteAdminArray");
        Integer cid = (Integer) map.get("cid");
        StringBuilder sb = new StringBuilder();
        sb.append("update circle_user set type = 3 where cid = #{cid} and state = 1 and uid in (");
        MessageFormat mf = new MessageFormat("#'{'deleteAdminArray[{0}]}");
        for (int i = 0; i < list.size(); i++) {
            sb.append(mf.format(new Object[]{i}));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public String addAdminList(Map map) {
        List<Integer> list = (List<Integer>) map.get("addAdminArray");
        Integer cid = (Integer) map.get("cid");
        StringBuilder sb = new StringBuilder();
        sb.append("update circle_user set type = 2 where cid = #{cid} and state = 1 and uid in (");
        MessageFormat mf = new MessageFormat("#'{'addAdminArray[{0}]}");
        for (int i = 0; i < list.size(); i++) {
            sb.append(mf.format(new Object[]{i}));
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
