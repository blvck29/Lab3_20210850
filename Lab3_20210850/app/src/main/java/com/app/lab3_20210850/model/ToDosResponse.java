package com.app.lab3_20210850.model;

import java.util.ArrayList;

public class ToDosResponse {
    private ArrayList<ToDo> todos;

    public ArrayList<ToDo> getTodos() {
        return todos;
    }

    public void setTodos(ArrayList<ToDo> todos) {
        this.todos = todos;
    }
}