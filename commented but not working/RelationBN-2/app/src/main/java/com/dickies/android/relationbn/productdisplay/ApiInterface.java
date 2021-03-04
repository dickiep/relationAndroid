package com.dickies.android.relationbn.productdisplay;

import com.dickies.android.relationbn.utils.Product;
import com.dickies.android.relationbn.utils.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface that allows interation with the database based on user input
 */

public interface ApiInterface {

    /**
     * Gets the products when a user enters text in the search field
     * @param keyword
     * @return
     */
    @GET("getproducts.php")
    Call<ArrayList<Product>> getProducts (@Query("key") String keyword);

    @GET("getuser.php")
    Call<ArrayList<User>> getUser (@Query("key") String keyword);

    /**
     * Gets the items a user has entered in their cart
     * @param userID
     * @return
     */
    @GET("getcart.php")
    Call<ArrayList<Product>> getCart(@Query("userID") int userID);

    @GET("getCartEmail.php")
    Call<ArrayList<Product>> getCartEmail(@Query("userEmail") String  userEmail);

    /**
     * Gets the products that relate to a category when a user searches by category
     * @param key
     * @return
     */
    @GET("categorysearch.php")
    Call<ArrayList<Product>> getProductsByCategory(@Query("key") int key);

    /**
     * Writes to the database when a user want to update a product
     * @param key
     * @return
     */
    @GET("productupdate.php")
    Call<Product> updateProduct(@Query("key") int key);
}
