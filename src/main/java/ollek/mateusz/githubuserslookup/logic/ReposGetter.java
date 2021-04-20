package ollek.mateusz.githubuserslookup.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ReposGetter {
    private String user_name;
    private List<RepoHolder> repos;

    public ReposGetter(String user_name){
        this.user_name = user_name;
        repos = null;
    }

    public void searchForRepos(){
        try {
            URL url = new URL("https://api.github.com/users/" + user_name + "/repos");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int status = con.getResponseCode();
            if(status == 200){
                String responseString = convertStreamToString(con.getInputStream());
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                repos = mapper.readValue(responseString, new TypeReference<List<RepoHolder>>() { });
            }
            con.disconnect();
        }catch (Exception e){}

    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public StarsCountResponseHolder calcStarsCount(){
        this.searchForRepos();
        StarsCountResponseHolder starsCountResponseHolder = new StarsCountResponseHolder(user_name);
        for(RepoHolder repo: repos){
            starsCountResponseHolder.updateStargazers_count(repo.getStargazers_count());
        }
        return starsCountResponseHolder;
    }

    public void setRepos(List<RepoHolder> repos) {
        this.repos = repos;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public List<RepoHolder> getRepos() {
        return repos;
    }
}
