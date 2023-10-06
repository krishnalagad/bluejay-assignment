package com.learnspring.bluejayassignment.service;

import com.learnspring.bluejayassignment.helper.ExcelHelper;
import com.learnspring.bluejayassignment.model.Bluejay;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AssignmentService {

    List<Bluejay> list = null;

    public void getDataFromExcel(MultipartFile file) {
        try{
            list = ExcelHelper.convertExcelToList(file.getInputStream());
            for (Bluejay b: list)
                System.out.println(b);
            System.out.println("Size of data: " + list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getEmployeeNameAndPosition() {
        
    }
}