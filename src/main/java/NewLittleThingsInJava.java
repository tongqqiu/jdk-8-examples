import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;


public class NewLittleThingsInJava {

    public static void main(String[] args) throws IOException, InterruptedException {

        // String join
        String joined = String.join("/", "usr", "local", "bin"); // "usr/local/bin"
        System.out.println(joined);
        String ids = String.join(", ", ZoneId.getAvailableZoneIds());
        System.out.println(ids);

        // Files.lines
        Path samplePath = Paths.get("data", "sample.txt"); // Use Path instead of File since 1.7
        Stream<String> lines = Files.lines(samplePath);
        Optional<String> passwordEntry = lines.filter(s -> s.contains("password")).findFirst();
        System.out.println(passwordEntry.get());

        // Base64 Encoding
        Base64.Encoder encoder = Base64.getEncoder();
        String username = "abc";
        String password = "password";
        String original = username + ":" + password;
        String encoded = encoder.encodeToString(original.getBytes(StandardCharsets.UTF_8));
        System.out.println(encoded);


        // the try-with-resources statement has the form res.close() is called automatically.
        try (Scanner in = new Scanner(Paths.get("data/sample.txt"))) { while (in.hasNext())
            System.out.println(in.next().toLowerCase()); }


        // Null-safe Equality Testing
        String a = null;
        String b = "";
        System.out.println(Objects.equals(a, b));

        // Computing Hash Codes
        System.out.println(Objects.hash(a, b));

        // compare numeric
        System.out.println(Integer.compare(-1, 1));

        // global logging: instead of System.out.println("x=" + x);.
        Logger.getGlobal().info("hello world");

        // processbuilder re-direction
        ProcessBuilder builder = new ProcessBuilder( "grep", "-o", "[A-Za-z_][A-Za-z_0-9]*");
        builder.redirectInput(Paths.get("src/main/java/NewLittleThingsInJava.java").toFile());
        builder.redirectOutput(Paths.get("identifiers.txt").toFile());
        Process process = builder.start();
        process.waitFor();

        // new Time
        Instant start = Instant.now();

        // do something
        Thread.sleep(20);
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        long millis = timeElapsed.toMillis();
        System.out.println("It took : " + millis + " ms");

        // local date
        LocalDate today = LocalDate.now(); // Today’s date
        LocalDate alonzosBirthday = LocalDate.of(1903, 6, 14);

        // The first Tuesday of a month can be computed like this:
        LocalDate firstTuesday = LocalDate.of(2014, 5, 1).with( TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));

        // zoned time
        // // 1969-07-16T09:32-04:00[America/New_York]
        ZonedDateTime apollo11launch = ZonedDateTime.of(1969, 7, 16, 9, 32, 0, 0, ZoneId.of("America/New_York"));

        // time format
        String formatted = DateTimeFormatter.ISO_DATE_TIME.format(apollo11launch);
        System.out.println(formatted);

        // Atomic values
        AtomicLong largest = new AtomicLong();
        long observed = 1L;
        largest.updateAndGet(x -> Math.max(x, observed));

        // concurrent hashmap

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.compute("word", (k, v) -> v == null ? 1 : v + 1);
        int threshold = 1;
        String result = map.search(threshold, (k, v) -> v > 1000 ? k : null);

        Set<String> words = ConcurrentHashMap.<String>newKeySet();
        // array parallel
        String contents = new String(Files.readAllBytes(
                Paths.get("data/sample.txt")), StandardCharsets.UTF_8); // Read file into string
        String[] wordsArray = contents.split("[\\P{L}]+"); // Split along nonletters Arrays.parallelSort(words);
        Arrays.parallelSort(wordsArray);


        // Lambda, new a thread
        new Thread(
                () -> System.out.println("Hello from thread")
        ).start();


        // print all elements in a array
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        list.forEach(System.out::println);


        // predicate
        evaluate(list, (n)-> n%2 == 0 );

        // stream
        int sum = list.stream().map(x -> x*x).reduce((x,y) -> x + y).get();
        System.out.println(sum);

    }


    public static void evaluate(List<Integer> list, Predicate<Integer> predicate) {
        for(Integer n: list)  {
            if(predicate.test(n)) {
                System.out.println(n + " ");
            }
        }
    }
}
