package com.example.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SimpleBoardTest {
    
    private static int createdBoardId; // Store the board ID between tests
    
    // Common method to get all boards
    private java.util.List<Integer> getAllBoardIds() {
        Response getAllBoardsResponse = given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/boards")
                .then()
                .extract()
                .response();
        
        Assert.assertEquals(getAllBoardsResponse.getStatusCode(), 200, "Expected status code 200 for getting all boards");
        return getAllBoardsResponse.jsonPath().getList("id");
    }
    
    // Common method to verify board exists
    private void verifyBoardExists(int boardId) {
        java.util.List<Integer> boardIds = getAllBoardIds();
        Assert.assertTrue(boardIds.contains(boardId), 
                         "Board ID " + boardId + " should be present in the boards list");
        System.out.println("Verified that board ID " + boardId + " exists in the boards list");
    }
    
    // Common method to verify board does not exist
    private void verifyBoardDoesNotExist(int boardId) {
        java.util.List<Integer> boardIds = getAllBoardIds();
        Assert.assertFalse(boardIds.contains(boardId), 
                          "Board ID " + boardId + " should not be present in the boards list");
        System.out.println("Verified that board ID " + boardId + " is not in the boards list");
    }
    
    @Test
    public void testCreateBoard() {
        // Set base URI
        RestAssured.baseURI = "http://localhost:3000";
        
        // Get initial board count
        java.util.List<Integer> initialBoardIds = getAllBoardIds();
        System.out.println("Initial board IDs: " + initialBoardIds);
        
        // Request body
        String requestBody = "{\"name\":\"aaaa\"}";
        
        // Send POST request to create board
        Response createResponse = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/boards")
                .then()
                .extract()
                .response();
        
        // Assert status code is 201
        Assert.assertEquals(createResponse.getStatusCode(), 201, "Expected status code 201 for board creation");
        
        // Extract the ID from the response
        createdBoardId = createResponse.jsonPath().getInt("id");
        Assert.assertNotNull(createdBoardId, "Board ID should be returned in response");
        
        System.out.println("Created board with ID: " + createdBoardId);
        System.out.println("Response: " + createResponse.getBody().asPrettyString());
        
        // Verify the board was actually created by checking if it exists in the list
        verifyBoardExists(createdBoardId);
        
        // Verify we have one more board than before
        java.util.List<Integer> finalBoardIds = getAllBoardIds();
        Assert.assertEquals(finalBoardIds.size(), initialBoardIds.size() + 1, 
                           "Should have one more board after creation");
        System.out.println("Final board IDs: " + finalBoardIds);
    }
    
    @Test(dependsOnMethods = "testCreateBoard")
    public void testDeleteBoard() {
        // Set base URI
        RestAssured.baseURI = "http://localhost:3000";
        
        // Verify board exists before deletion
        verifyBoardExists(createdBoardId);
        
        // Delete the board using the ID from the previous test
        Response deleteResponse = given()
                .accept(ContentType.JSON)
                .when()
                .delete("/api/boards/" + createdBoardId)
                .then()
                .extract()
                .response();
        
        // Assert delete status code (usually 200 for successful deletion)
        Assert.assertEquals(deleteResponse.getStatusCode(), 200, "Expected status code 200 for board deletion");
        
        System.out.println("Successfully deleted board with ID: " + createdBoardId);
        
        // Verify the board is actually deleted
        verifyBoardDoesNotExist(createdBoardId);
    }
} 