package deletebonds;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.calypso.tk.core.Product;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.util.ConnectionUtil;

/**
 * <pre>
* Author         : CPQi Argentina, Henrique Neto, L0665398
* Purpose        : <Purpose>
* Input files    : N/A
* Log File       : N/A
* Output file    : N/A
*
 * Copyright 2017 Banco Galicia
 * </pre>
 */
public class AppDeleteProducts {
    
    public static final String ID	= "id";
    public static final String HELP	= "help";
    public static final String ISIN_RIC	= "isin_ric";

    public static void main(String[] args) throws Exception {
	if (args == null || args.length == 0 || args.length == 1 && HELP.equals(args[0])) {
	    System.out.println("Usage Params:");
	    System.out.println("user password env file_path file_type");
	    System.out.println("User:  user to connect to delete the products");
	    System.out.println("Password:  password to the user to connect to delete the products");
	    System.out.println("Env: Enviroment to delete the product ");
	    System.out.println("file_path: Full path to the file that contains the products to be removed ");
	    System.out.println("file_type: File content. It should be 'id' or 'isin_ric' ");
	    
	} else {
	    if (args == null || args.length != 5) {
		throw new Exception("Should be informed 5 parameters.");
	    }
	    String option = args[4];

	    switch (option) {
		case ID:
		    deletePorId(args);
		    break;
		case ISIN_RIC:
		    deletePorRicIsin(args);
		    break;
		default:
		    throw new Exception("Invalid option: " + option);
	    }
	}
    }

    public static void deletePorId(String[] args) throws Exception {
	ConnectionUtil.connect(args[0], args[1], "Navigator", args[2]);
	List<String> ids = Files.readAllLines(Paths.get(args[3]));
	for (String id : ids) {
	    Product product = DSConnection.getDefault().getRemoteProduct().getProduct(Integer.valueOf(id));
	    if (product == null) {
		System.out.println("Produto  " + ids + " nao encontrado.");
	    } else {
		DSConnection.getDefault().getRemoteProduct().removeProduct(product);
	    }

	}
    }
    
    public static void deletePorRicIsin(String[] args) throws Exception {
	ConnectionUtil.connect(args[0], args[1], "Navigator", args[2]);
	List<String> isins = Files.readAllLines(Paths.get(args[3]));
	for (String isin : isins) {
	    Product product = DSConnection.getDefault().getRemoteProduct().getProductByCode("ISIN", isin);
	    if (product == null) {
		product = DSConnection.getDefault().getRemoteProduct().getProductByCode("RIC", isin);
	    }
	    if (product == null) {
		System.out.println("Produto  " + isin + " nao encontrado.");
	    } else {
		DSConnection.getDefault().getRemoteProduct().removeProduct(product);
	    }

	}
    }
}
