package Models;

public class FavouriteBooksModel {

    String favImg,favbName,favBookId,favUserId;

    public FavouriteBooksModel(){

    }

    public FavouriteBooksModel(String favImg,String favbName,String favBookId,String favUserId){

        this.favImg = favImg;
        this.favbName = favbName;
        this.favBookId = favBookId;
        this.favUserId = favUserId;
    }

    public String getFavImg() {
        return favImg;
    }

    public void setFavImg(String favImg) {
        this.favImg = favImg;
    }

    public String getFavbName() {
        return favbName;
    }

    public void setFavbName(String favbName) {
        this.favbName = favbName;
    }

    public String getFavBookId() {
        return favBookId;
    }

    public void setFavBookId(String favBookId) {
        this.favBookId = favBookId;
    }

    public String getFavUserId() {
        return favUserId;
    }

    public void setFavUserId(String favUserId) {
        this.favUserId = favUserId;
    }
}
