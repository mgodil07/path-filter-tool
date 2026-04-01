# Path Filter Tool

Small CLI tool that evaluates a list of file paths against filters defined in `src/main/resources/filters.yaml`.
It is useful in CI/CD pipelines to detect what changed so you only rebuild or retest the parts that matter, improving build speed.

## Use Cases

- Personal projects with custom folder structures
- Monorepos or services where only some areas should trigger specific jobs
- Faster CI/CD by avoiding unnecessary rebuilds

## Requirements

- Java 21
- macOS/Linux shell or Windows PowerShell

## Build

From the project root:

- macOS/Linux:
  - `./gradlew build`
- Windows:
  - `.\gradlew.bat build`

## Run

This project uses the Gradle `application` plugin (`mainClass = pathfilter.Main`).
Pass whichever files changed in your commit/PR as arguments.

- macOS/Linux:
  - `./gradlew run --args="src/api/user.py src/api/tests/test_user.py docs/intro.md"`
- Windows:
  - `.\gradlew.bat run --args="src/api/user.py src/api/tests/test_user.py docs/intro.md"`

The `filters.yaml` in this repo is intentionally simple for demo/testing.  
You can edit `src/main/resources/filters.yaml` to match your own project folders and naming conventions.

Example test arguments you can use now:

- `src/api/handlers/users.py src/api/tests/test_users.py README.md`

## Output

The app prints one line per filter in this format:

- `<filter_name>=true|false`

Example:

- `api_sources=true`
- `tests=true`
- `documentation=true`
- `infra=false`
