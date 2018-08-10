import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;


public class SpotifyRetriever {
	private static final String clientId = "";
	private static final String clientSecret = "";
	private static final URI redirectUri = SpotifyHttpManager.makeUri("https://api.spotify.com/v1/users");
	private static final String userID = "";
	private static AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest;
	private static GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest;
	private static final String code ="";
	private static ArrayList<String> playlistIds = new ArrayList<String>();
	
	
	//Create the spotifyApi object
	private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
			.setClientId(clientId)
			.setClientSecret(clientSecret)
			.setRedirectUri(redirectUri)
			.build();
	
	//Create the object for the authorizationCode
	private static final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
			.scope("playlist-read-private, playlist-read-collaborative")
			.show_dialog(true)
			.build();
	
	//Create the object for the refresh and access token
	private static final AuthorizationCodeRequest authorizationCodeRequest  = spotifyApi.authorizationCode(code)
			.build();
	
	public static void authorizationCodeUri_Sync() {
	    final URI uri = authorizationCodeUriRequest.execute();
	    try {
	    	final URL url = uri.toURL();
	    	URLConnection myURLConnection = url.openConnection();
	    	myURLConnection.connect();
	    }catch(MalformedURLException e) {
	    	System.out.println("Error: "+e.getMessage());
	    }catch(IOException e) {
	    	System.out.println("Error: "+e.getMessage());
	    }
    	System.out.println(uri.toString());
	  }

	public static void authorizationCode_Sync() {
	    try {
	        final Future<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = authorizationCodeRequest.executeAsync();

	        // ...

	        final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeCredentialsFuture.get();

	        // Set access and refresh token for further "spotifyApi" object usage
	        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
	        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

	        authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh()
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
		      spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
		      spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

		      System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
		    } catch (IOException | SpotifyWebApiException e) {
		      System.out.println("Error: " + e.getMessage());
		    }
		  }
	  
	  public static void getListOfUsersPlaylists_Sync() {
		    try {		  	
		    	getListOfCurrentUsersPlaylistsRequest = spotifyApi
		    	          .getListOfCurrentUsersPlaylists()
		    	          .limit(20)
		    	          .offset(0)
		    	          .build();
		    	
		      final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();

		      System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
		      PrintWriter pw = new PrintWriter(new FileOutputStream("results"));

		      for (PlaylistSimplified s: playlistSimplifiedPaging.getItems()) {
		    	  pw.println(s.getName());
		    	  pw.println(s.getId());
		      }
	    	  pw.close();

		      System.out.println("Total: " + playlistSimplifiedPaging.getItems());
		    } catch (IOException | SpotifyWebApiException e) {
		    	authorizationCodeRefresh_Sync();
		    	getListOfUsersPlaylists_Sync();
		    }
		  }
	  

	  
	  public static void getPlaylistsTracks_Sync() {
	       int i = 1;
		  for (String id:playlistIds) {
			  final GetPlaylistsTracksRequest getPlaylistsTracksRequest = spotifyApi
			          .getPlaylistsTracks(userID, id)
			          .offset(0)
			          .build();
			    try {
			       final Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsTracksRequest.execute();
			      PrintWriter pw = new PrintWriter(new FileOutputStream("trackResults"+i));

			      for (PlaylistTrack s: playlistTrackPaging.getItems()) {
			    	  pw.println(s.getTrack().getName());
			      }
			      pw.close();
			      System.out.println("Total: " + playlistTrackPaging.getTotal());
			      i++;
			    } catch (IOException | SpotifyWebApiException e) {
			      System.out.println("Error: " + e.getMessage());
			    }
			  }
	  }
		public static void getPlaylistIDs() {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader("C:\\Projects\\Workspaces\\SpotifyPlaylistMigration\\results"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			String i = "";
			try {
				while ((i=br.readLine())!=null) {
					playlistIds.add(i);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	public static void main(String args[]) {
//		authorizationCodeUri_Sync();
		getPlaylistIDs();
		authorizationCode_Sync();
//		authorizationCodeRefresh_Sync();
//		getListOfUsersPlaylists_Sync();
		getPlaylistsTracks_Sync();
		
	}
}

