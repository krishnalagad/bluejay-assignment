package com.learnspring.bluejayassignment.controller;

import com.learnspring.bluejayassignment.helper.ExcelHelper;
import com.learnspring.bluejayassignment.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/ass")
public class Controller {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadData(@RequestParam("file")MultipartFile file) {
        if (ExcelHelper.checkFileType(file)) {
            this.assignmentService.getDataFromExcel(file);
            return ResponseEntity.ok("Data Uplaoded.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please upload excel file only.");
    }
}