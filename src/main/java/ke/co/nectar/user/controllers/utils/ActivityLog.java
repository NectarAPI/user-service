package ke.co.nectar.user.controllers.utils;

public class ActivityLog {

    private String category;
    private String description;

    public ActivityLog(String category, String description) {
        setCategory(category);
        setDescription(description);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
