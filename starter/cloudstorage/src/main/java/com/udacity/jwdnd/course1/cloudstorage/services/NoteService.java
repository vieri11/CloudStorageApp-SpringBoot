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
        Note newNote = getNoteObj(addNote);

        noteMapper.insertNote(newNote);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

    public Note[] getNoteListByUserId(Integer userId) {
        return noteMapper.getNotesByUserId(userId);
    }

    public void editNote (NoteForm noteForm) {
        Note editNote = getNoteObj(noteForm);

        noteMapper.editNote(editNote);
    }

    private Note getNoteObj(NoteForm noteForm) {
        Note newNote = new Note(noteForm.getNoteId(), noteForm.getNoteTitle(), noteForm.getNoteDescription(), noteForm.getUserId());

        return newNote;
    }
}
