package com.nirupama.prreview.repository;

import com.nirupama.prreview.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByOwnerAndRepoAndPrNumber(String owner, String repo, int prNumber);
}