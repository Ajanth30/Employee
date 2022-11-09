package com.example.EmolyeeService.Controller;

import com.example.EmolyeeService.Dto.EmployeeDto;
import com.example.EmolyeeService.Dto.UserDto;
import com.example.EmolyeeService.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/saveEmployee")
    public ResponseEntity<Object>saveEmp(@RequestBody EmployeeDto employeeDto){
        return ResponseEntity.ok().body(employeeService.SaveEmp(employeeDto));
    }

    @GetMapping("/getAllEmp")
    public ResponseEntity<Object>getAllEmployees(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @DeleteMapping("deleteEmployee/{id}")
    public ResponseEntity<Object>deleteEmpById(@PathVariable("id")int id){
        employeeService.deleteEmpById(id);
        return ResponseEntity.ok("deleted");
    }

    @PutMapping("editEmployee/{id}")
    public ResponseEntity<Object>deleteEmpById(@PathVariable("id")int id,@RequestBody EmployeeDto employeeDto){
        employeeService.editEmpById(id,employeeDto);
        return ResponseEntity.ok("edited");
    }










}
