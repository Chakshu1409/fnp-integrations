# FNP Integrations

A Spring Boot application for handling third-party integrations with standardized configuration management, error handling, and REST client utilities.

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Running the Application

**Local Environment (Default):**
```bash
./mvnw spring-boot:run
```

**UAT Environment:**
```bash
./mvnw spring-boot:run -Puat
```

**Production Environment:**
```bash
./mvnw spring-boot:run -Pprod
```

## 📁 Project Structure

```
src/main/java/com/fnp/integrations/
├── config/                     # Configuration classes
│   └── RestTemplateConfig.java
├── constants/                  # Dynamic configuration constants
│   └── DynamicConstants.java
├── controller/                 # REST controllers
│   └── ConfigController.java
├── dto/                       # Data Transfer Objects
│   └── ResponseDto.java
├── enums/                     # Enumerations
│   └── ResponseStatus.java
├── exception/                 # Exception handling
│   ├── ResponseException.java
│   └── GlobalExceptionHandler.java
└── service/                   # Business logic services
    ├── RestClient.java        # REST client interface
    ├── impl/
    │   └── RestClientImpl.java
    └── ExternalApiService.java
```

## 🔧 Core Components

### 1. Environment Configuration

The project supports three environments with automatic profile switching:

#### Property Files
- `application.properties` - Base configuration
- `application-local.properties` - Local development settings
- `application-uat.properties` - UAT environment settings
- `application-prod.properties` - Production environment settings

#### Maven Profiles
- `local` - Sets `spring.profiles.active=local` (default)
- `uat` - Sets `spring.profiles.active=uat`
- `prod` - Sets `spring.profiles.active=prod`

### 2. DynamicConstants

Centralized configuration access using `@Value` annotations.

```java
@Service
public class MyService {
    private final DynamicConstants constants;
    
    public void doSomething() {
        String apiUrl = constants.getApiBaseUrl();
        boolean debugMode = constants.isDebugMode();
        int timeout = constants.getApiTimeout();
    }
}
```

**Available Properties:**
- `applicationName` - Application name
- `activeProfile` - Current Spring profile
- `debugMode` - Debug mode flag
- `cacheEnabled` - Cache enabled flag
- `apiBaseUrl` - Base URL for external APIs
- `apiTimeout` - API timeout in milliseconds
- `apiRetryCount` - Number of retry attempts
- `securityEnabled` - Security enabled flag
- `jwtExpiration` - JWT expiration time
- `serverPort` - Server port
- `contextPath` - Application context path

### 3. ResponseDto - Standardized API Responses

All controllers should return `ResponseDto<T>` for consistent API responses.

#### Success Response Format:
```json
{
  "status": "SUCCESS",
  "statusCode": 200,
  "errorCode": null,
  "message": "Operation completed successfully",
  "response": { /* actual data */ },
  "responseData": null
}
```

#### Error Response Format:
```json
{
  "status": "ERROR",
  "statusCode": 400,
  "errorCode": 1001,
  "message": "Error description",
  "response": { /* error details */ },
  "responseData": null
}
```

#### Usage in Controllers:
```java
@RestController
public class MyController {
    
    @GetMapping("/data")
    public ResponseDto<List<Data>> getData() {
        List<Data> data = service.getData();
        return ResponseDto.success("Data retrieved successfully", data);
    }
    
    @PostMapping("/data")
    public ResponseDto<Data> createData(@RequestBody DataRequest request) {
        try {
            Data data = service.createData(request);
            return ResponseDto.success("Data created successfully", data);
        } catch (ValidationException e) {
            return ResponseDto.error(400, 1001, "Invalid input provided");
        }
    }
}
```

### 4. ResponseException - Custom Exception Handling

Use `ResponseException` instead of `RuntimeException` for consistent error handling.

#### Basic Usage:
```java
// Simple error with default 400 status code
throw new ResponseException("Something went wrong");

// Custom error code
throw new ResponseException(404, "Resource not found");

// With additional data
Map<String, Object> errorData = new HashMap<>();
errorData.put("field", "value");
throw new ResponseException("Error with data", errorData);

// With custom HTTP status
throw new ResponseException("Bad request", HttpStatus.BAD_REQUEST);
```

