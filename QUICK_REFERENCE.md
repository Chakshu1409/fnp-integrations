# Quick Reference Guide

## 🚀 Common Tasks

### 1. Creating a New Controller

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseDto<List<User>> getUsers() {
        List<User> users = userService.getUsers();
        return ResponseDto.success("Users retrieved successfully", users);
    }
    
    @GetMapping("/{id}")
    public ResponseDto<User> getUser(@PathVariable String id) {
        User user = userService.getUser(id);
        return ResponseDto.success("User retrieved successfully", user);
    }
    
    @PostMapping
    public ResponseDto<User> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        return ResponseDto.success("User created successfully", user);
    }
}
```

### 2. Creating a New Service

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final RestClient restClient;
    private final DynamicConstants constants;
    
    public List<User> getUsers() {
        String url = constants.getApiBaseUrl() + "/users";
        HttpHeaders headers = createHeaders();
        
        ParameterizedTypeReference<List<User>> responseType = 
            new ParameterizedTypeReference<List<User>>() {};
        
        return restClient.get(url, headers, responseType, true);
    }
    
    public User getUser(String id) {
        String url = constants.getApiBaseUrl() + "/users/" + id;
        HttpHeaders headers = createHeaders();
        
        return restClient.get(url, headers, User.class, true);
    }
    
    public User createUser(CreateUserRequest request) {
        String url = constants.getApiBaseUrl() + "/users";
        HttpHeaders headers = createHeaders();
        
        return restClient.post(url, headers, request, User.class, true);
    }
    
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAuthToken());
        headers.set("User-Agent", "FNP-Integrations/1.0");
        return headers;
    }
}
```

### 3. Creating DTOs

```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String name;
    private String email;
    private String phone;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
}
```

### 4. Error Handling Patterns

```java
// Validation error
if (request.getName() == null || request.getName().trim().isEmpty()) {
    throw new ResponseException("Name is required");
}

// Not found error
User user = userRepository.findById(id);
if (user == null) {
    throw new ResponseException(404, "User not found");
}

// Business logic error
if (user.getStatus().equals("INACTIVE")) {
    throw new ResponseException("Cannot perform action on inactive user");
}

// External API error with data
try {
    return externalApiService.callApi();
} catch (Exception e) {
    Map<String, Object> errorData = new HashMap<>();
    errorData.put("apiUrl", url);
    errorData.put("timestamp", System.currentTimeMillis());
    throw new ResponseException("External API call failed", errorData);
}
```

### 5. Adding New Configuration Properties

1. **Add to property files:**
```properties
# application-local.properties
my.feature.enabled=true
my.api.endpoint=/custom-endpoint
my.timeout=5000
```

2. **Add to DynamicConstants:**
```java
@Value("${my.feature.enabled:false}")
private boolean myFeatureEnabled;

@Value("${my.api.endpoint}")
private String myApiEndpoint;

@Value("${my.timeout:3000}")
private int myTimeout;
```

3. **Use in service:**
```java
@Service
public class MyService {
    private final DynamicConstants constants;
    
    public void doSomething() {
        if (constants.isMyFeatureEnabled()) {
            String endpoint = constants.getMyApiEndpoint();
            int timeout = constants.getMyTimeout();
            // Use the configuration
        }
    }
}
```

## 🔧 Common Commands

### Running the Application
```bash
# Local development
./mvnw spring-boot:run

# UAT environment
./mvnw spring-boot:run -Puat

# Production environment
./mvnw spring-boot:run -Pprod
```

### Testing Endpoints
```bash
# Health check
curl http://localhost:8080/api/config/health

# Get configuration
curl http://localhost:8080/api/config/info

# Test REST client
curl http://localhost:8080/api/config/test-rest-client

# Test error handling
curl "http://localhost:8080/api/config/test-error?type=response"
```

### Building the Project
```bash
# Clean and compile
./mvnw clean compile

# Run tests
./mvnw test

# Package the application
./mvnw package
```

## 📝 Code Templates

