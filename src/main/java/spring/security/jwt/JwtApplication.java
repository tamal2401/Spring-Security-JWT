package spring.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class JwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }

}

@Controller
class TemplateController {
    @GetMapping(value={"/","/login"})
    public String login() {
        return "login";
    }

    @PostMapping("/dashboard")
    public String home(Model model) {
        return "home";
    }
}

@Controller
class MainController {

    public final static List<Student> stuArr = Arrays.asList(
            new Student(1, "Tamal"),
            new Student(2, "Susanta"),
            new Student(3, "Sagnik")
    );

    @GetMapping("/api/student/{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer id) {
        return stuArr.stream()
                .filter(each -> each.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Student {id} does not exist", id)));
    }

    @GetMapping("/api/student/all")
    public List<Student> findAllStudents() {
        return stuArr;
    }

    @GetMapping("/api/index")
    public String index() {
        return "<h1>Welcome in spring boot</h1>";
    }
}

@Controller
class StudentManagementController {

    public final static List<Student> stuArr = Arrays.asList(
            new Student(1, "Tamal"),
            new Student(2, "Susanta"),
            new Student(3, "Sagnik")
    );

    @GetMapping("/api/management/all")
    public List<Student> findAllStudents() {
        return stuArr;
    }

    @PostMapping("/api/management")
    public void registerStudent(@RequestBody Student student) {
        System.out.println(student);

    }

    @DeleteMapping("/api/management/{studentId}")
    public void Delete(@PathVariable("studentId") Integer id) {

    }

    @PutMapping("/api/management/{studentId}")
    public void updateStudent(@PathVariable("studentId") Integer id, @RequestBody Student student) {
        System.out.println(id + " : " + student.toString());
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
class Student {
    private Integer id;
    private String name;
}