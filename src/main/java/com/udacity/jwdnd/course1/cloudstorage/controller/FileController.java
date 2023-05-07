package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileController {


    private final FileService fileService;

    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }


    @PostMapping()
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        String errorMsg = null;

        String userName = (String) authentication.getPrincipal();
        User user = userService.getUser(userName);

        if (fileUpload.isEmpty()) {
            errorMsg = "Please select a non-empty file.";
        }

        if (fileService.checkFileExist(fileUpload.getOriginalFilename(), user.getUserId())) {
            errorMsg = "The file already exists.";

        }

        if (errorMsg != null) {
            redirectAttributes.addFlashAttribute("error", errorMsg);
            return "redirect:/home/result?error";
        }

        try {
            fileService.insertFile(fileUpload, user.getUserId());
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "upload file got errors");
            return "redirect:/home/result?error";
        }
        return "redirect:/home/result?success";
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int fileId, RedirectAttributes redirectAttributes) {
        if (fileId == 0) {
            redirectAttributes.addAttribute("error", "File id is empty");
            return "redirect:/home/result?error";
        }

        fileService.deleteFile(fileId);
        return "redirect:/home/result?success";
    }

    @GetMapping("/view/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Integer fileId) {
        File file = fileService.getFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }


}
