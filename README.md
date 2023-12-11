# Proiect GlobalWaves  - Etapa 2
Ciocea Bogdan-Andrei 323CA
(literally Ryan Gosling)
<div align="center"><img src="https://media1.tenor.com/m/Jsh8LhS7PuYAAAAC/i-drive-drive.gif" width="600px"></div>

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa1](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa1)


## Skel Structure

* src/
  * checker/ - checker files
  * fileio/ - contains classes used to read data from the json files
  * app/ - contains the classes that implement the application
    * audio/ - contains the classes that implement the audio part of the application
     * Helpers/ - contains the classes that implement the helpers for the audio part of the application
    * merch/ - contains the classes that implement the merch part of the application
    * prints/ - contains the classes that implement the prints part of the application
    * event/ - contains the class Event used to store the events in the application
    * announcement/ - contains the class Announcement used to store the announcements in the application
  * main/
      * Main - the Main class runs the checker on your implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
      * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
        to the out.txt file. Thus, you can compare this result with ref.
* input/ - contains the tests and library in JSON format
* ref/ - contains all reference output for the tests in JSON format

<div align="center"><img src="https://media.tenor.com/dRP_otVcxVkAAAAM/ryan-gosling-car.gif" width="500px"></div>


## Implementation
## `changePage` Method Overview

The `changePage` method is designed to manage user page navigation in our application. Here's a breakdown of its key functionalities:

### Functionality
- **User Identification**: Retrieves a user based on the username from the `commandInput`.
- **Existence Verification**: Checks that the user exists to prevent errors.
- **Response Message Initialization**: Sets up a message for successful page navigation.
- **Page Access Validation**: Ensures users only access pages appropriate for their role.
- **Online Status Check**: Verifies if the user is online and updates the message if they're not.
- **User Page Update**: Updates the user's current page in the system.
- **Response Composition**: Puts together a detailed response with the command, username, timestamp, and the message.
- **Response Delivery**: Sends back the structured response, usually for front-end display.

## `printCurrentPage` Method Overview

The `printCurrentPage` method in our application is designed to display detailed information relevant to the user's current page view. It caters to different page contents dynamically based on user interaction and status.

### Functionality
- **User Identification**: Retrieves user details using the username from `commandInput`.
- **Initial Placeholder Message**: Sets up a preliminary default message.
- **User Existence Verification**: Checks if the user exists in the system.
- **Processing Liked Songs and Playlists**: For online users, retrieves and processes their liked songs and followed playlists.
- **Dynamic Content Display**:
  - On the 'Home' page, shows top liked songs and playlists.
  - The 'LikedContent' page displays detailed liked songs and playlists.
  - For the 'Artist' page, it lists albums, merchandise, and events of the selected artist.
  - On the 'Host' page, provides information about podcasts and announcements by the selected host.
- **Offline Status Handling**: Updates the message if the user is offline.
- **Response Message Composition**: Tailors the response message based on the user's current page and interaction details.
- **Response Structuring**: Creates an `ObjectNode` for a structured response including user details and the message.

## `addUser` Method Overview

The `addUser` method is designed to add a new user to the application. It checks for existing usernames and handles the addition of new users based on their role.

### Functionality
- **Instance Retrieval**: Fetches an instance of the `Admin` class.
- **User Lookup**: Checks if a user with the provided username already exists.
- **Host Helpers Clearance**: Clears host helpers if the host list is empty.
- **User Existence Check**: If the user already exists, sets a message indicating the username is taken.
- **User Creation**: If the username is not taken, creates a new `User` object with details from `commandInput`.
- **Role-based Processing**: Depending on the user type ('artist' or 'host'), it performs specific actions:
  - For artists, adds them to the artists list and creates an `ArtistHelper`.
  - For hosts, clears host helpers if needed, adds them to the hosts list, and creates a `HostHelper`.
- **User Addition**: Adds the new user to the general users list.
- **Success Message**: Sets a success message once the user is added.
- **Response Structuring**: Constructs an `ObjectNode` to structure the response, including the command, username, timestamp, and the message.

## `deleteUser` Method Overview

The `deleteUser` method is designed for removing a user from the application. It handles different types of users (hosts, artists, and regular users) and ensures safe deletion without disrupting ongoing activities.

