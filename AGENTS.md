# Static Site Generator MVP - High Level Design

## Overview
A Clojure-based static site generator inspired by Astro, focusing on simplicity, performance, and developer experience. The MVP will provide core functionality for building static sites with markdown content, component slots, and a development server.

## Core Philosophy
- **Content-first**: Markdown files as primary content source
- **Component-based**: Hiccup templates with slot support
- **Zero JavaScript by default**: Only ship JS when explicitly needed
- **Fast refresh**: Development server with hot reload
- **Type-safe**: Leverage Malli for validation throughout

## MVP Scope

### Phase 1: Foundation (Week 1)
**Goal**: Basic file processing and HTML generation

1. **Project Structure**
   - Setup tools.deps project with deps.edn
   - Core namespaces: `core`, `markdown`, `templating`, `server`
   - Configuration via EDN files

2. **Markdown Processing**
   - Parse `.md` files with frontmatter support
   - Convert markdown to HTML using a library like `markdown-clj`
   - Extract and validate frontmatter with Malli schemas

3. **Basic Templating**
   - Hiccup-based layout system
   - Simple page wrapper component
   - Static asset handling (CSS, images)

### Phase 2: Component System (Week 2)
**Goal**: Reusable components with slot support

1. **Component Architecture**
   - Define component protocol/interface
   - Implement slot mechanism similar to Astro's
   - Named slots and default slot support

2. **Component Registry**
   - Component discovery and registration
   - Dependency resolution
   - Component composition

3. **Template Processing**
   - Process Hiccup templates with embedded components
   - Slot content injection
   - Props passing and validation with Malli

### Phase 3: Routing & Building (Week 3)
**Goal**: File-based routing and static build

1. **Routing System**
   - Reitit-based routing from file structure
   - Dynamic route parameters
   - Route metadata from frontmatter

2. **Build Pipeline**
   - Collect all content files
   - Generate route manifest
   - Output static HTML files
   - Copy static assets

3. **URL Management**
   - Permalink generation
   - Relative/absolute URL helpers
   - Asset fingerprinting

### Phase 4: Development Server (Week 4)
**Goal**: Fast development experience

1. **Dev Server**
   - Ring/Jetty server with Reitit
   - File watcher for auto-rebuild
   - WebSocket for live reload
   - Error overlay for development

2. **Incremental Building**
   - Track file dependencies
   - Rebuild only affected pages
   - Cache compiled templates

## Technical Architecture

### Core Components

```
┌─────────────────────────────────────────┐
│            CLI Interface                 │
├─────────────────────────────────────────┤
│          Configuration Layer             │
│         (deps.edn, site.edn)           │
├─────────────────────────────────────────┤
│          Content Pipeline                │
│   ┌──────────┐  ┌──────────┐           │
│   │ Markdown │  │ Template │           │
│   │ Parser   │  │ Engine   │           │
│   └──────────┘  └──────────┘           │
├─────────────────────────────────────────┤
│          Component System                │
│   ┌──────────┐  ┌──────────┐           │
│   │  Slots   │  │  Props   │           │
│   │  Engine  │  │Validation│           │
│   └──────────┘  └──────────┘           │
├─────────────────────────────────────────┤
│          Routing Layer                   │
│         (Reitit-based)                  │
├─────────────────────────────────────────┤
│     Output Generation / Dev Server       │
└─────────────────────────────────────────┘
```

### Data Flow

1. **Content File** → Frontmatter extraction → Markdown parsing → HTML generation
2. **Template File** → Hiccup parsing → Component resolution → Slot injection → HTML output
3. **Route Definition** → File system scan → Route tree generation → URL mapping

### Key Data Structures

```clojure
;; Page representation
{:type :page
 :path "blog/post.md"
 :frontmatter {:title "..."
               :layout "post"
               :date #inst "2024-01-01"}
 :content "HTML string"
 :route "/blog/post"}

;; Component definition
{:name :card
 :props-schema [:map [:title :string]
                     [:description {:optional true} :string]]
 :slots {:default true
         :footer true}
 :render-fn (fn [props slots] ...)}

;; Site configuration
{:base-url "https://example.com"
 :output-dir "dist"
 :layouts-dir "src/layouts"
 :components-dir "src/components"
 :content-dir "content"}
```

