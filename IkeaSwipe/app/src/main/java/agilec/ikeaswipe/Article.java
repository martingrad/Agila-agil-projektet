package agilec.ikeaswipe;

/**
 * Created by Ingelhag on 14/04/15.
 */
public class Article {

    private String title;
    private String articleNumber;
    private int quantity;
    private int quantityLeft;
    private String imgUrl;
    private int[] steps;

    public Article(String title, String articleNumber, int quantity, int quantityLeft, String imgUrl, int[] steps) {
        this.title = title;
        this.articleNumber = articleNumber;
        this.quantity = quantity;
        this.quantityLeft = quantityLeft;
        this.imgUrl = imgUrl;
        this.steps = steps;
    }

    public String getTitle() {
        return title;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantityLeft() {
        return quantityLeft;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int[] getSteps() {
        return steps;
    }
}
