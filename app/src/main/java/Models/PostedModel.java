package Models;

public class PostedModel {

    String purchaseImage,purchaseBookName,postId;

    public PostedModel(){

    }

    public PostedModel(String purchaseImage, String purchaseBookName,String postId){

        this.purchaseBookName = purchaseBookName;
        this.purchaseImage = purchaseImage;
        this.postId = postId;
    }

    public String getPurchaseImage() {
        return purchaseImage;
    }

    public void setPurchaseImage(String purchaseImage) {
        this.purchaseImage = purchaseImage;
    }

    public String getPurchaseBookName() {
        return purchaseBookName;
    }

    public void setPurchaseBookName(String purchaseBookName) {
        this.purchaseBookName = purchaseBookName;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}


