/**
 * Created by jorgelima on 13-12-2014.
 */
public class Negotiation extends CloneableObject {



    double newPrice;
    Proposal p;

    public Negotiation(double newPrice, Proposal p){
        this.newPrice = newPrice;
        this.p = p;
    }

    public Proposal getP() {
        return p;
    }

    public double getNewPrice() {
        return newPrice;
    }

}
