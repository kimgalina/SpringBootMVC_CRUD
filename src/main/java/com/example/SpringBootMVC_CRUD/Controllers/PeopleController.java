package com.example.SpringBootMVC_CRUD.Controllers;

import com.example.SpringBootMVC_CRUD.DAO.PersonDAO;
import com.example.SpringBootMVC_CRUD.Models.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
// все http запросы с url /people
public class PeopleController {

    private PersonDAO personDAO;

    @Autowired
    PeopleController(PersonDAO personDAO){
        this.personDAO = personDAO;
    }

    // возвращает список из людей
    @GetMapping("/")
    public String index(Model model){
        // добавляем в модель под ключом "people" значение - лист людей
        model.addAttribute("people",personDAO.index());
        // возвращаем название html
        return "people/index";
    }

    // возвращает какого-то конкретного человека
     @GetMapping("/{id}")
    // извлекаем id из url спом аннотации  @PathVariable
    public String show(@PathVariable("id") int id , Model model ){
        model.addAttribute("person",personDAO.show(id));
        return "people/show";
    }

    // по url localhost:/8080/people/new
    // будет отправлять html страницу с формой для создания нового человека
    @GetMapping("/new")
    public String newPerson(Model model){
        // тк мы будем использовать thymeleaf формы мы должны передавть им
        // тот обьект ( сущность ) для которого эта форма создается
        // а это можно сделать только через модель
        model.addAttribute("person",new Person());

        return "/people/new";
    }

    // этот метод будет принимать post запрос и будет добавлять нового человека в базу данных
    @PostMapping()
    //чтообы на этапе внедрения значений полей из html формы в обьект класса Person
    // все проверялось на валидность подключаем аннотацию @Valid
    // если появляется ошибка , то эта ошибка помещается в отдельный обьект - BindingResult
    //он всегда в параметрах функции должен идти после того обьекта который валидируется
    public String create(@ModelAttribute("person") @Valid Person person ,
                         BindingResult bindingResult){
        // получить данные из формы
        // создать нового человека
        //положить в этого человека данные , которые пришли из формы

        // если есть ошибки валидации то вернуть обратно на страницу с формой регистрации
        if(bindingResult.hasErrors())
            return "people/new";

        // добавить этого человека в базу данных
        personDAO.save(person);

        // когда человек будет добавлен в базу данных
        // браузер автоматом перейдет на url - localhost:8080/people
        return "redirect:/people/";

    }
    @GetMapping("/{id}/edit")
    public String edit(Model model , @PathVariable("id") int id){
        // ложим в модель человека именно с этим id
        model.addAttribute("person",personDAO.show(id));

        return "people/edit";
    }

    // из формы в edit.html мы будни принимать Person для этого нужна
    // аннотация @ModelAttribute чтобы она автматом
    // создавала новый обект person
    // этому обьекту присваивать данные из формы
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person ,BindingResult bindingResult,
                         @PathVariable("id") int id){

        if(bindingResult.hasErrors()){
            return "/people/edit";
        }
        personDAO.update(id,person);

        return "redirect:/people/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personDAO.delete(id);
        return "redirect:/people/";
    }

}
