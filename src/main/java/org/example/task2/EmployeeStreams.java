package org.example.task2;

import java.util.List;

public class EmployeeStreams {

    public List<Employee> getThreeEldestEngineers(List<Employee> list) {
        return list.stream()
                .filter(employee -> employee.jobTitle().equals("Engineer"))
                .sorted((o1,o2)-> o2.age() - o1.age())
                .limit(3)
                .toList();
    }

    public Double getAverageAge(List<Employee> list) {
        return list.stream()
                .filter(employee -> employee.jobTitle().equals("Engineer"))
                .mapToInt(Employee::age)
                .average()
                .orElseThrow();
    }
}
