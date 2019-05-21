package Models;

public class BannerModel {

    private String bannerImage;

    public BannerModel(){

    }

    public BannerModel(String bannerImage){

        this.bannerImage = bannerImage;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }
}
