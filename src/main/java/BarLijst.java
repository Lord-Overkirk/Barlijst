import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BarLijst {

    static ArrayList<Customer> customers;
    static ArrayList<Customer> selectedCustomers = new ArrayList<>();

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        BarLijst.customers = new ArrayList<>();

        try {
            List<Customer> fileCustomers = Arrays.asList(mapper.readValue(Paths.get("list.json").toFile(), Customer[].class));
            fileCustomers.forEach(System.out::println);
            customers.addAll(fileCustomers);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Screen s = new Screen();
    }

    public static Customer getCustomer(int uid) {
        for (Customer customer : customers) {
            if (customer.getUid() == uid) {
                return customer;
            }
        }
        return null;
    }
}