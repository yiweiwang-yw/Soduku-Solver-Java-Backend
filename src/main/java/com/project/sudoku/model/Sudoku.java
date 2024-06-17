package com.project.sudoku.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@DynamoDbBean
public class Sudoku {
    private String puzzle_id;
    private int clues;
    private String difficulty;
    private List<List<String>> puzzle;
    private List<List<String>> solution;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("puzzle_id")
    public String getPuzzle_id() {
        return puzzle_id;
    }

    public void setPuzzle_id(String puzzle_id) {
        this.puzzle_id = puzzle_id;
    }

    @DynamoDbAttribute("clues")
    public int getClues() {
        return clues;
    }

    public void setClues(int clues) {
        this.clues = clues;
    }

    @DynamoDbAttribute("difficulty")
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @DynamoDbAttribute("puzzle")
    public List<List<String>> getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(List<List<String>> puzzle) {
        this.puzzle = puzzle;
    }

    @DynamoDbAttribute("solution")
    public List<List<String>> getSolution() {
        return solution;
    }

    public void setSolution(List<List<String>> solution) {
        this.solution = solution;
    }
}
