## This project is set to work with maven

### To Build The Project

compile the project and manage dependencies:

```bash
mvn clean install
```

This command cleans any previous builds and compiles the project.

---

### 6. Run With Command

After building, you can run using:

```bash
java -jar target/AdvRecommander-1.0.jar data/impressions.json data/clicks.json
```

The command will generate two file
metrics_output.json
recommendations_output.json

### Other git command used

```bash
git init
git branch -m master main
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/kianoosh/AdvRecommander
git push -u origin main
```
