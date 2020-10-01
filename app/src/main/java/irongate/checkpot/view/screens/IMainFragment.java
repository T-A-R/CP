package irongate.checkpot.view.screens;

public interface IMainFragment {
    void showScreensaver(String title, boolean full);
    void hideScreensaver();
    void showMenu();
    void showDrawer();
    void hideMenu();
    void setMenuCursor(int index);
}
