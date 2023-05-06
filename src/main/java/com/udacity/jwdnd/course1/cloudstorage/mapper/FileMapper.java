package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    File getFileByName(String fileName);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileById(Integer fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insetFile(File file);

    @Select("SELECT * FROM FILES WHERE userid= #{userId} ")
    List<File> getFilesByUserId(Integer userId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFile(int fileId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId} AND filename = #{fileName}")
    File getFileByUserIdAndUserName(Integer userId, String fileName);
}
