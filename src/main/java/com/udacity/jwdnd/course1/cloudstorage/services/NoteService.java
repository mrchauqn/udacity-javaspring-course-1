package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    public Note[] getNotesByUser(Integer userid) {
        return noteMapper.getNotesForUser(userid);
    }

    public void insert(Note note) {
        noteMapper.insert(note);
    }

    public void update(Note note) {
        noteMapper.updateNote(note.getNoteid(), note.getNotetitle(), note.getNotedescription());
    }

    public void delete(Integer noteid) {
        noteMapper.deleteNote(noteid);
    }
}
