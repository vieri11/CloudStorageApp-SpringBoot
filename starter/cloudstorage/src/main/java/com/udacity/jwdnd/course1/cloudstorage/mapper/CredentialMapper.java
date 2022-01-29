package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CredentialMapper {

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) " +
            "VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys=true, keyProperty="credentialId")
    Integer addCredential (Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    void deleteCredential(Integer credentialId);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    Credential[] getCredentialsByUserId(Integer userId);

    @Update("UPDATE CREDENTIALS SET url= #{url}, username=#{username}, key=#{key}, password=#{password} " +
            "WHERE credentialid = #{credentialId}")
    void editCredential(Credential credentialId);
}
