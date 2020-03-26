# awesomePlaylist
A really cool playlist creator with an authentication system, the ability to search, add and save the songs you need. 

This program is a shaper of playlists with a user authentication system.
You can register/login, find songs by different indicators and add them to your personal playlist.

The database consists of: a list of artists, where each artist(artistId, artistName) has his own identifier(artistId) and
name(artistName); from the list of songs where all the information about the song is stored (songId, title, artistId,
albumName, year); registration table(userId, username, password), which is created at the beginning of the program and stores
the identifier, name and password of the user; individual playlist table(Id, songId), which is created with the user and
stores data about those songs that the user added.
