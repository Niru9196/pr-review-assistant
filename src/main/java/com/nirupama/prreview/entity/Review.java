package com.nirupama.prreview.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;
    private String repo;
    private int prNumber;
    private String filename;

    @Column(columnDefinition = "TEXT")
    private String reviewText;

    private Instant createdAt;

    protected Review() {
        // required no-arg constructor for JPA
    }

    public Review(String owner, String repo, int prNumber, String filename, String reviewText) {
        this.owner = owner;
        this.repo = repo;
        this.prNumber = prNumber;
        this.filename = filename;
        this.reviewText = reviewText;
        this.createdAt = Instant.now();
    }

    // Getters (no setters — we're treating this as immutable once created)
    public Long getId() { return id; }
    public String getOwner() { return owner; }
    public String getRepo() { return repo; }
    public int getPrNumber() { return prNumber; }
    public String getFilename() { return filename; }
    public String getReviewText() { return reviewText; }
    public Instant getCreatedAt() { return createdAt; }
}