package tr.com.getir.getirfinalcase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tr.com.getir.getirfinalcase.exception.errormessages.GeneralErrorMessage;
import tr.com.getir.getirfinalcase.model.dto.request.UserUpdateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.GenericResponse;
import tr.com.getir.getirfinalcase.model.dto.response.UserResponse;
import tr.com.getir.getirfinalcase.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Librarian user operations")
public class UserManagementController {

    private final UserService userService;


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


    @Operation(
            summary = "Get all users",
            description = "Returns a list of all registered users. Accessible only by users with LIBRARIAN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public GenericResponse<List<UserResponse>> getAllUsers(){
        List<UserResponse> responses = userService.getAllUsers();
        return new GenericResponse<>(true, "Users retrieved successfully", responses);
    }


    @Operation(
            summary = "Update user by id",
            description = "Updates a user's profile by their id. Accessible only to LIBRARIAN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public GenericResponse<String> updateUserById(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request){
        userService.updateUser(id, request);
        return new GenericResponse<>(true, "User updated successfully", null);
    }


    @Operation(
            summary = "Delete user by id",
            description = "Deletes a user by their id. Accessible only to users with LIBRARIAN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - only LIBRARIAN can perform this operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public GenericResponse<Void> deleteUSer(@PathVariable Long id){
        userService.deleteUser(id);
        return new GenericResponse<>(true, "User deleted successfully", null);
    }
}
