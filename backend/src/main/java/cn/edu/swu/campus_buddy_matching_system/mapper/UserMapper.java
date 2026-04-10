package cn.edu.swu.campus_buddy_matching_system.mapper;

import cn.edu.swu.campus_buddy_matching_system.model.entity.User;

import java.util.List;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    // ==================== 注解方式实现 ====================

    @Insert("""
            INSERT INTO users
            (student_id, username, password, email, nickname, avatar_url, college, grade, tags, credit_score, enabled)
            VALUES
            (#{studentId}, #{username}, #{password}, #{email}, #{nickname}, #{avatarUrl}, #{college}, #{grade}, #{tags}, #{creditScore}, #{enabled})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results(id = "userResult", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "email", column = "email"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "avatarUrl", column = "avatar_url"),
            @Result(property = "college", column = "college"),
            @Result(property = "grade", column = "grade"),
            @Result(property = "tags", column = "tags"),
            @Result(property = "creditScore", column = "credit_score"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "roles", column = "id", many = @Many(select = "cn.edu.swu.campus_buddy_matching_system.mapper.RoleMapper.findRolesByUserId"))
    })
    User findById(@Param("id") Long id);

    @Select("SELECT * FROM users WHERE username = #{username}")
    @ResultMap("userResult")
    User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM users WHERE student_id = #{studentId}")
    @ResultMap("userResult")
    User findByStudentId(@Param("studentId") String studentId);

    @Select("SELECT * FROM users WHERE email = #{email}")
    @ResultMap("userResult")
    User findByEmail(@Param("email") String email);

    @Update("UPDATE users SET password = #{password}, updated_at = NOW() WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Update("UPDATE users SET credit_score = #{creditScore}, updated_at = NOW() WHERE id = #{id}")
    int updateCreditScore(@Param("id") Long id, @Param("creditScore") Integer creditScore);

    List<User> selectByPage(@Param("offset") int offset,
            @Param("size") int size,
            @Param("username") String username,
            @Param("email") String email,
            @Param("enabled") Boolean enabled);

    long countByPage(@Param("username") String username,
            @Param("email") String email,
            @Param("enabled") Boolean enabled);

    User selectWithRoles(Long id);

    int delete(Long id);
}