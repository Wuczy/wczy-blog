package com.site.blog.my.core.service;

import com.site.blog.my.core.pojo.AdminUser;

public interface AdminUserService {

    /**
     * 修改当前登录用户的密码
     *
     * @param userName
     * @param password
     * @return
     */
    AdminUser login(String userName, String password);

    /**
     * 根据id获取用户信息
     *
     * @param loginUserId
     * @return
     */
    AdminUser getUserDetailById(Integer loginUserId);

    /**
     * 修改当前登录用户的密码
     *
     * @param loginUserId
     * @param originalPassword
     * @param newPassword
     * @return
     */
    Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);

    /**
     * 修改当前登录用户的名称信息
     *
     * @param loginUserId
     * @param loginUserName
     * @param nickName
     * @return
     */
    Boolean updateName(Integer loginUserId, String loginUserName, String nickName);

}
