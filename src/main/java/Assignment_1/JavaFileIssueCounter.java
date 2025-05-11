package Assignment_1;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class JavaFileIssueCounter {

    static List<File> javaFiles = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter directory path: ");
        String path = scanner.nextLine();
        File directory = new File(path);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory!");
            return;
        }

        Thread fileCollector = new Thread(() -> collectJavaFiles(directory));
        fileCollector.start();
        fileCollector.join();

        System.out.println("\nJava Files Found:");
        if (javaFiles.isEmpty()) {
            System.out.println("No Java files found.");
            return;
        } else {
            for (File file : javaFiles) {
                System.out.println(" - " + file.getName());
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<IssueResult>> results = new ArrayList<>();

        for (File file : javaFiles) {
            results.add(executor.submit(() -> countSolvedIssues(file)));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        int totalIssues = 0;
        boolean anyIssuesFound = false;

        System.out.println("\nIssues Found:");
        for (Future<IssueResult> future : results) {
            try {
                IssueResult result = future.get();
                if (!result.issueLines.isEmpty()) {
                    anyIssuesFound = true;
                    System.out.println("File: " + result.fileName);
                    for (String issue : result.issueLines) {
                        System.out.println("  " + issue);
                    }
                }
                totalIssues += result.count;
            } catch (ExecutionException e) {
                System.out.println("Error during issue counting: " + e.getMessage());
            }
        }

        if (!anyIssuesFound) {
            System.out.println("No issues found in any file.");
        }

        System.out.println("\nSummary:");
        System.out.println("Number of Java Files = " + javaFiles.size());
        System.out.println("Number of Issues = " + totalIssues);
    }

    private static void collectJavaFiles(File dir) {
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                collectJavaFiles(file);
            } else if (file.getName().endsWith(".java")) {
                javaFiles.add(file);
            }
        }
    }

    private static IssueResult countSolvedIssues(File file) {
        List<String> issues = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                if (line.contains("// SOLVED")) {
                    issues.add("Line " + lineNumber + ": " + line.trim());
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println("Failed to read: " + file.getName());
        }
        return new IssueResult(file.getName(), issues);
    }

    static class IssueResult {
        String fileName;
        List<String> issueLines;
        int count;

        IssueResult(String fileName, List<String> issueLines) {
            this.fileName = fileName;
            this.issueLines = issueLines;
            this.count = issueLines.size();
        }
    }
}
