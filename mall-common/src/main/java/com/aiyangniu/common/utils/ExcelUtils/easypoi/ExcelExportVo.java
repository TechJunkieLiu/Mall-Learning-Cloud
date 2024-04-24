package com.aiyangniu.common.utils.ExcelUtils.easypoi;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.Data;

import java.util.List;

/**
 * EasyPoi 实体类
 * 三级表头推荐用注解 @ExcelCollection + @Excel 组合实现，超过三级表头推荐用模板实现
 *
 * @author lzq
 * @date 2024/04/02
 */
@Data
public class ExcelExportVo {

    /** 这两个就是一级表头，最后一级表头对应的是具体的某个属性，它们都是被包裹在一级表头下的 **/
    @ExcelCollection(name = "用户信息")
    private List<UserInfo> userInfoList;

    @ExcelCollection(name = "用户角色和权限")
    private List<RoleInfo> roleInfoList;

    @Data
    public static class UserInfo{

        /** 二级表头可以用 groupName 实现 **/
        @Excel(name = "用户账号", width = 20, groupName = "基本信息")
        private String userName;

        @Excel(name = "用户姓名", width = 20, groupName = "基本信息")
        private String realName;

        @Excel(name = "手机号码", width = 20, groupName = "基本信息")
        private String phone;

        @Excel(name = "所在公司", width = 20, groupName = "单位部门")
        private String com;

        @Excel(name = "所在部门", width = 20, groupName = "单位部门")
        private String dept;

        public UserInfo(String userName, String realName, String phone, String com, String dept) {
            this.userName = userName;
            this.realName = realName;
            this.phone = phone;
            this.com = com;
            this.dept = dept;
        }
    }

    @Data
    public static class RoleInfo{

        @Excel(name = "所属角色名称", width = 20, groupName = "角色")
        private String roleName;

        @Excel(name = "所属角色代码", width = 20, groupName = "角色")
        private String roleCode;

        @Excel(name = "菜单权限", width = 40, groupName = "权限")
        private String menu;

        @Excel(name = "数据权限", width = 40, groupName = "权限")
        private String data;

        public RoleInfo(String roleName, String roleCode, String menu, String data) {
            this.roleName = roleName;
            this.roleCode = roleCode;
            this.menu = menu;
            this.data = data;
        }
    }
}
