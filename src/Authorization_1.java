import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;

public class Authorization_1 {
	private static final String clientId = "";
	private static final String clientSecret = "";
	private static final URI redirectUri = SpotifyHttpManager.makeUri("https://api.spotify.com/v1/users");
	
	public static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
			.setClientId(clientId)
			.setClientSecret(clientSecret)
			.setRedirectUri(redirectUri)
			.build();
	private static AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
			.scope("playlist-read-private, playlist-read-collaborative")
			.show_dialog(true)
			.build();
	
	public static void authorizationCodeUri_Sync() {
	    final URI uri = authorizationCodeUriRequest.execute();
    	System.out.println(uri.toString());
	  }

	public static void main(String args[]) {
		authorizationCodeUri_Sync();
	}
}
