package com.avanshu.smartexpensetracker.controller;

import com.avanshu.smartexpensetracker.DTO.ExpenseDTO;
import com.avanshu.smartexpensetracker.entity.Expense;
import com.avanshu.smartexpensetracker.entity.User;
import com.avanshu.smartexpensetracker.repository.UserRepository;
import com.avanshu.smartexpensetracker.service.ExpenseService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final UserRepository userRepository;
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService,
                             UserRepository userRepository) {
        this.expenseService = expenseService;
        this.userRepository = userRepository;
    }

    // Add expense to a user
    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return expenseService.addExpense(user.getId(), expense);
    }


    @PutMapping("/{expenseId}")
    public Expense updateExpense(@PathVariable Long expenseId,
                                 @RequestBody Expense expense) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return expenseService.updateExpense(user.getId(), expenseId, expense);
    }

    @DeleteMapping("/{expenseId}")
    public String deleteExpense(@PathVariable Long expenseId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        expenseService.deleteExpense(user.getId(), expenseId);

        return "Expense deleted successfully";
    }

    @GetMapping("/total")
    public Map<String, Double> getMonthlyTotal(@RequestParam int month,
                                               @RequestParam int year) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Double total = expenseService.getMonthlyTotal(user.getId(), month, year);

        return Map.of("total", total);
    }
    @GetMapping("/category-summary")
    public List<Map<String, Object>> getCategorySummary(@RequestParam int month,
                                                        @RequestParam int year) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return expenseService.getCategorySummary(user.getId(), month, year);
    }
    @GetMapping("/yearly-total")
    public Map<String, Double> getYearlyTotal(@RequestParam int year) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Double total = expenseService.getYearlyTotal(user.getId(), year);

        return Map.of("total", total);
    }
    // Get all expenses of a user
    @GetMapping
    public List<ExpenseDTO> getExpenses() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return expenseService.getExpensesByUser(user.getId());
    }
}
