package mds.ufscar.pergunte;

/**
 * Created by Danilo on 19/01/2017.
 */

public class Section implements ListItem {

    private final String mTitle;

    public Section(String title) {
        this.mTitle = title;
    }

    public String getTitle(){
        return mTitle;
    }

    @Override
    public boolean isSection() {
        return true;
    }
}
