package Models;

public class LoadHomeBooks {

    String bName,bImage,bUid,bId,bSub,bClass;

    public LoadHomeBooks(){

    }

    public LoadHomeBooks(String bName,String bImage,String bUid,String bId,String bSub,String bClass){

        this.bName = bName;
        this.bImage = bImage;
        this.bUid = bUid;
        this.bId = bId;
        this.bSub = bSub;
        this.bClass = bClass;

    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbImage() {
        return bImage;
    }

    public void setbImage(String bImage) {
        this.bImage = bImage;
    }

    public String getbUid() {
        return bUid;
    }

    public void setbUid(String bUid) {
        this.bUid = bUid;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getbSub() {
        return bSub;
    }

    public void setbSub(String bSub) {
        this.bSub = bSub;
    }

    public String getbClass() {
        return bClass;
    }

    public void setbClass(String bClass) {
        this.bClass = bClass;
    }
}
