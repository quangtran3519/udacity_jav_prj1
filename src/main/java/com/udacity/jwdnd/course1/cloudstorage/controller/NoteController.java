package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;


@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;


    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }


    @PostMapping()
    public String addNewNote(@ModelAttribute Note note, Authentication authentication, RedirectAttributes redirectAttributes) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userService.getUser(loggedInUserName);

        if (Objects.isNull(user)) {
            return "redirect:/login?error";
        }

        try {
            note.setUserId(user.getUserId());

            if (!Objects.isNull(note.getNoteId())) {
                noteService.updateNote(note);
            } else {
                noteService.insertNote(note);
            }

            return "redirect:/home/result?success";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "handling update notes got errors");
            return "redirect:/home/result?error";
        }


    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int noteId, RedirectAttributes redirectAttributes) {
        if (noteId == 0) {
            redirectAttributes.addFlashAttribute("error", "Note id is empty");
            return "redirect:/home/result?error";
        }
        try {
            noteService.deleteNote(noteId);
            return "redirect:/home/result?success";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Delete note got err");
            return "redirect:/home/result?error";
        }
    }


}
