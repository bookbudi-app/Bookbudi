package Models;

public class FaqModel {

    String faqQns,faqAns;

    public FaqModel(){

    }

    public FaqModel(String faqQns,String faqAns){

        this.faqQns = faqQns;
        this.faqAns = faqAns;
    }

    public String getFaqQns() {
        return faqQns;
    }

    public void setFaqQns(String faqQns) {
        this.faqQns = faqQns;
    }

    public String getFaqAns() {
        return faqAns;
    }

    public void setFaqAns(String faqAns) {
        this.faqAns = faqAns;
    }
}
