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
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.service.AuthenticationService;
import tr.com.getir.getirfinalcase.service.UserService;


@RestController
@RequestMapping("/api/v1/users/profile")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Authenticated user profile operations")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;


    @Operation(
            summary = "Get authenticated user's profile",
            description = "Returns the profile information of the currently authenticated user. Accessible only by users with PATRON role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @GetMapping
    @PreAuthorize("hasRole('PATRON')")
    public GenericResponse<UserResponse> getUser(){
        User user =authenticationService.getAuthenticatedUser();
        UserResponse response = userService.getUserById(user.getId());
        return new GenericResponse<>(true, "User details retrieved successfully", response);
    }


    @Operation(
            summary = "Update authenticated user's profile",
            description = "Updates the profile of the currently authenticated user. Accessible only by users with PATRON role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })
    @PatchMapping
    @PreAuthorize("hasRole('PATRON')")
    public GenericResponse<Void> updateUser(@RequestBody @Valid UserUpdateRequest request){
        User user =authenticationService.getAuthenticatedUser();
        userService.updateUser(user.getId(), request);
        return new GenericResponse<>(true, "User updated successfully", null);
    }



    @Operation(
            summary = "Delete own user profile",
            description = "Deletes the currently authenticated user's profile. Accessible only by users with PATRON role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @DeleteMapping
    @PreAuthorize("hasRole('PATRON')")
    public GenericResponse<Void> deleteUser(){
        User user = authenticationService.getAuthenticatedUser();
        userService.deleteUser(user.getId());
        return new GenericResponse<>(true, "User deleted successfully", null);
    }

}
