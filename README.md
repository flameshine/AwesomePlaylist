# awesomePlaylist
<h3>A playlist-shaper with a user authentication system that allows the user to interact with the database, where the songs 
are stored and add them to personal playlist, which stored in the same database.</h3>
<h2>Overview:</h2>
<ul>
<li><h2>Structure</h2>
This program consists of a singleton connection pool, a songDatabase that interacts directly with songs, a userDatabase
that interacts directly with a user authentication system and, of course, main class.
</li>
<li><h2>View</h2>
All interactions in the program occur in the console. Every effort was made to make the interface as user-friendly as 
possible.
</li>
<li><h2>Tools</h2>
This project was developed in Intellij IDEA using standard Java libraries and their tools. Also involved MySQL database and 
Stream API.
</li>
<li><h2>Database</h2>
Consists of: a list of artists, where each artist(artistId, artistName) has his own identifier(artistId) and
name(artistName); from the list of songs where all the information about the song is stored (songId, title, artistId,
albumName, year); registration table(userId, username, password), which is created at the beginning of the program and stores
the identifier, name and password of the user; individual playlist table(Id, songId), which is created with the user and
stores data about those songs that the user added.
</li>
<li><h2>Additions</h2>
This is my first relatively powerful project, which will serve as a start for more serious professional growth.
Also I'd like to mention my dear friend https://github.com/KostiaLeo who helped me achieve these results.
</li>
</ul>
