/**
 * Created by jorgelima on 10-12-2014.
 */
public class Proposal extends CloneableObject {


    private String product;
    private double price;
    private Request r;


    private SellerAgentBDI sa;

    public Proposal(String prod, Request r, double price, SellerAgentBDI seller)
    {
        this.product = prod;
        this.price = price;
        this.r=r;
        this.sa = seller;
    }

    public String getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public Request getR() {
        return r;
    }

    public SellerAgentBDI getSa() {
        return sa;
    }

    public Proposal clone() {
        return (Proposal)super.clone();
    }
}