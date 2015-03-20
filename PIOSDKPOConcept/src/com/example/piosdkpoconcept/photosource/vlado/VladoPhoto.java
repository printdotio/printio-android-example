package com.example.piosdkpoconcept.photosource.vlado;

public class VladoPhoto {

	protected String imageUrl;
	protected String thumbnailUrl;
	protected boolean selected;
	protected int indexOfPhoto = -1;

	public VladoPhoto(String imageUrl, String thumbnailUrl) {
		this.imageUrl = imageUrl;
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public boolean isSelected() {
		return selected;
	}

	public boolean setSelected(boolean selected) {
		return selected;
	}

	public int getIndexOfPhoto() {
		return indexOfPhoto;
	}

	public void setIndexOfPhoto(int indexOfPhoto) {
		this.indexOfPhoto = indexOfPhoto;
	}

}
