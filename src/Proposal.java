/**
 * Created by jorgelima on 10-12-2014.
 */
public class Proposal {


    private String product;
    private int price;
    private Request r;

    public Proposal(String prod, Request r, int price)
    {
        this.product = prod;
        this.price = price;
        this.r=r;
    }

    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public Request getR() {
        return r;
    }
}