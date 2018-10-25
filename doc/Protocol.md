# Protocol Definition

In order to provide communication between Multicast servers and RMI Servers,
 a protocol has been defined for message standardization.

## Message Format

The messages' format will be as such:

` key:value;key2:value2 `

All key-value pairs in a single message will be related to a single object,
 separated by a \n character at the end of each message. As an example,
 
`type:artist;name:Artist Name;activity_start:1970;activity_end:2000`

# Protocol

## Register

When a user wishes to register a new account:

-   `type:register;user:<username>;password:<password>`

If the registration was successful:

-   `type:register_response;status:successful`

If not:

-   `type:register_response;status:unsuccessful;reason:<WHY>`

## Login

When a user tries to login:

-   `type:login_request;user:<username>;password:<password>`

If the user logins:

-   `type:login_auth;status:granted;editor:<true/false>;notifications:<true/false>;notification_text:<notification_text1>;notification_text:<notification_text2>;...`

If the user fails to authenticate:

-   `type:login_auth;status:failed`

## Search

### Artist
When the user searches for an artist:

-   `type:artist_search;name:<artist_name>`

If an artist, or several, is found:

-   `type:artist_search_response;status:found;name:<artist_name1>;name:<artist_name2>;...`

If none are found:

-   `type:artist_search_response;status:not_found`

---
If the user wishes to see more info on the artist:

-   `type:artist_info;name:<artist_name>`

If info is retrieved:

-   `type:artist_info_response;status:found;name:<artist_name>;activity_start:<date1>;activity_end:<date2>;description:<description>;album:<album_name1>;album_release:<album_release_date1>;album:<album_name2>;album_release:<album_release_date2> ...`

if not:

-   `type:artist_info_response;status:not_found`

### Album
When the user searches for an album of an artist:

-   `type:album_search_artist;name:<artist_name>`

When the user searches for an album directly:

-   `type:album_search;name:<album_name>`

If an album, or several, is found:

-   `type:album_search_response;status:found;name:<album_name1>:name:<album_name2>;...`

If none are found:

-   `type:album_search_response;status:not_found`

---
If the user wishes to see more info on the album:

-   `type:album_info;artist_name:<artist_name>;album_name:<album_name>`

If the info is found:

-   `type:album_info_reponse;status:found;artist_name:<artist_name>;album_name:<album_name>;song:<song1>;song:<song2>;...;review:<review_score>;review_desc:<review_description>;...;`

If not:

-   `type:album_info_response;status:not_found`

---
If the user wishes to write a review:

-   `type:album_review;artist_name:<artist_name>;album_name:<album_name>;review:<review_score>;review_desc:<review_description>`

If the review was made successfully:

-   `type:album_review_response;status:successful`

If there was an error creating it:

-   `type:album_review_response;status:unsuccessful`