package com.example.piosdkpoconcept;

import java.util.Arrays;
import java.util.List;

import print.io.photosource.PhotoSource;
import print.io.photosource.impl.dropbox.DropboxPhotoSource;
import print.io.photosource.impl.facebook.FacebookPhotoSource;
import print.io.photosource.impl.flickr.FlickrPhotoSource;
import print.io.photosource.impl.instagram.InstagramPhotoSource;
import print.io.photosource.impl.phone.PhonePhotoSource;
import print.io.photosource.impl.photobucket.PhotobucketPhotoSource;
import print.io.photosource.impl.picasa.PicasaPhotoSource;
import print.io.photosource.impl.preselected.PreselectedPhotoSource;

import com.example.piosdkpoconcept.photosource.vlado.VladoPhotoSource;

/**
 * Encapsulates mechanism for creating photo sources used by Example app.
 * 
 * @author Vlado
 */
public class PhotoSourceFactory {

	private PhotoSource dropboxPS;
	private PhotoSource facebookPS;
	private PhotoSource flickrPS;
	private PhotoSource instagramPS;
	private PhotoSource phonePS;
	private PhotoSource photobucketPS;
	private PhotoSource picasaPS;
	private PhotoSource preselectedPS;
	private PhotoSource vladoPS;

	public PhotoSource getDropboxPS() {
		if (dropboxPS == null) {
			DropboxPhotoSource dropboxPS = new DropboxPhotoSource();
			dropboxPS.setConsumerKey(PIOConstants.Dropbox.CONSUMER_KEY);
			dropboxPS.setConsumerSecret(PIOConstants.Dropbox.CONSUMER_SECRET);
			this.dropboxPS = dropboxPS;
		}
		return dropboxPS;
	}

	public PhotoSource getFacebookPS() {
		if (facebookPS == null) {
			facebookPS = new FacebookPhotoSource();
		}
		return facebookPS;
	}

	public PhotoSource getFlickrPS() {
		if (flickrPS == null) {
			FlickrPhotoSource flickrPS = new FlickrPhotoSource();
			flickrPS.setConsumerKey(PIOConstants.Flickr.CONSUMER_KEY);
			flickrPS.setConsumerSecret(PIOConstants.Flickr.CONSUMER_SECRET);
			this.flickrPS = flickrPS;
		}
		return flickrPS;
	}

	public PhotoSource getInstagramPS() {
		if (instagramPS == null) {
			InstagramPhotoSource instagramPS = new InstagramPhotoSource();
			instagramPS.setClientId(PIOConstants.Instagram.CLIENT_ID);
			instagramPS.setCallbackUri(PIOConstants.Instagram.CALLBACK_URI);
			this.instagramPS = instagramPS;
		}
		return instagramPS;
	}

	public PhotoSource getPhonePS() {
		if (phonePS == null) {
			phonePS = new PhonePhotoSource();
		}
		return phonePS;
	}

	public PhotoSource getPhotobucketPS() {
		if (photobucketPS == null) {
			PhotobucketPhotoSource photobucketPS = new PhotobucketPhotoSource();
			photobucketPS.setClientId(PIOConstants.Photobucket.CLIENT_ID);
			photobucketPS.setClientSecret(PIOConstants.Photobucket.CLIENT_SECRET);
			this.photobucketPS = photobucketPS;
		}
		return photobucketPS;
	}

	public PhotoSource getPicasaPS() {
		if (picasaPS == null) {
			picasaPS = new PicasaPhotoSource();
		}
		return picasaPS;
	}

	public PhotoSource getPreselectedPS() {
		if (preselectedPS == null) {
			preselectedPS = new PreselectedPhotoSource();
		}
		return preselectedPS;
	}

	public PhotoSource getVladoPS() {
		if (vladoPS == null) {
			vladoPS = new VladoPhotoSource();
		}
		return vladoPS;
	}

	public List<PhotoSource> getAll() {
		return Arrays.asList(getDropboxPS(),
				getFacebookPS(),
				getFlickrPS(),
				getInstagramPS(),
				getPhonePS(),
				getPhotobucketPS(),
				getPicasaPS(),
				getPreselectedPS(),
				getVladoPS());
	}

}
