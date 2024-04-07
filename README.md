# BAALL-Parser-Gen

BAALL-Parser-Gen is a utility program to generate a recursive descent parser from a LL(k) grammar definition. It is
designed for the BAALL programming language compiler.

## How to use

### 0. Have Java Runtime compatible with Java 17 installed.

### 1. Download the latest release.

→ [Releases](https://github.com/nikschadowsky/BAALL-Parser-Gen/releases)

### 2. Navigate to the jar's location in your terminal of choice

### 3. Run

```ignorelang
java -jar <your-release.jar> <grammar-file-location>
```

```<grammar-file-location>``` is a required argument that specifies where the grammar definition file is located.

#### Options

BAALL-Parser-Gen behavior can be altered with different options:

|           Argument            |                                                      Explanation                                                      |
|:-----------------------------:|:---------------------------------------------------------------------------------------------------------------------:|
| ```-d <target-destination>``` | Specify a directory for the generated sources to be exported to. <br/>Default is the current working directory ('.'). |
|    ```-p <package-name>```    |        Specify the package name the generated classes use in their package statement. <br/>Default is 'x.y.z'.        |
|           ```-m```            |                            Specify that the grammar should be minimized before generation.                            |
|           ```-o```            |  Specify that existing files should be **overridden**. This option is inferred per default if not further specified.  |
|           ```-k```            |                               Specify that the existing files should be **kept** as is.                               |
|           ```-h```            |                         Show help text. Must be first, grammar location is not required here.                         |
