package com.nirupama.prreview.entity;

import com.nirupama.prreview.review.dto.FindingCategory;
import com.nirupama.prreview.review.dto.Severity;
import jakarta.persistence.*;

@Entity
@Table(name = "review_findings")
public class ReviewFinding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    private FindingCategory category;

    private String file;

    private Integer line;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String suggestion;

    private Double confidence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    protected ReviewFinding() {
        // required by JPA
    }

    public ReviewFinding(
            Severity severity,
            FindingCategory category,
            String file,
            Integer line,
            String title,
            String description,
            String suggestion,
            Double confidence
    ) {
        this.severity = severity;
        this.category = category;
        this.file = file;
        this.line = line;
        this.title = title;
        this.description = description;
        this.suggestion = suggestion;
        this.confidence = confidence;
    }

    void setReview(Review review) {
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public Severity getSeverity() {
        return severity;
    }

    public FindingCategory getCategory() {
        return category;
    }

    public String getFile() {
        return file;
    }

    public Integer getLine() {
        return line;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public Double getConfidence() {
        return confidence;
    }
}
