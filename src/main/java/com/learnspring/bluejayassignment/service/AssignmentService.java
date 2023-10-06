package com.learnspring.bluejayassignment.service;

import com.learnspring.bluejayassignment.helper.ExcelHelper;
import com.learnspring.bluejayassignment.model.Bluejay;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Array;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    List<Bluejay> list = null;
    Map<String, Integer> map = new LinkedHashMap<>();

    public void getDataFromExcel(MultipartFile file) {
        try {
            list = ExcelHelper.convertExcelToList(file.getInputStream());
//            for(Bluejay b: this.list)
//                System.out.println(b);
            System.out.println("Size of data: " + list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getEmployeeNameAndPosition() {
        list.forEach(emp -> {
            System.out.println("Employee Name: " + emp.getEmployeeName() + "    " + "Employee Position: "
                    + emp.getPositionStatus() + "    " + "Employee ID: " + emp.getPositionId());
        });
    }

    public void getEmployeeWithSevenDays() {
        List<String> idList = new ArrayList<>();
        Set<Bluejay> setSevenDays = new LinkedHashSet<>();
        this.list.forEach(emp -> {
            idList.add(emp.getPositionId());
        });
        map = this.getOccuranceOfFileNumbers(idList);
//        System.out.println(map);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
//            System.out.println(key + "-> " + value);
            if (value >= 7 && value < 10) {
                Bluejay b = this.getById(key);
//                System.out.println(b);
                setSevenDays.add(b);
            }
        }
        System.out.println();
        for (Bluejay b : setSevenDays) {
            System.out.println("Name: " + b.getEmployeeName() + "    " + "Position: " + b.getPositionStatus() + "    "
                    + "ID: " + b.getPositionId());
        }
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    public void getEmployeeWith14Hours() {
        Set<Bluejay> setFourteenHours = new LinkedHashSet<>();
        for (Bluejay b : this.list) {
            if (!b.getTime().isEmpty() && !b.getTimeOut().isEmpty()) {
                double time = Double.parseDouble(b.getTime());
                double timeOut = Double.parseDouble(b.getTimeOut());
                String startTime = AssignmentService.parseExcelDateTime(time);
                String endTime = AssignmentService.parseExcelDateTime(timeOut);
                boolean hours = AssignmentService.isTimeDifferenceGreaterThan14Hours(startTime, endTime);
                if (hours) {
                    setFourteenHours.add(b);
                }
            }
        }
        System.out.println();
        for (Bluejay b : setFourteenHours) {
            System.out.println("Name: " + b.getEmployeeName() + "    " + "Position: " + b.getPositionStatus() + "    "
                    + "ID: " + b.getPositionId());
        }
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    public void getEmployeeNameWithShift() {
        List<List<Bluejay>> l = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : this.map.entrySet()) {
            int value = entry.getValue();
            String key = entry.getKey();
            if (value > 1) {
                List<Bluejay> raw = new ArrayList<>();
                for (Bluejay b : this.list) {
                    if (key.equals(b.getPositionId())) {
                        raw.add(b);
                    }
                }
                l.add(raw);
            }
        }

        List<Bluejay> filteredShifts = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            List<Bluejay> bluejays = l.get(i);
            for (int j = 1; j < bluejays.size(); j++) {
                Bluejay currentShift = bluejays.get(j - 1);
                Bluejay nextShift = bluejays.get(j);
                if (!currentShift.getTimeOut().isEmpty() && !nextShift.getTime().isEmpty()) {
                    double currentTime = Double.parseDouble(currentShift.getTimeOut());
                    double nextTime = Double.parseDouble(nextShift.getTime());
                    String currentTimeShift = AssignmentService.parseExcelDateTime(currentTime);
                    String nextTimeShift = AssignmentService.parseExcelDateTime(nextTime);

                    long hoursDiff = AssignmentService
                            .isTimeDifferenceGreaterThan1HourAndLessThan10Hours(currentTimeShift, nextTimeShift);

                    if (hoursDiff > 1 && hoursDiff < 10) {
                        filteredShifts.add(bluejays.get(j-1));
                        filteredShifts.add(bluejays.get(j));
                    }
                }
            }
        }

        System.out.println();
        for (Bluejay b : filteredShifts) {
            System.out.println("Name: " + b.getEmployeeName() + "    " + "Position: " + b.getPositionStatus() + "    "
                    + "ID: " + b.getPositionId());
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        
    }

    // ----------------------------------------------Helper Methods-----------------------------------------------------

    private Map<String, Integer> getOccuranceOfFileNumbers(List<String> list) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < list.size(); i++) {
            int count = 0;
            for (int j = 0; j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    count += 1;
                }
            }
            map.put(list.get(i), count);
        }
        return map;
    }

    private Bluejay getById(String id) {
        Set<Bluejay> set = new LinkedHashSet<>();
        Bluejay b = null;
        this.list.forEach(emp -> {
            if (id.equals(emp.getPositionId()))
                set.add(emp);
        });
        Iterator<Bluejay> iterator = set.iterator();
        if (iterator.hasNext())
            b = iterator.next();
        return b;
    }

    private static boolean isTimeDifferenceGreaterThan14Hours(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);

        // Calculate the difference in hours
        long hoursDifference = java.time.Duration.between(startDateTime, endDateTime).toHours();

        return hoursDifference > 14;
    }

    private static long isTimeDifferenceGreaterThan1HourAndLessThan10Hours(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        LocalDateTime timeOut = LocalDateTime.parse(startTime, formatter);
        LocalDateTime time = LocalDateTime.parse(endTime, formatter);

        // Calculate the difference in hours
        long hoursDifference = java.time.Duration.between(timeOut, time).toHours();

        return hoursDifference;
    }

    public static String parseExcelDateTime(double excelDate) {
        LocalDateTime dateTime = LocalDateTime.of(1900, 1, 1, 0, 0)
                .plusDays((long) excelDate - 2) // Subtracting 2 to account for Excel's date epoch starting on January 1, 1900
                .plusSeconds((long) ((excelDate % 1) * 86_400)); // 86,400 seconds in a day

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a");
        return dateTime.format(formatter);
    }
}