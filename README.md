# GoLand Commit Template Plugin

![Build](https://github.com/user/goland-commit-template-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

## Description

This plugin provides customizable Git commit message templates for GoLand IDE. It helps developers follow consistent commit message conventions by providing predefined templates and allowing custom templates to be configured.

## Features

- Predefined commit message templates (Angular, Conventional)
- Custom templates configuration
- Emoji support for commit types
- Integration with GoLand's VCS interface
- Option to automatically fill subject with current branch name

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "GoLand Commit Template"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/user/goland-commit-template-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Building

### Prerequisites

- Java JDK 21 or Docker

### Building with Java

If you have Java installed:

```bash
./gradlew build
```

The plugin JAR will be located at `build/libs/goland-commit-template-plugin-1.0-SNAPSHOT.jar`.

### Building with Docker

If you don't have Java installed but have Docker:

```bash
# Make the build script executable
chmod +x build-with-docker.sh

# Run the build script
./build-with-docker.sh
```

This will create a Docker container with all necessary dependencies and build the plugin.
The plugin JAR will be available at `build/libs/goland-commit-template-plugin-1.0-SNAPSHOT.jar`.

For detailed instructions on building with Docker, see [DOCKER_BUILD.md](DOCKER_BUILD.md).

## Usage

1. Open a Git commit dialog in GoLand
2. Click on "Use Commit Template" in the commit message toolbar
3. Select a template and commit type
4. The template will be applied to the commit message

## Configuration

1. Go to <kbd>Settings/Preferences</kbd> > <kbd>Tools</kbd> > <kbd>Commit Template Settings</kbd>
2. Configure the following options:
   - Show emoji in commit types
   - Maximum subject length
   - Fill subject with current branch name
   - Add, edit, or remove templates

## Template Format

Templates use placeholders that will be replaced when applied:

- `<type>` - The commit type (feat, fix, etc.)
- `<scope>` - The scope of the change
- `<subject>` - The subject of the commit
- `<body>` - The body of the commit
- `<footer>` - The footer of the commit

Example template (Angular):
```
<type>(<scope>): <subject>

<body>

<footer>
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.