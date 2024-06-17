package com.project.sudoku.controller;

import com.project.sudoku.model.Sudoku;
import com.project.sudoku.service.DynamoDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SudokuController {
    @Autowired
    private DynamoDBService dynamoDBService;

    @GetMapping("/getRandomItem")
    public Sudoku getRandomItem() {
        return dynamoDBService.getRandomItem();
    }
}
