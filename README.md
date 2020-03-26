# awesomePlaylist
<h3>A playlist maker with an authentication system that allows the user to interact with the database, where the songs 
are stored and add them to personal playlist, which stored in the same database.</h3>

This program is a shaper of playlists with a user authentication system.
You can register/login, find songs by different indicators and add them to your personal playlist.

The database consists of: a list of artists, where each artist(artistId, artistName) has his own identifier(artistId) and
name(artistName); from the list of songs where all the information about the song is stored (songId, title, artistId,
albumName, year); registration table(userId, username, password), which is created at the beginning of the program and stores
the identifier, name and password of the user; individual playlist table(Id, songId), which is created with the user and
stores data about those songs that the user added.
