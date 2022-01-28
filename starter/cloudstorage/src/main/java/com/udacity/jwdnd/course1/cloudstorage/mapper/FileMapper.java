package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid=#{userId}")
    File[] getFileListByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid=#{fileId}")
    File getFileByFileId(Integer fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys=true, keyProperty="fileId")
    Integer addFile(File newFile);

    @Delete("DELETE FROM FILES WHERE fileid=#{fileId}")
    void deleteFileById(Integer fileId);

    @Select("SELECT filename FROM FILES WHERE userid=#{userId}")
    String[] getFileNamesByUserId(Integer userId);

}
