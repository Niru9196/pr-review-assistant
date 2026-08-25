# PR Review Assistant

An AI-powered GitHub pull request review assistant built with Spring Boot. It fetches PR diffs via the GitHub REST API, generates automated code review feedback using an LLM, and persists review history for later retrieval.

## Why this project

Built as a hands-on portfolio project while transitioning into backend/Java development — focused on demonstrating real Spring Boot fundamentals (REST APIs, dependency injection, external API integration, JPA persistence, centralized error handling) alongside practical AI integration.

## Tech Stack

- **Java 21**
- **Spring Boot 4** (Spring Web, Spring Data JPA)
- **Spring AI** — LLM integration via an OpenAI-compatible client
- **OpenRouter** — LLM provider gateway (model configurable)
- **H2 Database** (in-memory, for local development)
- **Maven**
- **GitHub REST API** — for fetching PR data
- **Lombok**