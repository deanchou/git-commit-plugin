# Git Commit Plugin for JetBrains IDEs

## Description

This plugin provides customizable Git commit message templates for JetBrains IDEs (GoLand, IntelliJ IDEA, etc.). It helps developers follow consistent commit message conventions by providing predefined templates and allowing custom templates to be configured. The plugin supports both English and Chinese interfaces with automatic language detection.

## Features

- **Predefined commit message templates** (Angular, Conventional)
- **Custom templates configuration** with easy-to-use settings interface
- **Emoji support** for commit types to make commits more visual
- **Seamless integration** with GoLand's VCS interface
- **Auto-fill subject** with current branch name option
- **Internationalization support** - Automatic English/Chinese interface switching
- **Optimized UI layout** with improved commit type selection area
- **Real-time template preview** and validation

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Git Commit Plugin"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/user/git-commit-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Building

### Prerequisites

- Java JDK 21 or Docker

### Building with Java

If you have Java installed:

```bash
./gradlew build
```

The plugin JAR will be located at `build/libs/git-commit-plugin-1.0-SNAPSHOT.jar`.

### Building with Docker

If you don't have Java installed but have Docker:

```bash
# Make the build script executable
chmod +x build-with-docker.sh

# Run the build script
./build-with-docker.sh
```

This will create a Docker container with all necessary dependencies and build the plugin.
The plugin JAR will be available at `build/libs/git-commit-plugin-1.0-SNAPSHOT.jar`.

**Alternative Docker build methods:**

```bash
# Using docker-compose directly
docker-compose up --build

# Clean build (no cache)
docker-compose build --no-cache
docker-compose up
```

For detailed instructions on building with Docker, see [DOCKER_BUILD.md](DOCKER_BUILD.md).

## Usage

1. Open a Git commit dialog in your JetBrains IDE (GoLand, IntelliJ IDEA, etc.)
2. Click on "Use Commit Template" (使用提交模板 in Chinese) in the commit message toolbar
3. In the template dialog:
   - Select a commit type from the dropdown (with emoji support)
   - Enter scope (optional)
   - Enter subject line
   - Add body content (optional)
   - Add footer content (optional)
4. Click "OK" to apply the template to your commit message
5. The formatted commit message will be inserted into the commit dialog

### Language Support

The plugin automatically detects your system language:
- **English environment**: All interface elements display in English
- **Chinese environment**: All interface elements display in Chinese (中文界面)

## Configuration

1. Go to <kbd>Settings/Preferences</kbd> > <kbd>Tools</kbd> > <kbd>Commit Template Settings</kbd>
2. Configure the following options:
   - **Show emoji in commit types**: Enable/disable emoji display in commit type dropdown
   - **Maximum subject length**: Set character limit for commit subject lines
   - **Fill subject with current branch name**: Automatically populate subject with branch name
   - **Template management**: Add, edit, or remove custom templates
   - **Default template selection**: Choose which template to use by default

### Available Commit Types

The plugin includes standard commit types:
- `feat` 🚀 - New features
- `fix` 🐛 - Bug fixes
- `docs` 📚 - Documentation changes
- `style` 💎 - Code style changes
- `refactor` 📦 - Code refactoring
- `test` 🚨 - Test additions/modifications
- `chore` 🔧 - Maintenance tasks

## Template Format

Templates use placeholders that will be replaced when applied:

- `<type>` - The commit type (feat, fix, etc.)
- `<scope>` - The scope of the change (optional)
- `<subject>` - The subject of the commit
- `<body>` - The body of the commit (optional)
- `<footer>` - The footer of the commit (optional)

### Example Templates

**Angular Convention:**
```
<type>(<scope>): <subject>

<body>

<footer>
```

**Conventional Commits:**
```
<type>(<scope>): <subject>

<body>

BREAKING CHANGE: <footer>
```

**Simple Format:**
```
<type>: <subject>
```

### Custom Template Creation

You can create custom templates in the settings:
1. Go to plugin settings
2. Click "Add Template"
3. Define your template name and format
4. Use placeholders as needed
5. Save and select as default (optional)

## Development

### Project Structure

```
src/main/kotlin/com/github/user/golandcommittemplate/
├── actions/           # Plugin actions (CommitTemplateAction)
├── model/            # Data models (CommitType, Template)
├── services/         # Settings and configuration services
├── settings/         # UI components for settings
└── ui/              # Dialog and UI components
```

### Key Components

- **CommitTemplateAction**: Main action triggered from VCS toolbar
- **TemplateSettingsService**: Manages template configuration
- **CommitFormDialog**: UI dialog for template input
- **TemplateConfigurable**: Settings page integration

### Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

### Requirements

- IntelliJ IDEA 2023.1+ or GoLand 2023.1+
- Java JDK 21+
- Gradle 8.5+

## Troubleshooting

### Common Issues

**Plugin not appearing in toolbar:**
- Ensure you're in a Git repository
- Check if VCS integration is enabled
- Restart your IDE after installation

**Templates not saving:**
- Check file permissions in IDE settings directory
- Verify plugin has write access

**Language not switching:**
- Plugin detects system locale automatically
- Restart your IDE if language doesn't update

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- This project is inspired by and references [RedJue/git-commit-plugin](https://github.com/RedJue/git-commit-plugin). Special thanks to the original author for the excellent foundation and ideas.
- Inspired by conventional commit standards
- Built with IntelliJ Platform SDK
- Uses Kotlin for modern, concise code