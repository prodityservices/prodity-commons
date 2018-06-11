
package io.prodity.commons.spigot.legacy.gui.anvil.click;

/**
 * Handles clicks in an inventory gui.
 * <p>
 * Created on Jan 18, 2017.
 *
 * @author FakeNeth
 */
public interface GUIClickHandler {

    /**
     * Handles a {@link GUIClickEvent}.
     *
     * @param event The {@link GUIClickEvent}.
     */
    void handleClick(GUIClickEvent event);

}
