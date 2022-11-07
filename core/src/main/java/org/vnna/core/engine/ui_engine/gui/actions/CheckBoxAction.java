package org.vnna.core.engine.ui_engine.gui.actions;

public interface CheckBoxAction {

    default void onCheck(boolean checked){return;};

    default void onMouseClick(int mouseButton){
        return;
    }

    default void onMouseDoubleClick(int button){
        return;
    }

    default void onMouseScroll(float scrolled){return;}
}
