package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/home")
public class HomeController {
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final FileService fileService;

    public HomeController(UserService userService,
                          NoteService noteService,
                          CredentialService credentialService,
                          FileService fileService) {
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    @GetMapping()
    public String getHomePage(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userService.getUser(loggedInUserName);

        if (Objects.isNull(user)) {
            return "redirect:/login?error";
        }

        List<Note> notes = noteService.getNotesByUserId(user.getUserId());
        List<File> files = fileService.getFilesByUserId(user.getUserId());
        List<Credential> credentials = credentialService.getCredentialsByUserId(user.getUserId());

        model.addAttribute("notes", notes);
        model.addAttribute("files", files);
        model.addAttribute("credentials", credentials);
        return "home";
    }


    @GetMapping("/result")
    public String result() {
        return "result";
    }
}
