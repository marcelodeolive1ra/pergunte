package mds.ufscar.pergunte;

/**
 * Created by Danilo on 19/01/2017.
 */

public class MateriaSection implements MateriaItem {

    private final String mTitle;

    public MateriaSection(String title) {
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
