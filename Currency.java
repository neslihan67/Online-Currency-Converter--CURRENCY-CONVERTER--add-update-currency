package sample;

public interface Currency {
    /**
     *
     * @return String current Date
     */
    String getDate();
    /**
     *
     * @return String Money Name
     */
    String getName();
    /**
     *
     * @return float Buying Price
     */
    float getBuyingPrice();
    /**
     *
     * @return float Selling Price
     */
    float getSellingPrice();
    /**
     *
     * @return boolean is Forex
     */
    boolean isForex();


}

