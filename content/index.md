---
title: Welcome to Mimas
layout: default
---

# Welcome to Mimas

Mimas is a Clojure-based static site generator inspired by Astro.

## Features

- **Content-first**: Markdown files as primary content source
- **Component-based**: Hiccup templates with slot support
- **Zero JavaScript by default**: Only ship JS when explicitly needed
- **Fast refresh**: Development server with hot reload

## Getting Started

Create a new page in the `content/` directory and start the dev server:

```bash
clj -M:dev
```

Then build your site:

```bash
clj -M:build
```
