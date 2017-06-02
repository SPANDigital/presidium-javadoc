# Presidium Javadoc

A Java [Doclet](http://docs.oracle.com/javase/8/docs/technotes/guides/javadoc/doclet/overview.html) based tool 
for importing Java source code comments to markdown for [Presidium](http://presidium.spandigital.net) documentation.

> Supports a limited subset of javadoc tags. Additional support is under development.

## Prerequisites
- Requires Java JDK 8+ to run

## Generation
This tool can be run as a:
 - Standalone executable
 - Javadoc Doclet

## Standalone
```
usage: presidium-javadoc
```

| Option | Description 
|:---|:---
| -d,--directory `path`                     | The destination directory to save the generated documentation to. default: './docs'
| -h,--help                                 | Shows this help.
| -p,--subpackages `package1:package2:...`  | Packages to generate documentation from. default: all
| -s,--sourcepath `path`                    | Java source code directory.
| -t,--title `string`                        | Reference title. default: 'javadoc'
| -u,--url `foo/bar/{title-slug}`            | Section url. default: 'reference/javadoc'
| -d,--directory `path`                      | The destination directory to save the generated documentation to. default: './docs'
| -h,--help                                  | Shows this help.
| -p,--subpackages `package1:package2:...`   | Packages to generate documentation from. default: all
| -s,--sourcepath `path`                     | Java source code directory.
| -t,--title `string`                        | Reference title. default: 'javadoc'
| -u,--url `foo/bar/{title-slug}`            | Section url. default: 'reference/javadoc'

## Javadoc
This tool provides a subset of the standard java doclet and can be built using `javadoc` and the [-doclet option](http://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html#CHDCGDCA): 

```bash
$ javadoc -sourcepath <src-path> -doclet net.spandigital.presidium.Doclet -docletpath presidium-javadoc-#.#.#.jar -d <dist-path> -subpackages <packages>
```

## Gradle
Include a custom doclet with the [gradle javadoc task](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.javadoc.Javadoc.html)

### npm

Include as part of the npm build building your Presidium site as in the following sample:

```json
    "scripts": {
        "import-javadoc-api" : "presidium-javadoc -s <src-path> -d content/_reference/javadoc-api -p <packages> -t Javadoc\ API -u reference/javadoc-api"
    },
    "devDependencies": {
        "presidium-javadoc" : "#.#.#"
    }
```

# Development

To build and run locally:

Uses the [gradle application plugin](https://docs.gradle.org/current/userguide/application_plugin.html):
```bash
gradle installDist
```

Installs to: `build/install/presidium-javadoc`

## Publish to NPM
```bash
$ gradle installDist
$ cd build/install/presidium-javadoc
$ npm publish
```

## Publish to Maven
TODO
