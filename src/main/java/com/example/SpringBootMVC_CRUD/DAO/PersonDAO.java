package com.example.SpringBootMVC_CRUD.DAO;

import com.example.SpringBootMVC_CRUD.Models.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

// будет общаться с базой данных
@Component
public class PersonDAO {
    private static final String URL = "jdbc:postgresql://localhost:5433/SpringBootMVC_CRUD";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "vyhuhol05.kg";

    private static Connection connection;

    // удостоверяемся тем что класс Driver загружен правильно
    static{
        try{
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException exc){
           exc.printStackTrace();
        }
        // если драйвер загружен подключаемся к бд
        try {
            connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // возвращает список людей
    public List<Person> index()  {
        List<Person> people = new ArrayList<>();

       try{
           Statement statement = connection.createStatement();
           String SQL = "SELECT * FROM person";
           ResultSet result = statement.executeQuery(SQL);
           // заполняем вручную поля сущности person используя результат запроса
           while(result.next()){
                Person person = new Person();
                person.setId(result.getInt("id"));
                person.setName(result.getString("name"));
                person.setAge(result.getInt("age"));
                person.setEmail(result.getString("email"));

                people.add(person);
           }

       }catch(SQLException e){
           e.printStackTrace();
       }

       return people;

    }

    public Person show(int id){
        Person person = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM person WHERE id = ?");
            preparedStatement.setInt(1,id);

            ResultSet result = preparedStatement.executeQuery();
            while(result.next()){
                person = new Person();
                person.setId(result.getInt("id"));
                person.setName(result.getString("name"));
                person.setAge(result.getInt("age"));
                person.setEmail(result.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public void save(Person person){

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into person (name, age, email) values(?,?,?)");
            preparedStatement.setString(1,person.getName());
            preparedStatement.setInt(2,person.getAge());
            preparedStatement.setString(3,person.getEmail());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void update(int id,Person updatedPerson){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE person SET name =?,age=?,email=? WHERE id =?");
            preparedStatement.setString(1,updatedPerson.getName());
            preparedStatement.setInt(2,updatedPerson.getAge());
            preparedStatement.setString(3,updatedPerson.getEmail());
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(int id){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM person WHERE id=?");

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}
