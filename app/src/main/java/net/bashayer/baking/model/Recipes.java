package net.bashayer.baking.model;

import java.io.Serializable;
import java.util.List;

public class Recipes implements Serializable {
    private List<Recipe> response;

    public List<Recipe> getResponse() {
        return response;
    }

    public void setResponse(List<Recipe> response) {
        this.response = response;
    }
}
