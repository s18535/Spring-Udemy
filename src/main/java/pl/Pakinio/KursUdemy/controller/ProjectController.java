package pl.Pakinio.KursUdemy.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.Pakinio.KursUdemy.logic.ProjectService;
import pl.Pakinio.KursUdemy.model.Project;
import pl.Pakinio.KursUdemy.model.ProjectStep;
import pl.Pakinio.KursUdemy.model.projection.ProjectWriteModel;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model, Authentication auth){
        //if (auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("project",new ProjectWriteModel());
            return "projects";
        //}
        //return "index";

    }

    @PostMapping
    String addProject(
            @ModelAttribute("project") @Valid ProjectWriteModel current,
            BindingResult bindingResult,
            Model model
    ){
        if (bindingResult.hasErrors()){
            return "projects";
        }
        service.save(current);
        model.addAttribute("project",new ProjectWriteModel());
        model.addAttribute("projects",getProjects());
        model.addAttribute("message","Dodano projekt");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current){
        current.getSteps().add(new ProjectStep());
        return "projects";

    }

   /* @PostMapping("/fake/{id}")
    String createGroupFake(
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")LocalDateTime deadline
    ){
        return createGroup(current,model,id,deadline);
    }*/

    @Timed(value = "project.create.group",histogram = true,percentiles = {0.5,0.95,0.99})
    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")LocalDateTime deadline
            ){
        try {
          service.createGroup(deadline,id);
          model.addAttribute("message","Dodano grupe!");
        }catch (IllegalStateException | IllegalArgumentException e){
            model.addAttribute("message","Bład podczas tworzenia grupy!");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects(){
        return service.readAll();
    }
}
