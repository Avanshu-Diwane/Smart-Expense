package com.avanshu.smartexpensetracker.repository;

import com.avanshu.smartexpensetracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ExpenseRepository extends JpaRepository<Expense, Long> {

        List<Expense> findByUserId(Long userId);

    @Query("""
           SELECT SUM(e.amount)
           FROM Expense e
           WHERE e.user.id = :userId
           AND FUNCTION ('MONTH',e.date) = :month
           AND FUNCTION ('YEAR',e.date) = :year
           """)
    Double getTotalByMonth(@Param("userId") Long userId,
                           @Param("month") int month,
                           @Param("year") int year);



    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND YEAR(e.date) = :year")
    Double getYearlyTotal(@Param("userId") Long userId,
                          @Param("year") int year);
    @Query("""
    SELECT e.category, SUM(e.amount), b.limitAmount 
    FROM Expense e 
    LEFT JOIN Budget b ON e.category = b.category AND e.user.id = b.user.id
    WHERE e.user.id = :userId 
    AND FUNCTION('MONTH', e.date) = :month 
    AND FUNCTION('YEAR', e.date) = :year
    GROUP BY e.category, b.limitAmount
""")
    List<Object[]> getBudgetAnalysis(@Param("userId") Long userId, @Param("month") int month, @Param("year") int year);

    @Query("""
       SELECT e.category, SUM(e.amount)
       FROM Expense e
       WHERE e.user.id = :userId
       AND FUNCTION ('MONTH',e.date) = :month
       AND FUNCTION ('YEAR',e.date) = :year
       GROUP BY e.category
       """)
    List<Object[]> getCategorySummary(@Param("userId") Long userId,
                                      @Param("month") int month,
                                      @Param("year") int year);

    Optional<Expense> findByIdAndUserId(Long expenseId, Long userId);
}



