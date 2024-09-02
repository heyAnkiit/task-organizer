# Task Organizer API

## Overview

Task Organizer API is a Spring Boot application designed to help users create, manage, and track tasks. The application provides endpoints for user registration, login, and various task-related operations such as creating, updating, and commenting on tasks.

## Features

- User Registration and Login with JWT-based Authentication.
- Task Management:
    - Create, Update, and Delete tasks.
    - Transition task status.
    - Add comments to tasks.
    - Attach files to tasks.
- Audit fields like `created_at`, `updated_at`, `created_by`, and `updated_by`.
- Dockerized for easy deployment.