### Functionality
- **Initial Success Message**: Assumes the user will be deleted successfully.
- **Admin Instance Retrieval**: Fetches an instance of `Admin` for global data access.
- **Role-Specific Deletion Logic**:
  - **Host User**: Checks if any user is on the host's page or playing an episode from the host's podcast. If not, proceeds to delete the host and their podcasts.
  - **Artist User**: Similar checks for artist pages and songs. Deletes the artist, their songs, and albums, and updates related lists.
  - **Regular User**: Checks if the user's playlists are being used by others. Deletes the user and updates liked songs and followed playlists.
- **Safe Deletion Checks**: Ensures the user can be safely deleted without affecting other users.
- **Data Cleanup**: Removes the user's data from various lists and performs necessary cleanups.
- **Conditional Return Messages**: Returns a message indicating either successful deletion or the reason for inability to delete.

## `showAlbums` Method Overview

The `showAlbums` method is responsible for retrieving and displaying albums associated with a specific user. This method is part of the application's feature to showcase a user's album collection.

### Functionality
- **Admin Instance Retrieval**: Fetches an instance of `Admin` to access global data.
- **User Lookup**: Retrieves the user based on the provided username from `commandInput`.
- **ObjectNode Creation**: Initializes an `ObjectNode` for structuring the response.
- **Response Data Initialization**: Adds the command, username, and timestamp to the `objectNode`.
- **Albums Processing**: Creates a list of `AlbumHelper` objects, each representing an album with its songs.
  - For each album, it adds the album's name and the names of its songs to a new `AlbumHelper` instance.
- **Result Addition**: Converts the list of `AlbumHelper` objects to a JSON tree and adds it to the `objectNode` under "result".
- **Return Structured Response**: Returns the `objectNode` containing the command details and the formatted list of albums.

## `showPodcasts` Method Overview

The `showPodcasts` method is designed to retrieve and display podcasts associated with a specific user. It forms part of the application's functionality for presenting a user's podcast collection.

### Functionality
- **Admin Instance Retrieval**: Obtains an instance of the `Admin` class to access application-wide data.
- **User Lookup**: Retrieves the user details based on the username provided in the `commandInput`.
- **Podcast Helpers Initialization**: Initializes a list of `PodcastHelper` objects to represent each podcast.
- **User Podcasts Processing**: Iterates through each podcast of the user, creating a `PodcastHelper` object with the name of the podcast and its episodes.
- **Response Structure Setup**: Initializes an `ObjectNode` for the response.
  - Adds command, user, and timestamp information to the `objectNode`.
  - Converts the list of `PodcastHelper` objects to a JSON tree and includes it under "result" in the `objectNode`.
- **Return Structured Response**: Returns the `objectNode` containing the details of the command, user information, and the structured list of podcasts.

## `addAlbum` Method Overview

The `addAlbum` method is crafted to facilitate artists in adding new albums to the application. It includes validations to ensure that only artists can add albums and that album names are unique.

### Functionality
- **Admin Instance Retrieval**: Gets the singleton instance of `Admin`.
- **Artist Verification**: Checks if the current user is an artist. If not, returns a message indicating the user is not an artist.
- **Album Name Uniqueness Check**: Ensures the artist does not already have an album with the same name.
- **Song Uniqueness in Album**: Creates a set to ensure all songs in the new album are unique.
- **New Album Creation**: Creates a new `Album` with details provided in `commandInput`.
- **Album Addition to Artist**: Adds the newly created album to the artist's list of albums.
- **Library Data Management**:
  - Reads existing library data from a JSON file.
  - Adds new songs from the album to the library.
  - Handles IOExceptions during file read operations.
- **Artist Record Update**: Adds the new album to the artist's record in the admin's data, if the artist exists.
- **Album Search Helper Addition**: Adds a new `AlbumSearchHelper` for the newly added album.
- **Global Song List Update in Admin**: Updates the global song list with new songs.
- **Success Message Return**: Returns a message indicating successful addition of the new album.

## `removeAlbum` Method Overview

The `removeAlbum` method is built to facilitate artists in removing their albums from the application. It includes several checks to ensure safe removal without affecting other users.

### Functionality
- **Admin Instance Retrieval**: Obtains the singleton instance of `Admin` for accessing user and album data.
- **Artist Retrieval and Verification**: Retrieves the artist using the provided username and verifies their existence and artist status.
- **Album Existence Check**: Checks if the artist has an album with the specified name.
- **User Activity Checks**:
  - Ensures no user is currently playing a song from the album.
  - Verifies that no song from the album is in any user's playlist.
