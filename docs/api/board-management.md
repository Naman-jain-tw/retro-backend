# Board Management APIs

## Table of Contents
- [Create Board](#create-board)
- [Join Board](#join-board)

## Create Board

### Endpoint Details
`POST /api/boards`

### Purpose
Creates a new retrospective board and assigns the creator as an admin user.

### Request Parameters
| Parameter    | Type   | Required | Description             |
|-------------|--------|----------|-------------------------|
| name        | String | Yes      | Name of the board       |
| creatorName | String | Yes      | Name of the board creator|

### Flow
1. **Controller Layer** (`BoardController.createBoard`)
   - Receives HTTP POST request with name and creatorName
   - Validates request parameters
   - Delegates to service layer
   - Returns ResponseEntity with created board

2. **Service Layer** (`BoardService.createBoard`)
   - Creates new User entity for admin
   - Creates new Board entity
   - Creates BoardUser relationship with ADMIN role
   - Handles transaction management

3. **Repository Layer**
   - `UserRepository`: Persists new user
   - `BoardRepository`: Persists new board
   - `BoardUserRepository`: Persists user-board relationship

### Technical Implementation Details

#### Controller Layer
java
@RestController
@RequestMapping("/api/boards")
public class BoardController {
@PostMapping
@Operation(summary = "Create a new board")
public ResponseEntity<Board> createBoard(...)

**Key Annotations:**
- `@RestController`: [Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-restcontroller.html)
  - Combines @Controller and @ResponseBody
  - Automatically serializes responses to JSON
  - Handles REST endpoints

- `@Operation`: [Documentation](https://swagger.io/docs/specification/describing-parameters/)
  - OpenAPI documentation annotation
  - Provides API documentation in Swagger UI
  - Describes endpoint purpose and parameters

#### Service Layer
java
@Service
@Transactional
public class BoardService {
public Board createBoard(String name, String creatorName)


**Key Annotations:**
- `@Service`: [Documentation](https://docs.spring.io/spring-framework/reference/core/beans/stereotype-annotations.html)
  - Marks class as service layer component
  - Enables component scanning
  - Allows dependency injection

- `@Transactional`: [Documentation](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html)
  - Manages database transactions
  - Ensures data consistency
  - Provides automatic rollback on exceptions


## Join Board

### Endpoint Details
`POST /api/boards/{boardId}/join`

### Purpose
Allows a user to join an existing board as a regular user (non-admin).

### Parameters
| Parameter | Type   | Location | Required | Description          |
|-----------|--------|----------|----------|---------------------|
| boardId   | UUID   | Path     | Yes      | UUID of the board   |
| userName  | String | Query    | Yes      | Name of joining user|

### Flow
1. **Controller Layer** (`BoardController.joinBoard`)
   - Receives HTTP POST request
   - Validates board ID and username
   - Delegates to service layer
   - Returns ResponseEntity with BoardUser details

2. **Service Layer** (`BoardService.joinBoard`)
   - Finds existing board by UUID
   - Creates new User entity
   - Creates BoardUser relationship with USER role
   - Handles transaction management

3. **Repository Layer**
   - `BoardRepository`: Finds existing board
   - `UserRepository`: Persists new user
   - `BoardUserRepository`: Persists user-board relationship

### Technical Implementation Details

#### Entity Relationships

java
@Entity
public class BoardUser {
@ManyToOne
@JoinColumn(name = "board_id")
private Board board;
@ManyToOne
@JoinColumn(name = "user_id")
private User user;
}


**Key Annotations:**
- `@Entity`: [Documentation](https://jakarta.ee/specifications/persistence/3.0/apidocs/jakarta.persistence/jakarta/persistence/entity)
  - JPA entity annotation
  - Maps class to database table
  - Required for ORM functionality

- `@ManyToOne`: [Documentation](https://jakarta.ee/specifications/persistence/3.0/apidocs/jakarta.persistence/jakarta/persistence/manytoone)
  - Defines many-to-one relationship
  - Multiple users can join one board
  - JPA relationship mapping