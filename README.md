# Simple REST Assured Test

A minimal REST Assured test that creates and deletes a board, with verification using GET requests.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Your API server running on `http://localhost:3000`

## Running the Test

1. Make sure your API server is running on `http://localhost:3000`
2. Open a terminal in the `apiTesting` directory
3. Run the test using Maven:

```bash
mvn test
```

## The Two Tests

### Test 1: `testCreateBoard()`
- Creates a board with `{"name":"aaaa"}`
- Verifies the board was created by checking if it exists in the boards list
- Confirms the total board count increased by 1

### Test 2: `testDeleteBoard()`
- Deletes the board created in Test 1
- Verifies the board was deleted by checking it's no longer in the boards list

## GET Command Usage

The tests use GET requests to verify board operations:

```bash
GET http://localhost:3000/api/boards
```

This command:
- Fetches all boards
- Returns a list of board objects with IDs
- Used to verify if a board exists or was deleted

## Test Flow

1. **Before Creation**: GET all boards to get initial count
2. **Create Board**: POST request to create board
3. **After Creation**: GET all boards to verify board exists
4. **Before Deletion**: GET all boards to confirm board exists
5. **Delete Board**: DELETE request to remove board
6. **After Deletion**: GET all boards to verify board is gone

## Project Structure

```
apiTesting/
├── pom.xml                    # Maven configuration
├── testng.xml                 # TestNG configuration
├── README.md                  # This file
└── src/
    └── test/
        └── java/
            └── com/
                └── example/
                    └── tests/
                        └── SimpleBoardTest.java  # The test file
```

## Important Notes

- **Server Required**: Your API must be running on `http://localhost:3000`
- **Dependencies**: Test 2 depends on Test 1 (runs in sequence)
- **Verification**: Uses GET requests to ensure operations actually worked
- **Cleanup**: Automatically deletes the created board 