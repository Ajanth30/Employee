package com.example.EmolyeeService.ServiceImpl;

import com.example.EmolyeeService.Dto.EmployeeDto;
import com.example.EmolyeeService.Dto.UserDto;
import com.example.EmolyeeService.Entity.Employee;
import com.example.EmolyeeService.Repository.EmployeeRepository;
import com.example.EmolyeeService.Service.EmailService;
import com.example.EmolyeeService.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.EmolyeeService.Util.Constants.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private WebClient webClient;
    @Autowired
    private EmailService emailService;


    List<String> defectDesignations= Arrays.asList("QAE","SE","HR");
    List<Integer>defectEmployees=new ArrayList<>();
    @Override
    public String SaveEmp(EmployeeDto employeeDto) {
        Employee employee=new Employee();
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setGender(employeeDto.getGender());
        employee.setDesignation(employeeDto.getDesignation());
        employee.setAddress(employeeDto.getAddress());
        if(employeeRepo.existsByEmail(employee.getEmail())){
            return "Employee Already exists";
        }else {
            String response="Employee added\n";
            employeeRepo.save(employee);
            for(String designations:defectDesignations){
                if(employee.getDesignation().contains(designations)){
                    UserDto userDto=new UserDto();
                    userDto.setEmp_id(employee.getId());
                    userDto.setUserName(employee.getEmail());
                    userDto.setName(employee.getName());
                    userDto.setPassword(employeeDto.getPassword());
                    userDto.setDesignation(employee.getDesignation());
                    String body="Hi"+", "+employeeDto.getName()+"\n"
                            +"You can LogIn by Use the following credentials"+"\nUsername : -"+employee.getEmail()
                            +"\nPassword : "+employeeDto.getPassword()+"\nTo confirm your account, please click here : "+
                            "\n"+DEFECT_TRACKER_URL;
                    emailService.SendVerificationMail(employeeDto.getEmail(),body);
                    defectEmployees.add(employee.getId());
                    System.out.println(defectEmployees);
                    response+=webClient.post().uri(SAVE_IN_DEFECT_TRACKER).bodyValue(userDto).retrieve()
                            .bodyToMono(String.class).block();



                }
            }return response;

        }

    }

    @Override
    public Employee getEmpById(int id) {

        return employeeRepo.findById(id).orElse(new Employee());
    }


    @Override
    public void deleteEmpById(int id) {
        for(int i:defectEmployees){
            if(i==id){
                webClient.delete().uri(DELETE_DEFECT_TRACKER_USERS+id)
                        .retrieve().bodyToMono(String.class).block();
                break;
            }
        }
        employeeRepo.deleteById(id);
    }
    @Override
    public void editEmpById(int id, EmployeeDto employeeDto) {
        Employee employee=employeeRepo.findById(id).orElse(new Employee());
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setGender(employeeDto.getGender());
        employee.setDesignation(employeeDto.getDesignation());
        employee.setAddress(employeeDto.getAddress());
        employeeRepo.save(employee);

    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }


}

