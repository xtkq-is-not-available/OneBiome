package de.xtkq.onebiome.utils;

import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class UpdateUtils {
    private static final String GITHUB_API = "https://api.github.com/repos/xtkq-is-not-available/%s/releases/latest";
    private static boolean updateAvailable = false;
    private static String latestRelease;
    private static String latestReleaseURL;
    private final JavaPlugin plugin;

    public UpdateUtils(JavaPlugin paramPlugin) {
        this.plugin = paramPlugin;
        latestRelease = paramPlugin.getDescription().getVersion();
    }

    public static boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public static String getLatestRelease() {
        return latestRelease;
    }

    public static String getLatestReleaseURL() {
        return latestReleaseURL;
    }

    public void checkForUpdates() {
        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            Gson gson = new Gson();
            try {
                URL url = new URL(String.format(GITHUB_API, this.plugin.getName()));
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setRequestProperty("accept", "application/vnd.github.v3+json");

                if (httpsURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                    Map<?, ?> map = gson.fromJson(bufferedReader.readLine(), Map.class);
                    bufferedReader.close();

                    if (isUpdateAvailable(this.plugin.getDescription().getVersion(), ((String) map.get("name")).substring(1))) {
                        updateAvailable = true;
                        latestRelease = ((String) map.get("name")).substring(1);
                        latestReleaseURL = (String) map.get("html_url");
                        this.plugin.getLogger().info("Update v" + latestRelease + " is available: " + latestReleaseURL);
                    }
                }
            } catch (Exception ignored) {
            }
            // Start 2min after server start and repeat every 3h
        }, 2400L, 216000L);
    }

    /**
     * Returns True if the provided Version is higher than the local plugin version. Also returns False if the versions are equal.
     *
     * @param paramGithubVersion the plugin version which was retrieved from github.
     * @param paramPluginVersion the local plugin version.
     * @return True if the version on the forums is newer than the local version. False otherwise.
     */
    private boolean isUpdateAvailable(String paramPluginVersion, String paramGithubVersion) {
        String[] localVersion = paramPluginVersion.split("\\.");
        String[] serverVersion = paramGithubVersion.split("\\.");
        int result;
        int i = 0;
        while (i < localVersion.length && i < serverVersion.length && localVersion[i].equals(serverVersion[i])) {
            i++;
        }
        if (i < localVersion.length && i < serverVersion.length) {
            int diff = Integer.valueOf(localVersion[i]).compareTo(Integer.valueOf(serverVersion[i]));
            result = Integer.signum(diff);
        } else {
            result = Integer.signum(localVersion.length - serverVersion.length);
        }
        return result == -1;
    }
}

