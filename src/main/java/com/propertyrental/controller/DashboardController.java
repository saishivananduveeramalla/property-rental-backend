package com.propertyrental.controller;

import com.propertyrental.dto.DashboardStatsDTO;
import com.propertyrental.entity.Role;
import com.propertyrental.entity.User;
import com.propertyrental.service.AuthService;
import com.propertyrental.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private AuthService authService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        User user = authService.getCurrentAuthenticatedUser();
        if (user.getRole() == Role.ROLE_ADMIN) {
            return ResponseEntity.ok(dashboardService.getAdminStats());
        } else if (user.getRole() == Role.ROLE_OWNER) {
            return ResponseEntity.ok(dashboardService.getOwnerStats());
        } else {
            return ResponseEntity.ok(dashboardService.getTenantStats());
        }
    }
}