- **Song Removal from User Data**:
  - Removes the album's songs from all users' liked songs.
  - Removes the album's songs from the global songs list.
- **Album Removal**: Deletes the album from the global albums list and the artist's album list.
- **Success Message**: Returns a message confirming successful deletion of the album.

## `addEvent` Method Overview

The `addEvent` method enables artists to add new events to the application. It includes validation for the artist's role and checks for date and event name uniqueness.

### Functionality
- **Artist Role Verification**: Checks if the user is an artist. If not, returns a message indicating the user is not an artist.
- **Date Parsing and Validation**:
  - Parses the event date from the `commandInput`.
  - Uses `DateTimeFormatter` for date format consistency.
  - Handles invalid date formats with a `DateTimeParseException`.
  - Checks for valid date range and specific conditions like the maximum day in February.
- **Event Name Uniqueness Check**: Ensures that the event name is unique among the artist's events.
- **Event Creation and Addition**:
  - Creates a new `Event` with the provided name, description, and date.
  - Adds the new event to the artist's list of events.
- **Success Message**: Returns a message confirming the successful addition of the new event.

## `removeEvent` Method Overview

The `removeEvent` method is designed to allow artists to remove their events from the application. It ensures that only artists can delete events and verifies the existence of the event before deletion.

### Functionality
- **Artist Role Verification**: Confirms that the user is an artist. If not, a message indicating the user is not an artist is returned.
- **Event Existence Check**: Searches for the event with the specified name among the artist's events.
- **Event Deletion**:
  - If the event is found, it is removed from the artist's list of events.
  - If the event is not found, a message indicating the absence of the event is returned.
- **Success Message**: Returns a confirmation message once the event is successfully deleted.

## `addMerch` Method Overview

The `addMerch` method facilitates artists in adding new merchandise items to the application. It includes checks for the artist's role, merchandise name uniqueness, and price validation.

### Functionality
- **Artist Role Verification**: Confirms if the user is an artist. Returns an error message if the user is not an artist.
- **Merchandise Name Uniqueness Check**: Ensures the artist does not already have merchandise with the same name.
- **Price Validation**: Checks if the price of the merchandise is a non-negative value.
- **Merchandise Creation and Addition**:
  - If all conditions are met, creates a new `Merch` object with the provided name, description, and price.
  - Adds the new merchandise to the artist's list of merchandise.
- **Success Message**: Returns a confirmation message once the merchandise is successfully added.

## `addPodcast` Method Overview

The `addPodcast` method is designed to enable hosts to add new podcasts to the application. It includes checks for the host's role and podcast name uniqueness, as well as episode validation.

### Functionality
- **Host Role Verification**: Confirms if the user is a host. Returns an error message if not.
- **Podcast Name Uniqueness Check**: Ensures no existing podcast has the same name as the new one.
- **Episode Processing**: Converts the provided episodes into `Episode` objects.
- **Episode Uniqueness Check**: Ensures the new episodes are not duplicates of existing ones in any of the host's podcasts.
- **Podcast Creation and Addition**:
  - Creates a new `Podcast` object with the provided name and episodes.
  - Adds the new podcast to the host's podcast list and the admin's global podcast list, provided all conditions are met.
- **Success Message**: Returns a message confirming the successful addition of the new podcast.

## `removePodcast` Method Overview

The `removePodcast` method is crafted to enable hosts to remove their podcasts from the application. This includes validation of the host's role and checks to ensure the podcast is not currently being played by any user.

### Functionality
- **Host Role Verification**: Checks if the user is a host. Returns an error message if not.
- **Podcast Existence Check**: Searches for the podcast with the specified name among the host's podcasts.
- **User Activity Check**:
  - Ensures no user is currently playing the podcast.
  - If a user is playing it, returns an error message.
- **Podcast Removal**:
  - If the podcast is found and not being played, it is removed from both the host's list of podcasts and the admin's global podcast list.
- **Success Message**: Returns a message confirming successful deletion of the podcast.

## `addAnnouncement` Method Overview

The `addAnnouncement` method enables hosts to add new announcements to the application. It includes checks to verify the user's role as a host and ensures the uniqueness of the announcement name.

