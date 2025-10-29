# Mimas

A Clojure-based static site generator inspired by Astro, focusing on simplicity, performance, and developer experience.

## Project Structure

```
mimas/
├── deps.edn              # Dependencies and aliases
├── site.edn              # Site configuration
├── src/mimas/            # Core source code
│   ├── core.clj         # Main entry point
│   ├── markdown.clj     # Markdown processing with frontmatter
│   ├── templating.clj   # Hiccup & slots
│   ├── router.clj       # File-based routing with Reitit
│   ├── server.clj       # Dev server
│   └── build.clj        # Static build pipeline
├── content/              # Markdown content files
│   ├── index.md         # Homepage
│   └── blog/            # Blog posts
├── layouts/              # Layout components
│   └── default.clj      # Default layout
├── components/           # Reusable components
│   └── card.clj         # Example card component
├── public/               # Static assets
│   ├── css/
│   │   └── style.css    # Main stylesheet
│   └── images/
└── dist/                 # Build output (generated)
```

## Getting Started

### Development Server

Start the development server:

```bash
clj -M:dev
```

The site will be available at http://localhost:3000

### Build

Generate the static site:

```bash
clj -M:build
```

Output will be in the `dist/` directory.

## Core Features (MVP)

- [x] Markdown to HTML conversion
- [x] Frontmatter processing
- [x] Basic Hiccup templating
- [x] File-based routing
- [x] Static build command
- [x] Development server
- [x] Basic component structure with slots

## Configuration

Edit `site.edn` to configure your site:

```clojure
{:base-url "http://localhost:3000"
 :site-title "My Site"
 :output-dir "dist"
 :content-dir "content"
 :public-dir "public"
 :dev-server {:port 3000
              :host "localhost"}}
```

## Creating Content

Create markdown files in the `content/` directory:

```markdown
---
title: My Page Title
date: 2024-01-01
layout: default
---

# My Page

Your content here...
```

## Next Steps

See [AGENTS.md](./AGENTS.md) for the full development roadmap and implementation plan.

## License

MIT
