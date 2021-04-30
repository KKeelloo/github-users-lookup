# githubuserslookup
<p>Serwer uses github API without token authorization which is limited to 60 requests per hour</p>
<p>Usage</p>
<p>on default serwer runs on localhost:8080</p>
<p>supports:</p>
<p>GET /repos/{user_name} - gets list of repositories and number of stars </p>
<p>GET /starscount/{user_name} - gets number of stars </p>
<p>to run on Ubuntu 20.04 open root project folder in terminal and type 'mvn spring-boot:run', doing this in Windows 10 cmd/powershell should also work (if there is mvn in the PATH)</p>
<p> may add custom 404 page later</p>
