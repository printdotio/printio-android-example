package com.example.piosdkpoconcept.photosource.vlado;

import print.io.beans.PhotoData;

public class VladoPhoto extends PhotoData {

	public VladoPhoto(String imageUrl, String thumbnailUrl) {
		this.imageUrl = imageUrl;
		this.thumbnailUrl = thumbnailUrl;
	}

}
