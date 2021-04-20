package ollek.mateusz.githubuserslookup.logic;

public class StarsCountResponseHolder {
    private String user_name;
    private int stargazers_count;

    public StarsCountResponseHolder(String user_name){
        this.user_name=user_name;
        stargazers_count=0;
    }


    public void setUser_name(String user_name){
        this.user_name = user_name;
    }

    public String getUser_name(){
        return user_name;
    }

    public void setStargazers_count(int stargazers_count){
        this.stargazers_count=0;
    }

    public int getStargazers_count(){
        return stargazers_count;
    }

    public void updateStargazers_count(int add){
        stargazers_count+=add;
    }
}