#### Error Handling:
The `GlobalExceptionHandler` automatically catches `ResponseException` and returns standardized error responses.

### 5. RestClient - Centralized HTTP Client

**IMPORTANT**: Always use the `RestClient` for making external API calls. Never use `RestTemplate` directly.

#### Basic Usage:

```java
@Service
public class MyIntegrationService {
    private final RestClient restClient;
    private final DynamicConstants constants;
    
    // GET request
    public UserData getUserData(String userId) {
        String url = constants.getApiBaseUrl() + "/users/" + userId;
        HttpHeaders headers = createHeaders();
        
        return restClient.get(url, headers, UserData.class, true);
    }
    
    // POST request with payload
    public UserData createUser(CreateUserRequest request) {
        String url = constants.getApiBaseUrl() + "/users";
        HttpHeaders headers = createHeaders();
        
        return restClient.post(url, headers, request, UserData.class, true);
    }
    
    // PUT request
    public UserData updateUser(String userId, UpdateUserRequest request) {
        String url = constants.getApiBaseUrl() + "/users/" + userId;
        HttpHeaders headers = createHeaders();
        
        return restClient.put(url, headers, request, UserData.class, true);
    }
    
    // DELETE request
    public void deleteUser(String userId) {
        String url = constants.getApiBaseUrl() + "/users/" + userId;
        HttpHeaders headers = createHeaders();
        
        restClient.delete(url, headers, Void.class, true);
    }
    
    // Complex response types
    public List<UserData> getAllUsers() {
        String url = constants.getApiBaseUrl() + "/users";
        HttpHeaders headers = createHeaders();
        
        ParameterizedTypeReference<List<UserData>> responseType = 
            new ParameterizedTypeReference<List<UserData>>() {};
        
        return restClient.get(url, headers, responseType, true);
    }
    
    // Safe calls (returns null instead of throwing exception)
    public UserData getUserDataSafe(String userId) {
        String url = constants.getApiBaseUrl() + "/users/" + userId;
        HttpHeaders headers = createHeaders();
        
        return restClient.get(url, headers, UserData.class, false); // failFast=false
    }
    
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAuthToken());
        headers.set("User-Agent", "FNP-Integrations/1.0");
        return headers;
    }
}
```

#### RestClient Methods:

| Method | Description | Parameters |
|--------|-------------|------------|
| `get()` | GET request | URL, headers, return type, failFast |
| `post()` | POST request | URL, headers, payload, return type, failFast |
| `put()` | PUT request | URL, headers, payload, return type, failFast |
| `delete()` | DELETE request | URL, headers, return type, failFast |

#### Parameters:
- `url` - Target URL
- `headers` - HTTP headers
- `payload` - Request body (for POST/PUT)
- `returnType` - Expected response type
- `failFast` - If true, throws exception on error; if false, returns null

#### Error Handling:
The RestClient automatically handles:
- **400 Bad Request** → `ResponseException` with 400 status
- **401 Unauthorized** → Automatic retry, then `ResponseException`
- **404 Not Found** → `ResponseException` with 404 status
- **429 Rate Limited** → `ResponseException` with 429 status
- **Network/Server Errors** → `ResponseException` with 500 status

### 6. ResponseStatus Enum

Standardized error codes and messages.

```java
// In your service
throw new ResponseException(
    ResponseStatus.MICROSERVICE_INTERNAL_ERROR.getErrorCode(),
    ResponseStatus.MICROSERVICE_INTERNAL_ERROR.getErrorMessage()
);
```

**Available Status Codes:**
- `SUCCESS(200)` - Success
- `BAD_REQUEST(400)` - Bad request
- `UNAUTHORIZED(401)` - Unauthorized
- `NOT_FOUND(404)` - Resource not found
- `INTERNAL_SERVER_ERROR(500)` - Internal server error
- `MICROSERVICE_INTERNAL_ERROR(500)` - Microservice error
- `MICROSERVICE_EXCHANGE_ERROR(502)` - Exchange error
- `INTEGRATION_FAILED(501)` - Integration failed
- `EXTERNAL_API_ERROR(502)` - External API error

## 🛠️ Best Practices

### 1. Controller Guidelines

