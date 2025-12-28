# Movie Recommendation System (Group 47)

This repository contains the Movie Recommendation System implemented in Java.

## Prerequisites

- Java JDK 11 or newer installed and `java`/`javac` available on your PATH.
  - Verify by running in PowerShell:
    ```powershell
    java -version; javac -version
    ```

## Build & Run (recommended)

The CLI entry point for the application is the `Main` class in the `milestone2` folder. The program expects the data files to be in the `milestone2/data` directory, so run the program from the `milestone2` folder.

Open PowerShell and run the following commands from the repository root (or run them from your terminal after cloning):

```powershell
# Change to the milestone2 folder (working directory must be milestone2 so the "data" folder is found)
cd .\milestone2

# Compile all Java source files and place class files into a new 'bin' folder
javac -d .\bin .\src\*.java

# Run the program
java -cp .\bin Main
```

Notes:
- The program reads `data/movies.txt` and (optionally) `data/my_ratings.txt` relative to the `milestone2` directory. Make sure those files exist in `milestone2\data` before running, or the program will start with an empty rating set and still function.
- When you exit the program (choose the Exit option), any ratings you added will be saved to `milestone2\data\my_ratings.txt`.

## Alternative (single-line from repo root)

If you prefer a single-line approach from the repository root, you can run:

```powershell
cd .\milestone2; javac -d .\bin .\src\*.java; java -cp .\bin Main
```

## Troubleshooting

- If `javac` or `java` is not recognized, install the JDK and add its `bin` directory to your PATH (or set `JAVA_HOME`).
- If you get errors during compilation, ensure you are using a compatible JDK (11+).

