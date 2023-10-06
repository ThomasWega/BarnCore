package com.bof.barn.core.gui.premade.menu.harvestable;

import com.bof.barn.core.gui.premade.button.page.NextPageButton;
import com.bof.barn.core.gui.premade.button.page.PreviousPageButton;
import com.github.stefvanschie.inventoryframework.adventuresupport.TextHolder;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.jetbrains.annotations.NotNull;

/**
 * Premade gui with pagination. Handles the removal of navigation buttons on first and last page
 */
public abstract class PaginatedNavGUI extends ChestGui {
    public final StaticPane navPane;
    public final PaginatedPane paginatedPane;
    public final NextPageButton nextPageItem;
    public final PreviousPageButton previousPageItem;

    public PaginatedNavGUI(int rows, @NotNull TextHolder title, int x, int y, int length, int height) {
        super(rows, title);
        this.navPane = new StaticPane(0, (this.getRows() - 1), 9, 1);
        this.paginatedPane = new PaginatedPane(x, y, length, height);
        this.previousPageItem = new PreviousPageButton();
        this.nextPageItem = new NextPageButton();

        this.previousPageItem.setAction(event -> {
            this.paginatedPane.setPage(paginatedPane.getPage() - 1);
            this.handleFirstLastPages();
            this.update();
        });

        this.nextPageItem.setAction(event -> {
            this.paginatedPane.setPage(paginatedPane.getPage() + 1);
            this.handleFirstLastPages();
            this.update();
        });

        this.navPane.addItem(this.previousPageItem, 0, 0);
        this.navPane.addItem(this.nextPageItem, 8, 0);

        this.addPane(this.navPane);
        this.addPane(this.paginatedPane);

        this.handleFirstLastPages();
        this.update();
    }

    private void handleFirstLastPages() {
        int pagesCount = this.paginatedPane.getPages() - 1;
        // if no pages are present, it will display 0, so it needs to also be checked for -1
        this.nextPageItem.setVisible(this.paginatedPane.getPage() != pagesCount && pagesCount != -1);
        this.previousPageItem.setVisible(this.paginatedPane.getPage() != 0);
    }

    @Override
    public void update() {
        this.handleFirstLastPages();
        super.update();
    }
}
