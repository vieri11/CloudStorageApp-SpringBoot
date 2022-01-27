package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void addNote(NoteForm addNote){
        Note newNote = new Note();

        newNote.setNoteTitle(addNote.getNoteTitle());
        newNote.setNoteDescription(addNote.getNoteDescription());
        newNote.setUserId(addNote.getUserId());

        noteMapper.insertNote(newNote);
    }

    public Note[] getNoteListByUserId(Integer userId) {
        return noteMapper.getNotesByUserId(userId);
    }

}
