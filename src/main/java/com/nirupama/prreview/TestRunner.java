package com.nirupama.prreview;

import com.nirupama.prreview.client.GitHubClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TestRunner implements CommandLineRunner {

    private final GitHubClient gitHubClient;

    public TestRunner(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Map<String, Object>> files = gitHubClient.getPullRequestFiles("spring-projects", "spring-boot", 1);
        System.out.println("Files changed: " + files.size());
        for (Map<String, Object> file : files) {
            System.out.println(" - " + file.get("filename"));
        }
    }
}