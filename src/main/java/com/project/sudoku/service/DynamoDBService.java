package com.project.sudoku.service;

import com.project.sudoku.model.Sudoku;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
public class DynamoDBService {

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Sudoku> sudokuTable;

    public DynamoDBService() {
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of("ap-southeast-2"))
                .build();
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.sudokuTable = enhancedClient.table("SudokuPuzzles", TableSchema.fromBean(Sudoku.class));
    }

    public Sudoku getRandomItem() {
        try {
            // Generate a random UUID
            String randomUuid = UUID.randomUUID().toString();

            // Construct the exclusive start key
            Map<String, AttributeValue> lastKeyEvaluated = new HashMap<>();
            lastKeyEvaluated.put("puzzle_id", AttributeValue.builder().s(randomUuid).build());

            // Create a scan request with the exclusive start key and limit of 1
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(sudokuTable.tableName())
                    .exclusiveStartKey(lastKeyEvaluated)
                    .limit(1)
                    .build();

            // Perform the scan operation
            ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
            if (scanResponse.count() > 0) {
                Map<String, AttributeValue> item = scanResponse.items().get(0);
                return convertItemToSudoku(item);
            } else {
                System.err.println("No items found in the table");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Unable to read item: " + e.getMessage());
            return null;
        }
    }

    private Sudoku convertItemToSudoku(Map<String, AttributeValue> item) {
        Sudoku sudoku = new Sudoku();
        sudoku.setPuzzle_id(item.get("puzzle_id").s());
        sudoku.setClues(Integer.parseInt(item.get("clues").n()));
        sudoku.setDifficulty(item.get("difficulty").s());
        sudoku.setPuzzle(convertListAttribute(item.get("puzzle").l()));
        sudoku.setSolution(convertListAttribute(item.get("solution").l()));
        return sudoku;
    }

    private List<List<String>> convertListAttribute(List<AttributeValue> attributeList) {
        return attributeList.stream()
                .map(attribute -> attribute.l().stream().map(AttributeValue::s).toList())
                .toList();
    }

    public void close() {
        dynamoDbClient.close();
    }

    public static void main(String[] args) {
        DynamoDBService service = new DynamoDBService();
        Sudoku item = service.getRandomItem();
        if (item != null) {
            System.out.println("Item retrieved: " + item);
        }
        service.close();
    }
}