### Controller Template
```java
@RestController
@RequestMapping("/api/{resource}")
@RequiredArgsConstructor
@Slf4j
public class {Resource}Controller {
    
    private final {Resource}Service {resource}Service;
    
    @GetMapping
    public ResponseDto<List<{Resource}>> get{Resources}() {
        log.info("Getting all {resources}");
        List<{Resource}> {resources} = {resource}Service.get{Resources}();
        return ResponseDto.success("{Resources} retrieved successfully", {resources});
    }
    
    @GetMapping("/{id}")
    public ResponseDto<{Resource}> get{Resource}(@PathVariable String id) {
        log.info("Getting {resource} with id: {}", id);
        {Resource} {resource} = {resource}Service.get{Resource}(id);
        return ResponseDto.success("{Resource} retrieved successfully", {resource});
    }
    
    @PostMapping
    public ResponseDto<{Resource}> create{Resource}(@RequestBody Create{Resource}Request request) {
        log.info("Creating new {resource}");
        {Resource} {resource} = {resource}Service.create{Resource}(request);
        return ResponseDto.success("{Resource} created successfully", {resource});
    }
    
    @PutMapping("/{id}")
    public ResponseDto<{Resource}> update{Resource}(@PathVariable String id, @RequestBody Update{Resource}Request request) {
        log.info("Updating {resource} with id: {}", id);
        {Resource} {resource} = {resource}Service.update{Resource}(id, request);
        return ResponseDto.success("{Resource} updated successfully", {resource});
    }
    
    @DeleteMapping("/{id}")
    public ResponseDto<Void> delete{Resource}(@PathVariable String id) {
        log.info("Deleting {resource} with id: {}", id);
        {resource}Service.delete{Resource}(id);
        return ResponseDto.success("{Resource} deleted successfully", null);
    }
}
```

### Service Template
```java
@Slf4j
@Service
@RequiredArgsConstructor
public class {Resource}Service {
    
    private final RestClient restClient;
    private final DynamicConstants constants;
    
    public List<{Resource}> get{Resources}() {
        String url = constants.getApiBaseUrl() + "/{resources}";
        HttpHeaders headers = createHeaders();
        
        ParameterizedTypeReference<List<{Resource}>> responseType = 
            new ParameterizedTypeReference<List<{Resource}>>() {};
        
        return restClient.get(url, headers, responseType, true);
    }
    
    public {Resource} get{Resource}(String id) {
        String url = constants.getApiBaseUrl() + "/{resources}/" + id;
        HttpHeaders headers = createHeaders();
        
        return restClient.get(url, headers, {Resource}.class, true);
    }
    
    public {Resource} create{Resource}(Create{Resource}Request request) {
        String url = constants.getApiBaseUrl() + "/{resources}";
        HttpHeaders headers = createHeaders();
        
        return restClient.post(url, headers, request, {Resource}.class, true);
    }
    
    public {Resource} update{Resource}(String id, Update{Resource}Request request) {
        String url = constants.getApiBaseUrl() + "/{resources}/" + id;
        HttpHeaders headers = createHeaders();
        
        return restClient.put(url, headers, request, {Resource}.class, true);
    }
    
    public void delete{Resource}(String id) {
        String url = constants.getApiBaseUrl() + "/{resources}/" + id;
        HttpHeaders headers = createHeaders();
        
        restClient.delete(url, headers, Void.class, true);
    }
    
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAuthToken());
        headers.set("User-Agent", "FNP-Integrations/1.0");
        return headers;
    }
    
    private String getAuthToken() {
        // Implement your authentication logic here
        return "your-auth-token";
    }
}
```

## 🚨 Troubleshooting

### Common Error Messages

1. **"Connection refused"**
   - Check if external service is running
   - Verify URL in configuration
   - Check network connectivity

2. **"401 Unauthorized"**
   - Check authentication headers
   - Verify API keys/tokens
   - Check if credentials are expired

3. **"404 Not Found"**
   - Verify endpoint URL
   - Check if resource exists
   - Review API documentation

4. **"500 Internal Server Error"**
   - Check application logs
   - Verify request payload
   - Check external service status

### Debug Steps

1. **Enable debug logging:**
   ```properties
   logging.level.com.fnp=DEBUG
   logging.level.org.springframework.web=DEBUG
   ```

2. **Check REST client logs:**
   - Request/response details are automatically logged
   - Timing information is included
   - Error details are captured

3. **Test with curl:**
   ```bash
   curl -v http://localhost:8080/api/config/info
   ```

4. **Check environment:**
   ```bash
   echo $SPRING_PROFILES_ACTIVE
   ```

## 📚 Useful Links

- [Main README](./README.md) - Complete documentation
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring Web Docs](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Lombok Docs](https://projectlombok.org/features/all) 