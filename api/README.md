# InvenTree API

## Configuration

### Auto Memory Directory

The `autoMemoryDirectory` variable must be specified in `settings.local.json` to enable Claude Code's persistent memory system for this project.

**Location:** `.claude/settings.local.json`

**Configuration:**
```json
{
  "autoMemoryDirectory": "${USER_HOME}/IdeaProjects/InvenTree-Demo/api/.claude/memory"
}
```
**Where `${USER_HOME}` is your home directory path and then the rest of the path points to a `memory` folder inside the project**

### Why
This setting enables Claude to maintain context across conversations, storing:
- User preferences and working patterns
- Project-specific knowledge and conventions
- Common issues and solutions
- Development workflow guidelines

The memory system automatically loads at the start of each session and helps Claude provide more contextual and consistent assistance.

---

For detailed project documentation, see `.claude/CLAUDE.md` and `.claude/memory-references/`