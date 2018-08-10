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
	
	private static final String clientId = "";
	private static final String clientSecret = "";
	private static final URI redirectUri = SpotifyHttpManager.makeUri("https://api.spotify.com/v1/users");
	private static final String userID = "";
	
	private static String code = "";
	
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
