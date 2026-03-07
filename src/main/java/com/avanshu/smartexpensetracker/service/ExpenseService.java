package com.avanshu.smartexpensetracker.service;

import com.avanshu.smartexpensetracker.DTO.ExpenseDTO;
import com.avanshu.smartexpensetracker.entity.Expense;
import com.avanshu.smartexpensetracker.entity.User;
import com.avanshu.smartexpensetracker.repository.ExpenseRepository;
import com.avanshu.smartexpensetracker.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;


    public ExpenseService(ExpenseRepository expenseRepository,
                          UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    // Add expense to specific user
    public Expense addExpense(Long userId, Expense expense) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        expense.setUser(user);

        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long userId, Long expenseId, Expense updatedExpense) {

        Expense existingExpense = expenseRepository
                .findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Expense not found"));

        existingExpense.setTitle(updatedExpense.getTitle());
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDate(updatedExpense.getDate());

        return expenseRepository.save(existingExpense);
    }
    public Double getYearlyTotal(Long userId, int year) {
        return expenseRepository.getYearlyTotal(userId, year);
    }

    public void deleteExpense(Long userId, Long expenseId) {

        Expense expense = expenseRepository
                .findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Expense not found"));

        expenseRepository.delete(expense);
    }

    public Double getMonthlyTotal(Long userId, int month, int year) {

        Double total = expenseRepository.getTotalByMonth(userId, month, year);

        return total != null ? total : 0.0;
    }

    public List<Map<String, Object>> getCategorySummary(Long userId, int month, int year) {

        List<Object[]> results = expenseRepository.getCategorySummary(userId, month, year);

        return results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("category", row[0]);
            map.put("total", row[1]);
            return map;
        }).toList();
    }

    // Get all expenses of a specific user
    public List<ExpenseDTO> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private ExpenseDTO convertToDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setTitle(expense.getTitle());
        dto.setAmount(expense.getAmount());
        dto.setCategory(expense.getCategory());
        dto.setDate(expense.getDate());
        return dto;
    }

}
