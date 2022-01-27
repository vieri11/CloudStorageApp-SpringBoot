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

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }
    public Note[] getNoteListByUserId(Integer userId) {
        return noteMapper.getNotesByUserId(userId);
    }

    public void editNote (NoteForm noteForm) {
        Note editNote = new Note();

        editNote.setNoteId(noteForm.getNoteId());
        editNote.setNoteTitle(noteForm.getNoteTitle());
        editNote.setNoteDescription(noteForm.getNoteDescription());
        editNote.setUserId(noteForm.getUserId());

        noteMapper.editNote(editNote);
    }
}
