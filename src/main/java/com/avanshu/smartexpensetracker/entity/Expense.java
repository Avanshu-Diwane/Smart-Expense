
package com.avanshu.smartexpensetracker.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
    @Entity
    @Getter @Setter
    @Table(name = "expenses")
    public class Expense {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

        private Double amount;

        private String category;

        private LocalDate date;

    @ManyToOne
    @JsonIgnore
    private User user;

        public void setUser(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }

        // Getters and Setters
    }