### Functionality
- **Host Role Verification**: Confirms whether the user is a host. If not, an error message is returned.
- **Announcement Name Uniqueness Check**: Ensures the host does not already have an announcement with the same name.
- **Announcement Addition**: If the user is a host and the announcement name is unique, a new `Announcement` is created and added to the host's list of announcements.
- **Success Message**: Returns a confirmation message indicating the successful addition of the new announcement.

## `removeAnnouncement` Method Overview

The `removeAnnouncement` method is designed to allow hosts to delete their announcements from the application. It includes a role check to ensure only hosts can perform this action and verifies the existence of the announcement before deletion.

### Functionality
- **Host Role Verification**: Checks if the user is a host. If not, returns an error message.
- **Announcement Existence Check**: Searches for the announcement with the specified name among the host's announcements.
- **Announcement Deletion**:
  - If the announcement is found, it is removed from the host's list of announcements.
  - If the announcement is not found, an error message indicating its absence is returned.
- **Success Message**: Returns a confirmation message upon successful deletion of the announcement.

## `switchConnectionStatus` Method Overview

The `switchConnectionStatus` method is created to toggle the online status of regular users (non-artists and non-hosts) in the application. It checks the user's role and switches their online status accordingly.

### Functionality
- **Role Check**: Verifies whether the user is a regular user (neither an artist nor a host). If the user is an artist or a host, it returns an error message.
- **Connection Status Toggle**: Changes the user's online status from online to offline or vice versa.
- **Success Message**: Returns a message confirming the successful change of status.

## `getTop5Albums` Method Overview

The `getTop5Albums` method is designed to retrieve the top 5 albums based on their popularity, as indicated by the number of likes. This method sorts the albums and picks the top ones, providing a quick view of the most liked albums.

### Functionality
- **Album Sorting**: Sorts the albums first by the number of likes in descending order, and then by name for tie-breaking.
- **Top Album Selection**:
  - Iterates through the sorted list of albums.
  - Adds the names of the albums to the `topAlbums` list until the top 5 are selected.
- **Return Top Albums**: Returns a list of the names of the top 5 albums.

## `getTop5Artists` Method Overview

The `getTop5Artists` method retrieves a list of the top 5 artists, presumably based on popularity or a similar metric. This method is part of the application's functionality for showcasing leading artists.

### Functionality
- **Admin Instance Retrieval**: Obtains an instance of the `Admin` class to access global data.
- **Top Artists Retrieval**: Calls `adminInstance.getTop5Artists()` to get a list of the top 5 artists.
- **ObjectNode Creation**: Initializes an `ObjectNode` for structuring the response.
- **Response Data Population**:
  - Adds the command and timestamp from `commandInput` to the `objectNode`.
  - Converts the list of top artists into a JSON tree and adds it under "result" in the `objectNode`.
- **Return Structured Response**: Returns the `objectNode` containing the command details and the list of top artists.

## `getAllUsers` Method Overview

The `getAllUsers` method retrieves and categorizes all users in the application into normal users, artists, and hosts. It's designed to provide a comprehensive view of the user base, sorted by their roles.

### Functionality
- **Admin Instance Retrieval**: Gets the singleton instance of `Admin` to access the list of all users.
- **User Categorization**:
  - Separates users into normal users, artists, and hosts based on their type.
- **Sorting Users**:
  - Compiles a sorted list of usernames, starting with normal users, followed by artists, and then hosts.
- **ObjectNode Creation and Population**:
  - Initializes an `ObjectNode` to structure the response.
  - Adds command and timestamp information from `commandInput` to the `objectNode`.
  - Converts the sorted list of usernames into a JSON tree and adds it under "result" in the `objectNode`.
- **Return Structured Response**: Returns the `objectNode` containing the details of the command and the categorized list of users.

## `getOnlineUsers` Method Overview

The `getOnlineUsers` method is designed to retrieve a list of all users who are currently online in the application. This function is particularly useful for tracking user activity and presence.

### Functionality
- **Online User Retrieval**: Iterates through all users, adding the usernames of those who are online to a list.
- **Admin Instance Retrieval**: Obtains an instance of `Admin` to access the list of all users.
- **ObjectNode Creation and Population**:
  - Initializes an `ObjectNode` for structuring the response.
  - Adds the command and timestamp from `commandInput` to the `objectNode`.
  - Converts the list of online usernames into a JSON tree and includes it under "result" in the `objectNode`.
- **Return Structured Response**: Returns the `objectNode` containing the command details and the list of online users.
