# awesomePlaylist
<h3>A playlist-shaper with a user authentication system that allows the user to interact with the database, where the songs 
are stored and add them to personal playlist, which stored in the same database.</h3>
<h3>Overview</h3>
<ul>
<li><h3>Structure</h3>
This program consists of a singleton connection pool, a songDatabase that interacts directly with songs, a userDatabase
that interacts directly with a user authentication system and, of course, main class.
</li>
<li><h3>View</h3>
All interactions in the program occur in the console. Every effort was made to make the interface as user-friendly as 
possible.
</li>
<li><h3>Database</h3>
Consists of: a list of artists, where each artist(artistId, artistName) has his own identifier(artistId) and
name(artistName); from the list of songs where all the information about the song is stored (songId, title, artistId,
albumName, year); registration table(userId, username, password), which is created at the beginning of the program and stores
the identifier, name and password of the user; individual playlist table(Id, songId), which is created with the user and
stores data about those songs that the user added.
</li>
</ul>
