package tr.com.getir.getirfinalcase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.getir.getirfinalcase.model.dto.response.GenericResponse;
import tr.com.getir.getirfinalcase.model.dto.response.UserResponse;
import tr.com.getir.getirfinalcase.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET USER PROFILE
    @Operation(
            summary = "Get authenticated user's profile",
            description = "Returns the profile information of the currently authenticated user (only for PATRON role)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action."),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/profile")
    @PreAuthorize("hasRole('PATRON')")
    public GenericResponse<UserResponse> getUser(Authentication authentication){
        UserResponse response = userService.getUser(authentication.getName());
        return new GenericResponse<>(true, "User details retrieved successfully", response);
    }
}
