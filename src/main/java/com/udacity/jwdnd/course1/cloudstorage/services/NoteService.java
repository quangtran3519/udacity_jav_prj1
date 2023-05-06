package com.udacity.jwdnd.course1.cloudstorage.services;


import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NoteService {

    public final Logger logger = LoggerFactory.getLogger(HashService.class);
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotesByUserId(Integer userId) {
        logger.info(" start get notes by user id {}", userId);
        return noteMapper.getListNoteByUserId(userId);

    }

    public Note getNoteById(Integer id) {
        logger.info(" start get note by id {}", id);
        return noteMapper.getNoteById(id);
    }


    public void insertNote(Note note) throws Exception {
        logger.info(" start insert note {}", note);
        int result = noteMapper.insertNote(note);
        if (result == 0) {
            throw new Exception("insert note is failed");
        }
    }

    public void deleteNote(int id) throws Exception {
        logger.info(" start deleteNote  {}", id);
        int result = noteMapper.deleteNote(id);
        if (result == 0) {
            throw new Exception("delete note is failed");
        }
    }

    public void updateNote(Note note) throws Exception {
        logger.info(" start update note {}", note);

        Note existNote = noteMapper.getNoteById(note.getNoteId());

        if (Objects.isNull(existNote)) {
            throw new Exception("note isn't existed");
        }


        int result = noteMapper.updateNote(note);
        if (result == 0) {
            throw new Exception("update note is failed");
        }
    }

}
