package Models;

public class MoreBooksModel {

    String moreBookImg,moreBookName,moreBookSub,moreBookClass,moreBookCity;

    public MoreBooksModel(){

    }
    public MoreBooksModel(String moreBookImg,String moreBookName,String moreBookSub,String moreBookClass,
                           String moreBookCity){

        this.moreBookImg = moreBookImg;
        this.moreBookName = moreBookName;
        this.moreBookSub = moreBookSub;
        this.moreBookClass = moreBookClass;
        this.moreBookCity = moreBookCity;
    }

    public String getMoreBookImg() {
        return moreBookImg;
    }

    public void setMoreBookImg(String moreBookImg) {
        this.moreBookImg = moreBookImg;
    }

    public String getMoreBookName() {
        return moreBookName;
    }

    public void setMoreBookName(String moreBookName) {
        this.moreBookName = moreBookName;
    }

    public String getMoreBookSub() {
        return moreBookSub;
    }

    public void setMoreBookSub(String moreBookSub) {
        this.moreBookSub = moreBookSub;
    }

    public String getMoreBookClass() {
        return moreBookClass;
    }

    public void setMoreBookClass(String moreBookClass) {
        this.moreBookClass = moreBookClass;
    }

    public String getMoreBookCity() {
        return moreBookCity;
    }

    public void setMoreBookCity(String moreBookCity) {
        this.moreBookCity = moreBookCity;
    }
}
