# PR Review Assistant

An AI-powered code review assistant for GitHub pull requests. Given a PR, it fetches the changed files, filters and parses the diffs, sends the relevant code to an LLM for review, and persists structured findings — severity, category, file, line, and suggested fix — for later retrieval.

## Why this project

Built as a hands-on portfolio project while transitioning into backend/Java development — focused on demonstrating real Spring Boot fundamentals (REST APIs, dependency injection, external API integration, JPA persistence, centralized error handling) alongside practical LLM integration and deliberate engineering tradeoffs (see below).

## How it works

1. **Fetch** — pulls the list of changed files for a PR via the GitHub REST API.
2. **Filter** (`FileReviewPolicy`) — only source files (`.java`, `.js`, `.ts`, `.tsx`, `.jsx`, `.py`, `.go`) with a non-empty diff are sent for review. Everything else (lockfiles, binaries, config, generated assets) is skipped and reported back explicitly rather than silently dropped.
3. **Parse** (`DiffParser`) — each file's unified diff is parsed into individual `ADDED`/`REMOVED`/`CONTEXT` lines with correct old-file and new-file line numbers, then trimmed to a small context window around each change — so the LLM sees precisely what changed, not a raw unstructured patch.
4. **Review** (`ReviewGenerator`) — the parsed diff is sent to an LLM (via Spring AI, OpenAI-compatible client) with a structured prompt, and the response is parsed directly into a typed `ReviewResponse` (findings, severity, risk level).
5. **Persist** — results are saved per file, queryable later by PR.

## API

| Method | Endpoint                              | Description                                  |
|--------|----------------------------------------|-----------------------------------------------|
| POST   | `/api/reviews`                         | Reviews a PR. Body: `{owner, repo, prNumber}` |
| GET    | `/api/reviews`                         | Lists all stored reviews                      |
| GET    | `/api/reviews/{owner}/{repo}/{prNumber}` | Lists reviews for one PR                    |

`POST /api/reviews` returns:
```json
{
  "reviews": [ { "filename": "...", "summary": "...", "riskLevel": "..." } ],
  "skippedFiles": ["package-lock.json", "logo.svg"]
}
```

## Tech Stack

- **Java 21**, **Spring Boot 4** (Web, Data JPA)
- **Spring AI** — LLM integration via an OpenAI-compatible client
- **OpenRouter** — LLM provider gateway (model configurable)
- **H2** — in-memory database for local development
- **Maven**

## Running locally

Requires a GitHub personal access token and an OpenRouter API key.

```bash
export GITHUB_TOKEN=your_github_token
export OPENROUTER_API_KEY=your_openrouter_key

./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`. H2 console is available at `/h2-console`.

```bash
curl -X POST http://localhost:8080/api/reviews \
  -H "Content-Type: application/json" \
  -d '{"owner": "spring-projects", "repo": "spring-boot", "prNumber": 12345}'
```