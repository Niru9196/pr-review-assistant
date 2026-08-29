package com.nirupama.prreview.entity;

import com.nirupama.prreview.review.dto.RiskLevel;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    private String summary;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    private Instant createdAt;

    @OneToMany(
            mappedBy = "review",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ReviewFinding> findings = new ArrayList<>();

    protected Review() {
        // required by JPA
    }

    public Review(
            String owner,
            String repo,
            int prNumber,
            String filename,
            String summary,
            RiskLevel riskLevel
    ) {
        this.owner = owner;
        this.repo = repo;
        this.prNumber = prNumber;
        this.filename = filename;
        this.summary = summary;
        this.riskLevel = riskLevel;
        this.createdAt = Instant.now();
    }

    public void addFinding(ReviewFinding finding) {
        findings.add(finding);
        finding.setReview(this);
    }

    public Long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getRepo() {
        return repo;
    }

    public int getPrNumber() {
        return prNumber;
    }

    public String getFilename() {
        return filename;
    }

    public String getSummary() {
        return summary;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<ReviewFinding> getFindings() {
        return List.copyOf(findings);
    }
}