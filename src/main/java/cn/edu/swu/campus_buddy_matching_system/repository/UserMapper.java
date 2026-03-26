package cn.edu.swu.campus_buddy_matching_system.repository;

import cn.edu.swu.campus_buddy_matching_system.model.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user (student_id, password_hash, nickname, credit_score, is_active, created_at) " +
            "VALUES (#{studentId}, #{passwordHash}, #{nickname}, 100, true, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT * FROM user WHERE student_id = #{studentId}")
    @Results(id = "userResult", value = {
        @Result(property = "passwordHash", column = "password_hash"),
        @Result(property = "studentId", column = "student_id"),
        @Result(property = "avatarUrl", column = "avatar_url"),
        @Result(property = "creditScore", column = "credit_score"),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "created_at")
    })
    User findByStudentId(String studentId);

    @Select("SELECT * FROM user WHERE id = #{id}")
    @ResultMap("userResult")
    User findById(Long id);

    @Update("UPDATE user SET credit_score = #{creditScore} WHERE id = #{id}")
    int updateCreditScore(@Param("id") Long id, @Param("creditScore") Integer creditScore);
}