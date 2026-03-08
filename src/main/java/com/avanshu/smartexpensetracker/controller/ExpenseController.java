package com.avanshu.smartexpensetracker.controller;

import com.avanshu.smartexpensetracker.DTO.ExpenseDTO;
import com.avanshu.smartexpensetracker.entity.Expense;
import com.avanshu.smartexpensetracker.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // Use a fixed User ID to bypass authentication issues during deployment
    private final Long TEST_USER_ID = 1L;

    // Add expense
    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {
        return expenseService.addExpense(TEST_USER_ID, expense);
    }

    // Update expense
    @PutMapping("/{expenseId}")
    public Expense updateExpense(@PathVariable Long expenseId, @RequestBody Expense expense) {
        return expenseService.updateExpense(TEST_USER_ID, expenseId, expense);
    }

    // Delete expense
    @DeleteMapping("/{expenseId}")
    public String deleteExpense(@PathVariable Long expenseId) {
        expenseService.deleteExpense(TEST_USER_ID, expenseId);
        return "Expense deleted successfully";
    }

    // Monthly Total for Dashboard Cards
    @GetMapping("/total")
    public Map<String, Double> getMonthlyTotal(@RequestParam int month, @RequestParam int year) {
        Double total = expenseService.getMonthlyTotal(TEST_USER_ID, month, year);
        return Map.of("total", total != null ? total : 0.0);
    }

    // Category Breakdown for Pie Chart
    @GetMapping("/category-summary")
    public List<Map<String, Object>> getCategorySummary(@RequestParam int month, @RequestParam int year) {
        return expenseService.getCategorySummary(TEST_USER_ID, month, year);
    }

    // Yearly Total
    @GetMapping("/yearly-total")
    public Map<String, Double> getYearlyTotal(@RequestParam int year) {
        Double total = expenseService.getYearlyTotal(TEST_USER_ID, year);
        return Map.of("total", total != null ? total : 0.0);
    }

    // All expenses for Bar and Line Charts
    @GetMapping
    public List<ExpenseDTO> getExpenses() {
        return expenseService.getExpensesByUser(TEST_USER_ID);
    }
}