package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    Note[] getNotes(Integer userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid ) " +
            "VALUES( #{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys=true, keyProperty="noteId")
    int insertNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    void deleteNote(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    Note[] getNotesByUserId(Integer userId);

    @Update("UPDATE NOTES SET notetitle= #{noteTitle}, notedescription=#{noteDescription} WHERE noteid = #{noteId}")
    void editNote(Note noteId);
}
