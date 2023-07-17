import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {
    private String firstName;
    private String lastName;
    private int balance;
    private int uid;
    private boolean selected;

    static int uid_gen = getMaxUid();

//    public Customer() {}

    @JsonCreator
    public Customer(@JsonProperty("firstName") String firstName,
                    @JsonProperty("lastName") String lastName,
                    @JsonProperty("balance") int balance,
                    @JsonProperty("uid") int uid,
                    @JsonProperty("selected") boolean selected) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.uid = uid;
        this.selected = selected;
        uid_gen++;
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = 0;
        uid_gen = getMaxUid() + 1;
        this.uid = uid_gen;
        this.selected = false;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int i) {
        this.balance = i;
    }

    public int getUid() {
        return uid;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void select(boolean select) {
        this.selected = select;
    }

    private static int getMaxUid() {
        int uid = 0;
        for (Customer customer: BarLijst.customers) {
            if (customer.getUid() > uid) {
                uid = customer.getUid();
            }
        }
        System.out.println("Max id: " + uid);
        return uid;
    }
}
