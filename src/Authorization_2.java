import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;

public class Authorization_2 extends Authorization_1{

	
	
	// variables
	
	private static final String clientId = "4e553333356e4435a1fcfc3a2ef30562";
	private static final String clientSecret = "a3c64eed7af748a3b771059c6f219cc1";
	private static final URI redirectUri = SpotifyHttpManager.makeUri("https://api.spotify.com/v1/users");
	private static final String userID = "12184443487";
	
	private static String code = "AQBYcRJMk6VIMK12jpnevSFgtkiBsk9yWvaP3sfyj69DGTDeV2udoccTfqQEKK-UukbBy60Qa2Uusd8Fh2MmmYLQE9hg6vxo9AjbJa35MxDPatpR4KIgjx28sHNEhd5HI5Mt0CeAHAfmBQLjDzQjVYsjavRE8g9ekdWN9p_Fp7-5Bdi-iSoSosUVnOnyIU1VtcnZhlCRX6xvf4pBM8hohce3N6ZIxjfpmvyA_Pn1lNU3o5AJLu1bCpd0vYKr4P6wKKImfUxAlWdYKg";
	
	private static AuthorizationCodeRequest authorizationCodeRequest;
	private static AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest;
	private static GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest;
	
	private static Authorization_1 mAuthOneObj;
	
	// init func
	Authorization_2(Authorization_1 authOneObj){
		mAuthOneObj = authOneObj;
	}
	
	private void Authorization_2(Authorization_1 Authorization_1Obj) {
		mAuthOneObj = Authorization_1Obj;
		authorizationCodeRequest = mAuthOneObj.spotifyApi.authorizationCode(code)
				.build();
		authorizationCode_Sync();
	}
	
	public static void authorizationCode_Sync() {
	    try {
	        final Future<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = authorizationCodeRequest.executeAsync();

	        // ...

	        final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeCredentialsFuture.get();

	        // Set access and refresh token for further "spotifyApi" object usage
	        mAuthOneObj.spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
	        mAuthOneObj.spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

	        authorizationCodeRefreshRequest = mAuthOneObj.spotifyApi.authorizationCodeRefresh()
		  	          .build();	        
	        System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
	      } catch (InterruptedException | ExecutionException e) {
	        System.out.println("Error: " + e.getCause().getMessage());
	      }
	    }
	
	  public static void authorizationCodeRefresh_Sync() {
		    try {
		      final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();

		      // Set access and refresh token for further "spotifyApi" object usage
		      mAuthOneObj.spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
		      mAuthOneObj.spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

		      System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
		    } catch (IOException | SpotifyWebApiException e) {
		      System.out.println("Error: " + e.getMessage());
		    }
		  }
	  
	// functions
	  public static void getListOfUsersPlaylists_Sync() {
		    try {		  	
		    	getListOfCurrentUsersPlaylistsRequest = mAuthOneObj.spotifyApi
		    	          .getListOfCurrentUsersPlaylists()
		    	          .limit(10)
		    	          .offset(0)
		    	          .build();
		    	
		      final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();

		      //TODO: Why can't I get the items in the playListSimlifiedPaging?
		      System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
		      System.out.println("Total: " + playlistSimplifiedPaging.getItems());
		    } catch (IOException | SpotifyWebApiException e) {
		    	authorizationCodeRefresh_Sync();
		    	getListOfUsersPlaylists_Sync();
		    }
		  }
	
	
	
	
	
	
	
	// function calls

	public static void mani(String args[]) {
		
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