✅ **DO:**
```java
@GetMapping("/users")
public ResponseDto<List<User>> getUsers() {
    List<User> users = userService.getUsers();
    return ResponseDto.success("Users retrieved successfully", users);
}
```

❌ **DON'T:**
```java
@GetMapping("/users")
public List<User> getUsers() {
    return userService.getUsers(); // Direct return without ResponseDto
}
```

### 2. Exception Handling

✅ **DO:**
```java
public void processData(String data) {
    if (data == null) {
        throw new ResponseException("Data cannot be null");
    }
    // Process data
}
```

❌ **DON'T:**
```java
public void processData(String data) {
    if (data == null) {
        throw new RuntimeException("Data cannot be null"); // Use ResponseException instead
    }
    // Process data
}
```

### 3. External API Calls

✅ **DO:**
```java
public UserData callExternalApi(String userId) {
    String url = constants.getApiBaseUrl() + "/users/" + userId;
    HttpHeaders headers = createHeaders();
    return restClient.get(url, headers, UserData.class, true);
}
```

❌ **DON'T:**
```java
public UserData callExternalApi(String userId) {
    RestTemplate restTemplate = new RestTemplate(); // Don't use RestTemplate directly
    return restTemplate.getForObject(url, UserData.class);
}
```

### 4. Configuration Access

✅ **DO:**
```java
@Service
public class MyService {
    private final DynamicConstants constants;
    
    public void doSomething() {
        String apiUrl = constants.getApiBaseUrl();
        // Use constants
    }
}
```

❌ **DON'T:**
```java
@Service
public class MyService {
    @Value("${api.base-url}")
    private String apiUrl; // Don't use @Value directly in services
    
    public void doSomething() {
        // Use apiUrl
    }
}
```

## 📋 API Endpoints

### Configuration Endpoints
- `GET /api/config/info` - Get application configuration
- `GET /api/config/health` - Health check
- `GET /api/config/test-error` - Test error handling
- `GET /api/config/test-rest-client` - Test REST client

### Example Response:
```bash
curl http://localhost:8080/api/config/info
```

```json
{
  "status": "SUCCESS",
  "statusCode": 200,
  "errorCode": null,
  "message": "Configuration information retrieved successfully",
  "response": {
    "applicationName": "fnp-integrations",
    "activeProfile": "local",
    "debugMode": true,
    "apiBaseUrl": "http://localhost:3000",
    "apiTimeout": 5000,
    "apiRetryCount": 3
  },
  "responseData": null
}
```

## 🔍 Debugging

### Logging
The application uses SLF4J with Lombok's `@Slf4j` annotation.

```java
@Slf4j
@Service
public class MyService {
    public void doSomething() {
        log.info("Processing request");
        log.debug("Debug information: {}", someData);
        log.error("Error occurred", exception);
    }
}
```

### REST Client Logging
The RestClient automatically logs:
- Request details (URL, method, headers, payload)
- Response details
- Timing information
- Error details

### Environment-Specific Logging
- **Local**: DEBUG level logging
- **UAT**: INFO level logging
- **Production**: WARN/ERROR level logging

## 🚨 Common Issues & Solutions

### 1. Connection Refused Errors
**Problem:** External API calls failing with "Connection refused"
**Solution:** Check if the external service is running and the URL is correct

### 2. 401 Unauthorized Errors
**Problem:** API calls returning 401
**Solution:** Check authentication headers and tokens

### 3. Timeout Errors
**Problem:** API calls timing out
**Solution:** Adjust timeout settings in `RestTemplateConfig`

### 4. Profile Not Switching
**Problem:** Environment-specific properties not loading
**Solution:** Ensure you're using the correct Maven profile (`-Plocal`, `-Puat`, `-Pprod`)

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Web Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Lombok Documentation](https://projectlombok.org/features/all)

## 🤝 Contributing

1. Follow the established patterns for controllers, services, and exceptions
2. Always use `ResponseDto` for API responses
3. Use `ResponseException` for error handling
4. Use `RestClient` for external API calls
5. Use `DynamicConstants` for configuration access
6. Add appropriate logging
7. Test your changes with different profiles

## 📞 Support

For questions or issues:
1. Check this README first
2. Review the example code in `ConfigController` and `ExternalApiService`
3. Check the logs for detailed error information
4. Contact the team lead for additional guidance 