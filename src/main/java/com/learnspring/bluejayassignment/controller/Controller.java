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
    public ResponseEntity<?> uploadData(@RequestParam("file") MultipartFile file) {
        if (ExcelHelper.checkFileType(file)) {
            // method to extract data from excel file
            this.assignmentService.getDataFromExcel(file);

            System.out.println();
            System.out.println("-----------------------------------------------------------------------------------------");

            // method to get name and position of an employee who has worked for seven consecutive days.
            System.out.println("name and position of an employee who has worked for seven consecutive days");
            this.assignmentService.getEmployeeWithSevenDays();

            // method to get name and position of an employee who has worked for more than 14 hours in a shift.
            System.out.println("name and position of an employee who has worked for more than 14 hours in a shift");
            this.assignmentService.getEmployeeWith14Hours();

            // method to get name and position of an employee who who have less than 10 hours of time between shifts but greater than 1 hour.
            System.out.println("name and position of an employee who who have less than 10 hours of time between shifts but greater than 1 hour");
            this.assignmentService.getEmployeeNameWithShift();

            return ResponseEntity.ok("Data Uplaoded.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please upload excel file only.");
    }
}