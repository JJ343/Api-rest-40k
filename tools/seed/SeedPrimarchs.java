import java.net.URI;
import java.net.http.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;


public class SeedPrimarchs {

    static final String API_BASE = System.getenv().getOrDefault("API_BASE_URL", "http://localhost:8080");
    static final String PRIMARCHS_URL = API_BASE.replaceAll("/$", "") + "/api/v1/primarchs";
    static final String AUTH_URL = API_BASE.replaceAll("/$", "") + "/api/v1/auth";

    // If JWT_TOKEN is missing, tries automatic login/register with these:
    static final String SEED_USER = System.getenv().getOrDefault("SEED_USERNAME", "seeder");
    static final String SEED_EMAIL = System.getenv().getOrDefault("SEED_EMAIL", "seeder@test.com");
    static final String SEED_PASS = System.getenv().getOrDefault("SEED_PASSWORD", "seeder1234");

    static final String SEED_FILE = System.getenv().getOrDefault(
            "SEED_FILE", "src/main/resources/db/seed/insert_another_primarchs.sql");

    static final HttpClient client = HttpClient.newHttpClient();

    record PrimarchSeed(String name, int legionNumber, String allegiance, String status, String homeworld, String description) {}

    public static void main(String[] args) throws Exception {
        String token = System.getenv().getOrDefault("JWT_TOKEN", "").trim();
        if (token.isEmpty()) {
            token = ensureToken();
        }
        if (token.isEmpty()) {
            System.err.println("ERROR: missing JWT. Set $env:JWT_TOKEN or allow auto-login via SEED_USERNAME/SEED_PASSWORD.");
            System.exit(1);
        }
        final String authHeader = "Bearer " + token;

        List<PrimarchSeed> seeds = parsePrimarchsSeed(Path.of(SEED_FILE));
        System.out.println("Seed file: " + SEED_FILE + " -> " + seeds.size() + " primarchs found.");

        String existing = get(PRIMARCHS_URL + "?size=100", authHeader);
        int created = 0, skipped = 0, failed = 0;

        for (PrimarchSeed p : seeds) {
            if (existing != null && existing.contains("\"name\":\"" + p.name() + "\"")) {
                System.out.println("SKIP (already exists): " + p.name());
                skipped++;
                continue;
            }
            String json = toJson(p);
            HttpResponse<String> res = post(PRIMARCHS_URL, json, authHeader);
            int code = res.statusCode();
            if (code == 201 || code == 200) {
                System.out.println("CREATED (" + code + "): " + p.name());
                created++;
            } else if (code == 409 || (res.body() != null && res.body().contains("already"))) {
                System.out.println("SKIP (409 conflict): " + p.name());
                skipped++;
            } else if (code == 401 || code == 403) {
                System.err.println("AUTH ERROR (" + code + ") on " + p.name() + ": " + res.body());
                System.err.println("Get a new token: POST " + AUTH_URL + "/login {\"username\":\"...\",\"password\":\"...\"}");
                failed++;
            } else {
                System.err.println("FAIL (" + code + ") " + p.name() + ": " + res.body());
                failed++;
            }
        }
        System.out.println("Summary: created=" + created + " skipped=" + skipped + " failed=" + failed);
    }

    // --- auth: login, on failure try register ---
    static String ensureToken() throws Exception {
        String loginJson = "{\"username\":\"" + esc(SEED_USER) + "\",\"password\":\"" + esc(SEED_PASS) + "\"}";
        HttpResponse<String> login = post(AUTH_URL + "/login", loginJson, null);
        String t = extractToken(login.body());
        if (t != null) {
            System.out.println("Auto-login OK as '" + SEED_USER + "'.");
            return t;
        }
        System.out.println("Login failed (" + login.statusCode() + "), trying register...");
        String regJson = "{\"username\":\"" + esc(SEED_USER) + "\",\"email\":\"" + esc(SEED_EMAIL) + "\",\"password\":\"" + esc(SEED_PASS) + "\"}";
        HttpResponse<String> reg = post(AUTH_URL + "/register", regJson, null);
        t = extractToken(reg.body());
        if (t != null) {
            System.out.println("Auto-register OK as '" + SEED_USER + "'.");
            return t;
        }
        // user already existed with a different password: return empty for a clear error
        System.err.println("Could not obtain token. login=" + login.body() + " register=" + reg.body());
        return "";
    }

    static String extractToken(String body) {
        if (body == null) return null;
        Matcher m = Pattern.compile("\"token\"\\s*:\\s*\"([^\"]+)\"").matcher(body);
        return m.find() ? m.group(1) : null;
    }

    // --- manual parsing of the .sql ---
    static List<PrimarchSeed> parsePrimarchsSeed(Path file) throws Exception {
        String sql = Files.readString(file);
        // keep only the INSERT INTO primarchs ... ON CONFLICT block
        Matcher block = Pattern.compile(
                "INSERT INTO primarchs.*?VALUES(.*?)ON CONFLICT",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(sql);
        if (!block.find()) throw new IllegalStateException("No INSERT INTO primarchs block found in " + file);
        String values = block.group(1);

        // ('name', legion, 'allegiance', 'status', 'homeworld'|NULL, 'desc'|NULL),
        Pattern tuple = Pattern.compile(
                "\\('((?:[^']|'')*)'\\s*,\\s*(\\d+)\\s*,\\s*'([^']*)'\\s*,\\s*'([^']*)'\\s*,\\s*(?:'((?:[^']|'')*)'|NULL)\\s*,\\s*(?:'((?:[^']|'')*)'|NULL)\\s*\\)",
                Pattern.DOTALL);
        List<PrimarchSeed> out = new ArrayList<>();
        Matcher m = tuple.matcher(values);
        while (m.find()) {
            out.add(new PrimarchSeed(
                    unesc(m.group(1)),
                    Integer.parseInt(m.group(2)),
                    m.group(3),
                    m.group(4),
                    m.group(5) == null ? null : unesc(m.group(5)),
                    m.group(6) == null ? null : unesc(m.group(6))));
        }
        return out;
    }

    static String unesc(String s) {
        return s.replace("''", "'");
    }

    static String toJson(PrimarchSeed p) {
        return "{\"name\":\"" + esc(p.name()) + "\""
                + ",\"legionNumber\":" + p.legionNumber()
                + ",\"allegiance\":\"" + esc(p.allegiance()) + "\""
                + ",\"status\":\"" + esc(p.status()) + "\""
                + (p.homeworld() == null ? ",\"homeworld\":null" : ",\"homeworld\":\"" + esc(p.homeworld()) + "\"")
                + (p.description() == null ? ",\"description\":null" : ",\"description\":\"" + esc(p.description()) + "\"")
                + "}";
    }

    static String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    static String get(String url, String auth) throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(url)).GET();
        if (auth != null) b.header("Authorization", auth);
        return client.send(b.build(), HttpResponse.BodyHandlers.ofString()).body();
    }

    static HttpResponse<String> post(String url, String json, String auth) throws Exception {
        HttpRequest.Builder b = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json));
        if (auth != null) b.header("Authorization", auth);
        return client.send(b.build(), HttpResponse.BodyHandlers.ofString());
    }
}