## Implementation Priorities

### Must Have (MVP)
- [x] Markdown to HTML conversion
- [x] Basic Hiccup templating
- [x] File-based routing
- [x] Static build command
- [x] Development server
- [x] Basic slot support
- [x] Frontmatter processing

### Should Have (Post-MVP)
- [ ] Collections (blog posts, tags)
- [ ] Pagination
- [ ] RSS/Sitemap generation
- [ ] Image optimization
- [ ] CSS processing
- [ ] Partial hydration

### Could Have (Future)
- [ ] MDX-like component embedding in Markdown
- [ ] API routes
- [ ] SSR capabilities
- [ ] Island architecture
- [ ] Build-time data fetching

## Project Structure

```
my-site/
├── deps.edn              # Dependencies and aliases
├── site.edn              # Site configuration
├── src/
│   ├── core.clj         # Main entry point
│   ├── markdown.clj     # Markdown processing
│   ├── templating.clj   # Hiccup & slots
│   ├── router.clj       # Reitit routing
│   ├── server.clj       # Dev server
│   └── build.clj        # Static build
├── content/
│   ├── index.md         # Homepage
│   └── blog/
│       └── post.md      # Blog posts
├── layouts/
│   └── default.clj      # Layout components
├── components/
│   └── card.clj         # Reusable components
├── public/              # Static assets
│   ├── css/
│   └── images/
└── dist/                # Build output

```

## Dependencies

```clojure
;; deps.edn
{:deps {org.clojure/clojure {:mvn/version "1.11.1"}
        metosin/reitit {:mvn/version "0.7.0"}
        metosin/malli {:mvn/version "0.13.0"}
        hiccup/hiccup {:mvn/version "2.0.0"}
        markdown-clj/markdown-clj {:mvn/version "1.11.7"}
        ring/ring-core {:mvn/version "1.11.0"}
        ring/ring-jetty-adapter {:mvn/version "1.11.0"}
        hawk/hawk {:mvn/version "0.2.11"}  ; file watching
        org.clojure/tools.cli {:mvn/version "1.0.219"}}}
```

## CLI Commands

```bash
# Development
clj -M:dev    # Start dev server on localhost:3000

# Build
clj -M:build  # Generate static site in dist/

# New content
clj -M:new page "About"       # Create new page
clj -M:new post "Hello World"  # Create new blog post
```

## Success Metrics for MVP

1. **Functional**
   - Can build a multi-page static site
   - Markdown content renders correctly
   - Components with slots work
   - Dev server hot reloads

2. **Performance**
   - Build time < 1s for 10 pages
   - Dev server starts < 2s
   - Page changes reflect < 500ms

3. **Developer Experience**
   - Clear error messages
   - Minimal configuration
   - Intuitive file structure
   - Good documentation

## Next Steps

1. Set up initial project structure with deps.edn
2. Implement markdown parser with frontmatter
3. Create basic Hiccup templating system
4. Add file-based router with Reitit
5. Implement slot mechanism
6. Build static output generator
7. Add development server with file watching
8. Write initial documentation and examples

## Risks and Mitigations

| Risk | Impact | Mitigation |
|------|---------|------------|
| Slot mechanism complexity | High | Start with simple default slot, add named slots later |
| File watching performance | Medium | Use efficient watcher, debounce rebuilds |
| Hiccup/HTML conversion edge cases | Medium | Extensive testing, clear escape hatches |
| Route generation ambiguity | Low | Clear conventions, configuration overrides |

## Open Questions

1. How closely should we mirror Astro's slot API?
2. Should we support inline components in Markdown (MDX-style)?
3. What's the best approach for asset fingerprinting?
4. Should layouts be special or just components?
5. How to handle data fetching at build time?

---

This document serves as the north star for MVP development. Each phase builds on the previous, ensuring we always have a working system that can generate static sites, even if features are limited initially.
