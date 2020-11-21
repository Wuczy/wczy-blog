package com.site.blog.my.core.dao;

import com.site.blog.my.core.pojo.BlogComment;
import java.util.List;
import java.util.Map;

public interface BlogCommentMapper {
    int deleteByPrimaryKey(Long commentId);

    int insert(BlogComment record);

    int insertSelective(BlogComment record);

    BlogComment selectByPrimaryKey(Long commentId);

    int updateByPrimaryKeySelective(BlogComment record);

    int updateByPrimaryKey(BlogComment record);

    List<BlogComment> findBlogCommentList(Map map);

    int getTotalBlogComments(Map map);

    int checkUndone(Integer[] ids);

    int checkDone(Integer[] ids);

    int deleteBatch(Integer[] ids);
}