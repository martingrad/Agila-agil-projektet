package agilec.ikeaswipe;

/**
 * Created by Ingelhag on 05/05/15.
 */
public class Step {

  private int step;
  private String title;
  private String imgUrl;
  private String checkbarButtonUrl;
  private String completeModelUrl;
  private boolean checked;
  private boolean currentStep;

  public Step(int step, String title, String imgUrl, String checkbarButtonUrl, String completeModelUrl, boolean checked, boolean currentStep) {
    this.step = step;
    this.title = title;
    this.imgUrl = imgUrl;
    this.checkbarButtonUrl = checkbarButtonUrl;
    this.completeModelUrl = completeModelUrl;
    this.checked = checked;
    this.currentStep = currentStep;
  }

  public int getStep() {
    return step;
  }

  public String getTitle() {
    return title;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public String getCheckbarButtonUrl() {
    return checkbarButtonUrl;
  }

  public String getCompleteModelUrl() {
    return completeModelUrl;
  }

  public boolean getChecked() {
    return checked;
  }

  public boolean getCurrentStep() {
    return currentStep;
  }

  public void setCurrentStep(boolean currentStep) {
    this.currentStep = currentStep;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }

}
