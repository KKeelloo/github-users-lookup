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
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReposGetter {
    private String user_name;
    private List<RepoHolder> repos;
    private static final Logger logger = LoggerFactory.getLogger(ReposGetter.class);
    public ReposGetter(String user_name){
        this.user_name = user_name;
        repos = null;
    }

    private HttpURLConnection createConnection(URL url) throws IOException{
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        return con;
    }

    private void errorThrower(HttpURLConnection con, int status) throws Exception{
        if(status==404){
            con.disconnect();
            throw new Exception("Make sure username is correct");
        }else{
            logger.info("message:" + con.getResponseMessage());
            if(con.getResponseMessage().compareTo("rate limit exceeded")==0){
                con.disconnect();
                throw new Exception("Looks like rate limit is exceeded, try again later");
            }
            con.disconnect();
            throw new Exception("Something unexpected happened");
        }
    }

    public void searchForRepos() throws Exception{
        try {
            Integer number_of_repos = this.numberOfRepos();
            if(number_of_repos==null){
                return;
            }
            logger.info(user_name+" should have " + number_of_repos + " repos");
            int rcv_counter = 1;
            while ((rcv_counter-1)*100 < number_of_repos) {
                URL url = new URL("https://api.github.com/users/" + user_name + "/repos?per_page=100&page=" + rcv_counter);
                HttpURLConnection con = createConnection(url);
                int status = con.getResponseCode();
                if (status == 200) {
                    String responseString = convertStreamToString(con.getInputStream());
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    if (repos == null)
                        repos = mapper.readValue(responseString, new TypeReference<List<RepoHolder>>() {
                        });
                    else
                        repos.addAll(mapper.readValue(responseString, new TypeReference<List<RepoHolder>>() {
                        }));
                    con.disconnect();
                }else
                    errorThrower(con, status);
                rcv_counter++;
            }
            logger.info(user_name+ " has " + repos.size() + " repos");
        }catch (IOException e){
            throw new Exception("Something went wrong while getting results this is bad");
        }
    }

    private Integer numberOfRepos() throws Exception{
        try {
            URL url = new URL("https://api.github.com/users/" + user_name);
            HttpURLConnection con = createConnection(url);
            int status = con.getResponseCode();
            if(status == 200){
                String responseString = convertStreamToString(con.getInputStream());
                con.disconnect();
                JSONObject jsonObject = (JSONObject) JSONValue.parse(responseString);
                return Integer.parseInt(jsonObject.get("public_repos").toString());
            }else
                errorThrower(con, status);
        }catch (IOException e){}
        return null;
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

    public StarsCountResponseHolder calcStarsCount() throws Exception{
        this.searchForRepos();
        if(repos == null)
            return null;
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
