package ollek.mateusz.githubuserslookup.logic;

public class RepoHolder {
    String name;
    int stargazers_count;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setStargazers_count(int stargazers_count){
        this.stargazers_count = stargazers_count;
    }

    public int getStargazers_count(){
        return stargazers_count;
    }
}
