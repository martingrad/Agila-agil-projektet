package agilec.ikeaswipe;

/**
 * An Article is one part. It could be a dowel, a screw or a allen key
 * Created by Ingelhag on 14/04/15.
 * @user @ingelhag
 */
public class Article {

    private String title;           // Title of the article
    private String articleNumber;   // Article number of the article
    private int quantity;           // Quantity from the beginning of the article
    private int quantityLeft;       // How many of this article remains
    private String imgUrl;          // The image url of the article
    private int[] steps;            // How many each step require of this article

    /**
     * Constructor
     * Set a new Article
     * @param title
     * @param articleNumber
     * @param quantity
     * @param quantityLeft
     * @param imgUrl
     * @param steps
     */
    public Article(String title, String articleNumber, int quantity, int quantityLeft, String imgUrl, int[] steps) {
        this.title = title;
        this.articleNumber = articleNumber;
        this.quantity = quantity;
        this.quantityLeft = quantityLeft;
        this.imgUrl = imgUrl;
        this.steps = steps;
    }

    /**
     * Returns the title of the Article
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the article number of the article
     * @return articleNumber
     */
    public String getArticleNumber() {
        return articleNumber;
    }

    /**
     * Returns the quantity from the beginning of the item
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns how many of this article remains
     * @return quantityLeft
     */
    public int getQuantityLeft() {
        return quantityLeft;
    }

    /**
     * Return the img url
     * @return imgUrl
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * Returns an array with information about how many of this article is required for each step
     * @return steps
     */
    public int[] getSteps() {
        return steps;
    }
}
