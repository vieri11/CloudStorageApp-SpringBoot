package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    Note[] getNotes(Integer userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid ) " +
            "VALUES( #{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys=true, keyProperty="noteId")
    int insertNote(Note note);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    Note[] getNotesByUserId(Integer userId);
}
