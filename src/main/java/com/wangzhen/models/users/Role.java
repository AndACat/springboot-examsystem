package com.wangzhen.models.users;
/**
 * @Author wangzhen
 * @Description 角色权限类
 * @CreateDate 2020/1/12 18:00
 */
public class Role {
    private String uuid;
    private String name;
    public Role(){}
    public Role(String name) {
        this.name = name;
    }
    public Role(String uuid, String name) {
        this.uuid = uuid;
        this.name = name                      ;
       }



    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}