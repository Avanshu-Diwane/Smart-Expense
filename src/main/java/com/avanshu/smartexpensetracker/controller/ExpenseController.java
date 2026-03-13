package com.avanshu.smartexpensetracker.controller;

import com.avanshu.smartexpensetracker.DTO.ExpenseDTO;
import com.avanshu.smartexpensetracker.entity.Expense;
import com.avanshu.smartexpensetracker.entity.User;
import com.avanshu.smartexpensetracker.repository.UserRepository;
import com.avanshu.smartexpensetracker.service.ExpenseService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseService expenseService, UserRepository userRepository) {
        this.expenseService = expenseService;
        this.userRepository = userRepository;
    }

    /**
     * Helper method to extract the user ID from the JWT/Security Context.
     * This ensures each user only sees and modifies their own data.
     */
    private Long getAuthenticatedUserId() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String) {
            String email = (String) principal;

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return user.getId();
        }

        throw new RuntimeException("Invalid authentication");
    }

    // Add expense linked to the authenticated user
    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {

        System.out.println("Expense Received: " + expense.getTitle());

        return expenseService.addExpense(getAuthenticatedUserId(), expense);
    }

    // Update only if the expense belongs to the authenticated user
    @PutMapping("/{expenseId}")
    public Expense updateExpense(@PathVariable Long expenseId, @RequestBody Expense expense) {
        return expenseService.updateExpense(getAuthenticatedUserId(), expenseId, expense);
    }

    // Delete only if the expense belongs to the authenticated user
    @DeleteMapping("/{expenseId}")
    public String deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(getAuthenticatedUserId(), expenseId);
        return "Expense deleted successfully";
    }

    // Monthly Total filtered by user
    @GetMapping("/total")
    public Map<String, Double> getMonthlyTotal(@RequestParam int month, @RequestParam int year) {
        Double total = expenseService.getMonthlyTotal(getAuthenticatedUserId(), month, year);
        return Map.of("total", total != null ? total : 0.0);
    }

    // Category Breakdown for the Pie Chart - filtered by user
    @GetMapping("/category-summary")
    public List<Map<String, Object>> getCategorySummary(@RequestParam int month, @RequestParam int year) {
        return expenseService.getCategorySummary(getAuthenticatedUserId(), month, year);
    }

    // Yearly Total filtered by user
    @GetMapping("/yearly-total")
    public Map<String, Double> getYearlyTotal(@RequestParam int year) {
        Double total = expenseService.getYearlyTotal(getAuthenticatedUserId(), year);
        return Map.of("total", total != null ? total : 0.0);
    }

    // List of expenses for the current user only
    @GetMapping
    public List<ExpenseDTO> getExpenses() {
        return expenseService.getExpensesByUser(getAuthenticatedUserId());
    }
}