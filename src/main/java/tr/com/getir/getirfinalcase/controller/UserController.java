package tr.com.getir.getirfinalcase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.getir.getirfinalcase.exception.errormessages.GeneralErrorMessage;
import tr.com.getir.getirfinalcase.model.dto.response.GenericResponse;
import tr.com.getir.getirfinalcase.model.dto.response.UserResponse;
import tr.com.getir.getirfinalcase.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operations related to user management")
public class UserController {

    private final UserService userService;

    // GET USER PROFILE
    @Operation(
            summary = "Get authenticated user's profile",
            description = "Returns the profile information of the currently authenticated user. Accessible only by users with PATRON role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @GetMapping("/profile")
    @PreAuthorize("hasRole('PATRON')")
    public GenericResponse<UserResponse> getUser(Authentication authentication){
        UserResponse response = userService.getUser(authentication.getName());
        return new GenericResponse<>(true, "User details retrieved successfully", response);
    }

    // GET USER BY ID
    @Operation(
            summary = "Get user by id",
            description = "Retrieves user details by user id. Accessible only by users with LIBRARIAN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public GenericResponse<UserResponse> getUserById(@PathVariable Long id){
        UserResponse response = userService.getUserById(id);
        return new GenericResponse<>(true, "User details retrieved successfully", response);
    }

    // GET ALL USERS
    @Operation(
            summary = "Get all users",
            description = "Returns a list of all registered users. Accessible only by users with LIBRARIAN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.")
    })

    @GetMapping("/")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public GenericResponse<List<UserResponse>> getAllUsers(){
        List<UserResponse> responses = userService.getAllUsers();
        return new GenericResponse<>(true, "Users retrieved successfully", responses);
    }
}
