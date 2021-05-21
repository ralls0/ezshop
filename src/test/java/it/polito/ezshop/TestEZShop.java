package it.polito.ezshop;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TestEZShop {

    EZShopInterface ezShop;

    @Before
    public void setUp() {
        ezShop = new it.polito.ezshop.data.EZShop();
        // try {
        //     ezShop.createUser("Marco", "CppSpaccaMaNoiUsiamoJava", "Administrator");
        //     ezShop.login("Marco", "CppSpaccaMaNoiUsiamoJava");
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }

    @After
    public void tearDown() {
        ezShop.reset();
        ezShop = null;
    }

    @Test
    public void testCreateUser() {

        assertThrows(it.polito.ezshop.exceptions.InvalidUsernameException.class, () -> {
            ezShop.createUser("",
                    "password", "Administrator");
        });

        // ------------------------------------------------------------- //

        assertThrows(it.polito.ezshop.exceptions.InvalidUsernameException.class, () -> {
            ezShop.createUser(null,
                    "password", "Administrator");
        });

        // ------------------------------------------------------------- //

        assertThrows(it.polito.ezshop.exceptions.InvalidPasswordException.class, () -> {
            ezShop.createUser("Giovanni",
                    "", "Administrator");
        });

        // ------------------------------------------------------------- //

        assertThrows(it.polito.ezshop.exceptions.InvalidPasswordException.class, () -> {
            ezShop.createUser("Giovanni",
                    null, "Administrator");
        });

        // ------------------------------------------------------------- //

        assertThrows(it.polito.ezshop.exceptions.InvalidRoleException.class, () -> {
            ezShop.createUser("Giovanni",
                    "password", "Cashier");
        });

        // ------------------------------------------------------------- //

        Integer id = -1;
        try {
            id = ezShop.createUser("Giovanni", "password", "Administrator");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(id == 1);

        // ------------------------------------------------------------- //

        try {
            id = ezShop.createUser("Giovanni", "password", "Administrator");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(id == -1);

    }

    @Test
    public void testDeleteUser() {

        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
            ezShop.deleteUser(1);
        });


        // ------------------------------------------------------------- //

        try {
            ezShop.createUser("Giovanni", "password", "Administrator");
            ezShop.login("Giovanni", "password");
            ezShop.createUser("Anna", "password", "Cashier");

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, () -> {
            ezShop.deleteUser(-1);
        });

        assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, () -> {
            ezShop.deleteUser(null);
        });

        // ------------------------------------------------------------- //

        try {
            ezShop.logout();
            ezShop.login("Anna", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
            ezShop.deleteUser(0);
        });

        // ------------------------------------------------------------- //

        boolean deleted = false;
        try {
            ezShop.logout();
            ezShop.login("Giovanni", "password");
            deleted = ezShop.deleteUser(2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(deleted);

    }

    @Test
    public void testGetAllUsers() {

        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
            ezShop.getAllUsers();
        });

        // ------------------------------------------------------------- //

        try {
            ezShop.createUser("Giovanni", "password", "Administrator");
            ezShop.login("Giovanni", "password");
            ezShop.createUser("Anna", "password", "Cashier");
            ezShop.logout();
            ezShop.login("Anna", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
            ezShop.getAllUsers();
        });

        // ------------------------------------------------------------- //


        List<User> users = null;

        try {
            ezShop.logout();
            ezShop.login("Giovanni", "password");
            users = ezShop.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(users);

    }

    @Test
    public void testGetUser() {

        try {
            ezShop.createUser("Giovanni", "password", "Administrator");
            ezShop.login("Giovanni", "password");
            ezShop.createUser("Anna", "password", "Cashier");

        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, () -> {
            ezShop.getUser(-1);
        });

        assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, () -> {
            ezShop.getUser(null);
        });

        // ------------------------------------------------------------- //

        try {
            ezShop.login("Anna", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
            ezShop.getUser(2);
        });

        // ------------------------------------------------------------- //

        User user = null;
        try {
            ezShop.logout();
            ezShop.login("Giovanni", "password");
            user = ezShop.getUser(2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(user);

    }

    @Test
    public void testUpdateUserRights() {

        try {
            ezShop.createUser("Giovanni", "password", "Administrator");
            ezShop.login("Giovanni", "password");
            ezShop.createUser("Anna", "password", "Cashier");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, () -> {
            ezShop.updateUserRights(-1, "ShopManager");
        });

        assertThrows(it.polito.ezshop.exceptions.InvalidUserIdException.class, () -> {
            ezShop.updateUserRights(null, "ShopManager");
        });

        assertThrows(it.polito.ezshop.exceptions.InvalidRoleException.class, () -> {
            ezShop.updateUserRights(1, null);
        });

        assertThrows(it.polito.ezshop.exceptions.InvalidRoleException.class, () -> {
            ezShop.updateUserRights(1, "Ruolo");
        });


        // ------------------------------------------------------------- //

        try {
            ezShop.login("Anna", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
            ezShop.updateUserRights(2, null);
        });

        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
            ezShop.updateUserRights(2, "ShopManager");
        });

        // ------------------------------------------------------------- //

        boolean modified = false;
        try {
            ezShop.login("Giovanni", "password");
            modified = ezShop.updateUserRights(2, "ShopManager");
            assertTrue(modified);
            modified = false;
            modified = ezShop.updateUserRights(3, "ShopManager");
            assertFalse(modified);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogin() {
        assertThrows(it.polito.ezshop.exceptions.InvalidUsernameException.class, () -> {
            ezShop.login("",
                    "password");
        });

        // ------------------------------------------------------------- //

        assertThrows(it.polito.ezshop.exceptions.InvalidUsernameException.class, () -> {
            ezShop.login(null,
                    "password");
        });

        // ------------------------------------------------------------- //

        assertThrows(it.polito.ezshop.exceptions.InvalidPasswordException.class, () -> {
            ezShop.login("Giovanni",
                    "");
        });

        // ------------------------------------------------------------- //

        assertThrows(it.polito.ezshop.exceptions.InvalidPasswordException.class, () -> {
            ezShop.createUser("Giovanni",
                    null, "Administrator");
        });

        // ------------------------------------------------------------- //

        User user = null;

        try {
            ezShop.createUser("Giovanni", "password", "Administrator");
            user = ezShop.login("Giovanni", "password");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(user);


    }

    @Test
    public void testLogout() {
        boolean logged_out = false;
        logged_out = ezShop.logout();
        assertTrue(logged_out);
    }


    @Test
    public void createProductType() {
    }

    @Test
    public void updateProduct() {
    }

    @Test
    public void deleteProductType() {
    }

    @Test
    public void getAllProductTypes() {
    }

    @Test
    public void getProductTypeByBarCode() {
    }

    @Test
    public void getProductTypesByDescription() {
    }

    @Test
    public void updateQuantity() {
    }

    @Test
    public void updatePosition() {
    }

    @Test
    public void defineCustomer() {
    }

    @Test
    public void modifyCustomer() {
    }

    @Test
    public void deleteCustomer() {
    }

    @Test
    public void getCustomer() {
    }

    @Test
    public void getAllCustomers() {
    }

    @Test
    public void createCard() {
    }

    @Test
    public void attachCardToCustomer() {
    }

    @Test
    public void modifyPointsOnCard() {
    }


    @Test
    public void testStartSaleTransaction() {

        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
            ezShop.startSaleTransaction();
        });

        try {
            ezShop.createUser("Marco", "CppSpaccaMaNoiUsiamoJava", "Administrator");
            ezShop.login("Marco", "CppSpaccaMaNoiUsiamoJava");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer id = -1;
        try {
            id = ezShop.startSaleTransaction();
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        }
        assertTrue("StartSaleTransaction mismatch", id >= 0);
    }

    @Test
    public void testAddProductToSale() {

        // start saleTransaction
        try {
            ezShop.createUser("Marco", "CppSpaccaMaNoiUsiamoJava", "Administrator");
            ezShop.login("Marco", "CppSpaccaMaNoiUsiamoJava");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create Product
        String productCode = "3000000000076";

        try {
            ezShop.createProductType("Product test", productCode, 12.0, "None");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create SaleTransaction
        Integer transactionId = -1;

        try {
            transactionId = ezShop.startSaleTransaction();
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        }
        assertTrue("StartSaleTransaction mismatch", transactionId >= 0);

        final Integer tempId = transactionId;

        // test UnauthorizedException
        ezShop.logout();
        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
            ezShop.addProductToSale(tempId, productCode, 12);
        });

        try {
            ezShop.login("Marco", "CppSpaccaMaNoiUsiamoJava");
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // test InvalidTransactionIdException
        assertThrows(it.polito.ezshop.exceptions.InvalidTransactionIdException.class, () -> {
            ezShop.addProductToSale(null, productCode, 12);
        });

        // test InvalidProductCodeException
        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, () -> {
            ezShop.addProductToSale(tempId, null, 12);
        });

        // test transactionId not valid
        boolean resultAddProductToSale = true;
        try {
            resultAddProductToSale = ezShop.addProductToSale(10, productCode, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue("AddProductToSale mismatch", !resultAddProductToSale);

        // test transactionId valid
        resultAddProductToSale = false;
        try {
            resultAddProductToSale = ezShop.addProductToSale(tempId, productCode, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue("AddProductToSale mismatch", !resultAddProductToSale);
    }

    @Test
    public void testDeleteProductFromSale() {

        // start saleTransaction
        try {
            ezShop.createUser("Marco", "CppSpaccaMaNoiUsiamoJava", "Administrator");
            ezShop.login("Marco", "CppSpaccaMaNoiUsiamoJava");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create Product
        String productCode = "3000000000076";

        try {
            ezShop.createProductType("Product test", productCode, 12.0, "None");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create SaleTransaction
        Integer transactionId = -1;

        try {
            transactionId = ezShop.startSaleTransaction();
        } catch (UnauthorizedException e) {
            e.printStackTrace();
        }
        assertTrue("StartSaleTransaction mismatch", transactionId >= 0);

        final Integer tempId = transactionId;

        try {
            ezShop.addProductToSale(tempId, productCode, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // test UnauthorizedException
        ezShop.logout();
        assertThrows(it.polito.ezshop.exceptions.UnauthorizedException.class, () -> {
            ezShop.deleteProductFromSale(tempId, productCode, 12);
        });

        try {
            ezShop.login("Marco", "CppSpaccaMaNoiUsiamoJava");
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // test InvalidTransactionIdException
        assertThrows(it.polito.ezshop.exceptions.InvalidTransactionIdException.class, () -> {
            ezShop.deleteProductFromSale(null, productCode, 12);
        });

        // test InvalidProductCodeException
        assertThrows(it.polito.ezshop.exceptions.InvalidProductCodeException.class, () -> {
            ezShop.deleteProductFromSale(tempId, null, 12);
        });

        // test transactionId not valid
        boolean resulDteleteProductFromSale = true;
        try {
            resulDteleteProductFromSale = ezShop.deleteProductFromSale(10, productCode, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue("DeleteProduct mismatch", !resulDteleteProductFromSale);

        resulDteleteProductFromSale = false;
        try {
            resulDteleteProductFromSale = ezShop.deleteProductFromSale(tempId, productCode, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue("DeleteProduct mismatch", !resulDteleteProductFromSale);
    }

}