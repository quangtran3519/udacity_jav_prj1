package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private final CredentialService credentialService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String addCredential(@ModelAttribute Credential credential, Authentication authentication, RedirectAttributes redirectAttributes) {
        String loggedInUserName = (String) authentication.getPrincipal();
        User user = userService.getUser(loggedInUserName);

        if (Objects.isNull(user)) {
            return "redirect:/login?error";
        }

        try {
            credential.setUserId(user.getUserId());

            if (!Objects.isNull(credential.getCredentialId())) {
                credentialService.updateCredential(credential);
            } else {
                credentialService.insertCredential(credential);
            }

            return "redirect:/home/result?success";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "handling update credentials got errors");
            return "redirect:/home/result?error";
        }

    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int credentialId, RedirectAttributes redirectAttributes) {
        if (credentialId == 0) {
            redirectAttributes.addFlashAttribute("error", "Credential id is empty");
            return "redirect:/home/result?error";
        }
        try {
            credentialService.deleteCredential(credentialId);
            return "redirect:/home/result?success";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Delete Credential got err");
            return "redirect:/home/result?error";
        }
    }


}
